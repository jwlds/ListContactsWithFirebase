package com.example.listcontacts.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.listcontacts.R
import com.example.listcontacts.database.FirebaseDao.Companion.getAuth
import com.example.listcontacts.databinding.FragmentLoginBinding
import com.example.listcontacts.utils.Utils
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment(), View.OnClickListener{

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        binding.btnRecover.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.btnLogin -> {
                try {
                    binding.loading.isVisible = true
                    login(binding.edtEmail.text.toString().trim(),
                        binding.edtPassword.text.toString().trim())
                } catch (arg: IllegalArgumentException)
                {
                    Utils.showToast(requireContext(),"Preecha todos os dados!")
                }
            }
            R.id.btnRegister -> {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            R.id.btnRecover -> {
                findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
            }

        }


    }

    private fun login(email: String,password: String){
        getAuth().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    binding.loading.isVisible = false
                    Utils.showToast(requireContext(),"Login realizado com sucesso!")
                    findNavController().navigate(R.id.action_global_listContactsFragment2)
                }
            }.addOnFailureListener { exeception ->
                val messageError = when (exeception) {
                    is FirebaseNetworkException -> "Falha na conexao com a internet"
                    is FirebaseAuthInvalidCredentialsException -> "Login ou Senha estão invalidos"
                    else -> "ERRO N SEI"
                }
                binding.loading.isVisible = false
                Utils.showToast(requireContext(),messageError)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}