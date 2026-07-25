package pe.edu.cibertec.nexora.service;

import java.util.List;

import pe.edu.cibertec.nexora.entity.Marca;

public interface MarcaService {
	
	List<Marca> buscarPorTextoIngresado(String marca_textoIngresado);
}
