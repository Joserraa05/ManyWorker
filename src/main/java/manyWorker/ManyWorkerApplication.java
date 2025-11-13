package manyWorker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import manyWorker.service.CategoriaService;

@SpringBootApplication
public class ManyWorkerApplication implements CommandLineRunner {

	@Autowired
	private CategoriaService categoriaService;

	public static void main(String[] args) {
		SpringApplication.run(ManyWorkerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Inicializar categorías por defecto al arrancar la aplicación
		categoriaService.inicializarCategorias();
	}
}