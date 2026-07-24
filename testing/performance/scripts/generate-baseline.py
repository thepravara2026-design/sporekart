#!/usr/bin/env python3
"""Generate performance baseline from k6 results."""

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


def main():
    parser = argparse.ArgumentParser(description='Generate performance baseline')
    parser.add_argument('--input', required=True, help='k6 JSON results file')
    parser.add_argument('--output', required=True, help='Output baseline JSON file')
    args = parser.parse_args()

    results = load_k6_results(args.input)
    durations = []

    for entry in results:
        if entry.get('type') == 'Point' and entry.get('metric') == 'http_req_duration':
            durations.append(entry['data']['value'])

    if not durations:
        print('FAIL: No duration metrics found')
        sys.exit(1)

    durations.sort()
    baseline = {
        'generated_at': __import__('datetime').datetime.utcnow().isoformat() + 'Z',
        'source_file': str(Path(args.input).resolve()),
        'avg_duration_ms': round(sum(durations) / len(durations), 2),
        'p95_duration_ms': durations[int(len(durations) * 0.95)],
        'p99_duration_ms': durations[int(len(durations) * 0.99)],
        'min_duration_ms': durations[0],
        'max_duration_ms': durations[-1],
        'sample_count': len(durations),
    }

    with open(args.output, 'w') as f:
        json.dump(baseline, f, indent=2)

    print(f'Baseline generated: {args.output}')
    print(f'  Avg: {baseline["avg_duration_ms"]}ms')
    print(f'  P95: {baseline["p95_duration_ms"]}ms')
    print(f'  P99: {baseline["p99_duration_ms"]}ms')
    print(f'  Samples: {baseline["sample_count"]}')


if __name__ == '__main__':
    main()
