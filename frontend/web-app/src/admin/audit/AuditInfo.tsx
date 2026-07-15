import { memo } from 'react';
import type { AuditMeta } from './types';

interface AuditInfoProps {
  meta: AuditMeta;
  compact?: boolean;
}

export const AuditInfo = memo(function AuditInfo({ meta, compact = false }: AuditInfoProps) {
  if (compact) {
    return (
      <div style={{ display: 'flex', gap: 12, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', flexWrap: 'wrap' }}>
        <span>Created by <strong>{meta.createdBy}</strong></span>
        <span>{meta.createdAt}</span>
        {meta.updatedBy && <span>· Updated by <strong>{meta.updatedBy}</strong></span>}
        {meta.updatedAt && <span>· {meta.updatedAt}</span>}
        {meta.version !== undefined && <span>· v{meta.version}</span>}
      </div>
    );
  }

  return (
    <div
      style={{
        display: 'flex',
        gap: 24,
        padding: '12px 16px',
        border: '1px solid var(--color-border)',
        borderRadius: 'var(--radius-md)',
        background: 'var(--color-surface)',
        fontSize: 'var(--text-caption)',
        color: 'var(--color-text-secondary)',
        flexWrap: 'wrap',
      }}
    >
      <div>
        <div style={{ color: 'var(--color-text-tertiary)', marginBottom: 2 }}>Created by</div>
        <div style={{ fontWeight: 500, color: 'var(--color-text-primary)' }}>{meta.createdBy}</div>
        <div style={{ color: 'var(--color-text-tertiary)' }}>{meta.createdAt}</div>
      </div>
      {meta.updatedBy && (
        <div>
          <div style={{ color: 'var(--color-text-tertiary)', marginBottom: 2 }}>Last updated by</div>
          <div style={{ fontWeight: 500, color: 'var(--color-text-primary)' }}>{meta.updatedBy}</div>
          <div style={{ color: 'var(--color-text-tertiary)' }}>{meta.updatedAt}</div>
        </div>
      )}
      {meta.version !== undefined && (
        <div>
          <div style={{ color: 'var(--color-text-tertiary)', marginBottom: 2 }}>Version</div>
          <div style={{ fontWeight: 500, color: 'var(--color-text-primary)' }}>{meta.version}</div>
        </div>
      )}
    </div>
  );
});
