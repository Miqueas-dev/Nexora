package pe.edu.cibertec.nexora.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.cibertec.nexora.entity.Estado;
import pe.edu.cibertec.nexora.entity.Marca;
import pe.edu.cibertec.nexora.service.EstadoService;

@RestController
@RequestMapping("/api/estados")
public class EstadoRestController {
	
	private final EstadoService estadoService;
	
	public EstadoRestController(EstadoService estadoService) {
		this.estadoService=estadoService;
	}
	
	//Endpoint: GET /api/estados
	@GetMapping
	public ResponseEntity<List<Estado>> listarEstados(){
		List<Estado> estados=estadoService.listar();
		return ResponseEntity.ok(estados);
	}

	//Endpoint: GET /api/estados/{id}
	@GetMapping("/{id}")
	public ResponseEntity<?> buscarEstado(@PathVariable Integer id){
		Estado estado = estadoService.buscarPorId(id);
		if (estado==null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("[Proceso de busqueda] El estado con ID "+id+" no existe.");
		return ResponseEntity.ok(estado);
	}
	
	
}
