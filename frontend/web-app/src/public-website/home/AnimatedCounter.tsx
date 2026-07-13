import { useEffect, useRef, useState } from 'react';
import { usePrefersReducedMotion } from './usePrefersReducedMotion';

export interface AnimatedCounterProps {
  value?: number;
  text?: string;
  prefix?: string;
  suffix?: string;
  decimals?: number;
  label: string;
  placeholder?: boolean;
}

function formatNumber(value: number, decimals: number): string {
  return value.toLocaleString('en-IN', {
    minimumFractionDigits: decimals,
    maximumFractionDigits: decimals,
  });
}

export function AnimatedCounter({
  value,
  text,
  prefix = '',
  suffix = '',
  decimals = 0,
  label,
  placeholder = false,
}: AnimatedCounterProps) {
  const reduced = usePrefersReducedMotion();
  const ref = useRef<HTMLDivElement>(null);
  const [display, setDisplay] = useState(text ?? (reduced ? String(value ?? 0) : '0'));
  const started = useRef(false);

  useEffect(() => {
    if (text !== undefined) {
      setDisplay(text);
      return;
    }
    if (value === undefined) {
      return;
    }
    if (reduced) {
      setDisplay(prefix + formatNumber(value, decimals) + suffix);
      return;
    }
    const node = ref.current;
    if (!node) {
      return;
    }
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting && !started.current) {
            started.current = true;
            const duration = 1400;
            const start = performance.now();
            const tick = (now: number) => {
              const progress = Math.min(1, (now - start) / duration);
              const eased = 1 - Math.pow(1 - progress, 3);
              const current = value * eased;
              setDisplay(prefix + formatNumber(current, decimals) + suffix);
              if (progress < 1) {
                requestAnimationFrame(tick);
              } else {
                setDisplay(prefix + formatNumber(value, decimals) + suffix);
              }
            };
            requestAnimationFrame(tick);
            observer.unobserve(entry.target);
          }
        });
      },
      { threshold: 0.4 },
    );
    observer.observe(node);
    return () => observer.disconnect();
  }, [text, value, prefix, suffix, decimals, reduced]);

  return (
    <div ref={ref}>
      <span style={{ display: 'block', fontSize: 'var(--text-title-md, 18px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>
        {display}
      </span>
      <span style={{ display: 'block', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)' }}>
        {label}
        {placeholder && (
          <span
            title="Placeholder metric — real data pending"
            style={{ marginLeft: 'var(--space-1, 4px)', fontSize: 'var(--text-body-xs, 12px)', color: 'var(--color-text-muted, #9ca3af)', fontStyle: 'italic' }}
          >
            (placeholder)
          </span>
        )}
      </span>
    </div>
  );
}
