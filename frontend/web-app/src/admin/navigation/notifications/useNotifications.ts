import { useState, useCallback, useEffect } from 'react';
import type { NavNotification } from '../types';

const STORAGE_KEY = 'nav_notifications';

function loadNotifications(): NavNotification[] {
  try { return JSON.parse(localStorage.getItem(STORAGE_KEY) ?? '[]'); } catch { return []; }
}

function saveNotifications(items: NavNotification[]) {
  try { localStorage.setItem(STORAGE_KEY, JSON.stringify(items)); } catch {}
}

export function useNotifications(initial: NavNotification[] = []) {
  const [notifications, setNotifications] = useState<NavNotification[]>(() => {
    const stored = loadNotifications();
    return stored.length > 0 ? stored : initial;
  });

  useEffect(() => { saveNotifications(notifications); }, [notifications]);

  const unreadCount = notifications.filter((n) => !n.read).length;

  const markRead = useCallback((id: string) => {
    setNotifications((prev) => prev.map((n) => (n.id === id ? { ...n, read: true } : n)));
  }, []);

  const markAllRead = useCallback(() => {
    setNotifications((prev) => prev.map((n) => ({ ...n, read: true })));
  }, []);

  const dismiss = useCallback((id: string) => {
    setNotifications((prev) => prev.filter((n) => n.id !== id));
  }, []);

  const addNotification = useCallback((n: NavNotification) => {
    setNotifications((prev) => [n, ...prev]);
  }, []);

  return { notifications, unreadCount, markRead, markAllRead, dismiss, addNotification };
}
