package es.usc.etse.grupo34.entidades;

public class StockMaquina {

    private int cantidad;

    public StockMaquina(int cantidadInicial) {
        if (cantidadInicial < 0) {
            throw new IllegalArgumentException("La cantidad inicial no puede ser negativa");
        }
        this.cantidad = cantidadInicial;
    }

    public void actualizarCantidad(int unidades) {
        if (unidades < 0) {
            throw new IllegalArgumentException("Las unidades no pueden ser negativas");
        }

        if (cantidad - unidades < 0) {
            throw new IllegalStateException("No hay stock suficiente para realizar la operacion");
        }

        cantidad -= unidades;
    }

    public int getCantidad() {
        return cantidad;
    }
}
