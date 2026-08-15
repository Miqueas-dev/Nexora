package pe.edu.cibertec.nexora.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

	/*
	 * REGISTRA UNA VENTA.
	 *
	 * El cliente viene seleccionado desde el formulario de venta. El vendedor se
	 * obtiene del usuario autenticado mediante su correo.
	 */
	@Override
	@Transactional
	public ComprobanteResponseDTO registrar(VentaRequestDTO venta, String correoVendedor) {

		// =========================
		// VALIDAR VENTA
		// =========================

		if (venta == null) {
			throw new IllegalArgumentException("La venta no puede ser nula.");
		}

		if (venta.getIdUsuario() == null) {
			throw new IllegalArgumentException("Debe indicar el cliente de la venta.");
		}

		if (correoVendedor == null || correoVendedor.isBlank()) {

			throw new IllegalArgumentException("No se pudo identificar al vendedor.");
		}

		// =========================
		// VALIDAR CLIENTE
		// =========================

		Usuario cliente = usuarioRepository.findById(venta.getIdUsuario()).orElseThrow(
				() -> new IllegalArgumentException("El Usuario con ID " + venta.getIdUsuario() + " no existe."));

		if (cliente.getTipo() == null || cliente.getTipo().getDescripcion() == null
				|| !cliente.getTipo().getDescripcion().equalsIgnoreCase("Cliente")) {

			throw new IllegalArgumentException("El usuario seleccionado no es un cliente.");
		}

		if (cliente.getEstado() == null || cliente.getEstado().getDescripcion() == null
				|| !cliente.getEstado().getDescripcion().equalsIgnoreCase("Activo")) {

			throw new IllegalArgumentException("El cliente se encuentra inactivo.");
		}

		// =========================
		// VALIDAR VENDEDOR
		// =========================

		Usuario vendedor = usuarioRepository.findByCorreoUsuario(correoVendedor)
				.orElseThrow(() -> new IllegalArgumentException("El vendedor no existe."));

		if (vendedor.getTipo() == null || vendedor.getTipo().getDescripcion() == null
				|| !vendedor.getTipo().getDescripcion().equalsIgnoreCase("Vendedor")) {

			throw new IllegalArgumentException("El usuario autenticado no es un vendedor.");
		}

		if (vendedor.getEstado() == null || vendedor.getEstado().getDescripcion() == null
				|| !vendedor.getEstado().getDescripcion().equalsIgnoreCase("Activo")) {

			throw new IllegalArgumentException("El vendedor se encuentra inactivo.");
		}

		// =========================
		// VALIDAR DETALLES
		// =========================

		if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {

			throw new IllegalArgumentException("La venta debe contener al menos un detalle.");
		}

		/*
		 * Agrupamos las cantidades por producto.
		 *
		 * Si Angular envía el mismo producto varias veces, validamos el stock
		 * considerando la suma total.
		 */
		Map<Integer, Integer> cantidades = new LinkedHashMap<>();

		for (DetalleVentaRequestDTO detalle : venta.getDetalles()) {

			if (detalle == null || detalle.getIdProducto() == null) {

				throw new IllegalArgumentException("Cada detalle debe indicar un producto.");
			}

			if (detalle.getCantidad() <= 0) {

				throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
			}

			try {

				cantidades.merge(detalle.getIdProducto(), detalle.getCantidad(),
						(actual, nueva) -> Math.addExact(actual, nueva));

			} catch (ArithmeticException e) {

				throw new IllegalArgumentException("La cantidad total solicitada " + "es demasiado grande.", e);
			}
		}

		// =========================
		// OBTENER PRODUCTOS
		// =========================

		Map<Integer, Producto> productos = new LinkedHashMap<>();

		List<Integer> idsProducto = new ArrayList<>(cantidades.keySet());

		idsProducto.sort(Integer::compareTo);

		for (Integer idProducto : idsProducto) {

			Producto producto = productoRepository.buscarPorIdParaVenta(idProducto).orElseThrow(
					() -> new IllegalArgumentException("El Producto con ID " + idProducto + " no existe."));

			// Producto debe estar activo
			if (producto.getEstado() == null || producto.getEstado().getDescripcion() == null
					|| !producto.getEstado().getDescripcion().equalsIgnoreCase("Activo")) {

				throw new IllegalArgumentException(
						"El producto " + producto.getDescripProducto() + " se encuentra inactivo.");
			}

			productos.put(idProducto, producto);
		}

		// =========================
		// VALIDAR STOCK
		// =========================

		for (Map.Entry<Integer, Integer> cantidad : cantidades.entrySet()) {

			Producto producto = productos.get(cantidad.getKey());

			if (cantidad.getValue() > producto.getStockProducto()) {

				throw new IllegalArgumentException(
						"Stock insuficiente para el producto " + producto.getDescripProducto() + ".");
			}
		}

		// =========================
		// CREAR COMPROBANTE
		// =========================

		Comprobante comprobante = new Comprobante();

		comprobante.setFechaComprobante(new Date(System.currentTimeMillis()));

		// Cliente
		comprobante.setUsuario(cliente);

		// Vendedor autenticado
		comprobante.setVendedor(vendedor);

		comprobante = comprobanteRepository.save(comprobante);

		// =========================
		// CREAR DETALLES
		// =========================

		BigDecimal total = BigDecimal.ZERO;

		List<DetalleComprobanteResponseDTO> detallesRespuesta = new ArrayList<>();

		for (DetalleVentaRequestDTO detalleVenta : venta.getDetalles()) {

			Producto producto = productos.get(detalleVenta.getIdProducto());

			DetalleComprobante detalle = new DetalleComprobante();

			detalle.setCantidadProducto(detalleVenta.getCantidad());

			/*
			 * Guardamos el precio actual.
			 *
			 * Si el precio del producto cambia después, la venta conserva su precio
			 * histórico.
			 */
			detalle.setPrecioVenta(producto.getPrecioProducto());

			detalle.setComprobante(comprobante);

			detalle.setProducto(producto);

			detalle = detalleComprobanteRepository.save(detalle);

			BigDecimal subtotal = producto.getPrecioProducto().multiply(BigDecimal.valueOf(detalleVenta.getCantidad()));

			total = total.add(subtotal);

			detallesRespuesta.add(crearDetalleRespuesta(detalle, subtotal));
		}

		// =========================
		// DESCONTAR STOCK
		// =========================

		for (Map.Entry<Integer, Integer> cantidad : cantidades.entrySet()) {

			Producto producto = productos.get(cantidad.getKey());

			producto.setStockProducto(producto.getStockProducto() - cantidad.getValue());

			productoRepository.save(producto);
		}

		return crearRespuesta(comprobante, detallesRespuesta, total);
	}

	/*
	 * CONSTRUYE LA RESPUESTA DEL COMPROBANTE.
	 */
	private ComprobanteResponseDTO crearRespuesta(Comprobante comprobante, List<DetalleComprobanteResponseDTO> detalles,
			BigDecimal total) {

		ComprobanteResponseDTO respuesta = new ComprobanteResponseDTO();

		respuesta.setNumComprobante(comprobante.getNumComprobante());

		respuesta.setFechaComprobante(comprobante.getFechaComprobante());

		// Cliente
		respuesta.setUsuario(comprobante.getUsuario());

		// Vendedor
		respuesta.setVendedor(comprobante.getVendedor());

		respuesta.setTotal(total);

		respuesta.setDetalles(detalles);

		return respuesta;
	}

	/*
	 * CONSTRUYE LA RESPUESTA DE CADA DETALLE.
	 */
	private DetalleComprobanteResponseDTO crearDetalleRespuesta(DetalleComprobante detalle, BigDecimal subtotal) {

		DetalleComprobanteResponseDTO respuesta = new DetalleComprobanteResponseDTO();

		respuesta.setIdDetalle(detalle.getIdDetalle());

		respuesta.setIdProducto(detalle.getProducto().getIdProducto());

		respuesta.setDescripcionProducto(detalle.getProducto().getDescripProducto());

		respuesta.setCantidad(detalle.getCantidadProducto());

		respuesta.setPrecioVenta(detalle.getPrecioVenta());

		respuesta.setSubtotal(subtotal);

		return respuesta;
	}

	/*
	 * ADMINISTRADOR: LISTA TODOS LOS COMPROBANTES.
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ComprobanteResponseDTO> listar() {

		List<ComprobanteResponseDTO> respuesta = new ArrayList<>();

		for (Comprobante comprobante : comprobanteRepository.findAll()) {

			respuesta.add(convertirComprobante(comprobante));
		}

		return respuesta;
	}

	/*
	 * ADMINISTRADOR: BUSCA CUALQUIER COMPROBANTE POR ID.
	 */
	@Override
	@Transactional(readOnly = true)
	public ComprobanteResponseDTO buscarPorId(Integer id) {

		Comprobante comprobante = comprobanteRepository.findById(id).orElse(null);

		if (comprobante == null) {
			return null;
		}

		return convertirComprobante(comprobante);
	}

	/*
	 * CONVIERTE UN COMPROBANTE EXISTENTE EN SU DTO DE RESPUESTA.
	 */
	private ComprobanteResponseDTO convertirComprobante(Comprobante comprobante) {

		List<DetalleComprobante> detalles = detalleComprobanteRepository
				.findByComprobanteNumComprobante(comprobante.getNumComprobante());

		List<DetalleComprobanteResponseDTO> detallesRespuesta = new ArrayList<>();

		BigDecimal total = BigDecimal.ZERO;

		for (DetalleComprobante detalle : detalles) {

			BigDecimal subtotal = detalle.getPrecioVenta().multiply(BigDecimal.valueOf(detalle.getCantidadProducto()));

			total = total.add(subtotal);

			detallesRespuesta.add(crearDetalleRespuesta(detalle, subtotal));
		}

		return crearRespuesta(comprobante, detallesRespuesta, total);
	}

	/*
	 * CLIENTE: LISTA SOLO SUS PROPIAS COMPRAS.
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ComprobanteResponseDTO> listarPorCliente(String correo) {

		Usuario cliente = usuarioRepository.findByCorreoUsuario(correo)
				.orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado."));

		List<ComprobanteResponseDTO> respuesta = new ArrayList<>();

		for (Comprobante comprobante : comprobanteRepository.findByUsuarioIdUsuario(cliente.getIdUsuario())) {

			respuesta.add(convertirComprobante(comprobante));
		}

		return respuesta;
	}

	/*
	 * VENDEDOR: LISTA SOLO LAS VENTAS REALIZADAS POR EL VENDEDOR AUTENTICADO.
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ComprobanteResponseDTO> listarPorVendedor(String correo) {

		Usuario vendedor = usuarioRepository.findByCorreoUsuario(correo)
				.orElseThrow(() -> new IllegalArgumentException("Vendedor no encontrado."));

		List<ComprobanteResponseDTO> respuesta = new ArrayList<>();

		for (Comprobante comprobante : comprobanteRepository.findByVendedorIdUsuario(vendedor.getIdUsuario())) {

			respuesta.add(convertirComprobante(comprobante));
		}

		return respuesta;
	}
}