import React from 'react';

export interface CalendarAgendaEvent {
  date: Date;
  label: string;
  description?: string;
  time?: string;
  color?: string;
}

export interface CalendarAgendaProps {
  events?: CalendarAgendaEvent[];
  startDate?: Date;
  endDate?: Date;
  maxItems?: number;
  className?: string;
  style?: React.CSSProperties;
}

function isSameDay(a: Date, b: Date): boolean {
  return (
    a.getFullYear() === b.getFullYear() &&
    a.getMonth() === b.getMonth() &&
    a.getDate() === b.getDate()
  );
}

const MONTH_NAMES = [
  'January', 'February', 'March', 'April', 'May', 'June',
  'July', 'August', 'September', 'October', 'November', 'December',
];

export const CalendarAgenda: React.FC<CalendarAgendaProps> = ({
  events = [],
  startDate,
  endDate,
  maxItems,
  className = '',
  style,
}) => {
  const filtered = React.useMemo(() => {
    let result = [...events].sort((a, b) => a.date.getTime() - b.date.getTime());
    if (startDate) result = result.filter((e) => e.date >= startDate);
    if (endDate) result = result.filter((e) => e.date <= endDate);
    if (maxItems !== undefined) result = result.slice(0, maxItems);
    return result;
  }, [events, startDate, endDate, maxItems]);

  const grouped = React.useMemo(() => {
    const map = new Map<string, CalendarAgendaEvent[]>();
    filtered.forEach((e) => {
      const key = e.date.toLocaleDateString();
      if (!map.has(key)) map.set(key, []);
      map.get(key)!.push(e);
    });
    return Array.from(map.entries()).sort(([a], [b]) => a.localeCompare(b));
  }, [filtered]);

  const containerStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    ...style,
  };

  const groupStyle: React.CSSProperties = {
    marginBottom: 'var(--space-4)',
  };

  const dateHeaderStyle: React.CSSProperties = {
    fontSize: 'var(--text-body-sm)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-secondary)',
    marginBottom: 'var(--space-2)',
    paddingBottom: 'var(--space-1)',
    borderBottom: '1px solid var(--color-border-default)',
    textTransform: 'uppercase' as const,
    letterSpacing: 'var(--tracking-wide)',
  };

  const eventRowStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-3)',
    padding: 'var(--space-2) var(--space-3)',
    borderRadius: 'var(--radius-sm)',
    transition: 'background-color var(--duration-fast) var(--easing-standard)',
    marginBottom: 'var(--space-1)',
  };

  const colorDotStyle: React.CSSProperties = {
    width: 8,
    height: 8,
    borderRadius: 'var(--radius-full)',
    flexShrink: 0,
  };

  const timeStyle: React.CSSProperties = {
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-disabled)',
    minWidth: 48,
    fontVariantNumeric: 'tabular-nums',
    flexShrink: 0,
  };

  const labelStyle: React.CSSProperties = {
    fontSize: 'var(--text-body-sm)',
    fontWeight: 'var(--weight-medium)',
    color: 'var(--color-text-primary)',
    flex: 1,
    minWidth: 0,
  };

  const descStyle: React.CSSProperties = {
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-secondary)',
    flex: 1,
    minWidth: 0,
  };

  const emptyStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 'var(--space-8) var(--space-4)',
    color: 'var(--color-text-disabled)',
    fontSize: 'var(--text-body-sm)',
    textAlign: 'center',
  };

  return (
    <div className={`sk-calendar-agenda ${className}`.trim()} style={containerStyle} role="list" aria-label="Agenda">
      {grouped.length === 0 && (
        <div style={emptyStyle}>No events scheduled</div>
      )}
      {grouped.map(([dateKey, dayEvents]) => {
        const date = new Date(dayEvents[0].date);
        const dayName = date.toLocaleDateString('en-US', { weekday: 'long' });
        const isToday = isSameDay(date, new Date());

        return (
          <div key={dateKey} style={groupStyle} role="listitem">
            <div style={dateHeaderStyle}>
              {isToday && '\u25CF '}
              {dayName}, {MONTH_NAMES[date.getMonth()]} {date.getDate()}, {date.getFullYear()}
            </div>
            {dayEvents.map((evt, i) => (
              <div key={i} style={eventRowStyle}>
                <span
                  style={{
                    ...colorDotStyle,
                    backgroundColor: evt.color || 'var(--color-bg-primary-default)',
                  }}
                  aria-hidden="true"
                />
                {evt.time && <span style={timeStyle}>{evt.time}</span>}
                <span style={evt.description ? labelStyle : { ...labelStyle, fontWeight: 'var(--weight-semibold)' }}>
                  {evt.label}
                </span>
                {evt.description && <span style={descStyle}>{evt.description}</span>}
              </div>
            ))}
          </div>
        );
      })}
    </div>
  );
};

CalendarAgenda.displayName = 'CalendarAgenda';
