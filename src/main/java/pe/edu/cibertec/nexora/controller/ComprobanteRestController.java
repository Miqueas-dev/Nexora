package pe.edu.cibertec.nexora.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.cibertec.nexora.dto.ComprobanteResponseDTO;
import pe.edu.cibertec.nexora.dto.VentaRequestDTO;
import pe.edu.cibertec.nexora.service.ComprobanteService;

@RestController
@RequestMapping("/api/comprobantes")
public class ComprobanteRestController {

    private final ComprobanteService
            comprobanteService;

    public ComprobanteRestController(
            ComprobanteService comprobanteService) {

        this.comprobanteService =
                comprobanteService;
    }

    // VENDEDOR REGISTRA VENTA
    @PostMapping
    public ResponseEntity<?> registrar(
            @RequestBody VentaRequestDTO venta,
            Authentication authentication) {

        try {

            ComprobanteResponseDTO comprobante =
                    comprobanteService.registrar(
                            venta,
                            authentication.getName());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(comprobante);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // ADMIN VE TODAS LAS VENTAS
    @GetMapping
    public ResponseEntity<?> listar() {

        return ResponseEntity.ok(
                comprobanteService.listar());
    }

    // ADMIN CONSULTA UNA VENTA
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(
            @PathVariable Integer id) {

        ComprobanteResponseDTO comprobante =
                comprobanteService
                    .buscarPorId(id);

        if (comprobante == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                        "El comprobante con ID "
                        + id
                        + " no existe.");
        }

        return ResponseEntity.ok(
                comprobante);
    }

    // CLIENTE VE SUS COMPRAS
    @GetMapping("/mios")
    public ResponseEntity<?> misComprobantes(
            Authentication authentication) {

        return ResponseEntity.ok(
                comprobanteService
                    .listarPorCliente(
                        authentication.getName())
        );
    }

    // VENDEDOR VE SUS VENTAS
    @GetMapping("/vendedor/mias")
    public ResponseEntity<?> misVentas(
            Authentication authentication) {

        return ResponseEntity.ok(
                comprobanteService
                    .listarPorVendedor(
                        authentication.getName())
        );
    }
}