#!/bin/bash
# SporeKart Enterprise Release Engineering Script
# Usage: ./scripts/release.sh [major|minor|patch]
set -euo pipefail

RELEASE_TYPE="${1:-patch}"
CURRENT_BRANCH=$(git branch --show-current)

if [ "$CURRENT_BRANCH" != "sporetest" ]; then
  echo "❌ Releases must be cut from sporetest branch (currently on $CURRENT_BRANCH)"
  exit 1
}

echo "=== SporeKart Release Engineering ==="
echo "Release type: $RELEASE_TYPE"

# Get current version
LATEST_TAG=$(git tag --sort=-version:refname | head -1 || echo "v2.0.0")
echo "Latest tag: $LATEST_TAG"

CURRENT_VER="${LATEST_TAG#v}"
IFS='.' read -r MAJOR MINOR PATCH <<< "$CURRENT_VER"

case "$RELEASE_TYPE" in
  major) NEW_VER="$((MAJOR+1)).0.0" ;;
  minor) NEW_VER="$MAJOR.$((MINOR+1)).0" ;;
  patch) NEW_VER="$MAJOR.$MINOR.$((PATCH+1))" ;;
esac

echo "New version: v$NEW_VER"

# Generate changelog
PREV_TAG="${LATEST_TAG}"
echo ""
echo "=== Changelog ==="
git log --oneline --no-decorate "${PREV_TAG}..HEAD" 2>/dev/null || echo "(no previous tag found)"

echo ""
echo "=== Dry run complete ==="
echo "To execute: git tag -a v${NEW_VER} -m \"Release v${NEW_VER}\" && git push origin v${NEW_VER}"
echo ""
echo "Or use the GitHub Actions release workflow:"
echo "  .github/workflows/release.yml"
