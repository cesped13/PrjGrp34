package es.usc.etse.grupo34.entidades;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductoTest {

    @Test
    void constructorDebeCrearProductoConDatosValidos() {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        assertEquals(1L, producto.getId());
        assertEquals("Agua", producto.getNombre());
        assertEquals(1.50, producto.getPrecio());
        assertEquals("Bebida", producto.getCategoria());
    }

    @Test
    void constructorDebeRecortarNombreYCategoria() {
        Producto producto = new Producto(2L, "  Zumo  ", 2.0, "  Bebida fria  ");

        assertEquals("Zumo", producto.getNombre());
        assertEquals("Bebida fria", producto.getCategoria());
    }

    @Test
    void constructorDebeFallarSiIdEsNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Producto(null, "Agua", 1.50, "Bebida"));
    }

    @Test
    void constructorDebeFallarSiNombreEsVacio() {
        assertThrows(IllegalArgumentException.class, () -> new Producto(1L, "", 1.50, "Bebida"));
    }

    @Test
    void constructorDebeFallarSiNombreEsNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Producto(1L, null, 1.50, "Bebida"));
    }

    @Test
    void constructorDebeFallarSiNombreEsSoloEspacios() {
        assertThrows(IllegalArgumentException.class, () -> new Producto(1L, "   ", 1.50, "Bebida"));
    }

    @Test
    void constructorDebeFallarSiCategoriaEsVacia() {
        assertThrows(IllegalArgumentException.class, () -> new Producto(1L, "Agua", 1.50, ""));
    }

    @Test
    void constructorDebeFallarSiCategoriaEsNula() {
        assertThrows(IllegalArgumentException.class, () -> new Producto(1L, "Agua", 1.50, null));
    }

    @Test
    void constructorDebeFallarSiCategoriaEsSoloEspacios() {
        assertThrows(IllegalArgumentException.class, () -> new Producto(1L, "Agua", 1.50, "  "));
    }

    @Test
    void constructorDebeFallarSiPrecioEsCero() {
        assertThrows(IllegalArgumentException.class, () -> new Producto(1L, "Agua", 0.0, "Bebida"));
    }

    @Test
    void constructorDebeFallarSiPrecioEsNegativo() {
        assertThrows(IllegalArgumentException.class, () -> new Producto(1L, "Agua", -1.0, "Bebida"));
    }

    @Test
    void constructorDebeFallarSiPrecioEsNaN() {
        assertThrows(IllegalArgumentException.class, () -> new Producto(1L, "Agua", Double.NaN, "Bebida"));
    }

    @Test
    void constructorDebeFallarSiPrecioEsInfinito() {
        assertThrows(IllegalArgumentException.class, () -> new Producto(1L, "Agua", Double.POSITIVE_INFINITY, "Bebida"));
    }

    @Test
    void setPrecioDebeActualizarCuandoEsValido() {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        producto.setPrecio(2.00);

        assertEquals(2.00, producto.getPrecio());
    }

    @Test
    void setPrecioDebeFallarSiEsCero() {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        assertThrows(IllegalArgumentException.class, () -> producto.setPrecio(0.0));
    }

    @Test
    void setPrecioDebeFallarSiEsNegativo() {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        assertThrows(IllegalArgumentException.class, () -> producto.setPrecio(-0.01));
    }

    @Test
    void setPrecioDebeFallarSiEsNaN() {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        assertThrows(IllegalArgumentException.class, () -> producto.setPrecio(Double.NaN));
    }
}
