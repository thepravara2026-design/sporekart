# Tool Inventory Report

**Pre-QA Sprint 2** | **Date:** 2026-07-17

---

## Installed Tools

| Tool | Version | Location | Status |
|------|---------|----------|--------|
| **Node.js** | v24.16.0 | Global | ✅ |
| **npm** | 11.13.0 | Global | ✅ |
| **Git** | 2.54.0.windows.1 | Global | ✅ |
| **TypeScript** | 7.0.2 | `shared-testing/` | ✅ |
| **Playwright** | 1.61.1 | `shared-testing/` | ✅ |
| **@playwright/test** | 1.61.1 | `shared-testing/` | ✅ |
| **@axe-core/playwright** | 4.12.1 | `shared-testing/` | ✅ |
| **axe-core** | 4.12.1 | `shared-testing/` | ✅ |
| **ESLint** | 10.7.0 | `shared-testing/` | ✅ |
| **Prettier** | 3.9.5 | `shared-testing/` | ✅ |
| **Lighthouse** | 13.4.0 | `shared-testing/` | ✅ |
| **Vite** | 5.4.21 | `frontend/web-app/` | ✅ |

## Playwright Browsers

| Browser | Version | Status |
|---------|---------|--------|
| Chromium | 149.0.7827.55 (v1228) | ✅ Installed |
| Firefox | 151.0 (v1532) | ✅ Installed |
| WebKit | 26.5 (v2311) | ✅ Installed (newly added) |
| FFmpeg | v1011 | ✅ Installed |

## Missing Tools

| Tool | Recommendation |
|------|---------------|
| Vitest/Jest (web-app) | Not needed — all QA is Playwright-based |
| Jest runners (mobile) | Available in mobile app package.json but not the QA focus |

**Verdict:** All required QA tools are installed and available.
