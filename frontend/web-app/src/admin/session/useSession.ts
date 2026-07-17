import { useState, useCallback, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import type { SessionState, SessionConfig, SessionInfo } from './types';

const DEFAULT_CONFIG: SessionConfig = {
  timeoutDuration: 30 * 60 * 1000,
  warningDuration: 2 * 60 * 1000,
};

export function useSession(config: SessionConfig = DEFAULT_CONFIG) {
  const navigate = useNavigate();
  const [state, setState] = useState<SessionState>('active');
  const [lastActivity, setLastActivity] = useState(Date.now());
  const [savedPage, setSavedPage] = useState<string | null>(() => {
    try { return sessionStorage.getItem('last_admin_page'); } catch { return null; }
  });
  const warningTimer = useRef<ReturnType<typeof setTimeout>>();
  const expireTimer = useRef<ReturnType<typeof setTimeout>>();

  const resetTimers = useCallback(() => {
    clearTimeout(warningTimer.current);
    clearTimeout(expireTimer.current);
    setState('active');
    setLastActivity(Date.now());

    warningTimer.current = setTimeout(() => {
      setState('timeout_warning');
    }, config.timeoutDuration - config.warningDuration);

    expireTimer.current = setTimeout(() => {
      setState('expired');
    }, config.timeoutDuration);
  }, [config]);

  useEffect(() => {
    resetTimers();
    const handleActivity = () => resetTimers();
    window.addEventListener('mousedown', handleActivity);
    window.addEventListener('keydown', handleActivity);
    window.addEventListener('touchstart', handleActivity);
    return () => {
      window.removeEventListener('mousedown', handleActivity);
      window.removeEventListener('keydown', handleActivity);
      window.removeEventListener('touchstart', handleActivity);
      clearTimeout(warningTimer.current);
      clearTimeout(expireTimer.current);
    };
  }, [resetTimers]);

  const saveCurrentPage = useCallback((path: string) => {
    try { sessionStorage.setItem('last_admin_page', path); } catch {}
    setSavedPage(path);
  }, []);

  const extendSession = useCallback(() => {
    resetTimers();
  }, [resetTimers]);

  const endSession = useCallback(() => {
    clearTimeout(warningTimer.current);
    clearTimeout(expireTimer.current);
    setState('expired');
    /**
     * BUG-RT-011: previously the session only flipped an internal state and
     * relied on a preview component to surface it. Now expiry redirects to the
     * dedicated /session-expired route so the user is unambiguously signed out
     * of the view regardless of which screen they are on.
     */
    try {
      sessionStorage.removeItem('sk_session_role');
      window.dispatchEvent(
        new StorageEvent('storage', { key: 'sk_session_role', newValue: null }),
      );
    } catch {
      /* storage unavailable */
    }
    navigate('/session-expired', { replace: true });
  }, [navigate]);

  const getSessionInfo = useCallback((): SessionInfo => ({
    state,
    lastActivity: lastActivity,
    expiresAt: lastActivity + config.timeoutDuration,
    idleThreshold: config.timeoutDuration - config.warningDuration,
  }), [state, lastActivity, config]);

  return {
    state,
    savedPage,
    saveCurrentPage,
    extendSession,
    endSession,
    getSessionInfo,
    isInactive: state === 'idle',
    isWarning: state === 'timeout_warning',
    isExpired: state === 'expired',
  };
}
