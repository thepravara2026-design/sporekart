# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: order-lifecycle.spec.ts >> Phase 1 — Order Creation >> No order creation API endpoint exposed
- Location: tests\order-lifecycle.spec.ts:42:7

# Error details

```
Error: apiRequestContext.post: connect ECONNREFUSED ::1:5173
Call log:
  - → POST http://localhost:5173/api/orders
    - user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/149.0.7827.55 Safari/537.36
    - accept: */*
    - accept-encoding: gzip,deflate,br
    - content-type: application/json
    - content-length: 2

```