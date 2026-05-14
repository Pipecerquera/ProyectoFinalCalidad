package com.AuthNode.auth.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.AuthNode.auth.Entity.ReservaEntity;
import com.AuthNode.auth.IRepository.IReservaRepository;
import com.AuthNode.auth.IService.IReservaService;

@Service
public class ReservaService implements IReservaService {

    @Autowired
    public IReservaRepository reservaRepository;

    @Override
    public List<ReservaEntity> getAll() throws Exception {
        return reservaRepository.findAll();
    }

    @Override
    public Page<ReservaEntity> getAllPageable(Pageable pageable) throws Exception {
        return reservaRepository.findAll(pageable);
    }

    @Override
    public ReservaEntity findById(int id) throws Exception {
        return reservaRepository.findById(id).orElse(null);
    }

    @Override
    public ReservaEntity save(ReservaEntity reserva) {
        return reservaRepository.save(reserva);
    }

    @Override
    public void delete(int id) throws Exception {
        reservaRepository.deleteById(id);
    }

    @Override
    public void update(int id, ReservaEntity reserva) throws Exception {

        ReservaEntity existingReserva = reservaRepository.findById(id)
            .orElseThrow(() ->
                new Exception("No se encontró la reserva con el ID proporcionado"));

        existingReserva.setSpace(reserva.getSpace());
        existingReserva.setFechaReserva(reserva.getFechaReserva());
        existingReserva.setFechaSalida(reserva.getFechaSalida());
        existingReserva.setVehiculo(reserva.getVehiculo());

        reservaRepository.save(existingReserva);
    }

    @Override
    public List<ReservaEntity> findByPersonaId(Integer personaId) throws Exception {
        return reservaRepository.findByPersonaId(personaId);
    }

    // Método para actualizar solo el campo isReserved
    public ReservaEntity updateIsReserved(int id, boolean isReserved) throws Exception {

        ReservaEntity existingReserva = reservaRepository.findById(id)
            .orElseThrow(() ->
                new Exception("No se encontró la reserva con el ID proporcionado"));

        existingReserva.getSpace().setReserved(isReserved);

        return reservaRepository.save(existingReserva);
    }

    // Método para actualizar solo el campo isOccupied
    public ReservaEntity updateIsOccupied(int id, boolean isOccupied) throws Exception {

        ReservaEntity existingReserva = reservaRepository.findById(id)
            .orElseThrow(() ->
                new Exception("No se encontró la reserva con el ID proporcionado"));

        existingReserva.getSpace().setOccupied(isOccupied);

        return reservaRepository.save(existingReserva);
    }

}