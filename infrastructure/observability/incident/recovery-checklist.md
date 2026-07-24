# Incident Recovery Checklist

## Discovery Phase
- [ ] Alert acknowledged in PagerDuty
- [ ] Incident declared in #sporekart-incidents
- [ ] Severity assessed and assigned
- [ ] On-call engineer assigned
- [ ] Initial status posted to incident channel

## Assessment Phase
- [ ] Service affected identified
- [ ] Impact scope determined (users, services, data)
- [ ] Root cause investigation started
- [ ] Logs checked for errors
- [ ] Metrics checked for anomalies
- [ ] Traces checked for failures
- [ ] Dependencies checked (DB, cache, AI, queue)

## Mitigation Phase
- [ ] Immediate fix applied
- [ ] Rollback initiated (if applicable)
- [ ] Traffic redirected (if applicable)
- [ ] Degraded mode enabled (if applicable)
- [ ] Affected users notified
- [ ] Mitigation verified working

## Recovery Phase
- [ ] Service restored to normal operation
- [ ] Health checks passing
- [ ] Metrics returning to baseline
- [ ] Alerts clearing
- [ ] Error rate normalizing
- [ ] Latency normalizing

## Verification Phase
- [ ] Synthetic tests passing
- [ ] Performance tests passing
- [ ] Integration tests passing
- [ ] Security checks passing
- [ ] All dependencies healthy
- [ ] Monitoring shows stable

## Post-Recovery Phase
- [ ] Incident resolved in PagerDuty
- [ ] Incident timeline documented
- [ ] Root cause documented
- [ ] Action items created
- [ ] Postmortem scheduled
- [ ] Runbook updated with findings
- [ ] Team notified of resolution

## Communication Checklist
- [ ] Status update to incident channel
- [ ] Status update to stakeholders
- [ ] Customer impact communicated
- [ ] Postmortem shared with team
- [ ] Lessons learned documented
