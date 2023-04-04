package com.example.listcontacts.ui.list

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.listcontacts.R
import com.example.listcontacts.dao.ContactDao
import com.example.listcontacts.database.FirebaseDao.Companion.getIdUser
import com.example.listcontacts.databinding.FragmentAddContactBinding
import com.example.listcontacts.databinding.FragmentListBinding
import com.example.listcontacts.model.Contact
import com.example.listcontacts.ui.auth.RegisterFragment
import com.example.listcontacts.utils.Utils
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class AddContactFragment : Fragment(), View.OnClickListener{

    private var _binding: FragmentAddContactBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialog: AlertDialog

    //Img
    private var bitmap: Bitmap? = null

    companion object {
        private val PERMISSION_GALLERY = Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private val requestGallery =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission ->
            when {
                permission -> {
                    resultGallery.launch(
                        Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                    )
                }
                else -> {
                    showDialogPermisson()
                }
            }

        }

    private val resultGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.data?.data != null) {
                bitmap = if (Build.VERSION.SDK_INT < 20) {
                    MediaStore.Images.Media.getBitmap(
                        requireActivity().baseContext.contentResolver,
                        result.data?.data
                    )
                } else {
                    val source = ImageDecoder.createSource(
                        requireContext().contentResolver,
                        result.data?.data!!
                    )
                    ImageDecoder.decodeBitmap(source)
                }
                binding.imgContact.setImageBitmap(bitmap)
            }

        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddContactBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener(this)
        binding.btnSaveContact.setOnClickListener(this)
        binding.imgContact.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.btnSaveContact -> {
                try {
                    binding.loading.isVisible = true
                    val dao = ContactDao()
                    dao.addContact(Contact(
                        name = binding.edtName.text.toString().trim(),
                        numberPhone = binding.edtNumberPhone.text.toString().trim(),
                        userId = getIdUser().toString()
                    ), bitmap = bitmap,
                    onSuccess = {
                        binding.loading.isVisible = false
                        Utils.showToast(requireContext(),"Sucesso")
                    },
                        onFailure = {exception ->
                            binding.loading.isVisible = false
                            Utils.showToast(requireContext(),"Error ${exception.message}")
                        }
                    )
                }catch (arg: IllegalArgumentException) {
                    Utils.showToast(requireContext(),"Preecha todos os campos")
                }
            }
            R.id.imgContact -> checkPermissionGallery()
            R.id.btnBack -> {
                findNavController().navigate(R.id.action_addContactFragment_to_listContactsFragment)
            }
        }
    }

    private fun checkPermissionGallery() {
        val permissionGallery = checkPermission(PERMISSION_GALLERY)

        when {
            permissionGallery -> {
                resultGallery.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                )
            }
            shouldShowRequestPermissionRationale(PERMISSION_GALLERY) -> showDialogPermisson()

            else -> requestGallery.launch(PERMISSION_GALLERY)
        }
    }

    private fun showDialogPermisson() {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Atenção")
            .setMessage("Precisamos do acesso a galeria do seu dispositivo, deseja permitir agora ?")
            .setPositiveButton("Sim") { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", requireActivity().packageName, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                dialog.dismiss()
            }.setNegativeButton("Não") { _, _ ->
                dialog.dismiss()
            }
        dialog = builder.create()
        dialog.show()

    }

    private fun checkPermission(permission: String) =
        ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED




    override fun onDestroy() {
        super.onDestroy()
    }



}