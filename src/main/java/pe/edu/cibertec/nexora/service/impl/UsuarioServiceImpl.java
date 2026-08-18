package pe.edu.cibertec.nexora.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import pe.edu.cibertec.nexora.dto.RegistroClienteRequestDTO;
import pe.edu.cibertec.nexora.entity.Estado;
import pe.edu.cibertec.nexora.entity.Tipo;
import pe.edu.cibertec.nexora.entity.Usuario;
import pe.edu.cibertec.nexora.repository.EstadoRepository;
import pe.edu.cibertec.nexora.repository.TipoRepository;
import pe.edu.cibertec.nexora.repository.UsuarioRepository;
import pe.edu.cibertec.nexora.service.EstadoService;
import pe.edu.cibertec.nexora.service.TipoService;
import pe.edu.cibertec.nexora.service.UsuarioService;

@Service
public class UsuarioServiceImpl
        implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoService tipoService;

    @Autowired
    private EstadoService estadoService;

    @Autowired
    private TipoRepository tipoRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /* LISTA TODOS LOS USUARIOS */
    @Override
    public List<Usuario> listar() {

        return usuarioRepository.findAll();
    }

    /*
     * REGISTRA O ACTUALIZA UN USUARIO.
     * ESTE METODO ES USADO POR EL ADMINISTRADOR.
     */
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
                || usuario.getEstado()
                        .getIdEstado() == null) {

            throw new RuntimeException(
                    "El estado del usuario es obligatorio.");
        }

        Estado estado = estadoService.buscarPorId(
                usuario.getEstado().getIdEstado());

        if (estado == null) {

            throw new RuntimeException(
                    "El estado con ID "
                    + usuario.getEstado()
                            .getIdEstado()
                    + " no existe.");
        }

        usuario.setEstado(estado);

        // =========================
        // REGISTRO NUEVO
        // =========================

        if (usuario.getIdUsuario() == null) {

            if (usuario.getClaveUsuario() == null
                    || usuario.getClaveUsuario()
                            .isBlank()) {

                throw new RuntimeException(
                        "La clave del usuario es obligatoria.");
            }

            if (usuarioRepository
                    .findByDniUsuario(
                            usuario.getDniUsuario())
                    .isPresent()) {

                throw new RuntimeException(
                        "Ya existe un usuario registrado con el DNI "
                        + usuario.getDniUsuario()
                        + ".");
            }

            if (usuarioRepository
                    .findByCorreoUsuario(
                            usuario.getCorreoUsuario())
                    .isPresent()) {

                throw new RuntimeException(
                        "Ya existe un usuario registrado con el correo "
                        + usuario.getCorreoUsuario()
                        + ".");
            }

            usuario.setClaveUsuario(
                    passwordEncoder.encode(
                            usuario.getClaveUsuario()));
        }

        // =========================
        // ACTUALIZACION
        // =========================

        else {

            Usuario existente =
                    usuarioRepository
                            .findById(
                                    usuario.getIdUsuario())
                            .orElse(null);

            if (existente == null) {

                throw new RuntimeException(
                        "El usuario con ID "
                        + usuario.getIdUsuario()
                        + " no existe.");
            }

            if (usuarioRepository
                    .existsByDniUsuarioAndIdUsuarioNot(
                            usuario.getDniUsuario(),
                            usuario.getIdUsuario())) {

                throw new RuntimeException(
                        "Ya existe otro usuario con el DNI "
                        + usuario.getDniUsuario()
                        + ".");
            }

            if (usuarioRepository
                    .existsByCorreoUsuarioAndIdUsuarioNot(
                            usuario.getCorreoUsuario(),
                            usuario.getIdUsuario())) {

                throw new RuntimeException(
                        "Ya existe otro usuario con el correo "
                        + usuario.getCorreoUsuario()
                        + ".");
            }

            /*
             * Si no se envia clave nueva,
             * se conserva la existente.
             */
            if (usuario.getClaveUsuario() == null
                    || usuario.getClaveUsuario()
                            .isBlank()) {

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

    /* BUSCA POR ID */
    @Override
    public Usuario buscarPorId(Integer id) {

        return usuarioRepository
                .findById(id)
                .orElse(null);
    }

    /* ELIMINA POR ID */
    @Override
    @Transactional
    public void eliminar(Integer id) {

        Usuario usuario =
                usuarioRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "El usuario con ID "
                                        + id
                                        + " no existe."));

        usuarioRepository.delete(usuario);
    }

    /* BUSCA POR DNI */
    @Override
    public Usuario buscarPorDni(String dni) {

        return usuarioRepository
                .findByDniUsuario(dni)
                .orElse(null);
    }

    /* BUSCA POR CORREO */
    @Override
    public Usuario buscarPorCorreo(
            String correo) {

        return usuarioRepository
                .findByCorreoUsuario(correo)
                .orElse(null);
    }

    /* LISTA POR TIPO */
    @Override
    public List<Usuario> listarPorTipo(
            Integer idTipo) {

        Tipo tipo =
                tipoService.buscarPorId(idTipo);

        if (tipo == null) {

            throw new RuntimeException(
                    "El tipo con ID "
                    + idTipo
                    + " no existe.");
        }

        return usuarioRepository
                .findByTipoIdTipo(idTipo);
    }

    /* LISTA POR ESTADO */
    @Override
    public List<Usuario> listarPorEstado(
            Integer idEstado) {

        Estado estado =
                estadoService.buscarPorId(
                        idEstado);

        if (estado == null) {

            throw new RuntimeException(
                    "El estado con ID "
                    + idEstado
                    + " no existe.");
        }

        return usuarioRepository
                .findByEstadoIdEstado(
                        idEstado);
    }

    /*
     * LISTA SOLO USUARIOS CLIENTE.
     * USADO POR EL VENDEDOR AL REALIZAR UNA VENTA.
     */
    @Override
    public List<Usuario> listarClientes() {

        return usuarioRepository
                .findByTipoDescripcionIgnoreCase(
                        "Cliente");
    }

    /*
     * REGISTRO PUBLICO DEL CLIENTE.
     *
     * IMPORTANTE:
     * Angular NO envia Tipo ni Estado.
     *
     * Spring asigna obligatoriamente:
     * - Tipo = Cliente
     * - Estado = Activo
     */
    @Override
    @Transactional
    public Usuario registrarCliente(
            RegistroClienteRequestDTO registro) {

        if (registro == null) {

            throw new IllegalArgumentException(
                    "Los datos del registro son obligatorios.");
        }

        // =========================
        // BUSCAR TIPO CLIENTE
        // =========================

        Tipo tipoCliente =
                tipoRepository
                        .findByDescripcionIgnoreCase(
                                "Cliente")
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "No existe el tipo Cliente "
                                        + "configurado en el sistema."));

        // =========================
        // BUSCAR ESTADO ACTIVO
        // =========================

        Estado estadoActivo =
                estadoRepository
                        .findByDescripcionIgnoreCase(
                                "Activo")
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "No existe el estado Activo "
                                        + "configurado en el sistema."));

        // =========================
        // CREAR USUARIO CLIENTE
        // =========================

        Usuario cliente =
                new Usuario();

        if (registro.getDniUsuario() != null) {

            cliente.setDniUsuario(
                    registro.getDniUsuario()
                            .trim());
        }

        if (registro.getNombreUsuario() != null) {

            cliente.setNombreUsuario(
                    registro.getNombreUsuario()
                            .trim());
        }

        if (registro.getApepatUsuario() != null) {

            cliente.setApepatUsuario(
                    registro.getApepatUsuario()
                            .trim());
        }

        if (registro.getApematUsuario() != null) {

            cliente.setApematUsuario(
                    registro.getApematUsuario()
                            .trim());
        }

        if (registro.getCorreoUsuario() != null) {

            cliente.setCorreoUsuario(
                    registro.getCorreoUsuario()
                            .trim()
                            .toLowerCase());
        }

        cliente.setClaveUsuario(
                registro.getClaveUsuario());

        cliente.setFecnacUsuario(
                registro.getFecnacUsuario());

        /*
         * EL CLIENTE NO PUEDE ELEGIR ESTOS DATOS.
         */
        cliente.setTipo(tipoCliente);
        cliente.setEstado(estadoActivo);

        /*
         * Reutilizamos guardar().
         *
         * Asi mantenemos en un solo lugar:
         * - validaciones
         * - DNI unico
         * - correo unico
         * - BCrypt
         */
        return guardar(cliente);
    }
}