import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import type { DeliveryChannel } from '../data/communicationTypes';
import { CHANNEL_LABELS } from '../data/communicationTypes';
import { CHANNEL_ICON } from '../data/communicationOptions';

export interface ChannelChipProps {
  channel: DeliveryChannel;
  future?: boolean;
}

const ChannelChip = memo(function ChannelChip({ channel, future = false }: ChannelChipProps) {
  return (
    <span
      title={future ? `${CHANNEL_LABELS[channel]} (future integration)` : CHANNEL_LABELS[channel]}
      style={{
        display: 'inline-flex',
        alignItems: 'center',
        gap: 'var(--space-1)',
        padding: '2px 8px',
        borderRadius: 'var(--radius-sm)',
        fontSize: 'var(--text-caption)',
        fontWeight: 500,
        color: future ? 'var(--color-text-muted)' : 'var(--color-text-secondary)',
        background: 'var(--color-bg-surface-muted)',
        border: `1px dashed ${future ? 'var(--color-border-default)' : 'transparent'}`,
        opacity: future ? 0.85 : 1,
        whiteSpace: 'nowrap',
      }}
    >
      <Icon name={CHANNEL_ICON[channel]} size={14} />
      {CHANNEL_LABELS[channel]}
    </span>
  );
});

export default ChannelChip;
