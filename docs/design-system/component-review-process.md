# Component Review Process — 8-Stage Pipeline

## Overview

Every new or changed component passes through an 8-stage review pipeline before it is considered production-ready. This process ensures visual consistency, accessibility compliance, responsive behavior, and documentation completeness.

## Pipeline Stages

### Stage 1: Preview

**Owner:** Component Engineer  
**Gate Criteria:**
- Component renders without errors in the Playground
- All variants are displayed on the preview page
- Component accepts all public props
- Basic interaction flows work (click, type, focus, blur)

**Output:** Playground preview page at `/design-system/component/:id`

### Stage 2: Review

**Owner:** Design System Council  
**Gate Criteria:**
- Component matches Figma spec (pixel-level comparison)
- All visual states are present (default, hover, focus, active, disabled, error)
- Responsive behavior at key breakpoints (1280px, 768px, 375px)
- Dark mode renders correctly
- Keyboard navigation is logical and complete

**Output:** Review notes with issues/approval

### Stage 3: Collect Feedback

**Owner:** Component Engineer  
**Gate Criteria:**
- All design feedback is logged as issues
- All accessibility feedback is documented
- Platform team feedback (Android/iOS) is incorporated where applicable
- Feedback items are triaged: P0 (blocking), P1 (should fix), P2 (nice to have)

**Output:** Triage list of feedback items

### Stage 4: Redesign

**Owner:** Component Engineer / Product Designer  
**Gate Criteria:**
- All P0 feedback items are addressed
- Code is updated and pushed
- Preview page reflects changes
- No new issues introduced by changes

**Output:** Updated component and preview

### Stage 5: Updated Preview

**Owner:** Component Engineer  
**Gate Criteria:**
- Changes are deployed to the Playground
- Reviewers can access updated preview at same URL
- Change log is noted in the component's manifest entry
- Version is bumped (patch for fixes, minor for new variants)

**Output:** Updated preview for re-review

### Stage 6: Approval

**Owner:** Principal Design System Architect  
**Gate Criteria:**
- All P0 and P1 feedback is resolved
- Accessibility audit passes (axe-core: 0 violations)
- Responsive validation passes at all breakpoints
- Component is documented (props, examples, usage guidelines)
- Tests pass (unit, integration, visual regression)
- Bundle size is within budget

**Output:** Signed approval in manifest (`approvalDate`, `reviewer`)

### Stage 7: Freeze

**Owner:** Principal Design System Architect  
**Gate Criteria:**
- Component is stable for 2 consecutive sprints in production
- No P0 bugs open for 30 days
- API is stable (no prop changes in the last sprint)
- Deprecation/removal plan exists for old patterns

**Output:** Freeze marker in manifest; component enters frozen state

### Stage 8: Version Increment → Documentation Update

**Owner:** Release Manager / Technical Writer  
**Gate Criteria:**
- Semantic version is determined (major/minor/patch based on change type)
- Changelog entry is written following the changelog format
- Documentation center is updated with new component details
- Migration guide is available if breaking changes exist
- Announcement is prepared for design system consumers

**Output:** Version bump, updated changelog, documentation commit

## Stage Transition Rules

- Stages **must** execute in order. No skipping.
- A stage can request a return to any earlier stage (e.g., Stage 6 Approval can send back to Stage 2 Review).
- Each return requires a new review cycle for the affected stages.
- The manifest `reviewStatus` reflects the current stage: `pending` (Stages 1–5), `changes-requested` (returned), `approved` (Stage 6+).

## Escalation

If a component remains in Stages 2–5 for more than 2 weeks:
- Component Engineer escalates to Design System Council
- Council reviews blocker list and assigns owners
- 48-hour SLA for resolution

## Metrics Tracked

- **Time-in-stage** — how long each component spends per stage
- **Return rate** — how often a component is sent back
- **Average cycle time** — total time from Stage 1 to Stage 8
- **Approval percentage** — components approved vs. reviewed in a sprint
