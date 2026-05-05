package es.usc.etse.grupo34.entidades;

public class Localizacion {

    private Long id;
    private String descripcion;
    private double latitud;
    private double longitud;

    public Localizacion(Long id, String descripcion, double latitud, double longitud) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        if (latitud < -90.0 || latitud > 90.0) {
            throw new IllegalArgumentException("La latitud debe estar en el rango [-90, 90]");
        }
        if (longitud < -180.0 || longitud > 180.0) {
            throw new IllegalArgumentException("La longitud debe estar en el rango [-180, 180]");
        }
        this.id = id;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Long getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }
}
