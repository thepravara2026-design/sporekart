import React, { useState, useCallback, useMemo } from 'react';
import { MOCK_CERTIFICATIONS } from '../mock/mockCertification';
import type { CertificationLevel } from '../types';

const levels: { key: CertificationLevel | 'all'; label: string }[] = [
  { key: 'all', label: 'All' },
  { key: 'enterprise', label: 'Enterprise' },
  { key: 'gold', label: 'Gold' },
  { key: 'silver', label: 'Silver' },
  { key: 'bronze', label: 'Bronze' },
  { key: 'marketplace_ready', label: 'Marketplace Ready' },
  { key: 'export_ready', label: 'Export Ready' },
];

const levelColors: Record<string, string> = {
  enterprise: '#7c3aed',
  gold: '#eab308',
  silver: '#94a3b8',
  bronze: '#d97706',
  marketplace_ready: '#059669',
  export_ready: '#2563eb',
  none: '#64748b',
};

export const CertificationDashboard: React.FC = React.memo(() => {
  const [filter, setFilter] = useState<CertificationLevel | 'all'>('all');
  const handleFilter = useCallback((f: CertificationLevel | 'all') => setFilter(f), []);

  const filtered = useMemo(() => {
    return filter === 'all' ? MOCK_CERTIFICATIONS : MOCK_CERTIFICATIONS.filter((c) => c.level === filter);
  }, [filter]);

  const stats = useMemo(() => {
    const byLevel: Record<string, number> = {};
    MOCK_CERTIFICATIONS.forEach((c) => { byLevel[c.level] = (byLevel[c.level] || 0) + 1; });
    return { total: MOCK_CERTIFICATIONS.length, byLevel };
  }, []);

  return (
    <div style={{ padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 20 }} aria-label="Certification dashboard">
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Certification Dashboard</h2>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 12 }}>
        {Object.entries(stats.byLevel).map(([level, count]) => (
          <div key={level} style={{ padding: 14, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', gap: 4 }}>
            <span style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', textTransform: 'capitalize' }}>{level}</span>
            <span style={{ fontSize: 'var(--text-h3)', fontWeight: 700, color: levelColors[level] ?? 'var(--color-text-primary)' }}>{count}</span>
          </div>
        ))}
        <div style={{ padding: 14, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', gap: 4 }}>
          <span style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>Total Certified</span>
          <span style={{ fontSize: 'var(--text-h3)', fontWeight: 700, color: 'var(--color-text-primary)' }}>{stats.total}</span>
        </div>
      </div>

      <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }} role="tablist" aria-label="Filter by certification level">
        {levels.map((l) => (
          <button
            key={l.key}
            role="tab"
            aria-selected={filter === l.key}
            onClick={() => handleFilter(l.key)}
            style={{
              padding: '6px 14px', borderRadius: 'var(--radius-sm)', border: `1px solid ${filter === l.key ? 'var(--color-accent-blue)' : 'var(--color-border)'}`,
              background: filter === l.key ? 'var(--color-accent-blue)' : 'var(--color-bg-surface-default)',
              color: filter === l.key ? '#fff' : 'var(--color-text-secondary)',
              fontSize: 'var(--text-body-xs)', fontWeight: filter === l.key ? 600 : 400, cursor: 'pointer', transition: 'all 0.15s',
            }}
          >
            {l.label}
          </button>
        ))}
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: 16 }}>
        {filtered.map((cert) => (
          <article key={cert.id} style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', gap: 10 }} aria-label={`${cert.level} certification for ${cert.productName}`}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
                <span style={{ fontSize: 28, lineHeight: 1 }} aria-hidden="true">{cert.badge}</span>
                <div>
                  <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{cert.productName}</div>
                  <span style={{ display: 'inline-flex', padding: '1px 6px', borderRadius: 'var(--radius-xs)', fontSize: 'var(--text-caption)', fontWeight: 600, color: '#fff', background: levelColors[cert.level] ?? 'var(--color-text-secondary)', textTransform: 'capitalize' }}>
                    {cert.level.replace('_', ' ')}
                  </span>
                </div>
              </div>
            </div>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '6px 12px', fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>
              <span>Completed</span><span style={{ color: 'var(--color-text-primary)', fontWeight: 500 }}>{cert.completionDate}</span>
              <span>Valid Until</span><span style={{ color: 'var(--color-text-primary)', fontWeight: 500 }}>{cert.validUntil}</span>
              <span>Issued By</span><span style={{ color: 'var(--color-text-primary)', fontWeight: 500 }}>{cert.issuedBy}</span>
            </div>
            <p style={{ margin: 0, fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', lineHeight: 1.5 }}>{cert.notes}</p>
          </article>
        ))}
      </div>

      {filtered.length === 0 && (
        <div style={{ textAlign: 'center', padding: '40px 0', color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body-sm)' }}>
          No certifications found for this level.
        </div>
      )}
    </div>
  );
});
