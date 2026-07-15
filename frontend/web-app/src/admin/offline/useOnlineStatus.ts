import { useState, useEffect } from 'react';
import type { OnlineStatus } from './types';

export function useOnlineStatus() {
  const [status, setStatus] = useState<OnlineStatus>(navigator.onLine ? 'online' : 'offline');

  useEffect(() => {
    const handleOnline = () => setStatus('online');
    const handleOffline = () => setStatus('offline');
    window.addEventListener('online', handleOnline);
    window.addEventListener('offline', handleOffline);
    return () => {
      window.removeEventListener('online', handleOnline);
      window.removeEventListener('offline', handleOffline);
    };
  }, []);

  const isOnline = status === 'online';
  const isOffline = status === 'offline';

  return { status, isOnline, isOffline };
}
