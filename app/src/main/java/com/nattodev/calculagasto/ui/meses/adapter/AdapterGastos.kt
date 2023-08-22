package com.nattodev.calculagasto.ui.meses.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.nattodev.calculagasto.R
import com.nattodev.calculagasto.ui.meses.gastos.MesSelecionadoActivity
import com.nattodev.calculagasto.ui.meses.model.Gasto
import com.nattodev.calculagasto.ui.meses.model.MesesUsuario

class AdapterGasto(val context: Context, var listaGasto: ArrayList<Gasto>): RecyclerView.Adapter<AdapterGasto.GastoViewHolder>() {

    private val db = FirebaseFirestore.getInstance()

    class GastoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nomeGasto = itemView.findViewById<TextView>(R.id.nomeGasto)
        val valorGasto = itemView.findViewById<TextView>(R.id.valorGasto)
        val btnExcluir = itemView.findViewById<Button>(R.id.btnExcluirGasto)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GastoViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_lista_gasto, parent, false)
        return GastoViewHolder(itemView)
    }

    override fun getItemCount(): Int = listaGasto.size

    override fun onBindViewHolder(holder: GastoViewHolder, position: Int) {
        val gastoDocumento: Gasto = listaGasto[position]

        holder.nomeGasto.text = gastoDocumento.descricao
        holder.valorGasto.text = "Total: R$${gastoDocumento.valor}"
        holder.btnExcluir.setOnClickListener {

        }
    }

}