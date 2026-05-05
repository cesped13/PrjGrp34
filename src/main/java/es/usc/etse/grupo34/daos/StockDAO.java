package es.usc.etse.grupo34.daos;

import es.usc.etse.grupo34.entidades.StockMaquina;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * DAO para la gestión de stock de máquinas en memoria.
 * Simula persistencia mediante una lista interna.
 * Proporciona operaciones de consulta de inventario por máquina y por máquina-producto.
 */
public class StockDAO {

    private List<StockMaquina> stocks;

    public StockDAO() {
        this.stocks = new ArrayList<>();
    }

    /**
     * Registra una entrada de stock vinculando máquina y producto.
     * Valida que el stock no sea nulo.
     *
     * @param stock la entrada de stock a registrar (no nula)
     * @throws IllegalArgumentException si el stock es nulo o el id está duplicado
     */
    public void add(StockMaquina stock) {
        if (stock == null) {
            throw new IllegalArgumentException("El stock no puede ser nulo");
        }
        for (StockMaquina s : stocks) {
            if (s.getId().equals(stock.getId())) {
                throw new IllegalArgumentException("Ya existe un stock con el id " + stock.getId());
            }
        }
        stocks.add(stock);
    }

    /**
     * Devuelve la lista de entradas de stock de una máquina.
     * Devuelve lista vacía si la máquina no tiene productos asociados.
     *
     * @param maquinaId identificador de la máquina
     * @return lista de StockMaquina asociados a la máquina (puede estar vacía)
     */
    public List<StockMaquina> findByMaquina(Long maquinaId) {
        List<StockMaquina> resultado = new ArrayList<>();
        for (StockMaquina s : stocks) {
            if (s.getMaquina().getId().equals(maquinaId)) {
                resultado.add(s);
            }
        }
        return resultado;
    }

    /**
     * Recupera el stock de un producto concreto en una máquina concreta.
     *
     * @param maqId  identificador de la máquina
     * @param prodId identificador del producto
     * @return la entrada de StockMaquina correspondiente
     * @throws NoSuchElementException si no existe la combinación máquina-producto
     */
    public StockMaquina findByMaquinaYProducto(Long maqId, Long prodId) {
        for (StockMaquina s : stocks) {
            if (s.getMaquina().getId().equals(maqId) && s.getProducto().getId().equals(prodId)) {
                return s;
            }
        }
        throw new NoSuchElementException(
                "No se encontró stock para la máquina " + maqId + " y producto " + prodId);
    }

    /**
     * Devuelve la lista completa de stocks registrados.
     *
     * @return lista de StockMaquina (puede estar vacía)
     */
    public List<StockMaquina> findAll() {
        return new ArrayList<>(stocks);
    }

    /**
     * Devuelve la lista de entradas de stock de una máquina cuyo
     * método necesitaReposicion() devuelva true.
     *
     * @param maquinaId identificador de la máquina
     * @return lista de StockMaquina que necesitan reposición
     * @throws NoSuchElementException si la máquina no tiene ningún stock registrado
     */
    public List<StockMaquina> getProductosParaReposicion(Long maquinaId) {
        List<StockMaquina> stocksDeMaquina = findByMaquina(maquinaId);
        if (stocksDeMaquina.isEmpty()) {
            throw new NoSuchElementException(
                    "No se encontraron stocks para la máquina con id " + maquinaId);
        }
        List<StockMaquina> paraReposicion = new ArrayList<>();
        for (StockMaquina s : stocksDeMaquina) {
            if (s.necesitaReposicion()) {
                paraReposicion.add(s);
            }
        }
        return paraReposicion;
    }
}
