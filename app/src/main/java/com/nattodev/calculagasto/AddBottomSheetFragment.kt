package com.nattodev.calculagasto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.nattodev.calculagasto.databinding.ActivityMesSelecionadoBinding
import com.nattodev.calculagasto.databinding.FragmentAddBottomSheetBinding
import com.nattodev.calculagasto.ui.meses.gastos.MesSelecionadoActivity
import java.text.DecimalFormat

class AddBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddBottomSheetBinding
    private var db = FirebaseFirestore.getInstance()
    private val userConectado = Firebase.auth.currentUser?.email
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = requireActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    /*fun abreBottomDialog() {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.fragment_add_bottom_sheet)
        dialog.show()

        val btnAddGastoBottom = binding.btnAddGastoBottom

        btnAddGastoBottom.setOnClickListener {

            val descGasto = binding.nomeGasto.text.toString()
            val valorGasto = binding.valorGasto.text.toString().toFloat()

            if(descGasto.isNotEmpty() || valorGasto.toString().isNotEmpty()) {
                val decimalFormat = DecimalFormat("#.00")
                val valorFormatado = decimalFormat.format(valorGasto)
                val mesSelecionado = activity?.intent?.getStringExtra("mesSelecionado")

                db.document("Usuarios/${userConectado}/MesesAno/${mesSelecionado}/gastoMes/gastoMes")
                    .update(mapOf(descGasto to valorFormatado)).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            dialog.dismiss()
                        } else {
                            Toast.makeText(requireContext(), "algo deu errado", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }*/
}