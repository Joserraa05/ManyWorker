package manyWorker.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import manyWorker.entity.Mensaje;
import manyWorker.repository.MensajeRepository;
import manyWorker.service.MensajeService;

// DTO para enviar mensajes
class EnviarMensajeRequest {
    public int idRemitente;
    public int idDestinatario;
    public String asunto;
    public String cuerpo;
}

// DTO para broadcast
class BroadcastRequest {
    public int idRemitente;
    public String asunto;
    public String cuerpo;
}

@RestController
@RequestMapping("/mensajes")
@Tag(name = "Mensajes", description = "Controlador para la gestión de mensajes")
public class MensajeController {

    @Autowired
    private MensajeService mensajeService;

    @Autowired
    private MensajeRepository mensajeRepository;

    @GetMapping
    @Operation(summary = "Obtener todos los mensajes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de mensajes obtenida correctamente")
    })
    public ResponseEntity<List<Mensaje>> findAll() {
        return ResponseEntity.ok(mensajeService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar mensaje por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mensaje encontrado"),
        @ApiResponse(responseCode = "404", description = "Mensaje no encontrado")
    })
    public ResponseEntity<Mensaje> findById(@PathVariable int id) {
        Optional<Mensaje> mensaje = mensajeService.findById(id);
        
        if (mensaje.isPresent()) {
            return ResponseEntity.ok(mensaje.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/enviar")
    @Operation(summary = "Enviar un mensaje entre actores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Mensaje enviado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<?> enviarMensaje(@RequestBody EnviarMensajeRequest request) {
        try {
            Mensaje nuevo = mensajeService.enviarMensaje(
                    request.idRemitente, request.idDestinatario, request.asunto, request.cuerpo);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/broadcast")
    @Operation(summary = "Enviar mensaje broadcast")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Mensajes broadcast enviados correctamente"),
        @ApiResponse(responseCode = "403", description = "No autorizado para enviar broadcast")
    })
    public ResponseEntity<?> enviarBroadcast(@RequestBody BroadcastRequest request) {
        try {
            List<Mensaje> enviados = mensajeService.enviarBroadcast(
                    request.idRemitente, request.asunto, request.cuerpo);
            return ResponseEntity.status(HttpStatus.CREATED).body(enviados);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un mensaje")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mensaje eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Mensaje no encontrado")
    })
    public ResponseEntity<String> delete(@PathVariable int id) {
        if (!mensajeService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mensaje no encontrado");
        }
        mensajeService.delete(id);
        return ResponseEntity.ok("Mensaje eliminado correctamente");
    }

    // Vamos a buscar por remitente y destinatario en vez de usar la bandeja de entrada
    @GetMapping("/remitente/{remitenteId}")
    @Operation(summary = "Buscar mensajes por remitente")
    public ResponseEntity<List<Mensaje>> findByRemitenteId(@PathVariable int remitenteId) {
        List<Mensaje> mensajes = mensajeRepository.findByRemitenteId(remitenteId);
        return ResponseEntity.ok(mensajes);
    }

    @GetMapping("/destinatario/{destinatarioId}")
    @Operation(summary = "Buscar mensajes por destinatario")
    public ResponseEntity<List<Mensaje>> findByDestinatarioId(@PathVariable int destinatarioId) {
        List<Mensaje> mensajes = mensajeRepository.findByDestinatarioId(destinatarioId);
        return ResponseEntity.ok(mensajes);
    }
}