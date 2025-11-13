package manyWorker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manyWorker.entity.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer>{

}
