import { memo, useMemo } from 'react';
import type { CalendarMonth, ViewMode } from '../types';

interface CalendarViewProps {
  calendarMonth: CalendarMonth;
  viewMode: ViewMode;
  onViewModeChange: (mode: ViewMode) => void;
  onMonthChange: (delta: number) => void;
}

const DAY_NAMES = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
const MONTH_NAMES = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

export const CalendarView = memo(function CalendarView({
  calendarMonth, viewMode, onViewModeChange, onMonthChange,
}: CalendarViewProps) {
  const firstDayOffset = useMemo(() => {
    const first = new Date(calendarMonth.year, calendarMonth.month - 1, 1).getDay();
    return Array.from({ length: first }, () => null) as (typeof calendarMonth.days[0] | null)[];
  }, [calendarMonth]);

  const weeks = useMemo(() => {
    const cells: (typeof calendarMonth.days[0] | null)[] = [...firstDayOffset, ...calendarMonth.days];
    const result: (typeof calendarMonth.days[0] | null)[][] = [];
    for (let i = 0; i < cells.length; i += 7) {
      result.push(cells.slice(i, i + 7));
    }
    return result;
  }, [calendarMonth, firstDayOffset]);

  return (
    <div className="attendance-calendar" style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: '1px solid var(--color-border-default)',
      background: 'var(--color-bg-surface-default)',
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 'var(--space-3)' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <button onClick={() => onMonthChange(-1)} aria-label="Previous month" style={{ padding: '4px 8px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>←</button>
          <span style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>
            {MONTH_NAMES[calendarMonth.month - 1]} {calendarMonth.year}
          </span>
          <button onClick={() => onMonthChange(1)} aria-label="Next month" style={{ padding: '4px 8px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>→</button>
        </div>
        <div style={{ display: 'flex', gap: 4 }} role="radiogroup" aria-label="Calendar view">
          {(['daily', 'weekly', 'monthly'] as ViewMode[]).map((mode) => (
            <button
              key={mode}
              onClick={() => onViewModeChange(mode)}
              aria-pressed={viewMode === mode}
              style={{
                padding: '4px 10px', borderRadius: 'var(--radius-sm)',
                border: '1px solid var(--color-border-default)',
                background: viewMode === mode ? 'var(--color-bg-primary-subtle)' : 'var(--color-bg-surface-default)',
                color: viewMode === mode ? 'var(--color-primary)' : 'var(--color-text-secondary)',
                cursor: 'pointer', fontSize: 'var(--text-caption)', textTransform: 'capitalize',
              }}
            >
              {mode}
            </button>
          ))}
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(7, 1fr)', gap: 2 }}>
        {DAY_NAMES.map((d) => (
          <div key={d} style={{ textAlign: 'center', fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-tertiary)', padding: '4px 0' }}>{d}</div>
        ))}
        {weeks.map((week, wi) =>
          week.map((day, di) => {
            if (!day) return <div key={`empty-${wi}-${di}`} />;
            const pct = day.attendancePercent;
            const bgColor = pct === null ? 'transparent' : pct >= 80 ? '#f0fdf4' : pct >= 60 ? '#fefce8' : '#fef2f2';
            const borderColor = day.isTrainingDay ? (day.hasSession ? 'var(--color-primary)' : 'var(--color-border-default)') : 'transparent';
            return (
              <div
                key={day.date}
                style={{
                  padding: 6, borderRadius: 'var(--radius-sm)',
                  background: bgColor, border: `1px solid ${borderColor}`,
                  textAlign: 'center', fontSize: 'var(--text-caption)',
                  minHeight: 50, display: 'flex', flexDirection: 'column',
                  justifyContent: 'center', alignItems: 'center',
                  opacity: day.isHoliday ? 0.4 : 1,
                }}
              >
                <span style={{ fontWeight: 'var(--weight-medium)' }}>{new Date(day.date).getDate()}</span>
                {day.isTrainingDay && <span style={{ fontSize: 10, color: 'var(--color-text-tertiary)' }}>{pct}%</span>}
                {day.isHoliday && <span style={{ fontSize: 9 }}>H</span>}
              </div>
            );
          })
        )}
      </div>
    </div>
  );
});
