import { PublicContentContainer } from '../PublicContentContainer';

export interface FutureStatisticsProps {
  stats?: { value: string; label: string }[];
}

export function FutureStatistics({
  stats = [
    { value: '12k+', label: 'Growers supported' },
    { value: '98%', label: 'Successful first grows' },
    { value: '40+', label: 'Cultivation strains' },
    { value: '24/7', label: 'Grower support' },
  ],
}: FutureStatisticsProps) {
  return (
    <section className="sk-public-statistics" aria-label="Key statistics" style={{ backgroundColor: 'var(--color-bg-surface-default, #ffffff)' }}>
      <PublicContentContainer>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(160px, 1fr))', gap: 'var(--space-5, 24px)', textAlign: 'center' }}>
          {stats.map((stat) => (
            <div key={stat.label}>
              <div style={{ fontSize: 'var(--text-title-lg, 26px)', fontWeight: 700, color: 'var(--color-text-accent, #1d4ed8)' }}>{stat.value}</div>
              <div style={{ marginTop: 'var(--space-1, 4px)', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)' }}>{stat.label}</div>
            </div>
          ))}
        </div>
      </PublicContentContainer>
    </section>
  );
}
