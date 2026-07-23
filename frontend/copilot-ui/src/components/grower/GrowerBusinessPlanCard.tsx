import React from 'react';
import type { BusinessPlan } from './types/grower';

const cardStyle: React.CSSProperties = {
  padding: '16px',
  background: 'var(--cp-surface, #1a1a2e)',
  borderRadius: '12px',
  border: '1px solid var(--cp-border, #2a2a4a)',
};

const speciesStyle: React.CSSProperties = {
  fontSize: '16px',
  fontWeight: 600,
  color: 'var(--cp-text, #e0e0e0)',
  margin: '0 0 12px',
};

const metricsRowStyle: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: '1fr 1fr',
  gap: '8px',
  marginBottom: 16,
};

const metricBoxStyle: React.CSSProperties = {
  padding: '10px',
  background: 'var(--cp-surface-alt, #16213e)',
  borderRadius: '8px',
  textAlign: 'center',
};

const metricValueStyle: React.CSSProperties = {
  fontSize: '18px',
  fontWeight: 700,
  color: 'var(--cp-accent, #4ade80)',
};

const metricLabelStyle: React.CSSProperties = {
  fontSize: '10px',
  textTransform: 'uppercase' as const,
  letterSpacing: '0.5px',
  color: 'var(--cp-text-dim, #888)',
  marginTop: 2,
};

const sectionTitleStyle: React.CSSProperties = {
  fontSize: '12px',
  fontWeight: 600,
  color: 'var(--cp-text, #e0e0e0)',
  marginBottom: 6,
};

const listStyle: React.CSSProperties = {
  margin: '0 0 12px',
  paddingLeft: 16,
  fontSize: '12px',
  color: 'var(--cp-text-dim, #aaa)',
  lineHeight: 1.6,
};

const roiHighlightStyle: React.CSSProperties = {
  fontSize: '24px',
  fontWeight: 700,
  color: 'var(--cp-accent, #4ade80)',
};

interface GrowerBusinessPlanCardProps {
  plan: BusinessPlan;
}

export default function GrowerBusinessPlanCard({ plan }: GrowerBusinessPlanCardProps) {
  return (
    <div style={cardStyle}>
      <h4 style={speciesStyle}>{plan.speciesName} — Business Plan</h4>

      <div style={metricsRowStyle}>
        <div style={metricBoxStyle}>
          <div style={metricValueStyle}>₹{plan.investmentAmount.toLocaleString()}</div>
          <div style={metricLabelStyle}>Investment</div>
        </div>
        <div style={metricBoxStyle}>
          <div style={metricValueStyle}>₹{plan.expectedRevenue.toLocaleString()}</div>
          <div style={metricLabelStyle}>Expected Revenue</div>
        </div>
        <div style={metricBoxStyle}>
          <div style={roiHighlightStyle}>{plan.expectedRoi}%</div>
          <div style={metricLabelStyle}>ROI</div>
        </div>
        <div style={metricBoxStyle}>
          <div style={metricValueStyle}>{plan.cycleDurationDays} days</div>
          <div style={metricLabelStyle}>Cycle Duration</div>
        </div>
      </div>

      {plan.marketInsights.length > 0 && (
        <>
          <div style={sectionTitleStyle}>📊 Market Insights</div>
          <ul style={listStyle}>
            {plan.marketInsights.map(m => <li key={m}>{m}</li>)}
          </ul>
        </>
      )}

      {plan.pricingSuggestions.length > 0 && (
        <>
          <div style={sectionTitleStyle}>💰 Pricing Suggestions</div>
          <ul style={listStyle}>
            {plan.pricingSuggestions.map(p => <li key={p}>{p}</li>)}
          </ul>
        </>
      )}
    </div>
  );
}
