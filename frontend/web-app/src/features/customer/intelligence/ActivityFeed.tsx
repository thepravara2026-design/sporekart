import React, { useState } from 'react';
import { ACTIVITY_LOG, ActivityEvent } from './mockData';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';

export const ActivityFeed: React.FC = () => {
  const [feed] = useState<ActivityEvent[]>(ACTIVITY_LOG);
  const [filter, setFilter] = useState<'all' | 'order' | 'training' | 'support'>('all');

  const filteredFeed = feed.filter(evt => {
    if (filter === 'all') return true;
    return evt.type === filter;
  });

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      
      {/* Title */}
      <div>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
          Personal Activity Feed
        </h2>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
          Real-time chronological timeline tracking orders, support tickets, and class completions.
        </p>
      </div>

      {/* Tabs */}
      <div 
        style={{ 
          display: 'flex', 
          gap: '12px', 
          borderBottom: '1px solid var(--color-border-default)', 
          paddingBottom: '12px',
          flexWrap: 'wrap'
        }}
      >
        {[
          { id: 'all', label: 'All Activities' },
          { id: 'order', label: 'Orders & Shipments' },
          { id: 'training', label: 'Academy & Study' },
          { id: 'support', label: 'Support & Tickets' },
        ].map((tab) => (
          <button
            key={tab.id}
            type="button"
            onClick={() => setFilter(tab.id as any)}
            style={{
              border: 'none',
              background: filter === tab.id ? 'var(--color-bg-primary-weak)' : 'transparent',
              padding: '6px 12px',
              borderRadius: 'var(--radius-sm)',
              fontSize: 'var(--text-body-sm)',
              fontWeight: filter === tab.id ? 'var(--weight-bold)' : 'var(--weight-medium)',
              color: filter === tab.id ? 'var(--color-primary)' : 'var(--color-text-secondary)',
              cursor: 'pointer'
            }}
          >
            {tab.label}
          </button>
        ))}
      </div>

      {/* Timeline logs */}
      <div style={{ position: 'relative', paddingLeft: '24px' }}>
        {/* vertical line indicator */}
        <div 
          style={{ 
            position: 'absolute', 
            left: '11px', 
            top: '8px', 
            bottom: '8px', 
            width: '2px', 
            background: 'var(--color-border-default)' 
          }} 
        />

        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          {filteredFeed.length === 0 ? (
            <div style={{ padding: '32px 0', color: 'var(--color-text-secondary)' }}>
              No activities found in this category.
            </div>
          ) : (
            filteredFeed.map((evt) => (
              <div key={evt.id} style={{ position: 'relative', display: 'flex', gap: '16px' }}>
                {/* circle bullet */}
                <div 
                  style={{ 
                    position: 'absolute', 
                    left: '-24px', 
                    top: '4px',
                    width: '12px', 
                    height: '12px', 
                    borderRadius: '50%', 
                    background: '#fff', 
                    border: '3px solid var(--color-primary)',
                    zIndex: 2
                  }} 
                />

                <Card variant="outlined" padding="md" style={{ flex: 1 }}>
                  <div style={{ display: 'flex', gap: '12px', alignItems: 'flex-start' }}>
                    <div style={{ color: 'var(--color-primary)', marginTop: '2px' }}>
                      <Icon name={evt.icon} size={16} color="currentColor" />
                    </div>
                    <div>
                      <h4 style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', margin: 0, fontWeight: 'var(--weight-medium)' }}>
                        {evt.message}
                      </h4>
                      <span style={{ fontSize: '10px', color: 'var(--color-text-secondary)', display: 'block', marginTop: '4px' }}>
                        Timestamp: {evt.date} · Source: {evt.type.toUpperCase()}
                      </span>
                    </div>
                  </div>
                </Card>
              </div>
            ))
          )}
        </div>
      </div>

    </div>
  );
};
export default ActivityFeed;
