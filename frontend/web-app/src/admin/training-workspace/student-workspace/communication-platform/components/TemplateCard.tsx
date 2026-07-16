import { memo } from 'react';
import type { NotificationTemplate } from '../types';
import { TypeBadge } from './TypeBadge';

export const TemplateCard = memo(function TemplateCard({ template }: { template: NotificationTemplate }) {
  return (
    <div style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: `1px solid ${template.isActive ? 'var(--color-border-default)' : '#fef2f2'}`, background: template.isActive ? 'var(--color-bg-surface-default)' : '#fef2f2', display: 'flex', flexDirection: 'column', gap: 8 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 4 }}>
        <TypeBadge type={template.type} />
        <span style={{ fontSize: 'var(--text-caption)', color: template.isActive ? '#16a34a' : '#dc2626', fontWeight: 'var(--weight-medium)' }}>
          {template.isActive ? 'Active' : 'Inactive'}
        </span>
      </div>
      <h4 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{template.name}</h4>
      <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: 0 }}>Subject: {template.subject}</p>
      <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', margin: 0, lineHeight: 1.4, whiteSpace: 'pre-wrap', maxHeight: 60, overflow: 'hidden' }}>{template.body}</p>
      <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
        {template.variables.map((v) => (
          <span key={v} style={{ fontSize: 'var(--text-caption)', padding: '1px 6px', borderRadius: 'var(--radius-sm)', background: '#eff6ff', color: '#2563eb' }}>{`{{${v}}}`}</span>
        ))}
      </div>
    </div>
  );
});
