import { Icon } from '../../../design-system/icons/Icon';
import '../customer.css';



export function QuickActions() {
  const actions = [
    { label: 'Browse Products', href: '/dashboard/products', icon: <Icon name="package" size={18} color="currentColor" /> },
    { label: 'Track Orders', href: '/dashboard/orders', icon: <Icon name="truck" size={18} color="currentColor" /> },
    { label: 'Continue Training', href: '/dashboard/training', icon: <Icon name="book-open" size={18} color="currentColor" /> },
    { label: 'Contact Support', href: '/dashboard/support', icon: <Icon name="help-circle" size={18} color="currentColor" /> },
    { label: 'Manage Addresses', href: '/dashboard/addresses', icon: <Icon name="map-pin" size={18} color="currentColor" /> },
    { label: 'Edit Profile', href: '/dashboard/profile', icon: <Icon name="user" size={18} color="currentColor" /> },
  ];

  return (
    <div className="cw-actions" role="list">
      {actions.map((a) => (
        <a key={a.label} href={a.href} className="cw-action" role="listitem">
          <span className="cw-action__icon">{a.icon}</span>
          <span className="cw-action__label">{a.label}</span>
        </a>
      ))}
    </div>
  );
}

export function ProfileSummary() {
  const profile = { name: 'Jane Growell', email: 'jane@sporekart.in', tier: 'Premium', verified: true };
  return (
    <div className="cw-profile-summary">
      <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '12px' }}>
        <div style={{ width: 40, height: 40, borderRadius: '50%', background: 'var(--color-bg-primary-default)', color: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 18, fontWeight: 700 }}>JG</div>
        <div>
          <p style={{ margin: 0, fontWeight: 600, fontSize: 'var(--text-body-sm)' }}>{profile.name}</p>
          <p style={{ margin: '2px 0 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{profile.email}</p>
        </div>
      </div>
      <div style={{ display: 'flex', gap: '8px', fontSize: 'var(--text-caption)' }}>
        <span style={{ background: 'var(--color-bg-accent-subtle)', color: 'var(--color-text-accent)', padding: '2px 8px', borderRadius: 'var(--radius-sm)' }}>{profile.tier}</span>
        <span style={{ background: 'var(--color-bg-success-subtle)', color: 'var(--color-text-success)', padding: '2px 8px', borderRadius: 'var(--radius-sm)' }}>{profile.verified ? 'Verified' : 'Unverified'}</span>
      </div>
    </div>
  );
}

export function NotificationsPreview() {
  const notifications = [
    { title: 'Order #SK-2024-0891 shipped', desc: 'Your Pink Oyster spores are on the way.', time: '1h ago', icon: <Icon name="truck" size={14} color="currentColor" />, tone: 'info' as const },
    { title: 'Training module completed', desc: 'Advanced Sterile Technique — certificate ready.', time: '4h ago', icon: <Icon name="check-circle" size={14} color="currentColor" />, tone: 'success' as const },
    { title: 'New cultivar available', desc: 'Lion\'s Mane (Hericium erinaceus) — limited stock.', time: '1d ago', icon: <Icon name="star" size={14} color="currentColor" />, tone: 'warning' as const },
  ];

  return (
    <div className="cw-notifications">
      {notifications.map((n, i) => (
        <a key={i} href="/dashboard/notifications" className="cw-notification" role="listitem">
          <span className={`cw-notification__icon cw-notification__icon--${n.tone}`}>{n.icon}</span>
          <div className="cw-notification__content">
            <p className="cw-notification__title">{n.title}</p>
            <p className="cw-notification__desc">{n.desc}</p>
          </div>
          <time className="cw-notification__time">{n.time}</time>
        </a>
      ))}
    </div>
  );
}

interface SummaryWidgetProps {
  stats: { label: string; value: string; trend?: string }[];
}

export function SummaryWidget({ stats }: SummaryWidgetProps) {
  return (
    <div className="cw-summary">
      {stats.map((s, i) => (
        <div key={i} className="cw-summary__stat">
          <span className="cw-summary__label">{s.label}</span>
          <>
            <span className="cw-summary__value">{s.value}</span>
            {s.trend && <span className="cw-summary__trend">{s.trend}</span>}
          </>
        </div>
      ))}
    </div>
  );
}

