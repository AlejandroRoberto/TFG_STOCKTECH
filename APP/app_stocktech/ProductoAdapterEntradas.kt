package com.example.app_stocktech

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductoAdapterEntradas(
    private val productos: List<Producto>,
    private val onItemClick: (Producto) -> Unit // Se pasa la función onItemClick
) : RecyclerView.Adapter<ProductoAdapterEntradas.ProductoViewHolder>() {


    // Inflar el layout para el ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ProductoViewHolder(view)
    }

    // Vincular los datos del producto al ViewHolder
    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.bind(producto) // Enlaza el producto al ViewHolder
    }

    // Obtener el número de items
    override fun getItemCount(): Int {
        return productos.size
    }

    // ViewHolder para cada producto
    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvId: TextView = itemView.findViewById(R.id.tvId)
        private val tvColor: TextView = itemView.findViewById(R.id.tvColor)
        private val tvLongitud: TextView = itemView.findViewById(R.id.tvLongitud)
        private val tvMaterial: TextView = itemView.findViewById(R.id.tvMaterial)
        private val tvPeso: TextView = itemView.findViewById(R.id.tvPeso)
        private val tvCantidad: TextView = itemView.findViewById(R.id.tvCantidad)

        // Función para asociar los datos con el ViewHolder
        fun bind(producto: Producto) {
            tvId.text = producto.id
            tvColor.text = "Color: ${producto.color}"
            tvLongitud.text = "Longitud: ${producto.longitud} m"
            tvMaterial.text = "Material: ${producto.material}"
            tvPeso.text = "Peso: ${producto.peso} kg"
            tvCantidad.text = "Cantidad: ${producto.cantidad}"

            // Configurar el clic en el item
            itemView.setOnClickListener {
                onItemClick(producto) // Cuando se haga clic, se invoca la función que pasaste desde la actividad
            }
        }
    }

}
