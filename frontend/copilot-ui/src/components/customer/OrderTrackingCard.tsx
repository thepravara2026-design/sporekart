import React from 'react';
import { CustomerOrder } from '../../types/customer';

interface OrderTrackingCardProps {
  order: CustomerOrder;
}

const statusConfig: Record<string, { label: string; color: string; bg: string }> = {
  PENDING: { label: 'Pending', color: '#92400e', bg: '#fef3c7' },
  PROCESSING: { label: 'Processing', color: '#1e40af', bg: '#dbeafe' },
  SHIPPED: { label: 'Shipped', color: '#166534', bg: '#dcfce7' },
  DELIVERED: { label: 'Delivered', color: '#065f46', bg: '#d1fae5' },
  CANCELLED: { label: 'Cancelled', color: '#991b1b', bg: '#fee2e2' },
};

const statusOrder = ['PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED'];

function getCurrentStep(status: string): number {
  const idx = statusOrder.indexOf(status.toUpperCase());
  return idx >= 0 ? idx : 0;
}

export default function OrderTrackingCard({ order }: OrderTrackingCardProps) {
  const config = statusConfig[order.status.toUpperCase()] || statusConfig.PENDING;
  const currentStep = getCurrentStep(order.status);

  return (
    <div style={{
      border: '1px solid #e5e7eb',
      borderRadius: '8px',
      padding: '16px',
      background: '#fff',
      fontFamily: 'system-ui, sans-serif',
      maxWidth: '360px',
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
        <div>
          <span style={{ fontSize: '12px', color: '#6b7280' }}>Order</span>
          <p style={{ margin: '2px 0 0', fontWeight: 600, fontSize: '14px', color: '#111827' }}>
            {order.orderId}
          </p>
        </div>
        <span style={{
          background: config.bg,
          color: config.color,
          padding: '4px 10px',
          borderRadius: '12px',
          fontSize: '12px',
          fontWeight: 600,
        }}>
          {config.label}
        </span>
      </div>

      <div style={{ position: 'relative', margin: '16px 0' }}>
        {statusOrder.map((status, idx) => {
          const cfg = statusConfig[status];
          const isActive = idx <= currentStep;
          const isLast = idx === statusOrder.length - 1;

          return (
            <div key={status} style={{ display: 'flex', alignItems: 'flex-start', marginBottom: isLast ? '0' : '0' }}>
              <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', marginRight: '10px' }}>
                <div style={{
                  width: '20px',
                  height: '20px',
                  borderRadius: '50%',
                  background: isActive ? '#2563eb' : '#d1d5db',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  color: '#fff',
                  fontSize: '10px',
                  fontWeight: 700,
                  flexShrink: 0,
                }}>
                  {isActive ? '✓' : idx + 1}
                </div>
                {!isLast && (
                  <div style={{
                    width: '2px',
                    height: '24px',
                    background: isActive && idx < currentStep ? '#2563eb' : '#d1d5db',
                  }} />
                )}
              </div>
              <div style={{ paddingBottom: isLast ? '0' : '12px' }}>
                <span style={{
                  fontSize: '13px',
                  fontWeight: isActive ? 600 : 400,
                  color: isActive ? '#111827' : '#9ca3af',
                }}>
                  {cfg.label}
                </span>
              </div>
            </div>
          );
        })}
      </div>

      <div style={{ borderTop: '1px solid #f3f4f6', paddingTop: '12px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '4px' }}>
          <span style={{ fontSize: '12px', color: '#6b7280' }}>Total</span>
          <span style={{ fontSize: '14px', fontWeight: 700, color: '#111827' }}>
            {order.currency} {order.totalAmount.toFixed(2)}
          </span>
        </div>
        {order.estimatedDelivery && (
          <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '4px' }}>
            <span style={{ fontSize: '12px', color: '#6b7280' }}>Est. Delivery</span>
            <span style={{ fontSize: '13px', color: '#374151' }}>
              {new Date(order.estimatedDelivery).toLocaleDateString()}
            </span>
          </div>
        )}
        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
          <span style={{ fontSize: '12px', color: '#6b7280' }}>Items</span>
          <span style={{ fontSize: '13px', color: '#374151' }}>{order.items.length}</span>
        </div>
      </div>

      {order.trackingUrl && (
        <a
          href={order.trackingUrl}
          target="_blank"
          rel="noopener noreferrer"
          style={{
            display: 'block',
            textAlign: 'center',
            marginTop: '12px',
            padding: '8px',
            background: '#2563eb',
            color: '#fff',
            borderRadius: '6px',
            fontWeight: 600,
            fontSize: '13px',
            textDecoration: 'none',
          }}
        >
          Track Package
        </a>
      )}
    </div>
  );
}
