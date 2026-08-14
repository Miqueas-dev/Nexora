package pe.edu.cibertec.nexora.controller;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.nexora.entity.Tipo;
import pe.edu.cibertec.nexora.service.TipoService;

@RestController
@RequestMapping("/api/tipos")
public class TipoRestController {

	private final TipoService _tipoService;

	public TipoRestController(TipoService tipoService) {
		_tipoService = tipoService;
	}
	
	/* GET /api/tipos  →  LISTA TODOS LOS TIPOS */
	@GetMapping
	public ResponseEntity<List<Tipo>> listarTipos() {
		List<Tipo> tipos = _tipoService.listar();
		return ResponseEntity.ok(tipos);
	}
	
	/* GET /api/tipos/{id}  →  BUSCA UN TIPO POR ID */
	@GetMapping("/{id}")
	public ResponseEntity<?> buscarTipo(@PathVariable Integer id) {
		Tipo tipo = _tipoService.buscarPorId(id);
		if (tipo == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("[Proceso de busqueda] El tipo con ID " + id + " no existe.");
		}
		return ResponseEntity.ok(tipo);
	}
	
	/* GET /api/tipos/buscar?descripcion=xxx  →  BÚSQUEDA PARCIAL POR DESCRIPCIÓN */
	@GetMapping("/buscar")
	public ResponseEntity<List<Tipo>> buscarPorDescripcion(@RequestParam String descripcion) {
		List<Tipo> tipos = _tipoService.buscarPorDescripcion(descripcion);
		return ResponseEntity.ok(tipos);
	}

	/* ENDPOINT: POST /api/tipos  →  REGISTRA UN NUEVO TIPO */
	@PostMapping
	public ResponseEntity<Tipo> registrarTipo(@RequestBody Tipo tipo) {
		tipo.setIdTipo(null);
		Tipo nuevoTipo = _tipoService.guardar(tipo);
		return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTipo);
	}
	
	/* ENDPOINT: PUT /api/tipos/{id}  →  ACTUALIZA UN TIPO EXISTENTE */
	@PutMapping("/{id}")
	public ResponseEntity<?> actualizarTipo(@PathVariable Integer id, @RequestBody Tipo tipo) {
		Tipo tipoExistente = _tipoService.buscarPorId(id);
		if (tipoExistente == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("[Proceso de actualizado] El tipo con ID " + id + " no existe.");
		}
		tipo.setIdTipo(id);
		Tipo tipoActualizado = _tipoService.guardar(tipo);
		return ResponseEntity.ok(tipoActualizado);
	}

	/* DELETE /api/tipos/{id}  →  ELIMINA UN TIPO.
	SI ESTÁ EN USO POR USUARIOS, SE CAPTURA EN LA EXCEPCIÓN.
	*/
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminarTipo(@PathVariable Integer id) {
		Tipo tipoConsultado = _tipoService.buscarPorId(id);
		if (tipoConsultado == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("[Proceso de borrado] El tipo con ID " + id + " no existe.");
		}
		try {
			_tipoService.eliminar(id);
			return ResponseEntity.ok("¡Tipo eliminado correctamente!");
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("[Proceso de borrado] El tipo con ID " + id + " no se puede eliminar porque está en uso por uno o más usuarios.");
		}
	}
}
