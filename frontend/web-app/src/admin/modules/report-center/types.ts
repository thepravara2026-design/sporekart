export enum ReportType {
  DAILY = 'daily',
  WEEKLY = 'weekly',
  MONTHLY = 'monthly',
  QUARTERLY = 'quarterly',
  YEARLY = 'yearly',
  CUSTOM = 'custom',
  EXECUTIVE = 'executive',
  OPERATIONAL = 'operational',
  BUSINESS = 'business',
  PERFORMANCE = 'performance',
  HEALTH = 'health',
  AUDIT = 'audit',
}

export enum ReportStatus {
  DRAFT = 'draft',
  GENERATED = 'generated',
  SCHEDULED = 'scheduled',
  EXPORTED = 'exported',
  FAILED = 'failed',
}

export enum ReportCategory {
  EXECUTIVE = 'executive',
  REVENUE = 'revenue',
  SALES = 'sales',
  ORDERS = 'orders',
  INVENTORY = 'inventory',
  CUSTOMERS = 'customers',
  PRODUCTS = 'products',
  MARKETPLACE = 'marketplace',
  TRAINING = 'training',
  VENDORS = 'vendors',
  GROWERS = 'growers',
  AI_PLATFORM = 'ai_platform',
  AUTOMATION = 'automation',
  PLATFORM_HEALTH = 'platform_health',
  RISK = 'risk',
  BUSINESS_HEALTH = 'business_health',
  COMPLIANCE = 'compliance',
}

export enum ExportFormat {
  PDF = 'pdf',
  EXCEL = 'excel',
  CSV = 'csv',
  JSON = 'json',
}

export enum ScheduleFrequency {
  DAILY = 'daily',
  WEEKLY = 'weekly',
  MONTHLY = 'monthly',
  QUARTERLY = 'quarterly',
  YEARLY = 'yearly',
  CUSTOM = 'custom',
}

export interface Report {
  id: string;
  title: string;
  description: string;
  type: ReportType;
  category: ReportCategory;
  status: ReportStatus;
  owner: string;
  summary: string;
  businessHealth: string;
  recommendations: string[];
  risks: string[];
  kpis: Record<string, string | number>;
  supportingMetrics: Record<string, string | number>;
  templateId?: string;
  traceId?: string;
  executionTimeMs?: number;
  generatedAt: string;
  createdAt: string;
}

export interface ReportTemplate {
  id: string;
  name: string;
  description: string;
  category: ReportCategory;
  type: ReportType;
  sections: string[];
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface ReportSchedule {
  id: string;
  reportId: string;
  frequency: ScheduleFrequency;
  recipients: string[];
  format: ExportFormat;
  nextRunAt: string;
  active: boolean;
  createdAt: string;
}

export interface ReportExport {
  id: string;
  reportId: string;
  format: ExportFormat;
  exportedBy: string;
  fileSize: string;
  exportedAt: string;
}

export interface BusinessIntelligenceReport {
  id: string;
  title: string;
  description: string;
  metrics: Record<string, string | number>;
  insights: string[];
  createdAt: string;
}

export interface ReportSummary {
  totalReports: number;
  byType: Record<string, number>;
  byCategory: Record<string, number>;
  byStatus: Record<string, number>;
}
