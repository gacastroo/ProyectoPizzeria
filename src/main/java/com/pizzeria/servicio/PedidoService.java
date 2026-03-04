package com.pizzeria.servicio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import com.pizzeria.excepcion.DescuentoInvalidoException;
import com.pizzeria.excepcion.PedidoNoEncontradoException;
import com.pizzeria.excepcion.PedidoVacioException;
import com.pizzeria.modelo.Cliente;
import com.pizzeria.modelo.Pedido;
import com.pizzeria.modelo.Preparable;
import com.pizzeria.modelo.Vendible;
import com.pizzeria.repositorio.PedidoRepository;

import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Servicio que contiene la LOGICA DE NEGOCIO de los pedidos.
 *
 * INYECCION DE DEPENDENCIAS POR CONSTRUCTOR:
 * Este servicio recibe su repositorio por constructor. NO lo crea el mismo.
 * Eso significa que:
 * 1. Podemos cambiar la implementacion del repositorio sin tocar el servicio
 * 2. Podemos probar el servicio con un repositorio "falso" (mock) en tests
 * 3. El servicio NO sabe si los datos estan en memoria, en archivo, o en MySQL
 *
 * En Spring, esta inyeccion se hace automaticamente con @Autowired:
 *
 *   @Service
 *   public class PedidoService {
 *       private final PedidoRepository repositorio;
 *
 *       @Autowired
 *       public PedidoService(PedidoRepository repositorio) {
 *           this.repositorio = repositorio;
 *       }
 *   }
 */
public class PedidoService {

    private final PedidoRepository repositorio;

