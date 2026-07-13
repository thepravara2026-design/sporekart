import React, { useState, useRef, useEffect, useCallback } from 'react';

export interface DatePickerProps {
  value?: Date;
  onChange?: (date: Date) => void;
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

export const DatePicker: React.FC<DatePickerProps> = ({
  value,
  onChange,
  minDate,
  maxDate,
  placeholder = 'Select date',
  className = '',
  style,
}) => {
  const [open, setOpen] = useState(false);
  const [viewYear, setViewYear] = useState(value?.getFullYear() ?? new Date().getFullYear());
  const [viewMonth, setViewMonth] = useState(value?.getMonth() ?? new Date().getMonth());
  const containerRef = useRef<HTMLDivElement>(null);
  const inputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    if (value) {
      setViewYear(value.getFullYear());
      setViewMonth(value.getMonth());
    }
  }, [value]);

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
    onChange?.(selected);
    setOpen(false);
    inputRef.current?.focus();
  }, [viewYear, viewMonth, onChange, minDate, maxDate]);

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
    color: value ? 'var(--color-text-primary)' : 'var(--color-text-disabled)',
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
    const isSelected = value && day !== null && isSameDay(value, new Date(viewYear, viewMonth, day));
    const isToday = day !== null && isSameDay(today, new Date(viewYear, viewMonth, day));
    const disabled = day === null || (minDate && new Date(viewYear, viewMonth, day) < minDate) || (maxDate && new Date(viewYear, viewMonth, day) > maxDate);
    return {
      width: 32,
      height: 32,
      display: 'inline-flex',
      alignItems: 'center',
      justifyContent: 'center',
      borderRadius: 'var(--radius-xs)',
      border: 'none',
      background: isSelected ? 'var(--color-bg-primary-default)' : isToday ? 'var(--color-bg-primary-weak)' : 'transparent',
      color: isSelected ? 'var(--color-text-on-primary)' : disabled ? 'var(--color-text-disabled)' : 'var(--color-text-primary)',
      fontWeight: isSelected ? 'var(--weight-semibold)' : 'var(--weight-normal)',
      cursor: disabled ? 'not-allowed' : 'pointer',
      fontSize: 'var(--text-body-sm)',
      opacity: disabled ? 'var(--opacity-disabled)' : undefined,
    };
  };

  return (
    <div
      ref={containerRef}
      className={`sk-date-picker ${className}`}
      style={{ position: 'relative', display: 'inline-block', ...style }}
    >
      <input
        ref={inputRef}
        type="text"
        readOnly
        value={value ? formatDate(value) : ''}
        placeholder={placeholder}
        style={inputStyle}
        onFocus={() => setOpen(true)}
        onClick={() => setOpen(true)}
        aria-haspopup="dialog"
        aria-expanded={open}
        role="combobox"
      />
      {open && (
        <div style={popoverStyle} role="dialog" aria-label="Date picker">
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
                {week.map((day, di) => (
                  <button
                    key={`${wi}-${di}`}
                    type="button"
                    style={dayCellStyle(day)}
                    disabled={day === null || (minDate && new Date(viewYear, viewMonth, day) < minDate) || (maxDate && new Date(viewYear, viewMonth, day) > maxDate)}
                    onClick={() => day !== null && handleSelectDay(day)}
                    aria-label={day ? `${MONTHS[viewMonth]} ${day}, ${viewYear}` : undefined}
                    tabIndex={day ? 0 : -1}
                  >
                    {day ?? ''}
                  </button>
                ))}
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

DatePicker.displayName = 'DatePicker';

export default DatePicker;
