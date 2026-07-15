import React from 'react';
import type { CommercialRule } from '../types';

interface CommercialRulesPanelProps {
  rules: CommercialRule[];
}

const sectionStyle: React.CSSProperties = {
  padding: 'var(--space-component-gap)',
  display: 'flex',
  flexDirection: 'column',
  gap: 16,
};

const h2Style: React.CSSProperties = {
  margin: 0,
  fontSize: 'var(--text-h2)',
  color: 'var(--color-text-primary)',
};

const gridStyle: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))',
  gap: 12,
};

const cardStyle: React.CSSProperties = {
  padding: 16,
  borderRadius: 'var(--radius-md)',
  border: '1px solid var(--color-border)',
  background: 'var(--color-bg-surface-default)',
};

const cardTitle: React.CSSProperties = {
  fontSize: 'var(--text-body-sm)',
  fontWeight: 600,
  color: 'var(--color-text-primary)',
};

const typeBadge: React.CSSProperties = {
  padding: '2px 8px',
  borderRadius: 10,
  fontSize: 'var(--text-body-xs)',
  fontWeight: 600,
  marginLeft: 8,
};

const detailRow: React.CSSProperties = {
  display: 'flex',
  justifyContent: 'space-between',
  padding: '4px 0',
  fontSize: 'var(--text-body-xs)',
  borderBottom: '1px solid var(--color-border)',
};

const ruleTypes: Record<string, { label: string; color: string }> = {
  min_price: { label: 'Min Price', color: 'var(--color-accent-blue)' },
  max_price: { label: 'Max Price', color: 'var(--color-accent-orange)' },
  suggested_price: { label: 'Suggested', color: 'var(--color-accent-green)' },
  margin: { label: 'Margin', color: 'var(--color-accent-purple)' },
  cost: { label: 'Cost', color: 'var(--color-accent-yellow)' },
  approval: { label: 'Approval', color: 'var(--color-accent-red)' },
};

export const CommercialRulesPanel: React.FC<CommercialRulesPanelProps> = React.memo(({ rules }) => {
  return (
    <div style={sectionStyle}>
      <h2 style={h2Style}>Commercial Rules</h2>
      <div style={gridStyle}>
        {rules.length === 0 && (
          <div style={{ gridColumn: '1 / -1', textAlign: 'center', padding: 48, color: 'var(--color-text-tertiary)' }}>
            No commercial rules configured
          </div>
        )}
        {rules.map((rule) => {
          const type = ruleTypes[rule.type] ?? { label: rule.type, color: 'var(--color-accent-gray)' };
          return (
            <div key={rule.id} style={cardStyle}>
              <div style={{ display: 'flex', alignItems: 'center', marginBottom: 8 }}>
                <span style={cardTitle}>{rule.name}</span>
                <span style={{ ...typeBadge, background: `${type.color}20`, color: type.color }}>{type.label}</span>
              </div>
              <div style={detailRow}>
                <span style={{ color: 'var(--color-text-tertiary)' }}>Value</span>
                <span style={{ fontWeight: 500 }}>{rule.type === 'approval' ? 'Required' : `₹${rule.value}`}</span>
              </div>
              <div style={detailRow}>
                <span style={{ color: 'var(--color-text-tertiary)' }}>Applies To</span>
                <span>{rule.applicableTo}</span>
              </div>
              <div style={detailRow}>
                <span style={{ color: 'var(--color-text-tertiary)' }}>Priority</span>
                <span>{rule.priority}</span>
              </div>
              <div style={detailRow}>
                <span style={{ color: 'var(--color-text-tertiary)' }}>Status</span>
                <span style={{ color: rule.status === 'active' ? 'var(--color-accent-green)' : 'var(--color-accent-red)' }}>
                  {rule.status}
                </span>
              </div>
              {rule.notes && (
                <div style={{ marginTop: 8, fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', fontStyle: 'italic' }}>
                  {rule.notes}
                </div>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
});
