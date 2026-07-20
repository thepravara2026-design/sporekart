#!/usr/bin/env bash
# SporeKart — Production Smoke Test Script
# PRR-C08: Post-Deployment Smoke Test
# Status: ✅ CONFIGURED — 20-Jul-2026

set -euo pipefail

BASE_URL="${1:-https://sporekart.com}"
PASS=0
FAIL=0

echo "=== SporeKart Production Smoke Tests ==="
echo "Target: $BASE_URL"
echo ""

check() {
    local desc="$1"
    local url="$2"
    local expected="$3"
    local actual=$(curl -sSf -o /dev/null -w "%{http_code}" "$url" 2>/dev/null || echo "000")

    if [ "$actual" = "$expected" ]; then
        echo "  ✅ $desc — $actual"
        PASS=$((PASS + 1))
    else
        echo "  ❌ $desc — expected $expected, got $actual"
        FAIL=$((FAIL + 1))
    fi
}

check_header() {
    local desc="$1"
    local url="$2"
    local header="$3"
    local value="$4"
    local actual=$(curl -sI "$url" 2>/dev/null | grep -i "$header" | head -1 || echo "")

    if echo "$actual" | grep -qi "$value"; then
        echo "  ✅ $desc — header present"
        PASS=$((PASS + 1))
    else
        echo "  ❌ $desc — header '$header: $value' missing"
        FAIL=$((FAIL + 1))
    fi
}

# ---- Health Check ----
check "Health endpoint" "$BASE_URL/health" "200"

# ---- Security Headers ----
check_header "Content-Security-Policy" "$BASE_URL/" "content-security-policy" "default-src"
check_header "Strict-Transport-Security" "$BASE_URL/" "strict-transport-security" "max-age"
check_header "X-Frame-Options" "$BASE_URL/" "x-frame-options" "DENY"
check_header "X-Content-Type-Options" "$BASE_URL/" "x-content-type-options" "nosniff"

# ---- Auth Pages ----
check "Login page" "$BASE_URL/auth/login" "200"
check "Register page" "$BASE_URL/auth/register" "200"
check "Forgot password" "$BASE_URL/auth/forgot-password" "200"

# ---- Public Pages ----
check "Home page" "$BASE_URL/" "200"
check "About page" "$BASE_URL/about" "200"
check "Contact page" "$BASE_URL/contact" "200"
check "FAQ page" "$BASE_URL/faq" "200"
check "Knowledge base" "$BASE_URL/knowledge-base" "200"

# ---- SEO ----
check "robots.txt" "$BASE_URL/robots.txt" "200"
check "sitemap.xml" "$BASE_URL/sitemap.xml" "200"
check "manifest.json" "$BASE_URL/manifest.json" "200"

# ---- Static Assets ----
check "favicon" "$BASE_URL/favicon.svg" "200"

echo ""
echo "=== Results ==="
echo "Passed: $PASS"
echo "Failed: $FAIL"
if [ "$FAIL" -gt 0 ]; then
    echo "❌ SMOKE TEST FAILED"
    exit 1
else
    echo "✅ ALL SMOKE TESTS PASSED"
fi
