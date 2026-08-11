package pe.edu.cibertec.nexora.service;

import java.util.List;
import pe.edu.cibertec.nexora.entity.Tipo;

public interface TipoService {
	
	/*LISTA TODOS LOS TIPOS REGISTRADOS EN LA BASE DE DATOS */
    List<Tipo> listar();

    /* GUARDA UN TIPO NUEVO O ACTUALIZA UNO EXISTENTE.
    SI EL IDTIPO ES NULL → REGISTRA; SI TIENE VALOR → ACTUALIZA */
    Tipo guardar(Tipo tipo);

    /* BUSCA UN TIPO POR SU IDENTIFICADOR (ID_TIPO) */
    Tipo buscarPorId(Integer id);

    /* ELIMINA UN TIPO POR SU IDENTIFICADOR */
    void eliminar(Integer id);

    /* BUSCA TIPOS CUYA DESCRIPCIÓN CONTENGA EL TEXTO INDICADO.
    BÚSQUEDA PARCIAL, SIN DISTINGUIR MAYÚSCULAS/MINÚSCULAS */
    List<Tipo> buscarPorDescripcion(String descripcion);
}
