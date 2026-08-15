package pe.edu.cibertec.nexora.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.cibertec.nexora.dto.LoginRequestDTO;
import pe.edu.cibertec.nexora.dto.LoginResponseDTO;
import pe.edu.cibertec.nexora.entity.Usuario;
import pe.edu.cibertec.nexora.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

	private final AuthenticationManager authenticationManager;

	private final UsuarioRepository usuarioRepository;

	public AuthRestController(AuthenticationManager authenticationManager, UsuarioRepository usuarioRepository) {

		this.authenticationManager = authenticationManager;

		this.usuarioRepository = usuarioRepository;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO login, HttpServletRequest request) {

		try {

			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(login.getCorreo(), login.getClave()));

			SecurityContext context = SecurityContextHolder.createEmptyContext();

			context.setAuthentication(authentication);

			SecurityContextHolder.setContext(context);

			request.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
					context);

			Usuario usuario = usuarioRepository.findByCorreoUsuario(authentication.getName()).orElseThrow();

			String rol = authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

			return ResponseEntity.ok(new LoginResponseDTO(usuario.getIdUsuario(), usuario.getNombreUsuario(),
					usuario.getCorreoUsuario(), rol));

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Correo o contraseña incorrectos.");
		}
	}

	@GetMapping("/me")
	public ResponseEntity<?> usuarioActual(Authentication authentication) {

		if (authentication == null || !authentication.isAuthenticated()) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		Usuario usuario = usuarioRepository.findByCorreoUsuario(authentication.getName()).orElse(null);

		if (usuario == null) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		String rol = authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

		return ResponseEntity.ok(new LoginResponseDTO(usuario.getIdUsuario(), usuario.getNombreUsuario(),
				usuario.getCorreoUsuario(), rol));
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {

		if (request.getSession(false) != null) {

			request.getSession(false).invalidate();
		}

		SecurityContextHolder.clearContext();

		return ResponseEntity.ok("Sesión cerrada correctamente.");
	}
}