package pe.edu.cibertec.nexora.service;

import java.util.List;

import pe.edu.cibertec.nexora.dto.RegistroClienteRequestDTO;
import pe.edu.cibertec.nexora.entity.Usuario;

public interface UsuarioService {

    List<Usuario> listar();

    Usuario buscarPorId(Integer id);

    Usuario guardar(Usuario usuario);

    void eliminar(Integer id);

    Usuario buscarPorDni(String dni);

    Usuario buscarPorCorreo(String correo);

    List<Usuario> listarPorTipo(Integer idTipo);

    List<Usuario> listarPorEstado(Integer idEstado);

    List<Usuario> listarClientes();

    Usuario registrarCliente(RegistroClienteRequestDTO registro);
}