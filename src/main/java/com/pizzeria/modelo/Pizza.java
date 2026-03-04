package com.pizzeria.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Clase abstracta base para todas las pizzas de la pizzeria.
 * No se puede instanciar directamente: siempre se usa PizzaClasica o
 PizzaGourmet.
 */
public abstract class Pizza implements Vendible, Preparable {

    // =====================================================
    // Enum para los tamanos de pizza
    // =====================================================

    public enum Tamano {
        PEQUENA("Pequena", 1.0),
        MEDIANA("Mediana", 1.3),
        GRANDE("Grande", 1.6);

        private final String nombre;
        private final double multiplicador;

        Tamano(String nombre, double multiplicador) {
            this.nombre = nombre;
            this.multiplicador = multiplicador;
        }

        public String getNombre() {
            return nombre;
        }

        public double getMultiplicador() {
            return multiplicador;
        }
    }

    // =====================================================
    // Campos privados e inmutables
    // =====================================================

    private final String nombre;
    private final double precioBase;
    private final Tamano tamano;
    private final List<String> ingredientes;

    // =====================================================
    // Constructor con validacion (fail-fast)
    // =====================================================

    protected Pizza(String nombre, double precioBase, Tamano tamano,
                    List<String> ingredientes) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre de la pizza no puede estar vacio");
        }
        if (precioBase <= 0) {
            throw new IllegalArgumentException(
                    "El precio base debe ser mayor que 0");
        }
        if (tamano == null) {
            throw new IllegalArgumentException("El tamano es obligatorio");
        }
        if (ingredientes == null || ingredientes.isEmpty()) {
            throw new IllegalArgumentException(
                    "La pizza debe tener al menos un ingrediente");
        }

        this.nombre = nombre.trim();
        this.precioBase = precioBase;
        this.tamano = tamano;
        this.ingredientes = new ArrayList<>(ingredientes); // copia defensiva
    }

    // =====================================================
    // Getters (sin setters: la pizza es inmutable)
    // =====================================================

    @Override
    public String getNombre() {
        return nombre;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public Tamano getTamano() {
        return tamano;
    }

    public List<String> getIngredientes() {
        return Collections.unmodifiableList(ingredientes);
    }

    // =====================================================
    // Implementacion de Vendible
    // =====================================================

    @Override
    public double getPrecio() {
        return precioBase * tamano.getMultiplicador();
    }

    @Override
    public String getDescripcion() {
        return nombre + " " + tamano.getNombre() + " - "
                + String.join(", ", ingredientes);
    }

    // =====================================================
    // Implementacion de Preparable
    // =====================================================

    @Override
    public void preparar() {
        System.out.println("Preparando " + nombre
                + " (" + tamano.getNombre() + ")...");
        System.out.println("  Ingredientes: "
                + String.join(", ", ingredientes));
        System.out.println("  Tiempo estimado: "
                + getTiempoPreparacion() + " minutos");
    }

    @Override
    public abstract int getTiempoPreparacion();

    // =====================================================
    // toString, equals, hashCode
    // =====================================================

    @Override
    public String toString() {
        return String.format("%s (%s) - %.2f€",
                nombre, tamano.getNombre(), getPrecio());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pizza otra = (Pizza) obj;
        return nombre.equals(otra.nombre) && tamano == otra.tamano;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, tamano);
    }
}