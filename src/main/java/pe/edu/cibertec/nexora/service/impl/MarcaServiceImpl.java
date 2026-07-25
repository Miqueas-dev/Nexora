package pe.edu.cibertec.nexora.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.cibertec.nexora.entity.Marca;
import pe.edu.cibertec.nexora.repository.MarcaRepository;
import pe.edu.cibertec.nexora.service.MarcaService;

@Service
public class MarcaServiceImpl implements MarcaService{
	
	@Autowired
	private MarcaRepository repo;
	
	
	@Override
	public List<Marca> buscarPorTextoIngresado(String marca_textoIngresado) {
		return repo.findByMarcaDescContaining(marca_textoIngresado);
	}

}
