package com.pizzeria.servicio;

import com.pizzeria.modelo.*;
import com.pizzeria.repositorio.PedidoRepositoryMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoServiceTest {

    private PedidoService pedidoService;
    private Cliente clientePrueba;

    @BeforeEach
    void setUp() {
        PedidoRepositoryMemoria pedidoRepository = new PedidoRepositoryMemoria();
        pedidoService = new PedidoService(pedidoRepository);
        clientePrueba = new Cliente("Juan Test", TipoCliente.PERSONA);
    }

    @Test
    @DisplayName("Debe crear un pedido para un cliente")
    void testCrearPedido() {
        Pedido pedido = pedidoService.crearPedido(clientePrueba);
        assertNotNull(pedido, "El pedido no debe ser null");
        assertEquals("Juan Test", pedido.getNombreCliente());
    }

    @Test
    @DisplayName("Un pedido nuevo debe tener 0 items")
    void testPedidoNuevoVacio() {
        Pedido pedido = pedidoService.crearPedido(clientePrueba);
        assertEquals(0, pedido.getCantidadItems(),
                "Un pedido nuevo debe tener 0 items");
    }

    @Test
    @DisplayName("Debe poder agregar una pizza al pedido")
    void testAgregarPizza() {
        Pedido pedido = pedidoService.crearPedido(clientePrueba);
        Pizza pizza = new PizzaClasica("Margherita", 8.50,
                Pizza.Tamano.MEDIANA, List.of("tomate", "mozzarella"));
        pedidoService.agregarItem(pedido.getNumero(), pizza);
        assertEquals(1, pedido.getCantidadItems());
    }

    @Test
    @DisplayName("El total debe ser positivo despues de agregar items")
    void testTotalPositivo() {
        Pedido pedido = pedidoService.crearPedido(clientePrueba);
        Pizza pizza = new PizzaClasica("Margherita", 8.50,
                Pizza.Tamano.MEDIANA, List.of("tomate", "mozzarella"));
        pedidoService.agregarItem(pedido.getNumero(), pizza);
        assertTrue(pedido.calcularTotal() > 0,
                "El total debe ser positivo despues de agregar items");
    }

}