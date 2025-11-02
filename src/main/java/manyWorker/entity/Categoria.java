package manyWorker.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class Categoria {
	//Comprobamos que el formato sea correcto por si se introduce manualmente
	@Id
	@Pattern(regexp = "\\d{6}-[A-Z0-9]{6}", message = "Formato de ID inválido (yyMMdd-XXXXXX)")
	private String id;
	@NotBlank
	private String titulo;
    
    private String leyesAplicables;

    @OneToMany
    private List<Tarea> tareas = new ArrayList<>();

	public Categoria(String id, @NotBlank String titulo, String leyesAplicables, List<Tarea> tareas) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.leyesAplicables = leyesAplicables;
		this.tareas = tareas;
	}

	public Categoria() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getLeyesAplicables() {
		return leyesAplicables;
	}

	public void setLeyesAplicables(String leyesAplicables) {
		this.leyesAplicables = leyesAplicables;
	}

	public List<Tarea> getTareas() {
		return tareas;
	}

	public void setTareas(List<Tarea> tareas) {
		this.tareas = tareas;
	}    
    
}
