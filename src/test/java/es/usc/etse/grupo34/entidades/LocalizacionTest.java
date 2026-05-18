package es.usc.etse.grupo34.entidades;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocalizacionTest {

    // CP1: localizacion valida con id y coordenadas dentro de rango.
    @Test
    void creaLocalizacionValida() {
        Localizacion localizacion = new Localizacion(1L, "Centro", 43.0, -8.0);

        assertAll("La localizacion debe conservar los datos de entrada",
                () -> assertEquals(1L, localizacion.getId()),
                () -> assertEquals("Centro", localizacion.getDescripcion()),
                () -> assertEquals(43.0, localizacion.getLatitud()),
                () -> assertEquals(-8.0, localizacion.getLongitud()));
    }

    // CP2: latitud mayor que 90.
    @Test
    void lanzaExcepcionSiLatitudEsMayorQueNoventa() {
        assertThrows(IllegalArgumentException.class,
                () -> new Localizacion(1L, "Centro", 91.0, -8.0));
    }

    // CP3: latitud menor que -90.
    @Test
    void lanzaExcepcionSiLatitudEsMenorQueMenosNoventa() {
        assertThrows(IllegalArgumentException.class,
                () -> new Localizacion(1L, "Centro", -91.0, -8.0));
    }

    // CP4: longitud mayor que 180.
    @Test
    void lanzaExcepcionSiLongitudEsMayorQueCientoOchenta() {
        assertThrows(IllegalArgumentException.class,
                () -> new Localizacion(1L, "Centro", 43.0, 181.0));
    }

    // CP5: longitud menor que -180.
    @Test
    void lanzaExcepcionSiLongitudEsMenorQueMenosCientoOchenta() {
        assertThrows(IllegalArgumentException.class,
                () -> new Localizacion(1L, "Centro", 43.0, -181.0));
    }

    // CP6: id nulo.
    @Test
    void lanzaExcepcionSiIdEsNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Localizacion(null, "Centro", 43.0, -8.0));
    }
}
