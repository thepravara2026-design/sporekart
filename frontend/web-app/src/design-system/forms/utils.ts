import { useCallback, useRef, useEffect } from 'react';

export function useDebounce<T extends (...args: any[]) => any>(fn: T, delay: number): T {
  const timer = useRef<ReturnType<typeof setTimeout>>();
  const fnRef = useRef(fn);
  fnRef.current = fn;
  
  useEffect(() => {
    return () => { if (timer.current) clearTimeout(timer.current); };
  }, []);

  return useCallback((...args: any[]) => {
    if (timer.current) clearTimeout(timer.current);
    timer.current = setTimeout(() => fnRef.current(...args), delay);
  }, [delay]) as T;
}

export function isFormValid(errors: Record<string, string | undefined>): boolean {
  return Object.values(errors).every(e => !e);
}

export function getFirstError(errors: Record<string, string | undefined>): { field: string; message: string } | null {
  const entry = Object.entries(errors).find(([_, e]) => e);
  return entry ? { field: entry[0], message: entry[1]! } : null;
}

export function focusFirstInvalidField(errors: Record<string, string | undefined>) {
  const first = getFirstError(errors);
  if (first) {
    const el = document.querySelector(`[name="${first.field}"]`) as HTMLElement;
    el?.focus();
  }
}
