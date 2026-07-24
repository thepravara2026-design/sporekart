#!/usr/bin/env python3
"""
SporeKart CI/CD PRR Validator
Verifies that all CI/CD infrastructure meets production standards.
"""

import json
import sys
from pathlib import Path
from datetime import datetime, timezone


REQUIRED_WORKFLOWS = [
    ".github/workflows/ci.yml",
    ".github/workflows/cd.yml",
    ".github/workflows/release.yml",
    ".github/workflows/rollback.yml",
    ".github/workflows/quality-gates.yml",
    ".github/workflows/performance-tests.yml",
]

REQUIRED_SCRIPTS = [
    "scripts/ci-build.sh",
    "scripts/deploy.sh",
    "scripts/rollback.sh",
    "scripts/release.sh",
]

REQUIRED_DOCKERFILES = [
    "docker/Dockerfile.backend",
    "docker/Dockerfile.gateway",
    "docker/Dockerfile.ai-service",
    "docker/Dockerfile.web-app",
    "docker/Dockerfile.ci",
]


def check_file(path):
    p = Path(path)
    exists = p.exists()
    size = p.stat().st_size if exists else 0
    return exists, size


def main():
    now = datetime.now(timezone.utc)
    print(f"\n{'='*70}")
    print(f"  SPOREKART CI/CD PRR VALIDATOR")
    print(f"  {now.strftime('%Y-%m-%d %H:%M:%S')} UTC")
    print(f"{'='*70}\n")

    all_pass = True

    print("  --- CI/CD Workflows ---")
    for wf in REQUIRED_WORKFLOWS:
        exists, size = check_file(wf)
        status = "✓" if exists else "✗"
        print(f"    {status} {wf:50} {f'({size} bytes)' if exists else 'MISSING!'}")
        if not exists:
            all_pass = False

    print("\n  --- Deployment Scripts ---")
    for script in REQUIRED_SCRIPTS:
        exists, size = check_file(script)
        status = "✓" if exists else "✗"
        print(f"    {status} {script:50} {f'({size} bytes)' if exists else 'MISSING!'}")
        if not exists:
            all_pass = False

    print("\n  --- Dockerfiles ---")
    for df in REQUIRED_DOCKERFILES:
        exists, size = check_file(df)
        status = "✓" if exists else "✗"
        print(f"    {status} {df:50} {f'({size} bytes)' if exists else 'MISSING!'}")
        if not exists:
            all_pass = False

    print(f"\n{'='*70}")
    if all_pass:
        print("  ★ CI/CD PRR: PASS — All required artifacts present ★")
    else:
        print("  ✗ CI/CD PRR: FAIL — Missing required artifacts")
    print(f"{'='*70}\n")

    report = {
        "validator": "ci-cd-prr",
        "timestamp": now.isoformat(),
        "passed": all_pass,
        "workflows": {w: check_file(w)[0] for w in REQUIRED_WORKFLOWS},
        "scripts": {s: check_file(s)[0] for s in REQUIRED_SCRIPTS},
        "dockerfiles": {d: check_file(d)[0] for d in REQUIRED_DOCKERFILES},
    }

    with open("testing/prr/ci-cd-validation.json", "w") as f:
        json.dump(report, f, indent=2)

    sys.exit(0 if all_pass else 1)


if __name__ == "__main__":
    main()
