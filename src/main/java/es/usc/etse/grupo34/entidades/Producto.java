package es.usc.etse.grupo34.entidades;

public class Producto {

    private final Long id;
    private final String nombre;
    private Double precio;
    private final String categoria;

    public Producto(Long id, String nombre, Double precio, String categoria) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio");
        }
        if (precio == null || precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero");
        }
        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("La categoria no puede estar vacia");
        }

        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
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
        if (precio == null || precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero");
        }
        this.precio = precio;
    }
}
