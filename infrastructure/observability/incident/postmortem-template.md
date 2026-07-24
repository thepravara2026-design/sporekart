# Incident Postmortem

## Incident Metadata

- **Incident ID**: INC-YYYY-MM-DD-###
- **Date**: YYYY-MM-DD
- **Duration**: HH:MM (start) → HH:MM (end)
- **Severity**: SEV1 / SEV2 / SEV3
- **Reported By**: @person
- **Summary**: One-line description of what happened

## Timeline

| Time (UTC) | Event |
|------------|-------|
| HH:MM | Alert triggered: [alert name] |
| HH:MM | Engineer acknowledged |
| HH:MM | Incident declared in #sporekart-sev1 |
| HH:MM | Initial diagnosis: [finding] |
| HH:MM | Mitigation action taken: [action] |
| HH:MM | Service recovered |
| HH:MM | Monitoring confirmed stable |
| HH:MM | Incident resolved |

## Impact

- **Users Affected**: [number or percentage]
- **Services Affected**: [list of services]
- **Error Rate**: [% during incident]
- **Latency Impact**: [P95/P99 during incident]
- **Revenue Impact**: [$ amount if applicable]
- **Data Loss**: [Yes/No, details]

## Root Cause

### Primary Cause
[Detailed description of what caused the incident]

### Contributing Factors
1. [Factor 1]
2. [Factor 2]
3. [Factor 3]

## Detection

- **How was it detected?**: [Alert / User report / Automated]
- **Time to detection**: [minutes]
- **Was detection timely?**: [Yes/No, why]

## Response

- **Time to first response**: [minutes]
- **Time to mitigation**: [minutes]
- **Time to resolution**: [minutes]
- **What went well?**:
  1. [Positive aspect]
  2. [Positive aspect]
- **What went poorly?**:
  1. [Negative aspect]
  2. [Negative aspect]

## Action Items

| # | Action | Owner | Ticket | Due Date |
|---|--------|-------|--------|----------|
| 1 | [Action] | @owner | LINK | YYYY-MM-DD |
| 2 | [Action] | @owner | LINK | YYYY-MM-DD |

## Prevention

### Short-term (within 1 week)
- [Action 1]
- [Action 2]

### Long-term (within 1 month)
- [Action 1]
- [Action 2]

## Lessons Learned

1. [Lesson 1]
2. [Lesson 2]
3. [Lesson 3]

## Appendix

- **Grafana Dashboard**: [link]
- **Alert History**: [link]
- **Logs**: [link]
- **Related Incidents**: [links]

---

**Reviewed By**: @reviewer
**Approved By**: @eng-lead
**Date**: YYYY-MM-DD
