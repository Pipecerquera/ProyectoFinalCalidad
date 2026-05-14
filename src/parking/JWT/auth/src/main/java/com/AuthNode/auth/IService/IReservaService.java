package com.AuthNode.auth.IService;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.AuthNode.auth.Entity.ReservaEntity;

public interface IReservaService {

    public List<ReservaEntity> getAll() throws Exception;

    public Page<ReservaEntity> getAllPageable(Pageable pageable) throws Exception;

    public ReservaEntity findById(int id) throws Exception;

    public ReservaEntity save(ReservaEntity reserva);

    public void delete(int id) throws Exception;

    public void update(int id, ReservaEntity reserva) throws Exception;

    // NUEVO
    public List<ReservaEntity> findByPersonaId(Integer personaId) throws Exception;

}