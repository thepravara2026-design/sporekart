import React from 'react';

export interface CalendarWeekEvent {
  date: Date;
  label: string;
  startTime?: string;
  endTime?: string;
  color?: string;
}

export interface CalendarWeekProps {
  year?: number;
  month?: number;
  weekStart?: Date;
  events?: CalendarWeekEvent[];
  className?: string;
  style?: React.CSSProperties;
}

const DAY_NAMES = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
const HOURS = Array.from({ length: 24 }, (_, i) => i);

function getWeekStart(date: Date): Date {
  const d = new Date(date);
  const day = d.getDay();
  d.setDate(d.getDate() - day);
  d.setHours(0, 0, 0, 0);
  return d;
}

function isSameDay(a: Date, b: Date): boolean {
  return (
    a.getFullYear() === b.getFullYear() &&
    a.getMonth() === b.getMonth() &&
    a.getDate() === b.getDate()
  );
}

export const CalendarWeek: React.FC<CalendarWeekProps> = ({
  year,
  month,
  weekStart,
  events = [],
  className = '',
  style,
}) => {
  const today = new Date();
  const ws = weekStart || (year !== undefined && month !== undefined ? getWeekStart(new Date(year, month, 1)) : getWeekStart(today));

  const days = Array.from({ length: 7 }, (_, i) => {
    const d = new Date(ws);
    d.setDate(ws.getDate() + i);
    return d;
  });

  const containerStyle: React.CSSProperties = {
    overflowX: 'auto',
    fontFamily: 'var(--font-family-sans)',
    ...style,
  };

  const tableStyle: React.CSSProperties = {
    width: '100%',
    minWidth: 700,
    borderCollapse: 'collapse',
    tableLayout: 'fixed',
  };

  const headerThStyle: React.CSSProperties = {
    fontSize: 'var(--text-caption)',
    fontWeight: 'var(--weight-medium)',
    color: 'var(--color-text-secondary)',
    textAlign: 'center',
    padding: 'var(--space-2) var(--space-1)',
    borderBottom: '1px solid var(--color-border-default)',
    width: `${100 / 8}%`,
  };

  const timeThStyle: React.CSSProperties = {
    ...headerThStyle,
    width: '5%',
    minWidth: 50,
  };

  const dayCellHeaderStyle: React.CSSProperties = {
    fontSize: 'var(--text-body-sm)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-primary)',
    display: 'block',
    marginBottom: 2,
  };

  const dayCellSubStyle: React.CSSProperties = {
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-disabled)',
    display: 'block',
  };

  const timeCellStyle: React.CSSProperties = {
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-disabled)',
    textAlign: 'right',
    padding: 'var(--space-1) var(--space-2)',
    borderBottom: '1px solid var(--color-border-default)',
    verticalAlign: 'top',
    width: '5%',
    minWidth: 50,
  };

  const dayCellStyle: React.CSSProperties = {
    borderBottom: '1px solid var(--color-border-default)',
    borderLeft: '1px solid var(--color-border-default)',
    verticalAlign: 'top',
    padding: 'var(--space-1)',
    height: 40,
    position: 'relative',
  };

  const eventBlockStyle: React.CSSProperties = {
    fontSize: 'var(--text-caption)',
    padding: '1px var(--space-1)',
    borderRadius: 'var(--radius-xs)',
    marginBottom: 1,
    color: 'var(--color-text-on-primary)',
    overflow: 'hidden',
    whiteSpace: 'nowrap',
    textOverflow: 'ellipsis',
  };

  return (
    <div className={`sk-calendar-week ${className}`.trim()} style={containerStyle}>
      <table style={tableStyle} role="grid" aria-label="Week calendar">
        <thead>
          <tr>
            <th scope="col" style={timeThStyle} aria-label="Time">
              {'\u23F0'}
            </th>
            {days.map((day, i) => {
              const isToday = isSameDay(day, today);
              return (
                <th key={i} scope="col" style={headerThStyle}>
                  <span style={dayCellHeaderStyle}>
                    {isToday ? '\u25CF ' : ''}{DAY_NAMES[i].slice(0, 3)}
                  </span>
                  <span style={dayCellSubStyle}>{day.getDate()}</span>
                </th>
              );
            })}
          </tr>
        </thead>
        <tbody>
          {HOURS.map((hour) => {
            const timeLabel = `${hour.toString().padStart(2, '0')}:00`;
            return (
              <tr key={hour}>
                <td style={timeCellStyle}>{timeLabel}</td>
                {days.map((day, di) => {
                  const dayEvents = events.filter((e) => {
                    if (!isSameDay(e.date, day)) return false;
                    if (e.startTime) {
                      const eHour = parseInt(e.startTime.split(':')[0], 10);
                      return eHour === hour;
                    }
                    return false;
                  });

                  return (
                    <td
                      key={di}
                      style={{
                        ...dayCellStyle,
                        backgroundColor: hour % 2 === 0 ? 'transparent' : 'var(--color-bg-background)',
                      }}
                      role="gridcell"
                    >
                      {dayEvents.map((evt, ei) => (
                        <div
                          key={ei}
                          style={{
                            ...eventBlockStyle,
                            backgroundColor: evt.color || 'var(--color-bg-primary-default)',
                          }}
                          title={`${evt.label}${evt.startTime ? ` (${evt.startTime}${evt.endTime ? ` - ${evt.endTime}` : ''})` : ''}`}
                        >
                          {evt.startTime && (
                            <span style={{ opacity: 0.8, marginRight: 4 }}>
                              {evt.startTime}
                              {evt.endTime && `-${evt.endTime}`}
                            </span>
                          )}
                          {evt.label}
                        </div>
                      ))}
                    </td>
                  );
                })}
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

CalendarWeek.displayName = 'CalendarWeek';
