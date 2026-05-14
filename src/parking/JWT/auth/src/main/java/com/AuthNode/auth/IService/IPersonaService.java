package com.AuthNode.auth.IService;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.AuthNode.auth.Entity.PersonaEntity;

public interface IPersonaService {
    List<PersonaEntity> getAll() throws Exception;
    PersonaEntity findById(int id) throws Exception;
    PersonaEntity save(PersonaEntity persona);        // sin throws Exception
    void update(int id, PersonaEntity persona) throws Exception;
    void delete(int id) throws Exception;
    Optional<PersonaEntity> findByCorreo(String correo) throws Exception;
    Page<PersonaEntity> getAllPageable(Pageable pageable) throws Exception;
    String verifyEmail(String token);
}