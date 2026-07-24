# Runbook: AI Provider Failure

## Severity: SEV2 (SEV1 if no fallback available)

## Symptoms
- AI completion failures increasing
- Provider latency > 5s
- Fallback count spiking
- Provider returning 429/503 errors
- Error budget burning for AI SLO

## Immediate Actions

1. **ACKNOWLEDGE** alert in PagerDuty
2. **ANNOUNCE** in #sporekart-sev2 (#sporekart-sev1 if critical)
3. **CHECK** provider status:
   - Azure OpenAI: https://status.azure.com
   - OpenAI: https://status.openai.com
   - Anthropic: https://status.anthropic.com

## Diagnosis

### Provider Metrics
- Check Grafana AI dashboard: provider latency, error rate
- Check provider routing: which provider is failing?
- Check fallback status: is fallback working?

### Error Analysis
```bash
kubectl logs -n sporekart-ai -l app=ai-service --tail=200 | grep "provider.*error"
```
Check for:
- Rate limit errors (429)
- Timeout errors (504)
- Authentication errors (401/403)
- Model not found (404)

## Recovery Actions

### Option A: Route to Fallback Provider
Update ConfigMap to update provider priority:
```bash
kubectl edit configmap ai-provider-config -n sporekart-ai
```
Set failed provider to lowest priority, promote fallback.

### Option B: Reduce Request Rate
```bash
kubectl edit configmap ai-rate-limits -n sporekart-ai
```
Reduce rate limit to stay within provider quota.

### Option C: Degrade Gracefully
Enable degraded mode (cache-only responses):
```bash
kubectl patch configmap ai-feature-flags -n sporekart-ai -p '{"data":{"degraded_mode":"true"}}'
```

### Option D: Wait for Provider Recovery
- Monitor provider status page
- Set up automated recovery check
- Re-enable provider once status shows healthy

## Verification
1. Provider health check passing
2. AI completion success rate > 99%
3. Fallback count decreasing
4. Latency within SLO (< 3s P95)
5. No provider errors in logs

## Post-Recovery
1. Review provider quota and rate limits
2. Consider adding additional provider fallback
3. Update AI provider routing logic if needed
4. Postmortem within 48h

## Contact
- AI Team: @ai-team
- Provider Support: OpenAI/Azure support tickets
