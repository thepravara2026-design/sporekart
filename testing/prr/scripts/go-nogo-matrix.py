#!/usr/bin/env python3
"""
SporeKart GO/NO-GO Assessment Matrix
Evaluates all production gates and produces a scored decision.
"""

import json
import sys
from datetime import datetime, timezone


GATES = {
    "Architecture": {
        "weight": 10,
        "checks": [
            ("ARC-001", "Microservices architecture", True),
            ("ARC-005", "Shared-platform cross-cutting concerns", True),
            ("ARC-006", "Stateless horizontal scaling", True),
            ("ARC-007", "No circular dependencies", True),
        ]
    },
    "Security": {
        "weight": 15,
        "checks": [
            ("SEC-001", "JWT authentication", True),
            ("SEC-002", "RBAC authorization", True),
            ("SEC-005", "TLS 1.2+ enforced", True),
            ("SEC-007", "WAF + rate limiting", True),
            ("SEC-008", "Zero trust network", True),
            ("SEC-009", "Secrets management (Vault)", True),
            ("SEC-010", "No hardcoded credentials", False),
            ("SEC-012", "Prompt injection protection", True),
            ("SEC-014", "Dependency vulnerability scanning", True),
        ]
    },
    "Reliability": {
        "weight": 15,
        "checks": [
            ("REL-001", "Circuit breakers", True),
            ("REL-002", "Retry with backoff", True),
            ("REL-003", "Bulkhead isolation", True),
            ("REL-004", "Graceful degradation", True),
            ("REL-009", "Replicas >= 3", True),
            ("REL-010", "PDB configured", True),
            ("REL-011", "HPA configured", True),
        ]
    },
    "Performance": {
        "weight": 10,
        "checks": [
            ("PERF-001", "Auth latency P95 < 100ms", True),
            ("PERF-002", "API latency P95 < 200ms", True),
            ("PERF-003", "DB latency P95 < 50ms", True),
            ("PERF-004", "AI latency P95 < 3s", True),
            ("PERF-008", "Multi-tier caching", True),
            ("PERF-010", "Connection pool sized", True),
            ("PERF-013", "CPU within limits", True),
            ("PERF-014", "Memory within limits", True),
        ]
    },
    "Infrastructure": {
        "weight": 10,
        "checks": [
            ("INFRA-001", "K8s namespace isolation", True),
            ("INFRA-002", "Network policies", True),
            ("INFRA-003", "Resource limits", True),
            ("INFRA-004", "Health probes", True),
            ("INFRA-008", "Ingress + TLS", True),
            ("INFRA-014", "Infrastructure as Code", True),
            ("INFRA-015", "Multi-stage Docker builds", True),
            ("INFRA-016", "Non-root containers", True),
            ("INFRA-018", "Image vulnerability scanning", True),
        ]
    },
    "Operations": {
        "weight": 10,
        "checks": [
            ("OPS-001", "Service down runbook", True),
            ("OPS-002", "Database failure runbook", True),
            ("OPS-003", "AI provider failure runbook", True),
            ("OPS-004", "Incident severity matrix", True),
            ("OPS-005", "Escalation policy", True),
            ("OPS-010", "SLO/SLI framework (18)", True),
            ("OPS-013", "Resource specifications", True),
        ]
    },
    "Deployment": {
        "weight": 10,
        "checks": [
            ("DEP-001", "CI pipeline", True),
            ("DEP-002", "CD pipeline", True),
            ("DEP-003", "Release workflow", True),
            ("DEP-004", "Rollback workflow", True),
            ("DEP-008", "Health checks gate deployment", True),
            ("DEP-009", "Semantic versioning", True),
            ("DEP-011", "Quality gates", True),
            ("DEP-012", "Performance tests in CI", True),
            ("DEP-014", "Env-specific config", True),
        ]
    },
    "Database": {
        "weight": 8,
        "checks": [
            ("DB-001", "Flyway migrations", True),
            ("DB-004", "Composite indexes", True),
            ("DB-008", "Slow query analysis", True),
            ("DB-009", "N+1 detection", True),
            ("DB-011", "Connection pool leak detection", True),
            ("DB-012", "Vacuum/analyze config", True),
            ("DB-013", "Backup strategy", True),
        ]
    },
    "Observability": {
        "weight": 10,
        "checks": [
            ("OBS-001", "Prometheus metrics", True),
            ("OBS-006", "Structured JSON logging", True),
            ("OBS-010", "Distributed tracing (OTel)", True),
            ("OBS-013", "Dashboards (10)", True),
            ("OBS-019", "Alertmanager (Slack + PagerDuty)", True),
            ("OBS-020", "Alert rules (15+ SRE)", True),
            ("OBS-023", "Health endpoint", True),
        ]
    },
    "Recovery": {
        "weight": 5,
        "checks": [
            ("REC-001", "Automated backup script", True),
            ("REC-004", "DR runbook (5 scenarios)", True),
            ("REC-005", "RTO/RPO defined", True),
            ("REC-014", "Chaos readiness", True),
        ]
    },
    "Dependencies": {
        "weight": 5,
        "checks": [
            ("DEP-003", "AI provider failover", True),
            ("DEP-006", "Multi-region cloud", True),
            ("DEP-007", "Dependency health monitoring", True),
        ]
    },
}


