# PROJECT_DOCUMENTATION

Central documentation repository for the SporeKart platform.

Defined in `docs/ENGINEERING_OPERATIONS_MANUAL.md` (Section 9). Every folder has a named owner and a documented update cadence.

| Folder | Owner | Contents |
|---|---|---|
| `Architecture/` | Architect | System, service, and data-flow diagrams |
| `Engineering Standards/` | Eng Lead | Coding standards, this manual |
| `Sprint Reports/` | Sprint Lead | Per-sprint execution summaries |
| `QA Reports/` | QA Lead | Test runs, coverage, evidence |
| `Bug Reports/` | QA | Defect records, root-cause analysis |
| `Release Notes/` | Release Mgr | Version changelogs |
| `Git Strategy/` | DevOps Lead | Branch/merge strategy, the operations manual |
| `Runbooks/` | SRE | Operational procedures |
| `Operations/` | SRE | Monitoring, on-call, incidents |
| `ADRs/` | Architect | Architecture Decision Records |
| `Production/` | Release Mgr | Deployments, rollback records |
| `Security/` | Security Lead | Threat models, audits |
| `Testing/` | QA Lead | Strategy, frameworks, coverage |

## Update Cadence

- **Per sprint:** Sprint Reports, QA Reports, Bug Reports.
- **Per release:** Release Notes, Production, Git Strategy (if changed).
- **On decision:** ADRs, Architecture, Engineering Standards.
- **On incident:** Operations, Runbooks, Security.

## Rules

- No documentation in this tree may contain secrets or credentials.
- All changes follow the Engineering Operations Manual SOP (Section 2).
- Deviations from standards require an approved ADR in `ADRs/`.
