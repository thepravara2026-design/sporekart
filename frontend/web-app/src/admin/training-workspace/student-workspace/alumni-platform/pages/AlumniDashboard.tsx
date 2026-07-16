import { useMemo } from 'react';
import { useAlumni } from '../state/AlumniContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { MetricCard } from '../components/MetricCard';
import { StudentCareerCard } from '../components/StudentCareerCard';
import { PlacementDriveCard } from '../components/PlacementDriveCard';

export default function AlumniDashboard() {
  const { dashboard, studentProfiles, placementDrives } = useAlumni();

  const activeDrives = useMemo(() => placementDrives.filter((d) => d.stage !== 'completed' && d.stage !== 'cancelled'), [placementDrives]);
  const placedStudents = useMemo(() => studentProfiles.filter((p) => ['selected', 'offer-received', 'offer-accepted', 'joined'].includes(p.placementStatus)), [studentProfiles]);

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Alumni Dashboard</h1>
        <SharedFilters currentPage="alumni" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Total Students" value={dashboard.totalStudents} icon="🎓" />
        <MetricCard label="Placement Ready" value={dashboard.placementReady} icon="✅" color="#2563eb" />
        <MetricCard label="Placed" value={dashboard.placed} icon="💼" color="#16a34a" />
        <MetricCard label="Placement %" value={`${dashboard.placementPercentage}%`} icon="📊" color="#059669" />
        <MetricCard label="Active Drives" value={dashboard.activeDrives} icon="🚀" color="#8b5cf6" />
        <MetricCard label="Partners" value={dashboard.partnerCompanies} icon="🏢" />
        <MetricCard label="Avg Package" value={dashboard.averagePackage} icon="💰" color="#d97706" />
        <MetricCard label="Highest" value={dashboard.highestPackage} icon="🏆" color="#dc2626" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(360px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget title="Upcoming Placement Drives" subtitle="Active and announced drives">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {activeDrives.slice(0, 4).map((d) => <PlacementDriveCard key={d.id} drive={d} />)}
            {activeDrives.length === 0 && <span style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body-sm)' }}>No active drives</span>}
          </div>
        </DashboardWidget>
        <DashboardWidget title="Recent Placements" subtitle="Latest placed students">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {placedStudents.slice(0, 4).map((p) => <StudentCareerCard key={p.id} profile={p} />)}
            {placedStudents.length === 0 && <span style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body-sm)' }}>No placements yet</span>}
          </div>
        </DashboardWidget>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget title="Top Recruiters" subtitle="Companies with highest hires">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
            {dashboard.topRecruiters.slice(0, 5).map((c, i) => (
              <div key={i} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', fontSize: 'var(--text-body-sm)' }}>
                <span style={{ color: 'var(--color-text-secondary)' }}>{c.companyName}</span>
                <span style={{ fontWeight: 'var(--weight-semibold)' }}>{c.totalHires} hires</span>
              </div>
            ))}
          </div>
        </DashboardWidget>
        <DashboardWidget title="Quick Stats" subtitle="Key metrics at a glance">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8, fontSize: 'var(--text-body-sm)' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}><span>Not Placed</span><strong style={{ color: '#dc2626' }}>{dashboard.notPlaced}</strong></div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}><span>Shortlisted</span><strong style={{ color: '#ca8a04' }}>{dashboard.shortlisted}</strong></div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}><span>Placement Ready</span><strong>{dashboard.placementReady}</strong></div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}><span>Active Drives</span><strong>{dashboard.activeDrives}</strong></div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}><span>Job Openings</span><strong>{dashboard.activeJobOpenings}</strong></div>
          </div>
        </DashboardWidget>
      </div>
    </main>
  );
}
