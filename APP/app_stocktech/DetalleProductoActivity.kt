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

class DetalleProductoActivity : AppCompatActivity() {

    private lateinit var tvProductoId: TextView
    private lateinit var tvCantidad: TextView
    private lateinit var etCantidad: EditText
    private lateinit var btnActualizarCantidad: Button
    private var productoId: String? = null
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_producto)

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
            Log.e("DetalleProductoActivity", "Producto ID no recibido")
        }

        // Configurar el botón de actualización de cantidad
        btnActualizarCantidad.setOnClickListener {
            val nuevaCantidad = etCantidad.text.toString().toIntOrNull()
            if (nuevaCantidad != null) {
                actualizarCantidad(nuevaCantidad)
            } else {
                Log.e("DetalleProductoActivity", "Cantidad inválida")
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
                    Log.e("DetalleProductoActivity", "Producto no encontrado")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("DetalleProductoActivity", "Error al cargar el producto: ${exception.message}")
            }
    }

    private fun actualizarCantidad(nuevaCantidad: Int) {
        if (productoId != null) {
            // Actualizar la cantidad en Firestore
            db.collection("productos")
                .document(productoId!!)
                .update("cantidad", nuevaCantidad)
                .addOnSuccessListener {
                    Toast.makeText(this@DetalleProductoActivity, "Cantidad actualizada correctamente",
                        Toast.LENGTH_SHORT).show()

                    // Notificar a la actividad anterior que los datos fueron actualizados
                    val resultIntent = Intent()
                    resultIntent.putExtra("productoId", productoId)  // Puede que necesites otros detalles también
                    resultIntent.putExtra("nuevaCantidad", nuevaCantidad)
                    setResult(RESULT_OK, resultIntent)  // Esto indica que la operación fue exitosa

                    // Regresar a la actividad anterior
                    finish()
                }
                .addOnFailureListener { exception ->
                    Log.e("DetalleProductoActivity", "Error al actualizar cantidad: ${exception.message}")
                }
        }
    }

}
