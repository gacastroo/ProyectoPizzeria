package com.pizzeria.modelo;

import java.util.List;

/**
 * Pizza gourmet: ingredientes premium, preparacion mas lenta, precio superior.
 *
 * A diferencia de PizzaClasica, esta subclase SI agrega un atributo propio
 * (chefRecomendacion) y sobreescribe varios metodos del padre.
 *
 * Conceptos que demuestra:
 * - Atributo extra en la subclase
 * - super() con parametros del padre + validacion propia
 * - @Override de getPrecio() llamando a super.getPrecio() (composicion de comportamiento)
 * - @Override de preparar() llamando a super.preparar() (extension de comportamiento)
 * - @Override de getDescripcion() para agregar info extra
 */
public class PizzaGourmet extends Pizza {

    private final String chefRecomendacion;

    public PizzaGourmet(String nombre, double precioBase, Tamano tamano,
                        List<String> ingredientes, String chefRecomendacion) {
        super(nombre, precioBase, tamano, ingredientes);

        if (chefRecomendacion == null || chefRecomendacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La pizza gourmet necesita recomendacion del chef");
        }
        this.chefRecomendacion = chefRecomendacion.trim();
    }

    public String getChefRecomendacion() {
        return chefRecomendacion;
    }

    @Override
    public int getTiempoPreparacion() {
        return 20;
    }

    @Override
    public double getPrecio() {
        return super.getPrecio() * 1.25;
    }

    @Override
    public void preparar() {
        super.preparar();
        System.out.println("  >> Recomendacion del chef: " + chefRecomendacion);
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " [GOURMET - " + chefRecomendacion + "]";
    }
}