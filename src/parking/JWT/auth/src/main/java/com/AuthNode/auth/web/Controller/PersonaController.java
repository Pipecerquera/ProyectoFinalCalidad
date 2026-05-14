package com.AuthNode.auth.web.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.AuthNode.auth.Dto.LoginDto;
import com.AuthNode.auth.Entity.PersonaEntity;
import com.AuthNode.auth.IService.IPersonaService;
import com.AuthNode.auth.web.Config.JwtUtil;

@CrossOrigin(origins = "*")
@RestController    
@RequestMapping("/api/auth")
public class PersonaController {

    @Autowired
    private IPersonaService personaService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(PersonaController.class);
    
    @Autowired
    PersonaController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    
    @GetMapping("/all")
    public List<PersonaEntity> getAllLogin() throws Exception {
        return personaService.getAll();
    }
    
    @GetMapping("/all/pageable")
    public Page<PersonaEntity> getAllLoginPageable(Pageable pageable) throws Exception {
        return personaService.getAllPageable(pageable);
    }
 
    @GetMapping("/{id}")
    public PersonaEntity getLoginById(@PathVariable int id) throws Exception {
        return personaService.findById(id);
    }
    
    @PostMapping("/auth")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) throws Exception {
        logger.info("Attempting to authenticate user with email: " + loginDto.getEmail());

        Optional<PersonaEntity> userOptional = personaService.findByCorreo(loginDto.getEmail());

        if (userOptional.isPresent()) {
            PersonaEntity user = userOptional.get();
            logger.info("User found with email: " + user.getEmail());

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                logger.info("Passwords match for user: " + user.getEmail());
                UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
                Authentication authentication = this.authenticationManager.authenticate(login);
                String jwt = this.jwtUtil.create(loginDto.getEmail());

                Map<String, Object> response = new HashMap<>();
                response.put("message", "scs");
                response.put("id", user.getId());
                response.put("role", user.getRole());
                response.put("nombre", user.getFullname());
                response.put("estado", user.getStatus());
                response.put("nofiticaciones", user.getNotifications());
                response.put("token", jwt);
                return ResponseEntity.ok()
                        .header(HttpHeaders.AUTHORIZATION, jwt)
                        .body(response);
            } else {
                logger.info("Contraseña no coincide para el usuario: " + user.getEmail());
            }
        } else {
            logger.info("Usuario no encontrado: " + loginDto.getEmail());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not found");
    }

    @GetMapping("/verify")
public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
    String resultado = personaService.verifyEmail(token);
 
    return switch (resultado) {
        case "VERIFICADO_OK" -> ResponseEntity.ok(
            "<html><body style='font-family:Arial;text-align:center;padding:60px'>" +
            "<h2 style='color:#1a73e8'>✅ ¡Cuenta verificada!</h2>" +
            "<p>Tu cuenta ha sido activada. Ya puedes <a href='http://localhost:4200/auth'>iniciar sesión</a>.</p>" +
            "</body></html>"
        );
        case "YA_VERIFICADO" -> ResponseEntity.ok(
            "<html><body style='font-family:Arial;text-align:center;padding:60px'>" +
            "<h2 style='color:#f59e0b'>⚠️ Ya verificado</h2>" +
            "<p>Esta cuenta ya fue verificada anteriormente. <a href='http://localhost:4200/auth'>Inicia sesión</a>.</p>" +
            "</body></html>"
        );
        default -> ResponseEntity.badRequest().body(
            "<html><body style='font-family:Arial;text-align:center;padding:60px'>" +
            "<h2 style='color:#ef4444'>❌ Link inválido</h2>" +
            "<p>El enlace de verificación no es válido o ya expiró.</p>" +
            "</body></html>"
        );
    };
}
    
    @PostMapping("/add")
public ResponseEntity<Map<String, Object>> addLogin(@RequestBody PersonaEntity login) throws Exception {

    Map<String, Object> response = new HashMap<>();

    // Validar que el correo venga lleno
    if (login.getEmail() != null && !login.getEmail().isEmpty()) {

        // ✅ Verificar si el correo ya existe
        Optional<PersonaEntity> existente = personaService.findByCorreo(login.getEmail());

        if (existente.isPresent()) {
            response.put("estado", false);
            response.put("mensaje", "El correo ya está registrado");

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(response);
        }

        // Encriptar contraseña
        login.setPassword(passwordEncoder.encode(login.getPassword()));

        // Fechas
        login.setCreated_at(LocalDateTime.now());
        login.setUpdated_at(LocalDateTime.now());
        login.setLast_login(LocalDateTime.now());

        // Valores por defecto
        if (login.getLocate() == null || login.getLocate().isEmpty()) {
            login.setLocate("N/A");
        }

        if (login.getRole() == null || login.getRole().isEmpty()) {
            login.setRole("user");
        }

        // Guardar usuario
        personaService.save(login);

        response.put("estado", true);
        response.put("mensaje", "Usuario registrado con éxito");

        return ResponseEntity.ok(response);

    } else {

        response.put("estado", false);
        response.put("mensaje", "El correo es obligatorio");

        return ResponseEntity.badRequest().body(response);
    }
}

    @PutMapping("/a/{id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable int id, @RequestBody PersonaEntity login) {
        try {
            login.setPassword(passwordEncoder.encode(login.getPassword()));
            login.setUpdated_at(LocalDateTime.now());
            personaService.update(id, login);
            
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Perfil actualizado con éxito");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "No se encontró el recurso");

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/d/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        try {
            personaService.delete(id);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        }
    }
}