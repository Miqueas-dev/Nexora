package pe.edu.cibertec.nexora.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import pe.edu.cibertec.nexora.dto.ComprobanteResponseDTO;
import pe.edu.cibertec.nexora.dto.VentaRequestDTO;
import pe.edu.cibertec.nexora.service.ComprobanteService;

@ExtendWith(MockitoExtension.class)
class ComprobanteRestControllerTests {

    @Mock
    private ComprobanteService comprobanteService;

    @InjectMocks
    private ComprobanteRestController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void configurarMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void postRetornaCreatedConTotalCalculado() throws Exception {
        when(comprobanteService.registrar(any(VentaRequestDTO.class)))
                .thenReturn(respuesta());

        mockMvc.perform(post("/api/comprobantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "idUsuario": 1,
                          "detalles": [
                            {"idProducto": 1, "cantidad": 2}
                          ]
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numComprobante").value(10))
                .andExpect(jsonPath("$.total").value(25.00));
    }

    @Test
    void postRetornaBadRequestCuandoElServicioRechazaLaVenta()
            throws Exception {

        when(comprobanteService.registrar(any(VentaRequestDTO.class)))
                .thenThrow(new IllegalArgumentException(
                        "Stock insuficiente."));

        mockMvc.perform(post("/api/comprobantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "idUsuario": 1,
                          "detalles": [
                            {"idProducto": 1, "cantidad": 99}
                          ]
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Stock insuficiente."));
    }

    @Test
    void noOcultaComoBadRequestUnFalloInterno() {
        when(comprobanteService.registrar(any(VentaRequestDTO.class)))
                .thenThrow(new RuntimeException("Fallo de base de datos"));

        assertThrows(
                RuntimeException.class,
                () -> controller.registrar(new VentaRequestDTO()));
    }

    @Test
    void getRetornaLaListaDeComprobantes() throws Exception {
        when(comprobanteService.listar())
                .thenReturn(List.of(respuesta()));

        mockMvc.perform(get("/api/comprobantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numComprobante").value(10));
    }

    @Test
    void getPorIdRetornaElComprobante() throws Exception {
        when(comprobanteService.buscarPorId(10))
                .thenReturn(respuesta());

        mockMvc.perform(get("/api/comprobantes/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(25.00));
    }

    @Test
    void getPorIdRetornaNotFoundCuandoNoExiste() throws Exception {
        when(comprobanteService.buscarPorId(999)).thenReturn(null);

        mockMvc.perform(get("/api/comprobantes/999"))
                .andExpect(status().isNotFound());
    }

    private ComprobanteResponseDTO respuesta() {
        ComprobanteResponseDTO respuesta = new ComprobanteResponseDTO();
        respuesta.setNumComprobante(10);
        respuesta.setFechaComprobante(Date.valueOf("2026-08-15"));
        respuesta.setTotal(new BigDecimal("25.00"));
        respuesta.setDetalles(new ArrayList<>());
        return respuesta;
    }
}
