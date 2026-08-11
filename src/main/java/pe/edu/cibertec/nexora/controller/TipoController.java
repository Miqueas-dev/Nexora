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
import pe.edu.cibertec.nexora.entity.Tipo;
import pe.edu.cibertec.nexora.service.TipoService;

@RestController
@RequestMapping("/tipos")
@CrossOrigin(origins = "*") // PERMITE PETICIONES DESDE ANGULAR (AJUSTAR ORÍGENES EN PRODUCCIÓN)
public class TipoController {
	
	@Autowired
    private TipoService tipoService;
	
	/* GET /TIPOS/LISTAR
	LISTA TODOS LOS TIPOS REGISTRADOS */
    @GetMapping("/listar")
    public List<Tipo> listar() {
        return tipoService.listar();
    }

    /* POST /TIPOS/REGISTRAR
    REGISTRA UN NUEVO TIPO */
    @PostMapping("/registrar")
    public Tipo registrar(@RequestBody Tipo tipo) {
        return tipoService.guardar(tipo);
    }

    /* GET /TIPOS/BUSCAR/{ID}
    BUSCA UN TIPO POR SU IDENTIFICADOR */
    @GetMapping("/buscar/{id}")
    public Tipo buscarPorId(@PathVariable Integer id) {
        return tipoService.buscarPorId(id);
    }

    /* PUT /TIPOS/ACTUALIZAR
    ACTUALIZA UN TIPO EXISTENTE */
    @PutMapping("/actualizar")
    public Tipo actualizar(@RequestBody Tipo tipo) {
        return tipoService.guardar(tipo);
    }

    /* DELETE /TIPOS/ELIMINAR/{ID}
    ELIMINA UN TIPO POR SU IDENTIFICADOR */
    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Integer id) {
        tipoService.eliminar(id);
    }

    /* GET /TIPOS/BUSCAR/DESCRIPCION?DESCRIPCION=TEXTO
    BUSCA TIPOS CUYA DESCRIPCIÓN CONTENGA EL TEXTO INDICADO (BÚSQUEDA PARCIAL, IGNORE CASE) */
    @GetMapping("/buscar/descripcion")
    public List<Tipo> buscarPorDescripcion(@RequestParam String descripcion) {
        return tipoService.buscarPorDescripcion(descripcion);
    }
}
