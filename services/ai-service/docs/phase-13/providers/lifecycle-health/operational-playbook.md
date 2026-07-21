# Enterprise AI Provider Operational Playbook

## Provider Lifecycle Operations

### Activate Provider
1. Verify provider registered in registry
2. Check provider health status
3. Transition to ACTIVE state
4. Start heartbeat monitoring
5. Verify health checks pass
6. Enable in Gateway routing

### Deactivate Provider
1. Drain active requests
2. Transition to MAINTENANCE or UNAVAILABLE
3. Stop heartbeat monitoring
4. Remove from Gateway routing
5. Verify zero active connections

## Recovery Procedures

### Automatic Recovery
1. Circuit breaker detects failures
2. Opens circuit after threshold
3. Waits cooldown period
4. Transitions to half-open
5. Tests with limited requests
6. Closes circuit on success

### Manual Recovery
1. Diagnose root cause
2. Apply fix
3. Run health checks
4. Reset circuit breaker
5. Reactivate provider

## Maintenance Windows

### Planned Maintenance
1. Schedule maintenance window
2. Notify dependent services
3. Drain provider traffic
4. Enter maintenance mode
5. Perform updates
6. Run health validation
7. Exit maintenance mode
8. Restore traffic

### Emergency Maintenance
1. Immediately enter maintenance
2. Apply emergency fix
3. Expedite health validation
4. Return to active state

## Health Escalation

1. Health check failure → log and metrics
2. 3 consecutive failures → alert
3. 5 consecutive failures → page on-call
4. Circuit open → automatic failover
5. Recovery failure → escalate to engineering

## Monitoring Thresholds

- Health check interval: 30s
- Heartbeat interval: 10s
- Heartbeat expiry: 30s
- Circuit failure threshold: 5
- Circuit reset timeout: 30s
- Recovery cooldown: 60s
- SLA target: 99.9%
