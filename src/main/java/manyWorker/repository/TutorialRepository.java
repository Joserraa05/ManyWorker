package manyWorker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manyWorker.entity.Trabajador;
import manyWorker.entity.Tutorial;

@Repository
public interface TutorialRepository extends JpaRepository<Tutorial, Integer> {

	// Buscar todos los tutoriales de un trabajador 
	List<Tutorial> findByAutor(Trabajador autor);
	
	// Buscar todos los tutoriales de un trabajador por ID
	List<Tutorial> findByAutorId(int autorId);
	
}
