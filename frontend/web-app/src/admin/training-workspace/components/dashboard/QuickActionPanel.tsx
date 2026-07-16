import { memo, useCallback } from 'react';
import { Card } from '../../../../design-system/components/composite/Card';
import { Icon } from '../../../../design-system/icons/Icon';
import type { QuickAction } from '../../data/mockData';

interface QuickActionPanelProps {
  actions: QuickAction[];
}

export const QuickActionPanel = memo(function QuickActionPanel({
  actions,
}: QuickActionPanelProps) {
  const handleAction = useCallback((_label: string) => {
    // Placeholder — future implementation
  }, []);

  return (
    <Card variant="default" padding="md" as="div">
      <h3 style={{
        margin: '0 0 var(--space-stack-sm)',
        fontSize: 'var(--text-h4)',
        fontWeight: 'var(--weight-semibold)',
        color: 'var(--color-text-primary)',
      }}>
        Quick Actions
      </h3>
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))',
        gap: 'var(--space-inline-sm)',
      }}>
        {actions.map((action) => (
          <button
            key={action.id}
            type="button"
            onClick={() => handleAction(action.label)}
            style={{
              display: 'flex', alignItems: 'center', gap: 'var(--space-inline-sm)',
              padding: 'var(--space-3)',
              background: 'var(--color-bg-background)',
              border: '1px solid var(--color-border-default)',
              borderRadius: 'var(--radius-sm)',
              cursor: 'pointer',
              color: 'var(--color-text-primary)',
              fontSize: 'var(--text-body-sm)',
              textAlign: 'left',
              transition: 'all var(--duration-fast) var(--easing-standard)',
            }}
            aria-label={`Quick action: ${action.label}`}
          >
            <span style={{
              width: 32, height: 32, borderRadius: 'var(--radius-sm)',
              background: 'var(--color-bg-primary-subtle)',
              color: 'var(--color-primary)',
              display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0,
            }}>
              <Icon name={action.icon} size={16} color="currentColor" />
            </span>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 2, minWidth: 0 }}>
              <span style={{ fontWeight: 'var(--weight-medium)', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                {action.label}
              </span>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                {action.description}
              </span>
            </div>
          </button>
        ))}
      </div>
    </Card>
  );
});
