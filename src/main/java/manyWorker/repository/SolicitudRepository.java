package manyWorker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manyWorker.entity.Solicitud;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {
	
}