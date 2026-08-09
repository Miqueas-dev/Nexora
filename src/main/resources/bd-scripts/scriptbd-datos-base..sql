-- ============================================
-- DATOS BASE PARA NEXORA
-- ============================================

USE DBNexora;

-- ============================================
-- MARCAS
-- ============================================

INSERT INTO tb_marcas (marca_desc) VALUES
('Lenovo'),
('Samsung'),
('Apple');

-- ============================================
-- ESTADOS
-- ============================================

INSERT INTO tb_estados (estado_desc) VALUES
('Activo'),
('Inactivo');

-- ============================================
-- PRODUCTO DE PRUEBA
-- ============================================

INSERT INTO tb_productos (
    prod_desc,
    prod_stock,
    prod_precio,
    id_marca,
    id_estado
)
VALUES (
    'Laptop Lenovo',
    10,
    2500.00,
    1,
    1
);