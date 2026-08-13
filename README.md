# Nexora — Registro de comprobantes

Implementación del ticket Jira para registrar comprobantes de venta y sus detalles usando Spring Boot, JPA y MySQL.

## Funcionalidades

- Registro transaccional de comprobantes y detalles.
- Validación de usuario, producto, cantidad y stock disponible.
- Obtención del precio directamente desde la base de datos.
- Actualización automática del stock.
- Consulta de todos los comprobantes y consulta por ID.
- DTO para las operaciones de registro y respuesta.
- Lógica de negocio concentrada en `ComprobanteServiceImpl`.

## Endpoints

- `POST /api/comprobantes`
- `GET /api/comprobantes`
- `GET /api/comprobantes/{id}`

## Configuración

La aplicación requiere Java 21 y las siguientes variables de entorno:

```text
DB_URL=jdbc:mysql://localhost:3306/nexora
DB_USERNAME=usuario_mysql
DB_PASSWORD=clave_mysql
```

En Windows PowerShell:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/nexora"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="tu_clave"
.\mvnw.cmd spring-boot:run
```

No subas contraseñas reales al repositorio.

## Pruebas

Los ejemplos para probar casos correctos e incorrectos se encuentran en `POSTMAN-COMPROBANTES.md`.
