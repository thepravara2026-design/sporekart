import React, { useRef, useEffect } from 'react';

const FOCUSABLE_SELECTOR =
  'a[href], button:not([disabled]), textarea:not([disabled]), input:not([disabled]), select:not([disabled]), [tabindex]:not([tabindex="-1"])';

export interface FocusTrapProps {
  children: React.ReactNode;
  active?: boolean;
  returnFocus?: boolean;
  initialFocus?: 'first' | 'last' | (() => HTMLElement | null);
}

export function FocusTrap({
  children,
  active = true,
  returnFocus = true,
  initialFocus = 'first',
}: FocusTrapProps) {
  const containerRef = useRef<HTMLDivElement>(null);
  const previousActiveElement = useRef<HTMLElement | null>(null);

  useEffect(() => {
    if (!active) return;

    const container = containerRef.current;
    if (!container) return;

    previousActiveElement.current = document.activeElement as HTMLElement;

    const getFocusable = () =>
      Array.from(container.querySelectorAll<HTMLElement>(FOCUSABLE_SELECTOR));

    const initialFocusables = getFocusable();
    if (initialFocusables.length > 0) {
      if (initialFocus === 'last') {
        initialFocusables[initialFocusables.length - 1].focus();
      } else if (typeof initialFocus === 'function') {
        const el = initialFocus();
        if (el) el.focus();
      } else {
        initialFocusables[0].focus();
      }
    }

    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key !== 'Tab') return;
      const focusable = getFocusable();
      if (focusable.length === 0) {
        e.preventDefault();
        return;
      }
      const first = focusable[0];
      const last = focusable[focusable.length - 1];
      if (e.shiftKey) {
        if (document.activeElement === first) {
          e.preventDefault();
          last.focus();
        }
      } else {
        if (document.activeElement === last) {
          e.preventDefault();
          first.focus();
        }
      }
    };

    document.addEventListener('keydown', handleKeyDown);

    return () => {
      document.removeEventListener('keydown', handleKeyDown);
      if (returnFocus && previousActiveElement.current) {
        previousActiveElement.current.focus();
      }
    };
  }, [active, returnFocus, initialFocus]);

  return <div ref={containerRef} style={{ outline: 'none' }} tabIndex={-1}>{children}</div>;
}

FocusTrap.displayName = 'FocusTrap';
