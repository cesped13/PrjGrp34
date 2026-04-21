package es.usc.etse.grupo34.entidades;

import java.time.LocalDate;

/**
 * Entidad asociativa entre Maquina y Producto.
 * Almacena la cantidad disponible, cantidad mínima, velocidad de consumo
 * y la fecha de última actualización. Concentra la lógica de negocio
 * más relevante del sistema.
 */
public class StockMaquina {

    private Long id;
    private Maquina maquina;
    private Producto producto;
    private int cantidad;
    private int cantidadMinima;
    private double velocidadConsumo;
    private LocalDate ultimaActualizacion;

    /**
     * Constructor que valida que máquina y producto no son nulos,
     * y que cantidad y cantidadMinima son >= 0.
     *
     * @param id              identificador único del stock (no nulo)
     * @param maquina         máquina asociada (no nula)
     * @param producto        producto asociado (no nulo)
     * @param cantidad        cantidad actual disponible (>= 0)
     * @param cantidadMinima  cantidad mínima permitida (>= 0)
     * @param velocidadConsumo velocidad estimada de consumo en unidades/día (> 0)
     * @throws IllegalArgumentException si alguna validación falla
     */
    public StockMaquina(Long id, Maquina maquina, Producto producto,
                        int cantidad, int cantidadMinima, double velocidadConsumo) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        if (maquina == null) {
            throw new IllegalArgumentException("La máquina no puede ser nula");
        }
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        if (cantidadMinima < 0) {
            throw new IllegalArgumentException("La cantidad mínima no puede ser negativa");
        }
        if (velocidadConsumo <= 0) {
            throw new IllegalArgumentException("La velocidad de consumo debe ser positiva");
        }
        this.id = id;
        this.maquina = maquina;
        this.producto = producto;
        this.cantidad = cantidad;
        this.cantidadMinima = cantidadMinima;
        this.velocidadConsumo = velocidadConsumo;
        this.ultimaActualizacion = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public Maquina getMaquina() {
        return maquina;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getCantidadMinima() {
        return cantidadMinima;
    }

    public double getVelocidadConsumo() {
        return velocidadConsumo;
    }

    public LocalDate getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    /**
     * Reduce la cantidad disponible tras una venta o consumo.
     *
     * @param unidades número de unidades a descontar (debe ser > 0)
     * @throws IllegalArgumentException si las unidades son negativas
     * @throws IllegalStateException    si la cantidad resultante sería menor que cero
     */
    public void actualizarCantidad(int unidades) {
        if (unidades < 0) {
            throw new IllegalArgumentException("Las unidades no pueden ser negativas");
        }
        if (this.cantidad - unidades < 0) {
            throw new IllegalStateException("La cantidad resultante no puede ser menor que cero");
        }
        this.cantidad -= unidades;
        this.ultimaActualizacion = LocalDate.now();
    }

    /**
     * Calcula la fecha estimada de agotamiento del producto.
     * fechaAgotamiento = ultimaActualizacion + (cantidad / velocidadConsumo) días
     *
     * @return fecha estimada de agotamiento como LocalDate
     */
    public LocalDate getFechaEstimadaAgotamiento() {
        long diasRestantes = (long) (cantidad / velocidadConsumo);
        return ultimaActualizacion.plusDays(diasRestantes);
    }

    /**
     * Determina si el producto necesita reposición.
     * Un producto necesita reposición cuando su fecha estimada de agotamiento
     * es igual o anterior al día siguiente al momento de la consulta.
     *
     * @return true si necesita reposición, false en caso contrario
     */
    public boolean necesitaReposicion() {
        LocalDate fechaAgotamiento = getFechaEstimadaAgotamiento();
        LocalDate manana = LocalDate.now().plusDays(1);
        return !fechaAgotamiento.isAfter(manana);
    }
}
