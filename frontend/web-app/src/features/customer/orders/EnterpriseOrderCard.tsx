import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';
import type { Order } from './mockData';

interface EnterpriseOrderCardProps {
  order: Order;
  onDownloadInvoice?: (orderId: string) => void;
}

export const EnterpriseOrderCard: React.FC<EnterpriseOrderCardProps> = ({ 
  order, 
  onDownloadInvoice 
}) => {
  const navigate = useNavigate();

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'Delivered':
        return { bg: 'var(--color-bg-success-weak, #f0fdf4)', text: 'var(--color-text-success, #166534)', border: '#bcf0da' };
      case 'Processing':
        return { bg: 'var(--color-bg-info-weak, #eff6ff)', text: 'var(--color-text-info, #1e40af)', border: '#bfdbfe' };
      case 'Dispatched':
      case 'In Transit':
      case 'Out for Delivery':
        return { bg: 'var(--color-bg-primary-weak, #f4f6f0)', text: 'var(--color-bg-primary-default, #4b6319)', border: '#d9e0ce' };
      case 'Cancelled':
        return { bg: 'var(--color-bg-danger-weak, #fef2f2)', text: 'var(--color-text-danger, #991b1b)', border: '#fecaca' };
      case 'Returned':
      case 'Refunded':
        return { bg: 'var(--color-bg-warning-weak, #fffbeb)', text: 'var(--color-text-warning, #92400e)', border: '#fef3c7' };
      default:
        return { bg: 'var(--color-bg-surface-default)', text: 'var(--color-text-primary)', border: 'var(--color-border-default)' };
    }
  };

  const colors = getStatusColor(order.status);

  return (
    <Card 
      variant="outlined" 
      padding="lg"
      style={{
        display: 'flex',
        flexDirection: 'column',
        gap: 'var(--space-stack-md)',
        transition: 'all 0.2s ease',
      }}
    >
      {/* Top Header Row */}
      <div 
        className="sk-order-card__header"
        style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          flexWrap: 'wrap',
          gap: '12px',
          borderBottom: '1px solid var(--color-border-default)',
          paddingBottom: 'var(--space-stack-sm)',
        }}
      >
        <div style={{ display: 'flex', gap: '24px', flexWrap: 'wrap' }}>
          <div>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block' }}>ORDER PLACED</span>
            <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>{order.date}</span>
          </div>
          <div>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block' }}>TOTAL</span>
            <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>₹{order.total.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</span>
          </div>
          <div>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block' }}>SHIP TO</span>
            <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>{order.shippingAddress.name}</span>
          </div>
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: '4px' }}>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>ORDER # {order.id}</span>
          <span 
            style={{
              fontSize: 'var(--text-caption)',
              fontWeight: 'var(--weight-bold)',
              padding: '4px 10px',
              borderRadius: 'var(--radius-tag, 9999px)',
              background: colors.bg,
              color: colors.text,
              border: `1px solid ${colors.border}`,
              display: 'inline-block',
            }}
          >
            {order.status}
          </span>
        </div>
      </div>

      {/* Main Body with Items list */}
      <div 
        className="sk-order-card__body"
        style={{
          display: 'flex',
          flexDirection: 'column',
          gap: 'var(--space-stack-md)',
        }}
      >
        <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          {order.items.map((item) => (
            <div 
              key={item.id}
              style={{
                display: 'flex',
                gap: '16px',
                alignItems: 'center',
              }}
            >
              {/* Product Thumbnail Placeholder */}
              <div 
                style={{
                  width: '56px',
                  height: '56px',
                  borderRadius: 'var(--radius-md)',
                  background: 'var(--color-bg-primary-weak)',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  fontSize: '24px',
                  flexShrink: 0,
                  border: '1px solid var(--color-border-default)',
                }}
              >
                {item.image}
              </div>

              {/* Product details */}
              <div style={{ flex: 1, minWidth: 0 }}>
                <h4 
                  style={{
                    fontSize: 'var(--text-body-sm)',
                    fontWeight: 'var(--weight-semibold)',
                    color: 'var(--color-text-primary)',
                    margin: 0,
                    whiteSpace: 'nowrap',
                    overflow: 'hidden',
                    textOverflow: 'ellipsis',
                  }}
                >
                  {item.name}
                </h4>
                <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: '2px 0 0' }}>
                  Qty: {item.quantity} · Price: ₹{item.price.toLocaleString('en-IN', { minimumFractionDigits: 2 })}
                </p>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block', marginTop: '2px' }}>
                  SKU: {item.sku}
                </span>
              </div>
            </div>
          ))}
        </div>

        {/* Shipping details */}
        <div 
          style={{
            background: 'var(--color-bg-surface-secondary, #fafafa)',
            borderRadius: 'var(--radius-md)',
            padding: '12px 16px',
            fontSize: 'var(--text-body-sm)',
            color: 'var(--color-text-secondary)',
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            flexWrap: 'wrap',
            gap: '8px',
          }}
        >
          <div>
            <Icon name="truck" size={16} color="currentColor" style={{ verticalAlign: 'middle', marginRight: '8px' }} />
            {order.status === 'Delivered' ? (
              <span>Delivered on {order.estimatedDelivery}</span>
            ) : (
              <span>Estimated Delivery: <strong style={{ color: 'var(--color-text-primary)' }}>{order.estimatedDelivery}</strong> via {order.courierName}</span>
            )}
          </div>
          <div>
            <span style={{ fontSize: 'var(--text-caption)' }}>
              Payment Method: <strong>{order.paymentMethod}</strong> ({order.paymentStatus})
            </span>
          </div>
        </div>
      </div>

      {/* Action Footer */}
      <div 
        className="sk-order-card__actions"
        style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          flexWrap: 'wrap',
          gap: '12px',
          paddingTop: 'var(--space-stack-sm)',
          borderTop: '1px solid var(--color-border-default)',
        }}
      >
        <div style={{ display: 'flex', gap: '8px' }}>
          <button 
            type="button"
            className="cw-btn cw-btn--primary cw-btn--sm"
            onClick={() => navigate(`/dashboard/orders/${order.id}`)}
          >
            View Details
          </button>
          
          {['In Transit', 'Dispatched', 'Out for Delivery'].includes(order.status) && (
            <button 
              type="button"
              className="cw-btn cw-btn--outlined cw-btn--sm"
              onClick={() => navigate(`/dashboard/orders/${order.id}/track`)}
            >
              Track Order
            </button>
          )}

          {order.status === 'Delivered' && order.returnEligible && (
            <button 
              type="button"
              className="cw-btn cw-btn--outlined cw-btn--sm"
              onClick={() => navigate(`/dashboard/orders/${order.id}/refund`)}
            >
              Return / Refund
            </button>
          )}
        </div>

        <div style={{ display: 'flex', gap: '16px', alignItems: 'center' }}>
          {onDownloadInvoice && (
            <button 
              type="button"
              onClick={() => onDownloadInvoice(order.id)}
              style={{
                background: 'none',
                border: 'none',
                padding: 0,
                fontSize: 'var(--text-body-sm)',
                fontWeight: 'var(--weight-semibold)',
                color: 'var(--color-primary, #4b6319)',
                cursor: 'pointer',
                display: 'flex',
                alignItems: 'center',
                gap: '4px',
              }}
            >
              <Icon name="download" size={14} color="currentColor" />
              Invoice
            </button>
          )}

          <a 
            href={`/dashboard/support?subject=Order%20Help%20${order.id}`}
            style={{
              fontSize: 'var(--text-body-sm)',
              fontWeight: 'var(--weight-semibold)',
              color: 'var(--color-text-secondary)',
              textDecoration: 'none',
              display: 'flex',
              alignItems: 'center',
              gap: '4px',
            }}
          >
            <Icon name="help-circle" size={14} color="currentColor" />
            Support
          </a>
        </div>
      </div>
    </Card>
  );
};
export default EnterpriseOrderCard;
