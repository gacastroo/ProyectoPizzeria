package com.pizzeria.modelo;

import java.util.Objects;

/**
 * Postre: un producto que se vende Y se prepara en cocina, pero NO es una Pizza.
 *
 * Implementa Vendible (se vende) y Preparable (se prepara en cocina).
 * A diferencia de Pizza, no usa herencia ni clase abstracta: es independiente.
 *
 * Esto demuestra que dos clases sin relacion de herencia pueden compartir
 * las mismas interfaces y funcionar en el mismo sistema (List<Vendible>,
 * instanceof Preparable, etc.) sin ningun cambio en el codigo existente.
 */
public class Postre implements Vendible, Preparable {

    private final String nombre;
    private final double precio;
    private final boolean requiereHorno;

    public Postre(String nombre, double precio, boolean requiereHorno) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del postre no puede estar vacio");
        }
        if (precio <= 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser mayor que 0");
        }

        this.nombre = nombre.trim();
        this.precio = precio;
        this.requiereHorno = requiereHorno;
    }

    public boolean isRequiereHorno() {
        return requiereHorno;
    }

    // =====================================================
    // Implementacion de Vendible
    // =====================================================

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public double getPrecio() {
        return precio;
    }

    @Override
    public String getDescripcion() {
        return nombre + (requiereHorno ? " (horneado)" : " (frio)");
    }

    // =====================================================
    // Implementacion de Preparable
    // =====================================================

    @Override
    public void preparar() {
        System.out.println("Preparando postre: " + nombre + "...");
        if (requiereHorno) {
            System.out.println("  Encendiendo horno para el postre");
        } else {
            System.out.println("  Preparacion en frio (sin horno)");
        }
        System.out.println("  Tiempo estimado: "
                + getTiempoPreparacion() + " minutos");
    }

    @Override
    public int getTiempoPreparacion() {
        return requiereHorno ? 15 : 5;
    }

    // =====================================================
    // toString, equals, hashCode
    // =====================================================

    @Override
    public String toString() {
        return String.format("%s - %.2f€", nombre, precio);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Postre otro = (Postre) obj;
        return nombre.equals(otro.nombre)
                && requiereHorno == otro.requiereHorno;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, requiereHorno);
    }
}
