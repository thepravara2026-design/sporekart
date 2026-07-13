import React, { useCallback, useMemo, useState } from 'react';

export interface CalendarDateRangeProps {
  startDate?: Date;
  endDate?: Date;
  onChange?: (start: Date, end: Date) => void;
  minDate?: Date;
  maxDate?: Date;
  numberOfMonths?: number;
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

function isInRange(date: Date, start: Date | undefined, end: Date | undefined): 'start' | 'end' | 'in' | 'out' {
  if (!start && !end) return 'out';
  if (start && isSameDay(date, start)) return 'start';
  if (end && isSameDay(date, end)) return 'end';
  if (start && end && date > start && date < end) return 'in';
  if (start && !end && date > start) return 'in';
  return 'out';
}

interface MonthProps {
  year: number;
  month: number;
  startDate?: Date;
  endDate?: Date;
  minDate?: Date;
  maxDate?: Date;
  onDateClick: (date: Date) => void;
}

const MonthView: React.FC<MonthProps> = ({ year, month, startDate, endDate, minDate, maxDate, onDateClick }) => {
  const today = useMemo(() => new Date(), []);
  const daysInMonth = getDaysInMonth(year, month);
  const startDay = getStartDayOfWeek(year, month);

  const weeks = useMemo(() => {
    const result: (number | null)[][] = [];
    let days: (number | null)[] = [];
    for (let i = 0; i < startDay; i++) days.push(null);
    for (let d = 1; d <= daysInMonth; d++) {
      days.push(d);
      if (days.length === 7) { result.push(days); days = []; }
    }
    if (days.length > 0) { while (days.length < 7) days.push(null); result.push(days); }
    return result;
  }, [daysInMonth, startDay]);

  const isDisabled = useCallback((day: number) => {
    const date = new Date(year, month, day);
    if (minDate && date < new Date(minDate.getFullYear(), minDate.getMonth(), minDate.getDate())) return true;
    if (maxDate && date > new Date(maxDate.getFullYear(), maxDate.getMonth(), maxDate.getDate())) return true;
    return false;
  }, [year, month, minDate, maxDate]);

  return (
    <table style={{ width: '100%', borderCollapse: 'collapse' }} role="grid" aria-label={`${new Date(year, month).toLocaleDateString('en-US', { month: 'long', year: 'numeric' })}`}>
      <thead>
        <tr>
          {DAY_NAMES.map((n) => (
            <th
              key={n}
              scope="col"
              style={{
                fontSize: 'var(--text-caption)',
                fontWeight: 'var(--weight-medium)',
                color: 'var(--color-text-secondary)',
                textAlign: 'center',
                padding: 'var(--space-1) 0',
                width: `${100 / 7}%`,
              }}
            >
              {n}
            </th>
          ))}
        </tr>
      </thead>
      <tbody>
        {weeks.map((week, wi) => (
          <tr key={wi}>
            {week.map((day, di) => {
              if (day === null) return <td key={di} style={{ textAlign: 'center', padding: 0 }} />;

              const date = new Date(year, month, day);
              const disabled = isDisabled(day);
              const range = isInRange(date, startDate, endDate);

              let bg = 'transparent';
              let color = 'var(--color-text-primary)';

              if (range === 'start' || range === 'end') {
                bg = 'var(--color-bg-primary-default)';
                color = 'var(--color-text-on-primary)';
              } else if (range === 'in') {
                bg = 'var(--color-bg-primary-weak)';
                color = 'var(--color-text-primary)';
              }

              const isToday = isSameDay(date, today);

              const btnStyle: React.CSSProperties = {
                width: 32,
                height: 32,
                display: 'inline-flex',
                alignItems: 'center',
                justifyContent: 'center',
                borderRadius: range === 'start' || range === 'end' ? 'var(--radius-full)' : '0',
                border: 'none',
                background: bg,
                color,
                cursor: disabled ? 'not-allowed' : 'pointer',
                fontSize: 'var(--text-body-sm)',
                margin: 0,
                position: 'relative',
                ...(isToday && range === 'out'
                  ? { border: '2px solid var(--color-bg-primary-default)', borderRadius: 'var(--radius-full)' }
                  : {}),
                ...(range === 'in'
                  ? {
                      borderRadius: 0,
                    }
                  : {}),
              };

              if (range === 'start') {
                btnStyle.borderTopRightRadius = 0;
                btnStyle.borderBottomRightRadius = 0;
              }
              if (range === 'end') {
                btnStyle.borderTopLeftRadius = 0;
                btnStyle.borderBottomLeftRadius = 0;
              }

              return (
                <td
                  key={di}
                  role="gridcell"
                  aria-selected={range !== 'out' || undefined}
                  aria-label={date.toLocaleDateString('en-US', { weekday: 'long', month: 'long', day: 'numeric', year: 'numeric' })}
                  style={{
                    textAlign: 'center',
                    padding: 0,
                    ...(range === 'start'
                      ? { background: 'linear-gradient(to right, transparent 50%, var(--color-bg-primary-weak) 50%)' }
                      : range === 'end'
                        ? { background: 'linear-gradient(to left, transparent 50%, var(--color-bg-primary-weak) 50%)' }
                        : range === 'in'
                          ? { backgroundColor: 'var(--color-bg-primary-weak)' }
                          : {}),
                  }}
                >
                  <button
                    type="button"
                    style={btnStyle}
                    onClick={() => { if (!disabled) onDateClick(date); }}
                    disabled={disabled}
                    tabIndex={disabled ? -1 : 0}
                    aria-label={date.toLocaleDateString('en-US', { weekday: 'long', month: 'long', day: 'numeric', year: 'numeric' })}
                  >
                    {day}
                  </button>
                </td>
              );
            })}
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export const CalendarDateRange: React.FC<CalendarDateRangeProps> = ({
  startDate: propStart,
  endDate: propEnd,
  onChange,
  minDate,
  maxDate,
  numberOfMonths = 2,
  className = '',
  style,
}) => {
  const today = useMemo(() => new Date(), []);

  const [internalStart, setInternalStart] = useState<Date | undefined>(propStart);
  const [internalEnd, setInternalEnd] = useState<Date | undefined>(propEnd);
  const [selecting, setSelecting] = useState<'start' | 'end'>('start');
  const [baseYear, setBaseYear] = useState(today.getFullYear());
  const [baseMonth, setBaseMonth] = useState(today.getMonth());

  const start = propStart ?? internalStart;
  const end = propEnd ?? internalEnd;

  const months = useMemo(() => {
    const result: { year: number; month: number }[] = [];
    for (let i = 0; i < numberOfMonths; i++) {
      let m = baseMonth + i;
      let y = baseYear;
      if (m > 11) { m -= 12; y += 1; }
      result.push({ year: y, month: m });
    }
    return result;
  }, [baseYear, baseMonth, numberOfMonths]);

  const handleDateClick = useCallback((date: Date) => {
    if (selecting === 'start') {
      setInternalStart(date);
      setInternalEnd(undefined);
      setSelecting('end');
    } else {
      let s: Date;
      let e: Date;
      if (start && date < start) {
        s = date;
        e = start;
      } else {
        s = start ?? date;
        e = date;
      }
      setInternalStart(s);
      setInternalEnd(e);
      setSelecting('start');
      onChange?.(s, e);
    }
  }, [selecting, start, onChange]);

  const canGoPrev = useMemo(() => {
    if (!minDate) return true;
    const firstMonth = months[0];
    const prevMonth = firstMonth.month === 0 ? 11 : firstMonth.month - 1;
    const prevYear = firstMonth.month === 0 ? firstMonth.year - 1 : firstMonth.year;
    return new Date(prevYear, prevMonth, 1) >= new Date(minDate.getFullYear(), minDate.getMonth(), 1);
  }, [months, minDate]);

  const canGoNext = useMemo(() => {
    if (!maxDate) return true;
    const lastMonth = months[months.length - 1];
    const nextMonth = lastMonth.month === 11 ? 0 : lastMonth.month + 1;
    const nextYear = lastMonth.month === 11 ? lastMonth.year + 1 : lastMonth.year;
    return new Date(nextYear, nextMonth, 1) <= new Date(maxDate.getFullYear(), maxDate.getMonth() + 1, 0);
  }, [months, maxDate]);

  const navigate = (dir: -1 | 1) => {
    const newMonth = baseMonth + dir;
    if (newMonth < 0) { setBaseYear((y) => y - 1); setBaseMonth(11); }
    else if (newMonth > 11) { setBaseYear((y) => y + 1); setBaseMonth(0); }
    else { setBaseMonth(newMonth); }
  };

  const containerStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    ...style,
  };

  const headerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: 'var(--space-3)',
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
    opacity: canGoPrev ? 1 : 0.3,
  };

  const monthsContainerStyle: React.CSSProperties = {
    display: 'grid',
    gridTemplateColumns: `repeat(${numberOfMonths}, 1fr)`,
    gap: 'var(--space-4)',
  };

  const monthTitleStyle: React.CSSProperties = {
    fontSize: 'var(--text-body)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-primary)',
    textAlign: 'center',
    marginBottom: 'var(--space-2)',
  };

  return (
    <div className={`sk-calendar-date-range ${className}`.trim()} style={containerStyle}>
      <div style={headerStyle}>
        <button
          type="button"
          style={{ ...navBtnStyle, opacity: canGoPrev ? 1 : 0.3 }}
          onClick={() => { if (canGoPrev) navigate(-1); }}
          disabled={!canGoPrev}
          aria-label="Previous months"
        >
          {'\u2039'}
        </button>
        <span style={{ fontSize: 'var(--text-h5)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>
          {start && end
            ? `${start.toLocaleDateString('en-US', { month: 'short', day: 'numeric' })} - ${end.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })}`
            : 'Select date range'}
        </span>
        <button
          type="button"
          style={{ ...navBtnStyle, opacity: canGoNext ? 1 : 0.3 }}
          onClick={() => { if (canGoNext) navigate(1); }}
          disabled={!canGoNext}
          aria-label="Next months"
        >
          {'\u203A'}
        </button>
      </div>
      <div style={monthsContainerStyle}>
        {months.map((m, i) => (
          <div key={i}>
            <div style={monthTitleStyle}>
              {new Date(m.year, m.month).toLocaleDateString('en-US', { month: 'long', year: 'numeric' })}
            </div>
            <MonthView
              year={m.year}
              month={m.month}
              startDate={start}
              endDate={end}
              minDate={minDate}
              maxDate={maxDate}
              onDateClick={handleDateClick}
            />
          </div>
        ))}
      </div>
    </div>
  );
};

CalendarDateRange.displayName = 'CalendarDateRange';
