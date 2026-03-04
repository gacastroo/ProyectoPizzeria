package com.pizzeria;

import com.pizzeria.excepcion.ClienteNoEncontradoException;
import com.pizzeria.excepcion.DescuentoInvalidoException;
import com.pizzeria.excepcion.PedidoNoEncontradoException;
import com.pizzeria.excepcion.PedidoVacioException;
import com.pizzeria.modelo.*;
import com.pizzeria.repositorio.ClienteRepository;
import com.pizzeria.repositorio.ClienteRepositoryMemoria;
import com.pizzeria.repositorio.PedidoRepository;
import com.pizzeria.repositorio.PedidoRepositoryMemoria;
import com.pizzeria.servicio.ClienteService;
import com.pizzeria.servicio.PedidoService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Aplicacion principal de la Pizzeria - Version 3 (Clientes como entidad).
 *
 * NOVEDAD respecto a v2:
 * - Los clientes ya NO son un simple String dentro de Pedido
 * - Cliente es una entidad propia con tipo (PERSONA/EMPRESA) y categoria de fidelidad
 * - ClienteService gestiona la logica de clientes (registro, fidelidad)
 * - PedidoService.crearPedido() ahora recibe un objeto Cliente, no un String
 */
public class PizzeriaApp {

    public static void main(String[] args) {

        System.out.println("=============================================");
        System.out.println("   PIZZERIA JAVA - Version 3 (Clientes)");
        System.out.println("=============================================");
        System.out.println();

        // ==========================================================
        // 1. CONFIGURACION: crear las dependencias manualmente
        //    Ahora hay DOS repositorios y DOS servicios
        // ==========================================================

        ClienteRepository clienteRepo = new ClienteRepositoryMemoria();
        PedidoRepository pedidoRepo = new PedidoRepositoryMemoria();

        ClienteService clienteService = new ClienteService(clienteRepo, pedidoRepo);
        PedidoService pedidoService = new PedidoService(pedidoRepo);

        // ==========================================================
        // 2. REGISTRAR CLIENTES (NUEVO en v3!)
        //    Antes: los clientes eran Strings dentro de crearPedido()
        //    Ahora: se registran como entidades con datos propios
        // ==========================================================

        System.out.println("--- REGISTRAR CLIENTES ---\n");

        Cliente ana = clienteService.registrarCliente("Ana Lopez", TipoCliente.PERSONA);
        ana.setEmail("ana.lopez@gmail.com");
        ana.setTelefono("612345678");

        Cliente carlos = clienteService.registrarCliente("Carlos Martinez", TipoCliente.PERSONA);

        // Una empresa tambien puede ser cliente!
        Cliente banco = clienteService.registrarCliente("Banco Santander", TipoCliente.EMPRESA);
        banco.setEmail("pedidos@santander.es");
        banco.setTelefono("911234567");

        Cliente luis = clienteService.registrarCliente("Luis Garcia", TipoCliente.PERSONA);

        System.out.println(ana);
        System.out.println(carlos);
        System.out.println(banco);
        System.out.println(luis);

        // ==========================================================
        // 3. CREAR PRODUCTOS (esto no cambio)
        // ==========================================================

        Pizza margarita = new PizzaClasica(
                "Margarita", 8.50, Pizza.Tamano.MEDIANA,
                Arrays.asList("tomate", "mozzarella", "albahaca"));

        Pizza carnivora = new PizzaClasica(
                "Carnivora", 10.50, Pizza.Tamano.GRANDE,
                Arrays.asList("tomate", "mozzarella", "jamon", "bacon",
                        "chorizo", "carne picada"));

        Pizza trufada = new PizzaGourmet(
                "Trufa Negra", 15.00, Pizza.Tamano.GRANDE,
                Arrays.asList("crema de trufa", "mozzarella di bufala", "rucula"),
                "Agregar aceite de trufa al servir");

        Bebida cocaCola = new Bebida("Coca-Cola", 2.50, 500);
        Postre tiramisu = new Postre("Tiramisu", 5.50, false);

        // ==========================================================
        // 4. CREAR PEDIDOS CON OBJETOS CLIENTE
        //    Antes: servicio.crearPedido("Ana Lopez")   <-- String
        //    Ahora: pedidoService.crearPedido(ana)       <-- Cliente
        // ==========================================================

        System.out.println("\n--- CREAR PEDIDOS ---\n");

        Pedido pedido1 = pedidoService.crearPedido(ana);
        pedidoService.agregarItem(pedido1.getNumero(), margarita);
        pedidoService.agregarItem(pedido1.getNumero(), trufada);
        pedidoService.agregarItem(pedido1.getNumero(), cocaCola);

        System.out.println(pedidoService.buscarPedido(pedido1.getNumero()));

        // ==========================================================
        // 5. CONFIRMAR PEDIDO
        // ==========================================================

        System.out.println("--- CONFIRMAR PEDIDO ---\n");
        pedidoService.confirmarPedido(pedido1.getNumero());

        // ==========================================================
        // 6. EXCEPCIONES DE NEGOCIO
        // ==========================================================

        System.out.println("\n--- EXCEPCIONES DE NEGOCIO ---\n");

        try {
            pedidoService.buscarPedido(999);
        } catch (PedidoNoEncontradoException e) {
            System.out.println("Error controlado: " + e.getMessage());
        }

        try {
            Pedido pedidoVacio = pedidoService.crearPedido(ana);
            pedidoService.confirmarPedido(pedidoVacio.getNumero());
        } catch (PedidoVacioException e) {
            System.out.println("Error controlado: " + e.getMessage());
        }

        try {
            clienteService.buscarCliente(999);
        } catch (ClienteNoEncontradoException e) {
            System.out.println("Error controlado: " + e.getMessage());
        }

        // ==========================================================
        // 7. MAS PEDIDOS PARA TENER DATOS VARIADOS
        // ==========================================================

        System.out.println("\n--- MAS PEDIDOS ---\n");

        Pedido pedido2 = pedidoService.crearPedido(carlos);
        pedidoService.agregarItem(pedido2.getNumero(), carnivora);

        Pedido pedido3 = pedidoService.crearPedido(ana);
        pedidoService.agregarItem(pedido3.getNumero(), margarita);
        pedidoService.agregarItem(pedido3.getNumero(), cocaCola);

        Pedido pedido4 = pedidoService.crearPedido(luis);
        pedidoService.agregarItem(pedido4.getNumero(), trufada);
        pedidoService.agregarItem(pedido4.getNumero(), trufada);
        pedidoService.agregarItem(pedido4.getNumero(), cocaCola);

        // El banco pide para una reunion
        Pedido pedidoBanco = pedidoService.crearPedido(banco);
        pedidoService.agregarItem(pedidoBanco.getNumero(), margarita);
        pedidoService.agregarItem(pedidoBanco.getNumero(), margarita);
        pedidoService.agregarItem(pedidoBanco.getNumero(), carnivora);
        pedidoService.agregarItem(pedidoBanco.getNumero(), cocaCola);
        pedidoService.agregarItem(pedidoBanco.getNumero(), cocaCola);
        pedidoService.agregarItem(pedidoBanco.getNumero(), cocaCola);

        System.out.println(pedidoService.buscarPedido(pedidoBanco.getNumero()));

        // ==========================================================
        // 8. APLICAR DESCUENTO
        // ==========================================================

        System.out.println("--- APLICAR DESCUENTO ---\n");
        pedidoService.aplicarDescuento(pedido1.getNumero(), 15);

        try {
            pedidoService.aplicarDescuento(pedido1.getNumero(), 80);
        } catch (DescuentoInvalidoException e) {
            System.out.println("\nError controlado: " + e.getMessage());
        }

        // ==========================================================
        // 9. RESUMEN DE VENTAS
        // ==========================================================

        System.out.println("\n" + pedidoService.generarResumen());

        // ==========================================================
        // 10. STREAMS: analisis de datos
        // ==========================================================

        System.out.println("--- ANALISIS CON STREAMS ---\n");

        List<Pedido> todosLosPedidos = pedidoService.listarTodos();

        // Promedio por pedido
        OptionalDouble promedio = todosLosPedidos.stream()
                .mapToDouble(Pedido::calcularTotal)
                .average();
        promedio.ifPresent(avg -> System.out.printf("Promedio por pedido: %.2f€%n", avg));

        // Pedidos con items
        long pedidosConItems = todosLosPedidos.stream()
                .filter(p -> p.getCantidadItems() > 0)
                .count();
        System.out.println("Pedidos con items: " + pedidosConItems);

        // Agrupar pedidos por cliente
        Map<String, List<Pedido>> porCliente = todosLosPedidos.stream()
                .collect(Collectors.groupingBy(Pedido::getNombreCliente));

        System.out.println("\nPedidos por cliente:");
        porCliente.forEach((cliente, pedidos) ->
                System.out.printf("  %s: %d pedido(s), total %.2f€%n",
                        cliente, pedidos.size(),
                        pedidos.stream().mapToDouble(Pedido::calcularTotal).sum()));

        // Gasto total por cliente
        Map<String, Double> gastosPorCliente = todosLosPedidos.stream()
                .collect(Collectors.groupingBy(
                        Pedido::getNombreCliente,
                        Collectors.summingDouble(Pedido::calcularTotal)));

        System.out.println("\nGasto total por cliente:");
        gastosPorCliente.forEach((cliente, total) ->
                System.out.printf("  %s: %.2f€%n", cliente, total));

        // Mejor cliente
        Map.Entry<String, Double> mejorCliente = gastosPorCliente.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow();
        System.out.printf("\nMejor cliente: %s (%.2f€)%n",
                mejorCliente.getKey(), mejorCliente.getValue());

        // Nombres unicos ordenados
        System.out.println("\nClientes (ordenados):");
        todosLosPedidos.stream()
                .map(Pedido::getNombreCliente)
                .distinct()
                .sorted()
                .forEach(nombre -> System.out.println("  - " + nombre));

        // ==========================================================
        // 11. COMBO
        // ==========================================================

        System.out.println("\n--- COMBO ---\n");

        Combo comboFamiliar = new Combo("Combo Familiar",
                margarita, cocaCola, tiramisu);

        System.out.println(comboFamiliar);
        System.out.printf("Sin combo pagarian: %.2f€%n",
                margarita.getPrecio() + cocaCola.getPrecio()
                        + tiramisu.getPrecio());

        pedidoService.agregarItem(pedido1.getNumero(), comboFamiliar);
        System.out.println("\nPedido actualizado:");
        System.out.println(pedidoService.buscarPedido(pedido1.getNumero()));

        // ==========================================================
        // 12. PROGRAMA DE FIDELIDAD (ahora en ClienteService)
        //     Antes: servicio.clasificarCliente("Ana Lopez") -- devuelve String
        //     Ahora: clienteService.actualizarCategoria(ana.getId()) -- actualiza objeto
        // ==========================================================

        System.out.println("--- PROGRAMA DE FIDELIDAD ---\n");

        System.out.println("Antes de actualizar:");
        System.out.println("  " + ana);
        System.out.println("  " + carlos);
        System.out.println("  " + banco);
        System.out.println("  " + luis);

        clienteService.actualizarCategoria(ana.getId());
        clienteService.actualizarCategoria(carlos.getId());
        clienteService.actualizarCategoria(banco.getId());
        clienteService.actualizarCategoria(luis.getId());

        System.out.println("\nDespues de actualizar:");
        System.out.println("  " + ana);     // 3 pedidos (1 + vacio + 3) -> PLATA
        System.out.println("  " + carlos);  // 1 pedido  -> BRONCE
        System.out.println("  " + banco);   // 1 pedido  -> BRONCE
        System.out.println("  " + luis);     // 1 pedido  -> BRONCE

        // ==========================================================
        // 13. BUSCAR POR TIPO DE CLIENTE
        // ==========================================================

        System.out.println("\n--- CLIENTES POR TIPO ---\n");

        List<Cliente> empresas = clienteService.buscarEmpresas();
        System.out.println("Empresas:");
        for (Cliente c : empresas) {
            System.out.println("  " + c);
        }

        List<Cliente> personas = clienteService.buscarPersonas();
        System.out.println("Personas:");
        for (Cliente c : personas) {
            System.out.println("  " + c);
        }

// Gasto total usando stream directo
        double total = todosLosPedidos.stream()
                .mapToDouble(Pedido::calcularTotal)
                .sum();
        System.out.println("Gasto total (stream): " + total);

        // ==========================================================
        // 14. INFORME DE CLIENTE
        // ==========================================================

        System.out.println("\n--- INFORMES ---\n");
        System.out.println(clienteService.generarInformeCliente(ana.getId()));
        System.out.println(clienteService.generarInformeCliente(banco.getId()));

        // ==========================================================
        // 15. CANCELAR PEDIDO
        // ==========================================================

        System.out.println("--- CANCELAR PEDIDO ---\n");
        pedidoService.cancelarPedido(pedido2.getNumero());
        System.out.println("Pedidos restantes: " + pedidoService.listarTodos().size());

        System.out.println("\n--- EXPORTAR A JSON ---\n");
        System.out.println(pedidoService.exportarJSON());
    }
}
