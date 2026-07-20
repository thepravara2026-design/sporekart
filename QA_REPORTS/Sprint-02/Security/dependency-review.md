# Dependency Security Review — QA Sprint 2 Part 10

**Release:** v1.0.0-rc1 | **Date:** 2026-07-17

---

## 1. Executive Summary

**Status:** ⚠️ WARNING | **Dependency Health Score:** 70/100

Low dependency footprint (3 runtime deps). No known CVEs in declared dependencies. However, no `npm audit` has been performed and mobile apps have uninstalled declared dependencies.

---

## 2. All Dependencies By Package

### Frontend Web-App (`frontend/web-app/package.json`)

| Dependency | Version | Type | Size (est.) | Purpose | Known CVEs |
|-----------|---------|------|-------------|---------|------------|
| `react` | ^18.3.1 | runtime | 130KB | UI framework | ✅ None |
| `react-dom` | ^18.3.1 | runtime | 130KB | DOM rendering | ✅ None |
| `react-router-dom` | ^6.26.2 | runtime | 65KB | Client routing | ✅ None |
| `@types/react` | ^18.3.5 | dev | — | TypeScript types | ✅ None |
| `@types/react-dom` | ^18.3.0 | dev | — | TypeScript types | ✅ None |
| `@vitejs/plugin-react` | ^4.3.1 | dev | — | Build plugin | ✅ None |
| `typescript` | ^5.5.4 | dev | — | Type checker | ✅ None |
| `vite` | ^5.4.6 | dev | — | Build tool | ✅ None |

### Mobile Shared Core (`mobile/shared-core/package.json`)

| Dependency | Version | Status | Risk |
|-----------|---------|--------|------|
| `@react-native-async-storage/async-storage` | ^1.21.0 | ✅ Declared | 🟢 LOW |
| `@react-native-community/hooks` | ^2.8.1 | ✅ Declared | 🟢 LOW |
| `axios` | ^1.7.0 | ✅ Declared | 🟢 LOW |
| `zustand` | ^4.5.0 | ✅ Declared | 🟢 LOW |
| `@tanstack/react-query` | ^5.30.0 | ✅ Declared | 🟢 LOW |
| `mmkv-react-native` | ^1.1.0 | ✅ Declared | 🟢 LOW |
| `react-native-sqlite-storage` | ^6.0.1 | ✅ Declared | 🟢 LOW |
| `react-native-device-info` | ^10.12.0 | ✅ Declared | 🟢 LOW |
| `react-native-encryption` | ^1.3.0 | ✅ Declared | 🟡 MEDIUM (custom crypto) |

### Mobile Apps (`mobile/*-app/package.json`)

| Dependency | Version | Status | Risk |
|-----------|---------|--------|------|
| `react-native` | 0.73.0 | ✅ Declared | 🟢 LOW |
| `expo` | ~50.0.0 | ✅ Declared | 🟢 LOW |
| `@react-navigation/*` | ^6.x | ✅ Declared | 🟢 LOW |
| `react-native-gesture-handler` | ^2.14.0 | ✅ Declared | 🟢 LOW |
| `react-native-safe-area-context` | ^4.8.0 | ✅ Declared | 🟢 LOW |
| `react-native-paper` | ^5.12.0 | ✅ Declared | 🟢 LOW |
| `expo-camera` | ~14.0.0 | ✅ Declared | 🟡 MEDIUM (camera permissions) |
| `expo-notifications` | ~0.27.0 | ✅ Declared | 🟡 MEDIUM (notification permissions) |

---

## 3. Security Audit Status

| Audit Type | Status | Notes |
|-----------|--------|-------|
| `npm audit` (web-app) | ❌ NOT RUN | Run `npm audit` before production |
| `npm audit` (mobile) | ❌ NOT RUN | Run in each mobile app package |
| Snyk / Dependabot | ❌ NOT CONFIGURED | Configure in CI pipeline |
| OWASP Dependency-Check | ❌ NOT CONFIGURED | Add to CI pipeline |
| SBOM generation | ❌ NOT CONFIGURED | Generate CycloneDX SBOM |

---

## 4. High-Risk Dependencies

| Dependency | Risk | Reason |
|-----------|------|--------|
| `react-native-encryption` | 🟡 MEDIUM | Custom crypto implementation should be audited by security team |
| `expo-camera` | 🟡 MEDIUM | Camera permissions require privacy review |
| `expo-notifications` | 🟡 MEDIUM | Notification permissions require privacy review |
| `react-native-sqlite-storage` | 🟢 LOW | Local SQLite — review for injection in queries |

---

## 5. Recommendations

| Priority | Action | Effort |
|----------|--------|--------|
| 🟠 HIGH | Run `npm audit` on all packages before production | 1 hour |
| 🟠 HIGH | Configure Dependabot or Renovate for auto-updates | 1 day |
| 🟡 MEDIUM | Review `react-native-encryption` implementation | 1 day |
| 🟡 MEDIUM | Install mobile app dependencies and audit | 2 hours |
| 🟡 MEDIUM | Add Snyk scanning to CI pipeline | 1 day |
| 🟢 LOW | Generate SBOM for compliance | 1 day |
| 🟢 LOW | Pin dependency versions (remove ^ ranges) | 1 hour |

---

*End of Dependency Review*
