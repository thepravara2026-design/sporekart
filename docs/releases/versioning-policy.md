# Versioning Policy — v1.0.0

> **Scheme**: Semantic Versioning (SemVer) — MAJOR.MINOR.PATCH  
> **Independence**: Design system versioning is independent of product versioning  
> **Current Version**: 1.0.0  

---

## Version Scheme

```
MAJOR.MINOR.PATCH [-pre-release-tag.X]
  │      │      │         │
  │      │      │         └── Pre-release: alpha, beta, release candidate
  │      │      └──────────── Patch: bug fixes, a11y, docs, performance
  │      └─────────────────── Minor: new components, new tokens, features
  └────────────────────────── Major: breaking changes, token removals, API redesigns
```

---

## Version Bump Rules

### Major (X.0.0)

Breaking changes that require consumer teams to modify their code:

- Removal or renaming of design tokens
- Component API redesign (props renamed, removed, or behavior changed)
- Dropped browser support
- Theme structural changes
- Removal of deprecated components

### Minor (0.X.0)

Non-breaking additions and deprecations:

- New components
- New design tokens
- New component features (new props, variants)
- Deprecation announcements
- New theme variants

### Patch (0.0.X)

Backward-compatible fixes and improvements:

- Bug fixes
- Accessibility improvements
- Documentation updates
- Performance optimizations
- Test additions
- Dependency updates (non-breaking)

---

## Pre-release Tags

For staging and validation before production releases:

| Tag | Purpose |
|-----|---------|
| `-alpha.X` | Early preview; APIs may change without notice |
| `-beta.X` | Feature-complete; testing and feedback phase |
| `-rc.X` | Release candidate; final validation before stable release |

Pre-release versions have **lower precedence** than the normal version. E.g., `1.0.0-alpha.1 < 1.0.0`.

---

## Zero Version (0.X.X)

During initial development (major version 0), **breaking changes are allowed** but must be clearly documented:

- Each breaking change must be called out in the changelog under a **Breaking Changes** heading.
- A migration guide must be provided for any breaking change.
- The zero-version phase ends when the design system reaches v1.0.0.

---

## Changelog

Every release **must** update `changelog.md` following the [Keep a Changelog](https://keepachangelog.com/) format:

### Sections

| Section | Content |
|---------|---------|
| Added | New components, tokens, features |
| Changed | Non-breaking modifications |
| Deprecated | Components or features scheduled for removal |
| Removed | Components or features removed in this release |
| Fixed | Bug fixes |
| Security | Vulnerability fixes |

---

## Deprecation Schedule

Components marked as deprecated follow this removal timeline:

1. **Minor version N** — Component marked as `@deprecated`; replacement announced.
2. **Minor versions N+1, N+2** — Component maintained with critical bug fixes only.
3. **Major version (N+1).0.0** — Component removed; migration guide published.

---

## Release Frequency

| Release Type | Frequency | Process |
|-------------|-----------|---------|
| Minor | Monthly | Scheduled minor release with feature work |
| Patch | Weekly (as needed) | Bug fixes and patches between minor releases |
| Major | As needed | Coordinated with product roadmap; at least 1 minor version notice |

---

## Current Version

**v1.0.0** — Initial frozen release of the Enterprise Design System.
