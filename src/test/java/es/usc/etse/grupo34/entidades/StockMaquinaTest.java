package es.usc.etse.grupo34.entidades;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StockMaquinaTest {

    @Test
    void debeReducirCantidadCuandoDescuentoEsValido() {
        StockMaquina stock = new StockMaquina(10);

        stock.actualizarCantidad(3);

        assertEquals(7, stock.getCantidad());
    }

    @Test
    void noDebeCambiarCantidadCuandoUnidadesEsCero() {
        StockMaquina stock = new StockMaquina(10);

        stock.actualizarCantidad(0);

        assertEquals(10, stock.getCantidad());
    }

    @Test
    void debeLanzarIllegalArgumentExceptionCuandoUnidadesEsNegativo() {
        StockMaquina stock = new StockMaquina(10);

        assertThrows(IllegalArgumentException.class, () -> stock.actualizarCantidad(-1));
        assertEquals(10, stock.getCantidad());
    }

    @Test
    void debeLanzarIllegalStateExceptionCuandoNoHayStockSuficiente() {
        StockMaquina stock = new StockMaquina(2);

        assertThrows(IllegalStateException.class, () -> stock.actualizarCantidad(3));
        assertEquals(2, stock.getCantidad());
    }
}
