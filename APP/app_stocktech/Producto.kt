package com.example.app_stocktech

data class Producto(
    var id: String = "",       // ID del producto (extraído de la clave del nodo)
    var color: String = "",    // Color del producto
    var longitud: Double = 0.0,     // Longitud del producto (en m)
    var material: String = "", // Material del producto
    var peso: Double = 0.0,       // Peso del producto (en kg)
    var cantidad: Int = 0      // Cantidad disponible en inventario
)
