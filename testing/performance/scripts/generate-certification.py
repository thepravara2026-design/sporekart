#!/usr/bin/env python3
"""Generate performance certification report from benchmark results."""

import json
import sys
import argparse
from pathlib import Path


TARGETS = {
    'auth': {'p95': 100, 'error_rate': 0.01, 'label': 'Authentication'},
    'api': {'p95': 200, 'error_rate': 0.001, 'label': 'API Response'},
    'database': {'p95': 50, 'error_rate': 0.001, 'label': 'Database Query'},
    'event': {'p95': 5, 'error_rate': 0.001, 'label': 'Event Publish'},
    'ai': {'p95': 3000, 'error_rate': 0.01, 'label': 'AI Request'},
}


def load_k6_results(path):
    results = []
    with open(path) as f:
        for line in f:
            line = line.strip()
            if line:
                try:
                    results.append(json.loads(line))
                except json.JSONDecodeError:
                    continue
    return results


def extract_metrics(results):
    durations = []
    errors = 0
    total = 0
    for entry in results:
        if entry.get('type') == 'Point' and 'metric' in entry:
            total += 1
            if entry['metric'] == 'http_req_duration':
                durations.append(entry['data']['value'])
            elif entry['metric'] == 'http_req_failed' and entry['data']['value'] == 1:
                errors += 1
    if not durations:
        return None
    durations.sort()
    return {
        'avg_ms': round(sum(durations) / len(durations), 2),
        'p95_ms': durations[int(len(durations) * 0.95)],
        'p99_ms': durations[int(len(durations) * 0.99)],
        'min_ms': durations[0],
        'max_ms': durations[-1],
        'error_rate': round(errors / max(total, 1), 4),
        'total_requests': total,
        'errors': errors,
    }


def main():
    parser = argparse.ArgumentParser(description='Generate performance certification')
    parser.add_argument('--reports', required=True, help='Reports directory')
    parser.add_argument('--output', required=True, help='Output markdown file')
    args = parser.parse_args()

    reports_dir = Path(args.reports)
    results = {}

    for report_file in reports_dir.rglob('*.json'):
        name = report_file.stem
        k6_data = load_k6_results(report_file)
        metrics = extract_metrics(k6_data)
        if metrics:
            results[name] = metrics

    now = __import__('datetime').datetime.utcnow()

    md = []
    md.append('# SporeKart Performance Certification Report\n')
    md.append(f'**Generated:** {now.strftime("%Y-%m-%d %H:%M:%S")} UTC\n')
    md.append(f'**Report Scope:** All performance benchmarks\n')
    md.append('---\n')
    md.append('## Executive Summary\n')

    passed = 0
    failed = 0
    for name, target in TARGETS.items():
        matched = False
        for rname, metrics in results.items():
            if name in rname:
                matched = True
                if metrics['p95_ms'] <= target['p95'] and metrics['error_rate'] <= target['error_rate']:
                    passed += 1
                else:
                    failed += 1
                break
        if not matched:
            failed += 1

    total = passed + failed
    md.append(f'- **Tests Passed:** {passed}/{total}')
    md.append(f'- **Tests Failed:** {failed}/{total}')
    md.append(f'- **Pass Rate:** {round(passed / max(total, 1) * 100, 1)}%')
    md.append(f'- **Verdict:** {"PASSED ✓" if failed == 0 else "FAILED ✗"}\n')

    md.append('## Detailed Results\n')
    md.append('| Component | Test | Avg (ms) | P95 (ms) | P99 (ms) | Error Rate | Target P95 (ms) | Status |')
    md.append('|-----------|------|----------|----------|----------|------------|-----------------|--------|')

    for name, target in TARGETS.items():
        matched = False
        for rname, metrics in results.items():
            if name in rname:
                matched = True
                p95_ok = metrics['p95_ms'] <= target['p95']
                err_ok = metrics['error_rate'] <= target['error_rate']
                status = '✓ PASS' if p95_ok and err_ok else '✗ FAIL'
                md.append(
                    f"| {target['label']} | {rname} | {metrics['avg_ms']} | "
                    f"{metrics['p95_ms']} | {metrics['p99_ms']} | "
                    f"{metrics['error_rate']:.2%} | {target['p95']} | {status} |"
                )
                break
        if not matched:
            md.append(f"| {target['label']} | N/A | N/A | N/A | N/A | N/A | {target['p95']} | ✗ NO DATA |")

    md.append('\n## Latency Distribution\n')
    for name, target in TARGETS.items():
        for rname, metrics in results.items():
            if name in rname:
                md.append(f'### {target["label"]} ({rname})')
                md.append(f'- Min: {metrics["min_ms"]}ms')
                md.append(f'- Avg: {metrics["avg_ms"]}ms')
                md.append(f'- P95: {metrics["p95_ms"]}ms')
                md.append(f'- P99: {metrics["p99_ms"]}ms')
                md.append(f'- Max: {metrics["max_ms"]}ms')
                md.append(f'- Requests: {metrics["total_requests"]}')
                md.append(f'- Errors: {metrics["errors"]}\n')

    md.append('## Optimization Recommendations\n')
    for name, target in TARGETS.items():
        for rname, metrics in results.items():
            if name in rname:
                if metrics['p95_ms'] > target['p95']:
                    md.append(f'- **{target["label"]}:** P95 {metrics["p95_ms"]}ms exceeds target {target["p95"]}ms — review and optimize')
                if metrics['error_rate'] > target['error_rate']:
                    md.append(f'- **{target["label"]}:** Error rate {metrics["error_rate"]:.2%} exceeds target {target["error_rate"]:.2%} — investigate failures')

    md.append('\n---\n')
    md.append('*Report generated by SporeKart Performance Certification Pipeline*')

    with open(args.output, 'w') as f:
        f.write('\n'.join(md))

    print(f'Certification report generated: {args.output}')


if __name__ == '__main__':
    main()
