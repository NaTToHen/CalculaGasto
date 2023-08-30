package com.nattodev.calculagasto.ui.meses.gastos

import GastoAdapter
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nattodev.calculagasto.MainActivity
import com.nattodev.calculagasto.R
import com.nattodev.calculagasto.classes.loadingDialog
import com.nattodev.calculagasto.databinding.ActivityMesSelecionadoBinding
import com.nattodev.calculagasto.formataFloat
import com.nattodev.calculagasto.formataNumeroGrande
import com.nattodev.calculagasto.toastErro
import com.nattodev.calculagasto.toastSucesso
import com.nattodev.calculagasto.ui.loginCadastro.LoginActivity
import com.nattodev.calculagasto.ui.meses.MesesFragment
import com.nattodev.calculagasto.ui.meses.model.Gasto

class MesSelecionadoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMesSelecionadoBinding
    private var db = Firebase.firestore
    private val userConectado = Firebase.auth.currentUser?.email
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemList: MutableList<Gasto>
    val valoresArray = mutableListOf<Float>()
    lateinit var loadingDialog: loadingDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMesSelecionadoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        loadingDialog = loadingDialog(this)
        loadingDialog.show()

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val logoTopo = findViewById<ImageView>(R.id.logoTopo)
        val btnSair = findViewById<ImageView>(R.id.btnSair)
        val mesSelecionado = intent.getStringExtra("mesSelecionado")
        binding.mesSelecionadoTv.text = mesSelecionado

        recyclerView = findViewById(R.id.listaGastosMes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        itemList = mutableListOf()
        carregarDadosDoFirestore()

        //botões do support bar
        btnVoltar.setOnClickListener {
            //updateVoltar.updateValorTotal()
            finish()
        }
        btnSair.setOnClickListener {
            exitDialog()
        }
        logoTopo.setOnClickListener {
            //updateVoltar.updateValorTotal()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.addGasto.setOnClickListener {
            abreBottomSheet()
        }
    }

    fun exitDialog() {
        db = FirebaseFirestore.getInstance()

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
        db = FirebaseFirestore.getInstance()

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.fragment_add_bottom_sheet)
        dialog.show()

        val btnAddGastoBottom = dialog.findViewById<Button>(R.id.btnAddGastoBottom)

        btnAddGastoBottom?.setOnClickListener {
            val descGasto = dialog.findViewById<EditText>(R.id.nomeGasto)?.text.toString()
            val valorGasto = dialog.findViewById<EditText>(R.id.valorGasto)?.text.toString()

            if(descGasto.isNotEmpty()) {
                if(valorGasto.isNotEmpty()) {
                    val valorFormatado = formataFloat(valorGasto)
                    val mesSelecionado = intent.getStringExtra("mesSelecionado")

                    val dadosDocumento = hashMapOf(
                        "descricao" to descGasto,
                        "valor" to valorFormatado
                    )

                    db.collection("Usuarios/${userConectado}/MesesAno/${mesSelecionado}/gastos")
                        .document(descGasto).set(dadosDocumento).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                dialog.dismiss()
                                carregarDadosDoFirestore()
                                toastSucesso("Cadastrado com sucesso", this)
                            } else if(task.isCanceled) {
                                Toast.makeText(this, "Algo deu errado", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                        }
                } else {
                   toastErro("Preencha o valor corretamente", this)
                }
            } else {
                toastErro("Preencha a descrição corretamente", this)
            }
        }
    }

    fun carregarDadosDoFirestore() {
        val db = FirebaseFirestore.getInstance()
        val mesSelecionado = intent.getStringExtra("mesSelecionado")

        db.collection("Usuarios/${userConectado}/MesesAno/${mesSelecionado}/gastos").get()
            .addOnSuccessListener {
                if(!it.isEmpty) {
                    valoresArray.clear()
                    itemList.clear()
                    for(data in it.documents) {
                        val gasto: Gasto? = data.toObject(Gasto::class.java)
                        val valor = data.getString("valor")

                        if(gasto != null) {
                            itemList.add(gasto)
                        }
                        if (valor != null) {
                            valoresArray.add(valor.toFloat())
                        }
                    }
                    val valorTotal = valoresArray.sum()
                    binding.valorTotalMesSelecionado.text = "R$ ${formataNumeroGrande(valorTotal)}"
                    db.document("Usuarios/${userConectado}/MesesAno/${mesSelecionado}")
                        .update(mapOf("value" to valorTotal))
                    recyclerView.adapter = GastoAdapter(this, itemList, mesSelecionado.toString())
                    loadingDialog.dismiss()
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Erro ao carregar dados: $exception")
                Toast.makeText(this, "erro ao carregar dados", Toast.LENGTH_SHORT).show()
            }
    }
}
