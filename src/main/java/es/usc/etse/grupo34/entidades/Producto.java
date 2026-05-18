package es.usc.etse.grupo34.entidades;

// Representa un producto del catalogo.
public class Producto {

    private final Long id;
    private final String nombre;
    private Double precio;
    private final String categoria;

    public Producto(Long id, String nombre, Double precio, String categoria) {
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

    public Double getPrecio() {
        return precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setPrecio(Double precio) {
        this.precio = validarPrecio(precio);
    }

    private static Long validarId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        return id;
    }

    private static String validarTexto(String texto, String campo) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("El " + campo + " no puede estar vacio");
        }
        return texto.trim();
    }

    private static Double validarPrecio(Double precio) {
        if (precio == null || Double.isNaN(precio) || Double.isInfinite(precio) || precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero");
        }
        return precio;
    }
}
