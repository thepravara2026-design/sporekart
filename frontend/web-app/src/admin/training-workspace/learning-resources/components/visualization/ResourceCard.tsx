import { Card } from '../../../../../design-system/components/composite/Card';
import { Badge } from '../../../../../design-system/components/display/Badge';
import type { ResourceItem } from '../../data/resourceMockData';
import { RESOURCE_TYPE_ICONS, RESOURCE_TYPE_LABELS } from '../../data/resourceMockData';

interface ResourceCardProps {
  resource: ResourceItem;
  variant?: 'grid' | 'list' | 'compact';
  onOpen?: (id: string) => void;
  onFavorite?: (id: string) => void;
  onPin?: (id: string) => void;
}

const STATUS_VARIANT: Record<string, 'success' | 'warning' | 'neutral'> = {
  active: 'success',
  draft: 'warning',
  archived: 'neutral',
};

export function ResourceCard({ resource, variant = 'grid', onOpen, onFavorite, onPin }: ResourceCardProps) {
  const icon = RESOURCE_TYPE_ICONS[resource.type];

  if (variant === 'compact') {
    return (
      <Card variant="outlined" padding="sm" hoverable onClick={() => onOpen?.(resource.id)} style={{ cursor: 'pointer' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <span style={{ fontSize: 18 }}>{icon}</span>
          <span style={{ flex: 1, fontSize: 'var(--font-size-sm)', fontWeight: 600, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{resource.name}</span>
          <span style={{ fontSize: 12 }} onClick={(e) => { e.stopPropagation(); onFavorite?.(resource.id); }}>{resource.favorite ? '\u2B50' : '\u2606'}</span>
        </div>
      </Card>
    );
  }

  if (variant === 'list') {
    return (
      <Card variant="outlined" padding="sm" hoverable onClick={() => onOpen?.(resource.id)} style={{ cursor: 'pointer' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
          <span style={{ fontSize: 22 }}>{icon}</span>
          <div style={{ flex: 1, minWidth: 0 }}>
            <div style={{ fontWeight: 600, fontSize: 'var(--font-size-sm)' }}>{resource.name}</div>
            <div style={{ fontSize: 12, color: 'var(--color-text-tertiary)' }}>{resource.code} · {resource.category}</div>
          </div>
          <Badge variant={STATUS_VARIANT[resource.status]} size="sm">{resource.status}</Badge>
          <span style={{ cursor: 'pointer' }} onClick={(e) => { e.stopPropagation(); onFavorite?.(resource.id); }}>{resource.favorite ? '\u2B50' : '\u2606'}</span>
          <span style={{ cursor: 'pointer' }} onClick={(e) => { e.stopPropagation(); onPin?.(resource.id); }}>{resource.pinned ? '\ud83d\udccc' : '\ud83d\udccd'}</span>
        </div>
      </Card>
    );
  }

  return (
    <Card variant="elevated" padding="md" hoverable onClick={() => onOpen?.(resource.id)} style={{ cursor: 'pointer' }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <span style={{ width: 44, height: 44, borderRadius: 10, background: resource.color, color: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 22 }}>{icon}</span>
        <div style={{ display: 'flex', gap: 6 }}>
          <span style={{ cursor: 'pointer' }} onClick={(e) => { e.stopPropagation(); onFavorite?.(resource.id); }}>{resource.favorite ? '\u2B50' : '\u2606'}</span>
          <span style={{ cursor: 'pointer' }} onClick={(e) => { e.stopPropagation(); onPin?.(resource.id); }}>{resource.pinned ? '\ud83d\udccc' : '\ud83d\udccd'}</span>
        </div>
      </div>
      <div style={{ fontWeight: 700, marginTop: 10, fontSize: 'var(--font-size-sm)' }}>{resource.name}</div>
      <div style={{ color: 'var(--color-text-tertiary)', fontSize: 12, marginBottom: 6 }}>{RESOURCE_TYPE_LABELS[resource.type]} · v{resource.version}</div>
      <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap' }}>
        <Badge variant={STATUS_VARIANT[resource.status]} size="sm">{resource.status}</Badge>
        <Badge variant="neutral" size="sm">{resource.visibility}</Badge>
      </div>
      <div style={{ marginTop: 8, fontSize: 12, color: 'var(--color-text-tertiary)' }}>{resource.usageCount} uses · {resource.fileSize}</div>
    </Card>
  );
}
