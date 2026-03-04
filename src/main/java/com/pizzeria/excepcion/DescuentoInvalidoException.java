package com.pizzeria.excepcion;

/**
 * Excepcion de negocio: se lanza cuando el porcentaje de descuento
 * no esta dentro del rango permitido (1% - 50%).
 *
 * En Spring, esta excepcion se mapea a un HTTP 400 Bad Request:
 *
 *   @ResponseStatus(HttpStatus.BAD_REQUEST)
 *   public class DescuentoInvalidoException extends RuntimeException { ... }
 */
public class DescuentoInvalidoException extends RuntimeException {

    public DescuentoInvalidoException(double porcentaje) {
        super("Descuento del " + porcentaje + "% no valido. Debe estar entre 1% y 50%");
    }
}
