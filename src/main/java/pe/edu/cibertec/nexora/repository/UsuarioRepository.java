package pe.edu.cibertec.nexora.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.nexora.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	
	/* BUSCA UN USUARIO POR SU DNI (ÚNICO). SIRVE PARA VALIDAR DUPLICADOS AL REGISTRAR/ACTUALIZAR */
	Optional<Usuario> findByDniUsuario(String dniUsuario);

	/* BUSCA UN USUARIO POR SU CORREO (ÚNICO). SIRVE PARA VALIDAR DUPLICADOS AL REGISTRAR/ACTUALIZAR */
	Optional<Usuario> findByCorreoUsuario(String correoUsuario);

	/* LISTA USUARIOS SEGÚN EL ID DEL TIPO. SIRVE PARA CONSULTAS POR ROL (ADMIN/CLIENTE) */
	List<Usuario> findByTipoIdTipo(Integer idTipo);

	/* LISTA USUARIOS SEGÚN EL ID DEL ESTADO. SIRVE PARA FILTRAR ACTIVOS/INACTIVOS */
	List<Usuario> findByEstadoIdEstado(Integer idEstado);

	/* VERIFICA SI YA EXISTE UN USUARIO CON ESE DNI (EXCLUYENDO UN ID DADO). ÚTIL EN ACTUALIZACIÓN */
	boolean existsByDniUsuarioAndIdUsuarioNot(String dniUsuario, Integer idUsuario);

	/* VERIFICA SI YA EXISTE UN USUARIO CON ESE CORREO (EXCLUYENDO UN ID DADO). ÚTIL EN ACTUALIZACIÓN */
	boolean existsByCorreoUsuarioAndIdUsuarioNot(String correoUsuario, Integer idUsuario);
}
