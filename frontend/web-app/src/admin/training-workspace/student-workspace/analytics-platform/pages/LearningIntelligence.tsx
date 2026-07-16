import { useAnalytics } from '../state/AnalyticsContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { ProgressRing } from '../components/ProgressRing';
import { LineChart } from '../components/LineChart';
import { PieChart } from '../components/PieChart';
import { RadarChart } from '../components/RadarChart';
import { ScoreCard } from '../components/ScoreCard';

export default function LearningIntelligence() {
  const { intelligence } = useAnalytics();

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Learning Intelligence</h1>
        <SharedFilters currentPage="analytics/learning-intelligence" />
      </div>

      {/* Learning Health */}
      <DashboardWidget title="Learning Health" subtitle="System-wide health indicators">
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
          {intelligence.learningHealth.map((h) => (
            <div key={h.metric} style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 4 }}>
              <span style={{
                width: 40, height: 40, borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center',
                background: h.status === 'healthy' ? '#16a34a' : h.status === 'warning' ? '#ca8a04' : '#dc2626',
                color: '#fff', fontSize: 18, fontWeight: 'var(--weight-bold)',
              }}>{h.value}%</span>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', textAlign: 'center' }}>{h.metric}</span>
            </div>
          ))}
        </div>
      </DashboardWidget>

      {/* Charts Row */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {/* Competency Analysis */}
        <DashboardWidget title="Competency Analysis" subtitle="Average competency levels">
          <RadarChart data={intelligence.competencyAnalysis} size={180} />
        </DashboardWidget>

        {/* Achievement Distribution */}
        <DashboardWidget title="Achievement Distribution" subtitle="By achievement type">
          <PieChart
            data={intelligence.achievementAnalysis.map((a) => ({ label: a.type, value: a.count }))}
            size={130}
            innerRadius={25}
          />
        </DashboardWidget>
      </div>

      {/* Forecasts */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(360px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget title="Completion Forecast" subtitle="Predicted vs actual completions">
          <LineChart
            data={intelligence.completionForecast.map((f) => ({ label: f.month, value: f.predicted }))}
            height={100}
            color="#2563eb"
          />
        </DashboardWidget>
        <DashboardWidget title="Certification Forecast" subtitle="Predicted vs actual certifications">
          <LineChart
            data={intelligence.certificationForecast.map((f) => ({ label: f.month, value: f.predicted }))}
            height={100}
            color="#16a34a"
          />
        </DashboardWidget>
      </div>

      {/* Engagement Analysis */}
      <DashboardWidget title="Engagement Analysis" subtitle="Student engagement trends">
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 8 }}>
          {intelligence.engagementAnalysis.slice(0, 8).map((e) => (
            <div key={e.studentId} style={{ display: 'flex', alignItems: 'center', gap: 8, padding: '6px 8px', borderRadius: 'var(--radius-sm)', background: 'var(--color-bg-surface-hover)' }}>
              <ProgressRing value={e.engagementScore} size={40} strokeWidth={4} color={e.engagementScore >= 80 ? '#16a34a' : e.engagementScore >= 50 ? '#2563eb' : '#dc2626'} />
              <div>
                <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)' }}>{e.studentName}</div>
                <div style={{ fontSize: 'var(--text-caption)', color: e.trend === 'improving' ? '#16a34a' : e.trend === 'declining' ? '#dc2626' : '#6b7280' }}>
                  {e.trend === 'improving' ? '↑ Improving' : e.trend === 'declining' ? '↓ Declining' : '→ Stable'}
                </div>
              </div>
            </div>
          ))}
        </div>
      </DashboardWidget>

      {/* Performance Analysis */}
      <DashboardWidget title="Performance Analysis" subtitle="Student score breakdowns">
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 16 }}>
          {intelligence.performanceAnalysis.slice(0, 4).map((p) => (
            <div key={p.studentId} style={{ padding: '8px 0' }}>
              <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: '0 0 8px' }}>{p.studentName}</h4>
              {p.scores.map((s) => (
                <ScoreCard key={s.category} category={s.category} score={s.score} />
              ))}
            </div>
          ))}
        </div>
      </DashboardWidget>
    </main>
  );
}
