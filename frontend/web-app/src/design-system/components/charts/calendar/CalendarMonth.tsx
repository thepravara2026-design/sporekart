import React, { useCallback, useMemo, useRef } from 'react';

export interface CalendarEvent {
  date: Date;
  label: string;
  color?: string;
}

export interface CalendarMonthProps {
  year?: number;
  month?: number;
  selectedDate?: Date;
  onDateSelect?: (date: Date) => void;
  events?: CalendarEvent[];
  minDate?: Date;
  maxDate?: Date;
  className?: string;
  style?: React.CSSProperties;
}

const DAY_NAMES = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

function isSameDay(a: Date, b: Date): boolean {
  return (
    a.getFullYear() === b.getFullYear() &&
    a.getMonth() === b.getMonth() &&
    a.getDate() === b.getDate()
  );
}

function getDaysInMonth(year: number, month: number): number {
  return new Date(year, month + 1, 0).getDate();
}

function getStartDayOfWeek(year: number, month: number): number {
  return new Date(year, month, 1).getDay();
}

export const CalendarMonth: React.FC<CalendarMonthProps> = ({
  year: propYear,
  month: propMonth,
  selectedDate,
  onDateSelect,
  events = [],
  minDate,
  maxDate,
  className = '',
  style,
}) => {
  const today = useMemo(() => new Date(), []);
  const [viewYear, setViewYear] = React.useState(propYear ?? today.getFullYear());
  const [viewMonth, setViewMonth] = React.useState(propMonth ?? today.getMonth());
  const [focusedDate, setFocusedDate] = React.useState<Date | null>(null);
  const gridRef = useRef<HTMLTableElement>(null);

  React.useEffect(() => {
    if (propYear !== undefined) setViewYear(propYear);
  }, [propYear]);

  React.useEffect(() => {
    if (propMonth !== undefined) setViewMonth(propMonth);
  }, [propMonth]);

  const daysInMonth = getDaysInMonth(viewYear, viewMonth);
  const startDay = getStartDayOfWeek(viewYear, viewMonth);

  const weeks = useMemo(() => {
    const result: (number | null)[][] = [];
    let days: (number | null)[] = [];
    for (let i = 0; i < startDay; i++) {
      days.push(null);
    }
    for (let d = 1; d <= daysInMonth; d++) {
      days.push(d);
      if (days.length === 7) {
        result.push(days);
        days = [];
      }
    }
    if (days.length > 0) {
      while (days.length < 7) days.push(null);
      result.push(days);
    }
    return result;
  }, [viewYear, viewMonth, daysInMonth, startDay]);

  const isDisabled = useCallback(
    (day: number) => {
      const date = new Date(viewYear, viewMonth, day);
      if (minDate && date < new Date(minDate.getFullYear(), minDate.getMonth(), minDate.getDate())) return true;
      if (maxDate && date > new Date(maxDate.getFullYear(), maxDate.getMonth(), maxDate.getDate())) return true;
      return false;
    },
    [viewYear, viewMonth, minDate, maxDate],
  );

  const navigate = useCallback((direction: -1 | 1) => {
    const newMonth = viewMonth + direction;
    if (newMonth < 0) {
      setViewYear((y) => y - 1);
      setViewMonth(11);
    } else if (newMonth > 11) {
      setViewYear((y) => y + 1);
      setViewMonth(0);
    } else {
      setViewMonth(newMonth);
    }
  }, [viewMonth]);

  const handleKeyDown = useCallback(
    (e: React.KeyboardEvent, day: number) => {
      if (!day) return;
      const date = new Date(viewYear, viewMonth, day);
      const disabled = isDisabled(day);
      let handled = true;

      switch (e.key) {
        case 'Enter':
        case ' ':
          if (!disabled) onDateSelect?.(date);
          break;
        case 'ArrowLeft':
          if (!disabled && day > 1) {
            const prev = new Date(viewYear, viewMonth, day - 1);
            if (!isDisabled(day - 1)) {
              setFocusedDate(prev);
              onDateSelect?.(prev);
            }
          }
          break;
        case 'ArrowRight':
          if (!disabled && day < daysInMonth) {
            const next = new Date(viewYear, viewMonth, day + 1);
            if (!isDisabled(day + 1)) {
              setFocusedDate(next);
              onDateSelect?.(next);
            }
          }
          break;
        case 'ArrowUp':
          if (!disabled && day - 7 >= 1) {
            const up = new Date(viewYear, viewMonth, day - 7);
            if (!isDisabled(day - 7)) {
              setFocusedDate(up);
              onDateSelect?.(up);
            }
          }
          break;
        case 'ArrowDown':
          if (!disabled && day + 7 <= daysInMonth) {
            const down = new Date(viewYear, viewMonth, day + 7);
            if (!isDisabled(day + 7)) {
              setFocusedDate(down);
              onDateSelect?.(down);
            }
          }
          break;
        default:
          handled = false;
      }

      if (handled) e.preventDefault();
    },
    [viewYear, viewMonth, daysInMonth, isDisabled, onDateSelect],
  );

  const headerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: 'var(--space-2) var(--space-1)',
    marginBottom: 'var(--space-2)',
  };

  const navBtnStyle: React.CSSProperties = {
    background: 'none',
    border: '1px solid var(--color-border-default)',
    borderRadius: 'var(--radius-btn)',
    padding: 'var(--space-1) var(--space-2)',
    cursor: 'pointer',
    fontSize: 'var(--text-body)',
    color: 'var(--color-text-primary)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    lineHeight: 1,
    minWidth: 32,
    height: 32,
  };

  const titleStyle: React.CSSProperties = {
    fontSize: 'var(--text-h5)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-primary)',
  };

  const tableStyle: React.CSSProperties = {
    width: '100%',
    borderCollapse: 'collapse',
  };

  const thStyle: React.CSSProperties = {
    fontSize: 'var(--text-caption)',
    fontWeight: 'var(--weight-medium)',
    color: 'var(--color-text-secondary)',
    textAlign: 'center',
    padding: 'var(--space-1) 0',
    width: `${100 / 7}%`,
  };

  const cellBase: React.CSSProperties = {
    textAlign: 'center',
    padding: 0,
    position: 'relative',
  };

  const dayBtnBase: React.CSSProperties = {
    width: 36,
    height: 36,
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 'var(--radius-full)',
    border: 'none',
    background: 'none',
    cursor: 'pointer',
    fontSize: 'var(--text-body-sm)',
    color: 'var(--color-text-primary)',
    position: 'relative',
    margin: '1px auto',
    transition: 'background-color var(--duration-fast) var(--easing-standard)',
  };

  const eventDotStyle: React.CSSProperties = {
    position: 'absolute',
    bottom: 2,
    left: '50%',
    transform: 'translateX(-50%)',
    width: 4,
    height: 4,
    borderRadius: 'var(--radius-full)',
  };

  return (
    <div
      className={`sk-calendar-month ${className}`.trim()}
      style={{
        fontFamily: 'var(--font-family-sans)',
        maxWidth: 320,
        ...style,
      }}
    >
      <div style={headerStyle}>
        <button
          type="button"
          style={navBtnStyle}
          onClick={() => navigate(-1)}
          aria-label="Previous month"
        >
          {'\u2039'}
        </button>
        <span style={titleStyle}>
          {new Date(viewYear, viewMonth).toLocaleDateString('en-US', {
            month: 'long',
            year: 'numeric',
          })}
        </span>
        <button
          type="button"
          style={navBtnStyle}
          onClick={() => navigate(1)}
          aria-label="Next month"
        >
          {'\u203A'}
        </button>
      </div>

      <table
        ref={gridRef}
        style={tableStyle}
        role="grid"
        aria-label="Calendar"
      >
        <thead>
          <tr>
            {DAY_NAMES.map((name) => (
              <th key={name} scope="col" style={thStyle}>
                {name}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {weeks.map((week, wi) => (
            <tr key={wi}>
              {week.map((day, di) => {
                if (day === null) {
                  return <td key={di} style={cellBase} />;
                }

                const date = new Date(viewYear, viewMonth, day);
                const disabled = isDisabled(day);
                const isToday = isSameDay(date, today);
                const isSelected = selectedDate ? isSameDay(date, selectedDate) : false;
                const isFocused = focusedDate ? isSameDay(date, focusedDate) : false;
                const dayEvents = events.filter((e) => isSameDay(e.date, date));

                let bg = 'transparent';
                let color = 'var(--color-text-primary)';
                let border = 'none';

                if (isSelected) {
                  bg = 'var(--color-bg-primary-default)';
                  color = 'var(--color-text-on-primary)';
                } else if (isToday && !isSelected) {
                  border = '2px solid var(--color-bg-primary-default)';
                }

                if (disabled) {
                  color = 'var(--color-text-disabled)';
                }

                return (
                  <td
                    key={di}
                    style={cellBase}
                    role="gridcell"
                    aria-selected={isSelected || undefined}
                    aria-disabled={disabled || undefined}
                    aria-label={date.toLocaleDateString('en-US', {
                      weekday: 'long',
                      month: 'long',
                      day: 'numeric',
                      year: 'numeric',
                    })}
                  >
                    <button
                      type="button"
                      style={{
                        ...dayBtnBase,
                        backgroundColor: bg,
                        color,
                        border,
                        cursor: disabled ? 'not-allowed' : 'pointer',
                        ...(isFocused && !disabled
                          ? { outline: '2px solid var(--color-focus-ring)', outlineOffset: 2 }
                          : {}),
                      }}
                      onClick={() => {
                        if (!disabled) {
                          onDateSelect?.(date);
                          setFocusedDate(date);
                        }
                      }}
                      onKeyDown={(e) => handleKeyDown(e, day)}
                      disabled={disabled}
                      tabIndex={disabled ? -1 : 0}
                      aria-label={date.toLocaleDateString('en-US', {
                        weekday: 'long',
                        month: 'long',
                        day: 'numeric',
                        year: 'numeric',
                      })}
                    >
                      {day}
                      {dayEvents.length > 0 && (
                        <span style={cellBase}>
                          {dayEvents.map((evt, ei) => (
                            <span
                              key={ei}
                              style={{
                                ...eventDotStyle,
                                backgroundColor: evt.color || 'var(--color-bg-primary-default)',
                                marginLeft: ei > 0 ? 6 : 0,
                              }}
                              aria-hidden="true"
                            />
                          ))}
                        </span>
                      )}
                    </button>
                  </td>
                );
              })}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

CalendarMonth.displayName = 'CalendarMonth';
