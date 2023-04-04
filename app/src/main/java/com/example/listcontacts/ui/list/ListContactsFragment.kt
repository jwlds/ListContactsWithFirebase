package com.example.listcontacts.ui.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.listcontacts.R
import com.example.listcontacts.dao.ContactDao
import com.example.listcontacts.databinding.FragmentListBinding
import com.example.listcontacts.ui.recyclerview.adapter.ListContactsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ListContactsFragment : Fragment(),View.OnClickListener{

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return  binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolBar.btnLogout.setOnClickListener(this)
        binding.btnAddContact.setOnClickListener(this)
        binding.loading.isVisible = true
        listAdapter()



    }

    private fun listAdapter() {
        val dao = ContactDao()
        dao.getAll { contacts ->
            binding.loading.isVisible = false
            binding.listContacts.setHasFixedSize(true)
            binding.listContacts.adapter = ListContactsAdapter(requireContext(), contacts) { contact ->
                dao.deleteContact(contact,
                    onSuccess = {
                        listAdapter()
                    },
                    onFailure = { exception ->

                    }
                )
            }
        }
    }


    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.btnLogout -> signOut()
            R.id.btnAddContact -> {
                findNavController().navigate(R.id.action_listContactsFragment_to_addContactFragment)
            }
        }
    }

    private fun signOut() {
        auth.signOut()
        findNavController().navigate(R.id.action_listContactsFragment_to_navigation)
    }



    override fun onDestroyView() {
        super.onDestroyView()
    }

}