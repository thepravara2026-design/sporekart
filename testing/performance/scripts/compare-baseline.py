#!/usr/bin/env python3
"""Compare current benchmark results against baseline for regression detection."""

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


def extract_avg_duration(results):
    durations = []
    for entry in results:
        if entry.get('type') == 'Point' and entry.get('metric') == 'http_req_duration':
            durations.append(entry['data']['value'])
    if not durations:
        return None
    return sum(durations) / len(durations)


def main():
    parser = argparse.ArgumentParser(description='Compare benchmark against baseline')
    parser.add_argument('--current', required=True, help='Current k6 results JSON')
    parser.add_argument('--baseline', required=True, help='Baseline JSON file')
    parser.add_argument('--max-degradation', type=float, default=15.0, help='Max allowed degradation %')
    args = parser.parse_args()

    if not Path(args.baseline).exists():
        print(f'WARN: No baseline found at {args.baseline}, skipping comparison')
        sys.exit(0)

    current_results = load_k6_results(args.current)
    current_avg = extract_avg_duration(current_results)

    with open(args.baseline) as f:
        baseline_data = json.load(f)

    if current_avg is None:
        print('FAIL: No duration metrics in current results')
        sys.exit(1)

    baseline_avg = baseline_data.get('avg_duration_ms', 0)
    if baseline_avg == 0:
        print('FAIL: Invalid baseline avg_duration_ms')
        sys.exit(1)

    degradation = ((current_avg - baseline_avg) / baseline_avg) * 100
    status = 'PASS' if degradation <= args.max_degradation else 'FAIL'

    print(f'\n=== Regression Comparison: {Path(args.current).name} ===')
    print(f'  Baseline avg:    {baseline_avg:.2f}ms')
    print(f'  Current avg:     {current_avg:.2f}ms')
    print(f'  Degradation:     {degradation:+.2f}%')
    print(f'  Max allowed:     {args.max_degradation:.2f}%')
    print(f'  Status:          {status}')

    if degradation > args.max_degradation:
        print(f'\nFAIL: Performance regressed by {degradation:.2f}% (max {args.max_degradation:.2f}%)')
        sys.exit(1)

    print(f'\nPASS: Performance within baseline ✓')
    sys.exit(0)


if __name__ == '__main__':
    main()
