# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: admin-console.spec.ts >> Phase 13 — Data Integrity >> Mock API data accessible from admin
- Location: tests\admin-console.spec.ts:448:7

# Error details

```
Error: apiRequestContext.get: connect ECONNREFUSED ::1:5174
Call log:
  - → GET http://localhost:5174/admin/products
    - user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/149.0.7827.55 Safari/537.36
    - accept: */*
    - accept-encoding: gzip,deflate,br

```