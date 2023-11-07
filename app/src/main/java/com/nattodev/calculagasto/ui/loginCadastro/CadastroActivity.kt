package com.nattodev.calculagasto.ui.loginCadastro

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.nattodev.calculagasto.MainActivity
import com.nattodev.calculagasto.databinding.ActivityCadastroBinding
import com.nattodev.calculagasto.toastErro
import com.nattodev.calculagasto.toastSucesso
import java.util.Calendar
import java.util.UUID

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        auth = Firebase.auth

        val textoPolitica = binding.textoPolitica
        textoPolitica.movementMethod = LinkMovementMethod.getInstance()

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnCadastrar.setOnClickListener { view ->
            val nome = binding.editNome.text.toString()
            val email = binding.editEmail.text.toString().trim()
            val valorMaximo = binding.editValorMaximo.text.toString()
            val senha = binding.editSenha.text.toString()
            val confirm = binding.editConfirmaSenha.text.toString()

            val calendario = Calendar.getInstance()
            val ano = calendario.get(Calendar.YEAR).toString()

            if( !email.isEmpty() || !senha.isEmpty() || !confirm.isEmpty() || !valorMaximo.isEmpty() || !nome.isEmpty()) {
                if(senha.length < 6) {
                    toastErro("A senha deve ter mais de 6 caracteres", this)
                } else {
                    if(senha == confirm) {
                        auth.createUserWithEmailAndPassword(email, senha)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success")
                                    val user = auth.currentUser

                                    salvaUsuario(nome, email, valorMaximo, senha, ano)
                                    updateUI(user)
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                    toastErro("Preencha todos os campos", this)

                                    binding.editSenha.setText("")
                                    binding.editConfirmaSenha.setText("")
                                    updateUI(null)
                                }
                            }
                    } else {
                        toastErro("As senhas precisam ser iguais", this)
                    }
                }
            } else {
                toastErro("Preencha todos os campos", this)
                binding.editSenha.setText("")
                binding.editConfirmaSenha.setText("")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun salvaUsuario(nome:String, email:String, valorMaximo:String, senha:String, ano:String) {
        val mapUsuarios = hashMapOf(
            "nome" to nome,
            "email" to email,
            "valorMaximo" to valorMaximo,
            "senha" to senha,
            "anoAtual" to ano,
            "valorTotal" to 0
        )

        val mapMeses = hashMapOf(
            "janeiro" to 0.0f,
            "fevereiro" to 0.0f,
            "março" to 0.0f,
            "abril" to 0.0f,
            "maio" to 0.0f,
            "junho" to 0.0f,
            "julho" to 0.0f,
            "agosto" to 0.0f,
            "setembro" to 0.0f,
            "outubro" to 0.0f,
            "novembro" to 0.0f,
            "dezembro" to 0.0f
        )

        val mapGastos = hashMapOf(
            "descricao" to "exemplo",
            "valor" to "0.0"
        )

        val calendar = Calendar.getInstance()
        val ano = calendar.get(Calendar.YEAR)

        //val nomeAleatorio = UUID.randomUUID().toString()

        db.collection("Usuarios").document(email).set(mapUsuarios)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    for (mes in mapMeses) {
                        db.collection("/Usuarios/${email}/MesesAno").document(mes.key).set(mes)
                            .addOnCompleteListener {
                                db.document("/Usuarios/${email}/MesesAno/${mes.key}").update(mapOf("ano" to ano))
                                db.collection("/Usuarios/${email}/MesesAno").document(mes.key)
                                    .collection("/gastos").document("exemplo").set(mapGastos)
                            }
                    }
                    toastSucesso("Cadastrado dom Sucesso", this)
                } else {
                    toastErro("Erro ao cadastrar usuario", this)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}