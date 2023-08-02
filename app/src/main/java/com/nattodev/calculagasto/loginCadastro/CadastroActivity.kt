package com.nattodev.calculagasto.loginCadastro

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnCadastrar.setOnClickListener { view ->
            val nome = binding.editNome.text.toString()
            val email = binding.editEmail.text.toString().trim()
            val valorMaximo = binding.editValorMaximo.text.toString()
            val senha = binding.editSenha.text.toString().trim()
            val confirm = binding.editConfirmaSenha.text.toString().trim()

            if(senha === confirm && !email.isEmpty() || !senha.isEmpty() || !confirm.isEmpty()
                || !valorMaximo.isEmpty() || !nome.isEmpty()) {
                if(senha.length < 6) {
                    val snackbar = Snackbar.make(view, "A senha deve ter mais de 6 caracteres", Snackbar.LENGTH_LONG)
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()
                } else {
                    auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success")
                                val user = auth.currentUser

                                salvaUsuario(nome, email, valorMaximo, senha)

                                val snackbar = Snackbar.make(view, "Cadastro realizado com sucesso", Snackbar.LENGTH_LONG)
                                snackbar.setBackgroundTint(Color.GREEN)
                                snackbar.show()
                                updateUI(user)
                            } else {
                                // If sign in fails, display a message to the user.
                                //Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                val snackbar = Snackbar.make(view, "preencha os campos corretamente", Snackbar.LENGTH_LONG)
                                snackbar.setBackgroundTint(Color.RED)
                                snackbar.show()

                                binding.editSenha.setText("")
                                binding.editConfirmaSenha.setText("")
                                updateUI(null)
                            }
                        }
                }
            } else {
                val snackbar = Snackbar.make(view, "preencha todos os campos", Snackbar.LENGTH_LONG)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()

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

    private fun salvaUsuario(nome:String, email:String, valorMaximo:String, senha:String) {
        val mapUsuarios = hashMapOf(
            "nome" to nome,
            "email" to email,
            "valorMaximo" to valorMaximo,
            "senha" to senha
        )

        db.collection("Usuarios").document(email).set(mapUsuarios)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Toast.makeText(applicationContext, "usuario cadastrado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "Erro ao cadastrar usuario", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user != null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}