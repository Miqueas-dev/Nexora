package pe.edu.cibertec.nexora.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.nexora.entity.Estado;
import pe.edu.cibertec.nexora.repository.EstadoRepository;
import pe.edu.cibertec.nexora.service.EstadoService;

@Service
public class EstadoServiceImpl implements EstadoService {
	
	@Autowired
	private EstadoRepository _estadoRepository;
	
	/* RETORNA TODOS LOS ESTADOS REGISTRADOS */
    @Override
    public List<Estado> listar() {
        return _estadoRepository.findAll();
    }

    /* REGISTRA UN ESTADO NUEVO O ACTUALIZA UNO EXISTENTE */
    @Override
    public Estado guardar(Estado estado) {
        return _estadoRepository.save(estado);
    }

    /* BUSCA UN ESTADO POR SU ID. RETORNA NULL SI NO SE ENCUENTRA */
    @Override
    public Estado buscarPorId(Integer id) {
        return _estadoRepository.findById(id).orElse(null);
    }

    /* ELIMINA UN ESTADO POR SU ID */
    @Override
    public void eliminar(Integer id) {
    	_estadoRepository.deleteById(id);
    }

    /* BUSCA ESTADOS CUYA DESCRIPCIÓN CONTENGA EL TEXTO ENVIADO (PARCIAL E IGNORE CASE) */
    @Override
    public List<Estado> buscarPorDescripcion(String descripcion) {
        return _estadoRepository.findByDescripcionContainingIgnoreCase(descripcion);
    }
}
