package pe.edu.cibertec.nexora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.cibertec.nexora.dto.VentaRequestDTO;
import pe.edu.cibertec.nexora.dto.ComprobanteResponseDTO;
import pe.edu.cibertec.nexora.service.ComprobanteService;

@RestController
@RequestMapping("/api/comprobantes")
public class ComprobanteRestController {

    @Autowired
    private ComprobanteService comprobanteService;

    @PostMapping
    public ResponseEntity<?> registrar(
            @RequestBody VentaRequestDTO venta) {
        try {
            ComprobanteResponseDTO comprobante =
                    comprobanteService.registrar(venta);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(comprobante);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(comprobanteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        ComprobanteResponseDTO comprobante =
                comprobanteService.buscarPorId(id);
        if (comprobante == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("El comprobante con ID "
                            + id + " no existe.");
        }
        return ResponseEntity.ok(comprobante);
    }
}
