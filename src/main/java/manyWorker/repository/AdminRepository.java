package manyWorker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manyWorker.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer>{

}
