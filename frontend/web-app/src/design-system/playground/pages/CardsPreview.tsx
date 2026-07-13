import { useState } from 'react';

const TrendUp = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <path d="M1 11l4-4 3 3 6-6" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
    <path d="M10 4h4v4" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);

const TrendDown = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <path d="M1 5l4 4 3-3 6 6" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
    <path d="M10 12h4V8" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);

const CheckIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <path d="M3 8l3 3 7-7" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);

const XIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <path d="M4 4l8 8M12 4l-8 8" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);

const AlertIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <circle cx="8" cy="8" r="7" stroke="currentColor" strokeWidth="1.5"/>
    <path d="M8 5v3M8 11h0" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/>
  </svg>
);

const InfoIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <circle cx="8" cy="8" r="7" stroke="currentColor" strokeWidth="1.5"/>
    <path d="M8 7v4M8 5h0" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/>
  </svg>
);

const PlusIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <path d="M8 3v10M3 8h10" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/>
  </svg>
);

const MailIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <rect x="1" y="3" width="14" height="10" rx="1" stroke="currentColor" strokeWidth="1.5"/>
    <path d="M1 4l7 5 7-5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);

const BellIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <path d="M8 1a5 5 0 00-5 5c0 4-2 5-2 5h14s-2-1-2-5a5 5 0 00-5-5z" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
    <path d="M6.5 12a1.5 1.5 0 003 0" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/>
  </svg>
);

const PlayIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <path d="M4 2l10 6-10 6V2z" fill="currentColor"/>
  </svg>
);

const ImageIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <rect x="1" y="2" width="14" height="12" rx="1" stroke="currentColor" strokeWidth="1.5"/>
    <circle cx="5" cy="6" r="1.5" fill="currentColor"/>
    <path d="M1 12l4-3 3 2 3-4 4 5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);

const ZapIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <path d="M7 1L2 9h5l-1 6 6-8H7l2-6H7z" fill="currentColor"/>
  </svg>
);

const SearchIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <circle cx="7" cy="7" r="5" stroke="currentColor" strokeWidth="1.5"/>
    <path d="M11 11l3.5 3.5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/>
  </svg>
);

const CardSection = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
    <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{label}</h2>
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>{children}</div>
  </section>
);

const CardFrame = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
    {children}
  </div>
);

