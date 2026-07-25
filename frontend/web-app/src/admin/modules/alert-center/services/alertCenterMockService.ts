import type { Alert, AlertMetrics } from '../types';

const delay = (ms: number) => new Promise((resolve) => setTimeout(resolve, ms));

const MOCK_ALERTS: Alert[] = [
  { id: 'alt-001', title: 'Revenue Drop Detected', description: 'Revenue dropped 15% below forecast in last 24 hours', category: 'BUSINESS', severity: 'CRITICAL', priority: 'P0', domain: 'REVENUE', status: 'OPEN', businessImpact: 'Potential $45K revenue loss', suggestedResolution: 'Investigate sales channels and adjust pricing', supportingMetrics: { dropPercent: 15 }, source: 'alert-engine', createdAt: new Date().toISOString(), acknowledgedAt: null, resolvedAt: null },
  { id: 'alt-002', title: 'Inventory Below Threshold', description: '3 SKUs below minimum stock threshold', category: 'OPERATIONAL', severity: 'HIGH', priority: 'P1', domain: 'INVENTORY', status: 'OPEN', businessImpact: 'Risk of stockout', suggestedResolution: 'Trigger reorder', supportingMetrics: { affectedSKUs: 3 }, source: 'alert-engine', createdAt: new Date().toISOString(), acknowledgedAt: null, resolvedAt: null },
  { id: 'alt-003', title: 'Order Failure Spike', description: 'Order failure rate increased to 5.2%', category: 'OPERATIONAL', severity: 'HIGH', priority: 'P1', domain: 'ORDERS', status: 'ACKNOWLEDGED', businessImpact: 'Customer satisfaction at risk', suggestedResolution: 'Investigate checkout pipeline', supportingMetrics: { failureRate: 5.2 }, source: 'alert-engine', createdAt: new Date().toISOString(), acknowledgedAt: new Date().toISOString(), resolvedAt: null },
  { id: 'alt-004', title: 'Failed Login Attempts Spike', description: 'Failed login attempts up 300% in last hour', category: 'SECURITY', severity: 'CRITICAL', priority: 'P0', domain: 'SECURITY', status: 'OPEN', businessImpact: 'Possible brute force attack', suggestedResolution: 'Enable rate limiting', supportingMetrics: { failedAttempts: 1256 }, source: 'alert-engine', createdAt: new Date().toISOString(), acknowledgedAt: null, resolvedAt: null },
  { id: 'alt-005', title: 'API Latency Increase', description: 'P95 API response time increased to 850ms', category: 'PERFORMANCE', severity: 'MEDIUM', priority: 'P2', domain: 'PLATFORM', status: 'OPEN', businessImpact: 'User experience degradation', suggestedResolution: 'Optimize database queries', supportingMetrics: { p95Latency: 850 }, source: 'alert-engine', createdAt: new Date().toISOString(), acknowledgedAt: null, resolvedAt: null },
  { id: 'alt-006', title: 'SSL Certificate Expiring', description: 'SSL certificate expires in 7 days', category: 'MAINTENANCE', severity: 'HIGH', priority: 'P1', domain: 'PLATFORM', status: 'RESOLVED', businessImpact: 'Service will be unavailable', suggestedResolution: 'Renew SSL certificate', supportingMetrics: { daysToExpiry: 7 }, source: 'alert-engine', createdAt: new Date().toISOString(), acknowledgedAt: new Date().toISOString(), resolvedAt: new Date().toISOString() },
];

export const alertCenterMockService = {
  async getAlerts(): Promise<Alert[]> {
    await delay(300);
    return [...MOCK_ALERTS];
  },

  async acknowledgeAlert(id: string): Promise<Alert> {
    await delay(200);
    const alert = MOCK_ALERTS.find((a) => a.id === id);
    if (!alert) throw new Error('Alert not found');
    alert.status = 'ACKNOWLEDGED';
    alert.acknowledgedAt = new Date().toISOString();
    return { ...alert };
  },

  async resolveAlert(id: string): Promise<Alert> {
    await delay(200);
    const alert = MOCK_ALERTS.find((a) => a.id === id);
    if (!alert) throw new Error('Alert not found');
    alert.status = 'RESOLVED';
    alert.resolvedAt = new Date().toISOString();
    return { ...alert };
  },

  async getAlertMetrics(): Promise<AlertMetrics> {
    await delay(200);
    const alerts = MOCK_ALERTS;
    return {
      totalAlerts: alerts.length,
      open: alerts.filter((a) => a.status === 'OPEN').length,
      acknowledged: alerts.filter((a) => a.status === 'ACKNOWLEDGED').length,
      resolved: alerts.filter((a) => a.status === 'RESOLVED').length,
      escalated: alerts.filter((a) => a.status === 'ESCALATED').length,
      critical: alerts.filter((a) => a.severity === 'CRITICAL').length,
      high: alerts.filter((a) => a.severity === 'HIGH').length,
      medium: alerts.filter((a) => a.severity === 'MEDIUM').length,
      low: alerts.filter((a) => a.severity === 'LOW').length,
    };
  },
};
