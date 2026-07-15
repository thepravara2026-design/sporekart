import React from 'react';
import Card from '../../../../design-system/components/composite/Card';
import Button from '../../../../design-system/components/core/Button';
import Icon from '../../../../design-system/icons/Icon';
import type { ChangedField, EditSectionId } from './types';
import { formatValue } from './changeDetection';
import { FIELD_LABELS } from '../creation/validation';

export interface ChangeDetectionPanelProps {
  changed: ChangedField[];
  original: Record<string, unknown>;
  current: Record<string, unknown>;
  onDiscardAll: () => void;
  onResetSection: (section: EditSectionId) => void;
}

const SECTION_LABELS: Record<EditSectionId, string> = {
  overview: 'Overview',
  basic: 'Basic Info',
  classification: 'Classification',
  packaging: 'Packaging & Physical',
  pricing: 'Pricing',
  seo: 'SEO',
  publishing: 'Publishing',
  activity: 'Activity',
  history: 'History',
  settings: 'Settings',
};

const ChangeDetectionPanelBase: React.FC<ChangeDetectionPanelProps> = ({
  changed,
  onDiscardAll,
  onResetSection,
}) => {
  const grouped = React.useMemo(() => {
    const map = new Map<EditSectionId, ChangedField[]>();
    for (const c of changed) {
      if (!map.has(c.section)) map.set(c.section, []);
      map.get(c.section)!.push(c);
    }
    return map;
  }, [changed]);

  return (
    <Card variant="outlined" padding="lg" aria-label="Change detection">
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 'var(--space-3)', marginBottom: 'var(--space-3)', flexWrap: 'wrap' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-2)' }}>
          <Icon name="GitCompare" size={18} />
          <h3 style={{ margin: 0, fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>
            Change Detection
          </h3>
          {changed.length > 0 && (
            <span
              style={{
                fontSize: 'var(--text-caption)',
                color: 'var(--color-text-warning)',
                background: 'var(--color-bg-warning-weak)',
                padding: '2px 8px',
                borderRadius: 'var(--radius-full)',
                fontWeight: 'var(--weight-semibold)',
              }}
            >
              {changed.length} field{changed.length === 1 ? '' : 's'}
            </span>
          )}
        </div>
        <Button
          variant="ghost"
          size="sm"
          disabled={changed.length === 0}
          onClick={onDiscardAll}
          leftIcon={<Icon name="Undo" size={16} />}
        >
          Discard all changes
        </Button>
      </div>

      {changed.length === 0 ? (
        <p style={{ margin: 0, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
          No unsaved modifications. The product matches its last saved version.
        </p>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-3)' }}>
          {Array.from(grouped.entries()).map(([section, fields]) => (
            <div key={section}>
              <div
                style={{
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'space-between',
                  marginBottom: 'var(--space-2)',
                }}
              >
                <span style={{ fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-secondary)', textTransform: 'uppercase', letterSpacing: '0.04em' }}>
                  {SECTION_LABELS[section]}
                </span>
                <Button
                  variant="ghost"
                  size="sm"
                  onClick={() => onResetSection(section)}
                  leftIcon={<Icon name="RefreshCw" size={14} />}
                >
                  Reset section
                </Button>
              </div>
              <ul style={{ listStyle: 'none', margin: 0, padding: 0, display: 'flex', flexDirection: 'column', gap: 'var(--space-1)' }}>
                {fields.map((c) => (
                  <li
                    key={c.field}
                    style={{
                      display: 'grid',
                      gridTemplateColumns: 'minmax(140px, 1fr) auto minmax(140px, 1fr)',
                      alignItems: 'center',
                      gap: 'var(--space-3)',
                      padding: 'var(--space-2) var(--space-3)',
                      borderRadius: 'var(--radius-sm)',
                      background: 'var(--color-bg-surface-raised)',
                      fontSize: 'var(--text-body-sm)',
                    }}
                  >
                    <div style={{ minWidth: 0 }}>
                      <div style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>{FIELD_LABELS[c.field] ?? c.field}</div>
                      <div style={{ color: 'var(--color-text-secondary)', textDecoration: 'line-through', wordBreak: 'break-word' }}>
                        {formatValue(c.oldValue)}
                      </div>
                    </div>
                    <Icon name="ArrowRight" size={16} style={{ color: 'var(--color-text-tertiary)', flexShrink: 0 }} />
                    <div style={{ minWidth: 0, color: 'var(--color-text-primary)', fontWeight: 'var(--weight-medium)', wordBreak: 'break-word' }}>
                      {formatValue(c.newValue)}
                    </div>
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </div>
      )}
    </Card>
  );
};

export const ChangeDetectionPanel = React.memo(ChangeDetectionPanelBase);
export default ChangeDetectionPanel;
