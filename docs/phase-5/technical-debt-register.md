# Technical Debt Register

---

## Minor Improvements (Sprint 21)

| Item                                               | Category      | Effort | Impact |
|----------------------------------------------------|---------------|--------|--------|
| Archive `DesignShowcase.tsx`                       | Code cleanup  | 1h     | Low    |
| Create barrel exports per component category       | DX            | 4h     | Medium |
| Add ESLint config                                  | Tooling       | 2h     | High   |
| Add `aria-hidden` to decorative icons              | A11y          | 2h     | Medium |

---

## Medium Priority (Sprint 21–22)

| Item                                                 | Category   | Effort | Impact |
|------------------------------------------------------|------------|--------|--------|
| Vitest + React Testing Library setup                 | Testing    | 8h     | High   |
| Component unit tests for core 10 components          | Testing    | 16h    | High   |
| GitHub Actions CI                                    | DevOps     | 8h     | High   |
| Bundle analyzer                                      | Performance| 4h     | Medium |

---

## Major Improvements (Sprint 22+)

| Item                                                | Category   | Effort | Impact |
|-----------------------------------------------------|------------|--------|--------|
| Storybook integration                               | DX         | 20h    | High   |
| Auto-generate component manifest from filesystem    | DX         | 8h     | Medium |
| Virtual scrolling for Table                         | Performance| 16h    | High   |
| Dark / High Contrast theme completion               | Theming    | 24h    | High   |

---

## Known Limitations

1. ESLint not configured; linting is manual only
2. No automated test suite exists
3. No visual regression testing
4. No CI pipeline configured
5. Dark theme: foundation only, not feature-complete
6. High contrast theme: foundation only, not feature-complete
7. Table virtual scrolling not implemented
8. Component manifest manually maintained

---

## Deferred Features

1. Rich text editor (planned Sprint 21+)
2. Advanced table with column resize / inline edit
3. Date/time picker components
4. Notification system real-time integration
5. Onboarding / tour components
6. Help system integration
