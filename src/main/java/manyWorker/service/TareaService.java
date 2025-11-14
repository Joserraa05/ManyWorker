package manyWorker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import manyWorker.entity.Tarea;
import manyWorker.repository.TareaRepository;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    public List<Tarea> findAll() {
        return tareaRepository.findAll();
    }

    public Optional<Tarea> findById(String id) {
        return tareaRepository.findById(id);
    }

    public Tarea save(Tarea tarea) {
        return tareaRepository.save(tarea);
    }

    public boolean existsById(String id) {
        return tareaRepository.existsById(id);
    }

    public Tarea update(String id, Tarea datos) {
        Optional<Tarea> optional = tareaRepository.findById(id);
        if (optional.isPresent()) {
            Tarea tarea = optional.get();
            tarea.setDescripcion(datos.getDescripcion());
            tarea.setDireccion(datos.getDireccion());
            tarea.setPrecioMax(datos.getPrecioMax());
            tarea.setFechaFin(datos.getFechaFin());
            tarea.setCategoria(datos.getCategoria());
            return tareaRepository.save(tarea);
        }
        return null;
    }

    public void delete(String id) {
        tareaRepository.deleteById(id);
    }

    // Método para verificar si una categoría tiene tareas asociadas
    public boolean existsByCategoriaId(String categoriaId) {
        return tareaRepository.existsByCategoria_Id(categoriaId);
    }
}