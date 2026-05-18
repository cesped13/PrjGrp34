package es.usc.etse.grupo34.daos;

import es.usc.etse.grupo34.entidades.StockMaquina;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

// DAO en memoria para el stock de las maquinas.
public class StockDAO {

    private List<StockMaquina> stocks;

    public StockDAO() {
        this.stocks = new ArrayList<>();
    }

    // Registra un stock si no es nulo ni duplicado.
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

    // Busca los stocks de una maquina.
    public List<StockMaquina> findByMaquina(Long maquinaId) {
        List<StockMaquina> resultado = new ArrayList<>();
        for (StockMaquina s : stocks) {
            if (s.getMaquina().getId().equals(maquinaId)) {
                resultado.add(s);
            }
        }
        return resultado;
    }

    // Busca el stock de un producto en una maquina.
    public StockMaquina findByMaquinaYProducto(Long maqId, Long prodId) {
        for (StockMaquina s : stocks) {
            if (s.getMaquina().getId().equals(maqId) && s.getProducto().getId().equals(prodId)) {
                return s;
            }
        }
        throw new NoSuchElementException(
                "No se encontró stock para la máquina " + maqId + " y producto " + prodId);
    }

    // Devuelve todos los stocks registrados.
    public List<StockMaquina> findAll() {
        return new ArrayList<>(stocks);
    }

    // Devuelve los stocks que necesitan reposicion.
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
