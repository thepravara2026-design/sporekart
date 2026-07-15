import { useCallback, useEffect, useRef, useState } from 'react';

export interface CountdownState {
  seconds: number;
  active: boolean;
  start: (seconds?: number) => void;
  reset: () => void;
}

/**
 * Drives the OTP "resend in Ns" countdown. When seconds reaches 0 the
 * countdown is inactive and the resend control becomes enabled.
 */
export function useCountdown(initial = 30): CountdownState {
  const [seconds, setSeconds] = useState(initial);
  const [active, setActive] = useState(true);
  const timer = useRef<ReturnType<typeof setInterval> | null>(null);

  const clear = useCallback(() => {
    if (timer.current) {
      clearInterval(timer.current);
      timer.current = null;
    }
  }, []);

  const start = useCallback(
    (value = initial) => {
      clear();
      setSeconds(value);
      setActive(true);
      timer.current = setInterval(() => {
        setSeconds((s) => {
          if (s <= 1) {
            clear();
            setActive(false);
            return 0;
          }
          return s - 1;
        });
      }, 1000);
    },
    [clear, initial],
  );

  const reset = useCallback(() => {
    clear();
    setSeconds(initial);
    setActive(false);
  }, [clear, initial]);

  useEffect(() => clear, [clear]);

  return { seconds, active, start, reset };
}
