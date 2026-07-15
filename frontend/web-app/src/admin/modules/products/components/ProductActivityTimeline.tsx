import React from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { MOCK_PRODUCT_ACTIVITY } from '../mock/mockProducts';
import type { ProductActivity, ProductActivityType } from '../types';

interface ProductActivityTimelineProps {
  activities?: ProductActivity[];
}

const activityIcon: Record<ProductActivityType, string> = {
  create: 'plus',
  update: 'settings',
  publish: 'upload',
  archive: 'archive',
  delete: 'alert-triangle',
  import: 'download',
  export: 'download',
  review: 'check-circle',
};

export const ProductActivityTimeline = React.memo(function ProductActivityTimeline({ activities = MOCK_PRODUCT_ACTIVITY }: ProductActivityTimelineProps) {
  return (
    <ul style={{ listStyle: 'none', margin: 0, padding: 0, display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-xs)' }}>
      {activities.map((activity) => (
        <li key={activity.id} style={{ display: 'flex', alignItems: 'flex-start', gap: 'var(--space-inline-xs)' }}>
          <span
            style={{
              display: 'inline-flex',
              alignItems: 'center',
              justifyContent: 'center',
              width: 28,
              height: 28,
              flexShrink: 0,
              borderRadius: 'var(--radius-full)',
              background: 'var(--color-primary-alpha)',
              color: 'var(--color-primary)',
            }}
          >
            <Icon name={activityIcon[activity.type]} size={14} />
          </span>
          <div style={{ minWidth: 0 }}>
            <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', margin: 0 }}>{activity.message}</p>
            <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', margin: '2px 0 0' }}>
              {activity.actor} · {new Date(activity.timestamp).toLocaleString()}
            </p>
          </div>
        </li>
      ))}
    </ul>
  );
});

export default ProductActivityTimeline;