    /**
     * Constructor con inyeccion de dependencias.
     * Recibe la dependencia (repositorio) en vez de crearla internamente.
     */
    public PedidoService(PedidoRepository repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Crea un nuevo pedido para un cliente y lo guarda en el repositorio.
     * REFACTORING: antes recibia String nombreCliente, ahora recibe un objeto Cliente.
     */
    public Pedido crearPedido(Cliente cliente) {
        Pedido pedido = new Pedido(cliente);
        return repositorio.guardar(pedido);
    }

    /**
     * Busca un pedido por numero.
     * Usa Optional.orElseThrow(): si el pedido no existe, lanza la excepcion.
     */
    public Pedido buscarPedido(int numero) {
        return repositorio.buscarPorNumero(numero)
                .orElseThrow(() -> new PedidoNoEncontradoException(numero));
    }

    public Pedido buscarCaros(double precioMinimo) {
        return (Pedido) repositorio.buscarCaros(precioMinimo);
            //.set(() -> new PedidoNoEncontradoException(precioMinimo));
    }

    /**
     * Agrega un item a un pedido existente.
     * Si el pedido no existe, lanza PedidoNoEncontradoException.
     */
    public Pedido agregarItem(int numeroPedido, Vendible item) {
        Pedido pedido = buscarPedido(numeroPedido);
        pedido.agregarItem(item);
        return repositorio.guardar(pedido);
    }


    /**
     * Lista todos los pedidos.
     */
    public List<Pedido> listarTodos() {
        return repositorio.buscarTodos();
    }

    /**
     * Busca pedidos de un cliente por su id.
     */
    public List<Pedido> buscarPorCliente(int idCliente) {
        return repositorio.buscarPorCliente(idCliente);
    }

    /**
     * Confirma un pedido: valida que tenga items y los envia a cocina.
     * Esta es LOGICA DE NEGOCIO: el modelo no sabe de "confirmar",
     * el repositorio no sabe de "cocina". Solo el servicio coordina todo.
     */
    public void confirmarPedido(int numeroPedido) {
        Pedido pedido = buscarPedido(numeroPedido);

        // Regla de negocio: no se puede confirmar un pedido vacio
        if (pedido.getCantidadItems() == 0) {
            throw new PedidoVacioException(numeroPedido);
        }

        System.out.println("Pedido #" + numeroPedido + " CONFIRMADO!");
        System.out.println("Enviando a cocina...\n");

        for (Preparable item : pedido.getItemsParaCocina()) {
            item.preparar();
            System.out.println();
        }

        System.out.println("Tiempo total estimado: ~" + pedido.getTiempoTotalPreparacion() + " minutos");
    }

    /**
     * Aplica un descuento a un pedido y retorna el total con descuento.
     * REGLAS DE NEGOCIO:
     * - El pedido debe existir
     * - El porcentaje debe estar entre 1 y 50
     * - El pedido no puede estar vacio
     */
    public double aplicarDescuento(int numeroPedido, double porcentaje) {
        Pedido pedido = buscarPedido(numeroPedido);

        // Regla de negocio: descuento entre 1% y 50%
        if (porcentaje < 1 || porcentaje > 50) {
            throw new DescuentoInvalidoException(porcentaje);
        }

        // Regla de negocio: no se descuenta un pedido vacio
        if (pedido.getCantidadItems() == 0) {
            throw new PedidoVacioException(numeroPedido);
        }

        double total = pedido.calcularTotal();
        double descuento = total * porcentaje / 100;
        double totalFinal = total - descuento;

        System.out.printf("Descuento del %.0f%% aplicado al pedido #%d%n", porcentaje, numeroPedido);
        System.out.printf("Total original: %.2f€%n", total);
        System.out.printf("Descuento:      -%.2f€%n", descuento);
        System.out.printf("Total final:    %.2f€%n", totalFinal);

        return totalFinal;
    }

    /**
     * Genera un resumen de todas las ventas.
     * Este tipo de metodo de "reporte" es muy comun en servicios reales.
     * En Spring, se expone con un endpoint como GET /api/pedidos/resumen.
     */
    public String generarResumen() {
        List<Pedido> todos = repositorio.buscarTodos();

        if (todos.isEmpty()) {
            return "No hay pedidos registrados.";
        }

        double totalVentas = todos.stream()
                .mapToDouble(Pedido::calcularTotal)
                .sum();

        Pedido mayor = todos.stream()
                .max(Comparator.comparingDouble(Pedido::calcularTotal))
                .orElseThrow();

        OptionalDouble promedio = todos.stream()
                .mapToDouble(Pedido::calcularTotal)
                .average();

        return String.format(
            "=== RESUMEN DE VENTAS ===%n" +
            "Total pedidos: %d%n" +
            "Ventas totales: %.2f€%n" +
            "Promedio/pedido: %.2f€%n" +
            "Pedido mas grande: #%d de %s (%.2f€)%n" +
            "=========================%n",
            todos.size(),
            totalVentas,
            promedio.orElse(0.0),
            mayor.getNumero(),
            mayor.getNombreCliente(),
            mayor.calcularTotal()
        );
    }

    // clasificarCliente() se movio a ClienteService.actualizarCategoria()
    // La logica de fidelidad pertenece al servicio de clientes, no al de pedidos.

    /**
     * Cancela un pedido (lo elimina del repositorio).
     */
    public void cancelarPedido(int numeroPedido) {
        buscarPedido(numeroPedido); // Verifica que existe
        repositorio.eliminar(numeroPedido);
        System.out.println("Pedido #" + numeroPedido + " cancelado.");
    }

    /**
     * Exporta todos los pedidos a formato JSON.
     * Usa Gson (dependencia Maven) + Streams.
     *
     * En Spring: esto no se hace asi. Spring convierte a JSON
     * automaticamente con Jackson cuando devolvemos un objeto
     * desde un @RestController. Pero la idea es la misma.
     */
    public String exportarJSON() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        List<Map<String, Object>> datos = repositorio.buscarTodos().stream()
                .map(p -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("numero", p.getNumero());
                    m.put("cliente", p.getNombreCliente());
                    m.put("items", p.getCantidadItems());
                    m.put("total", p.calcularTotal());
                    return m;
                })
                .collect(Collectors.toList());

        return gson.toJson(datos);
    }
}
