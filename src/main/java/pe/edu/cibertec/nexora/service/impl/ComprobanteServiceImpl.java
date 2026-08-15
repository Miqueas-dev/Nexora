package pe.edu.cibertec.nexora.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.cibertec.nexora.dto.ComprobanteResponseDTO;
import pe.edu.cibertec.nexora.dto.DetalleComprobanteResponseDTO;
import pe.edu.cibertec.nexora.dto.DetalleVentaRequestDTO;
import pe.edu.cibertec.nexora.dto.VentaRequestDTO;
import pe.edu.cibertec.nexora.entity.Comprobante;
import pe.edu.cibertec.nexora.entity.DetalleComprobante;
import pe.edu.cibertec.nexora.entity.Producto;
import pe.edu.cibertec.nexora.entity.Usuario;
import pe.edu.cibertec.nexora.repository.ComprobanteRepository;
import pe.edu.cibertec.nexora.repository.DetalleComprobanteRepository;
import pe.edu.cibertec.nexora.repository.ProductoRepository;
import pe.edu.cibertec.nexora.repository.UsuarioRepository;
import pe.edu.cibertec.nexora.service.ComprobanteService;

@Service
public class ComprobanteServiceImpl implements ComprobanteService {

    @Autowired
    private ComprobanteRepository comprobanteRepository;

