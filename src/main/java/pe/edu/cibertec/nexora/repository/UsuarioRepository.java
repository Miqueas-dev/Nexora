package pe.edu.cibertec.nexora.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.cibertec.nexora.entity.Usuario;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByDniUsuario(
            String dniUsuario);

    Optional<Usuario> findByCorreoUsuario(
            String correoUsuario);

    List<Usuario> findByTipoIdTipo(
            Integer idTipo);

    List<Usuario> findByEstadoIdEstado(
            Integer idEstado);

    boolean existsByDniUsuarioAndIdUsuarioNot(
            String dniUsuario,
            Integer idUsuario);

    boolean existsByCorreoUsuarioAndIdUsuarioNot(
            String correoUsuario,
            Integer idUsuario);

    // Para el módulo de ventas
    List<Usuario> findByTipoDescripcionIgnoreCase(
            String descripcion);
}