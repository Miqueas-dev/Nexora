package pe.edu.cibertec.nexora.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.cibertec.nexora.entity.Estado;
import pe.edu.cibertec.nexora.service.EstadoService;

@RestController
@RequestMapping("/api/estados")
public class EstadoRestController {
	
	private final EstadoService estadoService;
	
	public EstadoRestController(EstadoService estadoService) {
		this.estadoService=estadoService;
	}
	
	//Endpoint: GET /api/marcas
		@GetMapping
		public ResponseEntity<List<Estado>> listarEstados(){
			List<Estado> estados=estadoService.listar();
			return ResponseEntity.ok(estados);
		}
	
	
	
	
}
