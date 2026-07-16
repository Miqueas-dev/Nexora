package pe.edu.cibertec.nexora.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_estados")
public class Estado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_estado")
	private Integer idEstado;
	
	@Column(name = "estado_desc", length = 15, nullable = false)
    private String descripcion;

}
