package manyWorker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manyWorker.entity.PerfilSocial;

@Repository
public interface PerfilSocialRepository extends JpaRepository<PerfilSocial, Integer>{

}
