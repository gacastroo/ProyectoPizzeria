package com.pizzeria.repositorio;

import com.pizzeria.modelo.Pedido;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementacion del repositorio que guarda pedidos en memoria (HashMap).
 *
 * Esta clase es la que "desaparece" cuando usamos Spring Data + Hibernate:
 * - El HashMap se reemplaza por una tabla en la base de datos
 * - Los metodos los genera Hibernate automaticamente
 * - Solo necesitamos la interfaz PedidoRepository
 *
 * Pero entender COMO funciona por dentro es clave para depurar
 * problemas en Spring cuando las cosas no funcionan como esperamos.
 *
 *
 */
public class PedidoRepositoryMemoria implements PedidoRepository {

    // Simulamos una "base de datos" con un HashMap
    // La clave es el numero de pedido, el valor es el Pedido completo
    private final Map<Integer, Pedido> datos = new HashMap<>();

    @Override
    public Pedido guardar(Pedido pedido) {
        datos.put(pedido.getNumero(), pedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> buscarPorNumero(int numero) {
        return Optional.ofNullable(datos.get(numero));
    }

    @Override
    public List<Pedido> buscarTodos() {
        return new ArrayList<>(datos.values());
    }


    @Override
    public List<Pedido> buscarPorCliente(int idCliente) {
        return datos.values().stream()
                .filter(p -> p.getCliente().getId() == idCliente)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pedido> buscarCaros(double precioMinimo) {
        return datos.values().stream()
                .filter(p -> p.calcularTotal() > precioMinimo)
                .collect(Collectors.toList());
    }


    @Override
    public void eliminar(int numero) {
        datos.remove(numero);
    }
}
