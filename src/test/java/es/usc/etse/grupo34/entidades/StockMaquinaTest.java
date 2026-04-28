package es.usc.etse.grupo34.entidades;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para StockMaquina (HU-03, Sprint 1).
 *
 * Estrategia:
 *  - Caja Negra (CE) sobre el constructor y los métodos de negocio.
 *  - Mocks de Maquina y Producto para aislar la unidad bajo prueba.
 *  - Mockito se usa para verificar que no se producen llamadas
 *    inesperadas sobre los colaboradores.
 */
@DisplayName("StockMaquina – pruebas unitarias")
class StockMaquinaTest {

    // fixtures

    AutoCloseable acl;

    @Mock
    Maquina maquinaMock;

    @Mock
    Producto productoMock;

    @BeforeEach
    void setUp() throws Exception {
        acl = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        acl.close();
    }

    // CP1: constructor válido

    @Test
    @DisplayName("CP1 – Constructor válido crea objeto con los datos correctos")
    void constructorValido_creaObjeto() {
        StockMaquina stock = new StockMaquina(1L, maquinaMock, productoMock, 10, 2, 1.0);

        assertAll("Atributos del objeto creado",
                () -> assertEquals(1L,          stock.getId(),              "El id debe coincidir"),
                () -> assertSame(maquinaMock,   stock.getMaquina(),         "La máquina debe ser la misma instancia"),
                () -> assertSame(productoMock,  stock.getProducto(),        "El producto debe ser la misma instancia"),
                () -> assertEquals(10,          stock.getCantidad(),        "La cantidad debe ser 10"),
                () -> assertEquals(2,           stock.getCantidadMinima(),  "La cantidad mínima debe ser 2"),
                () -> assertEquals(1.0,         stock.getVelocidadConsumo(),"La velocidad de consumo debe ser 1.0"),
                () -> assertNotNull(stock.getUltimaActualizacion(),         "La fecha de actualización no puede ser nula")
        );
    }

    // CP2: maquina nula

