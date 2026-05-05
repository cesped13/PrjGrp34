package es.usc.etse.grupo34.entidades;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Producto – pruebas unitarias")
class ProductoTest {

    @Test
    @DisplayName("Constructor válido crea el producto con los datos correctos")
    void constructorValido_creaProducto() {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        assertAll("Atributos del producto",
                () -> assertEquals(1L, producto.getId()),
                () -> assertEquals("Agua", producto.getNombre()),
                () -> assertEquals(1.50, producto.getPrecio(), 0.001),
                () -> assertEquals("Bebida", producto.getCategoria())
        );
    }

    @Test
    @DisplayName("Constructor recorta nombre y categoría")
    void constructor_recortaNombreYCategoria() {
        Producto producto = new Producto(2L, "  Zumo  ", 2.0, "  Bebida fria  ");

        assertEquals("Zumo", producto.getNombre());
        assertEquals("Bebida fria", producto.getCategoria());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Nombre nulo, vacío o solo espacios lanza IllegalArgumentException")
    void nombreInvalido_lanzaExcepcion(String nombre) {
        assertThrows(IllegalArgumentException.class,
                () -> new Producto(1L, nombre, 1.50, "Bebida"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Categoría nula, vacía o solo espacios lanza IllegalArgumentException")
    void categoriaInvalida_lanzaExcepcion(String categoria) {
        assertThrows(IllegalArgumentException.class,
                () -> new Producto(1L, "Agua", 1.50, categoria));
    }

    @Test
    @DisplayName("Id nulo lanza IllegalArgumentException")
    void idNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Producto(null, "Agua", 1.50, "Bebida"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(doubles = {0.0, -1.0, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    @DisplayName("Precio inválido lanza IllegalArgumentException")
    void precioInvalido_lanzaExcepcion(Double precio) {
        assertThrows(IllegalArgumentException.class,
                () -> new Producto(1L, "Agua", precio, "Bebida"));
    }

    @Test
    @DisplayName("setPrecio actualiza precio válido")
    void setPrecio_valido_actualiza() {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        producto.setPrecio(2.00);

        assertEquals(2.00, producto.getPrecio(), 0.001);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(doubles = {0.0, -1.0, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    @DisplayName("setPrecio rechaza precio inválido")
    void setPrecio_invalido_lanzaExcepcion(Double precio) {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        assertThrows(IllegalArgumentException.class,
                () -> producto.setPrecio(precio));
    }
}
