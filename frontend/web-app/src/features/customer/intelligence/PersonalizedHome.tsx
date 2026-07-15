import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { PROFILE, SUGGESTIONS, ACTIVITY_LOG } from './mockData';
import { Grid } from '../../../design-system/components/layout/Grid';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';
import { Toast } from '../../../design-system/components/feedback/Toast';

export const PersonalizedHome: React.FC = () => {
  const navigate = useNavigate();
  const [profile] = useState(PROFILE);
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  // Digital assistant chat drawer state
  const [isAssistantOpen, setIsAssistantOpen] = useState(false);
  const [chatLog, setChatLog] = useState<Array<{ sender: 'user' | 'assistant', text: string }>>([
    { sender: 'assistant', text: 'Hello Jane! I am your SporeKart Digital Assistant. How can I help you manage your cleanroom or orders today?' }
  ]);

  const assistantSuggestions = [
    { q: 'Where is my order ORD-8842?', a: 'Your order ORD-2026-8842 is currently in transit via Delhivery. It is at the Bengaluru sorting hub and scheduled to deliver tomorrow by 4:00 PM.' },
    { q: 'Give me laboratory sterility tips.', a: 'Sterile Transfer Rule: Flame sterilize loop needles red-hot, clean HEPA filters, work at center stage of laminar flow hoods, and wear gloves wiped with 70% IPA.' },
    { q: 'Any webinars this week?', a: 'Yes! "Substrate Chemistry & Water Balance" is hosted by Dr. Anita Rao this Thursday at 2:00 PM.' }
  ];

  const handleTriggerSuggestion = (q: string, a: string) => {
    setChatLog(prev => [
      ...prev,
      { sender: 'user', text: q },
      { sender: 'assistant', text: a }
    ]);
  };

  const getGreeting = () => {
    const hours = new Date().getHours();
    if (hours < 12) return 'Good morning';
    if (hours < 18) return 'Good afternoon';
    return 'Good evening';
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', position: 'relative' }}>
      
      {/* Toast */}
      {toastMessage && (
        <div style={{ position: 'fixed', bottom: '24px', right: '24px', zIndex: 1000 }}>
          <Toast message={toastMessage} tone="info" onClose={() => setToastMessage(null)} />
        </div>
      )}

      {/* Dynamic Greeting & Profile Summary */}
      <Card 
        variant="outlined" 
        padding="lg"
        style={{
          background: 'linear-gradient(135deg, var(--color-bg-primary-weak) 0%, #ffffff 100%)',
          borderLeft: '4px solid var(--color-primary)'
        }}
      >
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px' }}>
          <div style={{ display: 'flex', gap: '16px', alignItems: 'center' }}>
            <div style={{ fontSize: '48px' }}>{profile.avatar}</div>
            <div>
              <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
                {getGreeting()},
              </span>
              <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: '4px 0' }}>
                {profile.name}
              </h2>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-primary)', fontWeight: 'bold' }}>
                ⭐ {profile.tier} TIER MEMBER
              </span>
            </div>
          </div>

          <div style={{ textAlign: 'right', minWidth: '180px' }}>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block' }}>
              LOYALTY BALANCE
            </span>
            <strong style={{ fontSize: 'var(--text-body-lg)', color: 'var(--color-text-primary)', display: 'block', margin: '4px 0' }}>
              {profile.points} Points
            </strong>
            <div style={{ width: '100%', height: '6px', background: '#cbd5e1', borderRadius: '3px', overflow: 'hidden' }}>
              <div style={{ width: '80%', height: '100%', background: 'var(--color-primary)' }} />
            </div>
            <span style={{ fontSize: '10px', color: 'var(--color-text-secondary)', display: 'block', marginTop: '4px' }}>
              550 points to next reward voucher
            </span>
          </div>
        </div>
      </Card>

      {/* Main split grid */}
      <Grid columns="2fr 1.1fr" gap="24px" style={{ alignItems: 'start' }}>
        
        {/* Left Area: Resume Study, active orders status, activities preview */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          
          {/* Resume Learning Course card */}
          <Card variant="outlined" padding="lg">
            <span style={{ fontSize: '10px', color: 'var(--color-primary)', fontWeight: 'bold', textTransform: 'uppercase' }}>
              CONTINUE LEARNING
            </span>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px', marginTop: '8px' }}>
              <div>
                <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
                  Sterile Laboratory Inoculations
                </h3>
                <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: '4px 0 12px' }}>
                  Lesson 3: Pouring Malt Extract Agar Plates (MEA)
                </p>
                <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                  <div style={{ width: '160px', height: '8px', background: '#cbd5e1', borderRadius: '4px', overflow: 'hidden' }}>
                    <div style={{ width: '60%', height: '100%', background: 'var(--color-primary)' }} />
                  </div>
                  <span style={{ fontSize: '11px', color: 'var(--color-text-primary)', fontWeight: 'bold' }}>
                    60% Done
                  </span>
                </div>
              </div>

              <button
                type="button"
                className="cw-btn cw-btn--primary"
                onClick={() => navigate('/dashboard/training/classroom/course-sterile-techniques')}
                style={{ display: 'flex', alignItems: 'center', gap: '6px' }}
              >
                <Icon name="play" size={14} color="currentColor" />
                Resume Lecture
              </button>
            </div>
          </Card>

          {/* Active order card timeline summary */}
          <Card variant="outlined" padding="lg">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
              <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
                Active Order Status
              </h3>
              <button 
                type="button" 
                className="cw-btn cw-btn--outlined cw-btn--xs"
                onClick={() => navigate('/dashboard/orders')}
              >
                All Orders
              </button>
            </div>

            <div style={{ display: 'flex', gap: '16px', alignItems: 'center' }}>
              <div style={{ fontSize: '28px', color: 'var(--color-primary)' }}>📦</div>
              <div style={{ flex: 1 }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap', fontSize: 'var(--text-caption)' }}>
                  <strong style={{ color: 'var(--color-text-primary)' }}>ORD-2026-8842</strong>
                  <span style={{ color: 'var(--color-primary)', fontWeight: 'bold' }}>IN TRANSIT</span>
                </div>
                <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
                  En route via Delhivery · Estimate Delivery: Tomorrow, 4:00 PM
                </p>
              </div>
            </div>
          </Card>

          {/* Product Recommendations Shelf */}
          <div>
            <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', marginBottom: '12px' }}>
              Trending Cultivars & Equipment
            </h3>
            <Grid columns="repeat(auto-fit, minmax(200px, 1fr))" gap="16px">
              {SUGGESTIONS.map((item) => (
                <Card 
                  key={item.id} 
                  variant="outlined" 
                  padding="md"
                  style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between', height: '100%' }}
                >
                  <div>
                    <div style={{ fontSize: '32px', marginBottom: '8px' }}>{item.image}</div>
                    <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '4px 0 2px' }}>
                      {item.name}
                    </h4>
                    <p style={{ fontSize: '11px', color: 'var(--color-text-secondary)', margin: 0, lineHeight: '1.3' }}>
                      {item.description}
                    </p>
                  </div>
                  <div style={{ borderTop: '1px solid var(--color-border-default)', paddingTop: '8px', marginTop: '12px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <strong style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>{item.price}</strong>
                    <button
                      type="button"
                      className="cw-btn cw-btn--primary cw-btn--xs"
                      onClick={() => {
                        setToastMessage(`Added "${item.name}" to cart!`);
                        setTimeout(() => setToastMessage(null), 3000);
                      }}
                      style={{ fontSize: '10px' }}
                    >
                      Buy Now
                    </button>
                  </div>
                </Card>
              ))}
            </Grid>
          </div>

        </div>

        {/* Right Area: quick status metrics overview, latest activities */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          
          {/* Quick Metrics */}
          <Card variant="outlined" padding="md">
            <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-sm)' }}>
              Grower Checklist
            </h4>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
              {[
                { label: 'Profile verification', checked: true },
                { label: 'Completed Sterile course', checked: true },
                { label: 'Log tax invoicing format', checked: true },
                { label: 'Attend live webinar class', checked: false }
              ].map((item, idx) => (
                <div key={idx} style={{ display: 'flex', alignItems: 'center', gap: '10px', fontSize: 'var(--text-caption)' }}>
                  <span style={{ color: item.checked ? 'var(--color-text-success, #166534)' : '#cbd5e1', fontSize: '16px' }}>
                    {item.checked ? '✓' : '○'}
                  </span>
                  <span style={{ color: 'var(--color-text-primary)', textDecoration: item.checked ? 'line-through' : 'none' }}>
                    {item.label}
                  </span>
                </div>
              ))}
            </div>
          </Card>

          {/* Activity timeline feed preview */}
          <Card variant="outlined" padding="md">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
              <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
                Recent Activities
              </h4>
              <button 
                type="button" 
                className="cw-btn cw-btn--outlined cw-btn--xs"
                onClick={() => navigate('/dashboard/activity')}
                style={{ fontSize: '9px' }}
              >
                Full Feed
              </button>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
              {ACTIVITY_LOG.slice(0, 3).map((act) => (
                <div key={act.id} style={{ display: 'flex', gap: '10px', alignItems: 'flex-start', borderBottom: '1px solid var(--color-border-default)', paddingBottom: '8px' }}>
                  <div style={{ color: 'var(--color-primary)', marginTop: '2px' }}>
                    <Icon name={act.icon} size={12} color="currentColor" />
                  </div>
                  <div>
                    <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-primary)', margin: 0, lineHeight: '1.3' }}>
                      {act.message}
                    </p>
                    <span style={{ fontSize: '9px', color: 'var(--color-text-secondary)' }}>
                      {act.date}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          </Card>

        </div>

      </Grid>

      {/* Floating AI Digital Assistant Widget */}
      <div 
        style={{ 
          position: 'fixed', 
          bottom: '24px', 
          right: '24px', 
          zIndex: 999,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'flex-end',
          gap: '12px'
        }}
      >
        {isAssistantOpen && (
          <Card 
            variant="outlined" 
            padding="md"
            style={{ 
              width: '320px', 
              maxHeight: '400px', 
              boxShadow: '0 10px 25px rgba(0,0,0,0.1)', 
              background: '#fff',
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'space-between'
            }}
          >
            {/* Assistant Header */}
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: '1px solid var(--color-border-default)', paddingBottom: '8px', marginBottom: '12px' }}>
              <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'bold', color: 'var(--color-text-primary)' }}>
                🤖 AI grow Mentor
              </span>
              <button 
                type="button" 
                onClick={() => setIsAssistantOpen(false)}
                style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--color-text-secondary)' }}
              >
                ✕
              </button>
            </div>

            {/* Chat Box logs */}
            <div style={{ flex: 1, overflowY: 'auto', display: 'flex', flexDirection: 'column', gap: '10px', maxHeight: '200px', marginBottom: '12px', paddingRight: '4px' }}>
              {chatLog.map((log, i) => (
                <div 
                  key={i} 
                  style={{
                    alignSelf: log.sender === 'user' ? 'flex-end' : 'flex-start',
                    background: log.sender === 'user' ? 'var(--color-bg-primary-default)' : '#f1f5f9',
                    color: log.sender === 'user' ? '#fff' : 'var(--color-text-primary)',
                    padding: '8px 12px',
                    borderRadius: '8px',
                    fontSize: '11px',
                    maxWidth: '85%',
                    lineHeight: '1.4'
                  }}
                >
                  {log.text}
                </div>
              ))}
            </div>

            {/* Quick Actions / suggestions stubs */}
            <div style={{ display: 'flex', flexDirection: 'column', gap: '6px' }}>
              <span style={{ fontSize: '9px', color: 'var(--color-text-secondary)', fontWeight: 'bold' }}>SUGGESTED QUESTIONS</span>
              {assistantSuggestions.map((item, idx) => (
                <button
                  key={idx}
                  type="button"
                  onClick={() => handleTriggerSuggestion(item.q, item.a)}
                  style={{
                    textAlign: 'left',
                    background: '#f8fafc',
                    border: '1px solid var(--color-border-default)',
                    borderRadius: '4px',
                    padding: '6px 8px',
                    fontSize: '10px',
                    color: 'var(--color-text-primary)',
                    cursor: 'pointer',
                    outline: 'none'
                  }}
                >
                  {item.q}
                </button>
              ))}
            </div>
          </Card>
        )}

        {/* Launcher button */}
        <button
          type="button"
          onClick={() => setIsAssistantOpen(!isAssistantOpen)}
          style={{
            width: '56px',
            height: '56px',
            borderRadius: '50%',
            background: 'var(--color-bg-primary-default)',
            color: '#fff',
            border: 'none',
            cursor: 'pointer',
            boxShadow: '0 4px 12px rgba(0,0,0,0.15)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            fontSize: '24px',
            outline: 'none'
          }}
        >
          💬
        </button>
      </div>

    </div>
  );
};
export default PersonalizedHome;
