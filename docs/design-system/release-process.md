# Release Process

## Overview

The design system release process governs how new components, tokens, and changes move from the Playground through review into production. This is a staged, gated process designed to maintain quality and prevent regressions.

## Release Stages

### Stage 1: Staging Preview

**Owner:** Component Engineer / Release Manager  
**Actions:**
- All changes are merged to the `develop` branch
- A staging build is deployed to the staging environment
- Staging URL is shared with the Design System Council
- All components in the release are verified against the manifest

**Gate Criteria:**
- [ ] Staging build deploys without errors
- [ ] All manifest entries point to correct preview URLs
- [ ] No broken links in the catalog or documentation center
- [ ] Search index works on the staging build

### Stage 2: Review Gates

**Owner:** Design System Council  
**Actions:**
- Each component is reviewed against the 8-stage pipeline
- Accessibility audit is run on all changed/added components
- Responsive validation is run on all changed/added components
- Documentation is reviewed for completeness
- Changelog draft is reviewed

**Gate Criteria:**
- [ ] All changed components have completed the review pipeline up to Stage 6
- [ ] No P0 or P1 accessibility violations
- [ ] Responsive validation passes for all changed components
- [ ] Documentation is complete for all new/changed components
- [ ] Changelog is accurate and complete

### Stage 3: Version Bump

**Owner:** Principal Design System Architect  
**Actions:**
- Determine version bump level (major/minor/patch) based on changes
- Update `@sporekart/tokens` version if tokens changed
- Update `@sporekart/ui` version if components changed
- Update component-level versions in `componentManifest.ts`
- Update peer dependency ranges if needed

**Gate Criteria:**
- [ ] Version bump is appropriate for the change set
- [ ] All component versions are consistent with their change level
- [ ] Token and component versions are compatible
- [ ] Peer dependency ranges are valid

### Stage 4: Changelog Generation

**Owner:** Technical Writer  
**Actions:**
- Generate changelog from commit history between last tag and current HEAD
- Categorize changes (Added, Changed, Fixed, Deprecated, Removed, Security)
- Review each entry for accuracy
- Add migration notes for breaking changes
- Review by Principal DS Architect

**Gate Criteria:**
- [ ] All notable changes are represented
- [ ] Breaking changes have migration notes
- [ ] Deprecations are clearly marked with timeline
- [ ] Changelog entries reference issue/PR numbers

### Stage 5: npm Publish (Future)

**Owner:** Release Manager  
**Actions:**
- Run `npm run build:tokens` to build token package
- Run `npm run build:ui` to build component package
- Run `npm run test` to execute full test suite
- Run `npm run lint` to execute full lint suite
- Run `npm publish` for `@sporekart/tokens`
- Run `npm publish` for `@sporekart/ui`
- Tag the release in git

**Gate Criteria:**
- [ ] All tests pass (unit, integration, visual regression)
- [ ] Lint passes with zero errors
- [ ] Build completes without errors
- [ ] Bundle size is within budget
- [ ] npm publish succeeds

### Stage 6: Announcement

**Owner:** Design System Steward  
**Actions:**
- Write release announcement for:
  - Slack #design-system channel
  - Email to design system consumers
  - Monthly design system newsletter
- Include in the announcement:
  - Version number
  - Key highlights
  - Breaking changes and migration guide links
  - New components and their preview URLs
  - Deprecation notices and timelines
  - Thanks to contributors

**Gate Criteria:**
- [ ] Announcement is published to all channels
- [ ] Migration guides are linked for all breaking changes
- [ ] Known issues are documented
- [ ] Support Slack channel is monitored for questions

## Release Checklist

### Pre-Release (1 week before)

- [ ] Freeze feature changes
- [ ] Run full accessibility audit
- [ ] Run full responsive validation
- [ ] Review documentation completeness
- [ ] Draft changelog
- [ ] Tag release candidate
- [ ] Deploy release candidate to staging

### Release Day

- [ ] Final review of release candidate
- [ ] Run full test suite
- [ ] Version bump
- [ ] Generate final changelog
- [ ] Build packages
- [ ] Publish to npm
- [ ] Tag release in git
- [ ] Deploy to production
- [ ] Verify production deployment
- [ ] Send announcement

### Post-Release (1 week after)

- [ ] Monitor for regressions
- [ ] Review support channel for issues
- [ ] Collect feedback from consumers
- [ ] Plan hotfixes if needed
- [ ] Retrospective with design system team

## Hotfix Process

For P0/P1 bugs that cannot wait for the next monthly release:

1. Bug is reported and confirmed
2. Hotfix branch is created from the last release tag
3. Fix is implemented and reviewed
4. Patch version is bumped
5. Changelog is updated
6. Package is published as a patch release
7. Fix is cherry-picked back to `develop`

**SLA:** P0 bugs: 24 hours; P1 bugs: 72 hours

## Release Roles

| Role | Responsibility |
|------|----------------|
| Release Manager | Coordinates the release process |
| Principal DS Architect | Approves version bump and release content |
| Technical Writer | Generates changelog and announcements |
| Component Owners | Verify their components in the release |
| QA Engineer | Runs validation tests |
| Design System Council | Final approval |

## Release Artifacts

| Artifact | Location | Format |
|----------|----------|--------|
| npm package: tokens | npm registry | `.tgz` |
| npm package: ui | npm registry | `.tgz` |
| Changelog | `/CHANGELOG.md` | Markdown |
| Release tag | git | `v2026.2.0` |
| Release notes | GitHub Releases | Markdown |
| Migration guides | `/docs/design-system/migrations/` | Markdown |
