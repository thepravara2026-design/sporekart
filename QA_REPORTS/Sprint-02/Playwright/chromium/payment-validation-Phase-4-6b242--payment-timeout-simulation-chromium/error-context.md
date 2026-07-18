# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: payment-validation.spec.ts >> Phase 4 — Mock Payment Failure >> IMPLEMENTATION GAP: No payment timeout simulation
- Location: tests\payment-validation.spec.ts:231:7

# Error details

```
Error: apiRequestContext.get: connect ECONNREFUSED ::1:5173
Call log:
  - → GET http://localhost:5173/api/payments/timeout
    - user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/149.0.7827.55 Safari/537.36
    - accept: */*
    - accept-encoding: gzip,deflate,br

```