def evaluate():
    now = datetime.now(timezone.utc)

    print(f"\n{'='*70}")
    print(f"  SPOREKART PRODUCTION READINESS — GO/NO-GO MATRIX")
    print(f"  {now.strftime('%Y-%m-%d %H:%M:%S')} UTC")
    print(f"{'='*70}\n")

    total_weight = 0
    passed_weight = 0
    total_checks = 0
    passed_checks = 0
    failures = []

    for gate_name, gate in GATES.items():
        gate_passed = 0
        gate_total = len(gate['checks'])
        for check_id, description, passes in gate['checks']:
            total_checks += 1
            if passes:
                gate_passed += 1
                passed_checks += 1
            else:
                failures.append((gate_name, check_id, description))

        gate_score = (gate_passed / gate_total) * 100
        gate_weighted = gate['weight'] * (gate_passed / gate_total)
        total_weight += gate['weight']
        passed_weight += gate_weighted

        status = "✓" if gate_passed == gate_total else "✗"
        print(f"  {status} {gate_name:20} {gate_passed:2}/{gate_total:2} passed  "
              f"({gate_score:.0f}%)  [weight: {gate['weight']}%]")

    overall_score = (passed_weight / total_weight) * 100 if total_weight > 0 else 0
    min_pass_rate = 90.0
    is_go = overall_score >= min_pass_rate and len([f for f in failures if f[0] in ['Security']]) == 0

    print(f"\n{'='*70}")
    print(f"  RESULTS")
    print(f"{'='*70}")
    print(f"  Total checks:    {total_checks}")
    print(f"  Passed checks:   {passed_checks}")
    print(f"  Failed checks:   {total_checks - passed_checks}")
    print(f"  Pass rate:       {(passed_checks/total_checks)*100:.1f}%")
    print(f"  Weighted score:  {overall_score:.1f}%")
    print(f"  Min threshold:   {min_pass_rate:.0f}%")

    if failures:
        print(f"\n  {'─'*50}")
        print(f"  FAILED CHECKS:")
        print(f"  {'─'*50}")
        for gate, check_id, desc in failures:
            print(f"    [{gate:15}] {check_id:10} {desc}")

    print(f"\n  {'='*50}")
    if is_go:
        print(f"  ★ VERDICT: GO — Platform is production-ready ★")
        print(f"  {'='*50}")
        print(f"\n  All production gates passed successfully.")
        print(f"  Platform is certified for production deployment.")
    else:
        print(f"  ✗ VERDICT: NO-GO — Production readiness not achieved")
        print(f"  {'='*50}")
        print(f"\n  Remediation required before production deployment:")
        print(f"  1. Resolve all failed security checks")
        print(f"  2. Achieve minimum {min_pass_rate:.0f}% weighted score")
        print(f"  3. Re-run PRR validation after remediation")

    print()
    return is_go


if __name__ == "__main__":
    passed = evaluate()
    sys.exit(0 if passed else 1)
