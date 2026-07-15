import React from 'react';

import type { TrackingMilestone } from './mockData';

interface OrderTimelineProps {
  milestones: TrackingMilestone[];
  status: string;
}

export const OrderTimeline: React.FC<OrderTimelineProps> = ({ milestones, status }) => {
  const isCancelled = status === 'Cancelled';
  const isReturned = status === 'Returned' || status === 'Refunded';

  return (
    <div 
      className="sk-timeline"
      style={{
        display: 'flex',
        flexDirection: 'column',
        gap: 'var(--space-stack-md)',
        padding: 'var(--space-stack-md) 0',
      }}
    >
      <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-xs)' }}>
        Order Lifecycle Status: <span style={{ 
          color: isCancelled ? 'var(--color-text-danger, #dc2626)' : isReturned ? 'var(--color-text-warning, #d97706)' : 'var(--color-bg-primary-default)',
          fontWeight: 'var(--weight-bold)'
        }}>{status}</span>
      </h3>

      <div 
        className="sk-timeline__steps"
        style={{
          display: 'flex',
          flexDirection: 'column',
          position: 'relative',
          paddingLeft: '24px',
          borderLeft: '2px solid var(--color-border-default)',
          gap: 'var(--space-stack-md)',
          marginLeft: '8px',
        }}
      >
        {milestones.map((m, i) => {
          const isActive = m.completed && (i === milestones.length - 1 || !milestones[i + 1]?.completed);
          const isWarning = isCancelled && m.status === 'Cancelled' || isReturned && (m.status === 'Returned' || m.status === 'Refunded');
          
          return (
            <div 
              key={m.status} 
              className="sk-timeline__step"
              style={{
                position: 'relative',
                opacity: m.completed ? 1 : 0.4,
              }}
            >
              {/* Dot / Icon container */}
              <div 
                className="sk-timeline__dot"
                style={{
                  position: 'absolute',
                  left: '-33px',
                  top: '2px',
                  width: '16px',
                  height: '16px',
                  borderRadius: '50%',
                  backgroundColor: m.completed
                    ? isWarning
                      ? 'var(--color-text-danger, #dc2626)'
                      : 'var(--color-bg-primary-default)'
                    : 'var(--color-bg-surface-default)',
                  border: `2px solid ${m.completed ? 'transparent' : 'var(--color-border-default)'}`,
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  color: '#fff',
                  fontSize: '10px',
                  fontWeight: 'bold',
                  boxShadow: isActive ? '0 0 0 4px var(--color-bg-primary-weak)' : 'none',
                }}
              >
                {m.completed && (
                  <span style={{ display: 'block', width: '6px', height: '6px', borderRadius: '50%', backgroundColor: '#fff' }} />
                )}
              </div>

              {/* Text detail */}
              <div>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'baseline' }}>
                  <h4 
                    style={{ 
                      fontSize: 'var(--text-body-sm)', 
                      fontWeight: isActive ? 'var(--weight-bold)' : 'var(--weight-semibold)',
                      color: isActive ? 'var(--color-text-primary)' : 'var(--color-text-secondary)',
                      margin: 0,
                    }}
                  >
                    {m.status}
                  </h4>
                  <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                    {m.timestamp}
                  </span>
                </div>
                <p 
                  style={{ 
                    fontSize: 'var(--text-caption)', 
                    color: 'var(--color-text-secondary)', 
                    margin: '2px 0 0',
                  }}
                >
                  {m.detail}
                </p>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};
