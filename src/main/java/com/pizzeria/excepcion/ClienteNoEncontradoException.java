package com.pizzeria.excepcion;

/**
 * Excepcion de negocio: se lanza cuando se busca un cliente que no existe.
 * Misma estructura que PedidoNoEncontradoException.
 *
 * En Spring: @ResponseStatus(HttpStatus.NOT_FOUND)
 */
public class ClienteNoEncontradoException extends RuntimeException {

    private final int idCliente;

    public ClienteNoEncontradoException(int idCliente) {
        super("Cliente #" + idCliente + " no encontrado");
        this.idCliente = idCliente;
    }

    public int getIdCliente() {
        return idCliente;
    }
}