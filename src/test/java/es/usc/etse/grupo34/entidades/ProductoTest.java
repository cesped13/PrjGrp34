package es.usc.etse.grupo34.entidades;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para {@link Producto} (HU-02, Sprint 1).
 *
 * Estrategia (IEEE 829):
 *  Caja Negra: CP1, CP4/5, CP8/9
 *  Caja Blanca (McCabe): ramas de validación restantes
 */
@DisplayName("Producto – pruebas unitarias (HU-02)")
class ProductoTest {

    @Test
    @DisplayName("CP1 – Constructor válido crea el producto con los datos correctos")
    void cp1_constructorValido_creaProducto() {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        assertAll("Atributos del producto",
                () -> assertEquals(1L, producto.getId()),
                () -> assertEquals("Agua", producto.getNombre()),
                () -> assertEquals(1.50, producto.getPrecio(), 0.001),
                () -> assertEquals("Bebida", producto.getCategoria())
        );
    }

    // CP4 y CP5 – precio = 0 y negativo
    @ParameterizedTest(name = "precio inválido {0} → IllegalArgumentException")
    @ValueSource(doubles = {0.0, -1.0})
    @DisplayName("CP4/CP5 – Precio cero o negativo lanza IllegalArgumentException")
    void cp4_cp5_precioInvalido_lanzaExcepcion(double precio) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Producto(1L, "Agua", precio, "Bebida")
        );
    }

    // CP8 – setPrecio válido  /  CP9 – setPrecio con 0
    @Test
    @DisplayName("CP8/CP9 – setPrecio: actualiza precio válido y rechaza precio = 0")
    void cp8_cp9_setPrecio_validoEInvalido() {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        // CP8 – valor válido
        producto.setPrecio(2.00);
        assertEquals(2.00, producto.getPrecio(), 0.001, "CP8: precio actualizado");

        // CP9 – valor inválido: el precio no debe cambiar
        assertThrows(IllegalArgumentException.class, () -> producto.setPrecio(0.0),
                "CP9: precio 0 debe lanzar excepción");
        assertEquals(2.00, producto.getPrecio(), 0.001,
                "CP9: el precio no debe modificarse tras excepción");
    }

    // Caja Blanca (McCabe) – ramas de validación restantes

    @Test
    @DisplayName("CB1 – Constructor con id nulo lanza IllegalArgumentException")
    void cb1_constructor_idNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Producto(null, "Agua", 1.50, "Bebida"));
    }

    @ParameterizedTest(name = "nombre inválido [{0}] → IllegalArgumentException")
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("CB2 – Constructor con nombre vacío o espacios lanza IllegalArgumentException")
    void cb2_constructor_nombreVacio_lanzaExcepcion(String nombre) {
        assertThrows(IllegalArgumentException.class,
                () -> new Producto(1L, nombre, 1.50, "Bebida"));
    }

    @Test
    @DisplayName("CB3 – Constructor con nombre null lanza IllegalArgumentException")
    void cb3_constructor_nombreNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Producto(1L, null, 1.50, "Bebida"));
    }

    @Test
    @DisplayName("CB4 – Constructor con precio null lanza IllegalArgumentException")
    void cb4_constructor_precioNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Producto(1L, "Agua", null, "Bebida"));
    }

    @ParameterizedTest(name = "categoría inválida [{0}] → IllegalArgumentException")
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("CB5 – Constructor con categoría vacía o espacios lanza IllegalArgumentException")
    void cb5_constructor_categoriaVacia_lanzaExcepcion(String categoria) {
        assertThrows(IllegalArgumentException.class,
                () -> new Producto(1L, "Agua", 1.50, categoria));
    }

    @Test
    @DisplayName("CB6 – Constructor con categoría null lanza IllegalArgumentException")
    void cb6_constructor_categoriaNula_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Producto(1L, "Agua", 1.50, null));
    }

    @ParameterizedTest(name = "setPrecio inválido {0} → IllegalArgumentException")
    @ValueSource(doubles = {-1.0})
    @DisplayName("CB7 – setPrecio con precio negativo lanza IllegalArgumentException")
    void cb7_setPrecio_negativo_lanzaExcepcion(double precio) {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        assertThrows(IllegalArgumentException.class,
                () -> producto.setPrecio(precio),
                "CB7: precio negativo debe lanzar excepción");
    }

    @Test
    @DisplayName("CB8 – setPrecio con null lanza IllegalArgumentException")
    void cb8_setPrecio_null_lanzaExcepcion() {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        assertThrows(IllegalArgumentException.class,
                () -> producto.setPrecio(null),
                "CB8: precio null debe lanzar excepción");
    }
}
