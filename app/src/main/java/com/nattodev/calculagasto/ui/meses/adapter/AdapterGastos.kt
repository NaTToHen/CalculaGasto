package com.nattodev.calculagasto.ui.meses.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.nattodev.calculagasto.R
import com.nattodev.calculagasto.ui.meses.model.Gasto
import com.nattodev.calculagasto.ui.meses.model.MesesUsuario

class AdapterGastos(val context: Context, val listaGastos: ArrayList<Gasto>): RecyclerView.Adapter<AdapterGastos.GastosViewHolder>() {

    private val db = FirebaseFirestore.getInstance()

    class GastosViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val gasto = itemView.findViewById<TextView>(R.id.nomeGasto)
        val valorGasto = itemView.findViewById<TextView>(R.id.valorGasto)
        val btnExcluirGasto = itemView.findViewById<Button>(R.id.btnExcluirGasto)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GastosViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_lista_gasto, parent, false)
        return GastosViewHolder(itemView)
    }

    override fun getItemCount(): Int = listaGastos.size

    override fun onBindViewHolder(holder: GastosViewHolder, position: Int) {
        val gastoPosition: Gasto = listaGastos[position]

        holder.gasto.text = gastoPosition.nomeGasto
        holder.valorGasto.text = "R$ ${gastoPosition.valorGasto.toString()}"
        holder.btnExcluirGasto.setOnClickListener {

        }

    }
}