package com.pizzeria.repositorio;

import com.pizzeria.modelo.Cliente;
import com.pizzeria.modelo.TipoCliente;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementacion del repositorio de clientes en memoria (HashMap).
 * Misma estructura que PedidoRepositoryMemoria.
 *
 * En Spring con JPA, esta clase desaparece: Hibernate genera
 * la implementacion automaticamente a partir de la interfaz.
 */
public class ClienteRepositoryMemoria implements ClienteRepository {

    private final Map<Integer, Cliente> datos = new HashMap<>();

    // -------------------------------
    // Guardar o actualizar cliente
    // -------------------------------
    @Override
    public Cliente guardar(Cliente cliente) {
        datos.put(cliente.getId(), cliente);
        return cliente;
    }

    // -------------------------------
    // Buscar por ID
    // -------------------------------
    @Override
    public Optional<Cliente> buscarPorId(int id) {
        return Optional.ofNullable(datos.get(id));
    }

    // -------------------------------
    // Buscar todos los clientes
    // -------------------------------
    @Override
    public List<Cliente> buscarTodos() {
        return new ArrayList<>(datos.values());
    }

    @Override
    public List<Cliente> buscarPorRanking() {
        return List.of();
    }

    // -------------------------------
    // Buscar por nombre (case insensitive)
    // -------------------------------
    @Override
    public Optional<Cliente> buscarPorNombre(String nombre) {
        if (nombre == null) return Optional.empty();
        return datos.values().stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre.trim()))
                .findFirst();
    }

    // -------------------------------
    // Buscar por tipo (PERSONA / EMPRESA)
    // -------------------------------
    @Override
    public List<Cliente> buscarPorTipo(TipoCliente tipo) {
        if (tipo == null) return Collections.emptyList();
        return datos.values().stream()
                .filter(c -> c.getTipo() == tipo)
                .collect(Collectors.toList());
    }

    // -------------------------------
    // Eliminar cliente por ID
    // -------------------------------
    @Override
    public void eliminar(int id) {
        datos.remove(id);
    }
}