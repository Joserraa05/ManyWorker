package manyWorker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import manyWorker.entity.Categoria;
import manyWorker.repository.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> findById(String id) {
        return categoriaRepository.findById(id);
    }

    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public boolean existsById(String id) {
        return categoriaRepository.existsById(id);
    }

    // Actualizar categoría
    public Categoria update(String id, Categoria datos) {
        Optional<Categoria> categoriaExistente = categoriaRepository.findById(id);
        if (categoriaExistente.isPresent()) {
            Categoria categoria = categoriaExistente.get();
            categoria.setTitulo(datos.getTitulo());
            categoria.setLeyesAplicables(datos.getLeyesAplicables());
            categoria.setEsReparacion(datos.isEsReparacion());
            return categoriaRepository.save(categoria);
        } else {
            throw new RuntimeException("Categoría no encontrada con ID: " + id);
        }
    }

    // Eliminar categoría
    public void delete(String id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if (categoria.isPresent()) {
            if (!categoria.get().getTareas().isEmpty()) {
                throw new IllegalStateException("No se puede eliminar una categoría que tiene tareas asociadas");
            }
            categoriaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Categoría no encontrada con ID: " + id);
        }
    }

    // Inicializar categorías por defecto
    public void inicializarCategorias() {
        if (categoriaRepository.count() == 0) {
            crearCategoria("Carpintería", "Términos de carpintería", false);
            crearCategoria("Reparación de techos", "Términos de reparación de techos", true);
            crearCategoria("Limpieza", "Términos de limpieza", false);
            crearCategoria("Puertas", "Términos de instalación de puertas", false);
            crearCategoria("Cableado eléctrico", "Términos de electricidad", false);
            crearCategoria("Instalación de ventiladores", "Términos de instalación", false);
            crearCategoria("Reparación de cercas", "Términos de reparación de cercas", true);
            crearCategoria("Sistemas de seguridad para el hogar", "Términos de seguridad", false);
            crearCategoria("Instalación de aislamiento", "Términos de instalación", false);
            crearCategoria("Reparaciones de lámparas", "Términos de reparación de lámparas", true);
            crearCategoria("Mudanzas", "Términos de mudanzas", false);
            crearCategoria("Pintura", "Términos de pintura", false);
            crearCategoria("Control de plagas", "Términos de control de plagas", false);
            crearCategoria("Reparaciones de fontanería", "Términos de fontanería", true);
            crearCategoria("Techado", "Términos de techado", false);
            crearCategoria("Instalación de estanterías", "Términos de instalación", false);
            crearCategoria("Paneles solares", "Términos de energía solar", false);
            crearCategoria("Insonorización", "Términos de insonorización", false);
            crearCategoria("Reparación de sistemas de riego", "Términos de sistemas de riego", true);
            crearCategoria("Reparación de ventanas", "Términos de reparación de ventanas", true);
        }
    }

    private void crearCategoria(String titulo, String leyes, boolean esReparacion) {
        Categoria categoria = new Categoria();
        categoria.setTitulo(titulo);
        categoria.setLeyesAplicables(leyes);
        categoria.setEsReparacion(esReparacion);
        categoriaRepository.save(categoria);
    }
}