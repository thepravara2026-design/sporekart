import { memo } from 'react';
import { useIntelligenceData } from '../hooks';
import { InsightCard } from '../components/InsightCard';

export const InsightsPage = memo(function InsightsPage() {
  const { insights, loading } = useIntelligenceData();
  if (loading) return <div>Loading...</div>;
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2, 20px)', fontWeight: 700 }}>Insights</h2>
      <p style={{ margin: 0, color: 'var(--color-text-secondary)' }}>Actionable intelligence and recommendations based on inventory data</p>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
        {insights.map((insight) => <InsightCard key={insight.id} insight={insight} />)}
      </div>
    </div>
  );
});
