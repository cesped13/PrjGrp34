package es.usc.etse.grupo34.daos;

import es.usc.etse.grupo34.entidades.Producto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductoDAOTest {

    @Test
    void addDebeGuardarProductoValido() {
        ProductoDAO dao = new ProductoDAO();
        Producto producto = mockProducto(1L, "Agua");

        dao.add(producto);

        List<Producto> productos = dao.findAll();
        assertEquals(1, productos.size());
        assertSame(producto, productos.get(0));
        verify(producto, atLeastOnce()).getId();
        verify(producto, atLeastOnce()).getNombre();
    }

    @Test
    void addDebeFallarSiProductoEsNulo() {
        ProductoDAO dao = new ProductoDAO();

        assertThrows(IllegalArgumentException.class, () -> dao.add(null));
    }

    @Test
    void addDebeFallarSiIdEstaDuplicado() {
        ProductoDAO dao = new ProductoDAO();
        Producto primero = mockProducto(1L, "Agua");
        Producto segundoConMismoId = mockProductoConId(1L);

        dao.add(primero);

        assertThrows(IllegalArgumentException.class, () -> dao.add(segundoConMismoId));
    }

    @Test
    void addDebeFallarSiNombreEstaDuplicado() {
        ProductoDAO dao = new ProductoDAO();
        Producto primero = mockProducto(1L, "Agua");
        Producto segundoConMismoNombre = mockProducto(2L, "Agua");

        dao.add(primero);

        assertThrows(IllegalArgumentException.class, () -> dao.add(segundoConMismoNombre));
    }

    @Test
    void findByIdDebeDevolverProductoSiExiste() {
        ProductoDAO dao = new ProductoDAO();
        Producto producto = mockProducto(9L, "Cafe");
        dao.add(producto);

        Producto encontrado = dao.findById(9L);

        assertSame(producto, encontrado);
    }

    @Test
    void findByIdDebeFallarSiNoExiste() {
        ProductoDAO dao = new ProductoDAO();
        dao.add(mockProducto(1L, "Agua"));

        assertThrows(NoSuchElementException.class, () -> dao.findById(999L));
    }

    @Test
    void findByIdDebeFallarSiIdEsNulo() {
        ProductoDAO dao = new ProductoDAO();

        assertThrows(IllegalArgumentException.class, () -> dao.findById(null));
    }

    @Test
    void findAllDebeDevolverListaVaciaSiNoHayDatos() {
        ProductoDAO dao = new ProductoDAO();

        List<Producto> productos = dao.findAll();

        assertTrue(productos.isEmpty());
    }

    @Test
    void findAllDebeDevolverCopiaDefensiva() {
        ProductoDAO dao = new ProductoDAO();
        Producto producto = mockProducto(7L, "Te");
        dao.add(producto);

        List<Producto> copia = dao.findAll();
        copia.clear();

        assertEquals(1, dao.findAll().size());
    }

    private Producto mockProducto(Long id, String nombre) {
        Producto producto = mock(Producto.class);
        when(producto.getId()).thenReturn(id);
        when(producto.getNombre()).thenReturn(nombre);
        return producto;
    }

    private Producto mockProductoConId(Long id) {
        Producto producto = mock(Producto.class);
        when(producto.getId()).thenReturn(id);
        return producto;
    }
}
