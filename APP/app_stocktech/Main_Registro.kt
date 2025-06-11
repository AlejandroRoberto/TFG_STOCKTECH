package com.example.app_stocktech

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class Main_Registro : AppCompatActivity() {

    lateinit var btnRegistrarse : Button
    lateinit var etNombreUsuarioRegistro : EditText
    lateinit var etContraseñaUsuarioRegistro : EditText
    lateinit var etConfirmarContraseña : EditText

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_registro)

        initComponents()
        initListeners()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_registro)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initComponents(){
        btnRegistrarse = findViewById(R.id.btnRegistrarse)
        etNombreUsuarioRegistro = findViewById(R.id.etNombreUsuarioRegistro)
        etContraseñaUsuarioRegistro = findViewById(R.id.etContraseñaUsuarioRegistro)
        etConfirmarContraseña = findViewById(R.id.etConfirmarContraseña)

    }
    private fun initListeners(){

        btnRegistrarse.setOnClickListener(){
            if (etContraseñaUsuarioRegistro.text.toString() == etConfirmarContraseña.text.toString()){
                registerUser(etNombreUsuarioRegistro.text.toString(), etContraseñaUsuarioRegistro.text.toString())
                val intent = Intent(this, Main_Inicio::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this@Main_Registro, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registro exitoso
                    val user = auth.currentUser
                    Toast.makeText(this@Main_Registro, "Usuario registrado: ${user?.email}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}