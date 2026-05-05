package es.usc.etse.grupo34.daos;

import es.usc.etse.grupo34.entidades.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para {@link ProductoDAO} (HU-05, Sprint 1).
 *
 * Estrategia (IEEE 829) – Caja Negra, Clases de Equivalencia:
 *  CP10 – add válido: findAll devuelve el producto
 *  CP11 – add(null) lanza IllegalArgumentException
 *  CP12 – add con id duplicado lanza IllegalArgumentException
 *  CP13/14 – findById: existente devuelve producto / inexistente lanza excepción
 *  CP15 – findAll vacío + copia defensiva
 */
@DisplayName("ProductoDAO – pruebas de integración (HU-05)")
class ProductoDAOTest {

    private ProductoDAO dao;

    @BeforeEach
    void setUp() {
        dao = new ProductoDAO();
    }

    // CP10 – add válido: producto almacenado, findAll devuelve 1 elemento
    @Test
    @DisplayName("CP10 – add de producto válido: findAll devuelve exactamente un elemento")
    void cp10_addProductoValido_findAllDevuelveUnElemento() {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        dao.add(producto);

        List<Producto> lista = dao.findAll();
        assertEquals(1, lista.size(), "findAll debe devolver 1 elemento tras add");
        assertSame(producto, lista.get(0), "Debe ser la misma instancia");
    }

    // CP11 – add(null) lanza excepción
    @Test
    @DisplayName("CP11 – add(null) lanza IllegalArgumentException y el DAO queda vacío")
    void cp11_addNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> dao.add(null));
        assertTrue(dao.findAll().isEmpty(), "El DAO debe permanecer vacío");
    }

    // CP12 – id duplicado lanza excepción
    @Test
    @DisplayName("CP12 – add con id duplicado lanza IllegalArgumentException")
    void cp12_addIdDuplicado_lanzaExcepcion() {
        Producto primero = new Producto(1L, "Agua", 1.50, "Bebida");
        Producto duplicado = new Producto(1L, "Agua2", 2.00, "Bebida");

        dao.add(primero);

        assertThrows(IllegalArgumentException.class, () -> dao.add(duplicado));
        assertEquals(1, dao.findAll().size(), "El DAO no debe aceptar el duplicado");
    }

    // CP13 – findById existente  /  CP14 – findById inexistente
    @Test
    @DisplayName("CP13/CP14 – findById: devuelve producto existente y lanza excepción si no existe")
    void cp13_cp14_findById_existenteEInexistente() {
        Producto producto = new Producto(9L, "Café", 1.80, "Bebida");
        dao.add(producto);

        // CP13 – id existente
        Producto encontrado = dao.findById(9L);
        assertSame(producto, encontrado, "CP13: debe devolver la instancia correcta");

        // CP14 – id inexistente
        assertThrows(NoSuchElementException.class, () -> dao.findById(999L),
                "CP14: id inexistente debe lanzar NoSuchElementException");
    }

    // CP15 – findAll vacío y copia defensiva
    @Test
    @DisplayName("CP15 – findAll: lista vacía y copia defensiva no afecta al DAO")
    void cp15_findAll_vacioYCopiaDefensiva() {
        // Lista vacía
        assertTrue(dao.findAll().isEmpty(), "CP15: findAll debe devolver lista vacía");

        // Copia defensiva: mutar la lista devuelta no altera el DAO
        dao.add(new Producto(7L, "Té", 1.20, "Bebida"));
        List<Producto> copia = dao.findAll();
        copia.clear();
        assertEquals(1, dao.findAll().size(),
                "CP15: la lista interna no debe verse afectada por modificaciones externas");
    }
}
