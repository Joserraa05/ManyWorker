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
import manyWorker.entity.Tarea;
import manyWorker.service.TareaService;

@RestController
@RequestMapping("/tareas")
@Tag(name = "Tareas", description = "Controlador para la gestión de tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @GetMapping
    @Operation(summary = "Obtener todas las tareas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tareas obtenida correctamente")
    })
    public ResponseEntity<List<Tarea>> findAll() {
        return ResponseEntity.ok(tareaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tarea por ID")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Tarea encontrada"),
        @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    public ResponseEntity<Tarea> findById(@PathVariable String id) {
        Optional<Tarea> oTarea = tareaService.findById(id);
        
        if (oTarea.isPresent()) return ResponseEntity.ok(oTarea.get());
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva tarea")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "Tarea creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<String> save(@RequestBody Tarea tarea) {
        Tarea savedTarea = tareaService.save(tarea);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Tarea creada correctamente con ID: " + savedTarea.getId());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una tarea")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Tarea actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    public ResponseEntity<String> update(@PathVariable String id, @RequestBody Tarea tarea) {
        Tarea updatedTarea = tareaService.update(id, tarea);
        if (updatedTarea == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarea no encontrada");
        }
        return ResponseEntity.ok("Tarea actualizada correctamente");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una tarea")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Tarea eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    public ResponseEntity<String> delete(@PathVariable String id) {
        if (!tareaService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarea no encontrada");
        }
        tareaService.delete(id);
        return ResponseEntity.ok("Tarea eliminada correctamente");
    }
}