import { AttendanceTimeline } from '../components/AttendanceTimeline';
import { getAttendanceTimeline } from '../data/mockData';

export function AttendanceTimelinePage() {
  const timelineEvents = getAttendanceTimeline();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Attendance Timeline</h1>
        <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', margin: '4px 0 0 0' }}>
          Student attendance lifecycle and milestones
        </p>
      </div>

      <div style={{ maxWidth: 600 }}>
        <AttendanceTimeline events={timelineEvents} />
      </div>
    </div>
  );
}
