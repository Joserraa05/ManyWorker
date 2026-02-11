package manyWorker.service;

import manyWorker.entity.*;
import manyWorker.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    private final CategoriaRepository categoriaRepository;
    private final AdminRepository adminRepository;
    private final ClienteRepository clienteRepository;
    private final TrabajadorRepository trabajadorRepository;
    private final TutorialRepository tutorialRepository;

    // Inyección de dependencias por constructor
    public DataInitializer(CategoriaRepository categoriaRepository, 
                           AdminRepository adminRepository,
                           ClienteRepository clienteRepository, 
                           TrabajadorRepository trabajadorRepository,
                           TutorialRepository tutorialRepository, PasswordEncoder passwordEncoder) {
        this.categoriaRepository = categoriaRepository;
        this.adminRepository = adminRepository;
        this.clienteRepository = clienteRepository;
        this.trabajadorRepository = trabajadorRepository;
        this.tutorialRepository = tutorialRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("---------- INICIO DATA INITIALIZER ----------");

        // 1. BLOQUE DE CATEGORÍAS
        long numCategorias = categoriaRepository.count();
        if (numCategorias == 0) {
            System.out.println(">> No hay categorías. Creando...");
            populateCategorias();
        } else {
            System.out.println(">> Ya existen " + numCategorias + " categorías. Saltando creación.");
        }

        // 2. BLOQUE DE ACTORES (Admin, Cliente, Trabajador)
        long numTrabajadores = trabajadorRepository.count();
        if (numTrabajadores == 0) {
            System.out.println(">> No hay trabajadores. Creando Actores y Tutoriales...");
            populateActoresYTutoriales();
        } else {
            System.out.println(">> Ya existen actores. Saltando creación.");
        }

        System.out.println("---------- FIN DATA INITIALIZER ----------");
    }

    private void populateCategorias() {
        List<String> titulos = Arrays.asList(
            "Carpintería", "Reparación de techos", "Limpieza", "Puertas", "Cableado eléctrico",
            "Instalación de ventiladores", "Reparación de cercas", "Sistemas de seguridad para el hogar",
            "Instalación de aislamiento", "Reparaciones de lámparas", "Mudanzas", "Pintura",
            "Control de plagas", "Reparaciones de fontanería", "Techado", "Instalación de estanterías",
            "Paneles solares", "Insonorización", "Reparación de sistemas de riego", "Reparación de ventanas"
        );

        for (String titulo : titulos) {
            Categoria cat = new Categoria();
            cat.setTitulo(titulo);
            cat.setEsReparacion(true);
            cat.setLeyesAplicables("Normativa ISO-9001 y CTE.");
            categoriaRepository.save(cat);
        }
        System.out.println(">> Categorías insertadas correctamente.");
    }

    private void populateActoresYTutoriales() {
        // --- 1. Crear ADMIN ---
        Admin admin = new Admin();
        admin.setNombre("Super");
        admin.setApellido("Administrador");
        admin.setCorreo("admin@manyworker.com");
        admin.setTelefono("600000000");
        admin.setDireccion("Calle Principal 1");
        admin.setRol(Roles.ADMINISTRADOR);
        admin.setAuthority(Roles.ADMINISTRADOR.toString());
        admin.setUsername("admin"); 
        admin.setPassword(passwordEncoder.encode("admin123"));

        adminRepository.save(admin);
        System.out.println(">> Admin creado: admin@manyworker.com");

        // --- 2. Crear CLIENTE ---
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan");
        cliente.setApellido("Pérez");
        cliente.setRol(Roles.CLIENTE);
        cliente.setAuthority(Roles.CLIENTE.toString());
        cliente.setCorreo("juan@cliente.com");
        cliente.setTelefono("611223344");
        cliente.setDireccion("Avenida del Cliente 23");
        cliente.setUsername("juanperez");
        cliente.setPassword(passwordEncoder.encode("cliente123"));

        clienteRepository.save(cliente);
        System.out.println(">> Cliente creado: juan@cliente.com");

        // --- 3. Crear TRABAJADOR ---
        Trabajador trabajador = new Trabajador();
        trabajador.setNombre("Pepe");
        trabajador.setApellido("Gotera");
        trabajador.setCorreo("pepe@trabajador.com");
        trabajador.setRol(Roles.TRABAJADOR);
        trabajador.setAuthority(Roles.TRABAJADOR.toString());
        trabajador.setTelefono("699887766");
        trabajador.setDireccion("Polígono Industrial Norte");
        trabajador.setNombreComercial("Reformas Integrales Pepe S.L.");
        trabajador.setUsername("pepegotera");
        trabajador.setPassword(passwordEncoder.encode("trabajador123"));

        trabajador = trabajadorRepository.save(trabajador);
        System.out.println(">> Trabajador creado: pepe@trabajador.com");

        // --- 4. Crear TUTORIAL ---
        Tutorial tutorial = new Tutorial();
        tutorial.setTitulo("Cómo arreglar un grifo que gotea");
        tutorial.setResumen("Guía paso a paso para solucionar goteos en grifos monomando.");
        tutorial.setTexto("Primero cierra la llave de paso...");
        
        List<String> imagenes = new ArrayList<>();
        imagenes.add("https://valvulasarco.com/wp-content/uploads/2024/04/00415B.png");
        tutorial.setImagenes(imagenes);

        tutorial.setAutor(trabajador);

        tutorialRepository.save(tutorial);
        System.out.println(">> Tutorial creado vinculado a Pepe.");
    }
}