# Governance Automation Runbook

## Overview
This runbook covers operational procedures for the Governance Automation & Lifecycle Orchestration Platform.

## Automation Management

### Creating an Automation Job
1. Define the job class implementing the `AutomationJob` interface
2. Register the job via `POST /api/v1/automation/jobs`
3. Configure schedule frequency and timing
4. Attach retry and escalation policies
5. Enable the job to start execution

### Monitoring Jobs
- Use `GET /api/v1/automation/jobs` to list all jobs
- Use `GET /api/v1/automation/jobs/{jobId}` for detailed status
- Monitor job execution count, failure count, last run status
- Check `GET /api/v1/automation/scheduled-tasks` for upcoming executions

### Managing Job Failures
1. Check `lastRunStatus` and `failureCount` on job details
2. Review task execution logs for error details
3. Verify retry policy configuration
4. Check escalation policy for manual intervention
5. Manual retry via `POST /api/v1/automation/jobs/{jobId}/execute`

## Scheduler Configuration

### Frequency Types
- **ONCE** — Use for one-time maintenance tasks. Specify exact `scheduledAt` time.
- **HOURLY** — Use for frequent checks. Set `interval` in hours.
- **DAILY** — Use for daily maintenance. Specify `time` in HH:mm:ss.
- **WEEKLY** — Use for weekly tasks. Specify `dayOfWeek` and `time`.
- **MONTHLY** — Use for monthly tasks. Specify `dayOfMonth` and `time`.
- **CRON** — Use for complex schedules. Provide full cron expression.

### Best Practices
- Avoid scheduling multiple jobs at the same time to prevent resource contention
- Use timezone-aware scheduling for global deployments
- Enable overlap prevention for long-running jobs
- Set execution windows for jobs with time constraints
- Configure catch-up policy for missed executions appropriately

## Retry Policy Configuration

### Retry Parameters
| Parameter | Recommended | Description |
|-----------|-------------|-------------|
| maxAttempts | 3-5 | Maximum retry attempts |
| backoffIntervalMs | 5000-30000 | Initial backoff in milliseconds |
| backoffMultiplier | 2.0-3.0 | Exponential backoff multiplier |
| maxBackoffIntervalMs | 300000-600000 | Maximum backoff cap |
| jitterEnabled | true | Add randomness to backoff |

### Example Configurations
- **Transient failures:** maxAttempts=5, backoffMs=5000, multiplier=2.0
- **Resource contention:** maxAttempts=3, backoffMs=30000, multiplier=3.0
- **Critical tasks:** maxAttempts=5, backoffMs=10000, multiplier=2.0, jitter=true

## Escalation Policy Configuration

### Escalation Levels
```json
{
  "levels": [
    {
      "level": 1,
      "thresholdMs": 3600000,
      "action": "NOTIFY_OWNER",
      "targets": ["role:ai_operator"]
    },
    {
      "level": 2,
      "thresholdMs": 14400000,
      "action": "NOTIFY_MANAGER",
      "targets": ["role:ai_automation_manager"]
    },
    {
      "level": 3,
      "thresholdMs": 86400000,
      "action": "OVERRIDE",
      "targets": ["role:ai_administrator"]
    }
  ]
}
```

### Best Practices
- Configure at least 3 escalation levels for critical jobs
- Set realistic thresholds based on job criticality
- Notify multiple targets per level for redundancy
- Use OVERRIDE action only at highest level
- Audit all escalation actions for compliance

## Lifecycle Management

### State Transitions
- Verify source state before requesting transition
- Check transition validation rules for restrictions
- Ensure proper authorization for the transition
- Monitor transition duration via history

### Expiration Policies
- Configure TTL based on entity type retention requirements
- Set notification windows for pre-expiration alerts
- Enable auto-execute for unattended expiration handling
- Review expired entities periodically

## Monitoring & Alerting

### Key Metrics
- **Job Execution Rate** — Number of jobs executed per minute
- **Job Success Rate** — Percentage of successful executions
- **Workflow Completion Rate** — Percentage of completed workflows
- **Average Execution Duration** — Mean job execution time
- **Escalation Rate** — Number of escalations triggered
- **Retry Rate** — Number of retry attempts
- **Lifecycle Transitions** — Number of transitions per minute
- **Expired Entities** — Number of entities expired

### Health Checks
- Verify Automation Engine status via health endpoint
- Check Redis cache connectivity
- Verify Kafka topic availability
- Monitor database connection pool
- Check scheduler thread pool health

## Troubleshooting

### Job Not Executing
1. Check if job is enabled (`enabled=true`)
2. Verify schedule configuration is valid
3. Check scheduler thread pool availability
4. Verify distributed lock was released from previous execution
5. Review application logs for scheduler errors

### Transition Failed
1. Verify entity type is registered for lifecycle management
2. Check that transition is valid in the state machine
3. Verify user has authorization for the transition
4. Check for concurrent transition conflicts
5. Review transition history for error details

### Workflow Timeout
1. Verify timeout configuration for the workflow
2. Check individual step execution durations
3. Review resource constraints (CPU, memory, I/O)
4. Consider increasing timeout or optimizing steps
5. Use compensation for partial completions

### Escalation Not Triggering
1. Verify escalation policy is attached to job
2. Check escalation level thresholds are configured
3. Verify escalation targets are valid
4. Check escalation service is enabled
5. Review escalation audit for configuration errors

### Redis Cache Issues
1. Check `automation:lock:` namespace for stale locks
2. Verify TTL configurations are appropriate
3. Monitor cache hit/miss ratios
4. Clear stale entries if needed via Redis CLI
5. Verify Redis cluster connectivity

## Emergency Procedures

### Force Stop a Running Job
1. Call `POST /api/v1/automation/jobs/{jobId}/cancel`
2. Verify distributed lock is released
3. Check for partial state changes
4. Initiate compensation if needed
5. Review audit trail for the cancellation

### Manual Lifecycle Override
1. Ensure AI_ADMINISTRATOR authorization
2. Request transition with force flag
3. Verify target state is valid
4. Record reason in audit trail
5. Monitor for cascading effects

### Cache Invalidation
1. Use Redis CLI to clear specific namespaces
2. Clear `automation:lock:` for stuck locks
3. Clear `automation:job:` for stale job definitions
4. Clear `automation:schedule:` for incorrect schedules
5. Verify cache rebuild on next access