    @Autowired
    private DetalleComprobanteRepository detalleComprobanteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    @Transactional
    public ComprobanteResponseDTO registrar(VentaRequestDTO venta) {
        if (venta == null) {
            throw new IllegalArgumentException(
                    "La venta no puede ser nula.");
        }

        if (venta.getIdUsuario() == null) {
            throw new IllegalArgumentException(
                    "Debe indicar el usuario de la venta.");
        }

        Usuario usuario = usuarioRepository.findById(venta.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException(
                        "El Usuario con ID "
                                + venta.getIdUsuario()
                                + " no existe."));

        if (venta.getDetalles() == null
                || venta.getDetalles().isEmpty()) {
            throw new IllegalArgumentException(
                    "La venta debe contener al menos un detalle.");
        }

        Map<Integer, Integer> cantidades = new LinkedHashMap<>();

        for (DetalleVentaRequestDTO detalle : venta.getDetalles()) {
            if (detalle == null || detalle.getIdProducto() == null) {
                throw new IllegalArgumentException(
                        "Cada detalle debe indicar un producto.");
            }

            if (detalle.getCantidad() <= 0) {
                throw new IllegalArgumentException(
                        "La cantidad debe ser mayor que cero.");
            }

            try {
                cantidades.merge(
                        detalle.getIdProducto(),
                        detalle.getCantidad(),
                        (actual, nueva) -> Math.addExact(actual, nueva));
            } catch (ArithmeticException e) {
                throw new IllegalArgumentException(
                        "La cantidad total solicitada es demasiado grande.",
                        e);
            }
        }

        Map<Integer, Producto> productos = new LinkedHashMap<>();
        List<Integer> idsProducto = new ArrayList<>(cantidades.keySet());
        idsProducto.sort(Integer::compareTo);

        for (Integer idProducto : idsProducto) {
            Producto producto = productoRepository
                    .buscarPorIdParaVenta(idProducto)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "El Producto con ID "
                                    + idProducto
                                    + " no existe."));
            productos.put(idProducto, producto);
        }

        for (Map.Entry<Integer, Integer> cantidad : cantidades.entrySet()) {
            Producto producto = productos.get(cantidad.getKey());
            if (cantidad.getValue() > producto.getStockProducto()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para el producto "
                                + producto.getDescripProducto() + ".");
            }
        }

        Comprobante comprobante = new Comprobante();
        comprobante.setFechaComprobante(
                new Date(System.currentTimeMillis()));
        comprobante.setUsuario(usuario);
        comprobante = comprobanteRepository.save(comprobante);

        BigDecimal total = BigDecimal.ZERO;
        List<DetalleComprobanteResponseDTO> detallesRespuesta =
                new ArrayList<>();

        for (DetalleVentaRequestDTO detalleVenta : venta.getDetalles()) {
            Producto producto = productos.get(
                    detalleVenta.getIdProducto());

            DetalleComprobante detalle = new DetalleComprobante();
            detalle.setCantidadProducto(detalleVenta.getCantidad());
            detalle.setPrecioVenta(producto.getPrecioProducto());
            detalle.setComprobante(comprobante);
            detalle.setProducto(producto);
            detalle = detalleComprobanteRepository.save(detalle);

            BigDecimal subtotal = producto.getPrecioProducto()
                    .multiply(BigDecimal.valueOf(
                            detalleVenta.getCantidad()));
            total = total.add(subtotal);
            detallesRespuesta.add(crearDetalleRespuesta(
                    detalle,
                    subtotal));
        }

        for (Map.Entry<Integer, Integer> cantidad : cantidades.entrySet()) {
            Producto producto = productos.get(cantidad.getKey());
            producto.setStockProducto(
                    producto.getStockProducto() - cantidad.getValue());
            productoRepository.save(producto);
        }

        return crearRespuesta(
                comprobante,
                detallesRespuesta,
                total);
    }

    private ComprobanteResponseDTO crearRespuesta(
            Comprobante comprobante,
            List<DetalleComprobanteResponseDTO> detalles,
            BigDecimal total) {

        ComprobanteResponseDTO respuesta = new ComprobanteResponseDTO();
        respuesta.setNumComprobante(comprobante.getNumComprobante());
        respuesta.setFechaComprobante(comprobante.getFechaComprobante());
        respuesta.setUsuario(comprobante.getUsuario());
        respuesta.setTotal(total);
        respuesta.setDetalles(detalles);
        return respuesta;
    }

    private DetalleComprobanteResponseDTO crearDetalleRespuesta(
            DetalleComprobante detalle,
            BigDecimal subtotal) {

        DetalleComprobanteResponseDTO respuesta =
                new DetalleComprobanteResponseDTO();
        respuesta.setIdDetalle(detalle.getIdDetalle());
        respuesta.setIdProducto(
                detalle.getProducto().getIdProducto());
        respuesta.setDescripcionProducto(
                detalle.getProducto().getDescripProducto());
        respuesta.setCantidad(detalle.getCantidadProducto());
        respuesta.setPrecioVenta(detalle.getPrecioVenta());
        respuesta.setSubtotal(subtotal);
        return respuesta;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobanteResponseDTO> listar() {
        List<ComprobanteResponseDTO> respuesta = new ArrayList<>();
        for (Comprobante comprobante : comprobanteRepository.findAll()) {
            respuesta.add(convertirComprobante(comprobante));
        }
        return respuesta;
    }

    @Override
    @Transactional(readOnly = true)
    public ComprobanteResponseDTO buscarPorId(Integer id) {
        Comprobante comprobante = comprobanteRepository
                .findById(id)
                .orElse(null);
        if (comprobante == null) {
            return null;
        }
        return convertirComprobante(comprobante);
    }

    private ComprobanteResponseDTO convertirComprobante(
            Comprobante comprobante) {

        List<DetalleComprobante> detalles = detalleComprobanteRepository
                .findByComprobanteNumComprobante(
                        comprobante.getNumComprobante());
        List<DetalleComprobanteResponseDTO> detallesRespuesta =
                new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (DetalleComprobante detalle : detalles) {
            BigDecimal subtotal = detalle.getPrecioVenta()
                    .multiply(BigDecimal.valueOf(
                            detalle.getCantidadProducto()));
            total = total.add(subtotal);
            detallesRespuesta.add(crearDetalleRespuesta(
                    detalle,
                    subtotal));
        }

        return crearRespuesta(
                comprobante,
                detallesRespuesta,
                total);
    }
}
