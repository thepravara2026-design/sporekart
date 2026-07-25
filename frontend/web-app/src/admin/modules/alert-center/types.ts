export type AlertStatus = 'OPEN' | 'ACKNOWLEDGED' | 'RESOLVED' | 'CLOSED' | 'ESCALATED';
export type AlertSeverity = 'CRITICAL' | 'HIGH' | 'MEDIUM' | 'LOW' | 'INFO';
export type AlertCategory = 'BUSINESS' | 'OPERATIONAL' | 'SECURITY' | 'PERFORMANCE' | 'MAINTENANCE' | 'INFORMATIONAL';

export interface Alert {
  id: string;
  title: string;
  description: string;
  category: AlertCategory;
  severity: AlertSeverity;
  priority: string;
  domain: string;
  status: AlertStatus;
  businessImpact: string;
  suggestedResolution: string;
  supportingMetrics: Record<string, unknown>;
  source: string;
  createdAt: string;
  acknowledgedAt: string | null;
  resolvedAt: string | null;
}

export interface AlertMetrics {
  totalAlerts: number;
  open: number;
  acknowledged: number;
  resolved: number;
  escalated: number;
  critical: number;
  high: number;
  medium: number;
  low: number;
}
