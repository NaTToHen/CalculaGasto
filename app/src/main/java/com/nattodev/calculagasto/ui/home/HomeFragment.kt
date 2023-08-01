package com.nattodev.calculagasto.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nattodev.calculagasto.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {
            val homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
            _binding = FragmentHomeBinding.inflate(inflater, container, false)
            val root: View = binding.root
            binding.apply {
                barChartMeses.animation.duration = animationDuration
                barChartMeses.animate(barSet)
            }
            return root
    }
    companion object {

        private val barSet = listOf(
            "JAN" to 4F,
            "FEV" to 8F,
            "MAR" to 2F,
            "MAI" to 2.3F,
            "ABR" to 5F,
            "JUN" to 4F,
            "JUL" to 4F,
            "AGO" to 7F,
            "SET" to 2F,
            "OUT" to 2.3F,
            "NOV" to 5F,
            "DEZ" to 4F
        )
        private const val animationDuration = 1000L
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}