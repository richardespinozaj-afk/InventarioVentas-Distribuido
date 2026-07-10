#!/usr/bin/env bash
# Prueba funcional del CRUD y de la comunicacion entre servicios.
set -e
BASE="${1:-http://localhost}"
echo "== Health del servicio principal =="
curl -s "$BASE/api/health"; echo
echo "== Listar productos =="
curl -s "$BASE/api/productos"; echo
echo "== Crear producto =="
curl -s -X POST "$BASE/api/productos" -H "Content-Type: application/json" \
  -d '{"codigo":"P099","nombre":"Producto Demo","precio":10.5,"stock":50}'; echo
echo "== Registrar venta (descuenta stock) =="
curl -s -X POST "$BASE/api/ventas" -H "Content-Type: application/json" \
  -d '{"cliente":"Juan Perez","items":[{"productoId":1,"cantidad":3}]}'; echo
echo "== Reporte de stock bajo (servicio secundario via REST) =="
curl -s "$BASE/api/reportes/stock-bajo?umbral=10"; echo
echo "== Resumen de ventas =="
curl -s "$BASE/api/reportes/resumen-ventas"; echo
