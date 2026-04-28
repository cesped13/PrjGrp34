package es.usc.etse.grupo34.entidades;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

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

    @Test
    void debeReducirCantidadCuandoDescuentoEsValido() {
        stock.actualizarCantidad(3);

        assertEquals(7, stock.getCantidad());
        assertEquals(java.time.LocalDate.now(), stock.getUltimaActualizacion());
        verifyNoInteractions(maquinaMock, productoMock);
    }

    @Test
    void noDebeCambiarCantidadCuandoUnidadesEsCero() {
        stock.actualizarCantidad(0);

        assertEquals(10, stock.getCantidad());
        verifyNoInteractions(maquinaMock, productoMock);
    }

    @Test
    void debeLanzarIllegalArgumentExceptionCuandoUnidadesEsNegativoONulo() {
        assertThrows(IllegalArgumentException.class, () -> stock.actualizarCantidad(-1));
        assertThrows(IllegalArgumentException.class, () -> stock.actualizarCantidad(null));
        assertEquals(10, stock.getCantidad());
        verifyNoInteractions(maquinaMock, productoMock);
    }

    @Test
    void debeLanzarIllegalStateExceptionCuandoNoHayStockSuficiente() {
        stock = new StockMaquina(101L, maquinaMock, productoMock, 2, 1, 1.0);

        assertThrows(IllegalStateException.class, () -> stock.actualizarCantidad(3));
        assertEquals(2, stock.getCantidad());
        verifyNoInteractions(maquinaMock, productoMock);
    }

    @Test
    void necesitaReposicionDebeSerTrueCuandoAgotamientoEsHoy() {
        StockMaquina stockSpy = spy(stock);
        doReturn(java.time.LocalDate.now()).when(stockSpy).getFechaEstimadaAgotamiento();

        assertTrue(stockSpy.necesitaReposicion());
        verify(stockSpy, times(1)).getFechaEstimadaAgotamiento();
    }

    @Test
    void necesitaReposicionDebeSerFalseCuandoAgotamientoEsPasadoManana() {
        StockMaquina stockSpy = spy(stock);
        doReturn(java.time.LocalDate.now().plusDays(2)).when(stockSpy).getFechaEstimadaAgotamiento();

        assertFalse(stockSpy.necesitaReposicion());
        verify(stockSpy, atLeastOnce()).getFechaEstimadaAgotamiento();
        verify(stockSpy, atMost(1)).getFechaEstimadaAgotamiento();
    }

    @Test
    void getFechaEstimadaAgotamientoDebeCalcularConCeil() {
        stock = new StockMaquina(102L, maquinaMock, productoMock, 10, 1, 3.0);

        java.time.LocalDate esperada = java.time.LocalDate.now().plusDays(4);
        assertEquals(esperada, stock.getFechaEstimadaAgotamiento());
    }

    @Test
    void constructorDebeValidarParametrosObligatorios() {
        assertThrows(IllegalArgumentException.class, () -> new StockMaquina(null, maquinaMock, productoMock, 1, 1, 1.0));
        assertThrows(IllegalArgumentException.class, () -> new StockMaquina(1L, null, productoMock, 1, 1, 1.0));
        assertThrows(IllegalArgumentException.class, () -> new StockMaquina(1L, maquinaMock, null, 1, 1, 1.0));
        assertThrows(IllegalArgumentException.class, () -> new StockMaquina(1L, maquinaMock, productoMock, -1, 1, 1.0));
        assertThrows(IllegalArgumentException.class, () -> new StockMaquina(1L, maquinaMock, productoMock, 1, -1, 1.0));
        assertThrows(IllegalArgumentException.class, () -> new StockMaquina(1L, maquinaMock, productoMock, 1, 1, 0.0));
    }
}
