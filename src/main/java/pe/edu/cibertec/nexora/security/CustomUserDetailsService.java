package pe.edu.cibertec.nexora.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.cibertec.nexora.entity.Usuario;
import pe.edu.cibertec.nexora.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(
            UsuarioRepository usuarioRepository) {

        this.usuarioRepository =
                usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(
            String correo)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository
                .findByCorreoUsuario(correo)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuario no encontrado."));

        if (usuario.getTipo() == null
                || usuario.getTipo()
                        .getDescripcion() == null) {

            throw new UsernameNotFoundException(
                    "El usuario no tiene un tipo asignado.");
        }

        String descripcion = usuario.getTipo()
                .getDescripcion()
                .trim()
                .toUpperCase();

        String rol;

        switch (descripcion) {

            case "ADMINISTRADOR":
                rol = "ADMIN";
                break;

            case "VENDEDOR":
                rol = "VENDEDOR";
                break;

            case "CLIENTE":
                rol = "CLIENTE";
                break;

            default:
                throw new UsernameNotFoundException(
                        "Tipo de usuario no válido.");
        }

        boolean activo =
                usuario.getEstado() != null
                && usuario.getEstado()
                        .getDescripcion() != null
                && usuario.getEstado()
                        .getDescripcion()
                        .equalsIgnoreCase("Activo");

        return User.builder()
                .username(
                        usuario.getCorreoUsuario())
                .password(
                        usuario.getClaveUsuario())
                .roles(rol)
                .disabled(!activo)
                .build();
    }
}