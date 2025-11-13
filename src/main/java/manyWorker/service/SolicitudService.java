package manyWorker.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import manyWorker.entity.Solicitud;
import manyWorker.entity.Tarea;
import manyWorker.repository.SolicitudRepository;
import manyWorker.repository.TareaRepository;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private TareaRepository tareaRepository;

    // Listar todas las solicitudes
    public List<Solicitud> listar() {
        return solicitudRepository.findAll();
    }

    // Buscar solicitud por ID
    public Optional<Solicitud> findById(int id) {
        return solicitudRepository.findById(id);
    }

    //Crear nueva solicitud con validación de reparación
    public Solicitud crear(Solicitud solicitud) {
        // Validaciones básicas existentes
        if (solicitud.getTrabajador() == null) {
            throw new IllegalArgumentException("La solicitud debe tener un trabajador asignado");
        }

        //Si la tarea es de reparación, validar precio y comentario
        if (solicitud.getTarea() != null && esTareaDeReparacion(solicitud.getTarea())) {
            validarSolicitudReparacion(solicitud);
        }

        // Validaciones generales (aplican a todas las solicitudes)
        if (solicitud.getPrecioOfrecido() == null || solicitud.getPrecioOfrecido() <= 0) {
            throw new IllegalArgumentException("El precio ofrecido debe ser mayor que 0");
        }
        if (solicitud.getComentario() == null || solicitud.getComentario().isBlank()) {
            throw new IllegalArgumentException("El comentario es obligatorio");
        }

        solicitud.setFechaRegistro(LocalDateTime.now());
        solicitud.setEstado(Solicitud.EstadoSolicitud.PENDIENTE);
        return solicitudRepository.save(solicitud);
    }

    //Validar si es tarea de reparación
    private boolean esTareaDeReparacion(Tarea tarea) {
        if (tarea.getCategoria() == null) {
            return false;
        }
        return tarea.getCategoria().isEsReparacion();
    }

    //Validaciones específicas para reparaciones
    private void validarSolicitudReparacion(Solicitud solicitud) {
        // Para reparaciones, el precio es obligatorio (ya validado arriba pero con mensaje específico)
        if (solicitud.getPrecioOfrecido() == null) {
            throw new IllegalArgumentException("Las solicitudes para tareas de reparación deben incluir un precio ofrecido");
        }
        
        if (solicitud.getPrecioOfrecido() <= 0) {
            throw new IllegalArgumentException("El precio ofrecido debe ser mayor a 0");
        }
        
        //Para reparaciones el comentario es obligatorio y con longitud mínima
        if (solicitud.getComentario() == null || solicitud.getComentario().trim().isEmpty()) {
            throw new IllegalArgumentException("Las solicitudes para tareas de reparación deben incluir un comentario");
        }
        
        if (solicitud.getComentario().trim().length() < 10) {
            throw new IllegalArgumentException("El comentario debe tener al menos 10 caracteres");
        }
    }

    //Aceptar solicitud
    public Solicitud aceptar(int id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        solicitud.setEstado(Solicitud.EstadoSolicitud.ACEPTADO);
        return solicitudRepository.save(solicitud);
    }

    //Rechazar solicitud
    public Solicitud rechazar(int id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        solicitud.setEstado(Solicitud.EstadoSolicitud.RECHAZADO);
        return solicitudRepository.save(solicitud);
    }

    //Eliminar solicitud (solo pendiente)
    public void eliminar(int id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (solicitud.getEstado() != Solicitud.EstadoSolicitud.PENDIENTE) {
            throw new RuntimeException("No se puede eliminar una solicitud que no esté pendiente");
        }

        solicitudRepository.delete(solicitud);
    }
}