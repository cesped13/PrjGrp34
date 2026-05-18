package es.usc.etse.grupo34.entidades;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class StockMaquinaTest {

    @Mock
    private Maquina maquinaMock;

    @Mock
    private Producto productoMock;

    private StockMaquina stock;

    @BeforeEach
    void setUp() {
        stock = new StockMaquina(100L, maquinaMock, productoMock, 10, 2, 2.5);
    }

    // CP1: constructor con stock valido.
    @Test
    void constructorValidoDebeCrearStockMaquina() {
        assertEquals(100L, stock.getId());
        assertEquals(maquinaMock, stock.getMaquina());
        assertEquals(productoMock, stock.getProducto());
        assertEquals(10, stock.getCantidad());
        assertEquals(2, stock.getCantidadMinima());
        assertEquals(2.5, stock.getVelocidadConsumo());
        assertEquals(LocalDate.now(), stock.getUltimaActualizacion());

        verifyNoInteractions(maquinaMock, productoMock);
    }

    // CP2-CP7: constructor con parametros obligatorios nulos o invalidos.
    @Test
    void constructorDebeValidarParametrosObligatorios() {
        assertThrows(IllegalArgumentException.class,
                () -> new StockMaquina(null, maquinaMock, productoMock, 1, 1, 1.0));

        assertThrows(IllegalArgumentException.class,
                () -> new StockMaquina(1L, null, productoMock, 1, 1, 1.0));

        assertThrows(IllegalArgumentException.class,
                () -> new StockMaquina(1L, maquinaMock, null, 1, 1, 1.0));

        assertThrows(IllegalArgumentException.class,
                () -> new StockMaquina(1L, maquinaMock, productoMock, null, 1, 1.0));

        assertThrows(IllegalArgumentException.class,
                () -> new StockMaquina(1L, maquinaMock, productoMock, -1, 1, 1.0));

        assertThrows(IllegalArgumentException.class,
                () -> new StockMaquina(1L, maquinaMock, productoMock, 1, null, 1.0));

        assertThrows(IllegalArgumentException.class,
                () -> new StockMaquina(1L, maquinaMock, productoMock, 1, -1, 1.0));

        assertThrows(IllegalArgumentException.class,
                () -> new StockMaquina(1L, maquinaMock, productoMock, 1, 1, null));

        assertThrows(IllegalArgumentException.class,
                () -> new StockMaquina(1L, maquinaMock, productoMock, 1, 1, 0.0));

        assertThrows(IllegalArgumentException.class,
                () -> new StockMaquina(1L, maquinaMock, productoMock, 1, 1, -1.0));
    }

    // CP8: actualizarCantidad reduce la cantidad con unidades validas.
    @Test
    void actualizarCantidadDebeReducirCantidadCuandoUnidadesEsValido() {
        stock.actualizarCantidad(3);

        assertEquals(7, stock.getCantidad());
        assertEquals(LocalDate.now(), stock.getUltimaActualizacion());

        verifyNoInteractions(maquinaMock, productoMock);
    }

    // CP9: actualizarCantidad con cero mantiene la cantidad.
    @Test
    void actualizarCantidadNoDebeCambiarCantidadCuandoUnidadesEsCero() {
        stock.actualizarCantidad(0);

        assertEquals(10, stock.getCantidad());

        verifyNoInteractions(maquinaMock, productoMock);
    }

    // CP10 y CP11: actualizarCantidad rechaza unidades negativas o nulas.
    @Test
    void actualizarCantidadDebeLanzarExcepcionCuandoUnidadesEsNuloONegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> stock.actualizarCantidad(null));

        assertThrows(IllegalArgumentException.class,
                () -> stock.actualizarCantidad(-1));

        assertEquals(10, stock.getCantidad());

        verifyNoInteractions(maquinaMock, productoMock);
    }

    // CP12: actualizarCantidad rechaza consumo superior al stock.
    @Test
    void actualizarCantidadDebeLanzarExcepcionCuandoNoHayStockSuficiente() {
        assertThrows(IllegalStateException.class,
                () -> stock.actualizarCantidad(11));

        assertEquals(10, stock.getCantidad());

        verifyNoInteractions(maquinaMock, productoMock);
    }

    // CP15: getFechaEstimadaAgotamiento aplica redondeo hacia arriba.
    @Test
    void getFechaEstimadaAgotamientoDebeCalcularConCeil() {
        StockMaquina stock = new StockMaquina(101L, maquinaMock, productoMock, 10, 1, 3.0);

        assertEquals(LocalDate.now().plusDays(4), stock.getFechaEstimadaAgotamiento());
    }

    // CP13: agotamiento en la fecha actual.
    @Test
    void getFechaEstimadaAgotamientoDebeSerHoyCuandoCantidadEsCero() {
        StockMaquina stock = new StockMaquina(102L, maquinaMock, productoMock, 0, 1, 3.0);

        assertEquals(LocalDate.now(), stock.getFechaEstimadaAgotamiento());
    }

    // CP13: necesitaReposicion devuelve true para hoy o manana.
    @Test
    void necesitaReposicionDebeSerTrueCuandoAgotamientoEsHoyOMañana() {
        StockMaquina stockHoy = new StockMaquina(103L, maquinaMock, productoMock, 0, 1, 1.0);
        StockMaquina stockManana = new StockMaquina(104L, maquinaMock, productoMock, 1, 1, 1.0);

        assertTrue(stockHoy.necesitaReposicion());
        assertTrue(stockManana.necesitaReposicion());
    }

    // CP14: necesitaReposicion devuelve false despues de manana.
    @Test
    void necesitaReposicionDebeSerFalseCuandoAgotamientoEsDespuesDeManana() {
        StockMaquina stock = new StockMaquina(105L, maquinaMock, productoMock, 3, 1, 1.0);

        assertFalse(stock.necesitaReposicion());
    }
}
