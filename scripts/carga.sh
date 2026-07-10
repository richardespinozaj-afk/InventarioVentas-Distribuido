#!/usr/bin/env bash
# Demuestra BALANCEO/ESCALABILIDAD: reparte peticiones entre los nodos.
BASE="${1:-http://localhost}"
echo "Distribucion de 20 peticiones entre nodos:"
for i in $(seq 1 20); do curl -s "$BASE/api/health" | grep -o '"node":"[^"]*"'; done | sort | uniq -c
