package es.usc.etse.grupo34.entidades;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MaquinaTest {

    @Test
    void creaMaquinaValida() {
        Localizacion localizacion = new Localizacion(1L, "Centro", 43.0, -8.0);

        Maquina maquina = new Maquina(7L, "M1", localizacion);

        assertAll("La maquina debe crearse con nombre y localizacion validos",
                () -> assertEquals(7L, maquina.getId()),
                () -> assertEquals("M1", maquina.getNombre()),
                () -> assertSame(localizacion, maquina.getLocalizacion()));
    }

    @Test
    void lanzaExcepcionSiNombreEsVacio() {
        Localizacion localizacion = new Localizacion(1L, "Centro", 43.0, -8.0);

        assertThrows(IllegalArgumentException.class,
                () -> new Maquina(7L, "", localizacion));
    }

    @Test
    void lanzaExcepcionSiNombreEsNulo() {
        Localizacion localizacion = new Localizacion(1L, "Centro", 43.0, -8.0);

        assertThrows(IllegalArgumentException.class,
                () -> new Maquina(7L, null, localizacion));
    }

    @Test
    void lanzaExcepcionSiLocalizacionEsNula() {
        assertThrows(IllegalArgumentException.class,
                () -> new Maquina(7L, "M1", null));
    }
}
