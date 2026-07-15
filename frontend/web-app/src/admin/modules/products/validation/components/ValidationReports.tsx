import React from 'react';
import type { ValidationReport } from '../types';

interface ValidationReportsProps {
  reports: ValidationReport[];
}

const typeIcons: Record<string, string> = {
  validation_summary: '📊',
  compliance: '🛡️',
  marketplace: '🛒',
  seo: '🔍',
  accessibility: '♿',
  executive: '📋',
};

const typeColors: Record<string, string> = {
  validation_summary: '#3b82f6',
  compliance: '#8b5cf6',
  marketplace: '#059669',
  seo: '#d97706',
  accessibility: '#6366f1',
  executive: '#7c3aed',
};

const typeLabels: Record<string, string> = {
  validation_summary: 'Validation Summary',
  compliance: 'Compliance',
  marketplace: 'Marketplace',
  seo: 'SEO',
  accessibility: 'Accessibility',
  executive: 'Executive',
};

function scoreColor(s: number): string {
  if (s >= 80) return 'var(--color-success)';
  if (s >= 60) return 'var(--color-warning)';
  return 'var(--color-danger)';
}

export const ValidationReports = React.memo(function ValidationReports({ reports }: ValidationReportsProps) {
  return (
    <div style={{ padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 }} aria-label="Validation reports">
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Validation Reports</h2>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 16 }}>
        {reports.map((report) => (
          <article key={report.id} style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', gap: 10 }} aria-label={report.title}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
                <span style={{ fontSize: 22, lineHeight: 1 }} aria-hidden="true">{typeIcons[report.type] ?? '📄'}</span>
                <div>
                  <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{report.title}</div>
                  <span style={{ display: 'inline-flex', padding: '1px 6px', borderRadius: 'var(--radius-xs)', fontSize: 'var(--text-caption)', fontWeight: 600, color: '#fff', background: typeColors[report.type] ?? 'var(--color-text-secondary)' }}>
                    {typeLabels[report.type] ?? report.type}
                  </span>
                </div>
              </div>
              <div style={{ textAlign: 'right' }}>
                <div style={{ fontSize: 'var(--text-h5)', fontWeight: 700, color: scoreColor(report.overallScore) }}>{report.overallScore}%</div>
                <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>score</div>
              </div>
            </div>
            <div style={{ display: 'flex', gap: 16, fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>
              <span>{new Date(report.generatedAt).toLocaleDateString()}</span>
              <span>•</span>
              <span>{report.generatedBy}</span>
              <span>•</span>
              <span>{report.productCount} product{report.productCount !== 1 ? 's' : ''}</span>
            </div>
            <p style={{ margin: 0, fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', lineHeight: 1.5, display: '-webkit-box', WebkitLineClamp: 2, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
              {report.summary}
            </p>
          </article>
        ))}
      </div>

      {reports.length === 0 && (
        <div style={{ textAlign: 'center', padding: '40px 0', color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body-sm)' }}>
          No reports available.
        </div>
      )}
    </div>
  );
});
