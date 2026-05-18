package es.usc.etse.grupo34.daos;

import es.usc.etse.grupo34.entidades.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("ProductoDAO – pruebas de integración (HU-02)")
class ProductoDAOTest {

    private ProductoDAO dao;

    @BeforeEach
    void setUp() {
        dao = new ProductoDAO();
    }

    // CP10: add valido y findById posterior.
    @Test
    @DisplayName("CP10 – add de producto válido: findById devuelve el producto")
    void cp10_addProductoValido_findByIdDevuelveProducto() {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        dao.add(producto);

        Producto encontrado = dao.findById(1L);
        assertSame(producto, encontrado, "findById debe devolver la misma instancia");
    }

    // CP12: add con producto nulo.
    @Test
    @DisplayName("add(null) lanza IllegalArgumentException y el DAO queda vacío")
    void addNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> dao.add(null));
        assertTrue(dao.findAll().isEmpty());
    }

    // CP14: findById con id inexistente.
    @Test
    @DisplayName("CP14 – findById: id inexistente lanza excepción")
    void cp14_findById_inexistente_lanzaExcepcion() {
        assertThrows(NoSuchElementException.class, () -> dao.findById(999L),
                "CP14: id inexistente debe lanzar NoSuchElementException");
    }

    // CP15: findAll con DAO vacio.
    @Test
    @DisplayName("CP15 – findAll: lista vacía cuando no hay productos")
    void cp15_findAll_vacio() {
        assertTrue(dao.findAll().isEmpty(), "CP15: findAll debe devolver lista vacía");
    }

    // CB1: add con id duplicado usando mocks.
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
