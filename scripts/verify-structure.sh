#!/usr/bin/env bash
set -euo pipefail
find . -maxdepth 3 -type d | sort | sed -n "1,240p"
