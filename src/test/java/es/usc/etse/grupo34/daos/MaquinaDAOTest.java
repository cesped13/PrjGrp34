package es.usc.etse.grupo34.daos;

import es.usc.etse.grupo34.entidades.Localizacion;
import es.usc.etse.grupo34.entidades.Maquina;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MaquinaDAOTest {

    @Test
    void addGuardaUnaMaquinaValida() {
        MaquinaDAO maquinaDAO = new MaquinaDAO();
        Maquina maquina = crearMaquina(1L, "M1");

        maquinaDAO.add(maquina);
        List<Maquina> maquinas = maquinaDAO.findAll();

        assertAll("La maquina debe quedar almacenada en el DAO",
                () -> assertEquals(1, maquinas.size()),
                () -> assertSame(maquina, maquinas.getFirst()),
                () -> assertSame(maquina, maquinaDAO.findById(1L)));
    }

    @Test
    void addLanzaExcepcionSiLaMaquinaEsNula() {
        MaquinaDAO maquinaDAO = new MaquinaDAO();

        assertThrows(IllegalArgumentException.class,
                () -> maquinaDAO.add(null));
    }

    @Test
    void addLanzaExcepcionSiElIdEstaDuplicado() {
        MaquinaDAO maquinaDAO = new MaquinaDAO();
        Maquina primera = crearMaquina(1L, "M1");
        Maquina duplicada = crearMaquina(1L, "M2");

        maquinaDAO.add(primera);

        assertThrows(IllegalArgumentException.class,
                () -> maquinaDAO.add(duplicada));
    }

    @Test
    void findByIdDevuelveLaMaquinaExistente() {
        MaquinaDAO maquinaDAO = new MaquinaDAO();
        Maquina maquina = crearMaquina(5L, "M5");
        maquinaDAO.add(maquina);

        Maquina recuperada = maquinaDAO.findById(5L);

        assertAll("La busqueda por id debe devolver la maquina correcta",
                () -> assertNotNull(recuperada),
                () -> assertEquals("M5", recuperada.getNombre()),
                () -> assertSame(maquina, recuperada));
    }

    @Test
    void findByIdLanzaExcepcionSiNoExisteLaMaquina() {
        MaquinaDAO maquinaDAO = new MaquinaDAO();

        assertThrows(NoSuchElementException.class,
                () -> maquinaDAO.findById(99L));
    }

    @Test
    void findAllDevuelveListaVaciaSiNoHayMaquinas() {
        MaquinaDAO maquinaDAO = new MaquinaDAO();

        List<Maquina> maquinas = maquinaDAO.findAll();

        assertAll("La lista debe comenzar vacia",
                () -> assertNotNull(maquinas),
                () -> assertTrue(maquinas.isEmpty()));
    }

    private Maquina crearMaquina(Long id, String nombre) {
        Localizacion localizacion = new Localizacion(id, "Centro", 43.0, -8.0);
        return new Maquina(id, nombre, localizacion);
    }
}
