package com.example.app_stocktech

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class Salidas : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private val productos = mutableListOf<Producto>()
    private lateinit var btnVolverInicio: Button

    var animation : Animation? = null
    var animation2 : Animation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_salidas)


        initComponents()
        initListeners()

        // Definir la función onItemClick
        val onItemClick: (Producto) -> Unit = { producto ->
            // Acción cuando se hace clic en un producto
            val intent = Intent(this, DetalleProductoSalida::class.java)
            intent.putExtra("productoId", producto.id)  // Pasar el ID del producto
            startActivity(intent)
        }

        // Crear el adaptador y pasar la lista de productos y la función onItemClick
        adapter = ProductoAdapter(productos, onItemClick)
        recyclerView.adapter = adapter

        // Obtener los productos desde Firestore
        val db = FirebaseFirestore.getInstance()

        db.collection("productos")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("Firestore", "Error al escuchar cambios: ${exception.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    productos.clear() // Limpia la lista antes de actualizarla
                    for (document in snapshot.documents) {
                        val producto = document.toObject(Producto::class.java)
                        producto?.id = document.id // Asigna el ID del documento
                        if (producto != null) {
                            productos.add(producto)
                        }
                    }
                    adapter.notifyDataSetChanged() // Notifica cambios al adaptador
                }
            }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun initComponents(){
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        btnVolverInicio = findViewById(R.id.btnVolverInicio)

        animation = AnimationUtils.loadAnimation(this, R.anim.btn_anim_start)
        animation2 = AnimationUtils.loadAnimation(this, R.anim.btn_anim_end)

    }

    private fun initListeners(){
        btnVolverInicio.setOnClickListener {

            btnVolverInicio.startAnimation(animation)
            btnVolverInicio.startAnimation(animation2)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val productoId = data.getStringExtra("productoId")
            val nuevaCantidad = data.getIntExtra("nuevaCantidad", -1)

            if (productoId != null && nuevaCantidad != -1) {
                // Aquí deberías actualizar el modelo de datos de tu lista
                // Actualiza el producto correspondiente en tu lista y notifica al adaptador
                actualizarProductoEnLista(productoId, nuevaCantidad)
            }
        }
    }

    private fun actualizarProductoEnLista(productoId: String, nuevaCantidad: Int) {
        // Aquí actualizas el modelo de datos y luego notificas al adaptador del RecyclerView
        val producto = Producto(productoId)

        producto.cantidad = nuevaCantidad
        val index = productos.indexOfFirst { it.id == productoId }
        if (index != -1) {
            productos[index] = producto
        }
        adapter.notifyItemChanged(index) // Notifica al adaptador del RecyclerView

    }
}