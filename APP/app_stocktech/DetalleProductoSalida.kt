package com.example.app_stocktech

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class DetalleProductoSalida : AppCompatActivity() {

    private lateinit var tvProductoId: TextView
    private lateinit var tvCantidad: TextView
    private lateinit var etCantidad: EditText
    private lateinit var btnActualizarCantidad: Button
    private var productoId: String? = null
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_producto_salida)

        // Obtener el ID del producto
        productoId = intent.getStringExtra("productoId")

        // Inicializar vistas
        tvProductoId = findViewById(R.id.tvProductoId)
        tvCantidad = findViewById(R.id.tvCantidad)
        etCantidad = findViewById(R.id.etCantidad)
        btnActualizarCantidad = findViewById(R.id.btnActualizarCantidad)

        // Verifica que el productoId no sea null
        if (productoId != null) {
            tvProductoId.text = "Producto ID: $productoId"
            cargarProducto(productoId!!)
        } else {
            Log.e("DetalleProductoSalida", "Producto ID no recibido")
        }

        // Configurar el botón de actualización de cantidad
        btnActualizarCantidad.setOnClickListener {
            val nuevaCantidad = etCantidad.text.toString().toIntOrNull()
            if (nuevaCantidad != null) {
                actualizarCantidad(nuevaCantidad)
            } else {
                Log.e("DetalleProductoSalida", "Cantidad inválida")
            }
        }
    }

    private fun cargarProducto(productoId: String) {
        db.collection("productos")
            .document(productoId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val producto = document.toObject(Producto::class.java)
                    if (producto != null) {
                        tvCantidad.text = "Cantidad: ${producto.cantidad}"
                    }
                } else {
                    Log.e("DetalleProductoSalida", "Producto no encontrado")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("DetalleProductoSalida", "Error al cargar el producto: ${exception.message}")
            }
    }

    private fun actualizarCantidad(nuevaCantidad: Int) {
        if (productoId == null) return

        val docRef = db.collection("productos").document(productoId!!)

        docRef.get()
            .addOnSuccessListener { document ->
                if (!document.exists()) {
                    Log.e("DetalleProductoSalida", "Producto no encontrado para actualizar")
                    return@addOnSuccessListener
                }

                val cantidadActual = document.getLong("cantidad")?.toInt() ?: 0

                // Verifica si la cantidad solicitada resultará en un valor negativo
                if (cantidadActual - nuevaCantidad < 0) {
                    Toast.makeText(this@DetalleProductoSalida, "No hay suficiente stock para esa cantidad.", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val cantidadTotal = cantidadActual - nuevaCantidad

                docRef.update("cantidad", cantidadTotal)
                    .addOnSuccessListener {
                        Log.d("DetalleProductoSalida", "Cantidad actualizada exitosamente: $cantidadActual - $nuevaCantidad = $cantidadTotal")
                        Toast.makeText(this@DetalleProductoSalida, "Cantidad actualizada exitosamente: $cantidadTotal", Toast.LENGTH_SHORT).show()
                        val resultIntent = Intent().apply {
                            putExtra("productoId", productoId)
                            putExtra("nuevaCantidad", cantidadTotal)
                        }
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }
                    .addOnFailureListener { exception ->
                        Log.e("DetalleProductoSalida", "Error al actualizar cantidad: ${exception.message}")
                    }
            }
            .addOnFailureListener { exception ->
                Log.e("DetalleProductoSalida", "Error al leer producto: ${exception.message}")
            }
    }





}
