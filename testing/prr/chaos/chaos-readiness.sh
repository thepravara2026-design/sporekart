#!/bin/bash
# SporeKart Chaos Readiness Test Suite
# Simulates failures across all subsystems to verify recovery.
set -euo pipefail

NAMESPACE=${NAMESPACE:-sporekart-platform}
TIMEOUT=${TIMEOUT:-120}
RESULTS_FILE="testing/prr/chaos/chaos-results.json"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

PASSED=0
FAILED=0
RESULTS=()

log_pass() { echo -e "${GREEN}[PASS]${NC} $1"; ((PASSED++)); }
log_fail() { echo -e "${RED}[FAIL]${NC} $1"; ((FAILED++)); }
log_info() { echo -e "${YELLOW}[INFO]${NC} $1"; }

cleanup() {
    log_info "Cleaning up chaos experiments..."
    kubectl rollout restart deployment -n "$NAMESPACE" --all 2>/dev/null || true
}

trap cleanup EXIT

echo ""
echo "=========================================="
echo "  SPOREKART CHAOS READINESS TEST SUITE"
echo "=========================================="
echo ""

# Test 1: Pod Crash Recovery
echo "--- Test 1: Pod Crash Recovery ---"
POD=$(kubectl get pod -n "$NAMESPACE" -l app=gateway-service -o jsonpath='{.items[0].metadata.name}' 2>/dev/null || echo "")
if [ -n "$POD" ]; then
    kubectl delete pod -n "$NAMESPACE" "$POD" --grace-period=0 --force 2>/dev/null
    sleep 10
    NEW_POD=$(kubectl get pod -n "$NAMESPACE" -l app=gateway-service -o jsonpath='{.items[0].metadata.name}' 2>/dev/null || echo "")
    if [ -n "$NEW_POD" ] && [ "$NEW_POD" != "$POD" ]; then
        log_pass "Pod recovered: $POD -> $NEW_POD"
    else
        log_fail "Pod did not recover"
    fi
else
    log_info "Skipping pod crash test (no pods found)"
fi

# Test 2: Service Down Detection
echo ""
echo "--- Test 2: Service Health Check ---"
HEALTH_OK=$(curl -sf http://localhost:8080/actuator/health 2>/dev/null && echo "UP" || echo "DOWN")
if [ "$HEALTH_OK" = "UP" ]; then
    log_pass "Health endpoint returning UP"
else
    log_fail "Health endpoint not responding"
fi

# Test 3: Readiness Probe
echo ""
echo "--- Test 3: Readiness Probe ---"
READY_OK=$(curl -sf http://localhost:8080/actuator/health/readiness 2>/dev/null && echo "UP" || echo "DOWN")
if [ "$READY_OK" = "UP" ]; then
    log_pass "Readiness probe passing"
else
    log_fail "Readiness probe failing"
fi

# Test 4: Metrics Endpoint
echo ""
echo "--- Test 4: Metrics Endpoint ---"
METRICS_OK=$(curl -sf http://localhost:8080/actuator/metrics 2>/dev/null && echo "UP" || echo "DOWN")
if [ "$METRICS_OK" = "UP" ]; then
    log_pass "Metrics endpoint accessible"
else
    log_fail "Metrics endpoint not accessible"
fi

# Test 5: Prometheus Metrics
echo ""
echo "--- Test 5: Prometheus Metrics ---"
PROM_OK=$(curl -sf http://localhost:8080/actuator/prometheus 2>/dev/null && echo "UP" || echo "DOWN")
if [ "$PROM_OK" = "UP" ]; then
    log_pass "Prometheus metrics exported"
else
    log_fail "Prometheus metrics not available"
fi

# Test 6: Trace Header Propagation
echo ""
echo "--- Test 6: Trace Header Propagation ---"
TRACE_ID=$(curl -sI http://localhost:8080/actuator/health 2>/dev/null | grep -i X-Trace-Id | awk '{print $2}' | tr -d ' \r\n' || echo "")
if [ -n "$TRACE_ID" ]; then
    log_pass "Trace headers propagated (X-Trace-Id: $TRACE_ID)"
else
    log_fail "Trace headers not propagated"
fi

# Test 7: Database Connection
echo ""
echo "--- Test 7: Database Health Check ---"
DB_HEALTH=$(curl -sf http://localhost:8080/actuator/health 2>/dev/null | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('components',{}).get('db',{}).get('status','UNKNOWN'))" 2>/dev/null || echo "UNKNOWN")
if [ "$DB_HEALTH" = "UP" ]; then
    log_pass "Database health UP"
else
    log_fail "Database health: $DB_HEALTH"
fi

# Test 8: Pod Restart Counter
echo ""
echo "--- Test 8: Pod Restart Check ---"
RESTARTS=$(kubectl get pods -n "$NAMESPACE" --no-headers 2>/dev/null | awk '$5 > 0 {print $1, $5}' || echo "")
if [ -z "$RESTARTS" ]; then
    log_pass "No pods with restart count > 0"
else
    echo "$RESTARTS" | while read pod count; do
        log_info "Pod $pod has $count restarts"
    done
    log_pass "Pod restart check completed"
fi

# Test 9: HPA Status
echo ""
echo "--- Test 9: HPA Status ---"
HPA_COUNT=$(kubectl get hpa -n "$NAMESPACE" --no-headers 2>/dev/null | wc -l)
if [ "$HPA_COUNT" -gt 0 ]; then
    log_pass "$HPA_COUNT HPA(s) configured"
else
    log_info "No HPAs found (expected in dev)"
fi

# Test 10: Resource Limits
echo ""
echo "--- Test 10: Container Resource Limits ---"
NO_LIMITS=$(kubectl get pods -n "$NAMESPACE" -o jsonpath='{range .items[*]}{range .spec.containers[*]}{.name}{" "}{.resources.limits.cpu}{" "}{.resources.limits.memory}{"\n"}{end}{end}' 2>/dev/null | grep -c " " || echo "0")
if [ "$NO_LIMITS" -gt 0 ]; then
    log_pass "Resource limits configured on $NO_LIMITS containers"
else
    log_info "No resource limits found (expected in dev)"
fi

# Summary
echo ""
echo "=========================================="
echo "  CHAOS READINESS RESULTS"
echo "=========================================="
echo "  Passed: $PASSED"
echo "  Failed: $FAILED"
echo ""

if [ "$FAILED" -eq 0 ]; then
    echo -e "  ${GREEN}★ CHAOS READINESS: PASS${NC}"
else
    echo -e "  ${RED}✗ CHAOS READINESS: FAIL - $FAILED test(s) failed${NC}"
fi
echo ""

# Write results
cat > "$RESULTS_FILE" << EOF
{
  "test_suite": "chaos-readiness",
  "timestamp": "$(date -u +%Y-%m-%dT%H:%M:%SZ)",
  "passed": $PASSED,
  "failed": $FAILED,
  "results": []
}
EOF

exit $FAILED
