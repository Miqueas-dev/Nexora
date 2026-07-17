package pe.edu.cibertec.nexora.entity;

import java.math.BigDecimal;

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
@Table(name="tb_det_compventas")

@Getter
@Setter
@NoArgsConstructor
public class DetalleComprobante {
	
	
	@Column(name = "cant_prod")
	private int cantidadProducto;
	
	@Column(name = "precio_venta",nullable = false, precision = 10, scale = 2)
	private BigDecimal precioVenta;
	
	
}
