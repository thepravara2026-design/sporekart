#!/bin/bash
set -euo pipefail

# SporeKart Enterprise Health Check Framework
# Validates every service's health, readiness, and dependency status

BASE_URL="${1:-http://localhost:8080}"
FAILURES=0

check_endpoint() {
  local name="$1"
  local url="$2"
  local expected="${3:-200}"

  local code
  code=$(curl -sSf -o /dev/null -w "%{http_code}" --max-time 5 "$url" 2>/dev/null || echo "000")

  if [ "$code" = "$expected" ]; then
    echo "✅ $name — HTTP $code"
  else
    echo "❌ $name — expected $expected, got $code"
    FAILURES=$((FAILURES + 1))
  fi
}

echo "=== SporeKart Enterprise Health Check ==="
echo "Base URL: $BASE_URL"
echo ""

# Platform services
echo "--- Platform Services ---"
check_endpoint "Gateway Health" "${BASE_URL}/actuator/health"
check_endpoint "Gateway Liveness" "${BASE_URL}/actuator/health/liveness"
check_endpoint "Gateway Readiness" "${BASE_URL}/actuator/health/readiness"

# Database
echo ""
echo "--- Database Status ---"
check_endpoint "Flyway Status" "${BASE_URL}/actuator/flyway"

# Metrics
echo ""
echo "--- Metrics ---"
check_endpoint "Prometheus Metrics" "${BASE_URL}/actuator/prometheus"

# Info
echo ""
echo "--- Build Info ---"
check_endpoint "App Info" "${BASE_URL}/actuator/info"

# Security
echo ""
echo "--- Security Headers ---"
CSP=$(curl -sI "${BASE_URL}/actuator/health" | grep -i 'content-security-policy' || true)
HSTS=$(curl -sI "${BASE_URL}/actuator/health" | grep -i 'strict-transport-security' || true)

if [ -n "$CSP" ]; then
  echo "✅ Content-Security-Policy header present"
else
  echo "❌ Content-Security-Policy header missing"
  FAILURES=$((FAILURES + 1))
fi

if [ -n "$HSTS" ]; then
  echo "✅ Strict-Transport-Security header present"
else
  echo "❌ Strict-Transport-Security header missing"
  FAILURES=$((FAILURES + 1))
fi

echo ""
echo "=== Results ==="
if [ "$FAILURES" -eq 0 ]; then
  echo "✅ ALL CHECKS PASSED"
else
  echo "❌ $FAILURES CHECK(S) FAILED"
fi

exit "$FAILURES"
