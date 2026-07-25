import type { BusinessRisk, RiskSummary } from '../types';

const delay = (ms: number) => new Promise((resolve) => setTimeout(resolve, ms));

const MOCK_RISKS: BusinessRisk[] = [
  { id: 'rsk-001', title: 'Revenue Concentration Risk', description: 'Top 3 products contribute 65% of revenue', category: 'REVENUE', severity: 'HIGH', domain: 'REVENUE', impact: 'Product line over-concentration', likelihood: 62, riskScore: 74, mitigationSteps: 'Diversify product portfolio', status: 'ACTIVE', createdAt: new Date().toISOString() },
  { id: 'rsk-002', title: 'Stockout Risk', description: '5 high-demand SKUs at risk of stockout', category: 'INVENTORY', severity: 'HIGH', domain: 'INVENTORY', impact: 'Potential $30K lost sales', likelihood: 68, riskScore: 80, mitigationSteps: 'Implement automated reorder points', status: 'ACTIVE', createdAt: new Date().toISOString() },
  { id: 'rsk-003', title: 'Process Bottleneck Risk', description: 'Order fulfillment bottleneck at packing stage', category: 'OPERATIONAL', severity: 'MEDIUM', domain: 'OPERATIONS', impact: 'Throughput reduced by 15%', likelihood: 48, riskScore: 55, mitigationSteps: 'Automate packing workflow', status: 'ACTIVE', createdAt: new Date().toISOString() },
  { id: 'rsk-004', title: 'Market Share Risk', description: 'Competitor gaining market share', category: 'MARKETPLACE', severity: 'HIGH', domain: 'MARKETPLACE', impact: 'Market share down 4%', likelihood: 58, riskScore: 70, mitigationSteps: 'Enhance product discovery', status: 'ACTIVE', createdAt: new Date().toISOString() },
  { id: 'rsk-005', title: 'Customer Satisfaction Risk', description: 'NPS score dropped from 72 to 65', category: 'CUSTOMER', severity: 'MEDIUM', domain: 'CUSTOMERS', impact: 'Customer loyalty declining', likelihood: 52, riskScore: 60, mitigationSteps: 'Implement customer feedback loop', status: 'ACTIVE', createdAt: new Date().toISOString() },
];

export const riskDashboardMockService = {
  async getRisks(): Promise<BusinessRisk[]> {
    await delay(300);
    return [...MOCK_RISKS];
  },

  async getRiskSummary(): Promise<RiskSummary> {
    await delay(200);
    return { totalRisks: 5, critical: 0, high: 3, medium: 2, low: 0, averageRiskScore: 67.8 };
  },
};
