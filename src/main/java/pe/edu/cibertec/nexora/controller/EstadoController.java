package pe.edu.cibertec.nexora.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.nexora.entity.Estado;
import pe.edu.cibertec.nexora.service.EstadoService;

@RestController
@RequestMapping("/estados")
@CrossOrigin(origins = "*") // PERMITE PETICIONES DESDE ANGULAR (AJUSTAR ORÍGENES EN PRODUCCIÓN)
public class EstadoController {
	
	@Autowired
    private EstadoService estadoService;
	
	/* GET /ESTADOS/LISTAR
	LISTA TODOS LOS ESTADOS REGISTRADOS */
    @GetMapping("/listar")
    public List<Estado> listar() {
        return estadoService.listar();
    }

    /* POST /ESTADOS/REGISTRAR
    REGISTRA UN NUEVO ESTADO */
    @PostMapping("/registrar")
    public Estado registrar(@RequestBody Estado estado) {
        return estadoService.guardar(estado);
    }

    /* GET /ESTADOS/BUSCAR/{ID}
    BUSCA UN ESTADO POR SU IDENTIFICADOR */
    @GetMapping("/buscar/{id}")
    public Estado buscarPorId(@PathVariable Integer id) {
        return estadoService.buscarPorId(id);
    }

    /* PUT /ESTADOS/ACTUALIZAR
    ACTUALIZA UN ESTADO EXISTENTE */
    @PutMapping("/actualizar")
    public Estado actualizar(@RequestBody Estado estado) {
        return estadoService.guardar(estado);
    }

    /* DELETE /ESTADOS/ELIMINAR/{ID}
    ELIMINA UN ESTADO POR SU IDENTIFICADOR */
    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Integer id) {
        estadoService.eliminar(id);
    }

    /* GET /ESTADOS/BUSCAR/DESCRIPCION?DESCRIPCION=TEXTO
    BUSCA ESTADOS CUYA DESCRIPCIÓN CONTENGA EL TEXTO INDICADO (BÚSQUEDA PARCIAL, IGNORE CASE) */
    @GetMapping("/buscar/descripcion")
    public List<Estado> buscarPorDescripcion(@RequestParam String descripcion) {
        return estadoService.buscarPorDescripcion(descripcion);
    }
}
