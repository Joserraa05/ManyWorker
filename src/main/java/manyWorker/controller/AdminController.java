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
import manyWorker.entity.Admin;
import manyWorker.service.AdminService;

@RestController
@RequestMapping("/admin")
@Tag(name = "Administradores", description = "Controlador para la gestión de administradores")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    @Operation(summary = "Obtener todos los administradores")
    public ResponseEntity<List<Admin>> findAll() {
        return ResponseEntity.ok(adminService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar administrador por ID")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Administrador encontrado"),
            @ApiResponse(responseCode = "404", description = "Administrador no encontrado")
    })
    public ResponseEntity<Admin> findById(@PathVariable int id) {
        Optional<Admin> oAdmin = adminService.findById(id);
        
        if (oAdmin.isPresent()) return ResponseEntity.ok(oAdmin.get());
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo administrador")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "201", description = "Administrador creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos") 
    })
    public ResponseEntity<String> save(@RequestBody Admin admin) {
        Admin savedAdmin = adminService.save(admin);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Administrador creado correctamente con ID: " + savedAdmin.getId());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un administrador")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Administrador actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Administrador no encontrado")
    })
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Admin admin) {
        Admin updatedAdmin = adminService.update(id, admin);
        if (updatedAdmin == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Administrador actualizado correctamente");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un administrador")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Administrador eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Administrador no encontrado") 
    })
    public ResponseEntity<String> delete(@PathVariable int id) {
        if (!adminService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        adminService.delete(id);
        return ResponseEntity.ok("Administrador eliminado correctamente");
    }
}
