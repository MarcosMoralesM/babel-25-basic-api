package com.helloworld.babel.restaurant.controllers.platos;

import com.helloworld.babel.restaurant.model.Plato;
import com.helloworld.babel.restaurant.servicios.platos.PlatosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Controlador de platos")
@RequestMapping("restaurante/platos")
public class PlatosControllerImpl implements PlatosController {

	@Schema(description = "Servicio de platos")
	private final PlatosService platosService;

	public PlatosControllerImpl(PlatosService platosService) {
		this.platosService = platosService;
	}


	@Override
	@GetMapping("")
	@Operation(summary = "Obtener lista de platos")
	public List<Plato> getPlatos() {
		List<Plato> platos = platosService.getPlatos();
		return platos;
	}


	@Override
	@GetMapping("/{id}")
	@Operation(summary = "Obtener plato a partir del ID")
	public Plato getPlatosById(@Parameter(description = "ID del plato a obtener por parametro", required = true)
								@PathVariable String id)
	{
		Optional<Plato> plato = platosService.getPlatosById(Integer.parseInt(id));
		if (plato.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plato no encontrado");
		} else {
			return plato.get();
		}
	}

	@Override
	@PutMapping("/{id}")
	@Operation(summary = "Actualizar plato")
	public ResponseEntity<Void> updatePlato(@Parameter(description = "ID del plato a actualizar por parametro", required = true)
											@PathVariable int id,
											@Parameter(description = "Plato para actualizar por parametro", required = true)
											@RequestBody Plato plato)
	{
		plato.setId(id);
		Optional<Plato> updatedPlato = platosService.updatePlato(plato);
		if (updatedPlato.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plato a actualizar no encontrado");
		} else {
			return ResponseEntity.
					noContent().
					header("Content-Location", "/restaurante/platos/" + plato.getId()).
					build();
		}
	}

	@Override
	@DeleteMapping("/{id}")
	@Operation(summary = "Borrar plato")
	public ResponseEntity<Void> deletePlato(@Parameter(description = "ID del plato a borrar por parametro", required = true)
											@PathVariable int id)
	{
		platosService.deletePlato(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	@PostMapping("")
	@Operation(summary = "Crear plato")
	public ResponseEntity<Long> createPlato(@Parameter(description = "ID del plato a crear por parametro", required = true)
											@RequestBody Plato plato)
	{
		long platoCreadoId = platosService.createPlato(plato);
		return ResponseEntity.
				created(URI.create("/restaurante/platos/" + platoCreadoId)).
				body(platoCreadoId);
	}
}
