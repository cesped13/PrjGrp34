package es.usc.etse.grupo34.entidades;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class StockMaquina {

    private final Long id;
    private final Maquina maquina;
    private final Producto producto;
    private Integer cantidad;
    private final Integer cantidadMinima;
    private final Double velocidadConsumo;
    private LocalDate ultimaActualizacion;

    public StockMaquina(Long id, Maquina maquina, Producto producto, Integer cantidad, Integer cantidadMinima,
                        Double velocidadConsumo) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        if (maquina == null) {
            throw new IllegalArgumentException("La maquina no puede ser nula");
        }
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (cantidad == null || cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        if (cantidadMinima == null || cantidadMinima < 0) {
            throw new IllegalArgumentException("La cantidad minima no puede ser negativa");
        }
        if (velocidadConsumo == null || velocidadConsumo <= 0) {
            throw new IllegalArgumentException("La velocidad de consumo debe ser mayor que cero");
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

    public Integer getCantidad() {
        return cantidad;
    }

    public Integer getCantidadMinima() {
        return cantidadMinima;
    }

    public Double getVelocidadConsumo() {
        return velocidadConsumo;
    }

    public LocalDate getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void actualizarCantidad(Integer unidades) {
        if (unidades == null || unidades < 0) {
            throw new IllegalArgumentException("Las unidades no pueden ser negativas");
        }

        if (cantidad - unidades < 0) {
            throw new IllegalStateException("No hay stock suficiente para realizar la operacion");
        }

        cantidad -= unidades;
        ultimaActualizacion = LocalDate.now();
    }

    public boolean necesitaReposicion() {
        LocalDate manana = LocalDate.now().plusDays(1);
        LocalDate fechaAgotamiento = getFechaEstimadaAgotamiento();
        return !fechaAgotamiento.isAfter(manana);
    }

    public LocalDate getFechaEstimadaAgotamiento() {
        long diasParaAgotamiento = (long) Math.ceil(cantidad / velocidadConsumo);
        return LocalDate.now().plus(diasParaAgotamiento, ChronoUnit.DAYS);
    }
}
