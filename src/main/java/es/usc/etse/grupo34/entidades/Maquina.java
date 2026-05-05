package es.usc.etse.grupo34.entidades;

public class Maquina {

    private Long id;
    private String nombre;
    private Localizacion localizacion;

    public Maquina(Long id, String nombre, Localizacion localizacion) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser vacío ni nulo");
        }
        if (localizacion == null) {
            throw new IllegalArgumentException("La localización no puede ser nula");
        }
        this.id = id;
        this.nombre = nombre;
        this.localizacion = localizacion;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public Localizacion getLocalizacion() { return localizacion; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser vacío ni nulo");
        }
        this.nombre = nombre;
    }
}
