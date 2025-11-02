package manyWorker.entity;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Future;

@Entity
public class Tarea {
	@Id
	private String id; // formato "yymmdd-xxxxxx"
	
	private LocalDate fechaPublicacion;
	
	private String descripcion;
	private String direccion;
	
	private Double precioMax;
	
	@Future
	private LocalDate fechaFin;
	
	@ManyToOne
	private Categoria categoria;
	
	// Generador automático de ID antes de persistir
	@PrePersist
	public void generarId() {
	    String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
	    String random = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
	    this.id = fecha + "-" + random;
	    this.fechaPublicacion = LocalDate.now();
	}
	
	public Tarea(String id, LocalDate fechaPublicacion, String descripcion, String direccion, Double precioMax,
			@Future LocalDate fechaFin, Categoria categoria) {
		super();
		this.id = id;
		this.fechaPublicacion = fechaPublicacion;
		this.descripcion = descripcion;
		this.direccion = direccion;
		this.precioMax = precioMax;
		this.fechaFin = fechaFin;
		this.categoria = categoria;
	}
	
	public Tarea() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDate getFechaPublicacion() {
		return fechaPublicacion;
	}

	public void setFechaPublicacion(LocalDate fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Double getPrecioMax() {
		return precioMax;
	}

	public void setPrecioMax(Double precioMax) {
		this.precioMax = precioMax;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

}
