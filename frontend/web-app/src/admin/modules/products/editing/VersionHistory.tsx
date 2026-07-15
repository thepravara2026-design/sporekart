import React from 'react';
import type { ProductVersion } from './types';
import { StatusBadge } from '../../../components/status';
import { lifecycleLabel, lifecycleToBadge } from '../lifecycle';
import type { ProductLifecycleState } from '../types';
import { formatCurrency } from '../creation/validation';
import Card from '../../../../design-system/components/composite/Card';
import Button from '../../../../design-system/components/core/Button';
import Icon from '../../../../design-system/icons/Icon';

export interface VersionHistoryProps {
  versions: ProductVersion[];
  previewVersionId: string | null;
  canRestore: boolean;
  canDuplicate: boolean;
  onPreview: (id: string) => void;
  onClosePreview: () => void;
  onRestore: (id: string) => void;
  onDuplicate: (id: string) => void;
}

const rowStyle: React.CSSProperties = {
  display: 'flex',
  flexWrap: 'wrap',
  alignItems: 'center',
  gap: 'var(--space-3)',
  padding: 'var(--space-3) var(--space-4)',
  borderBottom: 'var(--border-width-thin) solid var(--color-border)',
};

const metaStyle: React.CSSProperties = {
  color: 'var(--color-text-secondary)',
  fontSize: 'var(--text-body-sm)',
};

const actionsStyle: React.CSSProperties = {
  display: 'flex',
  gap: 'var(--space-2)',
  marginLeft: 'auto',
};

const VersionHistoryBase: React.FC<VersionHistoryProps> = ({
  versions,
  previewVersionId,
  canRestore,
  canDuplicate,
  onPreview,
  onClosePreview,
  onRestore,
  onDuplicate,
}) => {
  const sorted = React.useMemo(
    () => [...versions].sort((a, b) => b.version - a.version),
    [versions],
  );

  const previewVersion = React.useMemo(
    () => versions.find((v) => v.id === previewVersionId) ?? null,
    [versions, previewVersionId],
  );

  return (
    <section aria-label="Version History">
      <h2
        style={{
          fontSize: 'var(--text-heading-sm)',
          fontWeight: 'var(--weight-semibold)',
          color: 'var(--color-text-primary)',
          margin: '0 0 var(--space-3) 0',
        }}
      >
        Version History
      </h2>

      {previewVersion && (
        <Card variant="outlined" padding="md" style={{ marginBottom: 'var(--space-4)' }}>
          <div
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: 'var(--space-2)',
              marginBottom: 'var(--space-3)',
            }}
          >
            <Icon name="Eye" size={18} />
            <strong style={{ color: 'var(--color-text-primary)' }}>
              Preview · Version {previewVersion.version}
            </strong>
            <StatusBadge
              status={lifecycleLabel(previewVersion.status)}
              variant={lifecycleToBadge(previewVersion.status)}
              size="sm"
            />
          </div>
          <dl
            style={{
              display: 'grid',
              gridTemplateColumns: 'max-content 1fr',
              gap: 'var(--space-2) var(--space-4)',
              margin: 0,
            }}
          >
            <dt style={metaStyle}>Name</dt>
            <dd style={{ margin: 0, color: 'var(--color-text-primary)' }}>{previewVersion.data.name}</dd>
            <dt style={metaStyle}>SKU</dt>
            <dd style={{ margin: 0, color: 'var(--color-text-primary)' }}>{previewVersion.data.sku}</dd>
            <dt style={metaStyle}>Status</dt>
            <dd style={{ margin: 0, color: 'var(--color-text-primary)' }}>
              {lifecycleLabel(previewVersion.status as ProductLifecycleState)}
            </dd>
            <dt style={metaStyle}>Price</dt>
            <dd style={{ margin: 0, color: 'var(--color-text-primary)' }}>
              {formatCurrency(previewVersion.data.price, previewVersion.data.currency)}
            </dd>
            <dt style={metaStyle}>MRP</dt>
            <dd style={{ margin: 0, color: 'var(--color-text-primary)' }}>
              {formatCurrency(previewVersion.data.mrp, previewVersion.data.currency)}
            </dd>
            <dt style={metaStyle}>Meta Title</dt>
            <dd style={{ margin: 0, color: 'var(--color-text-primary)' }}>
              {previewVersion.data.metaTitle || '—'}
            </dd>
            <dt style={metaStyle}>Summary</dt>
            <dd style={{ margin: 0, color: 'var(--color-text-primary)' }}>
              {previewVersion.data.shortDescription || '—'}
            </dd>
          </dl>
          <div style={{ marginTop: 'var(--space-4)', display: 'flex', justifyContent: 'flex-end' }}>
            <Button variant="secondary" size="sm" leftIcon={<Icon name="X" size={16} />} onClick={onClosePreview}>
              Close Preview
            </Button>
          </div>
        </Card>
      )}

      <Card variant="ghost" padding="none">
        {sorted.length === 0 ? (
          <div style={{ padding: 'var(--space-4)', ...metaStyle }}>No versions available.</div>
        ) : (
          <ul style={{ listStyle: 'none', margin: 0, padding: 0 }}>
            {sorted.map((v) => (
              <li key={v.id} style={rowStyle}>
                <Icon name="History" size={18} />
                <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                  <span style={{ color: 'var(--color-text-primary)', fontWeight: 'var(--weight-semibold)' }}>
                    Version {v.version}
                  </span>
                  <span style={metaStyle}>
                    {new Date(v.createdAt).toLocaleString()} · {v.modifiedBy}
                  </span>
                </div>
                <StatusBadge
                  status={lifecycleLabel(v.status)}
                  variant={lifecycleToBadge(v.status)}
                  size="sm"
                />
                {(v.reason || v.summary) && (
                  <span style={{ ...metaStyle, flexBasis: '100%' }}>
                    {v.reason ? `Reason: ${v.reason}` : ''}
                    {v.reason && v.summary ? ' · ' : ''}
                    {v.summary ? `Summary: ${v.summary}` : ''}
                  </span>
                )}
                <div style={actionsStyle}>
                  <Button
                    variant="outline"
                    size="sm"
                    leftIcon={<Icon name="Eye" size={16} />}
                    onClick={() => onPreview(v.id)}
                    aria-label={`Preview version ${v.version}`}
                  >
                    Preview
                  </Button>
                  <Button
                    variant="secondary"
                    size="sm"
                    leftIcon={<Icon name="RefreshCw" size={16} />}
                    disabled={!canRestore}
                    onClick={() => onRestore(v.id)}
                    aria-label={`Restore version ${v.version}`}
                  >
                    Restore
                  </Button>
                  <Button
                    variant="ghost"
                    size="sm"
                    leftIcon={<Icon name="Copy" size={16} />}
                    disabled={!canDuplicate}
                    onClick={() => onDuplicate(v.id)}
                    aria-label={`Duplicate version ${v.version}`}
                  >
                    Duplicate
                  </Button>
                </div>
              </li>
            ))}
          </ul>
        )}
      </Card>
    </section>
  );
};

export const VersionHistory = React.memo(VersionHistoryBase);
export default VersionHistory;
