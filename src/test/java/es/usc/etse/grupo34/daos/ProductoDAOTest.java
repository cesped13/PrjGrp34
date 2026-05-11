package es.usc.etse.grupo34.daos;

import es.usc.etse.grupo34.entidades.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas de integración para {@link ProductoDAO} (HU-02, Sprint 1).
 *
 * Estrategia (IEEE 829):
 *  Caja Negra: CP10, CP11, CP14, CP15
 *  Caja Blanca (McCabe): control de duplicados (CP12)
 */
@DisplayName("ProductoDAO – pruebas de integración (HU-02)")
class ProductoDAOTest {

    private ProductoDAO dao;

    @BeforeEach
    void setUp() {
        dao = new ProductoDAO();
    }

    // CP10 – add válido: producto almacenado y recuperable
    @Test
    @DisplayName("CP10 – add de producto válido: findById devuelve el producto")
    void cp10_addProductoValido_findByIdDevuelveProducto() {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        dao.add(producto);

        Producto encontrado = dao.findById(1L);
        assertSame(producto, encontrado, "findById debe devolver la misma instancia");
    }

    @Test
    @DisplayName("add(null) lanza IllegalArgumentException y el DAO queda vacío")
    void addNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> dao.add(null));
        assertTrue(dao.findAll().isEmpty());
    }

    // CP14 – findById inexistente
    @Test
    @DisplayName("CP14 – findById: id inexistente lanza excepción")
    void cp14_findById_inexistente_lanzaExcepcion() {
        assertThrows(NoSuchElementException.class, () -> dao.findById(999L),
                "CP14: id inexistente debe lanzar NoSuchElementException");
    }

    // CP15 – findAll vacío
    @Test
    @DisplayName("CP15 – findAll: lista vacía cuando no hay productos")
    void cp15_findAll_vacio() {
        assertTrue(dao.findAll().isEmpty(), "CP15: findAll debe devolver lista vacía");
    }

    // Caja Blanca (McCabe) – control de duplicados
    @Test
    @DisplayName("CB1 – add con id duplicado consulta getId y lanza IllegalArgumentException")
    void cb1_addIdDuplicado_consultaGetIdYLanzaExcepcion() {
        Producto existente = mock(Producto.class);
        Producto duplicado = mock(Producto.class);

        when(existente.getId()).thenReturn(1L);
        when(duplicado.getId()).thenReturn(1L);

        dao.add(existente);

        assertThrows(IllegalArgumentException.class, () -> dao.add(duplicado));

        InOrder orden = inOrder(existente, duplicado);
        orden.verify(existente, atLeastOnce()).getId();
        orden.verify(duplicado, atLeastOnce()).getId();
    }
}

