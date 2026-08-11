package pe.edu.cibertec.nexora.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.cibertec.nexora.entity.Marca;
import pe.edu.cibertec.nexora.service.MarcaService;

@RestController
@RequestMapping("/api/marcas")
public class MarcaRestController {
	
	private final MarcaService marcaService;
	
	public MarcaRestController(MarcaService marcaService) {
		this.marcaService=marcaService;
	}
	
	//Endpoint: GET /api/marcas
	@GetMapping
	public ResponseEntity<List<Marca>> listarMarcas(){
		List<Marca> marcas=marcaService.listar();
		return ResponseEntity.ok(marcas);
	}
	
	
}
