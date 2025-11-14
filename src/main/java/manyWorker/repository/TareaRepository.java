package manyWorker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manyWorker.entity.Tarea;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, String> {
	boolean existsByCategoria_Id(String id);
}