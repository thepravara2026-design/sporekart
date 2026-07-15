import { memo, type CSSProperties } from 'react';
import type { ClassificationType } from '../types';

const typeColors: Record<ClassificationType, CSSProperties> = {
  raw_material: { background: 'var(--color-info-alpha)', color: 'var(--color-info)' },
  work_in_progress: { background: 'var(--color-warning-alpha)', color: 'var(--color-warning)' },
  finished_good: { background: 'var(--color-success-alpha)', color: 'var(--color-success)' },
  consumable: { background: 'var(--color-neutral-alpha)', color: 'var(--color-neutral)' },
  asset: { background: 'var(--color-primary-alpha)', color: 'var(--color-primary)' },
  packaging: { background: 'var(--color-info-alpha)', color: 'var(--color-info)' },
  supplies: { background: 'var(--color-surface-hover)', color: 'var(--color-text-secondary)' },
};

interface Props {
  type: ClassificationType;
}

export const ClassificationBadge = memo(function ClassificationBadge({ type }: Props) {
  const colors = typeColors[type] ?? typeColors.supplies;
  return (
    <span style={{ display: 'inline-block', padding: '2px 8px', borderRadius: 'var(--radius-badge)', fontSize: 'var(--text-caption)', fontWeight: 500, ...colors }}>
      {type.replace(/_/g, ' ')}
    </span>
  );
});
