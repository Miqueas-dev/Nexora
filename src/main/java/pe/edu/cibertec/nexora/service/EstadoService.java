package pe.edu.cibertec.nexora.service;

import java.util.List;
import pe.edu.cibertec.nexora.entity.Estado;

public interface EstadoService {
	
	/* LISTA TODOS LOS ESTADOS REGISTRADOS EN LA BASE DE DATOS */
    List<Estado> listar();

    /* GUARDA UN ESTADO NUEVO O ACTUALIZA UNO EXISTENTE.
    SI EL IDESTADO ES NULL → REGISTRA; SI TIENE VALOR → ACTUALIZA */
    Estado guardar(Estado estado);

    /* BUSCA UN ESTADO POR SU IDENTIFICADOR (ID_ESTADO) */
    Estado buscarPorId(Integer id);

    /* ELIMINA UN ESTADO POR SU IDENTIFICADOR */
    void eliminar(Integer id);

    /* BUSCA ESTADOS CUYA DESCRIPCIÓN CONTENGA EL TEXTO INDICADO
    (BÚSQUEDA PARCIAL, SIN DISTINGUIR MAYÚSCULAS/MINÚSCULAS) */
    List<Estado> buscarPorDescripcion(String descripcion);
}
