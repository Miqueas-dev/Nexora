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

-- ============================================
-- TIPOS
-- ============================================

INSERT INTO tb_tipos (tipo_desc) VALUES
('Administrador'),
('Cliente');

-- ============================================
-- USUARIOS DE PRUEBA
-- ============================================

INSERT INTO tb_usuarios (
    usu_dni,
    usu_nombre,
    usu_apellidopaterno,
    usu_apellidomaterno,
    usu_correo,
    usu_clave,
    usu_fecnac,
    id_tipo,
    id_estado
)
VALUES
(
    '74851236',
    'Alexander',
    'Perez',
    'Gomez',
    'alexander.perez@nexora.com',
    '12345678',
    '2004-05-15',
    1,
    1
),
(
    '63527481',
    'Carlos',
    'Ramirez',
    'Torres',
    'carlos.ramirez@nexora.com',
    '87654321',
    '2003-11-22',
    2,
    1
);

