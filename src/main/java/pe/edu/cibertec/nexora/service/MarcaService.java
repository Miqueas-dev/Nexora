package pe.edu.cibertec.nexora.service;

import java.util.List;

import pe.edu.cibertec.nexora.entity.Marca;

public interface MarcaService {
	
	//Metodo para listar Marcas
	List<Marca> listar();
	//Metodo para registrar nueva marca
	Marca guardar(Marca marca);
	//Metodo usado en el proceso de actualizar marca
	Marca buscarPorId(Integer id);
	//Metodo para eliminar marca
	void eliminar(Integer id);
	
	
	//Metodo para buscar Marca por texto ingresado
	List<Marca> buscarPorTextoIngresado(String marca_textoIngresado);
}
