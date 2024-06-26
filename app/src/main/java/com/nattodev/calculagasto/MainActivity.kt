package com.nattodev.calculagasto

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nattodev.calculagasto.classes.loadingDialog
import com.nattodev.calculagasto.databinding.ActivityMainBinding
import com.nattodev.calculagasto.ui.loginCadastro.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val db = Firebase.firestore
    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.parseColor("#44FF4B")
        MobileAds.initialize(this) {}

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        getSupportActionBar()?.setTitle("")

        MobileAds.initialize(this)
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        binding.appBarMain.btnSair.setOnClickListener {
            exitDialog()
        }

        binding.appBarMain.logoTopo.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_meses, R.id.nav_config
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        mostraDadosHeader()

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun mostraDadosHeader() {
        val userConectado = Firebase.auth.currentUser
        userConectado?.let {
            // Name, email address, and profile photo Url
            val email = it.email
            val headerView: View = binding.navView.getHeaderView(0)

            db.collection("Usuarios").document(email.toString()).get()
                .addOnCompleteListener { documento ->
                    if (documento.isSuccessful) {
                        val nome = documento.result.get("nome").toString()
                        val valorTotalgasto = documento.result.get("valorTotal").toString().toFloat()

                        val nomeHeader = headerView.findViewById<TextView>(R.id.nome_usuario)
                        val valorHeader = headerView.findViewById<TextView>(R.id.valor_total)

                        nomeHeader.text = nome
                        valorHeader.text = "R$ ${formataNumeroGrande(valorTotalgasto)}"
                    }
                }
        }
    }

    fun exitDialog() {
        val dialog = Dialog(this@MainActivity)
        dialog.setContentView(R.layout.fragment_desconectar)

        val dialogButtonYes = dialog.findViewById(R.id.btn_deslogarConfirm) as Button
        val dialogButtonNo = dialog.findViewById(R.id.btn_deslogarCancelar) as Button

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
}