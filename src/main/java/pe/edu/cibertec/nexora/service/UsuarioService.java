package pe.edu.cibertec.nexora.service;

import java.util.List;
import pe.edu.cibertec.nexora.entity.Usuario;

public interface UsuarioService {
	
	/* LISTA TODOS LOS USUARIOS REGISTRADOS EN LA BASE DE DATOS */
	List<Usuario> listar();

	/* GUARDA UN USUARIO NUEVO O ACTUALIZA UNO EXISTENTE.
	VALIDA QUE TIPO Y ESTADO EXISTAN, Y QUE DNI/CORREO NO ESTÉN DUPLICADOS */
	Usuario guardar(Usuario usuario);

	/* BUSCA UN USUARIO POR SU IDENTIFICADOR (ID_USUARIO). RETORNA NULL SI NO EXISTE */
	Usuario buscarPorId(Integer id);

	/* ELIMINA UN USUARIO POR SU IDENTIFICADOR.
	SI TIENE COMPROBANTES U OTRAS RELACIONES, LANZARÁ EXCEPCIÓN DE INTEGRIDAD */
	void eliminar(Integer id);

	/* BUSCA UN USUARIO POR DNI. SIRVE PARA VALIDACIONES Y CONSULTAS */
	Usuario buscarPorDni(String dni);

	/* BUSCA UN USUARIO POR CORREO. SIRVE PARA VALIDACIONES Y CONSULTAS */
	Usuario buscarPorCorreo(String correo);

	/* LISTA USUARIOS FILTRADOS POR ID DE TIPO (ADMINISTRADOR / CLIENTE) */
	List<Usuario> listarPorTipo(Integer idTipo);

	/* LISTA USUARIOS FILTRADOS POR ID DE ESTADO (ACTIVO / INACTIVO) */
	List<Usuario> listarPorEstado(Integer idEstado);
	
	List<Usuario> listarClientes();
}
