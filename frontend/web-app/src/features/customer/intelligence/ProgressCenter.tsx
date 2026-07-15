import React from 'react';
import { Card } from '../../../design-system/components/composite/Card';

export const ProgressCenter: React.FC = () => {
  const steps = [
    { label: 'Account Registration', desc: 'Secure profile created and authenticated.', done: true },
    { label: 'Lab Verification', desc: 'Cleanroom layout profiles established.', done: true },
    { label: 'First Cultivar Inoculation', desc: 'Pink Oyster grain bags inoculated at field capacity.', done: true },
    { label: 'Syllabus Accreditation', desc: 'Sterile Techniques course module completed 100%.', done: true },
    { label: 'Commercial Production Scale', desc: 'Order total volumes exceeds 50kg grain spawn.', done: false },
  ];

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', maxWidth: '650px', margin: '0 auto' }}>
      
      {/* Title */}
      <div>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
          Customer Journey Progress
        </h2>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
          Milestone tracker mapping your journey from beginner hobbyist to verified commercial grower.
        </p>
      </div>

      {/* Progress timeline bar */}
      <Card variant="outlined" padding="lg">
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          {steps.map((step, idx) => (
            <div key={idx} style={{ display: 'flex', gap: '16px', alignItems: 'flex-start' }}>
              <div 
                style={{ 
                  width: '28px', 
                  height: '28px', 
                  borderRadius: '50%', 
                  background: step.done ? 'var(--color-bg-primary-default)' : '#cbd5e1', 
                  color: '#fff', 
                  display: 'flex', 
                  alignItems: 'center', 
                  justifyContent: 'center',
                  fontSize: '12px',
                  fontWeight: 'bold'
                }}
              >
                {step.done ? '✓' : idx + 1}
              </div>
              <div style={{ borderBottom: idx < steps.length - 1 ? '1px solid var(--color-border-default)' : 'none', paddingBottom: '16px', flex: 1 }}>
                <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
                  {step.label}
                </h4>
                <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: '4px 0 0', lineHeight: '1.4' }}>
                  {step.desc}
                </p>
              </div>
            </div>
          ))}
        </div>
      </Card>

    </div>
  );
};
export default ProgressCenter;
