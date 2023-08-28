package com.nattodev.calculagasto.ui.loginCadastro

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nattodev.calculagasto.MainActivity
import com.nattodev.calculagasto.R
import com.nattodev.calculagasto.classes.Usuario
import com.nattodev.calculagasto.databinding.ActivityLoginBinding
import com.nattodev.calculagasto.toastErro

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        supportActionBar?.hide()

        binding.btnLogar.setOnClickListener { view ->
            val EmailUsuario = binding.editEmail.text.toString()
            val SenhaUsuario = binding.editSenha.text.toString()
            if(EmailUsuario.isEmpty() || SenhaUsuario.isEmpty()) {
                toastErro("Email ou senha não peenchidos", this)
            } else {
                auth.signInWithEmailAndPassword(EmailUsuario, SenhaUsuario).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser

                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        toastErro("preencha os campos corretamente", this)
                        updateUI(null)

                        binding.editEmail.setText("")
                        binding.editSenha.setText("")
                    }
                }
            }
            //signIn(EmailUsuario, SenhaUsuario)
        }
        binding.btnCadastro.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
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