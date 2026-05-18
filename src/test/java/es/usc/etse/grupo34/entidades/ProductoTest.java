package es.usc.etse.grupo34.entidades;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Producto – pruebas unitarias (HU-02)")
class ProductoTest {

    // CP1: constructor con producto valido.
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

    // CP4 y CP5: constructor con precio cero o negativo.
    @ParameterizedTest(name = "precio inválido {0} → IllegalArgumentException")
    @ValueSource(doubles = {0.0, -1.0})
    @DisplayName("CP4/CP5 – Precio cero o negativo lanza IllegalArgumentException")
    void cp4_cp5_precioInvalido_lanzaExcepcion(double precio) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Producto(1L, "Agua", precio, "Bebida")
        );
    }

    // CP8 y CP9: setPrecio valido y setPrecio con cero.
    @Test
    @DisplayName("CP8/CP9 – setPrecio: actualiza precio válido y rechaza precio = 0")
    void cp8_cp9_setPrecio_validoEInvalido() {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        producto.setPrecio(2.00);
        assertEquals(2.00, producto.getPrecio(), 0.001, "CP8: precio actualizado");

        assertThrows(IllegalArgumentException.class, () -> producto.setPrecio(0.0),
                "CP9: precio 0 debe lanzar excepción");
        assertEquals(2.00, producto.getPrecio(), 0.001,
                "CP9: el precio no debe modificarse tras excepción");
    }

    // CB1: constructor con id nulo.
    @Test
    @DisplayName("CB1 – Constructor con id nulo lanza IllegalArgumentException")
    void cb1_constructor_idNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Producto(null, "Agua", 1.50, "Bebida"));
    }

    // CP2 y CP9: constructor con nombre vacio o en blanco.
    @ParameterizedTest(name = "nombre inválido [{0}] → IllegalArgumentException")
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("CB2 – Constructor con nombre vacío o espacios lanza IllegalArgumentException")
    void cb2_constructor_nombreVacio_lanzaExcepcion(String nombre) {
        assertThrows(IllegalArgumentException.class,
                () -> new Producto(1L, nombre, 1.50, "Bebida"));
    }

    // CP3: constructor con nombre nulo.
    @Test
    @DisplayName("CB3 – Constructor con nombre null lanza IllegalArgumentException")
    void cb3_constructor_nombreNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Producto(1L, null, 1.50, "Bebida"));
    }

    // CP11: constructor con precio nulo.
    @Test
    @DisplayName("CB4 – Constructor con precio null lanza IllegalArgumentException")
    void cb4_constructor_precioNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Producto(1L, "Agua", null, "Bebida"));
    }

    // CP7 y CP10: constructor con categoria vacia o en blanco.
    @ParameterizedTest(name = "categoría inválida [{0}] → IllegalArgumentException")
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("CB5 – Constructor con categoría vacía o espacios lanza IllegalArgumentException")
    void cb5_constructor_categoriaVacia_lanzaExcepcion(String categoria) {
        assertThrows(IllegalArgumentException.class,
                () -> new Producto(1L, "Agua", 1.50, categoria));
    }

    // CP8: constructor con categoria nula.
    @Test
    @DisplayName("CB6 – Constructor con categoría null lanza IllegalArgumentException")
    void cb6_constructor_categoriaNula_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Producto(1L, "Agua", 1.50, null));
    }

    // CP10: setPrecio con precio negativo.
    @ParameterizedTest(name = "setPrecio inválido {0} → IllegalArgumentException")
    @ValueSource(doubles = {-1.0})
    @DisplayName("CB7 – setPrecio con precio negativo lanza IllegalArgumentException")
    void cb7_setPrecio_negativo_lanzaExcepcion(double precio) {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        assertThrows(IllegalArgumentException.class,
                () -> producto.setPrecio(precio),
                "CB7: precio negativo debe lanzar excepción");
    }

    // CP17: setPrecio con precio nulo.
    @Test
    @DisplayName("CB8 – setPrecio con null lanza IllegalArgumentException")
    void cb8_setPrecio_null_lanzaExcepcion() {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        assertThrows(IllegalArgumentException.class,
                () -> producto.setPrecio(null),
                "CB8: precio null debe lanzar excepción");
    }
}
