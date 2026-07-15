import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { MOCK_ORDERS } from './mockData';
import { OrderTimeline } from './OrderTimeline';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';
import { Toast } from '../../../design-system/components/feedback/Toast';
import { Grid } from '../../../design-system/components/layout/Grid';

export const OrderDetailsPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  const order = MOCK_ORDERS.find((o) => o.id === id);

  if (!order) {
    return (
      <div style={{ textAlign: 'center', padding: '48px 0' }}>
        <h2>Order not found</h2>
        <p>The requested order ID does not exist in your account history.</p>
        <button type="button" className="cw-btn cw-btn--primary" onClick={() => navigate('/dashboard/orders')}>
          Back to Orders
        </button>
      </div>
    );
  }

  const handleDownloadInvoice = () => {
    setToastMessage(`Downloading invoice for order #${order.id}...`);
    setTimeout(() => setToastMessage(null), 3000);
  };

  const handleDownloadReceipt = () => {
    setToastMessage(`Downloading official receipt for transaction ${order.transactionId}...`);
    setTimeout(() => setToastMessage(null), 3000);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      {/* Toast Notification */}
      {toastMessage && (
        <div style={{ position: 'fixed', bottom: '24px', right: '24px', zIndex: 1000 }}>
          <Toast message={toastMessage} tone="info" onClose={() => setToastMessage(null)} />
        </div>
      )}

      {/* Back button and page header action row */}
      <div 
        style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          flexWrap: 'wrap',
          gap: '12px',
        }}
      >
        <button
          type="button"
          onClick={() => navigate('/dashboard/orders')}
          style={{
            background: 'none',
            border: 'none',
            padding: 0,
            fontSize: 'var(--text-body-sm)',
            fontWeight: 'var(--weight-semibold)',
            color: 'var(--color-text-secondary)',
            cursor: 'pointer',
            display: 'flex',
            alignItems: 'center',
            gap: '8px',
          }}
        >
          <Icon name="arrow-left" size={16} color="currentColor" />
          Back to Orders
        </button>

        <div style={{ display: 'flex', gap: '12px' }}>
          <button 
            type="button" 
            className="cw-btn cw-btn--outlined cw-btn--sm"
            onClick={handleDownloadInvoice}
            style={{ display: 'flex', alignItems: 'center', gap: '6px' }}
          >
            <Icon name="download" size={14} color="currentColor" />
            Download Invoice
          </button>
          
          <button 
            type="button" 
            className="cw-btn cw-btn--primary cw-btn--sm"
            onClick={() => {
              setToastMessage(`Re-adding items from order #${order.id} to cart...`);
              setTimeout(() => {
                setToastMessage(null);
                navigate('/cart');
              }, 1200);
            }}
          >
            Reorder Items
          </button>
        </div>
      </div>

      {/* Page Title & Meta summary banner */}
      <div 
        style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'baseline',
          borderBottom: '1px solid var(--color-border-default)',
          paddingBottom: '12px',
          flexWrap: 'wrap',
          gap: '12px',
        }}
      >
        <div>
          <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
            Order details
          </h2>
          <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
            Placed on {order.date} · Transaction ID: <code style={{ color: 'var(--color-text-primary)' }}>{order.transactionId}</code>
          </p>
        </div>
        <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
          <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>Status:</span>
          <span style={{ 
            fontSize: 'var(--text-caption)', 
            fontWeight: 'var(--weight-bold)', 
            background: 'var(--color-bg-primary-weak)', 
            color: 'var(--color-bg-primary-default)', 
            padding: '2px 8px', 
            borderRadius: 'var(--radius-tag, 9999px)' 
          }}>{order.status}</span>
        </div>
      </div>

      {/* Main Grid Workspace */}
      <Grid columns="2fr 1fr" gap="24px" style={{ alignItems: 'start' }}>
        {/* Left column (larger) - timeline, items list, address fields */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          
          {/* Timeline Card */}
          <Card variant="outlined" padding="lg">
            <OrderTimeline milestones={order.milestones} status={order.status} />
          </Card>

          {/* Products Purchased list */}
          <Card variant="outlined" padding="lg">
            <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-md)' }}>
              Products Purchased ({order.items.reduce((acc, i) => acc + i.quantity, 0)})
            </h3>
            
            <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
              {order.items.map((item) => (
                <div 
                  key={item.id}
                  style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    gap: '16px',
                    borderBottom: order.items.length > 1 ? '1px solid var(--color-border-default)' : 'none',
                    paddingBottom: order.items.length > 1 ? '16px' : '0',
                  }}
                >
                  <div style={{ display: 'flex', gap: '16px', alignItems: 'center' }}>
                    <div 
                      style={{
                        width: '64px',
                        height: '64px',
                        background: 'var(--color-bg-primary-weak)',
                        fontSize: '28px',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        borderRadius: 'var(--radius-md)',
                        border: '1px solid var(--color-border-default)',
                        flexShrink: 0,
                      }}
                    >
                      {item.image}
                    </div>
                    <div>
                      <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: 0 }}>
                        {item.name}
                      </h4>
                      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block', marginTop: '2px' }}>
                        SKU: {item.sku}
                      </span>
                      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                        Qty: {item.quantity} · Price: ₹{item.price.toLocaleString('en-IN', { minimumFractionDigits: 2 })}
                      </span>
                    </div>
                  </div>
                  <strong style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>
                    ₹{(item.quantity * item.price).toLocaleString('en-IN', { minimumFractionDigits: 2 })}
                  </strong>
                </div>
              ))}
            </div>
          </Card>

          {/* Addresses Grid */}
          <Grid columns="1fr 1fr" gap="16px">
            <Card variant="outlined" padding="md">
              <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-sm)', display: 'flex', alignItems: 'center', gap: '8px' }}>
                <Icon name="map-pin" size={16} color="currentColor" />
                Shipping Address
              </h4>
              <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: 0, lineHeight: '1.5' }}>
                <strong>{order.shippingAddress.name}</strong><br />
                {order.shippingAddress.line1}<br />
                {order.shippingAddress.line2 && <>{order.shippingAddress.line2}<br /></>}
                {order.shippingAddress.city}, {order.shippingAddress.state} - {order.shippingAddress.postalCode}<br />
                {order.shippingAddress.country}<br />
                Phone: {order.shippingAddress.phone}
              </p>
            </Card>

            <Card variant="outlined" padding="md">
              <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-sm)', display: 'flex', alignItems: 'center', gap: '8px' }}>
                <Icon name="credit-card" size={16} color="currentColor" />
                Billing Address
              </h4>
              <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: 0, lineHeight: '1.5' }}>
                <strong>{order.billingAddress.name}</strong><br />
                {order.billingAddress.line1}<br />
                {order.billingAddress.line2 && <>{order.billingAddress.line2}<br /></>}
                {order.billingAddress.city}, {order.billingAddress.state} - {order.billingAddress.postalCode}<br />
                {order.billingAddress.country}<br />
                Phone: {order.billingAddress.phone}
              </p>
            </Card>
          </Grid>
        </div>

        {/* Right column (sidebar) - Invoice calculator, Tracking status, Refunds, Support */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          
          {/* Order Invoice calculator */}
          <Card variant="outlined" padding="md">
            <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-sm)', borderBottom: '1px solid var(--color-border-default)', paddingBottom: '8px' }}>
              Order summary
            </h3>
            
            <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                <span>Subtotal</span>
                <span style={{ color: 'var(--color-text-primary)' }}>₹{order.subtotal.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                <span>GST (Tax)</span>
                <span style={{ color: 'var(--color-text-primary)' }}>₹{order.tax.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                <span>Shipping Charges</span>
                <span style={{ color: 'var(--color-text-primary)' }}>₹{order.shipping.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</span>
              </div>
              {order.discount > 0 && (
                <div style={{ display: 'flex', justifyContent: 'space-between', color: 'var(--color-text-success, #166534)' }}>
                  <span>Discount ({order.couponCode})</span>
                  <span>-₹{order.discount.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</span>
                </div>
              )}
              <div 
                style={{ 
                  display: 'flex', 
                  justifyContent: 'space-between', 
                  color: 'var(--color-text-primary)', 
                  fontWeight: 'var(--weight-bold)',
                  fontSize: 'var(--text-body-lg)',
                  borderTop: '1px solid var(--color-border-default)',
                  paddingTop: '8px',
                  marginTop: '4px',
                }}
              >
                <span>Grand Total</span>
                <span>₹{order.total.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</span>
              </div>
            </div>
          </Card>

          {/* Courier tracking section */}
          {['Processing', 'Dispatched', 'In Transit', 'Out for Delivery'].includes(order.status) && (
            <Card variant="outlined" padding="md" style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
              <div>
                <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: 0 }}>
                  Fulfillment Status
                </h4>
                <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: '2px 0 0' }}>
                  Courier: <strong>{order.courierName}</strong>
                </p>
                <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: '2px 0 0' }}>
                  Tracking Number: <code style={{ color: 'var(--color-text-primary)' }}>{order.trackingNumber}</code>
                </p>
              </div>

              {order.trackingNumber !== 'PENDING' && (
                <button
                  type="button"
                  className="cw-btn cw-btn--primary cw-btn--sm"
                  onClick={() => navigate(`/dashboard/orders/${order.id}/track`)}
                  style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '6px' }}
                >
                  <Icon name="truck" size={14} color="currentColor" />
                  Track Live Shipment
                </button>
              )}
            </Card>
          )}

          {/* Payment summary details */}
          <Card variant="outlined" padding="md">
            <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-xs)' }}>
              Payment Details
            </h4>
            <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 0, lineHeight: '1.4' }}>
              Method: <strong>{order.paymentMethod}</strong><br />
              Status: <span style={{ color: 'var(--color-bg-primary-default)', fontWeight: 'var(--weight-bold)' }}>{order.paymentStatus}</span><br />
              Date: {order.paymentDate}
            </p>
            <button
              type="button"
              onClick={handleDownloadReceipt}
              style={{
                background: 'none',
                border: 'none',
                padding: 0,
                fontSize: 'var(--text-caption)',
                fontWeight: 'var(--weight-semibold)',
                color: 'var(--color-primary)',
                cursor: 'pointer',
                marginTop: '8px',
                display: 'flex',
                alignItems: 'center',
                gap: '4px',
              }}
            >
              <Icon name="download" size={12} color="currentColor" />
              Download Receipt
            </button>
          </Card>

          {/* Returns & Refunds card */}
          <Card variant="outlined" padding="md" style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
            <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: 0 }}>
              Returns & Refunds
            </h4>
            {order.status === 'Delivered' && order.returnEligible ? (
              <>
                <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 0 }}>
                  This order is eligible for return until 10 days from delivery.
                </p>
                <button
                  type="button"
                  className="cw-btn cw-btn--outlined cw-btn--sm"
                  onClick={() => navigate(`/dashboard/orders/${order.id}/refund`)}
                  style={{ marginTop: '4px' }}
                >
                  Initiate Return Request
                </button>
              </>
            ) : order.status === 'Refunded' || order.status === 'Returned' ? (
              <>
                <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 0 }}>
                  Return request details and active refund credit tracking.
                </p>
                <button
                  type="button"
                  className="cw-btn cw-btn--outlined cw-btn--sm"
                  onClick={() => navigate(`/dashboard/orders/${order.id}/refund`)}
                  style={{ marginTop: '4px' }}
                >
                  Track Refund Status
                </button>
              </>
            ) : (
              <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 0 }}>
                This order is not currently eligible for return or refund requests.
              </p>
            )}
          </Card>

          {/* Support help shortcuts */}
          <Card variant="outlined" padding="md">
            <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-xs)' }}>
              Need Help?
            </h4>
            <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: '0 0 12px' }}>
              Have questions about cultivars, shipping delay, or payment?
            </p>
            <a 
              href={`/dashboard/support?subject=Order%20Help%20${order.id}`}
              className="cw-btn cw-btn--outlined cw-btn--sm"
              style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '6px', textDecoration: 'none' }}
            >
              <Icon name="message-circle" size={14} color="currentColor" />
              Contact SporeCare
            </a>
          </Card>

        </div>
      </Grid>
    </div>
  );
};
export default OrderDetailsPage;
