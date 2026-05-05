package es.usc.etse.grupo34.daos;

import es.usc.etse.grupo34.entidades.Maquina;
import es.usc.etse.grupo34.entidades.Producto;
import es.usc.etse.grupo34.entidades.StockMaquina;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas de integración para StockDAO (HU-03, Sprint 1).
 *
 * Estrategia:
 *  - StockDAO es el SUT (System Under Test).
 *  - Maquina y Producto se simulan con Mockito para aislar el DAO
 *    de implementaciones concretas de esas clases.
 *  - Se verifica tanto el estado (listas devueltas) como el
 *    comportamiento (invocaciones sobre los mocks cuando procede).
 */
@DisplayName("StockDAO – pruebas de integración")
class StockDAOTest {

    // fixtures

    AutoCloseable acl;

    @Mock Maquina maquina1;
    @Mock Maquina maquina2;
    @Mock Producto producto1;
    @Mock Producto producto2;

    StockDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        acl = MockitoAnnotations.openMocks(this);
        dao = new StockDAO();

        // Los mocks de Maquina/Producto deben devolver sus ids cuando se les consulte
        when(maquina1.getId()).thenReturn(1L);
        when(maquina2.getId()).thenReturn(2L);
        when(producto1.getId()).thenReturn(10L);
        when(producto2.getId()).thenReturn(20L);
    }

    @AfterEach
    void tearDown() throws Exception {
        acl.close();
    }

    // ---
    // add()
    // ---

    @Nested
    @DisplayName("add()")
    class Add {

        // CP6

        @Test
        @DisplayName("CP6 – add con stock válido lo almacena correctamente")
        void addStockValido_seAlmacena() {
            StockMaquina stock = new StockMaquina(1L, maquina1, producto1, 10, 2, 1.0);

            dao.add(stock);

            List<StockMaquina> todos = dao.findAll();
            assertAll("Stock añadido correctamente",
                    () -> assertEquals(1, todos.size(), "Debe haber exactamente un stock"),
                    () -> assertSame(stock, todos.get(0), "El stock recuperado debe ser el mismo objeto")
            );
        }

        // CP7

        @Test
        @DisplayName("CP7 – add(null) lanza IllegalArgumentException")
        void addNull_lanzaIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> dao.add(null),
                    "Debe lanzar IllegalArgumentException al añadir null"
            );
            assertNotNull(ex.getMessage(), "El mensaje de la excepción no puede ser nulo");
        }

        @Test
        @DisplayName("add con id duplicado lanza IllegalArgumentException")
        void addIdDuplicado_lanzaIllegalArgumentException() {
            StockMaquina s1 = new StockMaquina(1L, maquina1, producto1, 10, 2, 1.0);
            StockMaquina s2 = new StockMaquina(1L, maquina2, producto2, 5,  1, 2.0);

            dao.add(s1);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> dao.add(s2),
                    "Debe lanzar IllegalArgumentException si el id ya existe"
            );
        }
    }

    // ---
    // findByMaquina()
    // ---

    @Nested
    @DisplayName("findByMaquina()")
    class FindByMaquina {

        // CP8

        @Test
        @DisplayName("CP8 – findByMaquina devuelve todos los stocks de la máquina solicitada")
        void findByMaquina_conStocks_devuelveLista() {
            StockMaquina s1 = new StockMaquina(1L, maquina1, producto1, 10, 2, 1.0);
            StockMaquina s2 = new StockMaquina(2L, maquina1, producto2, 5,  1, 2.0);
            StockMaquina s3 = new StockMaquina(3L, maquina2, producto1, 8,  0, 0.5);
            dao.add(s1);
            dao.add(s2);
            dao.add(s3);

            List<StockMaquina> resultado = dao.findByMaquina(1L);

            assertAll("Solo los stocks de la máquina 1",
                    () -> assertEquals(2, resultado.size(), "Debe devolver exactamente 2 stocks para la máquina 1"),
                    () -> assertTrue(resultado.contains(s1), "Debe contener el stock s1"),
                    () -> assertTrue(resultado.contains(s2), "Debe contener el stock s2"),
                    () -> assertFalse(resultado.contains(s3), "No debe contener el stock s3 (es de la máquina 2)")
            );
        }

        // CP9

        @Test
        @DisplayName("CP9 – findByMaquina devuelve lista vacía si la máquina no tiene stocks")
        void findByMaquina_sinStocks_devuelveListaVacia() {
            List<StockMaquina> resultado = dao.findByMaquina(99L);

            assertNotNull(resultado, "La lista no debe ser nula");
            assertTrue(resultado.isEmpty(), "Debe devolver lista vacía para una máquina sin stocks");
        }

        @Test
        @DisplayName("findByMaquina devuelve copia independiente (modificar la lista no afecta al DAO)")
        void findByMaquina_devuelveCopiaDefensiva() {
            StockMaquina s1 = new StockMaquina(1L, maquina1, producto1, 10, 2, 1.0);
            dao.add(s1);

            List<StockMaquina> primera  = dao.findByMaquina(1L);
            primera.clear();
            List<StockMaquina> segunda = dao.findByMaquina(1L);

            assertEquals(1, segunda.size(),
                    "Modificar la lista devuelta no debe afectar al estado interno del DAO");
        }
    }

    // ---
    // findByMaquinaYProducto()
    // ---

    @Nested
    @DisplayName("findByMaquinaYProducto()")
    class FindByMaquinaYProducto {

        // CP10

        @Test
        @DisplayName("CP10 – findByMaquinaYProducto devuelve el stock correcto")
        void findByMaquinaYProducto_existente_devuelveStock() {
            StockMaquina s1 = new StockMaquina(1L, maquina1, producto1, 10, 2, 1.0);
            StockMaquina s2 = new StockMaquina(2L, maquina1, producto2, 5,  1, 2.0);
            dao.add(s1);
            dao.add(s2);

            StockMaquina resultado = dao.findByMaquinaYProducto(1L, 10L);

            assertSame(s1, resultado,
                    "Debe devolver exactamente el stock vinculado a la máquina 1 y producto 10");
        }

        // CP11

        @Test
        @DisplayName("CP11 – findByMaquinaYProducto lanza NoSuchElementException si no existe la combinación")
        void findByMaquinaYProducto_noExistente_lanzaNoSuchElementException() {
            StockMaquina s1 = new StockMaquina(1L, maquina1, producto1, 10, 2, 1.0);
            dao.add(s1);

            assertThrows(
                    NoSuchElementException.class,
                    () -> dao.findByMaquinaYProducto(1L, 99L),
                    "Debe lanzar NoSuchElementException para una combinación máquina-producto inexistente"
            );
        }

        @Test
        @DisplayName("findByMaquinaYProducto lanza NoSuchElementException en DAO vacío")
        void findByMaquinaYProducto_daoVacio_lanzaNoSuchElementException() {
            assertThrows(
                    NoSuchElementException.class,
                    () -> dao.findByMaquinaYProducto(1L, 10L),
                    "Debe lanzar NoSuchElementException si el DAO está vacío"
            );
        }

        @Test
        @DisplayName("findByMaquinaYProducto invoca getId() sobre los mocks al buscar")
        void findByMaquinaYProducto_invocaGetIdEnColaboradores() {
            StockMaquina s1 = new StockMaquina(1L, maquina1, producto1, 10, 2, 1.0);
            dao.add(s1);

            dao.findByMaquinaYProducto(1L, 10L);

            // El DAO debe consultar los ids de la máquina y el producto para filtrar
            verify(maquina1,  atLeastOnce()).getId();
            verify(producto1, atLeastOnce()).getId();
        }
    }

    // ---
    // getProductosParaReposicion()
    // ---

    @Nested
    @DisplayName("getProductosParaReposicion()")
    class GetProductosParaReposicion {

        @Test
        @DisplayName("Devuelve solo los stocks que necesitan reposición")
        void getProductosParaReposicion_conMezcla_devuelveSoloLosQueNecesitan() {
            // s1: cantidad=0  → agotamiento hoy → necesita reposición
            StockMaquina s1 = new StockMaquina(1L, maquina1, producto1, 0,   0, 1.0);
            // s2: cantidad=100 → agotamiento lejos → no necesita reposición
            StockMaquina s2 = new StockMaquina(2L, maquina1, producto2, 100, 0, 1.0);
            dao.add(s1);
            dao.add(s2);

            List<StockMaquina> resultado = dao.getProductosParaReposicion(1L);

            assertAll("Solo s1 necesita reposición",
                    () -> assertEquals(1, resultado.size(), "Debe devolver exactamente 1 stock"),
                    () -> assertTrue(resultado.contains(s1), "Debe incluir el stock agotado"),
                    () -> assertFalse(resultado.contains(s2), "No debe incluir el stock con suficiente cantidad")
            );
        }

        @Test
        @DisplayName("Devuelve lista vacía si ningún stock de la máquina necesita reposición")
        void getProductosParaReposicion_todosOk_devuelveListaVacia() {
            StockMaquina s1 = new StockMaquina(1L, maquina1, producto1, 100, 0, 1.0);
            dao.add(s1);

            List<StockMaquina> resultado = dao.getProductosParaReposicion(1L);

            assertTrue(resultado.isEmpty(),
                    "Debe devolver lista vacía si ningún producto necesita reposición");
        }

        @Test
        @DisplayName("Lanza NoSuchElementException si la máquina no tiene ningún stock registrado")
        void getProductosParaReposicion_maquinaSinStocks_lanzaNoSuchElementException() {
            assertThrows(
                    NoSuchElementException.class,
                    () -> dao.getProductosParaReposicion(99L),
                    "Debe lanzar NoSuchElementException si la máquina no tiene stocks"
            );
        }

        @Test
        @DisplayName("Devuelve todos los stocks cuando todos necesitan reposición")
        void getProductosParaReposicion_todosAgotados_devuelveTodos() {
            StockMaquina s1 = new StockMaquina(1L, maquina1, producto1, 0, 0, 1.0);
            StockMaquina s2 = new StockMaquina(2L, maquina1, producto2, 0, 0, 1.0);
            dao.add(s1);
            dao.add(s2);

            List<StockMaquina> resultado = dao.getProductosParaReposicion(1L);

            assertEquals(2, resultado.size(),
                    "Debe devolver los 2 stocks cuando ambos necesitan reposición");
        }
    }

    // ---
    // findAll()
    // ---

    @Test
    @DisplayName("findAll devuelve lista vacía cuando el DAO está vacío")
    void findAll_daoVacio_devuelveListaVacia() {
        List<StockMaquina> resultado = dao.findAll();

        assertNotNull(resultado, "findAll no debe devolver null");
        assertTrue(resultado.isEmpty(), "findAll debe devolver lista vacía en un DAO recién creado");
    }

    @Test
    @DisplayName("findAll devuelve todos los stocks registrados sin importar la máquina")
    void findAll_variasEntradas_devuelveTodos() {
        StockMaquina s1 = new StockMaquina(1L, maquina1, producto1, 10, 2, 1.0);
        StockMaquina s2 = new StockMaquina(2L, maquina2, producto2, 5,  1, 2.0);
        dao.add(s1);
        dao.add(s2);

        List<StockMaquina> resultado = dao.findAll();

        assertEquals(2, resultado.size(), "findAll debe devolver los 2 stocks registrados");
    }

    // ---
    // Verificaciones de comportamiento con Mockito
    // ---

    @Test
    @DisplayName("findByMaquina consulta getId() de cada stock al filtrar por máquina")
    void findByMaquina_invocaGetIdDeMaquina() {
        StockMaquina s1 = new StockMaquina(1L, maquina1, producto1, 10, 2, 1.0);
        StockMaquina s2 = new StockMaquina(2L, maquina2, producto2, 5,  1, 2.0);
        dao.add(s1);
        dao.add(s2);

        dao.findByMaquina(1L);

        // El DAO debe preguntar el id de cada máquina para filtrar
        verify(maquina1, atLeastOnce()).getId();
        verify(maquina2, atLeastOnce()).getId();
    }
}
