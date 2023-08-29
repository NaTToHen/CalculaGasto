package com.nattodev.calculagasto.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.collection.LLRBNode
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.nattodev.calculagasto.R
import com.nattodev.calculagasto.classes.loadingDialog
import com.nattodev.calculagasto.databinding.FragmentHomeBinding
import com.nattodev.calculagasto.formataNumeroGrande
import com.nattodev.calculagasto.toastErro
import com.nattodev.calculagasto.ui.meses.model.MesesUsuario

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val db = FirebaseFirestore.getInstance()
    lateinit var loadingDialog: loadingDialog
    private val userConectado = Firebase.auth.currentUser?.email

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        mostraDadosUsuario()
        loadingDialog = loadingDialog(requireContext())
        loadingDialog.show()

        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
            _binding = FragmentHomeBinding.inflate(inflater, container, false)
            val root: View = binding.root

        val barSet = mutableListOf<Pair<String, Float>>()

        db.collection("/Usuarios/${userConectado}/MesesAno").get().addOnSuccessListener {
            if(!it.isEmpty) {
                for(data in it.documents) {
                    val mes: MesesUsuario? = data.toObject(MesesUsuario::class.java)
                    if(mes != null) {
                        val mesKey = mes.key?.substring(0, 3)
                        val mesValue = mes.value
                        barSet.add((mesKey to mesValue) as Pair<String, Float>)
                    } else {
                        toastErro("não encontrado", requireContext())
                    }
                }
            }
            val animationDuration = 1000L
            binding.apply {
                barChartMeses.animation.duration = animationDuration
                barChartMeses.animate(barSet)
            }
        }.addOnFailureListener {
            toastErro("Algo deu errado", requireContext())
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
                        val valorTotal = documento.result.get("valorTotal").toString().toFloat()
                        val valorMaximo = documento.result.get("valorMaximo").toString().toFloat()

                        binding.nomeUsuario.text = nome
                        if(valorTotal > valorMaximo) {
                            binding.totalGasto.setTextColor(Color.RED)
                        }
                        binding.totalGasto.text = "Total gasto: R$ ${formataNumeroGrande(valorTotal)}"

                        loadingDialog.dismiss()
                    }
                }
        }
    }
}