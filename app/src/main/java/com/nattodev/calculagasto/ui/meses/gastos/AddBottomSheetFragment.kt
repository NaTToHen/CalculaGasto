package com.nattodev.calculagasto.ui.meses.gastos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.nattodev.calculagasto.R
import com.nattodev.calculagasto.databinding.FragmentAddBottomSheetBinding
import com.nattodev.calculagasto.databinding.FragmentMesesBinding

class AddBottomSheetFragment : Fragment() {
    private var _binding: FragmentAddBottomSheetBinding? = null
    private val binding get() = _binding!!
    private var db = FirebaseFirestore.getInstance()
    private val userConectado = Firebase.auth.currentUser?.email

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBottomSheetBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val descGasto = binding.nomeGasto.text.toString().trim()
        val valorGasto = binding.valorGasto.text.toString()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}