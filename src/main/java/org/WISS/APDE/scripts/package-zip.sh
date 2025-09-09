#!/usr/bin/env bash
set -e
NAME="APDE_${NACHNAME}_${VORNAME}_Trainingsfirma"
mkdir -p delivery
zip -r "delivery/${NAME}.zip" backend frontend api docs README.md infra scripts .env.example
echo "=> delivery/${NAME}.zip"
