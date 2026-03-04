package com.pizzeria.excepcion;

/**
 * Excepcion de negocio: se lanza cuando se intenta confirmar un pedido sin items.
 *
 * Este tipo de validacion es responsabilidad del SERVICE, no del modelo.
 * El modelo (Pedido) permite crear un pedido vacio porque tal vez el cliente
 * aun no agrego nada. Pero al CONFIRMAR, el servicio valida que tenga items.
 *
 * Esta separacion (modelo permite, servicio valida) es un patron muy comun
 * en aplicaciones profesionales con Spring.
 */
public class PedidoVacioException extends RuntimeException {

    public PedidoVacioException(int numeroPedido) {
        super("El pedido #" + numeroPedido + " esta vacio, no se puede confirmar");
    }
}
