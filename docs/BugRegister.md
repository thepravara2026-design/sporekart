# SporeKart Enterprise — Bug Register

## Critical Bugs

| ID | Title | Component | Status | Fix |
|----|-------|-----------|--------|-----|
| CSP-01 | CSP blocks script execution in development | vite.config.ts | **FIXED** | Added conditional unsafe-inline for dev mode |

## High Bugs

| ID | Title | Steps to Reproduce | Expected | Actual | Severity |
|----|-------|-------------------|----------|--------|----------|
| HOME-01 | Homepage broken links | Navigate to /, check all anchor hrefs return 200 | All links return 200 | Some links return non-200 status | HIGH |

## Medium Bugs

| ID | Title | Steps to Reproduce | Expected | Actual | Severity |
|----|-------|-------------------|----------|--------|----------|
| AUTH-01 | OTP login non-functional | Click login, enter phone, submit OTP | OTP verified, redirect to dashboard | No response - tests time out | MEDIUM |
| PERF-01 | Orders page load >10s | Navigate to /orders | Page loads <3s | Page takes >10s | MEDIUM |
| TEST-01 | Test selectors mismatch app CSS | Run regression test for .sk-header | Test passes | .sk-header not found, should be .sk-public-header | MEDIUM |

## Low Bugs

| ID | Title | Steps to Reproduce | Expected | Actual | Severity |
|----|-------|-------------------|----------|--------|----------|
| NAV-01 | Brand link navigates incorrectly | Click brand link on homepage | Navigate to / | Navigation fails | LOW |
| SESSION-01 | Session persistence tests fail | Login, refresh page | Session persists | Session lost | LOW |
