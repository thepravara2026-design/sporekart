import { useState } from 'react';
import { AttendanceProvider } from '../state/AttendanceContext';
import { ATTENDANCE_NAV_ITEMS } from '../types';
import { AttendanceDashboardPage } from './AttendanceDashboardPage';
import { AttendanceRegisterPage } from './AttendanceRegisterPage';
import { AttendanceCalendarPage } from './AttendanceCalendarPage';
import { AttendanceAnalyticsPage } from './AttendanceAnalyticsPage';
import { AttendanceTimelinePage } from './AttendanceTimelinePage';
import { PolicyCenterPage } from './PolicyCenterPage';

function AttendanceIndexInner() {
  const [activeSection, setActiveSection] = useState('attendance');

  const renderSection = () => {
    switch (activeSection) {
      case 'attendance/register': return <AttendanceRegisterPage />;
      case 'attendance/calendar': return <AttendanceCalendarPage />;
      case 'attendance/analytics': return <AttendanceAnalyticsPage />;
      case 'attendance/timeline': return <AttendanceTimelinePage />;
      case 'attendance/policies': return <PolicyCenterPage />;
      default: return <AttendanceDashboardPage />;
    }
  };

  return (
    <div>
      <nav aria-label="Attendance sections" style={{ display: 'flex', gap: 4, flexWrap: 'wrap', marginBottom: 'var(--space-section-gap)', padding: 4, borderRadius: 'var(--radius-md)', background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)' }}>
        {ATTENDANCE_NAV_ITEMS.map((item) => (
          <button
            key={item.id}
            onClick={() => setActiveSection(item.id)}
            aria-current={activeSection === item.id ? 'page' : undefined}
            style={{
              padding: '6px 14px', borderRadius: 'var(--radius-sm)', border: 'none', cursor: 'pointer',
              background: activeSection === item.id ? 'var(--color-bg-primary-subtle)' : 'transparent',
              color: activeSection === item.id ? 'var(--color-primary)' : 'var(--color-text-secondary)',
              fontWeight: activeSection === item.id ? 'var(--weight-semibold)' : 'var(--weight-normal)',
              fontSize: 'var(--text-body-sm)', whiteSpace: 'nowrap',
            }}
          >
            {item.label}
          </button>
        ))}
      </nav>
      <div>{renderSection()}</div>
    </div>
  );
}

export function AttendanceIndex() {
  return (
    <AttendanceProvider>
      <AttendanceIndexInner />
    </AttendanceProvider>
  );
}

export default AttendanceIndex;
