import { useState, useMemo } from 'react';
import { useAlumni } from '../state/AlumniContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { MetricCard } from '../components/MetricCard';
import { StudentCareerCard } from '../components/StudentCareerCard';
import { CounselingSessionCard } from '../components/CounselingSessionCard';
import { InterviewPrepCard } from '../components/InterviewPrepCard';
import { SkillGapCard } from '../components/SkillGapCard';
import { EmptyState } from '../components/EmptyStates';
import Pagination from '../../../../components/navigation/Pagination';

export default function CareerDevelopmentCenter() {
  const {
    studentProfiles, counselingSessions, interviewPrep, skillGaps, getFilteredProfiles,
    page, pageSize, setPage, setPageSize,
  } = useAlumni();

  const [activeTab, setActiveTab] = useState('profiles');

  const filteredProfiles = useMemo(() => getFilteredProfiles().slice((page - 1) * pageSize, page * pageSize), [getFilteredProfiles, page, pageSize]);
  const totalProfiles = useMemo(() => getFilteredProfiles().length, [getFilteredProfiles]);

  const displayCounseling = useMemo(() => counselingSessions.slice((page - 1) * pageSize, page * pageSize), [counselingSessions, page, pageSize]);
  const displayInterview = useMemo(() => interviewPrep.slice((page - 1) * pageSize, page * pageSize), [interviewPrep, page, pageSize]);
  const displaySkillGaps = useMemo(() => skillGaps.slice((page - 1) * pageSize, page * pageSize), [skillGaps, page, pageSize]);

  const tabs = [
    { key: 'profiles', label: 'Career Profiles', count: studentProfiles.length, icon: '👤' },
    { key: 'counseling', label: 'Counseling', count: counselingSessions.length, icon: '💬' },
    { key: 'interviews', label: 'Interview Prep', count: interviewPrep.length, icon: '🎯' },
    { key: 'skill-gaps', label: 'Skill Gaps', count: skillGaps.length, icon: '📊' },
  ];

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Career Development Center</h1>
        <SharedFilters currentPage="alumni/career" showSort />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Students" value={studentProfiles.length} icon="🎓" />
        <MetricCard label="Counseling Sessions" value={counselingSessions.length} icon="💬" color="#2563eb" />
        <MetricCard label="Interviews" value={interviewPrep.length} icon="🎯" color="#8b5cf6" />
        <MetricCard label="Skill Gaps" value={skillGaps.length} icon="📊" color="#d97706" />
        <MetricCard label="Placement Ready" value={studentProfiles.filter((p) => p.placementStatus === 'placement-ready').length} icon="✅" color="#16a34a" />
        <MetricCard label="Placed" value={studentProfiles.filter((p) => ['selected', 'offer-received', 'offer-accepted', 'joined'].includes(p.placementStatus)).length} icon="💼" color="#059669" />
      </div>

      <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
        {tabs.map((tab) => (
          <button key={tab.key} onClick={() => setActiveTab(tab.key)}
            style={{
              padding: '4px 12px', borderRadius: 'var(--radius-full)', border: '1px solid var(--color-border-default)',
              cursor: 'pointer', fontSize: 'var(--text-caption)', whiteSpace: 'nowrap',
              background: activeTab === tab.key ? 'var(--color-primary)' : 'transparent',
              color: activeTab === tab.key ? 'var(--color-text-on-primary)' : 'var(--color-text-secondary)',
              fontWeight: activeTab === tab.key ? 'var(--weight-semibold)' : 'var(--weight-normal)',
            }}
          >
            {tab.icon} {tab.label} ({tab.count})
          </button>
        ))}
      </div>

      <DashboardWidget title={
        activeTab === 'profiles' ? 'Student Career Profiles' :
        activeTab === 'counseling' ? 'Career Counseling Sessions' :
        activeTab === 'interviews' ? 'Interview Preparation' : 'Skill Gap Assessments'
      } subtitle={`Showing ${activeTab === 'profiles' ? totalProfiles : activeTab === 'counseling' ? counselingSessions.length : activeTab === 'interviews' ? interviewPrep.length : skillGaps.length} records`}>
        {activeTab === 'profiles' && (
          filteredProfiles.length === 0 ? <EmptyState type="noSearchResults" /> :
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {filteredProfiles.map((p) => <StudentCareerCard key={p.id} profile={p} />)}
            <Pagination page={page} pageSize={pageSize} total={totalProfiles} onPageChange={setPage} onPageSizeChange={setPageSize} />
          </div>
        )}
        {activeTab === 'counseling' && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {displayCounseling.map((s) => <CounselingSessionCard key={s.id} session={s} />)}
            <Pagination page={page} pageSize={pageSize} total={counselingSessions.length} onPageChange={setPage} onPageSizeChange={setPageSize} />
          </div>
        )}
        {activeTab === 'interviews' && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {displayInterview.map((i) => <InterviewPrepCard key={i.id} prep={i} />)}
            <Pagination page={page} pageSize={pageSize} total={interviewPrep.length} onPageChange={setPage} onPageSizeChange={setPageSize} />
          </div>
        )}
        {activeTab === 'skill-gaps' && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {displaySkillGaps.map((g) => <SkillGapCard key={g.id} gap={g} />)}
            <Pagination page={page} pageSize={pageSize} total={skillGaps.length} onPageChange={setPage} onPageSizeChange={setPageSize} />
          </div>
        )}
      </DashboardWidget>
    </main>
  );
}
