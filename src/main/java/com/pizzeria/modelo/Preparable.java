package com.pizzeria.modelo;

/**
 * Interfaz para productos que requieren preparacion en cocina.
 * Pizza implements Vendible Y Preparable (se vende y se prepara).
 * Bebida implements solo Vendible (se vende, pero NO se prepara).
 */
public interface Preparable {

    /**
     * Simula la preparación del productos en cocina
     * Cada tiopo de producto defina como se prepara
     */
    void preparar();

    /**
     * @return tiempo estimado de preparacion en minutos
     */
    int getTiempoPreparacion();
}