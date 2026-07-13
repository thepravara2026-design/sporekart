import type { ReactNode } from 'react';
import { useEffect, useRef, useState } from 'react';
import { usePrefersReducedMotion } from './usePrefersReducedMotion';

export function MotionStyles() {
  return (
    <style>{`
      .sk-reveal {
        opacity: 0;
        transform: translateY(18px);
        transition: opacity 0.6s ease, transform 0.6s ease;
        will-change: opacity, transform;
      }
      .sk-reveal.is-visible {
        opacity: 1;
        transform: none;
      }
      .sk-reveal-delay-1 { transition-delay: 0.08s; }
      .sk-reveal-delay-2 { transition-delay: 0.16s; }
      .sk-reveal-delay-3 { transition-delay: 0.24s; }
      @media (prefers-reduced-motion: reduce) {
        .sk-reveal {
          opacity: 1 !important;
          transform: none !important;
          transition: none !important;
        }
      }
    `}</style>
  );
}

export interface RevealProps {
  children: ReactNode;
  delay?: 0 | 1 | 2 | 3;
  className?: string;
  id?: string;
}

export function Reveal({ children, delay = 0, className = '', id }: RevealProps) {
  const ref = useRef<HTMLDivElement>(null);
  const [visible, setVisible] = useState(false);
  const reduced = usePrefersReducedMotion();

  useEffect(() => {
    if (reduced) {
      setVisible(true);
      return;
    }
    const node = ref.current;
    if (!node) {
      return;
    }
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            setVisible(true);
            observer.unobserve(entry.target);
          }
        });
      },
      { threshold: 0.12, rootMargin: '0px 0px -8% 0px' },
    );
    observer.observe(node);
    return () => observer.disconnect();
  }, [reduced]);

  const classes = [
    'sk-reveal',
    delay ? `sk-reveal-delay-${delay}` : '',
    visible ? 'is-visible' : '',
    className,
  ]
    .filter(Boolean)
    .join(' ');

  return (
    <div ref={ref} id={id} className={classes}>
      {children}
    </div>
  );
}
