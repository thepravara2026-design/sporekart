import React from 'react';
import { Card } from './Card';

export interface SummaryCardProps {
  title: string;
  items: { label: string; value: string | number; highlight?: boolean }[];
  total?: string | number;
  actions?: React.ReactNode;
  loading?: boolean;
}

export const SummaryCard: React.FC<SummaryCardProps> = ({
  title,
  items,
  total,
  actions,
  loading = false,
}) => {
  return (
    <Card variant="default" padding="md" loading={loading}>
      <h3
        className="sk-summarycard__title"
        style={{
          fontSize: 'var(--text-body)',
          fontWeight: 'var(--weight-semibold)',
          color: 'var(--color-text-primary)',
          margin: '0 0 var(--space-stack-sm) 0',
          paddingBottom: 'var(--space-stack-xs)',
          borderBottom: 'var(--border-width-thin) solid var(--color-border-default)',
        }}
      >
        {title}
      </h3>
      <div
        className="sk-summarycard__items"
        style={{
          display: 'flex',
          flexDirection: 'column',
          gap: 'var(--space-stack-xs)',
          marginBottom: total != null ? 'var(--space-stack-sm)' : 0,
        }}
      >
        {items.map((item, idx) => (
          <div
            key={idx}
            className="sk-summarycard__item"
            style={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              fontSize: item.highlight ? 'var(--text-body)' : 'var(--text-body-sm)',
              fontWeight: item.highlight ? 'var(--weight-semibold)' : 'var(--weight-normal)',
              color: item.highlight
                ? 'var(--color-text-primary)'
                : 'var(--color-text-secondary)',
            }}
          >
            <span className="sk-summarycard__label">{item.label}</span>
            <span className="sk-summarycard__value">{item.value}</span>
          </div>
        ))}
      </div>
      {total != null && (
        <div
          className="sk-summarycard__total"
          style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            paddingTop: 'var(--space-stack-xs)',
            borderTop: 'var(--border-width-thin) solid var(--color-border-default)',
            fontSize: 'var(--text-h5)',
            fontWeight: 'var(--weight-bold)',
            color: 'var(--color-text-primary)',
          }}
        >
          <span>Total</span>
          <span>{total}</span>
        </div>
      )}
      {actions && (
        <div
          className="sk-summarycard__actions"
          style={{
            display: 'flex',
            gap: 'var(--space-inline-sm)',
            marginTop: 'var(--space-stack-sm)',
            paddingTop: 'var(--space-stack-sm)',
            borderTop: 'var(--border-width-thin) solid var(--color-border-default)',
          }}
        >
          {actions}
        </div>
      )}
    </Card>
  );
};

SummaryCard.displayName = 'SummaryCard';

export default SummaryCard;
