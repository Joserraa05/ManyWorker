package manyWorker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manyWorker.entity.Solicitud;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {
	List<Solicitud> findByTrabajadorId(int trabajadorId);
	
	
	List<Solicitud> findByTareaId(String tareaId);
}