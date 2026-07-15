import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { MOCK_ORDERS } from './mockData';
import EnterpriseOrderCard from './EnterpriseOrderCard';
import { Grid } from '../../../design-system/components/layout/Grid';
import { Icon } from '../../../design-system/icons/Icon';
import { Toast } from '../../../design-system/components/feedback/Toast';
import { ShimmerLoader } from '../../../design-system/components/feedback/ShimmerLoader';
import { Card } from '../../../design-system/components/composite/Card';

export const OrdersDashboard: React.FC = () => {
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState('');
  const [activeTab, setActiveTab] = useState<'All' | 'Active' | 'Completed' | 'Refunded'>('All');
  const [loading, setLoading] = useState(true);
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  useEffect(() => {
    // Mock loading delay to demonstrate premium shimmer transition
    const timer = setTimeout(() => {
      setLoading(false);
    }, 800);
    return () => clearTimeout(timer);
  }, []);

  const handleDownloadInvoice = (orderId: string) => {
    setToastMessage(`Downloading invoice for order #${orderId}...`);
    setTimeout(() => {
      setToastMessage(null);
    }, 3000);
  };

  const filteredOrders = MOCK_ORDERS.filter((order) => {
    // Search filter
    const matchesSearch = 
      order.id.toLowerCase().includes(searchTerm.toLowerCase()) ||
      order.items.some(item => item.name.toLowerCase().includes(searchTerm.toLowerCase()));
    
    if (!matchesSearch) return false;

    // Tab filter
    if (activeTab === 'Active') {
      return ['Processing', 'Dispatched', 'In Transit', 'Out for Delivery'].includes(order.status);
    }
    if (activeTab === 'Completed') {
      return order.status === 'Delivered';
    }
    if (activeTab === 'Refunded') {
      return ['Returned', 'Refunded'].includes(order.status);
    }
    return true; // All
  });

  // Calculate statistics
  const totalSpend = MOCK_ORDERS.reduce((acc, o) => o.status !== 'Cancelled' ? acc + o.total : acc, 0);
  const activeCount = MOCK_ORDERS.filter(o => ['Processing', 'Dispatched', 'In Transit', 'Out for Delivery'].includes(o.status)).length;
  const completedCount = MOCK_ORDERS.filter(o => o.status === 'Delivered').length;
  const refundCount = MOCK_ORDERS.filter(o => ['Returned', 'Refunded'].includes(o.status)).length;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      {/* Toast Notification */}
      {toastMessage && (
        <div style={{ position: 'fixed', bottom: '24px', right: '24px', zIndex: 1000 }}>
          <Toast message={toastMessage} tone="info" onClose={() => setToastMessage(null)} />
        </div>
      )}

      {/* Aggregate Stats Cards */}
      <section aria-label="Order statistics">
        <Grid columns="repeat(auto-fit, minmax(220px, 1fr))" gap="16px">
          <Card variant="default" padding="md" style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
            <div style={{ padding: '12px', borderRadius: 'var(--radius-md)', background: 'var(--color-bg-primary-weak)', color: 'var(--color-bg-primary-default)', display: 'flex' }}>
              <Icon name="shopping-bag" size={24} color="currentColor" />
            </div>
            <div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block' }}>Total Spend</span>
              <strong style={{ fontSize: 'var(--text-h4)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)' }}>₹{totalSpend.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</strong>
            </div>
          </Card>
          
          <Card variant="default" padding="md" style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
            <div style={{ padding: '12px', borderRadius: 'var(--radius-md)', background: 'var(--color-bg-info-weak)', color: 'var(--color-text-info)', display: 'flex' }}>
              <Icon name="truck" size={24} color="currentColor" />
            </div>
            <div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block' }}>Active Orders</span>
              <strong style={{ fontSize: 'var(--text-h4)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)' }}>{activeCount}</strong>
            </div>
          </Card>

          <Card variant="default" padding="md" style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
            <div style={{ padding: '12px', borderRadius: 'var(--radius-md)', background: 'var(--color-bg-success-weak)', color: 'var(--color-text-success)', display: 'flex' }}>
              <Icon name="check-circle" size={24} color="currentColor" />
            </div>
            <div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block' }}>Delivered</span>
              <strong style={{ fontSize: 'var(--text-h4)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)' }}>{completedCount}</strong>
            </div>
          </Card>

          <Card variant="default" padding="md" style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
            <div style={{ padding: '12px', borderRadius: 'var(--radius-md)', background: 'var(--color-bg-warning-weak)', color: 'var(--color-text-warning)', display: 'flex' }}>
              <Icon name="refresh-cw" size={24} color="currentColor" />
            </div>
            <div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block' }}>Returns & Refunds</span>
              <strong style={{ fontSize: 'var(--text-h4)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)' }}>{refundCount}</strong>
            </div>
          </Card>
        </Grid>
      </section>

      {/* AI Assistant Context Banner */}
      <section aria-label="AI Order Assistant Insights">
        <div 
          style={{
            background: 'linear-gradient(135deg, var(--color-bg-primary-weak) 0%, #f7f9f3 100%)',
            border: '1px solid var(--color-border-default)',
            borderRadius: 'var(--radius-card)',
            padding: '16px 20px',
            display: 'flex',
            alignItems: 'flex-start',
            gap: '16px',
            boxShadow: 'var(--shadow-1)',
          }}
        >
          <div style={{ padding: '8px', background: 'var(--color-bg-primary-default)', color: 'var(--color-text-on-primary)', borderRadius: 'var(--radius-sm)', display: 'flex', marginTop: '2px' }}>
            <Icon name="sparkles" size={16} color="currentColor" />
          </div>
          <div style={{ flex: 1 }}>
            <h4 style={{ margin: 0, fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>
              AI Order Assistant (Beta)
            </h4>
            <p style={{ margin: '4px 0 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', lineHeight: '1.4' }}>
              Your shipment <strong>ORD-2026-8842</strong> containing Pink Oyster spawn is in transit and departing Bengaluru Hub. Expected arrival is tomorrow. Click <a href="/dashboard/orders/ORD-2026-8842/track" style={{ color: 'var(--color-primary)', fontWeight: 'var(--weight-bold)', textDecoration: 'none' }}>Track Shipment</a> to see live milestones, or explore spawn bag sterilization prep guides.
            </p>
          </div>
        </div>
      </section>

      {/* Main Filter & Search Workspace */}
      <div 
        style={{
          display: 'flex',
          flexDirection: 'column',
          gap: '16px',
          background: 'var(--color-bg-surface-default)',
          border: '1px solid var(--color-border-default)',
          borderRadius: 'var(--radius-card)',
          padding: '20px',
        }}
      >
        {/* Search and Filters row */}
        <div 
          style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            flexWrap: 'wrap',
            gap: '16px',
          }}
        >
          {/* Tabs */}
          <div 
            style={{ 
              display: 'flex', 
              background: 'var(--color-bg-surface-secondary, #fafafa)', 
              padding: '4px', 
              borderRadius: 'var(--radius-md)',
              border: '1px solid var(--color-border-default)',
            }}
            role="tablist"
            aria-label="Order status filters"
          >
            {(['All', 'Active', 'Completed', 'Refunded'] as const).map((tab) => (
              <button
                key={tab}
                role="tab"
                aria-selected={activeTab === tab}
                type="button"
                onClick={() => setActiveTab(tab)}
                style={{
                  border: 'none',
                  background: activeTab === tab ? 'var(--color-bg-surface-default)' : 'transparent',
                  color: activeTab === tab ? 'var(--color-text-primary)' : 'var(--color-text-secondary)',
                  padding: '6px 16px',
                  borderRadius: 'var(--radius-sm)',
                  fontSize: 'var(--text-body-sm)',
                  fontWeight: activeTab === tab ? 'var(--weight-semibold)' : 'var(--weight-medium)',
                  cursor: 'pointer',
                  boxShadow: activeTab === tab ? 'var(--shadow-1)' : 'none',
                  transition: 'all 0.15s ease',
                }}
              >
                {tab}
              </button>
            ))}
          </div>

          {/* Search bar */}
          <div 
            style={{ 
              position: 'relative', 
              width: '100%', 
              maxWidth: '320px',
            }}
          >
            <span style={{ position: 'absolute', left: '12px', top: '10px', color: 'var(--color-text-secondary)', display: 'flex' }}>
              <Icon name="search" size={16} color="currentColor" />
            </span>
            <input
              type="text"
              placeholder="Search by order ID or product name..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              aria-label="Search orders"
              style={{
                width: '100%',
                padding: '8px 12px 8px 36px',
                border: '1px solid var(--color-border-default)',
                borderRadius: 'var(--radius-md)',
                fontSize: 'var(--text-body-sm)',
                outline: 'none',
                background: 'var(--color-bg-surface-default)',
                color: 'var(--color-text-primary)',
              }}
            />
          </div>
        </div>

        {/* Orders list */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px', marginTop: '8px' }}>
          {loading ? (
            // Shimmer loader cards
            Array.from({ length: 2 }).map((_, idx) => (
              <Card key={idx} variant="outlined" padding="lg">
                <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <ShimmerLoader width="200px" height="24px" />
                    <ShimmerLoader width="100px" height="24px" />
                  </div>
                  <ShimmerLoader width="100%" height="60px" />
                  <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '12px' }}>
                    <ShimmerLoader width="150px" height="32px" />
                    <ShimmerLoader width="80px" height="24px" />
                  </div>
                </div>
              </Card>
            ))
          ) : filteredOrders.length === 0 ? (
            // Empty State
            <div 
              style={{
                textAlign: 'center',
                padding: '48px 0',
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                gap: '12px',
              }}
            >
              <div 
                style={{
                  width: '64px',
                  height: '64px',
                  borderRadius: '50%',
                  background: 'var(--color-bg-primary-weak)',
                  color: 'var(--color-bg-primary-default)',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  fontSize: '28px',
                }}
              >
                📦
              </div>
              <div>
                <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
                  No orders found
                </h3>
                <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
                  {searchTerm ? 'Try adjusting your search query or status filters.' : 'You have not placed any orders yet.'}
                </p>
              </div>
              <button 
                type="button" 
                className="cw-btn cw-btn--primary"
                onClick={() => navigate('/dashboard/products')}
                style={{ marginTop: '8px' }}
              >
                Browse Products
              </button>
            </div>
          ) : (
            // Actual order cards list
            filteredOrders.map((order) => (
              <EnterpriseOrderCard 
                key={order.id} 
                order={order} 
                onDownloadInvoice={handleDownloadInvoice}
              />
            ))
          )}
        </div>
      </div>
    </div>
  );
};
export default OrdersDashboard;
