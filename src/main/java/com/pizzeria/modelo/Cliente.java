package com.pizzeria.modelo;

/**
 * Entidad Cliente: representa a una persona o empresa que hace pedidos.
 *
 * Antes, el cliente era solo un String dentro de Pedido.
 * Ahora es una entidad propia con sus datos y su categoria de fidelidad.
 *
 * En Spring con JPA:
 *
 *   @Entity
 *   public class Cliente {
 *       @Id @GeneratedValue
 *       private int id;
 *
 *       @Enumerated(EnumType.STRING)
 *       private TipoCliente tipo;
 *
 *       @Enumerated(EnumType.STRING)
 *       private CategoriaCliente categoria;
 *
 *       @OneToMany(mappedBy = "cliente")
 *       private List<Pedido> pedidos;
 *   }
 */
public class Cliente {

    private static int contadorClientes = 0;

    private final int id;
    private final String nombre;
    private final TipoCliente tipo;
    private String email;
    private String telefono;
    private CategoriaCliente categoria;

    /**
     * TODO 1: Implementar el constructor.
     *
     * Debe:
     * - Validar que nombre no sea null ni vacio (lanzar IllegalArgumentException)
     * - Validar que tipo no sea null (lanzar IllegalArgumentException)
     * - Asignar id autoincremental (usar contadorClientes, igual que Pedido)
     * - Hacer trim() al nombre
     * - Asignar la categoria inicial como BRONCE (todo cliente empieza siendo nuevo)
     */
    public Cliente(String nombre, TipoCliente tipo) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de cliente es obligatorio");
        }

        this.id = ++contadorClientes;       // asignar id autoincremental
        this.nombre = nombre.trim();        // trim al nombre
        this.tipo = tipo;
        this.categoria = CategoriaCliente.BRONCE;  // todo cliente empieza como BRONCE
    }

    // --- Getters ---

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public TipoCliente getTipo() { return tipo; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public CategoriaCliente getCategoria() { return categoria; }

    // --- Setters (solo campos mutables) ---

    public void setEmail(String email) { this.email = email; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setCategoria(CategoriaCliente categoria) { this.categoria = categoria; }

    /**
     * TODO 2: Implementar toString().
     *
     * Formato esperado:
     *   [PERSONA] Ana Lopez (#1) - Bronce - Cliente nuevo
     *   [EMPRESA] Banco Santander (#3) - Oro - Cliente VIP - pedidos@santander.es - Tel: 911234567
     *
     * Pistas:
     * - Usa categoria.getDescripcion() para el texto de la categoria
     * - Solo muestra email y telefono si no son null ni vacios
     * - Mira como Combo.java usa StringBuilder
     */
    @Override
    public String toString() {
        // TODO 2: Implementar aqui
        return "Cliente{id=" + id + ", nombre=" + nombre + "}";
    }
}
