package com.pizzeria.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Pedido: agrupa varios productos vendibles de un cliente.
 *
 * REFACTORING: antes el cliente era un String. Ahora es un objeto Cliente.
 * Esto permite:
 * - Tener datos del cliente (email, telefono, tipo, categoria)
 * - Buscar pedidos por id de cliente (mas fiable que por nombre)
 * - Que el cliente tenga su propio ciclo de vida (registro, fidelidad, etc.)
 *
 * En Spring/Hibernate:
 *   @ManyToOne
 *   @JoinColumn(name = "cliente_id")
 *   private Cliente cliente;
 */
public class Pedido {

    private static int contadorPedidos = 0;

    private final int numero;
    private final Cliente cliente;
    private final List<Vendible> items;
    private final LocalDateTime fechaCreacion;

    public Pedido(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente es obligatorio");
        }

        this.numero = ++contadorPedidos;
        this.cliente = cliente;
        this.items = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
    }

    public int getNumero() { return numero; }
    public Cliente getCliente() { return cliente; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    /**
     * Mantiene compatibilidad: devuelve el nombre del cliente.
     * Util para Streams y codigo que ya usaba getNombreCliente().
     */
    public String getNombreCliente() { return cliente.getNombre(); }

    public List<Vendible> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void agregarItem(Vendible item) {
        if (item == null) {
            throw new IllegalArgumentException("El item no puede ser nulo");
        }
        items.add(item);
    }

    public double calcularTotal() {
        double total = 0;
        for (Vendible item : items) {
            total += item.getPrecio();
        }
        return total;
    }

    public int getCantidadItems() { return items.size(); }

    public List<Preparable> getItemsParaCocina() {
        List<Preparable> paraCocina = new ArrayList<>();
        for (Vendible item : items) {
            if (item instanceof Preparable) {
                paraCocina.add((Preparable) item);
            }
        }
        return paraCocina;
    }

    public int getTiempoTotalPreparacion() {
        int tiempoMax = 0;
        for (Preparable item : getItemsParaCocina()) {
            if (item.getTiempoPreparacion() > tiempoMax) {
                tiempoMax = item.getTiempoPreparacion();
            }
        }
        return tiempoMax;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("========================================%n"));
        sb.append(String.format("  PEDIDO #%d%n", numero));
        sb.append(String.format("========================================%n"));
        sb.append(String.format("  Cliente: %s [%s]%n",
                cliente.getNombre(), cliente.getTipo()));
        sb.append(String.format("  Fecha:   %s%n",
                fechaCreacion.format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        sb.append(String.format("----------------------------------------%n"));

        for (int i = 0; i < items.size(); i++) {
            Vendible item = items.get(i);
            sb.append(String.format("  %d. %-30s %.2f€%n",
                    i + 1, item.getDescripcion(), item.getPrecio()));
        }

        sb.append(String.format("----------------------------------------%n"));
        sb.append(String.format("  TOTAL: %34s%.2f€%n", "",
                calcularTotal()));

        if (!getItemsParaCocina().isEmpty()) {
            sb.append(String.format("  Tiempo de preparacion: ~%d minutos%n",
                    getTiempoTotalPreparacion()));
        }

        sb.append(String.format("========================================%n"));
        return sb.toString();
    }
}
