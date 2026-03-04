package com.pizzeria.modelo;

import java.util.Objects;
/**
 * Bebida: un producto vendible que NO se prepara en cocina.
 * Implementa Vendible pero NO Preparable.
 */

public class Bebida implements Vendible {

    private final String nombre;
    private final double precio;
    private final int mililitros;

    public Bebida(String nombre, double precio, int mililitros) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre de la bebida no puede estar vacio");
        }
        if (precio <= 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser mayor que 0");
        }
        if (mililitros <= 0) {
            throw new IllegalArgumentException(
                    "Los mililitros deben ser mayores que 0");
        }

        this.nombre = nombre.trim();
        this.precio = precio;
        this.mililitros = mililitros;
    }

    @Override
    public String getNombre() { return nombre; }

    @Override
    public double getPrecio() { return precio; }

    @Override
    public String getDescripcion() {
        return nombre + " (" + mililitros + "ml)";
    }

    public int getMililitros() { return mililitros; }

    @Override
    public String toString() {
        return String.format("%s %dml - %.2f€", nombre, mililitros,
                precio);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Bebida otra = (Bebida) obj;
        return nombre.equals(otra.nombre) && mililitros ==
                otra.mililitros;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, mililitros);
    }
}