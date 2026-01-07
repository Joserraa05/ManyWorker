package manyWorker.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import manyWorker.entity.ActorLogin;
import manyWorker.security.JWTUtils;

@RestController
@RequestMapping("/actor")
@Tag(name = "Autenticación", description = "Controlador para la autenticación de actores del sistema")
public class ActorController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Permite a cualquier actor autenticarse en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
        @ApiResponse(responseCode = "400", description = "Datos de login incorrectos"),
        @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public ResponseEntity<?> login(@RequestBody ActorLogin actorLogin) {
        try {
            if (actorLogin.getUsername() == null || actorLogin.getUsername().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El nombre de usuario es obligatorio");
                return ResponseEntity.badRequest().body(error);
            }
            
            if (actorLogin.getPassword() == null || actorLogin.getPassword().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "La contraseña es obligatoria");
                return ResponseEntity.badRequest().body(error);
            }

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    actorLogin.getUsername(), 
                    actorLogin.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtUtils.generateToken(authentication);
            String username = authentication.getName();
            String rol = authentication.getAuthorities().iterator().next().getAuthority();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Autenticación exitosa");
            response.put("token", token);
            response.put("token_type", "Bearer");
            response.put("username", username);
            response.put("rol", rol);
            response.put("timestamp", java.time.LocalDateTime.now().toString());

            return ResponseEntity.ok(response);

        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Credenciales inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/registro")
    @Operation(summary = "Registrar nuevo usuario", description = "Registra un nuevo cliente o trabajador")
    public ResponseEntity<?> registro(@RequestBody manyWorker.entity.Actor nuevoActor) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Endpoint de registro");
        return ResponseEntity.ok(response);
    }
}