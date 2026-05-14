package com.AuthNode.auth.IRepository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.AuthNode.auth.Entity.PersonaEntity;

public interface IPersonaRepository extends JpaRepository<PersonaEntity, Integer>{

	Optional<PersonaEntity> findByEmail(String email);
	@Query("SELECT p FROM PersonaEntity p WHERE p.verificationToken = :token")
    Optional<PersonaEntity> findByVerificationToken(@Param("token") String token);
}
