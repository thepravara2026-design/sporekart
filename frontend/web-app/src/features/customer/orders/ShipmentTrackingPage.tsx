import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { MOCK_ORDERS } from './mockData';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';
import { Grid } from '../../../design-system/components/layout/Grid';

export const ShipmentTrackingPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const order = MOCK_ORDERS.find((o) => o.id === id);

  if (!order) {
    return (
      <div style={{ textAlign: 'center', padding: '48px 0' }}>
        <h2>Order not found</h2>
        <button type="button" className="cw-btn cw-btn--primary" onClick={() => navigate('/dashboard/orders')}>
          Back to Orders
        </button>
      </div>
    );
  }

  // Filter completed tracking milestones
  const trackingMilestones = order.milestones.filter(m => m.status !== 'Order Created' && m.status !== 'Payment Received');

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      {/* Header back button */}
      <div>
        <button
          type="button"
          onClick={() => navigate(`/dashboard/orders/${order.id}`)}
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
          Back to Details
        </button>
      </div>

      {/* Page Title */}
      <div>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
          Shipment tracking
        </h2>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
          Tracking details for shipment associated with order <strong>#{order.id}</strong>
        </p>
      </div>

      {/* Main Grid: Map and Tracking log */}
      <Grid columns="2fr 1.2fr" gap="24px" style={{ alignItems: 'start' }}>
        {/* Map view (left) */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          <Card variant="outlined" padding="lg">
            <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-md)' }}>
              Transit Map Route
            </h3>
            
            {/* SVG Interactive Map Mock */}
            <div 
              style={{
                width: '100%',
                height: '350px',
                background: '#f4f6f8',
                borderRadius: 'var(--radius-md)',
                border: '1px solid var(--color-border-default)',
                position: 'relative',
                overflow: 'hidden',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
              }}
            >
              <svg 
                width="100%" 
                height="100%" 
                viewBox="0 0 500 350" 
                style={{ position: 'absolute', top: 0, left: 0 }}
              >
                {/* Grid Gridlines for technical look */}
                <defs>
                  <pattern id="grid" width="20" height="20" patternUnits="userSpaceOnUse">
                    <path d="M 20 0 L 0 0 0 20" fill="none" stroke="#eef1f4" strokeWidth="1" />
                  </pattern>
                </defs>
                <rect width="100%" height="100%" fill="url(#grid)" />

                {/* Stylized Abstract Outline of India Map */}
                <path 
                  d="M 250 30 Q 230 50 200 60 T 170 120 T 160 180 Q 150 210 180 240 T 210 320 L 220 330 L 230 300 Q 240 280 260 250 T 320 200 T 350 120 T 310 70 Q 280 40 250 30 Z" 
                  fill="#e5e9ec" 
                  stroke="#cbd5e1" 
                  strokeWidth="1.5" 
                />

                {/* Warehouse Location (Bengaluru Hub) */}
                <circle cx="220" cy="270" r="6" fill="var(--color-bg-primary-default)" />
                <circle cx="220" cy="270" r="12" fill="none" stroke="var(--color-bg-primary-default)" strokeWidth="1.5" opacity="0.5">
                  <animate attributeName="r" values="6;16;6" dur="3s" repeatCount="indefinite" />
                  <animate attributeName="opacity" values="0.8;0;0.8" dur="3s" repeatCount="indefinite" />
                </circle>

                {/* Destination Location (Jane's House - HSR Layout) */}
                <circle cx="230" cy="245" r="6" fill="#1e40af" />
                <circle cx="230" cy="245" r="12" fill="none" stroke="#1e40af" strokeWidth="1.5" opacity="0.5">
                  <animate attributeName="r" values="6;16;6" dur="2s" repeatCount="indefinite" />
                  <animate attributeName="opacity" values="0.8;0;0.8" dur="2s" repeatCount="indefinite" />
                </circle>

                {/* Route connecting line */}
                <path 
                  d="M 220 270 Q 223 258 230 245" 
                  fill="none" 
                  stroke="var(--color-bg-primary-default)" 
                  strokeWidth="3" 
                  strokeDasharray="4 4"
                >
                  <animate attributeName="stroke-dashoffset" values="20;0" dur="1.5s" repeatCount="indefinite" />
                </path>

                {/* Map labels */}
                <text x="210" y="290" fill="var(--color-bg-primary-default)" fontSize="10" fontWeight="bold" fontFamily="sans-serif">
                  SporeHub (Bengaluru)
                </text>
                <text x="242" y="248" fill="#1e40af" fontSize="10" fontWeight="bold" fontFamily="sans-serif">
                  HSR Layout (Jane)
                </text>
              </svg>

              {/* Float Card Overlay */}
              <div 
                style={{
                  position: 'absolute',
                  top: '16px',
                  left: '16px',
                  background: 'rgba(255, 255, 255, 0.95)',
                  backdropFilter: 'blur(4px)',
                  padding: '8px 12px',
                  borderRadius: 'var(--radius-sm)',
                  boxShadow: 'var(--shadow-1)',
                  fontSize: 'var(--text-caption)',
                  border: '1px solid var(--color-border-default)',
                  pointerEvents: 'none',
                }}
              >
                <span style={{ fontWeight: 'bold', color: 'var(--color-text-primary)', display: 'block' }}>In Transit</span>
                <span style={{ color: 'var(--color-text-secondary)' }}>Bengaluru sorting hub &rarr; Hub</span>
              </div>
            </div>
          </Card>

          {/* Delivery milestones timeline */}
          <Card variant="outlined" padding="lg">
            <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-md)' }}>
              Scan History & Transit Logs
            </h3>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
              {trackingMilestones.map((m, i) => {
                const isFirst = i === 0;
                return (
                  <div key={i} style={{ display: 'flex', gap: '16px', opacity: m.completed ? 1 : 0.4 }}>
                    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                      <div 
                        style={{
                          width: '24px',
                          height: '24px',
                          borderRadius: '50%',
                          background: m.completed ? 'var(--color-bg-primary-default)' : 'var(--color-bg-surface-default)',
                          border: `2px solid ${m.completed ? 'transparent' : 'var(--color-border-default)'}`,
                          color: '#fff',
                          display: 'flex',
                          alignItems: 'center',
                          justifyContent: 'center',
                          fontSize: '12px',
                          fontWeight: 'bold',
                        }}
                      >
                        {m.completed ? '✓' : ''}
                      </div>
                      {i < trackingMilestones.length - 1 && (
                        <div style={{ width: '2px', flexGrow: 1, background: 'var(--color-border-default)', margin: '4px 0' }} />
                      )}
                    </div>
                    <div>
                      <div style={{ display: 'flex', gap: '12px', alignItems: 'baseline' }}>
                        <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: isFirst ? 'var(--weight-bold)' : 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: 0 }}>
                          {m.status}
                        </h4>
                        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                          {m.timestamp}
                        </span>
                      </div>
                      <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: '2px 0 0' }}>
                        {m.detail}
                      </p>
                    </div>
                  </div>
                );
              })}
            </div>
          </Card>
        </div>

        {/* Courier details & estimates (right sidebar) */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          
          <Card variant="outlined" padding="md">
            <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-sm)' }}>
              Shipment Information
            </h4>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '12px', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
              <div>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block' }}>COURIER PARTNER</span>
                <strong style={{ color: 'var(--color-text-primary)' }}>{order.courierName}</strong>
              </div>
              <div>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block' }}>TRACKING NUMBER</span>
                <strong style={{ color: 'var(--color-text-primary)' }}>{order.trackingNumber}</strong>
              </div>
              <div>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block' }}>ESTIMATED DELIVERY</span>
                <strong style={{ color: 'var(--color-bg-primary-default)', fontSize: 'var(--text-body-lg)' }}>{order.estimatedDelivery}</strong>
              </div>
            </div>
          </Card>

          <Card variant="outlined" padding="md">
            <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-sm)' }}>
              Delivery Destination
            </h4>
            <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 0, lineHeight: '1.5' }}>
              <strong>{order.shippingAddress.name}</strong><br />
              {order.shippingAddress.line1}<br />
              {order.shippingAddress.line2 && <>{order.shippingAddress.line2}<br /></>}
              {order.shippingAddress.city}, {order.shippingAddress.state} - {order.shippingAddress.postalCode}
            </p>
          </Card>

          <Card variant="outlined" padding="md">
            <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-xs)' }}>
              Fulfillment Partner
            </h4>
            <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 0, lineHeight: '1.4' }}>
              This package is dispatched through <strong>Delhivery Enterprise Service</strong>. If you experience delays or require custom routing, please trigger a support query below.
            </p>
            <a 
              href={`/dashboard/support?subject=Shipment%20Issue%20${order.id}`}
              className="cw-btn cw-btn--outlined cw-btn--sm"
              style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '6px', textDecoration: 'none', marginTop: '12px' }}
            >
              <Icon name="help-circle" size={14} color="currentColor" />
              Help with Delivery
            </a>
          </Card>
        </div>
      </Grid>
    </div>
  );
};
export default ShipmentTrackingPage;
