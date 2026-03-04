package com.pizzeria.modelo;

public class Combo implements Vendible {

    private final String nombre;
    private final Pizza pizza;
    private final Bebida bebida;
    private final Postre postre;
    private static final double DESCUENTO_COMBO = 0.10;

    public Combo(String nombre, Pizza pizza, Bebida bebida, Postre postre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del combo es obligatorio");
        }
        if (pizza == null || bebida == null || postre == null) {
            throw new IllegalArgumentException(
                    "El combo necesita pizza, bebida y postre");
        }
        this.nombre = nombre.trim();
        this.pizza = pizza;
        this.bebida = bebida;
        this.postre = postre;
    }

    @Override
    public String getNombre() { return nombre; }

    @Override
    public double getPrecio() {
        double totalSinDescuento = pizza.getPrecio()
                + bebida.getPrecio()
                + postre.getPrecio();
        return totalSinDescuento * (1 - DESCUENTO_COMBO);
    }

    @Override
    public String getDescripcion() {
        return nombre + " [" + pizza.getNombre()
                + " + " + bebida.getNombre()
                + " + " + postre.getNombre()
                + "] (-10%)";
    }

    @Override
    public String toString() {
        return String.format("%s - %.2f€ (ahorro: %.2f€)",
                getDescripcion(), getPrecio(),
                (pizza.getPrecio() + bebida.getPrecio()
                        + postre.getPrecio()) - getPrecio());
    }

    public Pizza getPizza() { return pizza; }
    public Bebida getBebida() { return bebida; }
    public Postre getPostre() { return postre; }
}