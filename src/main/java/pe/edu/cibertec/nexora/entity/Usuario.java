package pe.edu.cibertec.nexora.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="tb_usuarios")

@Getter
@Setter
@NoArgsConstructor
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Integer idUsuario;
	
	@Column(name="usu_dni",length=8,nullable=false,unique=true)
	private String dniUsuario;
	
	@Column(name="usu_nombre",length=25,nullable=false)
	private String nombreUsuario;
	
	@Column(name="usu_apellidopaterno",length=25,nullable=false)
	private String apepatUsuario;
	
	@Column(name="usu_apellidomaterno",length=25,nullable=false)
	private String apematUsuario;
	
	@Column(name="usu_correo",length=45,nullable=false,unique=true)
	private String correoUsuario;
	
	@Column(name="usu_clave",length=8,nullable=false)
	private String claveUsuario;
	
	@Column(name="usu_fecnac",nullable=false)
	private Date fecnacUsuario;
	
	
	
}
