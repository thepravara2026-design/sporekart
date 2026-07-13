import React from 'react';
import { Card } from './Card';

export interface OrderCardProps {
  orderId: string;
  status: string;
  date: string;
  total: string | number;
  items?: number;
  customer?: string;
  loading?: boolean;
  error?: string;
}

export const OrderCard: React.FC<OrderCardProps> = ({
  orderId,
  status,
  date,
  total,
  items,
  customer,
  loading = false,
  error,
}) => {
  return (
    <Card variant="outlined" padding="md" loading={loading} error={error}>
      <div
        className="sk-ordercard__header"
        style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          marginBottom: 'var(--space-stack-sm)',
        }}
      >
        <span
          className="sk-ordercard__order-id"
          style={{
            fontSize: 'var(--text-body-sm)',
            fontWeight: 'var(--weight-semibold)',
            color: 'var(--color-text-primary)',
          }}
        >
          #{orderId}
        </span>
        <span
          className="sk-ordercard__status"
          style={{
            fontSize: 'var(--text-caption)',
            fontWeight: 'var(--weight-medium)',
            padding: '2px var(--space-2)',
            borderRadius: 'var(--radius-tag)',
            background: 'var(--color-bg-primary-weak)',
            color: 'var(--color-bg-primary-default)',
          }}
        >
          {status}
        </span>
      </div>
      <div
        className="sk-ordercard__info"
        style={{
          display: 'flex',
          flexDirection: 'column',
          gap: 'var(--space-stack-xs)',
          fontSize: 'var(--text-body-sm)',
          color: 'var(--color-text-secondary)',
        }}
      >
        <div
          className="sk-ordercard__row"
          style={{ display: 'flex', justifyContent: 'space-between' }}
        >
          <span>Date</span>
          <span style={{ color: 'var(--color-text-primary)' }}>{date}</span>
        </div>
        <div
          className="sk-ordercard__row"
          style={{ display: 'flex', justifyContent: 'space-between' }}
        >
          <span>Total</span>
          <span
            style={{
              color: 'var(--color-text-primary)',
              fontWeight: 'var(--weight-semibold)',
            }}
          >
            {total}
          </span>
        </div>
        {items != null && (
          <div
            className="sk-ordercard__row"
            style={{ display: 'flex', justifyContent: 'space-between' }}
          >
            <span>Items</span>
            <span style={{ color: 'var(--color-text-primary)' }}>{items}</span>
          </div>
        )}
        {customer && (
          <div
            className="sk-ordercard__row"
            style={{ display: 'flex', justifyContent: 'space-between' }}
          >
            <span>Customer</span>
            <span style={{ color: 'var(--color-text-primary)' }}>{customer}</span>
          </div>
        )}
      </div>
    </Card>
  );
};

OrderCard.displayName = 'OrderCard';

export default OrderCard;