export function AccountCompletion() {
  return (
    <div className="cw-completion">
      <div className="cw-completion__label">
        <span>Profile Completion</span>
        <span>85%</span>
      </div>
      <div className="cw-completion__fill">
        <div className="cw-completion__bar" style={{ width: '85%' }} />
      </div>
      <div className="cw-completion__steps">
        <div className="cw-completion__step cw-completion__step--done">
          <Icon name="check-circle" size={12} color="currentColor" className="cw-completion__step-icon" />
          Email verified
        </div>
        <div className="cw-completion__step cw-completion__step--done">
          <Icon name="check-circle" size={12} color="currentColor" className="cw-completion__step-icon" />
          Phone verified
        </div>
        <div className="cw-completion__step cw-completion__step--done">
          <Icon name="check-circle" size={12} color="currentColor" className="cw-completion__step-icon" />
          Address added
        </div>
        <div className="cw-completion__step cw-completion__step--pending">
          <Icon name="clock" size={12} color="currentColor" className="cw-completion__step-icon" />
          Add payment method
        </div>
      </div>
    </div>
  );
}

export function RecommendedActions() {
  const recs = [
    { title: 'Complete your profile', desc: 'Add payment method to enable one-click ordering', icon: <Icon name="credit-card" size={14} color="currentColor" /> },
    { title: 'Enroll in Spore Cultivation 101', desc: 'Free training module for new growers', icon: <Icon name="book-open" size={14} color="currentColor" /> },
    { title: 'Review your wishlist', desc: '3 items waiting — limited stock on 2', icon: <Icon name="heart" size={14} color="currentColor" /> },
  ];

  return (
    <div className="cw-recommendations">
      {recs.map((r, i) => (
        <a key={i} href="#" className="cw-recommendation" role="listitem">
          <span className="cw-recommendation__icon">{r.icon}</span>
          <div className="cw-recommendation__text">
            <span className="cw-recommendation__title">{r.title}</span>
            <span className="cw-recommendation__desc">{r.desc}</span>
          </div>
        </a>
      ))}
    </div>
  );
}

export function RecentActivity() {
  const activities = [
    { type: 'order', label: 'Order placed', desc: 'Pink Oyster Grain Spawn — 2kg', time: '2 days ago', icon: <Icon name="shopping-bag" size={14} color="currentColor" /> },
    { type: 'training', label: 'Module completed', desc: 'Sterile Lab Setup — passed', time: '5 days ago', icon: <Icon name="check-circle" size={14} color="currentColor" /> },
    { type: 'wishlist', label: 'Added to wishlist', desc: 'Lion\'s Mane Sawdust Spawn', time: '1 week ago', icon: <Icon name="heart" size={14} color="currentColor" /> },
  ];

  return (
    <div className="cw-recommendations">
      {activities.map((a, i) => (
        <div key={i} className="cw-recommendation" style={{ cursor: 'default' }}>
          <span className="cw-recommendation__icon">{a.icon}</span>
          <div className="cw-recommendation__text">
            <span className="cw-recommendation__title">{a.label}</span>
            <span className="cw-recommendation__desc">{a.desc} · {a.time}</span>
          </div>
        </div>
      ))}
    </div>
  );
}

export function SupportShortcuts() {
  const shortcuts = [
    { label: 'Contact Support', href: '/dashboard/support', icon: <Icon name="message-circle" size={14} color="currentColor" /> },
    { label: 'Knowledge Base', href: '/support', icon: <Icon name="book" size={14} color="currentColor" /> },
    { label: 'Track Ticket', href: '/dashboard/support/tickets', icon: <Icon name="search" size={14} color="currentColor" /> },
    { label: 'Community Forum', href: '/community', icon: <Icon name="users" size={14} color="currentColor" /> },
  ];

  return (
    <div className="cw-actions" role="list">
      {shortcuts.map((s) => (
        <a key={s.label} href={s.href} className="cw-action" role="listitem">
          <span className="cw-action__icon">{s.icon}</span>
          <span className="cw-action__label">{s.label}</span>
        </a>
      ))}
    </div>
  );
}