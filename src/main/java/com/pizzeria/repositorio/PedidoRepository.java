package com.pizzeria.repositorio;

import com.pizzeria.modelo.Pedido;
import com.pizzeria.modelo.TipoCliente;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de persistencia para Pedido.
 *
 * ESTE ES EL PATRON REPOSITORY: separar la logica de acceso a datos
 * del resto de la aplicacion. El servicio (PedidoService) usa esta interfaz
 * sin saber NI IMPORTARLE de donde vienen los datos.
 *
 * Hoy la implementacion guarda en memoria (HashMap).
 * Manana, en Spring, esta interfaz se reemplaza por:
 *
 *   public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
 *       List<Pedido> findByClienteId(int clienteId);
 *   }
 *
 * Y Hibernate genera la implementacion automaticamente.
 */
public interface PedidoRepository {

    Pedido guardar(Pedido pedido);

    Optional<Pedido> buscarPorNumero(int numero);

    List<Pedido> buscarTodos();

    List<Pedido> buscarPorCliente(int idCliente);

        /** Busca pedidos con un total superior al precio indicado. */
    List<Pedido> buscarCaros(double precioMinimo);

    void eliminar(int numero);
}
