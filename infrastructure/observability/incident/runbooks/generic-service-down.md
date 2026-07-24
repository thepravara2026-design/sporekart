# Runbook: Service Down

## Severity: SEV1

## Symptoms
- Service health check failing
- 503/502 errors from upstream
- Grafana dashboard shows no data
- PagerDuty alert triggered

## Immediate Actions

1. **ACKNOWLEDGE** the alert in PagerDuty
2. **ANNOUNCE** in #sporekart-sev1
3. **CHECK** k9s or kubectl for pod status:
   ```
   kubectl get pods -n sporekart-platform -l app=<service>
   ```
4. **CHECK** pod logs:
   ```
   kubectl logs -n sporekart-platform -l app=<service> --tail=100
   ```
5. **CHECK** service endpoints:
   ```
   kubectl get endpoints -n sporekart-platform <service>
   ```

## Diagnosis Steps

### Step 1: Crash Loop?
```
kubectl describe pod -n sporekart-platform <pod-name>
```
- Check `lastState` for termination reason
- Check events for OOMKilled, ImagePullBackOff, CrashLoopBackOff

### Step 2: Resource Exhaustion?
- Check memory: `kubectl top pod -n sporekart-platform`
- Check CPU: `kubectl top pod -n sporekart-platform`
- Compare with resource limits

### Step 3: Database Connectivity?
```
kubectl exec -n sporekart-platform <pod> -- nc -zv postgres:5432
```
- Check if database is reachable
- Check connection pool metrics in Grafana

### Step 4: Cache/Redis?
```
kubectl exec -n sporekart-platform <pod> -- nc -zv redis:6379
```
- Check Redis connectivity
- Check cache hit ratio

### Step 5: Configuration Issue?
- Check ConfigMap: `kubectl get configmap -n sporekart-platform <service> -o yaml`
- Check Secrets: `kubectl get secret -n sporekart-platform <service> -o yaml`

## Recovery Actions

### Option A: Restart Deployment
```
kubectl rollout restart deployment/<service> -n sporekart-platform
```

### Option B: Rollback to Previous Version
```
kubectl rollout undo deployment/<service> -n sporekart-platform
```

### Option C: Scale Up Temporarily
```
kubectl scale deployment/<service> --replicas=5 -n sporekart-platform
```

## Verification
1. Health check passes: `curl http://<service>/actuator/health`
2. Readiness probe passes
3. Traffic flowing (check Grafana)
4. No errors in logs

## Post-Recovery
1. Update incident timeline in #sporekart-incidents
2. Assign root cause analysis
3. Schedule postmortem within 24h (SEV1)
4. Update this runbook with findings

## Contact
- On-Call: @sre-oncall
- Engineering Lead: @eng-lead
- VP Engineering: @vp-eng
