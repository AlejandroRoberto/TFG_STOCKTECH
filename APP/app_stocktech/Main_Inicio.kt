package com.example.app_stocktech

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class Main_Inicio : AppCompatActivity() {

    lateinit var tvRegistrarse: TextView
    lateinit var etNombreUsuario: EditText
    lateinit var etContraseñaUsuario: EditText
    lateinit var btnIniciarSesion: Button
    lateinit var btnGithub: ImageButton

    var animation : Animation? = null
    var animation2 : Animation? = null

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_inicio)

        initComponents()
        initListeners()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }



    private fun initComponents(){
        tvRegistrarse = findViewById(R.id.tvRegistrarse)
        etNombreUsuario = findViewById(R.id.etNombreUsuario)
        etContraseñaUsuario = findViewById(R.id.etContraseñaUsuario)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)
        btnGithub = findViewById(R.id.btnGitHub)

        animation = AnimationUtils.loadAnimation(this, R.anim.btn_anim_start)
        animation2 = AnimationUtils.loadAnimation(this, R.anim.btn_anim_end)
    }

    private fun initListeners(){

        tvRegistrarse.setOnClickListener(){
            val intent = Intent(this, Main_Registro::class.java)
            startActivity(intent)
        }

        btnIniciarSesion.setOnClickListener(){
            btnIniciarSesion.startAnimation(animation)
            btnIniciarSesion.startAnimation(animation2)
            loginUser(etNombreUsuario.text.toString(), etContraseñaUsuario.text.toString())
        }

        btnGithub.setOnClickListener(){
            val url = "https://github.com/AlejandroRoberto/AlejandroRoberto"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso
                    val user = auth.currentUser
                    Toast.makeText(this@Main_Inicio, "Usuario logueado: ${user?.email}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // Error en el inicio de sesión
                    Toast.makeText(this@Main_Inicio, "No se pudo iniciar sesión", Toast.LENGTH_SHORT).show()

                }
            }
    }
}