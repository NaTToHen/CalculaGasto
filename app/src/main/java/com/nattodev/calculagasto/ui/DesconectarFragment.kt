package com.nattodev.calculagasto.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nattodev.calculagasto.R
import com.nattodev.calculagasto.databinding.FragmentDesconectarBinding
import com.nattodev.calculagasto.databinding.FragmentHomeBinding
import com.nattodev.calculagasto.loginCadastro.LoginActivity

class DesconectarFragment : Fragment() {

    private var _binding: FragmentDesconectarBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDesconectarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnDeslogarConfirm.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent()
            startActivity(intent)
        }

        return root
    }
}