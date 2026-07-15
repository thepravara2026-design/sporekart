import React, { useState } from 'react';
import { LIVE_SESSIONS, LiveSession } from './mockData';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';
import { Toast } from '../../../design-system/components/feedback/Toast';

export const TrainingSchedulePage: React.FC = () => {
  const [sessions, setSessions] = useState<LiveSession[]>(LIVE_SESSIONS);
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  const handleRegisterToggle = (id: string) => {
    setSessions(prev =>
      prev.map(s => {
        if (s.id === id) {
          const isRegistering = !s.registered;
          setToastMessage(isRegistering ? `Registered for "${s.title}" successfully!` : `Cancelled registration for "${s.title}".`);
          return {
            ...s,
            registered: isRegistering,
            spotsRemaining: isRegistering ? s.spotsRemaining - 1 : s.spotsRemaining + 1
          };
        }
        return s;
      })
    );
    setTimeout(() => setToastMessage(null), 3000);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      {/* Toast Notification */}
      {toastMessage && (
        <div style={{ position: 'fixed', bottom: '24px', right: '24px', zIndex: 1000 }}>
          <Toast message={toastMessage} tone="success" onClose={() => setToastMessage(null)} />
        </div>
      )}

      {/* Title */}
      <div>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
          Live Webinar Schedule
        </h2>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
          Reserve your seat for interactive, live Q&A sessions hosted by lead mycology scientists.
        </p>
      </div>

      <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
        {sessions.map((session) => (
          <Card 
            key={session.id}
            variant="outlined"
            padding="lg"
            style={{ 
              display: 'flex', 
              justifyContent: 'space-between', 
              alignItems: 'center', 
              flexWrap: 'wrap', 
              gap: '16px',
              borderLeft: session.registered ? '4px solid var(--color-bg-primary-default)' : '1px solid var(--color-border-default)',
            }}
          >
            <div style={{ flex: 1, minWidth: '280px' }}>
              <div style={{ display: 'flex', gap: '12px', alignItems: 'center', flexWrap: 'wrap' }}>
                <span 
                  style={{ 
                    fontSize: '10px', 
                    fontWeight: 'bold', 
                    background: 'var(--color-bg-primary-weak)', 
                    color: 'var(--color-primary)', 
                    padding: '2px 8px', 
                    borderRadius: 'var(--radius-sm)' 
                  }}
                >
                  LIVE SESSION
                </span>

                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'flex', alignItems: 'center', gap: '4px' }}>
                  <Icon name="calendar" size={12} color="currentColor" />
                  {session.date} · {session.time}
                </span>
              </div>

              <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: '8px 0 4px' }}>
                {session.title}
              </h3>
              
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                Trainer: <strong>{session.trainer}</strong> · Duration: <strong>1.5 Hours</strong>
              </span>
            </div>

            <div style={{ display: 'flex', alignItems: 'center', gap: '20px', flexWrap: 'wrap' }}>
              <div style={{ textAlign: 'right', minWidth: '100px' }}>
                <span style={{ fontSize: '9px', color: 'var(--color-text-secondary)', display: 'block' }}>AVAILABLE SEATS</span>
                <strong style={{ fontSize: 'var(--text-body-sm)', color: session.spotsRemaining < 10 ? 'var(--color-text-danger, #dc2626)' : 'var(--color-text-primary)' }}>
                  {session.spotsRemaining} spots left
                </strong>
              </div>

              <button
                type="button"
                onClick={() => handleRegisterToggle(session.id)}
                className={`cw-btn ${session.registered ? 'cw-btn--outlined' : 'cw-btn--primary'}`}
                style={{ minWidth: '140px' }}
              >
                {session.registered ? 'Cancel Booking' : 'Register RSVP'}
              </button>
            </div>
          </Card>
        ))}
      </div>

      {/* Online session tips */}
      <Card variant="outlined" padding="md" style={{ background: '#f8fafc' }}>
        <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: '0 0 8px' }}>
          Webinar Access Policies
        </h4>
        <ul style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', paddingLeft: '16px', margin: 0, lineHeight: '1.5' }}>
          <li>Meeting links are activated 10 minutes prior to session timings on the dashboard.</li>
          <li>Recordings are archived and uploaded inside course repositories within 24 hours of webinar completion.</li>
          <li>Live webinar attendance is credited toward certified grower point progressions.</li>
        </ul>
      </Card>

    </div>
  );
};
export default TrainingSchedulePage;
