import React from 'react';
import { Card } from '../../../design-system/components/composite/Card';
import { Grid } from '../../../design-system/components/layout/Grid';

export const CustomerInsights: React.FC = () => {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      
      {/* Title */}
      <div>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
          Grower Insights & Account Health
        </h2>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
          Personal statistics compiled from orders history, cleanroom checks, and academy lesson completions.
        </p>
      </div>

      {/* Account Verification & Checklist */}
      <Grid columns="1.2fr 2fr" gap="24px" style={{ alignItems: 'start' }}>
        {/* Left: Verification Card */}
        <Card variant="outlined" padding="lg" style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          <div>
            <span style={{ 
              fontSize: '10px', 
              fontWeight: 'bold', 
              color: 'var(--color-text-success, #166534)', 
              background: '#dcfce7', 
              padding: '2px 8px', 
              borderRadius: 'var(--radius-sm)' 
            }}>
              ✓ ACCOUNT VERIFIED
            </span>
          </div>

          <div>
            <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: '4px 0' }}>
              Jane Doe
            </h3>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
              Client ID: SPK-2026-9921 · Tier: Master Grower
            </span>
          </div>

          <div style={{ borderTop: '1px solid var(--color-border-default)', paddingTop: '12px' }}>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block', marginBottom: '6px' }}>
              Profile Completion: <strong>85%</strong>
            </span>
            <div style={{ width: '100%', height: '8px', background: '#cbd5e1', borderRadius: '4px', overflow: 'hidden' }}>
              <div style={{ width: '85%', height: '100%', background: 'var(--color-primary)' }} />
            </div>
          </div>
        </Card>

        {/* Right: Stats Overview */}
        <Card variant="outlined" padding="lg">
          <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: '0 0 16px' }}>
            Workspace Statistics
          </h3>

          <Grid columns="1fr 1fr" gap="16px">
            {[
              { label: 'Sterile Loop Transfers', val: '98.4% Success', desc: 'Auto-sterile hood average' },
              { label: 'Courses Completed', val: '2 of 3 Modules', desc: 'Academy point progressions' },
              { label: 'Grain Spawn Purchased', val: '65 Kilograms', desc: 'Premium Pink Oyster bags' },
              { label: 'Support Tickets Resolved', val: '1 Ticket logged', desc: 'Delhivery logistics query' },
            ].map((stat, i) => (
              <div key={i} style={{ borderBottom: '1px solid var(--color-border-default)', paddingBottom: '12px' }}>
                <span style={{ fontSize: '10px', color: 'var(--color-text-secondary)', display: 'block' }}>
                  {stat.label.toUpperCase()}
                </span>
                <strong style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', display: 'block', margin: '2px 0' }}>
                  {stat.val}
                </strong>
                <span style={{ fontSize: '10px', color: 'var(--color-text-secondary)' }}>
                  {stat.desc}
                </span>
              </div>
            ))}
          </Grid>
        </Card>
      </Grid>

      {/* AI Smart Insights */}
      <div>
        <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', marginBottom: '16px' }}>
          Smart AI Grow Recommendations
        </h3>
        
        <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          {[
            {
              title: 'Inoculation loop cooling speed is 12% faster than average',
              desc: 'Your loop cooldown times are optimal. However, ensure scalpel loops do not stay red-hot longer than 5 seconds to prevent spore denaturation.',
              tag: 'LAB OPTIMIZATION',
              tone: 'var(--color-primary)'
            },
            {
              title: 'Casing Substrate Moisture check at field capacity limits',
              desc: 'Squeeze tests show that coco coir casing moisture is steady at 61%. Mist less frequently to avoid mold spawn drownings.',
              tag: 'CULTIVATION TIP',
              tone: '#eab308'
            },
            {
              title: 'GST tax details config verified',
              desc: 'Your corporate GSTIN accounts details match billing verification registers. Future invoices are dispatched automatically.',
              tag: 'BILLING AUDIT',
              tone: 'var(--color-text-success, #166534)'
            }
          ].map((insight, i) => (
            <Card 
              key={i} 
              variant="outlined" 
              padding="md"
              style={{
                borderLeft: `4px solid ${insight.tone}`
              }}
            >
              <span style={{ fontSize: '9px', fontWeight: 'bold', color: insight.tone }}>{insight.tag}</span>
              <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '6px 0 2px' }}>
                {insight.title}
              </h4>
              <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 0, lineHeight: '1.4' }}>
                {insight.desc}
              </p>
            </Card>
          ))}
        </div>
      </div>

    </div>
  );
};
export default CustomerInsights;
