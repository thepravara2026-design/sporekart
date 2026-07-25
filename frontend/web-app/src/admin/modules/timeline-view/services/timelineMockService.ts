import type { TimelineEvent } from '../types';

const delay = (ms: number) => new Promise((resolve) => setTimeout(resolve, ms));

const MOCK_EVENTS: TimelineEvent[] = [
  { id: 'tm-001', eventType: 'BUSINESS', category: 'REVENUE', domain: 'REVENUE', title: 'Revenue Alert Generated', description: 'Revenue drop of 15% detected', severity: 'CRITICAL', source: 'alert-engine', metadata: { dropPercent: 15 }, timestamp: new Date(Date.now() - 3600000).toISOString() },
  { id: 'tm-002', eventType: 'ALERT', category: 'INVENTORY', domain: 'INVENTORY', title: 'Inventory Alert', description: '3 SKUs below threshold', severity: 'HIGH', source: 'alert-engine', metadata: { affectedSKUs: 3 }, timestamp: new Date(Date.now() - 7200000).toISOString() },
  { id: 'tm-003', eventType: 'RISK', category: 'REVENUE', domain: 'REVENUE', title: 'Revenue Risk Assessment', description: 'Revenue risk score updated to 78', severity: 'HIGH', source: 'risk-engine', metadata: { riskScore: 78 }, timestamp: new Date(Date.now() - 10800000).toISOString() },
  { id: 'tm-004', eventType: 'PLATFORM', category: 'SYSTEM', domain: 'PLATFORM', title: 'Platform Health Check', description: 'Memory usage at 87%', severity: 'MEDIUM', source: 'anomaly-engine', metadata: { memoryUsage: 87 }, timestamp: new Date(Date.now() - 14400000).toISOString() },
  { id: 'tm-005', eventType: 'AI', category: 'AI_PLATFORM', domain: 'AI_PLATFORM', title: 'AI Runtime Warning', description: 'AI inference error rate at 4.7%', severity: 'HIGH', source: 'anomaly-engine', metadata: { errorRate: 4.7 }, timestamp: new Date(Date.now() - 18000000).toISOString() },
  { id: 'tm-006', eventType: 'WORKFLOW', category: 'WORKFLOW', domain: 'WORKFLOW', title: 'Workflow Failure', description: 'Order fulfillment workflow failure rate at 3.1%', severity: 'HIGH', source: 'alert-engine', metadata: { failureRate: 3.1 }, timestamp: new Date(Date.now() - 21600000).toISOString() },
  { id: 'tm-007', eventType: 'TRAINING', category: 'TRAINING', domain: 'TRAINING', title: 'Training Alert', description: 'Training completion rate fell to 72%', severity: 'MEDIUM', source: 'alert-engine', metadata: { completionRate: 72 }, timestamp: new Date(Date.now() - 25200000).toISOString() },
  { id: 'tm-008', eventType: 'INVENTORY', category: 'INVENTORY', domain: 'INVENTORY', title: 'Inventory Anomaly', description: 'Inventory outlier detected for 3 SKUs', severity: 'HIGH', source: 'anomaly-engine', metadata: { anomalyType: 'OUTLIER' }, timestamp: new Date(Date.now() - 28800000).toISOString() },
  { id: 'tm-009', eventType: 'MARKETPLACE', category: 'MARKETPLACE', domain: 'MARKETPLACE', title: 'Marketplace Slowdown', description: 'Transaction volume dropped 18%', severity: 'HIGH', source: 'alert-engine', metadata: { volumeDrop: 18 }, timestamp: new Date(Date.now() - 32400000).toISOString() },
];

export const timelineMockService = {
  async getEvents(): Promise<TimelineEvent[]> {
    await delay(300);
    return [...MOCK_EVENTS];
  },
};
