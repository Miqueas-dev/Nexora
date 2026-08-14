package pe.edu.cibertec.nexora.controller;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.nexora.entity.Usuario;
import pe.edu.cibertec.nexora.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {
	
	private final UsuarioService _usuarioService;
	
	public UsuarioRestController(UsuarioService usuarioService) {
		_usuarioService = usuarioService;
	}

	/* GET /api/usuarios → LISTA TODOS LOS USUARIOS */
	@GetMapping
	public ResponseEntity<List<Usuario>> listarUsuarios() {
		List<Usuario> usuarios = _usuarioService.listar();
		return ResponseEntity.ok(usuarios);
	}
	
	/* GET /api/usuarios/{id} → BUSCA UN USUARIO POR ID */
	@GetMapping("/{id}")
	public ResponseEntity<?> buscarUsuario(@PathVariable Integer id) {
		Usuario usuario = _usuarioService.buscarPorId(id);
		if (usuario == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("[Proceso de busqueda] El usuario con ID " + id + " no existe.");
		}
		return ResponseEntity.ok(usuario);
	}
	
	/* GET /api/usuarios/dni/{dni} → BUSCA USUARIO POR DNI */
	@GetMapping("/dni/{dni}")
	public ResponseEntity<?> buscarPorDni(@PathVariable String dni) {
		Usuario usuario = _usuarioService.buscarPorDni(dni);
		if (usuario == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("[Proceso de busqueda] No existe un usuario con DNI " + dni + ".");
		}
		return ResponseEntity.ok(usuario);
	}
	
	/* GET /api/usuarios/correo/{correo} → BUSCA USUARIO POR CORREO */
	@GetMapping("/correo/{correo}")
	public ResponseEntity<?> buscarPorCorreo(@PathVariable String correo) {
		Usuario usuario = _usuarioService.buscarPorCorreo(correo);
		if (usuario == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("[Proceso de busqueda] No existe un usuario con correo " + correo + ".");
		}
		return ResponseEntity.ok(usuario);
	}
	
	/* GET /api/usuarios/tipo/{idTipo} → LISTA USUARIOS POR TIPO */
	@GetMapping("/tipo/{idTipo}")
	public ResponseEntity<?> listarPorTipo(@PathVariable Integer idTipo) {
		try {
			List<Usuario> usuarios = _usuarioService.listarPorTipo(idTipo);
			return ResponseEntity.ok(usuarios);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	/* ENDPOINT: GET /api/usuarios/estado/{idEstado}  →  LISTA USUARIOS POR ESTADO (ACTIVOS/INACTIVOS) */
	@GetMapping("/estado/{idEstado}")
	public ResponseEntity<?> listarPorEstado(@PathVariable Integer idEstado) {
		try {
			List<Usuario> usuarios = _usuarioService.listarPorEstado(idEstado);
			return ResponseEntity.ok(usuarios);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	/* POST /api/usuarios  →  REGISTRA UN NUEVO USUARIO.
	VALIDACIONES: TIPO Y ESTADO DEBEN EXISTIR; DNI Y CORREO ÚNICOS. */
	@PostMapping
	public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
		try {
			usuario.setIdUsuario(null);
			Usuario nuevoUsuario = _usuarioService.guardar(usuario);
			return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	/* PUT /api/usuarios/{id}  →  ACTUALIZA UN USUARIO EXISTENTE.
	MISMAS VALIDACIONES QUE EL REGISTRO (TIPO, ESTADO, DNI/CORREO ÚNICOS). */
	@PutMapping("/{id}")
	public ResponseEntity<?> actualizarUsuario(@PathVariable Integer id, @RequestBody Usuario usuario) {
		Usuario usuarioExistente = _usuarioService.buscarPorId(id);
		if (usuarioExistente == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("[Proceso de actualizado] El usuario con ID " + id + " no existe.");
		}
		try {
			usuario.setIdUsuario(id);
			Usuario usuarioActualizado = _usuarioService.guardar(usuario);
			return ResponseEntity.ok(usuarioActualizado);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	/* DELETE /api/usuarios/{id}  →  ELIMINA UN USUARIO.
	SI TIENE COMPROBANTES U OTRAS RELACIONES, RETORNA A LA EXCEPCIÓN. */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminarUsuario(@PathVariable Integer id) {
		Usuario usuarioConsultado = _usuarioService.buscarPorId(id);
		if (usuarioConsultado == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("[Proceso de borrado] El usuario con ID " + id + " no existe.");
		}
		try {
			_usuarioService.eliminar(id);
			return ResponseEntity.ok("¡Usuario eliminado correctamente!");
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("[Proceso de borrado] El usuario con ID " + id + " no se puede eliminar porque está asociado a uno o más comprobantes u otras operaciones.");
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
