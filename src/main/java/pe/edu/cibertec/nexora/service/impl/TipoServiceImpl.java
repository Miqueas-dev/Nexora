package pe.edu.cibertec.nexora.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.nexora.entity.Tipo;
import pe.edu.cibertec.nexora.repository.TipoRepository;
import pe.edu.cibertec.nexora.service.TipoService;

@Service
public class TipoServiceImpl implements TipoService {
	
	@Autowired
	private TipoRepository _tipoRepository;
	
	/* RETORNA TODOS LOS TIPOS REGISTRADOS */
    @Override
    public List<Tipo> listar() {
        return _tipoRepository.findAll();
    }

    /* REGISTRA UN TIPO NUEVO O ACTUALIZA UNO EXISTENTE */
    @Override
    public Tipo guardar(Tipo tipo) {
        return _tipoRepository.save(tipo);
    }

    /* BUSCA UN TIPO POR SU ID. RETORNA NULL SI NO SE ENCUENTRA */
    @Override
    public Tipo buscarPorId(Integer id) {
        return _tipoRepository.findById(id).orElse(null);
    }

    /* ELIMINA UN TIPO POR SU ID */
    @Override
    public void eliminar(Integer id) {
    	_tipoRepository.deleteById(id);
    }

    /* BUSCA TIPOS CUYA DESCRIPCIÓN CONTENGA EL TEXTO ENVIADO (PARCIAL E IGNORE CASE) */
    @Override
    public List<Tipo> buscarPorDescripcion(String descripcion) {
        return _tipoRepository.findByDescripcionContainingIgnoreCase(descripcion);
    }
}
