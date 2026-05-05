package es.usc.etse.grupo34.daos;

import es.usc.etse.grupo34.entidades.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ProductoDAO {

    private final List<Producto> productos;

    public ProductoDAO() {
        this.productos = new ArrayList<>();
    }

    public void add(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }

        boolean idDuplicado = productos.stream()
                .anyMatch(p -> p.getId().equals(producto.getId()));

        if (idDuplicado) {
            throw new IllegalArgumentException("Ya existe un producto con ese id");
        }

        productos.add(producto);
    }

    public Producto findById(Long id) {
        return productos.stream()
                .filter(producto -> producto.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No existe producto con id " + id));
    }

    public List<Producto> findAll() {
        return new ArrayList<>(productos);
    }
}
