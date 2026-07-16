import { memo } from 'react';

interface ScoreCardProps {
  category: string;
  score: number;
  maxScore?: number;
}

export const ScoreCard = memo(function ScoreCard({ category, score, maxScore = 100 }: ScoreCardProps) {
  const pct = Math.round((score / maxScore) * 100);
  const color = pct >= 80 ? '#16a34a' : pct >= 60 ? '#2563eb' : pct >= 40 ? '#ca8a04' : '#dc2626';
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 4, padding: '8px 0' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)' }}>
        <span style={{ color: 'var(--color-text-secondary)' }}>{category}</span>
        <span style={{ fontWeight: 'var(--weight-semibold)', color }}>{score}/{maxScore}</span>
      </div>
      <div style={{ height: 6, background: 'var(--color-bg-skeleton-base)', borderRadius: 3, overflow: 'hidden' }}>
        <div style={{ width: `${pct}%`, height: '100%', background: color, borderRadius: 3, transition: 'width 0.3s' }} />
      </div>
    </div>
  );
});
