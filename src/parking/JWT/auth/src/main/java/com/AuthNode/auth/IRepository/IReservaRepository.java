package com.AuthNode.auth.IRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.AuthNode.auth.Entity.ReservaEntity;

public interface IReservaRepository extends JpaRepository<ReservaEntity, Integer> {

    @Query("""
        SELECT r FROM ReservaEntity r
        WHERE r.vehiculo.persona.id = :personaId
    """)
    List<ReservaEntity> findByPersonaId(@Param("personaId") Integer personaId);

}