export default function CardsPreview() {
  const [pricingHighlighted] = useState(true);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Cards</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All 15 card variants with states</p>
      </div>

      <CardSection label="Stat Cards">
        <CardFrame label="StatCard — Trend Up">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
            <div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Total Revenue</span>
              <div style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)' }}>$48,250</div>
            </div>
            <div style={{ color: 'var(--color-success, #16a34a)' }}><TrendUp /></div>
          </div>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-success, #16a34a)' }}>+12.5% from last month</span>
        </CardFrame>
        <CardFrame label="StatCard — Trend Down">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
            <div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Bounce Rate</span>
              <div style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)' }}>32.4%</div>
            </div>
            <div style={{ color: 'var(--color-danger, #dc2626)' }}><TrendDown /></div>
          </div>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-danger, #dc2626)' }}>+2.1% from last month</span>
        </CardFrame>
        <CardFrame label="MetricCard">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Conversion Rate</span>
            <span style={{ fontSize: 'var(--text-sm)', color: 'var(--color-success, #16a34a)', fontWeight: 'var(--weight-semibold)' }}>+3.2%</span>
          </div>
          <div style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)' }}>5.8%</div>
        </CardFrame>
        <CardFrame label="InfoCard">
          <div style={{ display: 'flex', gap: '12px', alignItems: 'flex-start' }}>
            <div style={{ color: 'var(--color-brand, #2563eb)', flexShrink: 0 }}><InfoIcon /></div>
            <div>
              <div style={{ fontWeight: 'var(--weight-semibold)' }}>New Feature Available</div>
              <p style={{ fontSize: 'var(--text-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>You can now export reports in CSV format. Check the analytics section.</p>
            </div>
          </div>
        </CardFrame>
        <CardFrame label="ProfileCard">
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
            <div style={{ width: 40, height: 40, borderRadius: '50%', background: 'var(--color-brand, #2563eb)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff', fontWeight: 'var(--weight-bold)', fontSize: 'var(--text-sm)' }}>JD</div>
            <div>
              <div style={{ fontWeight: 'var(--weight-semibold)' }}>John Doe</div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>john@example.com</span>
            </div>
          </div>
        </CardFrame>
        <CardFrame label="FeatureCard">
          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', alignItems: 'center', textAlign: 'center' }}>
            <div style={{ width: 40, height: 40, borderRadius: 'var(--radius-md)', background: 'var(--color-bg-primary-default)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff' }}><ZapIcon /></div>
            <div style={{ fontWeight: 'var(--weight-semibold)' }}>Lightning Fast</div>
            <p style={{ fontSize: 'var(--text-sm)', color: 'var(--color-text-secondary)', margin: 0 }}>Process orders 10x faster with our optimized pipeline.</p>
          </div>
        </CardFrame>
        <CardFrame label="PricingCard — Highlighted">
          <div style={{ display: 'flex', flexDirection: 'column', gap: '12px', border: pricingHighlighted ? '2px solid var(--color-brand, #2563eb)' : '1px solid var(--color-border-default)', borderRadius: 'var(--radius-card)', padding: '20px', background: pricingHighlighted ? 'var(--color-bg-primary-subtle, #eff6ff)' : 'transparent' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <span style={{ fontWeight: 'var(--weight-semibold)' }}>Pro Plan</span>
              {pricingHighlighted && <span style={{ fontSize: 'var(--text-caption)', background: 'var(--color-bg-primary-default)', color: '#fff', padding: '2px 8px', borderRadius: 'var(--radius-sm)' }}>Popular</span>}
            </div>
            <div><span style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)' }}>$29</span><span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>/mo</span></div>
            <ul style={{ fontSize: 'var(--text-sm)', margin: 0, padding: '0 0 0 16px', display: 'flex', flexDirection: 'column', gap: '4px' }}>
              <li>Up to 500 orders</li>
              <li>Basic analytics</li>
              <li>Email support</li>
            </ul>
            <button style={{ background: 'var(--color-bg-primary-default)', color: '#fff', border: 'none', borderRadius: 'var(--radius-md)', padding: '8px 16px', cursor: 'pointer', fontWeight: 'var(--weight-semibold)' }}>Subscribe</button>
          </div>
        </CardFrame>
        <CardFrame label="PricingCard — Normal">
          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-card)', padding: '20px' }}>
            <span style={{ fontWeight: 'var(--weight-semibold)' }}>Basic</span>
            <div><span style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)' }}>$9</span><span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>/mo</span></div>
            <ul style={{ fontSize: 'var(--text-sm)', margin: 0, padding: '0 0 0 16px', display: 'flex', flexDirection: 'column', gap: '4px' }}>
              <li>Up to 50 orders</li>
              <li>Basic analytics</li>
            </ul>
            <button style={{ background: 'transparent', color: 'var(--color-text-primary)', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-md)', padding: '8px 16px', cursor: 'pointer' }}>Subscribe</button>
          </div>
        </CardFrame>
        <CardFrame label="ProductCard">
          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
            <div style={{ width: '100%', height: 120, background: 'var(--color-bg-subtle, #f3f4f6)', borderRadius: 'var(--radius-md)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--color-text-tertiary)' }}><ImageIcon /></div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
              <div>
                <div style={{ fontWeight: 'var(--weight-semibold)' }}>Organic Mushroom Kit</div>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Grow your own</span>
              </div>
              <span style={{ fontSize: 'var(--text-sm)', background: 'var(--color-bg-success-subtle, #dcfce7)', color: 'var(--color-success, #16a34a)', padding: '2px 6px', borderRadius: 'var(--radius-sm)' }}>In Stock</span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <span style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>$24.99</span>
              <button style={{ background: 'var(--color-bg-primary-default)', color: '#fff', border: 'none', borderRadius: 'var(--radius-sm)', padding: '6px 12px', cursor: 'pointer', fontSize: 'var(--text-sm)' }}>Add to Cart</button>
            </div>
          </div>
        </CardFrame>
        <CardFrame label="OrderCard">
          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <span style={{ fontWeight: 'var(--weight-semibold)' }}>#ORD-2024-0842</span>
              <span style={{ fontSize: 'var(--text-caption)', padding: '2px 8px', borderRadius: 'var(--radius-sm)', background: 'var(--color-bg-warning-subtle, #fef3c7)', color: 'var(--color-warning, #f59e0b)' }}>Processing</span>
            </div>
            <div style={{ fontSize: 'var(--text-sm)', color: 'var(--color-text-secondary)' }}>2 items · $156.00</div>
            <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Placed Mar 15, 2026</div>
          </div>
        </CardFrame>
        <CardFrame label="SummaryCard">
          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <span style={{ fontSize: 'var(--text-sm)' }}>Subtotal</span>
              <span style={{ fontSize: 'var(--text-sm)' }}>$120.00</span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <span style={{ fontSize: 'var(--text-sm)' }}>Shipping</span>
              <span style={{ fontSize: 'var(--text-sm)' }}>$10.00</span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <span style={{ fontSize: 'var(--text-sm)' }}>Tax</span>
              <span style={{ fontSize: 'var(--text-sm)' }}>$9.60</span>
            </div>
            <div style={{ borderTop: '1px solid var(--color-border-default)', paddingTop: '8px', display: 'flex', justifyContent: 'space-between', fontWeight: 'var(--weight-bold)' }}>
              <span>Total</span>
              <span>$139.60</span>
            </div>
          </div>
        </CardFrame>
        <CardFrame label="StatusCard — Success">
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px', padding: '12px', borderRadius: 'var(--radius-md)', background: 'var(--color-bg-success-subtle, #dcfce7)' }}>
            <div style={{ width: 24, height: 24, borderRadius: '50%', background: 'var(--color-success, #16a34a)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff' }}><CheckIcon /></div>
            <div>
              <div style={{ fontWeight: 'var(--weight-semibold)' }}>Payment Successful</div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Transaction #TX-2024-0912</span>
            </div>
          </div>
        </CardFrame>
        <CardFrame label="StatusCard — Warning">
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px', padding: '12px', borderRadius: 'var(--radius-md)', background: 'var(--color-bg-warning-subtle, #fef3c7)' }}>
            <div style={{ width: 24, height: 24, borderRadius: '50%', background: 'var(--color-warning, #f59e0b)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff' }}><AlertIcon /></div>
            <div>
              <div style={{ fontWeight: 'var(--weight-semibold)' }}>Low Stock Alert</div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>3 items below threshold</span>
            </div>
          </div>
        </CardFrame>
        <CardFrame label="StatusCard — Error">
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px', padding: '12px', borderRadius: 'var(--radius-md)', background: 'var(--color-bg-danger-subtle, #fee2e2)' }}>
            <div style={{ width: 24, height: 24, borderRadius: '50%', background: 'var(--color-danger, #dc2626)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff' }}><XIcon /></div>
            <div>
              <div style={{ fontWeight: 'var(--weight-semibold)' }}>Payment Failed</div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Insufficient funds</span>
            </div>
          </div>
        </CardFrame>
        <CardFrame label="NotificationCard — Unread">
          <div style={{ display: 'flex', gap: '12px', alignItems: 'flex-start', padding: '12px', borderRadius: 'var(--radius-md)', background: 'var(--color-bg-primary-subtle, #eff6ff)', borderLeft: '3px solid var(--color-brand, #2563eb)' }}>
            <div style={{ color: 'var(--color-brand, #2563eb)', flexShrink: 0 }}><BellIcon /></div>
            <div style={{ flex: 1 }}>
              <div style={{ fontWeight: 'var(--weight-semibold)' }}>New order received</div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Order #2024-0912 from Jane Smith</span>
            </div>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>2m ago</span>
          </div>
        </CardFrame>
        <CardFrame label="NotificationCard — Read">
          <div style={{ display: 'flex', gap: '12px', alignItems: 'flex-start', padding: '12px', borderRadius: 'var(--radius-md)' }}>
            <div style={{ color: 'var(--color-text-tertiary)', flexShrink: 0 }}><BellIcon /></div>
            <div style={{ flex: 1 }}>
              <div style={{ fontWeight: 'var(--weight-normal)' }}>Order shipped</div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Your order #2024-0891 has shipped</span>
            </div>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>1h ago</span>
          </div>
        </CardFrame>
        <CardFrame label="QuickActionCard">
          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
            <div style={{ display: 'flex', gap: '8px' }}>
              <button style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '4px', padding: '12px', background: 'var(--color-bg-subtle, #f3f4f6)', border: 'none', borderRadius: 'var(--radius-md)', cursor: 'pointer' }}>
                <PlusIcon /> <span style={{ fontSize: 'var(--text-caption)' }}>New Order</span>
              </button>
              <button style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '4px', padding: '12px', background: 'var(--color-bg-subtle, #f3f4f6)', border: 'none', borderRadius: 'var(--radius-md)', cursor: 'pointer' }}>
                <SearchIcon /> <span style={{ fontSize: 'var(--text-caption)' }}>Search</span>
              </button>
              <button style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '4px', padding: '12px', background: 'var(--color-bg-subtle, #f3f4f6)', border: 'none', borderRadius: 'var(--radius-md)', cursor: 'pointer' }}>
                <MailIcon /> <span style={{ fontSize: 'var(--text-caption)' }}>Inbox</span>
              </button>
            </div>
          </div>
        </CardFrame>
        <CardFrame label="MediaCard">
          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
            <div style={{ width: '100%', height: 140, background: 'linear-gradient(135deg, var(--color-brand, #2563eb), #7c3aed)', borderRadius: 'var(--radius-md)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff' }}>
              <PlayIcon />
            </div>
            <div style={{ fontWeight: 'var(--weight-semibold)' }}>Getting Started Guide</div>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>12 min · Beginner</span>
          </div>
        </CardFrame>
        <CardFrame label="TrainingCard">
          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <span style={{ fontWeight: 'var(--weight-semibold)' }}>Mushroom Cultivation 101</span>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>60%</span>
            </div>
            <div style={{ width: '100%', height: 8, background: 'var(--color-bg-subtle, #f3f4f6)', borderRadius: '4px', overflow: 'hidden' }}>
              <div style={{ width: '60%', height: '100%', background: 'var(--color-brand, #2563eb)', borderRadius: '4px' }} />
            </div>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>3 of 5 modules completed</span>
          </div>
        </CardFrame>
        <CardFrame label="Card — Loading">
          <div style={{ display: 'flex', flexDirection: 'column', gap: '12px', padding: '16px' }}>
            <div style={{ width: '60%', height: 16, background: 'var(--color-bg-subtle, #f3f4f6)', borderRadius: '4px', animation: 'sk-pulse 1.5s ease-in-out infinite' }} />
            <div style={{ width: '40%', height: 24, background: 'var(--color-bg-subtle, #f3f4f6)', borderRadius: '4px', animation: 'sk-pulse 1.5s ease-in-out infinite' }} />
            <div style={{ width: '80%', height: 12, background: 'var(--color-bg-subtle, #f3f4f6)', borderRadius: '4px', animation: 'sk-pulse 1.5s ease-in-out infinite' }} />
          </div>
        </CardFrame>
        <CardFrame label="Card — Error State">
          <div style={{ display: 'flex', flexDirection: 'column', gap: '12px', alignItems: 'center', padding: '24px', textAlign: 'center' }}>
            <div style={{ color: 'var(--color-danger, #dc2626)' }}><AlertIcon /></div>
            <div style={{ fontWeight: 'var(--weight-semibold)' }}>Failed to load data</div>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Something went wrong. Please try again.</span>
            <button style={{ background: 'var(--color-bg-primary-default)', color: '#fff', border: 'none', borderRadius: 'var(--radius-sm)', padding: '6px 12px', cursor: 'pointer', fontSize: 'var(--text-sm)' }}>Retry</button>
          </div>
        </CardFrame>
      </CardSection>
    </div>
  );
}
