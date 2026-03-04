package com.pizzeria.excepcion;

/**
 * Excepcion de negocio: se lanza cuando se busca un pedido que no existe.
 *
 * Es una RuntimeException (no checked) porque representa un error de LOGICA,
 * no un error del sistema. El programador deberia verificar antes de buscar,
 * pero si no lo hace, la excepcion le avisa claramente que paso.
 *
 * Lleva contexto: el numero del pedido que se busco y no se encontro.
 * Esto facilita el debugging y los logs.
 *
 * En Spring, esta excepcion se mapea a un HTTP 404 con @ResponseStatus:
 *
 *   @ResponseStatus(HttpStatus.NOT_FOUND)
 *   public class PedidoNoEncontradoException extends RuntimeException { ... }
 */
public class PedidoNoEncontradoException extends RuntimeException {

    private final int numeroPedido;

    public PedidoNoEncontradoException(int numeroPedido) {
        super("Pedido #" + numeroPedido + " no encontrado");
        this.numeroPedido = numeroPedido;
    }

    public int getNumeroPedido() {
        return numeroPedido;
    }
}
