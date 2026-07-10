# Sistema Distribuido de Inventario y Ventas

Proyecto final del curso de **Sistemas Distribuidos** (UTP). Solución distribuida,
tolerante a fallos, con **API REST**, **contenedores Docker** y **balanceador Nginx**.

## Arquitectura

| Componente | Rol | Tecnología |
|---|---|---|
| `inventory-service` (x2) | Servicio **principal**: CRUD de productos y ventas | Java EE / JAX-RS + Tomcat 9 |
| `reporting-service` | Servicio **secundario**: reportes vía REST | Java EE / JAX-RS + Tomcat 9 |
| `mysql` | Base de datos | MySQL 8 |
| `loadbalancer` | Punto de entrada + balanceo + tolerancia a fallos | Nginx |

```
Usuario ──HTTP:80──► Nginx ──► inventory1 ─┐
                          └──► inventory2 ─┴─► MySQL
                          └──► reporting ──► (consume inventory vía REST)
```

## Requisitos
- Docker y Docker Compose (host Linux / AWS EC2 Ubuntu recomendado).

## Despliegue
```bash
docker compose up --build -d      # levanta los 5 contenedores
docker compose ps                 # verifica estado
```
- Interfaz web: `http://localhost/` (o la IP pública en AWS)
- API principal: `http://localhost/api/productos`
- Reportes: `http://localhost/api/reportes/stock-bajo?umbral=10`

## Pruebas y evidencias
```bash
bash scripts/smoke-test.sh        # CRUD + venta + reportes
bash scripts/carga.sh             # balanceo entre nodos
bash scripts/simular-fallo.sh     # tolerancia a fallos (apaga y recupera un nodo)
```

## Endpoints
| Método | Ruta | Descripción |
|---|---|---|
| GET/POST | `/api/productos` | Listar / crear producto |
| GET/PUT/DELETE | `/api/productos/{id}` | Consultar / actualizar / eliminar |
| GET/POST | `/api/ventas` | Listar / registrar venta (descuenta stock) |
| GET | `/api/health` | Estado del nodo + BD |
| GET | `/api/reportes/stock-bajo` | Productos bajo umbral (servicio secundario) |
| GET | `/api/reportes/resumen-ventas` | Total de ventas |

## Configuración (variables de entorno)
`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`, `NODE_ID` (inventory);
`INVENTORY_URL`, `NODE_ID` (reporting).

## Estructura
```
inventory-service/   servicio principal (WAR)
reporting-service/   servicio secundario (WAR)
nginx/nginx.conf     balanceador + failover
db/init.sql          esquema y datos de ejemplo
docker-compose.yml   orquestación
scripts/             pruebas y simulación de fallos
docs/                documento técnico + diagramas
```

## Documentación
Ver `docs/Documento_Tecnico_Inventario_Ventas.docx`.
