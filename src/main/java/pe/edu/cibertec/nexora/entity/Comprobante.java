package pe.edu.cibertec.nexora.entity;

import java.math.BigDecimal;
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
@Table(name="tb_comprobantes")

@Getter
@Setter
@NoArgsConstructor

public class Comprobante {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "num_comp")
	private Integer numComprobante;
	
	@Column(name="fecha_comp")
	private Date fechaComprobante;
	
	
}
