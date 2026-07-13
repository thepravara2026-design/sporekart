import React, { useState, useRef, useEffect, useCallback } from 'react';

export interface DateRangePickerProps {
  startDate?: Date;
  endDate?: Date;
  onChange?: (start: Date, end: Date) => void;
  minDate?: Date;
  maxDate?: Date;
  placeholder?: string;
  className?: string;
  style?: React.CSSProperties;
}

const MONTHS = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
const DAYS = ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'];

function formatDate(d: Date): string {
  const m = `0${d.getMonth() + 1}`.slice(-2);
  const day = `0${d.getDate()}`.slice(-2);
  return `${d.getFullYear()}-${m}-${day}`;
}

function getDaysInMonth(year: number, month: number): (number | null)[][] {
  const firstDay = new Date(year, month, 1).getDay();
  const daysInMonth = new Date(year, month + 1, 0).getDate();
  const weeks: (number | null)[][] = [];
  let week: (number | null)[] = [];
  for (let i = 0; i < firstDay; i++) week.push(null);
  for (let d = 1; d <= daysInMonth; d++) {
    week.push(d);
    if (week.length === 7) {
      weeks.push(week);
      week = [];
    }
  }
  if (week.length > 0) {
    while (week.length < 7) week.push(null);
    weeks.push(week);
  }
  return weeks;
}

function isSameDay(a: Date, b: Date): boolean {
  return a.getFullYear() === b.getFullYear() && a.getMonth() === b.getMonth() && a.getDate() === b.getDate();
}

function isInRange(day: Date, start?: Date, end?: Date): boolean {
  if (!start || !end) return false;
  return day >= start && day <= end;
}

