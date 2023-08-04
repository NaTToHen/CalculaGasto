package com.nattodev.calculagasto.ui.meses.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.nattodev.calculagasto.R
import com.nattodev.calculagasto.ui.meses.model.MesesUsuario

class AdapterMes(private val context: Context, private val listaMeses: ArrayList<MesesUsuario>): RecyclerView.Adapter<AdapterMes.MesViewHolder>() {

    private val db = FirebaseFirestore.getInstance()

    class MesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val mes = itemView.findViewById<TextView>(R.id.mesDoAno)
        val valorTotalMes = itemView.findViewById<TextView>(R.id.valorTotalMes)
        val btnEditarMes = itemView.findViewById<Button>(R.id.btnEditarMes)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MesViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_lista_meses, parent, false)
        return MesViewHolder(itemView)
    }

    override fun getItemCount(): Int = listaMeses.size

    override fun onBindViewHolder(holder: MesViewHolder, position: Int) {
        holder.mes.text = listaMeses[position].mes
        holder.valorTotalMes.text = listaMeses[position].valorTotalDoMes.toString()
        holder.btnEditarMes.setOnClickListener {

        }
    }

}