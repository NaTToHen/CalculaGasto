package com.nattodev.calculagasto.ui.meses.gastos

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.nattodev.calculagasto.MainActivity
import com.nattodev.calculagasto.R
import com.nattodev.calculagasto.databinding.ActivityMesSelecionadoBinding
import com.nattodev.calculagasto.ui.loginCadastro.LoginActivity
import com.nattodev.calculagasto.ui.meses.adapter.AdapterGasto
import com.nattodev.calculagasto.ui.meses.adapter.AdapterMes
import com.nattodev.calculagasto.ui.meses.model.Gasto
import com.nattodev.calculagasto.ui.meses.model.MesesUsuario
import java.text.DecimalFormat
import java.util.UUID

class MesSelecionadoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMesSelecionadoBinding
    private var db = FirebaseFirestore.getInstance()
    private val userConectado = Firebase.auth.currentUser?.email
    private lateinit var recyclerView : RecyclerView
    private lateinit var mutableListGastos: ArrayList<Gasto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMesSelecionadoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val logoTopo = findViewById<ImageView>(R.id.logoTopo)
        val btnSair = findViewById<ImageView>(R.id.btnSair)
        val mesSelecionado = intent.getStringExtra("mesSelecionado")

        Toast.makeText(this, "$mesSelecionado", Toast.LENGTH_SHORT).show()

        btnVoltar.setOnClickListener {
            finish()
        }
        btnSair.setOnClickListener {
            exitDialog()
        }
        logoTopo.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.addGasto.setOnClickListener{
            abreBottomSheet()
        }

        recyclerView = findViewById(R.id.listaGastosMes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        db.collection("/Usuarios/${userConectado}/MesesAno/${mesSelecionado}/gastos").get()
            .addOnSuccessListener {
                if(!it.isEmpty) {
                    for(data in it.documents) {
                        val mes: Gasto? = data.toObject(Gasto::class.java)
                        if(mes != null) {
                            mutableListGastos.add(mes)
                        } else {
                            Toast.makeText(this, "não encontrado", Toast.LENGTH_SHORT).show()
                        }
                    }
                    recyclerView.adapter = AdapterGasto(this, mutableListGastos)
                }
            }.addOnFailureListener {
                Toast.makeText(this, "algo deu errado", Toast.LENGTH_SHORT).show()
            }
    }

    fun exitDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.fragment_desconectar)
        val dialogButtonYes = dialog.findViewById<Button>(R.id.btn_deslogarConfirm)
        val dialogButtonNo = dialog.findViewById<Button>(R.id.btn_deslogarCancelar)

        dialogButtonNo.setOnClickListener {
            dialog.dismiss()
        }
        dialogButtonYes.setOnClickListener {
            dialog.dismiss()
            Firebase.auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        dialog.show()
    }

    fun abreBottomSheet() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.fragment_add_bottom_sheet)
        dialog.show()

        val btnAddGastoBottom = dialog.findViewById<Button>(R.id.btnAddGastoBottom)

        btnAddGastoBottom?.setOnClickListener {

            val descGasto = dialog.findViewById<EditText>(R.id.nomeGasto)?.text.toString()
            val valorGasto = dialog.findViewById<EditText>(R.id.valorGasto)?.text.toString().toFloat()

            if(!descGasto.isEmpty() || !valorGasto.toString().isEmpty()) {
                val decimalFormat = DecimalFormat("#.00")
                val valorFormatado = decimalFormat.format(valorGasto)
                val mesSelecionado = intent.getStringExtra("mesSelecionado")
                val nomeAleatorio = UUID.randomUUID().toString()

                val dadosDocumento = hashMapOf(
                    "descricao" to descGasto,
                    "valor" to valorFormatado
                )

                db.collection("Usuarios/${userConectado}/MesesAno/${mesSelecionado}/gastos")
                    .document(nomeAleatorio).set(dadosDocumento).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            dialog.dismiss()
                        } else {
                            Toast.makeText(this, "algo deu errado", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}