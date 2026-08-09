package pe.edu.cibertec.nexora.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "tb_productos")
@Getter
@Setter
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prod")
    private Integer idProducto;

    @Column(name = "prod_desc", length = 45, nullable = false)
    private String descripProducto;

    @Column(name = "prod_stock")
    private int stockProducto;

    @Column(
        name = "prod_precio",
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal precioProducto;

    // Muchos productos pueden pertenecer a una misma marca
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_marca", nullable = false)
    private Marca marca;

    // Muchos productos pueden tener el mismo estado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;
}