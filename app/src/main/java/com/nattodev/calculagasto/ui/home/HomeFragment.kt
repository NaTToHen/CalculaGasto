package com.nattodev.calculagasto.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.nattodev.calculagasto.R
import com.nattodev.calculagasto.databinding.FragmentHomeBinding
import com.nattodev.calculagasto.loginCadastro.LoginActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val db = FirebaseFirestore.getInstance()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {

            mostraDadosUsuario()

            val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
            _binding = FragmentHomeBinding.inflate(inflater, container, false)
            val root: View = binding.root

        val barSet = mutableListOf<Pair<String,Float>>(
            "JAN" to 0F,
            "FEV" to 2F,
            "MAR" to 0F,
            "MAI" to 3F,
            "ABR" to 6F,
            "JUN" to 0F,
            "JUL" to 5F,
            "AGO" to 0F,
            "SET" to 2F,
            "OUT" to 0F,
            "NOV" to 1F,
            "DEZ" to 4F
        )
        val animationDuration = 1000L
        /*for(mes in barSet) {
            mes.second
        }*/

        binding.apply {
            barChartMeses.animation.duration = animationDuration
            barChartMeses.animate(barSet)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun mostraDadosUsuario() {

        val userConectado = Firebase.auth.currentUser
        userConectado?.let {
            // Name, email address, and profile photo Url
            val email = it.email

            db.collection("Usuarios").document(email.toString()).get()
                .addOnCompleteListener { documento ->
                    if (documento.isSuccessful) {
                        val nome = documento.result.get("nome").toString()
                        val valorTotal = documento.result.get("valorTotal").toString()
                        binding.nomeUsuario.text = nome
                        binding.totalGasto.text = "Total gasto: R$ $valorTotal"
                    }
                }
        }
    }
}