import { memo } from 'react';
import type { CommunicationMessage } from '../types';
import { TypeBadge } from './TypeBadge';
import { PriorityBadge } from './PriorityBadge';
import { StatusBadge } from './StatusBadge';

export const MessageCard = memo(function MessageCard({ message }: { message: CommunicationMessage }) {
  return (
    <div style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', gap: 8 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: 4 }}>
        <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap' }}>
          <TypeBadge type={message.type} />
          <PriorityBadge priority={message.priority} />
        </div>
        <StatusBadge status={message.deliveryStatus} />
      </div>
      <h4 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{message.title}</h4>
      {message.subtitle && <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)', margin: 0 }}>{message.subtitle}</p>}
      <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: 0, lineHeight: 1.5 }}>{message.description}</p>
      <div style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap', gap: 4, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        <span>{message.studentName} • {message.courseName}</span>
        <span>{new Date(message.createdDate).toLocaleDateString()}</span>
      </div>
    </div>
  );
});
