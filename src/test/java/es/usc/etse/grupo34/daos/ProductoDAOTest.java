package es.usc.etse.grupo34.daos;

import es.usc.etse.grupo34.entidades.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductoDAO – pruebas de integración")
class ProductoDAOTest {

    private ProductoDAO dao;

    @BeforeEach
    void setUp() {
        dao = new ProductoDAO();
    }

    @Test
    @DisplayName("add de producto válido: findAll devuelve exactamente un elemento")
    void addProductoValido_findAllDevuelveUnElemento() {
        Producto producto = new Producto(1L, "Agua", 1.50, "Bebida");

        dao.add(producto);

        List<Producto> lista = dao.findAll();
        assertEquals(1, lista.size());
        assertSame(producto, lista.get(0));
    }

    @Test
    @DisplayName("add(null) lanza IllegalArgumentException y el DAO queda vacío")
    void addNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> dao.add(null));
        assertTrue(dao.findAll().isEmpty());
    }

    @Test
    @DisplayName("add con id duplicado lanza IllegalArgumentException")
    void addIdDuplicado_lanzaExcepcion() {
        Producto primero = new Producto(1L, "Agua", 1.50, "Bebida");
        Producto duplicado = new Producto(1L, "Agua2", 2.00, "Bebida");

        dao.add(primero);

        assertThrows(IllegalArgumentException.class, () -> dao.add(duplicado));
        assertEquals(1, dao.findAll().size());
    }

    @Test
    @DisplayName("add con nombre duplicado lanza IllegalArgumentException")
    void addNombreDuplicado_lanzaExcepcion() {
        Producto primero = new Producto(1L, "Agua", 1.50, "Bebida");
        Producto duplicado = new Producto(2L, "Agua", 2.00, "Bebida");

        dao.add(primero);

        assertThrows(IllegalArgumentException.class, () -> dao.add(duplicado));
        assertEquals(1, dao.findAll().size());
    }

    @Test
    @DisplayName("findById devuelve producto existente")
    void findById_existente_devuelveProducto() {
        Producto producto = new Producto(9L, "Cafe", 1.80, "Bebida");
        dao.add(producto);

        Producto encontrado = dao.findById(9L);

        assertSame(producto, encontrado);
    }

    @Test
    @DisplayName("findById lanza NoSuchElementException si no existe")
    void findById_inexistente_lanzaExcepcion() {
        Producto producto = new Producto(9L, "Cafe", 1.80, "Bebida");
        dao.add(producto);

        assertThrows(NoSuchElementException.class, () -> dao.findById(999L));
    }

    @Test
    @DisplayName("findById(null) lanza IllegalArgumentException")
    void findById_idNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> dao.findById(null));
    }

    @Test
    @DisplayName("findAll devuelve lista vacía y copia defensiva")
    void findAll_vacioYCopiaDefensiva() {
        assertTrue(dao.findAll().isEmpty());

        dao.add(new Producto(7L, "Te", 1.20, "Bebida"));
        List<Producto> copia = dao.findAll();
        copia.clear();

        assertEquals(1, dao.findAll().size());
    }

    // ---
    // Caja blanca (Sprint 2)
    // ---
    /**
     * Análisis de complejidad ciclomática sobre ProductoDAO:
     *
     *  add()      CC = 4 -> los caminos quedan cubiertos por los CPs de caja negra:
     *                        P1 (producto nulo), P2 (id duplicado), P3 (nombre duplicado),
     *                        P4 (alta correcta).
     *  findById() CC = 3 -> P1 (id nulo) y P3 (no existe) cubiertos por CN;
     *                        P2 (encuentra el producto tras iterar por más de un elemento)
     *                        requiere CB1.
     *  findAll()  CC = 1 -> no introduce decisiones.
     */
    @Nested
    @DisplayName("Caja Blanca")
    class CajaBlanca {

        /**
         * CB1 — findById(), camino P2.
         *
         * Camino: M1 -> M2(false) -> M2(true) -> M3
         *
         * El DAO contiene dos productos. El primero no coincide con el id buscado,
         * por lo que el bucle continúa; el segundo sí coincide y se devuelve.
         * Este caso fuerza la rama de iteración posterior que no queda garantizada
         * con una búsqueda directa sobre el primer elemento insertado.
         */
        @Test
        @DisplayName("CB1 – findById encuentra el producto en la 2ª iteración del bucle")
        void cb1_findById_encuentraEnSegundaIteracion() {
            Producto primero = new Producto(1L, "Agua", 1.50, "Bebida");
            Producto segundo = new Producto(2L, "Zumo", 2.00, "Bebida");
            dao.add(primero);
            dao.add(segundo);

            Producto encontrado = dao.findById(2L);

            assertAll("El producto encontrado debe ser el segundo, tras descartar el primero",
                    () -> assertSame(segundo, encontrado,
                            "Debe devolver el producto con id 2L"),
                    () -> assertNotSame(primero, encontrado,
                            "No debe devolver el primer producto, cuyo id no coincide")
            );
        }
    }
}

/*
 * Resumen de la prueba HU-02:
 * - Se mantienen las pruebas de caja negra para validar las reglas funcionales del Producto y del ProductoDAO.
 * - Se añade una prueba de caja blanca en Sprint 2 para forzar el camino del bucle de findById cuando la coincidencia
 *   no está en el primer elemento, completando la cobertura de decisión que no quedaba garantizada por los casos anteriores.
 * - No se ha creado una clase nueva porque la cobertura adicional encaja de forma natural dentro de ProductoDAOTest,
 *   siguiendo el mismo patrón de organización que StockDAOTest.
 */
