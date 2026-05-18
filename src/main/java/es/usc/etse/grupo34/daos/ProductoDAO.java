package es.usc.etse.grupo34.daos;

import es.usc.etse.grupo34.entidades.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

// DAO en memoria para productos.
public class ProductoDAO {

    private final List<Producto> productos;

    public ProductoDAO() {
        this.productos = new ArrayList<>();
    }

    public void add(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }

        if (existeId(producto.getId())) {
            throw new IllegalArgumentException("Ya existe un producto con id " + producto.getId());
        }

        if (existeNombre(producto.getNombre())) {
            throw new IllegalArgumentException("Ya existe un producto con nombre '" + producto.getNombre() + "'");
        }

        productos.add(producto);
    }


    public Producto findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id de busqueda no puede ser nulo");
        }

        for (Producto producto : productos) {
            if (producto.getId().equals(id)) {
                return producto;
            }
        }

        throw new NoSuchElementException("No existe ningun producto con id " + id);
    }

    public List<Producto> findAll() {
        return new ArrayList<>(productos);
    }

    private boolean existeId(Long id) {
        for (Producto producto : productos) {
            if (producto.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private boolean existeNombre(String nombre) {
        for (Producto producto : productos) {
            if (producto.getNombre().equals(nombre)) {
                return true;
            }
        }
        return false;
    }
}
