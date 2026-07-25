export type RiskSeverity = 'CRITICAL' | 'HIGH' | 'MEDIUM' | 'LOW';
export type RiskCategory = 'REVENUE' | 'INVENTORY' | 'OPERATIONAL' | 'MARKETPLACE' | 'CUSTOMER' | 'VENDOR' | 'TRAINING' | 'PLATFORM' | 'AUTOMATION' | 'AI';

export interface BusinessRisk {
  id: string;
  title: string;
  description: string;
  category: RiskCategory;
  severity: RiskSeverity;
  domain: string;
  impact: string;
  likelihood: number;
  riskScore: number;
  mitigationSteps: string;
  status: string;
  createdAt: string;
}

export interface RiskSummary {
  totalRisks: number;
  critical: number;
  high: number;
  medium: number;
  low: number;
  averageRiskScore: number;
}
