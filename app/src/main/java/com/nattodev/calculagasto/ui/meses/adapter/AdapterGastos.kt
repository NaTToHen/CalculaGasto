import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.nattodev.calculagasto.MainActivity
import com.nattodev.calculagasto.R
import com.nattodev.calculagasto.ui.meses.gastos.MesSelecionadoActivity
import com.nattodev.calculagasto.ui.meses.model.Gasto

class GastoAdapter(val context: Context, var listaItens: MutableList<Gasto>, val mesSelecionado:String) : RecyclerView.Adapter<GastoAdapter.ItemViewHolder>() {

    val db = FirebaseFirestore.getInstance()
    val userConectado = Firebase.auth.currentUser?.email

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gastoNome = itemView.findViewById<TextView>(R.id.gastoNome)
        val gastoValor = itemView.findViewById<TextView>(R.id.valorGasto)
        val btnExcluir = itemView.findViewById<Button>(R.id.btnExcluirGasto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_lista_gasto, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.gastoNome.text = listaItens[position].descricao
        holder.gastoValor.text = "R$ ${listaItens[position].valor}"

        holder.btnExcluir.setOnClickListener {
            db.document("Usuarios/${userConectado}/MesesAno/${mesSelecionado}/gastos/${listaItens[position].descricao}").delete().addOnSuccessListener {
                Toast.makeText(context, "Excluido com sucesso", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Erro ao excluir gasto", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun getItemCount(): Int {
        return listaItens.size
    }
}
