package com.pizzeria.modelo;

/**
 * Interfaz que representa cualquier producto que se puede vender en la
 pizzeria.
 * Tanto Pizza como Bebida implementan esta interfaz, aunque NO comparten
 herencia.
 * Eso demuestra que una interfaz agrupa COMPORTAMIENTO, no FAMILIA.
 */
public interface Vendible {
    /**
     * *
     * @return el nombre del producto
     */

    String getNombre();

    /**
     * Devuelve el precio final del producto
     * @return
     */
    double getPrecio();

    /**
     * Devuelve la descripcion legible del producto para mostrar el ticket del producto
     * @return
     */
    String getDescripcion();
}
