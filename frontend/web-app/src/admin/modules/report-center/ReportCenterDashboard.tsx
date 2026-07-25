import { useState, useEffect, useCallback } from 'react';
import type { Report, ReportSummary } from './types';
import { ReportStatus, ReportCategory, ReportType, ExportFormat } from './types';
import { ReportCenterMockService } from './services/reportCenterMockService';
import {
  REPORT_TYPE_LABELS,
  REPORT_CATEGORY_LABELS,
  REPORT_STATUS_LABELS,
  REPORT_TYPE_COLORS,
  REPORT_CATEGORY_COLORS,
  REPORT_STATUS_COLORS,
} from './constants';

const service = new ReportCenterMockService();

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

function KpiCard({ title, value, color }: { title: string; value: string; color: string }) {
  return (
    <div style={{ flex: 1, minWidth: 180, padding: 16, borderRadius: 8, border: '1px solid var(--color-border)', backgroundColor: 'var(--color-surface)' }}>
      <p style={{ margin: '0 0 4px', fontSize: 12, color: 'var(--color-text-secondary)' }}>{title}</p>
      <p style={{ margin: 0, fontSize: 24, fontWeight: 700, color }}>{value}</p>
    </div>
  );
}

export function ReportCenterDashboard() {
  const [reports, setReports] = useState<Report[]>([]);
  const [summary, setSummary] = useState<ReportSummary | null>(null);
  const [typeFilter, setTypeFilter] = useState<string>('');
  const [categoryFilter, setCategoryFilter] = useState<string>('');
  const [statusFilter, setStatusFilter] = useState<string>('');
  const [loading, setLoading] = useState(true);

  const loadData = useCallback(async () => {
    setLoading(true);
    const [r, s] = await Promise.all([service.getReports(), service.getReportSummary()]);
    setReports(r);
    setSummary(s);
    setLoading(false);
  }, []);

  useEffect(() => { loadData(); }, [loadData]);

  const handleExport = async (reportId: string) => {
    await service.exportReport(reportId, ExportFormat.PDF);
    loadData();
  };

  const filtered = reports.filter((r) => {
    if (typeFilter && r.type !== typeFilter) return false;
    if (categoryFilter && r.category !== categoryFilter) return false;
    if (statusFilter && r.status !== statusFilter) return false;
    return true;
  });

  if (loading) {
    return <div style={{ padding: 24, color: 'var(--color-text-secondary)' }}>Loading report center...</div>;
  }

  const executiveCount = reports.filter((r) => r.type === ReportType.EXECUTIVE).length;
  const revenueCount = reports.filter((r) => r.category === ReportCategory.REVENUE).length;
  const generatedCount = reports.filter((r) => r.status === ReportStatus.GENERATED).length;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 20, padding: 24 }}>
      <div>
        <h1 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Report Center</h1>
        <p style={{ margin: '4px 0 0', color: 'var(--color-text-secondary)', fontSize: 'var(--text-body)' }}>
          Enterprise reporting, analytics, and business intelligence.
        </p>
      </div>

      <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap' }}>
        <KpiCard title="Total Reports" value={String(summary?.totalReports ?? 0)} color="#2f6f4f" />
        <KpiCard title="Executive Reports" value={String(executiveCount)} color="#7c3aed" />
        <KpiCard title="Revenue Reports" value={String(revenueCount)} color="#1d9bf0" />
        <KpiCard title="Generated Reports" value={String(generatedCount)} color="#2f6f4f" />
      </div>

      {summary && (
        <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap' }}>
          {Object.entries(summary.byStatus).map(([key, count]) => (
            <div key={key} style={{ padding: '8px 12px', borderRadius: 6, border: '1px solid var(--color-border)', backgroundColor: 'var(--color-surface)', minWidth: 100 }}>
              <span style={badgeStyle(REPORT_STATUS_COLORS[key] || '#6b7280')}>{REPORT_STATUS_LABELS[key] || key}</span>
              <span style={{ marginLeft: 8, fontWeight: 600 }}>{count}</span>
            </div>
          ))}
        </div>
      )}

      <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap' }}>
        <select
          value={typeFilter}
          onChange={(e) => setTypeFilter(e.target.value)}
          style={{ padding: '6px 12px', borderRadius: 6, border: '1px solid var(--color-border)', backgroundColor: 'var(--color-surface)', color: 'var(--color-text-primary)' }}
        >
          <option value="">All Types</option>
          {Object.values(ReportType).map((t) => (
            <option key={t} value={t}>{REPORT_TYPE_LABELS[t] || t}</option>
          ))}
        </select>
        <select
          value={categoryFilter}
          onChange={(e) => setCategoryFilter(e.target.value)}
          style={{ padding: '6px 12px', borderRadius: 6, border: '1px solid var(--color-border)', backgroundColor: 'var(--color-surface)', color: 'var(--color-text-primary)' }}
        >
          <option value="">All Categories</option>
          {Object.values(ReportCategory).map((c) => (
            <option key={c} value={c}>{REPORT_CATEGORY_LABELS[c] || c}</option>
          ))}
        </select>
        <select
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value)}
          style={{ padding: '6px 12px', borderRadius: 6, border: '1px solid var(--color-border)', backgroundColor: 'var(--color-surface)', color: 'var(--color-text-primary)' }}
        >
          <option value="">All Statuses</option>
          {Object.values(ReportStatus).map((s) => (
            <option key={s} value={s}>{REPORT_STATUS_LABELS[s] || s}</option>
          ))}
        </select>
      </div>

      <div style={{ overflowX: 'auto', borderRadius: 8, border: '1px solid var(--color-border)' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 14 }}>
          <thead>
            <tr style={{ backgroundColor: 'var(--color-surface-secondary)', borderBottom: '1px solid var(--color-border)' }}>
              <th style={{ padding: '10px 12px', textAlign: 'left', fontWeight: 600, color: 'var(--color-text-secondary)' }}>Title</th>
              <th style={{ padding: '10px 12px', textAlign: 'left', fontWeight: 600, color: 'var(--color-text-secondary)' }}>Type</th>
              <th style={{ padding: '10px 12px', textAlign: 'left', fontWeight: 600, color: 'var(--color-text-secondary)' }}>Category</th>
              <th style={{ padding: '10px 12px', textAlign: 'left', fontWeight: 600, color: 'var(--color-text-secondary)' }}>Status</th>
              <th style={{ padding: '10px 12px', textAlign: 'left', fontWeight: 600, color: 'var(--color-text-secondary)' }}>Owner</th>
              <th style={{ padding: '10px 12px', textAlign: 'left', fontWeight: 600, color: 'var(--color-text-secondary)' }}>Generated At</th>
              <th style={{ padding: '10px 12px', textAlign: 'center', fontWeight: 600, color: 'var(--color-text-secondary)' }}>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filtered.map((r) => (
              <tr key={r.id} style={{ borderBottom: '1px solid var(--color-border)', backgroundColor: 'var(--color-surface)' }}>
                <td style={{ padding: '10px 12px', fontWeight: 500 }}>{r.title}</td>
                <td style={{ padding: '10px 12px' }}><span style={badgeStyle(REPORT_TYPE_COLORS[r.type] || '#6b7280')}>{REPORT_TYPE_LABELS[r.type] || r.type}</span></td>
                <td style={{ padding: '10px 12px' }}><span style={badgeStyle(REPORT_CATEGORY_COLORS[r.category] || '#6b7280')}>{REPORT_CATEGORY_LABELS[r.category] || r.category}</span></td>
                <td style={{ padding: '10px 12px' }}><span style={badgeStyle(REPORT_STATUS_COLORS[r.status] || '#6b7280')}>{REPORT_STATUS_LABELS[r.status] || r.status}</span></td>
                <td style={{ padding: '10px 12px', color: 'var(--color-text-secondary)' }}>{r.owner}</td>
                <td style={{ padding: '10px 12px', color: 'var(--color-text-secondary)' }}>{new Date(r.generatedAt).toLocaleDateString()}</td>
                <td style={{ padding: '10px 12px', textAlign: 'center' }}>
                  <button
                    onClick={() => handleExport(r.id)}
                    style={{ padding: '4px 12px', borderRadius: 4, border: '1px solid var(--color-border)', backgroundColor: 'transparent', color: 'var(--color-text-primary)', cursor: 'pointer', fontSize: 12 }}
                  >
                    Export
                  </button>
                </td>
              </tr>
            ))}
            {filtered.length === 0 && (
              <tr>
                <td colSpan={7} style={{ padding: 24, textAlign: 'center', color: 'var(--color-text-secondary)' }}>No reports match the selected filters.</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
