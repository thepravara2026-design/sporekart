export interface RevenueMetrics {
  totalRevenue: number;
  totalOrders: number;
  averageOrderValue: number;
  growthRate: number;
  revenueByProduct: Record<string, number>;
  revenueByCategory: Record<string, number>;
  revenueByRegion: Record<string, number>;
}

export interface CustomerAnalytics {
  totalCustomers: number;
  newCustomers: number;
  churnRate: number;
  customerLifetimeValue: number;
  retentionRate: number;
  customersBySegment: Record<string, number>;
}

export interface TrainingAnalytics {
  totalStudents: number;
  averageScore: number;
  completionRate: number;
  certificationsIssued: number;
}

export interface CultivationAnalytics {
  totalYieldKg: number;
  averageYieldPerBatch: number;
  contaminationRate: number;
  yieldBySpecies: Record<string, number>;
}

export interface BusinessInsight {
  insightId: string;
  title: string;
  description: string;
  category: string;
  severity: string;
  recommendation: string;
  confidenceScore: number;
  actionable: boolean;
}

export interface TrendDataPoint {
  period: string;
  value: number;
  movingAverage: number;
  direction: string;
  changePercent: number;
}

export interface AnomalyAlert {
  anomalyId: string;
  metric: string;
  observedValue: number;
  expectedValue: number;
  deviationScore: number;
  severity: string;
  description: string;
  recommendedAction: string;
}

export interface ForecastPoint {
  period: string;
  predictedValue: number;
  lowerBound: number;
  upperBound: number;
}

export interface ForecastResult {
  forecastId: string;
  metric: string;
  method: string;
  points: ForecastPoint[];
  confidenceInterval: number;
  recommendations: string;
}

export interface DashboardWidget {
  widgetId: string;
  title: string;
  widgetType: string;
  dataSource: string;
  metric: string;
  width: number;
  height: number;
}

export interface CustomerSegment {
  segmentId: string;
  name: string;
  customerCount: number;
  totalRevenue: number;
  averageRevenue: number;
  churnRate: number;
  recommendedStrategies: string[];
}

export interface CrossCopilotMetric {
  copilotType: string;
  metricName: string;
  currentValue: number;
  changePercent: number;
  trend: string;
}

export interface DashboardData {
  dashboardId: string;
  widgets: DashboardWidget[];
  summaryData: Record<string, unknown>;
  insights: BusinessInsight[];
}

export interface ChatMessage {
  id: string;
  role: 'user' | 'assistant';
  content: string;
  timestamp: string;
  suggestions?: Suggestion[];
}

export interface Suggestion {
  label: string;
  action: string;
  payload?: Record<string, unknown>;
}
