import { useState, useEffect, useCallback } from 'react';
import type { ReportTemplate } from './types';
import { ReportTemplateMockService } from './services/reportTemplateMockService';
import { CATEGORY_COLORS } from './constants';

const service = new ReportTemplateMockService();

function badgeStyle(color: string): React.CSSProperties {
  return {
    display: 'inline-block',
    padding: '2px 8px',
    borderRadius: 4,
    fontSize: 12,
    fontWeight: 600,
    color: '#fff',
    backgroundColor: color,
  };
}

export function ReportTemplatesDashboard() {
  const [templates, setTemplates] = useState<ReportTemplate[]>([]);
  const [loading, setLoading] = useState(true);

  const load = useCallback(async () => {
    setLoading(true);
    const t = await service.getTemplates();
    setTemplates(t);
    setLoading(false);
  }, []);

  useEffect(() => { load(); }, [load]);

  if (loading) {
    return <div style={{ padding: 24, color: 'var(--color-text-secondary)' }}>Loading templates...</div>;
  }

  const activeCount = templates.filter((t) => t.active).length;
  const totalSections = templates.reduce((sum, t) => sum + t.sectionsCount, 0);
  const totalUsage = templates.reduce((sum, t) => sum + t.usageCount, 0);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 20, padding: 24 }}>
      <div>
        <h1 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Report Templates</h1>
        <p style={{ margin: '4px 0 0', color: 'var(--color-text-secondary)', fontSize: 'var(--text-body)' }}>
          Manage report templates, layouts, and structure definitions.
        </p>
      </div>

      <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap' }}>
        {[
          { title: 'Total Templates', value: String(templates.length), color: '#2f6f4f' },
          { title: 'Active Templates', value: String(activeCount), color: '#1d9bf0' },
          { title: 'Total Sections', value: String(totalSections), color: '#7c3aed' },
          { title: 'Total Usage', value: String(totalUsage), color: '#d97706' },
        ].map((kpi) => (
          <div key={kpi.title} style={{ flex: 1, minWidth: 160, padding: 16, borderRadius: 8, border: '1px solid var(--color-border)', backgroundColor: 'var(--color-surface)' }}>
            <p style={{ margin: '0 0 4px', fontSize: 12, color: 'var(--color-text-secondary)' }}>{kpi.title}</p>
            <p style={{ margin: 0, fontSize: 24, fontWeight: 700, color: kpi.color }}>{kpi.value}</p>
          </div>
        ))}
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 16 }}>
        {templates.map((t) => (
          <div key={t.id} style={{ padding: 16, borderRadius: 8, border: '1px solid var(--color-border)', backgroundColor: 'var(--color-surface)', display: 'flex', flexDirection: 'column', gap: 8 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
              <div>
                <h3 style={{ margin: 0, fontSize: 16, fontWeight: 600 }}>{t.name}</h3>
                <p style={{ margin: '2px 0 0', fontSize: 13, color: 'var(--color-text-secondary)' }}>{t.description}</p>
              </div>
              <span style={{
                padding: '2px 8px',
                borderRadius: 10,
                fontSize: 11,
                fontWeight: 600,
                color: t.active ? '#2f6f4f' : '#6b7280',
                backgroundColor: t.active ? '#e0f2e9' : '#e5e7eb',
              }}>
                {t.active ? 'Active' : 'Inactive'}
              </span>
            </div>
            <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
              <span style={badgeStyle(CATEGORY_COLORS[t.category] || '#6b7280')}>{t.category}</span>
              <span style={badgeStyle('#6b7280')}>{t.type}</span>
            </div>
            <div style={{ display: 'flex', gap: 16, fontSize: 13, color: 'var(--color-text-secondary)' }}>
              <span>{t.sectionsCount} sections</span>
              <span>Used {t.usageCount} times</span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