    @Test
    @DisplayName("CP2 – Constructor lanza excepción cuando la máquina es nula")
    void constructorConMaquinaNula_lanzaIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new StockMaquina(1L, null, productoMock, 10, 2, 1.0),
                "Debe lanzar IllegalArgumentException si la máquina es nula"
        );
        assertNotNull(ex.getMessage(), "El mensaje de la excepción no puede ser nulo");
    }

    // CP3: producto nulo

    @Test
    @DisplayName("CP3 – Constructor lanza excepción cuando el producto es nulo")
    void constructorConProductoNulo_lanzaIllegalArgumentException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new StockMaquina(1L, maquinaMock, null, 10, 2, 1.0),
                "Debe lanzar IllegalArgumentException si el producto es nulo"
        );
    }

    // CP4: cantidad negativa

    @Test
    @DisplayName("CP4 – Constructor lanza excepción con cantidad negativa")
    void constructorConCantidadNegativa_lanzaIllegalArgumentException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new StockMaquina(1L, maquinaMock, productoMock, -1, 2, 1.0),
                "Debe lanzar IllegalArgumentException si la cantidad es negativa"
        );
    }

    // CP5: cantidadMinima negativa

    @Test
    @DisplayName("CP5 – Constructor lanza excepción con cantidadMinima negativa")
    void constructorConCantidadMinimaNegativa_lanzaIllegalArgumentException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new StockMaquina(1L, maquinaMock, productoMock, 10, -1, 1.0),
                "Debe lanzar IllegalArgumentException si la cantidad mínima es negativa"
        );
    }

    // velocidadConsumo <= 0

    @ParameterizedTest(name = "velocidadConsumo = {0} → IllegalArgumentException")
    @ValueSource(doubles = {0.0, -1.0, -100.0})
    @DisplayName("Constructor lanza excepción con velocidadConsumo <= 0")
    void constructorConVelocidadNoPositiva_lanzaIllegalArgumentException(double vel) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new StockMaquina(1L, maquinaMock, productoMock, 10, 2, vel),
                "Debe lanzar IllegalArgumentException si la velocidad de consumo no es positiva"
        );
    }

    // id nulo

    @Test
    @DisplayName("Constructor lanza excepción cuando el id es nulo")
    void constructorConIdNulo_lanzaIllegalArgumentException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new StockMaquina(null, maquinaMock, productoMock, 10, 2, 1.0),
                "Debe lanzar IllegalArgumentException si el id es nulo"
        );
    }

    // actualizarCantidad – válido

    @ParameterizedTest(name = "descontar {1} de {0} → queda {2}")
    @CsvSource({"10, 5, 5", "10, 10, 0", "10, 0, 10"})
    @DisplayName("actualizarCantidad reduce la cantidad correctamente")
    void actualizarCantidad_valido(int cantidadInicial, int unidades, int esperado) {
        StockMaquina stock = new StockMaquina(1L, maquinaMock, productoMock,
                cantidadInicial, 0, 1.0);
        stock.actualizarCantidad(unidades);
        assertEquals(esperado, stock.getCantidad(), "La cantidad resultante no es la esperada");
    }

    // actualizarCantidad – unidades negativas

    @Test
    @DisplayName("actualizarCantidad lanza IllegalArgumentException con unidades negativas")
    void actualizarCantidad_unidadesNegativas_lanzaIllegalArgumentException() {
        StockMaquina stock = new StockMaquina(1L, maquinaMock, productoMock, 10, 2, 1.0);
        assertThrows(
                IllegalArgumentException.class,
                () -> stock.actualizarCantidad(-1),
                "Debe lanzar IllegalArgumentException si las unidades son negativas"
        );
    }

    // actualizarCantidad – stock insuficiente

    @Test
    @DisplayName("actualizarCantidad lanza IllegalStateException si la cantidad resultante sería negativa")
    void actualizarCantidad_cantidadInsuficiente_lanzaIllegalStateException() {
        StockMaquina stock = new StockMaquina(1L, maquinaMock, productoMock, 5, 0, 1.0);
        assertThrows(
                IllegalStateException.class,
                () -> stock.actualizarCantidad(6),
                "Debe lanzar IllegalStateException si la cantidad quedaría por debajo de cero"
        );
    }

    // actualizarCantidad actualiza la fecha

    @Test
    @DisplayName("actualizarCantidad actualiza la fecha de última actualización a hoy")
    void actualizarCantidad_actualizaFecha() {
        StockMaquina stock = new StockMaquina(1L, maquinaMock, productoMock, 10, 0, 1.0);
        stock.actualizarCantidad(3);
        assertEquals(LocalDate.now(), stock.getUltimaActualizacion(),
                "La fecha de actualización debe ser hoy tras actualizar");
    }

    // getFechaEstimadaAgotamiento

    @Test
    @DisplayName("getFechaEstimadaAgotamiento calcula correctamente la fecha")
    void getFechaEstimadaAgotamiento_calculoCorrecto() {
        // cantidad=10, velocidad=2.0  →  10/2 = 5 días desde hoy
        StockMaquina stock = new StockMaquina(1L, maquinaMock, productoMock, 10, 0, 2.0);
        LocalDate esperado = LocalDate.now().plusDays(5);
        assertEquals(esperado, stock.getFechaEstimadaAgotamiento(),
                "La fecha estimada de agotamiento no es la esperada");
    }

    @Test
    @DisplayName("getFechaEstimadaAgotamiento con cantidad 0 devuelve hoy")
    void getFechaEstimadaAgotamiento_cantidadCero_devuelveHoy() {
        StockMaquina stock = new StockMaquina(1L, maquinaMock, productoMock, 0, 0, 1.0);
        assertEquals(LocalDate.now(), stock.getFechaEstimadaAgotamiento(),
                "Con cantidad 0 la fecha de agotamiento debe ser hoy");
    }

    // necesitaReposicion – verdadero

    @Test
    @DisplayName("necesitaReposicion devuelve true cuando el agotamiento es hoy o antes de mañana")
    void necesitaReposicion_agotamientoHoyOMañana_devuelveTrue() {
        // cantidad=1, velocidad=1.0  →  agotamiento = hoy + 1 día (= mañana) → necesita reposición
        StockMaquina stock = new StockMaquina(1L, maquinaMock, productoMock, 1, 0, 1.0);
        assertTrue(stock.necesitaReposicion(),
                "Debe necesitar reposición si el agotamiento es igual o antes de mañana");
    }

    @Test
    @DisplayName("necesitaReposicion devuelve true cuando el stock es 0 (agotamiento = hoy)")
    void necesitaReposicion_stockCero_devuelveTrue() {
        StockMaquina stock = new StockMaquina(1L, maquinaMock, productoMock, 0, 0, 1.0);
        assertTrue(stock.necesitaReposicion(),
                "Con stock 0 siempre debe necesitar reposición");
    }

    // necesitaReposicion – falso

    @Test
    @DisplayName("necesitaReposicion devuelve false cuando quedan más de un día de stock")
    void necesitaReposicion_stockSuficiente_devuelveFalse() {
        // cantidad=100, velocidad=1.0  →  agotamiento = hoy + 100 días → NO necesita reposición
        StockMaquina stock = new StockMaquina(1L, maquinaMock, productoMock, 100, 0, 1.0);
        assertFalse(stock.necesitaReposicion(),
                "Con stock suficiente no debe necesitar reposición");
    }

    // verificación mockito: colaboradores no tocados

    @Test
    @DisplayName("El constructor no invoca métodos de la máquina ni del producto")
    void constructor_noInvocaMetodosDeColaboradores() {
        new StockMaquina(1L, maquinaMock, productoMock, 10, 2, 1.0);

        // Los colaboradores solo se almacenan; no se deben invocar sus métodos
        verifyNoInteractions(maquinaMock, productoMock);
    }
}
