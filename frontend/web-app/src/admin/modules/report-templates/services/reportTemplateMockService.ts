import type { ReportTemplate } from '../types';
import { DEFAULT_TEMPLATES } from '../constants';

export class ReportTemplateMockService {
  private templates: ReportTemplate[] = [...DEFAULT_TEMPLATES];

  async getTemplates(): Promise<ReportTemplate[]> {
    return [...this.templates];
  }

  async getTemplateById(id: string): Promise<ReportTemplate | undefined> {
    return this.templates.find((t) => t.id === id);
  }

  async getActiveTemplates(): Promise<ReportTemplate[]> {
    return this.templates.filter((t) => t.active);
  }

  async toggleTemplateStatus(id: string): Promise<ReportTemplate | undefined> {
    const template = this.templates.find((t) => t.id === id);
    if (template) {
      template.active = !template.active;
    }
    return template;
  }
}
