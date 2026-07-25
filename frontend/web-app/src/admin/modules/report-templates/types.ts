export interface ReportTemplate {
  id: string;
  name: string;
  description: string;
  category: string;
  type: string;
  sectionsCount: number;
  active: boolean;
  usageCount: number;
  createdAt: string;
  updatedAt: string;
}
