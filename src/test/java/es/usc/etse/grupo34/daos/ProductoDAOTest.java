package es.usc.etse.grupo34.daos;

import es.usc.etse.grupo34.entidades.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductoDAO – pruebas de integración")
class ProductoDAOTest {

    private ProductoDAO dao;

    @BeforeEach
    void setUp() {
        dao = new ProductoDAO();
    }

    @Test
    @DisplayName("add de producto válido: findAll devuelve exactamente un elemento")
    void addProductoValido_findAllDevuelveUnElemento() {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        dao.add(producto);

        List<Producto> lista = dao.findAll();
        assertEquals(1, lista.size());
        assertSame(producto, lista.get(0));
    }

    @Test
    @DisplayName("add(null) lanza IllegalArgumentException y el DAO queda vacío")
    void addNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> dao.add(null));
        assertTrue(dao.findAll().isEmpty());
    }

    @Test
    @DisplayName("add con id duplicado lanza IllegalArgumentException")
    void addIdDuplicado_lanzaExcepcion() {
        Producto primero = new Producto(1L, "Agua", 1.50, "Bebida");
        Producto duplicado = new Producto(1L, "Agua2", 2.00, "Bebida");

        dao.add(primero);

        assertThrows(IllegalArgumentException.class, () -> dao.add(duplicado));
        assertEquals(1, dao.findAll().size());
    }

    @Test
    @DisplayName("add con nombre duplicado lanza IllegalArgumentException")
    void addNombreDuplicado_lanzaExcepcion() {
        Producto primero = new Producto(1L, "Agua", 1.50, "Bebida");
        Producto duplicado = new Producto(2L, "Agua", 2.00, "Bebida");

        dao.add(primero);

        assertThrows(IllegalArgumentException.class, () -> dao.add(duplicado));
        assertEquals(1, dao.findAll().size());
    }

    @Test
    @DisplayName("findById devuelve producto existente")
    void findById_existente_devuelveProducto() {
        Producto producto = new Producto(9L, "Cafe", 1.80, "Bebida");
        dao.add(producto);

        Producto encontrado = dao.findById(9L);

        assertSame(producto, encontrado);
    }

    @Test
    @DisplayName("findById lanza NoSuchElementException si no existe")
    void findById_inexistente_lanzaExcepcion() {
        Producto producto = new Producto(9L, "Cafe", 1.80, "Bebida");
        dao.add(producto);

        assertThrows(NoSuchElementException.class, () -> dao.findById(999L));
    }

    @Test
    @DisplayName("findById(null) lanza IllegalArgumentException")
    void findById_idNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> dao.findById(null));
    }

    @Test
    @DisplayName("findAll devuelve lista vacía y copia defensiva")
    void findAll_vacioYCopiaDefensiva() {
        assertTrue(dao.findAll().isEmpty());

        dao.add(new Producto(7L, "Te", 1.20, "Bebida"));
        List<Producto> copia = dao.findAll();
        copia.clear();

        assertEquals(1, dao.findAll().size());
    }
}
