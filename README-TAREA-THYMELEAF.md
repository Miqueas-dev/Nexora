# Tarea Thymeleaf integrada en Nexora

Se agregaron los componentes solicitados respetando la estructura original del proyecto.

## Archivos principales agregados

- `HomeController`
- `GlobalExceptionHandler`
- `ResourceNotFoundException`
- `templates/home/index.html`
- `templates/layout/header.html`
- `templates/layout/navbar.html`
- `templates/layout/footer.html`
- `templates/layout/scripts.html`
- `templates/error/404.html`
- `templates/error/500.html`
- `static/css/style.css`
- `static/js/main.js`
- `static/img/logo-nexora.svg`

## Integración realizada

- La dependencia `spring-boot-starter-thymeleaf` ya estaba presente en `pom.xml` y se conservó.
- Las vistas existentes de marcas fueron adaptadas para utilizar los fragmentos comunes.
- La tabla de marcas ahora mantiene las acciones dentro de una sola columna.
- Se agregó validación básica del nombre de marca.
- El controlador REST de productos no fue modificado.
- El manejador global se aplica solo a controladores MVC, para no cambiar las respuestas JSON de la API.

## Rutas

- Inicio: `http://localhost:8080/`
- Marcas: `http://localhost:8080/marcas`
- Productos REST: `http://localhost:8080/productos/listar`

## Base de datos

La configuración original se mantiene en `application.properties`:

- Base: `DBNexora`
- Usuario: `root`
- Clave: `mysql`

Modifique esos valores según su instalación local de MySQL.
