# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: admin-console.spec.ts >> Phase 12 — Security >> IMPLEMENTATION GAP: No admin API authentication
- Location: tests\admin-console.spec.ts:414:7

# Error details

```
Error: apiRequestContext.get: connect ECONNREFUSED ::1:5173
Call log:
  - → GET http://localhost:5173/admin/dashboard
    - user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/149.0.7827.55 Safari/537.36
    - accept: */*
    - accept-encoding: gzip,deflate,br

```