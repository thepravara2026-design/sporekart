import { useAlumni } from '../state/AlumniContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { MetricCard } from '../components/MetricCard';
import { ProgressRing } from '../components/ProgressRing';

export default function PlacementAnalytics() {
  const { analytics, dashboard } = useAlumni();

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Placement Analytics</h1>
        <SharedFilters currentPage="alumni/analytics" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Total Alumni" value={analytics.totalAlumni} icon="👥" />
        <MetricCard label="Active" value={analytics.activeAlumni} icon="✅" color="#16a34a" />
        <MetricCard label="Engaged" value={analytics.engagedAlumni} icon="📊" color="#2563eb" />
        <MetricCard label="Placement Rate" value={`${analytics.placementRate}%`} icon="📈" color="#059669" />
        <MetricCard label="Avg Salary" value={analytics.averageSalary} icon="💰" color="#d97706" />
        <MetricCard label="Mentors" value={analytics.totalMentors} icon="🤝" color="#8b5cf6" />
        <MetricCard label="Contributions" value={analytics.totalContributions} icon="🎁" />
        <MetricCard label="Events" value={analytics.totalEvents} icon="📅" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(360px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget title="Placement Progress" subtitle="Overall placement rate">
          <div style={{ display: 'flex', alignItems: 'center', gap: 24, padding: 8 }}>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 8 }}>
              <ProgressRing value={dashboard.placementPercentage} size={100} strokeWidth={8} color="#059669" />
              <span style={{ fontWeight: 'var(--weight-bold)', fontSize: 'var(--text-h4)' }}>{dashboard.placementPercentage}%</span>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Placement Rate</span>
            </div>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 8, fontSize: 'var(--text-body-sm)' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', gap: 16 }}><span>Placed</span><strong>{dashboard.placed}</strong></div>
              <div style={{ display: 'flex', justifyContent: 'space-between', gap: 16 }}><span>Ready</span><strong>{dashboard.placementReady}</strong></div>
              <div style={{ display: 'flex', justifyContent: 'space-between', gap: 16 }}><span>Not Placed</span><strong style={{ color: '#dc2626' }}>{dashboard.notPlaced}</strong></div>
              <div style={{ display: 'flex', justifyContent: 'space-between', gap: 16 }}><span>Avg Package</span><strong>{dashboard.averagePackage}</strong></div>
              <div style={{ display: 'flex', justifyContent: 'space-between', gap: 16 }}><span>Highest</span><strong style={{ color: '#d97706' }}>{dashboard.highestPackage}</strong></div>
            </div>
          </div>
        </DashboardWidget>

        <DashboardWidget title="Placement Trends" subtitle="Monthly placement count">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {analytics.placementTrends.map((t, i) => (
              <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 12, fontSize: 'var(--text-body-sm)' }}>
                <span style={{ minWidth: 40, color: 'var(--color-text-secondary)' }}>{t.month}</span>
                <div style={{ flex: 1, height: 12, borderRadius: 6, background: 'var(--color-bg-skeleton-base)', overflow: 'hidden' }}>
                  <div style={{ height: '100%', borderRadius: 6, background: '#2563eb', width: `${Math.min(100, (t.placedCount / Math.max(...analytics.placementTrends.map((x) => x.placedCount))) * 100)}%` }} />
                </div>
                <span style={{ fontWeight: 'var(--weight-semibold)', minWidth: 24, textAlign: 'right' }}>{t.placedCount}</span>
              </div>
            ))}
          </div>
        </DashboardWidget>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(360px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget title="Top Recruiters" subtitle="Companies by hires">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
            {analytics.topRecruiters.map((r, i) => (
              <div key={i} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', fontSize: 'var(--text-body-sm)' }}>
                <span style={{ color: 'var(--color-text-secondary)' }}>{r.companyName}</span>
                <span style={{ fontWeight: 'var(--weight-semibold)' }}>{r.hires} hires</span>
              </div>
            ))}
          </div>
        </DashboardWidget>

        <DashboardWidget title="Alumni Engagement Trends" subtitle="Monthly activity">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {analytics.engagementTrends.map((t, i) => (
              <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 12, fontSize: 'var(--text-body-sm)' }}>
                <span style={{ minWidth: 40, color: 'var(--color-text-secondary)' }}>{t.month}</span>
                <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 2 }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)' }}>
                    <span>Active</span><span>{t.activeCount}</span>
                  </div>
                  <div style={{ height: 6, borderRadius: 3, background: 'var(--color-bg-skeleton-base)', overflow: 'hidden' }}>
                    <div style={{ height: '100%', borderRadius: 3, background: '#16a34a', width: `${Math.min(100, (t.activeCount / Math.max(...analytics.engagementTrends.map((x) => x.activeCount))) * 100)}%` }} />
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)' }}>
                    <span>Contributions</span><span>{t.contributionCount}</span>
                  </div>
                  <div style={{ height: 6, borderRadius: 3, background: 'var(--color-bg-skeleton-base)', overflow: 'hidden' }}>
                    <div style={{ height: '100%', borderRadius: 3, background: '#8b5cf6', width: `${Math.min(100, (t.contributionCount / Math.max(...analytics.engagementTrends.map((x) => x.contributionCount))) * 100)}%` }} />
                  </div>
                </div>
              </div>
            ))}
          </div>
        </DashboardWidget>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget title="Alumni by Batch" subtitle="Distribution across batches">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
            {analytics.alumniByBatch.map((b, i) => (
              <div key={i} style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)' }}>
                <span>{b.batchName}</span>
                <span style={{ fontWeight: 'var(--weight-semibold)' }}>{b.count}</span>
              </div>
            ))}
          </div>
        </DashboardWidget>
        <DashboardWidget title="Alumni by Industry" subtitle="Industry distribution">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
            {analytics.alumniByIndustry.map((ind, i) => (
              <div key={i} style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)' }}>
                <span>{ind.industry}</span>
                <span style={{ fontWeight: 'var(--weight-semibold)' }}>{ind.count}</span>
              </div>
            ))}
          </div>
        </DashboardWidget>
        <DashboardWidget title="Alumni by Location" subtitle="Geographic distribution">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
            {analytics.alumniByLocation.map((loc, i) => (
              <div key={i} style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)' }}>
                <span>{loc.location}</span>
                <span style={{ fontWeight: 'var(--weight-semibold)' }}>{loc.count}</span>
              </div>
            ))}
          </div>
        </DashboardWidget>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget title="Placement Summary" subtitle="Key metrics">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8, fontSize: 'var(--text-body-sm)' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}><span>Active Drives</span><strong>{dashboard.activeDrives}</strong></div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}><span>Partner Companies</span><strong>{dashboard.partnerCompanies}</strong></div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}><span>Active Job Openings</span><strong>{dashboard.activeJobOpenings}</strong></div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}><span>Total Mentors</span><strong>{analytics.totalMentors}</strong></div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}><span>Active Mentorships</span><strong>{analytics.activeMentorships}</strong></div>
          </div>
        </DashboardWidget>
        <DashboardWidget title="Alumni Overview" subtitle="Engagement metrics">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8, fontSize: 'var(--text-body-sm)' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}><span>Total Alumni</span><strong>{analytics.totalAlumni}</strong></div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}><span>Active</span><strong style={{ color: '#16a34a' }}>{analytics.activeAlumni}</strong></div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}><span>Engaged</span><strong style={{ color: '#2563eb' }}>{analytics.engagedAlumni}</strong></div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}><span>Total Donations</span><strong>{analytics.totalDonations}</strong></div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}><span>Event Registrations</span><strong>{analytics.totalEventRegistrations}</strong></div>
          </div>
        </DashboardWidget>
      </div>
    </main>
  );
}
