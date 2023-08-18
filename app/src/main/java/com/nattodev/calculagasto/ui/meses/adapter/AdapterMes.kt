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
import com.nattodev.calculagasto.ui.meses.model.MesesUsuario

class AdapterMes(val context: Context, val listaMeses: ArrayList<MesesUsuario>): RecyclerView.Adapter<AdapterMes.MesViewHolder>() {

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
        val mes: MesesUsuario = listaMeses[position]
        val primeira = mes.key?.first()?.uppercase()

        holder.mes.text = "$primeira${mes.key?.substring(1)}"
        holder.valorTotalMes.text = "Total: R$${mes.value.toString()}"

        holder.btnEditarMes.setOnClickListener {

            val intent = Intent(context, MesSelecionadoActivity::class.java)
            intent.putExtra("mesSelecionado", mes.key)
            context.startActivity(intent)

        }
    }

}