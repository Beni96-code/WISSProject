#!/usr/bin/env bash
set -euo pipefail
COL="./api/postman/postman-candidates.json"
ENV="./api/postman/postman-env.local.json"
npx newman run "$COL" -e "$ENV" --reporters cli,junit --reporter-junit-export ./api/postman/reports/junit.xml
