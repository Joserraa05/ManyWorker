package manyWorker.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import manyWorker.entity.Cliente;
import manyWorker.service.ClienteService;

@RestController
@RequestMapping("/cliente")
@Tag(name = "Clientes", description = "Controlador para la gestión de clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Obtener todos los clientes")
    public ResponseEntity<List<Cliente>> findAll() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<Cliente> findById(@PathVariable int id) {
        Optional<Cliente> oCliente = clienteService.findById(id);
        
        if (oCliente.isPresent()) return ResponseEntity.ok(oCliente.get());
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo cliente")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "201", description = "Cliente creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos") 
    })
    public ResponseEntity<String> save(@RequestBody Cliente cliente) {
        Cliente savedCliente = clienteService.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Cliente creado correctamente con ID: " + savedCliente.getId());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un cliente")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Cliente cliente) {
        Cliente updatedCliente = clienteService.update(id, cliente);
        if (updatedCliente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Cliente actualizado correctamente");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un cliente")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Cliente eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado") 
    })
    public ResponseEntity<String> delete(@PathVariable int id) {
        if (!clienteService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clienteService.delete(id);
        return ResponseEntity.ok("Cliente eliminado correctamente");
    }
    
    @GetMapping("/exportar/{id}")
    @Operation(summary = "Exportar datos del cliente")
    public ResponseEntity<Map<String, Object>> exportarDatos(@PathVariable int id) {
        Map<String, Object> datos = clienteService.exportarDatos(id);
        return ResponseEntity.ok(datos);
    }
}