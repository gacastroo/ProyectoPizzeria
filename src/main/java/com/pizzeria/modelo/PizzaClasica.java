package com.pizzeria.modelo;

import java.util.List;

/**
 * Pizza clasica: ingredientes tradicionales, preparacion rapida.
 *
 * Esta es la subclase MAS SIMPLE posible. Demuestra que heredar
 * no siempre significa agregar muchos atributos: a veces solo
 * necesitas definir el comportamiento especifico (getTiempoPreparacion).
 *
 * Todo lo demas (nombre, precio, ingredientes, validacion, equals, toString)
 * ya lo tiene Pizza. Eso es el poder de la herencia: reutilizar sin repetir.
 */
public class PizzaClasica extends Pizza {

    public PizzaClasica(String nombre, double precioBase, Tamano tamano, List<String> ingredientes) {
        super(nombre, precioBase, tamano, ingredientes);
    }

    @Override
    public int getTiempoPreparacion() {
        return 12;
    }
}
