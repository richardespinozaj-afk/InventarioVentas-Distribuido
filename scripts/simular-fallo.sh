#!/usr/bin/env bash
# Demuestra TOLERANCIA A FALLOS: se apaga un nodo y el sistema sigue respondiendo.
BASE="${1:-http://localhost}"
echo ">> Estado inicial: ambos nodos activos"
for i in 1 2 3 4; do curl -s "$BASE/api/health" | grep -o '"node":"[^"]*"'; done

echo; echo ">> Deteniendo el nodo inventory1 (simulacion de caida)..."
docker stop iv-inventory1

echo ">> El balanceador debe seguir respondiendo desde inventory2:"
for i in 1 2 3 4 5; do curl -s "$BASE/api/productos" >/dev/null && echo "peticion $i: OK" || echo "peticion $i: FALLO"; done
curl -s "$BASE/api/health" | grep -o '"node":"[^"]*"'

echo; echo ">> Recuperando el nodo inventory1..."
docker start iv-inventory1
sleep 15
echo ">> Nodos activos tras recuperacion:"
for i in 1 2 3 4; do curl -s "$BASE/api/health" | grep -o '"node":"[^"]*"'; done
