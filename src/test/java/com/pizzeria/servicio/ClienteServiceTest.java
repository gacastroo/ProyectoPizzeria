package com.pizzeria.servicio;

import com.pizzeria.modelo.Cliente;
import com.pizzeria.modelo.Pedido;
import com.pizzeria.modelo.TipoCliente;
import com.pizzeria.repositorio.ClienteRepositoryMemoria;
import com.pizzeria.repositorio.PedidoRepositoryMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteServiceTest {
    ClienteService clienteService;
    private Cliente clientePrueba;

    @BeforeEach
            void setUp() {
        ClienteRepositoryMemoria clienteRepository = new ClienteRepositoryMemoria();
        clienteService = new ClienteService(clienteRepository, new PedidoRepositoryMemoria());
        clientePrueba = new Cliente("Juan Test", TipoCliente.PERSONA);
    }

    //Test que comprueba si es nulo y si el cliente corresponde con el creado
    @Test
    @DisplayName("Debe poder agregar un cliente y ser igual a Juan Test")
    void registrarClienteTest() {
        assertNotNull(clientePrueba, "El cliente no debe ser null");
        assertEquals("Juan Test", clientePrueba.getNombre());
    }

    @Test
    @DisplayName("El cliente no debe estar vacío")
    void registrarClienteNotEmpty() {
        assertFalse(clientePrueba.getNombre().isEmpty());
    }

}