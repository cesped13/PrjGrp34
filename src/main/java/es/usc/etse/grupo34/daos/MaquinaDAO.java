package es.usc.etse.grupo34.daos;

import es.usc.etse.grupo34.entidades.Maquina;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

// DAO en memoria para maquinas.
public class MaquinaDAO {

    private List<Maquina> maquinas = new ArrayList<>();

    public void add(Maquina maquina) {
        if (maquina == null) {
            throw new IllegalArgumentException("La máquina no puede ser nula");
        }
        for (Maquina m : maquinas) {
            if (m.getId().equals(maquina.getId())) {
                throw new IllegalArgumentException("Ya existe una máquina con el id: " + maquina.getId());
            }
        }
        maquinas.add(maquina);
    }

    public Maquina findById(Long id) {
        for (Maquina m : maquinas) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        throw new NoSuchElementException("No existe ninguna máquina con el id: " + id);
    }

    public List<Maquina> findAll() {
        return new ArrayList<>(maquinas);
    }
}
