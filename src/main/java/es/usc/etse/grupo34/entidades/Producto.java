package es.usc.etse.grupo34.entidades;

/**
 * Entidad de dominio para la HU-02.
 *
 * Si tu proyecto usa paquetes, añade aquí el package correspondiente.
 */
public class Producto {

    private final Long id;
    private final String nombre;
    private double precio;
    private final String categoria;

    public Producto(Long id, String nombre, double precio, String categoria) {
        this.id = validarId(id);
        this.nombre = validarTexto(nombre, "nombre");
        this.precio = validarPrecio(precio);
        this.categoria = validarTexto(categoria, "categoria");
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setPrecio(double precio) {
        this.precio = validarPrecio(precio);
    }

    private static Long validarId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id del producto no puede ser nulo");
        }
        return id;
    }

    private static String validarTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El " + campo + " del producto no puede estar vacio");
        }
        return texto.trim();
    }

    private static double validarPrecio(double precio) {
        if (Double.isNaN(precio) || Double.isInfinite(precio) || precio <= 0) {
            throw new IllegalArgumentException("El precio del producto debe ser estrictamente positivo");
        }
        return precio;
    }
}
