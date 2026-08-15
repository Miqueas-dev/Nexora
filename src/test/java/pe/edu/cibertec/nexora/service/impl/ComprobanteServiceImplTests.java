package pe.edu.cibertec.nexora.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pe.edu.cibertec.nexora.dto.DetalleVentaRequestDTO;
import pe.edu.cibertec.nexora.dto.ComprobanteResponseDTO;
import pe.edu.cibertec.nexora.dto.VentaRequestDTO;
import pe.edu.cibertec.nexora.entity.Comprobante;
import pe.edu.cibertec.nexora.entity.DetalleComprobante;
import pe.edu.cibertec.nexora.entity.Producto;
import pe.edu.cibertec.nexora.entity.Usuario;
import pe.edu.cibertec.nexora.repository.ComprobanteRepository;
import pe.edu.cibertec.nexora.repository.DetalleComprobanteRepository;
import pe.edu.cibertec.nexora.repository.ProductoRepository;
import pe.edu.cibertec.nexora.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class ComprobanteServiceImplTests {

    @Mock
    private ComprobanteRepository comprobanteRepository;

    @Mock
    private DetalleComprobanteRepository detalleComprobanteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ComprobanteServiceImpl service;

    @Test
    void rechazaUnaSolicitudNulaSinGuardarComprobante() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> service.registrar(null));

        assertTrue(error.getMessage().contains("venta"));
        verify(comprobanteRepository, never()).save(
                org.mockito.ArgumentMatchers.any(Comprobante.class));
    }

    @Test
    void rechazaUnUsuarioInexistenteSinGuardarComprobante() {
        VentaRequestDTO venta = venta(99, detalle(1, 1));
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException error = assertThrows(
                RuntimeException.class,
                () -> service.registrar(venta));

        assertTrue(error.getMessage().contains("Usuario"));
        verify(comprobanteRepository, never()).save(
                org.mockito.ArgumentMatchers.any(Comprobante.class));
    }

    @Test
    void rechazaDetallesNulos() {
        Usuario usuario = new Usuario();
        VentaRequestDTO venta = venta(1);
        venta.setDetalles(null);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> service.registrar(venta));

        assertTrue(error.getMessage().contains("detalle"));
    }

    @Test
    void rechazaDetallesVacios() {
        Usuario usuario = new Usuario();
        VentaRequestDTO venta = venta(1);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> service.registrar(venta));

        assertTrue(error.getMessage().contains("detalle"));
    }

    @Test
    void rechazaUnProductoInexistente() {
        Usuario usuario = new Usuario();
        VentaRequestDTO venta = venta(1, detalle(99, 1));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(productoRepository.buscarPorIdParaVenta(99))
                .thenReturn(Optional.empty());

        RuntimeException error = assertThrows(
                RuntimeException.class,
                () -> service.registrar(venta));

        assertTrue(error.getMessage().contains("Producto"));
        verify(comprobanteRepository, never()).save(
                org.mockito.ArgumentMatchers.any(Comprobante.class));
    }

    @Test
    void rechazaUnaCantidadCero() {
        Usuario usuario = new Usuario();
        VentaRequestDTO venta = venta(1, detalle(1, 0));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> service.registrar(venta));

        assertTrue(error.getMessage().contains("cantidad"));
    }

    @Test
    void rechazaUnaCantidadNegativa() {
        Usuario usuario = new Usuario();
        VentaRequestDTO venta = venta(1, detalle(1, -5));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> service.registrar(venta));

        assertTrue(error.getMessage().contains("cantidad"));
    }

    @Test
    void rechazaStockInsuficienteSumandoProductosRepetidos() {
        Usuario usuario = new Usuario();
        Producto producto = producto(5, 6, "100.00");
        VentaRequestDTO venta = venta(
                1,
                detalle(5, 3),
                detalle(5, 4));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(productoRepository.buscarPorIdParaVenta(5))
                .thenReturn(Optional.of(producto));

        RuntimeException error = assertThrows(
                RuntimeException.class,
                () -> service.registrar(venta));

        assertTrue(error.getMessage().contains("Stock insuficiente"));
        assertEquals(6, producto.getStockProducto());
        verify(comprobanteRepository, never()).save(
                org.mockito.ArgumentMatchers.any(Comprobante.class));
    }

    @Test
    void registraVentaUsandoPreciosBdYDescuentaStockAcumulado() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        Producto producto1 = producto(1, 5, "10.00");
        Producto producto2 = producto(2, 10, "2.50");
        VentaRequestDTO venta = venta(
                1,
                detalle(1, 2),
                detalle(1, 1),
                detalle(2, 3));

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(productoRepository.buscarPorIdParaVenta(1))
                .thenReturn(Optional.of(producto1));
        when(productoRepository.buscarPorIdParaVenta(2))
                .thenReturn(Optional.of(producto2));
        when(comprobanteRepository.save(any(Comprobante.class)))
                .thenAnswer(invocacion -> {
                    Comprobante comprobante = invocacion.getArgument(0);
                    comprobante.setNumComprobante(10);
                    return comprobante;
                });
        AtomicInteger siguienteDetalle = new AtomicInteger(1);
        when(detalleComprobanteRepository.save(any(DetalleComprobante.class)))
                .thenAnswer(invocacion -> {
                    DetalleComprobante detalle = invocacion.getArgument(0);
                    detalle.setIdDetalle(siguienteDetalle.getAndIncrement());
                    return detalle;
                });

        ComprobanteResponseDTO respuesta = service.registrar(venta);

        assertNotNull(respuesta);
        assertEquals(10, respuesta.getNumComprobante());
        assertNotNull(respuesta.getFechaComprobante());
        assertSame(usuario, respuesta.getUsuario());
        assertEquals(new BigDecimal("37.50"), respuesta.getTotal());
        assertEquals(3, respuesta.getDetalles().size());
        assertEquals(new BigDecimal("20.00"),
                respuesta.getDetalles().get(0).getSubtotal());
        assertEquals(new BigDecimal("10.00"),
                respuesta.getDetalles().get(1).getSubtotal());
        assertEquals(new BigDecimal("7.50"),
                respuesta.getDetalles().get(2).getSubtotal());
        assertEquals(2, producto1.getStockProducto());
        assertEquals(7, producto2.getStockProducto());
        verify(productoRepository).save(producto1);
        verify(productoRepository).save(producto2);
    }

    @Test
    void rechazaElDesbordamientoDeLaCantidadAcumulada() {
        Usuario usuario = new Usuario();
        VentaRequestDTO venta = venta(
                1,
                detalle(1, Integer.MAX_VALUE),
                detalle(1, 1));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> service.registrar(venta));

        assertTrue(error.getMessage().contains("cantidad total"));
        verify(comprobanteRepository, never()).save(any(Comprobante.class));
    }

    @Test
    void listaComprobantesConDetallesYTotalReconstruido() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        Comprobante comprobante = new Comprobante();
        comprobante.setNumComprobante(10);
        comprobante.setFechaComprobante(Date.valueOf("2026-08-15"));
        comprobante.setUsuario(usuario);
        Producto producto = producto(1, 5, "12.50");
        DetalleComprobante detalle = new DetalleComprobante();
        detalle.setIdDetalle(20);
        detalle.setComprobante(comprobante);
        detalle.setProducto(producto);
        detalle.setCantidadProducto(2);
        detalle.setPrecioVenta(new BigDecimal("12.50"));
        when(comprobanteRepository.findAll())
                .thenReturn(List.of(comprobante));
        when(detalleComprobanteRepository
                .findByComprobanteNumComprobante(10))
                .thenReturn(List.of(detalle));

        List<ComprobanteResponseDTO> respuesta = service.listar();

        assertEquals(1, respuesta.size());
        assertEquals(new BigDecimal("25.00"),
                respuesta.get(0).getTotal());
        assertEquals(20,
                respuesta.get(0).getDetalles().get(0).getIdDetalle());
    }

    @Test
    void retornaNullCuandoElComprobanteBuscadoNoExiste() {
        when(comprobanteRepository.findById(999))
                .thenReturn(Optional.empty());

        ComprobanteResponseDTO respuesta = service.buscarPorId(999);

        assertNull(respuesta);
    }

    private VentaRequestDTO venta(
            Integer idUsuario,
            DetalleVentaRequestDTO... detalles) {

        VentaRequestDTO venta = new VentaRequestDTO();
        venta.setIdUsuario(idUsuario);
        venta.setDetalles(new ArrayList<>(List.of(detalles)));
        return venta;
    }

    private DetalleVentaRequestDTO detalle(
            Integer idProducto,
            int cantidad) {

        DetalleVentaRequestDTO detalle = new DetalleVentaRequestDTO();
        detalle.setIdProducto(idProducto);
        detalle.setCantidad(cantidad);
        return detalle;
    }

    private Producto producto(
            Integer id,
            int stock,
            String precio) {

        Producto producto = new Producto();
        producto.setIdProducto(id);
        producto.setDescripProducto("Producto " + id);
        producto.setStockProducto(stock);
        producto.setPrecioProducto(new BigDecimal(precio));
        return producto;
    }
}
