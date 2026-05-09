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
 * Pruebas de integración para StockDAO (HU-03, Sprint 1 + Sprint 2).
 *
 * Estrategia:
 *  - StockDAO es el SUT (System Under Test).
 *  - Maquina y Producto se simulan con Mockito para aislar el DAO
 *    de implementaciones concretas de esas clases.
 *  - Se verifica tanto el estado (listas devueltas) como el
 *    comportamiento (invocaciones sobre los mocks cuando procede).
 *
 * Cobertura de caja blanca (McCabe) — Sprint 2:
 *  - findByMaquinaYProducto(): CC=3. Los caminos P1 y P2 quedan cubiertos
 *    por los CPs de caja negra (CP10, CP11, daoVacio). El camino P3
 *    (bucle itera más de una vez antes de encontrar la coincidencia)
 *    requiere CB1, añadido en la clase CajaBlanca anidada.
 *  - getProductosParaReposicion(): CC=4. Los cuatro caminos quedan cubiertos
 *    íntegramente por los CPs de caja negra existentes; no se añaden CPs nuevos.

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

    // ---
    // Caja blanca (Sprint 2)
    // ---
    /**
     * Análisis de complejidad ciclomática sobre StockDAO:
     *
     *  add()                       CC = 3  -> caminos P1-P3 cubiertos por CN (CP6, CP7, addIdDuplicado)
     *  findByMaquina()             CC = 2  -> caminos P1-P2 cubiertos por CN (CP8, CP9)
     *  findByMaquinaYProducto()    CC = 3  -> P1 (DAO vacío) y P2 (1ª iter. coincide) cubiertos por CN.
     *                                        P3 (varias iteraciones, coincide en la 2ª o posterior) -> CB1
     *  getProductosParaReposicion() CC = 4 -> los 4 caminos quedan cubiertos por los CPs de CN:
     *                                        P1 -> maquinaSinStocks (lanza NSE)
     *                                        P2 -> todosOk (lista vacía)
     *                                        P3 -> conMezcla (solo uno necesita)
     *                                        P4 -> todosAgotados (todos necesitan)
     *                                        No se añaden CPs nuevos para este método.
     */
    @Nested
    @DisplayName("Caja Blanca")
    class CajaBlanca {
 
        /**
         * CB1 — findByMaquinaYProducto(), camino P3.
         *
         * Camino: M1 -> M2 -> M3(false) -> M2 -> M3(true) -> M4
         *
         * El DAO contiene dos stocks de la misma máquina. El primero NO coincide
         * con el producto buscado (condición false en la 1ª iteración); el segundo
         * si coincide (condición true en la 2ª iteración). Este camino no estaba
         * forzado por ningún CP de caja negra, donde siempre se buscaba el primer
         * elemento insertado.
         *
         * Sin este caso, la decisión "false en M3" dentro del bucle nunca se
         * ejecutaba con un elemento posterior que sí coincidiese, dejando sin
         * cubrir la rama de continuación del bucle tras un fallo de coincidencia.
         */
        @Test
        @DisplayName("CB1 – findByMaquinaYProducto encuentra el stock en la 2ª iteración del bucle")
        void cb1_findByMaquinaYProducto_encuentraEnSegundaIteracion() {
            // Arrange: s1 no coincide con el producto buscado (producto2, id=20)
            //          s2 si coincide (producto2, id=20)
            // El bucle descarta s1 en la 1ª iteración y devuelve s2 en la 2ª.
            StockMaquina s1 = new StockMaquina(1L, maquina1, producto1, 10, 2, 1.0);
            StockMaquina s2 = new StockMaquina(2L, maquina1, producto2, 5,  1, 2.0);
            dao.add(s1);
            dao.add(s2);
 
            StockMaquina resultado = dao.findByMaquinaYProducto(1L, 20L);
 
            assertAll("El stock encontrado debe ser s2, descartando s1 en la primera iteración",
                    () -> assertSame(s2, resultado,
                            "Debe devolver s2, el stock vinculado a maquina1 y producto2"),
                    () -> assertNotSame(s1, resultado,
                            "No debe devolver s1, cuyo producto no coincide con el buscado")
            );
 
            // Verificación de comportamiento: el DAO debe haber consultado el id
            // de maquina1 al menos dos veces (una por cada iteración del bucle)
            // y el id de producto1 al menos una vez (para descartar s1).
            verify(maquina1,  atLeast(2)).getId();
            verify(producto1, atLeastOnce()).getId();
            verify(producto2, atLeastOnce()).getId();
        }
    }
}
