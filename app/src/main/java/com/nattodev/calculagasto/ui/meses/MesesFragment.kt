package com.nattodev.calculagasto.ui.meses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nattodev.calculagasto.databinding.FragmentMesesBinding
import com.nattodev.calculagasto.ui.meses.adapter.AdapterMes
import com.nattodev.calculagasto.ui.meses.model.Mes

class MesesFragment : Fragment() {

    private var _binding: FragmentMesesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val mesesViewModel =
        //ViewModelProvider(this).get(MesesViewModel::class.java)

        _binding = FragmentMesesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val listaMeses = binding.listaMeses
        listaMeses.layoutManager = LinearLayoutManager(requireContext())
        listaMeses.setHasFixedSize(true)




        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}