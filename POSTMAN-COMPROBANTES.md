# Pruebas en Postman - KAN-15

## Venta correcta

`POST http://localhost:8080/api/comprobantes`

```json
{
  "idUsuario": 1,
  "detalles": [
    { "idProducto": 1, "cantidad": 2 }
  ]
}
```

Debe responder `201`, guardar comprobante y detalle, tomar el precio de la BD y reducir el stock.

## Casos incorrectos

- Usuario inexistente: `404`.
- Producto inexistente: `404`.
- Cantidad cero o negativa: `400`.
- Lista de detalles vacía: `400`.
- Cantidad mayor al stock: `409`.

La transacción debe deshacer todos los cambios si falla cualquier detalle.

## Consultas

- `GET http://localhost:8080/api/comprobantes`
- `GET http://localhost:8080/api/comprobantes/1`
