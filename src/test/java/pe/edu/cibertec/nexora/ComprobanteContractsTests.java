package pe.edu.cibertec.nexora;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.persistence.LockModeType;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.Lock;

import pe.edu.cibertec.nexora.repository.ProductoRepository;

class ComprobanteContractsTests {

    @Test
    void disponeDeLosContratosNecesariosParaRegistrarVentas() {
        assertDoesNotThrow(() -> Class.forName(
                "pe.edu.cibertec.nexora.dto.DetalleVentaRequestDTO"));
        assertDoesNotThrow(() -> Class.forName(
                "pe.edu.cibertec.nexora.dto.VentaRequestDTO"));
        assertDoesNotThrow(() -> Class.forName(
                "pe.edu.cibertec.nexora.dto.DetalleComprobanteResponseDTO"));
        assertDoesNotThrow(() -> Class.forName(
                "pe.edu.cibertec.nexora.dto.ComprobanteResponseDTO"));
        assertDoesNotThrow(() -> Class.forName(
                "pe.edu.cibertec.nexora.repository.ComprobanteRepository"));
        assertDoesNotThrow(() -> Class.forName(
                "pe.edu.cibertec.nexora.repository.DetalleComprobanteRepository"));
        assertDoesNotThrow(() -> Class.forName(
                "pe.edu.cibertec.nexora.service.ComprobanteService"));
        assertDoesNotThrow(() -> Class.forName(
                "pe.edu.cibertec.nexora.service.impl.ComprobanteServiceImpl"));
        assertDoesNotThrow(() -> Class.forName(
                "pe.edu.cibertec.nexora.controller.ComprobanteRestController"));
    }

    @Test
    void bloqueaProductosDuranteElRegistroDeUnaVenta() throws Exception {
        Lock lock = ProductoRepository.class
                .getMethod("buscarPorIdParaVenta", Integer.class)
                .getAnnotation(Lock.class);

        assertEquals(LockModeType.PESSIMISTIC_WRITE, lock.value());
    }
}
