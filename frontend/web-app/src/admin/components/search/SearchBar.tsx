import { useState, useRef, useCallback, useEffect, memo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';

interface SearchBarProps {
  value: string;
  onChange: (value: string) => void;
  placeholder?: string;
  debounceMs?: number;
  instant?: boolean;
  onFocus?: () => void;
  onBlur?: () => void;
  className?: string;
  autoFocus?: boolean;
}

export const SearchBar = memo(function SearchBar({
  value,
  onChange,
  placeholder = 'Search...',
  debounceMs = 300,
  instant = false,
  onFocus,
  onBlur,
  className,
  autoFocus,
}: SearchBarProps) {
  const [localValue, setLocalValue] = useState(value);
  const timerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const inputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    setLocalValue(value);
  }, [value]);

  const handleChange = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      const v = e.target.value;
      setLocalValue(v);
      if (instant) {
        onChange(v);
      } else {
        if (timerRef.current) clearTimeout(timerRef.current);
        timerRef.current = setTimeout(() => onChange(v), debounceMs);
      }
    },
    [onChange, debounceMs, instant]
  );

  const handleClear = useCallback(() => {
    setLocalValue('');
    onChange('');
    inputRef.current?.focus();
  }, [onChange]);

  const handleKeyDown = useCallback(
    (e: React.KeyboardEvent) => {
      if (e.key === 'Enter') {
        onChange(localValue);
      }
      if (e.key === 'Escape') {
        handleClear();
      }
    },
    [onChange, localValue, handleClear]
  );

  const handleFocus = useCallback(
    (e: React.FocusEvent<HTMLInputElement>) => {
      e.target.style.borderColor = 'var(--color-primary)';
      e.target.style.boxShadow = '0 0 0 3px var(--color-primary-alpha)';
      onFocus?.();
    },
    [onFocus]
  );

  const handleBlur = useCallback(
    (e: React.FocusEvent<HTMLInputElement>) => {
      e.target.style.borderColor = 'var(--color-border)';
      e.target.style.boxShadow = 'none';
      onBlur?.();
    },
    [onBlur]
  );

  useEffect(() => {
    return () => {
      if (timerRef.current) clearTimeout(timerRef.current);
    };
  }, []);

  return (
    <div
      className={className}
      style={{
        position: 'relative',
        display: 'flex',
        alignItems: 'center',
        width: '100%',
        maxWidth: 400,
      }}
    >
      <span
        style={{
          position: 'absolute',
          left: 12,
          top: '50%',
          transform: 'translateY(-50%)',
          pointerEvents: 'none',
          color: 'var(--color-text-tertiary)',
          display: 'flex',
          alignItems: 'center',
        }}
      >
        <Icon name="search" size={16} />
      </span>
      <input
        ref={inputRef}
        type="text"
        value={localValue}
        onChange={handleChange}
        onKeyDown={handleKeyDown}
        onFocus={handleFocus}
        onBlur={handleBlur}
        placeholder={placeholder}
        autoFocus={autoFocus}
        aria-label={placeholder}
        style={{
          width: '100%',
          padding: '8px 36px 8px 36px',
          border: '1px solid var(--color-border)',
          borderRadius: 'var(--radius-md)',
          background: 'var(--color-surface)',
          color: 'var(--color-text-primary)',
          fontSize: 'var(--text-body)',
          outline: 'none',
          transition: 'border-color 0.15s, box-shadow 0.15s',
        }}
      />
      {localValue && (
        <button
          onClick={handleClear}
          aria-label="Clear search"
          style={{
            position: 'absolute',
            right: 8,
            top: '50%',
            transform: 'translateY(-50%)',
            background: 'none',
            border: 'none',
            cursor: 'pointer',
            color: 'var(--color-text-tertiary)',
            padding: 4,
            display: 'flex',
            alignItems: 'center',
          }}
        >
          <Icon name="x" size={16} />
        </button>
      )}
    </div>
  );
});
