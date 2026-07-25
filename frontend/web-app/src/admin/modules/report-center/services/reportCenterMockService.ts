import type { Report, ReportTemplate, ReportSchedule, ReportExport, BusinessIntelligenceReport, ReportSummary } from '../types';
import { ReportType, ReportStatus, ReportCategory, ExportFormat } from '../types';
import {
  DEFAULT_REPORTS,
  DEFAULT_TEMPLATES,
  DEFAULT_SCHEDULES,
  DEFAULT_EXPORTS,
  DEFAULT_BI_REPORTS,
  REPORT_SUMMARY_MOCK,
} from '../constants';

export class ReportCenterMockService {
  private reports: Report[] = [...DEFAULT_REPORTS];
  private templates: ReportTemplate[] = [...DEFAULT_TEMPLATES];
  private schedules: ReportSchedule[] = [...DEFAULT_SCHEDULES];
  private exports: ReportExport[] = [...DEFAULT_EXPORTS];
  private biReports: BusinessIntelligenceReport[] = [...DEFAULT_BI_REPORTS];

  async getReports(): Promise<Report[]> {
    return [...this.reports];
  }

  async getReportById(id: string): Promise<Report | undefined> {
    return this.reports.find((r) => r.id === id);
  }

  async getReportsByType(type: ReportType): Promise<Report[]> {
    return this.reports.filter((r) => r.type === type);
  }

  async getReportsByCategory(category: ReportCategory): Promise<Report[]> {
    return this.reports.filter((r) => r.category === category);
  }

  async getReportSummary(): Promise<ReportSummary> {
    return { ...REPORT_SUMMARY_MOCK };
  }

  async generateAllReports(): Promise<Report[]> {
    const generated = this.reports.map((r) => ({
      ...r,
      status: ReportStatus.GENERATED,
      generatedAt: new Date().toISOString(),
    }));
    this.reports = generated;
    return [...this.reports];
  }

  async exportReport(reportId: string, format: ExportFormat): Promise<ReportExport> {
    const report = this.reports.find((r) => r.id === reportId);
    const exportEntry: ReportExport = {
      id: `EXP-${String(this.exports.length + 1).padStart(3, '0')}`,
      reportId,
      format,
      exportedBy: 'System',
      fileSize: `${(Math.random() * 5 + 0.5).toFixed(1)} MB`,
      exportedAt: new Date().toISOString(),
    };
    this.exports.push(exportEntry);
    if (report) {
      report.status = ReportStatus.EXPORTED;
    }
    return exportEntry;
  }

  async getTemplates(): Promise<ReportTemplate[]> {
    return [...this.templates];
  }

  async getSchedules(): Promise<ReportSchedule[]> {
    return [...this.schedules];
  }

  async getBiReports(): Promise<BusinessIntelligenceReport[]> {
    return [...this.biReports];
  }
}
