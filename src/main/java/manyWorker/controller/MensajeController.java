package manyWorker.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import manyWorker.entity.Mensaje;
import manyWorker.service.MensajeService;

// DTO para enviar mensajes (objeto con el mensaje)
class EnviarMensajeRequest {
    public int idRemitente;
    public int idDestinatario;
    public String asunto;
    public String cuerpo;
}

//DTO para la solicitud de broadcast
class BroadcastRequest {
	public int idRemitente;
	public String asunto;
	public String cuerpo;
}

@RestController
@RequestMapping("/mensajes")
public class MensajeController {

    @Autowired
    private MensajeService mensajeService;

    @GetMapping
    public ResponseEntity<List<Mensaje>> findAll() {
        return ResponseEntity.ok(mensajeService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar mensaje por ID")
    public ResponseEntity<Mensaje> findById(@PathVariable int id) {
        Optional<Mensaje> mensaje = mensajeService.findById(id);
        return mensaje.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Mensaje> save(@RequestBody Mensaje mensaje) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mensajeService.save(mensaje));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Optional<Mensaje> mensaje = mensajeService.findById(id);
        if (mensaje.isPresent()) {
            mensajeService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Enviar un mensaje
    @PostMapping("/enviar")
    public ResponseEntity<Mensaje> enviarMensaje(@RequestBody EnviarMensajeRequest request) {
        Mensaje nuevo = mensajeService.enviarMensaje(
                request.idRemitente, request.idDestinatario, request.asunto, request.cuerpo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // Bandeja de salida
    @GetMapping("/enviados/{actorId}")
    public ResponseEntity<List<Mensaje>> obtenerMensajesEnviados(@PathVariable("actorId") int id) {
        return ResponseEntity.ok(mensajeService.obtenerMensajesEnviados(id));
    }

    // Bandeja de entrada
    @GetMapping("/recibidos/{actorId}")
    public ResponseEntity<List<Mensaje>> obtenerMensajesRecibidos(@PathVariable("actorId") int id) {
        return ResponseEntity.ok(mensajeService.obtenerMensajesRecibidos(id));
    }
    
    // Enviar mensaje broadcast (solo admin)
    @PostMapping("/broadcast")
    public ResponseEntity<?> enviarBroadcast(@RequestBody BroadcastRequest request) {
        try {
            List<Mensaje> enviados = mensajeService.enviarBroadcast(
                    request.idRemitente, request.asunto, request.cuerpo);
            return ResponseEntity.status(HttpStatus.CREATED).body(enviados);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}