package com.pizzeria.servicio;
import com.pizzeria.excepcion.ClienteNoEncontradoException;
import com.pizzeria.modelo.CategoriaCliente;
import com.pizzeria.modelo.Cliente;
import com.pizzeria.modelo.Pedido;
import com.pizzeria.modelo.TipoCliente;
import com.pizzeria.repositorio.ClienteRepository;
import com.pizzeria.repositorio.PedidoRepository;
import java.util.List;
import java.util.Map;

/**
 * Servicio de clientes: logica de negocio para gestionar clientes.
 *
 * NOVEDAD: este servicio tiene DOS dependencias (ClienteRepository + PedidoRepository).
 * Necesita ambos porque para clasificar un cliente necesita contar sus pedidos.
 *
 * En Spring:
 *   @Service
 *   public class ClienteService {
 *       @Autowired
 *       public ClienteService(ClienteRepository clienteRepo, PedidoRepository pedidoRepo) { ... }
 *   }
 */
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;

    public ClienteService(ClienteRepository clienteRepository,
                          PedidoRepository pedidoRepository) {
        this.clienteRepository = clienteRepository;
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Registra un nuevo cliente en el sistema.
     */
    public Cliente registrarCliente(String nombre, TipoCliente tipo) {
        Cliente cliente = new Cliente(nombre, tipo);
        return clienteRepository.guardar(cliente);
    }

    /**
     * Busca un cliente por su id. Lanza excepcion si no existe.
     * (Mismo patron que PedidoService.buscarPedido)
     */
    public Cliente buscarCliente(int id) {
        return clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new ClienteNoEncontradoException(id));
    }


    /**
     * Lista todos los clientes registrados.
     */
    public List<Cliente> listarTodos() {
        return clienteRepository.buscarTodos();
    }

    /**
     * Busca clientes de tipo EMPRESA.
     */
    public List<Cliente> buscarEmpresas() {
        return clienteRepository.buscarPorTipo(TipoCliente.EMPRESA);
    }

    /**
     * Busca clientes de tipo PERSONA.
     */
    public List<Cliente> buscarPersonas() {
        return clienteRepository.buscarPorTipo(TipoCliente.PERSONA);
    }


    /**
     * TODO 1: Implementar actualizarCategoria(int idCliente)
     *
     * Esta logica estaba antes en PedidoService.clasificarCliente().
     * Se movio aqui porque pertenece al dominio del CLIENTE, no del pedido.
     *
     * Pasos:
     * 1. Buscar el cliente con buscarCliente(idCliente)
     * 2. Buscar sus pedidos con pedidoRepository.buscarPorCliente(idCliente)
     * 3. Contar cuantos pedidos tiene
     * 4. Aplicar las reglas de negocio:
     *    - 1 pedido: CategoriaCliente.BRONCE
     *    - 2-3 pedidos: CategoriaCliente.PLATA
     *    - 4+ pedidos: CategoriaCliente.ORO
     * 5. Actualizar la categoria del cliente con cliente.setCategoria(nuevaCategoria)
     * 6. Guardar el cliente con clienteRepository.guardar(cliente)
     */
    public void actualizarCategoria(int idCliente) {
        Cliente cliente = buscarCliente(idCliente);

        // 2. Buscar sus pedidos
        List pedidosCliente = pedidoRepository.buscarPorCliente(idCliente);

        // 3. Contar pedidos
        int cantidadPedidos = pedidosCliente.size();

        // 4. Aplicar reglas de negocio
        if (cantidadPedidos >= 4) {
            cliente.setCategoria(CategoriaCliente.ORO);
        } else if (cantidadPedidos >= 2) {
            cliente.setCategoria(CategoriaCliente.PLATA);
        } else {
            cliente.setCategoria(CategoriaCliente.BRONCE);
        }
        // 5. Guardar cambios
        clienteRepository.guardar(cliente);
    }

    public String obtenerRankingClientes() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RANKING DE CLIENTES ===\n");
        clienteRepository.buscarTodos().stream()
                .map(cliente -> {
                    double gasto = pedidoRepository.buscarPorCliente(cliente.getId())
                            .stream()
                            .mapToDouble(Pedido::calcularTotal)
                            .sum();
                    return Map.entry(cliente, gasto);
                })
                .filter(entry -> entry.getValue() > 0)
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .forEach(entry -> sb.append(String.format(
                        "  %s → %.2f€%n",
                        entry.getKey().getNombre(), entry.getValue())));

        sb.append("===========================\n");
        return sb.toString();
    }

    /**
     * TODO 2: Implementar generarInformeCliente(int idCliente)
     *
     * Debe devolver un String con formato:
     *   === INFORME CLIENTE ===
     *     [PERSONA] Ana Lopez (#1) - Plata - Cliente frecuente
     *     Pedidos realizados: 2
     *     Gasto total: 26.00€
     *   =======================
     *
     * Pasos:
     * 1. Buscar el cliente
     * 2. Buscar sus pedidos
     * 3. Calcular gasto total (suma de calcularTotal() de cada pedido)
     * 4. Formatear con String.format()
     */
    public String generarInformeCliente(int idCliente) {
        // 1. Buscar el cliente
        Cliente cliente = buscarCliente(idCliente);

        // 2. Buscar sus pedidos (tipado correcto)
        List<Pedido> pedidosCliente = pedidoRepository.buscarPorCliente(idCliente);

        // 3. Contar pedidos y calcular gasto total
        int cantidadPedidos = pedidosCliente.size();
        double gastoTotal = pedidosCliente.stream()
                .mapToDouble(Pedido::calcularTotal)
                .sum();

        // 4. Generar informe
        StringBuilder sb = new StringBuilder();
        sb.append("=== INFORME CLIENTE ===\n");
        sb.append("  ").append(cliente).append("\n");
        sb.append("  Pedidos realizados: ").append(cantidadPedidos).append("\n");
        sb.append(String.format("  Gasto total: %.2f€%n", gastoTotal));
        sb.append("=======================\n");

        return sb.toString();
    }
}
