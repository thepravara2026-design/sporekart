import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import type { IntegrationProvider } from '../data/communicationTypes';

export interface FutureChannelCardProps {
  provider: IntegrationProvider;
  icon: string;
}

const FutureChannelCard = memo(function FutureChannelCard({ provider, icon }: FutureChannelCardProps) {
  return (
    <article
      aria-label={`${provider.name} (planned integration)`}
      style={{
        display: 'flex', flexDirection: 'column', gap: 'var(--space-3)',
        padding: 'var(--space-4)',
        background: 'var(--color-bg-surface-muted)',
        border: '1px dashed var(--color-border-default)',
        borderRadius: 'var(--radius-lg)',
      }}
    >
      <header style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 'var(--space-2)' }}>
        <span
          aria-hidden
          style={{
            display: 'inline-flex', alignItems: 'center', justifyContent: 'center',
            width: 40, height: 40, borderRadius: 'var(--radius-md)',
            background: 'var(--color-bg-surface-raised)', color: 'var(--color-text-muted)',
          }}
        >
          <Icon name={icon} size={20} />
        </span>
        <span
          style={{
            display: 'inline-flex', alignItems: 'center', gap: 'var(--space-1)',
            fontSize: 'var(--text-caption)', fontWeight: 600, color: 'var(--color-text-muted)',
            padding: '2px 8px', borderRadius: 'var(--radius-sm)', background: 'var(--color-bg-surface-raised)',
          }}
        >
          <Icon name="clock" size={12} /> Planned
        </span>
      </header>
      <div>
        <h3 style={{ margin: 0, fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)' }}>
          {provider.name}
        </h3>
        <p style={{ margin: '4px 0 0', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 1.5 }}>
          {provider.description}
        </p>
      </div>
      <button
        type="button"
        disabled
        aria-disabled
        style={{
          alignSelf: 'flex-start', padding: '6px 12px', borderRadius: 'var(--radius-sm)',
          border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)',
          color: 'var(--color-text-disabled)', fontSize: 'var(--text-body-sm)', cursor: 'not-allowed',
        }}
      >
        Configure (unavailable)
      </button>
    </article>
  );
});

export default FutureChannelCard;
