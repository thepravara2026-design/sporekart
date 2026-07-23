import React from 'react';

interface QuickActionBarProps {
  onAction: (action: string) => void;
}

const actions = [
  { id: 'search-products', label: 'Search Products', icon: '🔍' },
  { id: 'track-order', label: 'Track Order', icon: '📦' },
  { id: 'growing-tips', label: 'Growing Tips', icon: '🌱' },
  { id: 'faqs', label: 'FAQs', icon: '❓' },
  { id: 'recommendations', label: 'Recommendations', icon: '⭐' },
  { id: 'my-orders', label: 'My Orders', icon: '📋' },
  { id: 'training-courses', label: 'Training Courses', icon: '🎓' },
  { id: 'contact-support', label: 'Contact Support', icon: '💬' },
];

export default function QuickActionBar({ onAction }: QuickActionBarProps) {
  return (
    <div style={{
      display: 'grid',
      gridTemplateColumns: '1fr 1fr',
      gap: '8px',
      fontFamily: 'system-ui, sans-serif',
    }}>
      {actions.map((action) => (
        <button
          key={action.id}
          onClick={() => onAction(action.id)}
          style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            gap: '4px',
            padding: '12px 8px',
            border: '1px solid #e5e7eb',
            borderRadius: '8px',
            background: '#fff',
            cursor: 'pointer',
            transition: 'background 0.15s, border-color 0.15s',
          }}
          onMouseEnter={(e) => {
            e.currentTarget.style.background = '#f9fafb';
            e.currentTarget.style.borderColor = '#d1d5db';
          }}
          onMouseLeave={(e) => {
            e.currentTarget.style.background = '#fff';
            e.currentTarget.style.borderColor = '#e5e7eb';
          }}
        >
          <span style={{ fontSize: '24px', lineHeight: 1 }}>{action.icon}</span>
          <span style={{ fontSize: '11px', color: '#374151', fontWeight: 500, textAlign: 'center' }}>
            {action.label}
          </span>
        </button>
      ))}
    </div>
  );
}
