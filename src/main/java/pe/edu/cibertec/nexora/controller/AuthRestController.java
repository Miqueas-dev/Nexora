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
import pe.edu.cibertec.nexora.dto.RegistroClienteRequestDTO;
import pe.edu.cibertec.nexora.entity.Usuario;
import pe.edu.cibertec.nexora.repository.UsuarioRepository;
import pe.edu.cibertec.nexora.service.UsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final AuthenticationManager
            authenticationManager;

    private final UsuarioRepository
            usuarioRepository;

    private final UsuarioService
            usuarioService;

    public AuthRestController(
            AuthenticationManager authenticationManager,
            UsuarioRepository usuarioRepository,
            UsuarioService usuarioService) {

        this.authenticationManager =
                authenticationManager;

        this.usuarioRepository =
                usuarioRepository;

        this.usuarioService =
                usuarioService;
    }

    /*
     * LOGIN PARA CUALQUIER USUARIO:
     * ADMIN, VENDEDOR O CLIENTE.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequestDTO login,
            HttpServletRequest request) {

        try {

            Authentication authentication =
                    authenticationManager
                            .authenticate(
                                    new UsernamePasswordAuthenticationToken(
                                            login.getCorreo(),
                                            login.getClave()
                                    )
                            );

            SecurityContext context =
                    SecurityContextHolder
                            .createEmptyContext();

            context.setAuthentication(
                    authentication);

            SecurityContextHolder
                    .setContext(context);

            request.getSession(true)
                    .setAttribute(
                            HttpSessionSecurityContextRepository
                                    .SPRING_SECURITY_CONTEXT_KEY,
                            context
                    );

            Usuario usuario =
                    usuarioRepository
                            .findByCorreoUsuario(
                                    authentication.getName())
                            .orElseThrow();

            String rol =
                    authentication
                            .getAuthorities()
                            .iterator()
                            .next()
                            .getAuthority()
                            .replace(
                                    "ROLE_",
                                    "");

            return ResponseEntity.ok(
                    new LoginResponseDTO(
                            usuario.getIdUsuario(),
                            usuario.getNombreUsuario(),
                            usuario.getCorreoUsuario(),
                            rol
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(
                            HttpStatus.UNAUTHORIZED)
                    .body(
                            "Correo o contraseña incorrectos.");
        }
    }

    /*
     * REGISTRO PUBLICO.
     *
     * SIEMPRE CREA:
     * Tipo = Cliente
     * Estado = Activo
     */
    @PostMapping("/registro-cliente")
    public ResponseEntity<?> registrarCliente(
            @RequestBody
            RegistroClienteRequestDTO registro) {

        try {

            Usuario cliente =
                    usuarioService
                            .registrarCliente(
                                    registro);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(cliente);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    /*
     * DEVUELVE EL USUARIO
     * DE LA SESION ACTUAL.
     */
    @GetMapping("/me")
    public ResponseEntity<?> usuarioActual(
            Authentication authentication) {

        if (authentication == null
                || !authentication
                        .isAuthenticated()) {

            return ResponseEntity
                    .status(
                            HttpStatus.UNAUTHORIZED)
                    .build();
        }

        Usuario usuario =
                usuarioRepository
                        .findByCorreoUsuario(
                                authentication
                                        .getName())
                        .orElse(null);

        if (usuario == null) {

            return ResponseEntity
                    .status(
                            HttpStatus.UNAUTHORIZED)
                    .build();
        }

        String rol =
                authentication
                        .getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority()
                        .replace(
                                "ROLE_",
                                "");

        return ResponseEntity.ok(
                new LoginResponseDTO(
                        usuario.getIdUsuario(),
                        usuario.getNombreUsuario(),
                        usuario.getCorreoUsuario(),
                        rol
                )
        );
    }

    /*
     * CIERRA LA SESION.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request) {

        if (request.getSession(false)
                != null) {

            request.getSession(false)
                    .invalidate();
        }

        SecurityContextHolder
                .clearContext();

        return ResponseEntity.ok(
                "Sesión cerrada correctamente.");
    }
}