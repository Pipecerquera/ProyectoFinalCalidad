package com.AuthNode.auth.web.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.AuthNode.auth.Entity.ReservaEntity;
import com.AuthNode.auth.IService.IReservaService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/reserva")
public class ReservaController {

    @Autowired
    private IReservaService reservaService;

    private static final Logger logger =
            LoggerFactory.getLogger(ReservaController.class);

    // Obtener todas las reservas
    @GetMapping("/all")
    public ResponseEntity<List<ReservaEntity>> getAllReservas() {

        try {

            List<ReservaEntity> reservas = reservaService.getAll();

            return new ResponseEntity<>(reservas, HttpStatus.OK);

        } catch (Exception e) {

            logger.error("Error al obtener todas las reservas", e);

            return new ResponseEntity<>(
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // ✅ NUEVO -> obtener reservas por persona
    @GetMapping("/persona/{personaId}")
    public ResponseEntity<List<ReservaEntity>> getReservasByPersona(
            @PathVariable Integer personaId) {

        try {

            List<ReservaEntity> reservas =
                    reservaService.findByPersonaId(personaId);

            return new ResponseEntity<>(
                    reservas,
                    HttpStatus.OK
            );

        } catch (Exception e) {

            logger.error(
                    "Error obteniendo reservas por persona",
                    e
            );

            return new ResponseEntity<>(
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // Obtener reservas paginadas
    @GetMapping("/all/pageable")
    public ResponseEntity<Page<ReservaEntity>> getAllReservasPageable(
            Pageable pageable) {

        try {

            Page<ReservaEntity> reservas =
                    reservaService.getAllPageable(pageable);

            return new ResponseEntity<>(
                    reservas,
                    HttpStatus.OK
            );

        } catch (Exception e) {

            logger.error(
                    "Error al obtener reservas paginadas",
                    e
            );

            return new ResponseEntity<>(
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // Obtener reserva por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getReservaById(
            @PathVariable int id) {

        Map<String, Object> response = new HashMap<>();

        try {

            ReservaEntity reserva =
                    reservaService.findById(id);

            response.put("status", "success");
            response.put("reserva", reserva);

            return new ResponseEntity<>(
                    response,
                    HttpStatus.OK
            );

        } catch (Exception e) {

            response.put("status", "error");
            response.put("message", "Reserva no encontrada");

            return new ResponseEntity<>(
                    response,
                    HttpStatus.NOT_FOUND
            );
        }
    }

    // Agregar reserva
    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addReserva(
            @RequestBody ReservaEntity reserva) {

        Map<String, String> response = new HashMap<>();

        try {

            reservaService.save(reserva);

            response.put("status", "success");
            response.put("message",
                    "Reserva añadida con éxito");

            return new ResponseEntity<>(
                    response,
                    HttpStatus.CREATED
            );

        } catch (Exception e) {

            response.put("status", "error");
            response.put(
                    "message",
                    "Error al añadir la reserva: "
                            + e.getMessage()
            );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // Actualizar reserva
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updateReserva(
            @PathVariable int id,
            @RequestBody ReservaEntity reserva) {

        Map<String, String> response = new HashMap<>();

        try {

            reservaService.update(id, reserva);

            response.put("status", "success");
            response.put(
                    "message",
                    "Reserva actualizada con éxito"
            );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.OK
            );

        } catch (Exception e) {

            response.put("status", "error");
            response.put(
                    "message",
                    "Reserva no encontrada"
            );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.NOT_FOUND
            );
        }
    }

    // Eliminar reserva
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteReserva(
            @PathVariable("id") int id) {

        Map<String, String> response = new HashMap<>();

        try {

            reservaService.delete(id);

            response.put("status", "success");
            response.put(
                    "message",
                    "Reserva eliminada con éxito"
            );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.OK
            );

        } catch (Exception e) {

            response.put("status", "error");
            response.put(
                    "message",
                    "Reserva no encontrada"
            );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.NOT_FOUND
            );
        }
    }
}