package pe.edu.cibertec.nexora.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	//Endpoint: GET /api/marcas/{id}
	@GetMapping("/{id}")
	public ResponseEntity<?> buscarMarca(@PathVariable Integer id){
		Marca marca = marcaService.buscarPorId(id);
		if (marca==null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La marca con ID "+id+" no existe.");
		return ResponseEntity.ok(marca);
	}
	
	//Endpoint: POST /api/marcas
	@PostMapping
	public ResponseEntity<Marca> registrarMarca(@RequestBody Marca marca){
		marca.setIdMarca(null);
		Marca nuevaMarca = marcaService.guardar(marca);
		return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMarca);
	}
	
	
	
	
	
}
