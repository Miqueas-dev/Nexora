package pe.edu.cibertec.nexora.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import pe.edu.cibertec.nexora.entity.Estado;
import pe.edu.cibertec.nexora.entity.Tipo;
import pe.edu.cibertec.nexora.entity.Usuario;
import pe.edu.cibertec.nexora.repository.UsuarioRepository;
import pe.edu.cibertec.nexora.service.EstadoService;
import pe.edu.cibertec.nexora.service.TipoService;
import pe.edu.cibertec.nexora.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoService tipoService;

    @Autowired
    private EstadoService estadoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Lista todos los usuarios registrados
    @Override
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    // Registra o actualiza un usuario
    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {

        // =========================
        // VALIDACIONES GENERALES
        // =========================

        if (usuario.getDniUsuario() == null
                || usuario.getDniUsuario().isBlank()) {

            throw new RuntimeException(
                    "El DNI del usuario es obligatorio.");
        }

        if (usuario.getDniUsuario().length() != 8) {

            throw new RuntimeException(
                    "El DNI debe tener exactamente 8 caracteres.");
        }

        if (usuario.getNombreUsuario() == null
                || usuario.getNombreUsuario().isBlank()) {

            throw new RuntimeException(
                    "El nombre del usuario es obligatorio.");
        }

        if (usuario.getApepatUsuario() == null
                || usuario.getApepatUsuario().isBlank()) {

            throw new RuntimeException(
                    "El apellido paterno del usuario es obligatorio.");
        }

        if (usuario.getApematUsuario() == null
                || usuario.getApematUsuario().isBlank()) {

            throw new RuntimeException(
                    "El apellido materno del usuario es obligatorio.");
        }

        if (usuario.getCorreoUsuario() == null
                || usuario.getCorreoUsuario().isBlank()) {

            throw new RuntimeException(
                    "El correo del usuario es obligatorio.");
        }

        if (usuario.getFecnacUsuario() == null) {

            throw new RuntimeException(
                    "La fecha de nacimiento del usuario es obligatoria.");
        }

        // =========================
        // VALIDAR TIPO
        // =========================

        if (usuario.getTipo() == null
                || usuario.getTipo().getIdTipo() == null) {

            throw new RuntimeException(
                    "El tipo de usuario es obligatorio.");
        }

        Tipo tipo = tipoService.buscarPorId(
                usuario.getTipo().getIdTipo());

        if (tipo == null) {

            throw new RuntimeException(
                    "El tipo con ID "
                    + usuario.getTipo().getIdTipo()
                    + " no existe.");
        }

        usuario.setTipo(tipo);

        // =========================
        // VALIDAR ESTADO
        // =========================

        if (usuario.getEstado() == null
                || usuario.getEstado().getIdEstado() == null) {

            throw new RuntimeException(
                    "El estado del usuario es obligatorio.");
        }

        Estado estado = estadoService.buscarPorId(
                usuario.getEstado().getIdEstado());

        if (estado == null) {

            throw new RuntimeException(
                    "El estado con ID "
                    + usuario.getEstado().getIdEstado()
                    + " no existe.");
        }

        usuario.setEstado(estado);

        // =========================
        // REGISTRO NUEVO
        // =========================

        if (usuario.getIdUsuario() == null) {

            // Para registrar sí es obligatorio enviar clave
            if (usuario.getClaveUsuario() == null
                    || usuario.getClaveUsuario().isBlank()) {

                throw new RuntimeException(
                        "La clave del usuario es obligatoria.");
            }

            // Validar DNI único
            if (usuarioRepository
                    .findByDniUsuario(usuario.getDniUsuario())
                    .isPresent()) {

                throw new RuntimeException(
                        "Ya existe un usuario registrado con el DNI "
                        + usuario.getDniUsuario() + ".");
            }

            // Validar correo único
            if (usuarioRepository
                    .findByCorreoUsuario(usuario.getCorreoUsuario())
                    .isPresent()) {

                throw new RuntimeException(
                        "Ya existe un usuario registrado con el correo "
                        + usuario.getCorreoUsuario() + ".");
            }

            // Encriptar clave antes de guardar
            usuario.setClaveUsuario(
                    passwordEncoder.encode(
                            usuario.getClaveUsuario()));
        }

        // =========================
        // ACTUALIZACIÓN
        // =========================
        else {

            Usuario existente = usuarioRepository
                    .findById(usuario.getIdUsuario())
                    .orElse(null);

            if (existente == null) {

                throw new RuntimeException(
                        "El usuario con ID "
                        + usuario.getIdUsuario()
                        + " no existe.");
            }

            // Validar que otro usuario no tenga el mismo DNI
            if (usuarioRepository
                    .existsByDniUsuarioAndIdUsuarioNot(
                            usuario.getDniUsuario(),
                            usuario.getIdUsuario())) {

                throw new RuntimeException(
                        "Ya existe otro usuario con el DNI "
                        + usuario.getDniUsuario() + ".");
            }

            // Validar que otro usuario no tenga el mismo correo
            if (usuarioRepository
                    .existsByCorreoUsuarioAndIdUsuarioNot(
                            usuario.getCorreoUsuario(),
                            usuario.getIdUsuario())) {

                throw new RuntimeException(
                        "Ya existe otro usuario con el correo "
                        + usuario.getCorreoUsuario() + ".");
            }

            /*
             * Si no se envía una nueva clave,
             * conserva la que ya existe.
             *
             * Si se envía una nueva clave,
             * se vuelve a encriptar.
             */
            if (usuario.getClaveUsuario() == null
                    || usuario.getClaveUsuario().isBlank()) {

                usuario.setClaveUsuario(
                        existente.getClaveUsuario());

            } else {

                usuario.setClaveUsuario(
                        passwordEncoder.encode(
                                usuario.getClaveUsuario()));
            }
        }

        return usuarioRepository.save(usuario);
    }

    // Busca usuario por ID
    @Override
    public Usuario buscarPorId(Integer id) {

        return usuarioRepository
                .findById(id)
                .orElse(null);
    }

    // Elimina usuario por ID
    @Override
    @Transactional
    public void eliminar(Integer id) {

        Usuario usuario = usuarioRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "El usuario con ID "
                                + id
                                + " no existe."));

        usuarioRepository.delete(usuario);
    }

    // Busca usuario por DNI
    @Override
    public Usuario buscarPorDni(String dni) {

        return usuarioRepository
                .findByDniUsuario(dni)
                .orElse(null);
    }

    // Busca usuario por correo
    @Override
    public Usuario buscarPorCorreo(String correo) {

        return usuarioRepository
                .findByCorreoUsuario(correo)
                .orElse(null);
    }

    // Lista usuarios por Tipo
    @Override
    public List<Usuario> listarPorTipo(Integer idTipo) {

        Tipo tipo = tipoService.buscarPorId(idTipo);

        if (tipo == null) {

            throw new RuntimeException(
                    "El tipo con ID "
                    + idTipo
                    + " no existe.");
        }

        return usuarioRepository
                .findByTipoIdTipo(idTipo);
    }

    // Lista usuarios por Estado
    @Override
    public List<Usuario> listarPorEstado(Integer idEstado) {

        Estado estado = estadoService.buscarPorId(idEstado);

        if (estado == null) {

            throw new RuntimeException(
                    "El estado con ID "
                    + idEstado
                    + " no existe.");
        }

        return usuarioRepository
                .findByEstadoIdEstado(idEstado);
    }

    @Override
    public List<Usuario> listarClientes() {

        return usuarioRepository
                .findByTipoDescripcionIgnoreCase(
                        "Cliente");
    }
}