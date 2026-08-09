package pe.edu.cibertec.nexora.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_det_comprobantes")
@Getter
@Setter
@NoArgsConstructor
public class DetalleComprobante {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_detalle")
	private Integer idDetalle;

	@Column(name = "cant_prod", nullable = false)
	private int cantidadProducto;

	@Column(name = "precio_venta", nullable = false, precision = 10, scale = 2)
	private BigDecimal precioVenta;
	
	@ManyToOne
	@JoinColumn(name = "num_comp", nullable = false)
	private Comprobante comprobante;

	@ManyToOne
	@JoinColumn(name = "id_prod", nullable = false)
	private Producto producto;
	
}