export const DateRangePicker: React.FC<DateRangePickerProps> = ({
  startDate,
  endDate,
  onChange,
  minDate,
  maxDate,
  placeholder = 'Select date range',
  className = '',
  style,
}) => {
  const [open, setOpen] = useState(false);
  const [selectingStart, setSelectingStart] = useState(true);
  const [viewYear, setViewYear] = useState(new Date().getFullYear());
  const [viewMonth, setViewMonth] = useState(new Date().getMonth());
  const containerRef = useRef<HTMLDivElement>(null);
  const inputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (containerRef.current && !containerRef.current.contains(e.target as Node)) {
        setOpen(false);
      }
    };
    const handleEsc = (e: KeyboardEvent) => {
      if (e.key === 'Escape') setOpen(false);
    };
    if (open) {
      document.addEventListener('mousedown', handleClickOutside);
      document.addEventListener('keydown', handleEsc);
    }
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
      document.removeEventListener('keydown', handleEsc);
    };
  }, [open]);

  const rangeLabel = startDate && endDate
    ? `${formatDate(startDate)} \u2013 ${formatDate(endDate)}`
    : startDate
    ? `${formatDate(startDate)} \u2013 ...`
    : '';

  const handlePrevMonth = useCallback(() => {
    if (viewMonth === 0) {
      setViewMonth(11);
      setViewYear(y => y - 1);
    } else {
      setViewMonth(m => m - 1);
    }
  }, [viewMonth]);

  const handleNextMonth = useCallback(() => {
    if (viewMonth === 11) {
      setViewMonth(0);
      setViewYear(y => y + 1);
    } else {
      setViewMonth(m => m + 1);
    }
  }, [viewMonth]);

  const handleSelectDay = useCallback((day: number) => {
    const selected = new Date(viewYear, viewMonth, day);
    if (minDate && selected < minDate) return;
    if (maxDate && selected > maxDate) return;

    if (selectingStart) {
      if (endDate && selected > endDate) {
        onChange?.(selected, selected);
        setSelectingStart(false);
      } else {
        onChange?.(selected, endDate!);
        setSelectingStart(false);
      }
    } else {
      if (startDate && selected < startDate) {
        onChange?.(selected, startDate);
      } else {
        onChange?.(startDate!, selected);
      }
      setSelectingStart(true);
      setOpen(false);
      inputRef.current?.focus();
    }
  }, [viewYear, viewMonth, selectingStart, startDate, endDate, onChange, minDate, maxDate]);

  const handleOpen = useCallback(() => {
    setSelectingStart(true);
    setOpen(true);
  }, []);

  const today = new Date();
  const weeks = getDaysInMonth(viewYear, viewMonth);

  const inputStyle: React.CSSProperties = {
    width: '100%',
    height: 'var(--input-height-md)',
    padding: '0 var(--space-inline-md)',
    border: 'var(--border-width-thin) solid var(--color-border-default)',
    borderRadius: 'var(--radius-input)',
    background: 'var(--color-bg-surface-default)',
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-body)',
    color: rangeLabel ? 'var(--color-text-primary)' : 'var(--color-text-disabled)',
    cursor: 'pointer',
    boxSizing: 'border-box',
    outline: 'none',
  };

  const popoverStyle: React.CSSProperties = {
    position: 'absolute',
    top: 'calc(100% + 4px)',
    left: 0,
    zIndex: 'var(--z-dropdown)',
    background: 'var(--color-bg-surface-overlay)',
    border: 'var(--border-width-thin) solid var(--color-border-default)',
    borderRadius: 'var(--radius-dropdown)',
    boxShadow: 'var(--shadow-3)',
    padding: 'var(--space-3)',
    minWidth: 260,
  };

  const navBtnStyle: React.CSSProperties = {
    background: 'transparent',
    border: 'none',
    cursor: 'pointer',
    color: 'var(--color-text-secondary)',
    fontSize: 'var(--text-body)',
    padding: 'var(--space-1)',
    borderRadius: 'var(--radius-xs)',
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    width: 28,
    height: 28,
  };

  const dayCellStyle = (day: number | null): React.CSSProperties => {
    if (day === null) return { visibility: 'hidden', width: 32, height: 32 };
    const d = new Date(viewYear, viewMonth, day);
    const isStart = startDate && isSameDay(d, startDate);
    const isEnd = endDate && isSameDay(d, endDate);
    const inRange = isInRange(d, startDate, endDate);
    const isToday = isSameDay(d, today);
    const disabled = (minDate && d < minDate) || (maxDate && d > maxDate);
    let bg = 'transparent';
    let color = disabled ? 'var(--color-text-disabled)' : 'var(--color-text-primary)';
    if (isStart || isEnd) {
      bg = 'var(--color-bg-primary-default)';
      color = 'var(--color-text-on-primary)';
    } else if (inRange) {
      bg = 'var(--color-bg-primary-weak)';
      color = 'var(--color-bg-primary-default)';
    } else if (isToday) {
      bg = 'var(--color-bg-primary-weak)';
    }
    return {
      width: 32,
      height: 32,
      display: 'inline-flex',
      alignItems: 'center',
      justifyContent: 'center',
      borderRadius: isStart || isEnd ? 'var(--radius-xs)' : 0,
      border: 'none',
      background: bg,
      color,
      fontWeight: isStart || isEnd ? 'var(--weight-semibold)' : 'var(--weight-normal)',
      cursor: disabled ? 'not-allowed' : 'pointer',
      fontSize: 'var(--text-body-sm)',
      opacity: disabled ? 'var(--opacity-disabled)' : undefined,
    };
  };

  return (
    <div
      ref={containerRef}
      className={`sk-date-range-picker ${className}`}
      style={{ position: 'relative', display: 'inline-block', ...style }}
    >
      <input
        ref={inputRef}
        type="text"
        readOnly
        value={rangeLabel}
        placeholder={placeholder}
        style={inputStyle}
        onFocus={handleOpen}
        onClick={handleOpen}
        aria-haspopup="dialog"
        aria-expanded={open}
        role="combobox"
      />
      {open && (
        <div style={popoverStyle} role="dialog" aria-label="Date range picker">
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 'var(--space-2)' }}>
            <button
              style={navBtnStyle}
              onClick={handlePrevMonth}
              aria-label="Previous month"
              type="button"
            >
              {'\u276E'}
            </button>
            <span style={{ fontFamily: 'var(--font-family-sans)', fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>
              {MONTHS[viewMonth]} {viewYear}
            </span>
            <button
              style={navBtnStyle}
              onClick={handleNextMonth}
              aria-label="Next month"
              type="button"
            >
              {'\u276F'}
            </button>
          </div>
          <div>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(7, 32px)', gap: 2, marginBottom: 'var(--space-1)' }}>
              {DAYS.map(d => (
                <span key={d} style={{ textAlign: 'center', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', fontWeight: 'var(--weight-medium)' }}>
                  {d}
                </span>
              ))}
            </div>
            {weeks.map((week, wi) => (
              <div key={wi} style={{ display: 'grid', gridTemplateColumns: 'repeat(7, 32px)', gap: 2 }}>
                {week.map((day, di) => {
                  const d = day !== null ? new Date(viewYear, viewMonth, day) : null;
                  const disabled = d && ((minDate && d < minDate) || (maxDate && d > maxDate));
                  return (
                    <button
                      key={`${wi}-${di}`}
                      type="button"
                      style={dayCellStyle(day)}
                      disabled={!day || !!disabled}
                      onClick={() => day !== null && handleSelectDay(day)}
                      aria-label={day ? `${MONTHS[viewMonth]} ${day}, ${viewYear}` : undefined}
                      tabIndex={day ? 0 : -1}
                    >
                      {day ?? ''}
                    </button>
                  );
                })}
              </div>
            ))}
            <p style={{ fontFamily: 'var(--font-family-sans)', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 'var(--space-2) 0 0 0', textAlign: 'center' }}>
              {selectingStart ? 'Select start date' : 'Select end date'}
            </p>
          </div>
        </div>
      )}
    </div>
  );
};

DateRangePicker.displayName = 'DateRangePicker';

export default DateRangePicker;
