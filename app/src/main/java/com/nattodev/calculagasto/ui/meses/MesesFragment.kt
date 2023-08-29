package com.nattodev.calculagasto.ui.meses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.nattodev.calculagasto.databinding.FragmentMesesBinding
import com.nattodev.calculagasto.formataFloat
import com.nattodev.calculagasto.toastErro
import com.nattodev.calculagasto.ui.meses.adapter.AdapterMes
import com.nattodev.calculagasto.ui.meses.model.Gasto
import com.nattodev.calculagasto.ui.meses.model.MesesUsuario

class MesesFragment : Fragment() {

    private var _binding: FragmentMesesBinding? = null
    private lateinit var recyclerView : RecyclerView
    private lateinit var mesesArrayList: ArrayList<MesesUsuario>
    private var db = FirebaseFirestore.getInstance()
    private val userConectado = Firebase.auth.currentUser?.email
    private val gastostotaisList = mutableListOf<Float>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMesesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.listaMeses
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        mesesArrayList = arrayListOf()

        carregaDados()
        updateValorTotal()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun carregaDados() {
        db.collection("/Usuarios/${userConectado}/MesesAno").get().addOnSuccessListener {
            if(!it.isEmpty) {
                for(data in it.documents) {
                    val mes: MesesUsuario? = data.toObject(MesesUsuario::class.java)
                    if(mes != null) {
                        mesesArrayList.add(mes)
                    } else {
                        toastErro("não encontrado", requireContext())
                    }
                }
                recyclerView.adapter = AdapterMes(requireContext(), mesesArrayList)
            }
        }.addOnFailureListener {
            toastErro("Algo deu errado", requireContext())
        }
    }

    fun updateValorTotal() {
        db.collection("Usuarios/${userConectado}/MesesAno").get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    for (data in it.documents) {
                        val valor = data.getDouble("value")
                        if (valor != null) {
                            gastostotaisList.add(valor.toFloat())
                        }
                    }
                    val valorTotal = gastostotaisList.sum()
                    binding.valorTotalAno.text = "R$ ${formataFloat(valorTotal.toString())}"
                    db.document("/Usuarios/${userConectado}/")
                        .update(mapOf("valorTotal" to valorTotal))
                }
            }
    }
}