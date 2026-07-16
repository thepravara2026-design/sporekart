import { useState, useCallback } from 'react';
import { CalendarView } from '../components/CalendarView';
import { getCalendarData } from '../data/mockData';
import type { ViewMode } from '../types';

export function AttendanceCalendarPage() {
  const [year, setYear] = useState(2026);
  const [month, setMonth] = useState(7);
  const [viewMode, setViewMode] = useState<ViewMode>('monthly');

  const calendarMonth = getCalendarData(year, month);

  const handleMonthChange = useCallback((delta: number) => {
    let newMonth = month + delta;
    let newYear = year;
    if (newMonth > 12) { newMonth = 1; newYear++; }
    if (newMonth < 1) { newMonth = 12; newYear--; }
    setMonth(newMonth);
    setYear(newYear);
  }, [month, year]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Attendance Calendar</h1>
        <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', margin: '4px 0 0 0' }}>
          Monthly, weekly, and daily attendance view
        </p>
      </div>

      <CalendarView
        calendarMonth={calendarMonth}
        viewMode={viewMode}
        onViewModeChange={setViewMode}
        onMonthChange={handleMonthChange}
      />

      <div style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
        <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: '0 0 var(--space-2) 0' }}>Legend</h3>
        <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap', fontSize: 'var(--text-caption)' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 4 }}><div style={{ width: 12, height: 12, borderRadius: 2, background: '#f0fdf4', border: '1px solid #16a34a' }} /> ≥80% Attendance</div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 4 }}><div style={{ width: 12, height: 12, borderRadius: 2, background: '#fefce8', border: '1px solid #ca8a04' }} /> 60–79% Attendance</div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 4 }}><div style={{ width: 12, height: 12, borderRadius: 2, background: '#fef2f2', border: '1px solid #dc2626' }} /> &lt;60% Attendance</div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 4 }}><div style={{ width: 12, height: 12, borderRadius: 2, border: '1px solid var(--color-primary)' }} /> Training Day</div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 4 }}>H Holiday</div>
        </div>
      </div>
    </div>
  );
}
