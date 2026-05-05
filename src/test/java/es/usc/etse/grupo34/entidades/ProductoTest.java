package es.usc.etse.grupo34.entidades;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para {@link Producto} (HU-05, Sprint 1).
 *
 * Estrategia (IEEE 829) – Caja Negra, Clases de Equivalencia:
 *  CP1  – Constructor válido
 *  CP2/3 – nombre vacío / nulo
 *  CP4/5 – precio = 0 / negativo
 *  CP6/7 – id nulo / categoría vacía
 *  CP8/9 – setPrecio válido / inválido
 */
@DisplayName("Producto – pruebas unitarias (HU-05)")
class ProductoTest {

    // CP1 – Constructor válido
    @Test
    @DisplayName("CP1/CB1 – Constructor válido crea el producto con los datos correctos")
    void cp1_cb1_constructorValido_creaProducto() {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        assertAll("Atributos del producto",
                () -> assertEquals(1L,       producto.getId()),
                () -> assertEquals("Agua",   producto.getNombre()),
                () -> assertEquals(1.50,     producto.getPrecio(), 0.001),
                () -> assertEquals("Bebida", producto.getCategoria())
        );
    }

    // CP2 y CP3 – nombre vacío y nulo
    @ParameterizedTest(name = "nombre inválido [{0}] → IllegalArgumentException")
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("CP2/CP3/CB2 – Nombre nulo, vacío o solo espacios lanza IllegalArgumentException")
    void cp2_cp3_cb2_nombreInvalido_lanzaExcepcion(String nombre) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Producto(1L, nombre, 1.50, "Bebida")
        );
    }

    // CP4 y CP5 – precio = 0 y negativo
    @ParameterizedTest(name = "precio inválido {0} → IllegalArgumentException")
    @ValueSource(doubles = {0.0, -1.0})
    @DisplayName("CP4/CP5/CB3 – Precio cero o negativo lanza IllegalArgumentException")
    void cp4_cp5_cb3_precioInvalido_lanzaExcepcion(double precio) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Producto(1L, "Agua", precio, "Bebida")
        );
    }

    // CP6 y CP7 – id nulo y categoría vacía
    @Test
    @DisplayName("CP6/CP7/CB4 – Id nulo y categoría vacía lanzan IllegalArgumentException")
    void cp6_cp7_cb4_idNuloYCategoriaVacia_lanzan() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Producto(null, "Agua", 1.50, "Bebida"),
                        "CP6: id nulo"),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Producto(1L, "Agua", 1.50, ""),
                        "CP7: categoría vacía")
        );
    }

    // CP8 – setPrecio válido  /  CP9 – setPrecio con 0
    @Test
    @DisplayName("CP8/CP9/CB5 – setPrecio: actualiza precio válido y rechaza precio = 0")
    void cp8_cp9_cb5_setPrecio_validoEInvalido() {
        // Usamos spy para poder verificar la llamada al setter (CP8)
        Producto productoSpy = spy(new Producto(1L, "Agua", 1.50, "Bebida"));

        // CP8 – valor válido
        productoSpy.setPrecio(2.00);
        assertEquals(2.00, productoSpy.getPrecio(), 0.001, "CP8: precio actualizado");

        // Verificamos con InOrder que setPrecio fue llamado antes que getPrecio
        InOrder orden = inOrder(productoSpy);
        orden.verify(productoSpy).setPrecio(2.00);
        orden.verify(productoSpy).getPrecio();

        // CP9 – valor inválido: el precio no debe cambiar
        assertThrows(IllegalArgumentException.class, () -> productoSpy.setPrecio(0.0),
                "CP9: precio 0 debe lanzar excepción");
        assertEquals(2.00, productoSpy.getPrecio(), 0.001,
                "CP9: el precio no debe modificarse tras excepción");

        // El setter inválido no debió provocar una segunda llamada a getPrecio exitosa
        verify(productoSpy, times(2)).getPrecio(); // una en CP8, otra en CP9
    }

    // Caja Blanca (McCabe) – setPrecio: ramas adicionales

    @ParameterizedTest(name = "setPrecio inválido {0} → IllegalArgumentException")
    @ValueSource(doubles = {-1.0})
    @DisplayName("CB6 – setPrecio con precio negativo lanza IllegalArgumentException")
    void cb6_setPrecio_negativo_lanzaExcepcion(double precio) {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        assertThrows(IllegalArgumentException.class,
                () -> producto.setPrecio(precio),
                "CB6: precio negativo debe lanzar excepción");
    }

    @Test
    @DisplayName("CB7 – setPrecio con null lanza IllegalArgumentException")
    void cb7_setPrecio_null_lanzaExcepcion() {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        assertThrows(IllegalArgumentException.class,
                () -> producto.setPrecio(null),
                "CB7: precio null debe lanzar excepción");
    }
}
