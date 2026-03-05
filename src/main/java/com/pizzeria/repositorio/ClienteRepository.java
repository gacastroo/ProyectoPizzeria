package com.pizzeria.repositorio;

import com.pizzeria.modelo.Cliente;
import com.pizzeria.modelo.TipoCliente;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de persistencia para Cliente.
 * Sigue el mismo patron que PedidoRepository.
 *
 * En Spring con JPA:
 *
 *   public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
 *       Optional<Cliente> findByNombreIgnoreCase(String nombre);
 *       List<Cliente> findByTipo(TipoCliente tipo);
 *   }
 *
 * TODO: Completar los metodos que faltan.
 * Mira PedidoRepository.java como ejemplo: tiene guardar, buscarPorNumero,
 * buscarTodos, buscarPorCliente y eliminar. Aqui necesitas los equivalentes
 * para Cliente.
 */

public interface ClienteRepository {

    /** Guarda un cliente (nuevo o actualizado). */
    Cliente guardar(Cliente cliente);

    /** Busca un cliente por su id. */
    Optional<Cliente> buscarPorId(int id);

    /** Devuelve todos los clientes registrados. */
    List<Cliente> buscarTodos();

    List<Cliente> buscarPorRanking();

    /** Busca un cliente por nombre (case insensitive). */
    Optional<Cliente> buscarPorNombre(String nombre);

    /** Filtra clientes por tipo (PERSONA o EMPRESA). */
    List<Cliente> buscarPorTipo(TipoCliente tipo);

    /** Elimina un cliente por su id. */
    void eliminar(int id);
}
