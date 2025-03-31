package com.helloworld.babel.restaurant.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.*;

@Tag(name = "Modelo de plato")
public class Plato {

	@Schema(description = "Enumerado con la categoría del plato")
	public enum Categoria {
		PRIMER_PLATO ("Entrante"),
		SEGUNDO_PLATO ("Plato principal"),
		POSTRE ("Postre");

		private String descripcion;

		Categoria(String descripcion) {
			this.descripcion = descripcion;
		}

		@JsonValue
		public String getDescripcion() {
			return descripcion;
		}

		@JsonCreator
		public static Categoria fromDescripcion(String descripcion) {
			return switch (descripcion) {
				case "Entrante" -> PRIMER_PLATO;
				case "Plato principal" -> SEGUNDO_PLATO;
				case "Postre" -> POSTRE;
				default -> PRIMER_PLATO;
			};
		}

	}

	@PositiveOrZero(message = "El ID del plato ha de ser positivo")
	@Schema(description = "ID del plato", example = "1")
	private Integer id;

	@Schema(description = "Nombre del plato", example = "Pollo empanado")
	private String nombre;

	@PositiveOrZero(message = "El precio del plato ha de ser positivo")
	@Schema(description = "Precio del plato", example = "7.99")
	private double precio;

	@NotNull(message = "La categoría no puede ser nula")
	@Schema(description = "Categoría del plato", example = "Entrante")
	private Categoria categoria;

	public Plato(Integer id, String nombre, double precio, Categoria categoria) {
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		this.categoria = categoria;
	}

	public Integer getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public double getPrecio() {
		return precio;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public static Plato fromPlatoDAO(com.helloworld.babel.restaurant.daos.model.Plato plato) {

		Categoria categoria = switch (plato.categoria()) {
			case 1 -> Categoria.PRIMER_PLATO;
			case 2 -> Categoria.SEGUNDO_PLATO;
			case 3 -> Categoria.POSTRE;
			default -> Categoria.PRIMER_PLATO;
		};

		return new Plato(
				plato.id(),
				plato.nombre(),
				plato.precio(),
				categoria
		);
	}

	public com.helloworld.babel.restaurant.daos.model.Plato toPlatoDAO() {

		int cat = switch (this.getCategoria()) {
			case PRIMER_PLATO -> 1;
			case SEGUNDO_PLATO -> 2;
			case POSTRE -> 3;
		};

		return new com.helloworld.babel.restaurant.daos.model.Plato(
				this.getId(),
				this.getNombre(),
				this.getPrecio(),
				cat
		);
	}

}
