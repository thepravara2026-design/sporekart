#!/usr/bin/env python3
"""Validate k6 benchmark results against threshold targets."""

import json
import sys
import argparse
from pathlib import Path


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
    p95 = durations[int(len(durations) * 0.95)]
    p99 = durations[int(len(durations) * 0.99)]
    avg = sum(durations) / len(durations)

    return {
        'avg_ms': round(avg, 2),
        'p95_ms': p95,
        'p99_ms': p99,
        'error_rate': round(errors / max(total, 1), 4),
        'total_requests': total,
        'errors': errors,
        'samples': len(durations),
    }


def validate(metrics, threshold_p95, threshold_error_rate):
    failures = []
    if metrics['p95_ms'] > threshold_p95:
        failures.append(f"p95 {metrics['p95_ms']}ms exceeds threshold {threshold_p95}ms")
    if metrics['error_rate'] > threshold_error_rate:
        failures.append(f"error rate {metrics['error_rate']} exceeds threshold {threshold_error_rate}")
    return failures


def main():
    parser = argparse.ArgumentParser(description='Validate k6 benchmark results')
    parser.add_argument('--input', required=True, help='k6 JSON results file')
    parser.add_argument('--threshold-p95', type=int, default=500, help='p95 latency threshold in ms')
    parser.add_argument('--threshold-error-rate', type=float, default=0.01, help='max error rate')
    args = parser.parse_args()

    results = load_k6_results(args.input)
    metrics = extract_metrics(results)

    if not metrics:
        print('FAIL: No duration metrics found in results')
        sys.exit(1)

    print(f'\n=== Benchmark Results: {Path(args.input).name} ===')
    print(f'  Total requests: {metrics["total_requests"]}')
    print(f'  Samples:        {metrics["samples"]}')
    print(f'  Avg latency:    {metrics["avg_ms"]}ms')
    print(f'  P95 latency:    {metrics["p95_ms"]}ms')
    print(f'  P99 latency:    {metrics["p99_ms"]}ms')
    print(f'  Error rate:     {metrics["error_rate"]:.2%}')
    print(f'  Errors:         {metrics["errors"]}')

    failures = validate(metrics, args.threshold_p95, args.threshold_error_rate)
    if failures:
        print('\nFAIL: Threshold violations:')
        for f in failures:
            print(f'  ✗ {f}')
        sys.exit(1)
    else:
        print('\nPASS: All thresholds met ✓')
        sys.exit(0)


if __name__ == '__main__':
    main()
