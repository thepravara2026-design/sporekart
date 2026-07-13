# Versioning Strategy

## Overview

The SporeKart Design System uses **semantic versioning (SemVer 2.0)** applied at three levels: tokens, components, and the full design system. This multi-level approach allows independent iteration while maintaining system-wide compatibility.

## Versioning Layers

| Layer | Package | Version | Scope |
|-------|---------|---------|-------|
| **Tokens** | `@sporekart/tokens` | `1.2.0` | CSS custom properties, primitive/semantic values |
| **Components** | `@sporekart/ui` | `2.1.3` | React components, hooks, providers |
| **Full System** | coordinated release | `2026.2.0` | Aligned quarterly releases |

## Semantic Versioning for Tokens

```
MAJOR.MINOR.PATCH
```

| Bump | When | Example |
|------|------|---------|
| **Major** | Breaking value change (existing tokens change meaning/visual output) | `1.0.0` → `2.0.0` |
| **Minor** | New token added; new scale step; new semantic alias | `1.0.0` → `1.1.0` |
| **Patch** | Token description update; token deprecation notice; typo fix | `1.0.0` → `1.0.1` |

Token version bumps do NOT automatically trigger a component release. Components declare a peer dependency range on tokens.

## Semantic Versioning for Components

```
MAJOR.MINOR.PATCH
```

| Bump | When | Example |
|------|------|---------|
| **Major** | Breaking API change (prop rename/removal, changed behavior) | `2.0.0` → `3.0.0` |
| **Minor** | New component; new variant; new prop (backward-compatible) | `2.0.0` → `2.1.0` |
| **Patch** | Bug fix; accessibility improvement; performance optimization | `2.0.0` → `2.0.1` |

### Component Version Manifest

Each component entry in `componentManifest.ts` tracks its own version. This enables consumers to pin to specific component versions.

## Full Design System Version

```
YYYY.QUARTER.RELEASE
```

| Example | Meaning |
|---------|---------|
| `2026.1.0` | Q1 2026 release |
| `2026.2.0` | Q2 2026 release (current) |
| `2026.3.0` | Q3 2026 release |

Aligned with the quarterly release cadence. Combines token and component updates into a coherent, tested system release.

## Changelog Format

All changes are documented in `CHANGELOG.md` under the root and per-package changelogs.

```markdown
## [2.1.0] - 2026-07-13

### Added
- **Button**: New `loading` prop with spinner state (#123)
- **Card**: New `elevation` variant for interactive cards (#124)
- **Tokens**: Added `--color-dataViz-categorical-7` and `-8` (#125)

### Changed
- **ThemeProvider**: Performance improvement — reduced re-renders on theme switch (#126)

### Fixed
- **Modal**: Focus trap now correctly skips hidden siblings (#127)
- **Select**: Dropdown closes on Escape in all browsers (#128)

### Deprecated
- **Button**: `iconOnly` prop deprecated; use `aria-label` instead (#129)

### Security
- Updated `decode-uri-component` dependency to 0.2.2 (#130)
```

### Change Types

| Type | Purpose |
|------|---------|
| `Added` | New features |
| `Changed` | Changes in existing functionality |
| `Fixed` | Bug fixes |
| `Deprecated` | Soon-to-be-removed features |
| `Removed` | Removed features |
| `Security` | Security fixes |

Each entry includes a reference to the related issue/PR number.

## Release Cadence

| Channel | Frequency | Version Bump | Approval |
|---------|-----------|--------------|----------|
| **Tokens** | As needed (with components) | Minor/Patch | Principal DS Architect |
| **Components** | Monthly (scheduled) + hotfix | Minor/Patch | Design System Council |
| **Full System** | Quarterly (sprint-aligned) | Major | CDO + CPO |
| **Hotfix** | As needed (P0/P1 bugs) | Patch | Principal DS Architect |

## Deprecation Policy

### Deprecation Timeline

| Phase | Duration | Actions |
|-------|----------|---------|
| **Announce** | Day 0 | Console warning in dev; deprecation notice in docs; migration guide published |
| **Support** | Months 0–6 | Bug fixes accepted; migration help provided; codemods available |
| **Deprecate** | Months 6–12 | No bug fixes; console.error in dev; migration mandatory |
| **Remove** | Month 12 | Code deleted; imports fail; docs archived |

### Deprecation Notice Format

Components emit a console warning in development mode:

```
[@sporekart/ui] Button variant "iconOnly" is deprecated. Use `aria-label` instead. 
See migration guide: /design-system/docs/migrations/button-iconOnly
```

### Emergency Deprecation

For security vulnerabilities or critical accessibility violations:

| Phase | Duration |
|-------|----------|
| Announce | Immediate |
| Migrate | 2 weeks |
| Remove | 2 weeks |

## Version Compatibility Matrix

| System Version | Token Version | Component Version | Status |
|----------------|---------------|-------------------|--------|
| 2026.2.0 | 1.2.x | 2.1.x | Current |
| 2026.1.0 | 1.1.x | 2.0.x | Supported (until 2026.3.0) |
| 2025.4.0 | 1.0.x | 1.3.x | Deprecated (support ends 2026.3.0) |
| ≤2025.3.0 | <1.0 | <1.3 | End of life |

## Peer Dependency Policy

```json
{
  "peerDependencies": {
    "@sporekart/tokens": "^1.2.0",
    "react": "^18.2.0",
    "react-dom": "^18.2.0"
  }
}
```

- Components accept `^MINOR` ranges for tokens (auto-accept minor and patch)
- Major token bumps require coordinated component release
- React peer dependency is pinned to the current major version
