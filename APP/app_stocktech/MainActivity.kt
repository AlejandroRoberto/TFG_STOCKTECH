package com.example.app_stocktech

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var btnManageProducts: Button
    private lateinit var btnStockEntries: Button
    private lateinit var btnStockExits: Button

    var animation : Animation? = null
    var animation2 : Animation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        initComponents()
        initListeners()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initComponents(){
        btnManageProducts = findViewById(R.id.btnManageProducts)
        btnStockEntries = findViewById(R.id.btnStockEntries)
        btnStockExits = findViewById(R.id.btnStockExits)

        animation = AnimationUtils.loadAnimation(this, R.anim.btn_anim_start)
        animation2 = AnimationUtils.loadAnimation(this, R.anim.btn_anim_end)
    }
    private fun initListeners(){

        btnManageProducts.setOnClickListener{
            btnManageProducts.startAnimation(animation)
            btnManageProducts.startAnimation(animation2)
            val intent = Intent(this, Main_Inventario::class.java)
            startActivity(intent)
        }

        btnStockEntries.setOnClickListener{
            btnStockEntries.startAnimation(animation)
            btnStockEntries.startAnimation(animation2)
            val intent = Intent(this, Entradas::class.java)
            startActivity(intent)
        }

        btnStockExits.setOnClickListener{
            btnStockExits.startAnimation(animation)
            btnStockExits.startAnimation(animation2)
            val intent = Intent(this, Salidas::class.java)
            startActivity(intent)
        }

    }
}