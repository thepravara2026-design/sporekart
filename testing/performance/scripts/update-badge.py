#!/usr/bin/env python3
"""Generate performance badge JSON for README."""

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
    parser = argparse.ArgumentParser(description='Update performance badge')
    parser.add_argument('--reports', required=True, help='Reports directory')
    args = parser.parse_args()

    reports_dir = Path(args.reports)
    all_ok = True
    total_checks = 0

    for report_file in sorted(reports_dir.rglob('*.json')):
        k6_data = load_k6_results(report_file)
        durations = []
        errors = 0
        for entry in k6_data:
            if entry.get('type') == 'Point' and entry.get('metric') == 'http_req_duration':
                durations.append(entry['data']['value'])
            elif entry.get('type') == 'Point' and entry.get('metric') == 'http_req_failed' and entry['data']['value'] == 1:
                errors += 1
        if durations:
            durations.sort()
            p95 = durations[int(len(durations) * 0.95)]
            if p95 > 500:
                all_ok = False
            total_checks += 1

    badge = {
        'schemaVersion': 1,
        'label': 'performance',
        'message': 'passing' if all_ok else 'failing',
        'color': 'brightgreen' if all_ok else 'red',
        'isError': not all_ok,
    }

    output_path = reports_dir / '.perf-badge.json'
    with open(output_path, 'w') as f:
        json.dump(badge, f, indent=2)

    print(f'Badge generated: {output_path}')
    print(f'Status: {"passing" if all_ok else "failing"}')


if __name__ == '__main__':
    main()
