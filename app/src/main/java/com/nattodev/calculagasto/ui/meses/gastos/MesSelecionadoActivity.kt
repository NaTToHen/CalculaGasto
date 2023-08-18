package com.nattodev.calculagasto.ui.meses.gastos

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.nattodev.calculagasto.MainActivity
import com.nattodev.calculagasto.R
import com.nattodev.calculagasto.databinding.ActivityMesSelecionadoBinding
import com.nattodev.calculagasto.databinding.FragmentAddBottomSheetBinding
import com.nattodev.calculagasto.loginCadastro.LoginActivity
import java.text.DecimalFormat

class MesSelecionadoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMesSelecionadoBinding
    private var db = FirebaseFirestore.getInstance()
    private val userConectado = Firebase.auth.currentUser?.email

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

        binding.addGasto.setOnClickListener {
            abreBottomDialog()
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

    fun abreBottomDialog() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.fragment_add_bottom_sheet)
        val btnAddGastoBottom = dialog.findViewById<Button>(R.id.btnAddGastoBottom)

        btnAddGastoBottom?.setOnClickListener { view ->
            val descGasto = dialog.findViewById<EditText>(R.id.nomeGasto)?.text.toString()
            val valorGasto = dialog.findViewById<EditText>(R.id.valorGasto)?.text.toString().toFloat()
            val decimalFormat = DecimalFormat("#.00")
            val valorFormatado = decimalFormat.format(valorGasto)

            val mesSelecionado = intent.getStringExtra("mesSelecionado")

            if(descGasto.isEmpty()) {
                val snackbar = Snackbar.make(view, "preencha todos os campos", Snackbar.LENGTH_LONG)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else {
                db.document("Usuarios/${userConectado}/MesesAno/${mesSelecionado}/gastoMes/gastoMes")
                    .update(mapOf(descGasto to valorFormatado)).addOnCompleteListener {
                        val snackbar = Snackbar.make(view, "Adicionado com sucesso", Snackbar.LENGTH_LONG)
                        snackbar.setBackgroundTint(Color.GREEN)
                        snackbar.show()

                        dialog.dismiss()
                    }.addOnFailureListener {
                        val snackbar = Snackbar.make(view, "Algo deu errado", Snackbar.LENGTH_LONG)
                        snackbar.setBackgroundTint(Color.RED)
                        snackbar.show()
                    }
            }
        }
        dialog.show()
    }
}