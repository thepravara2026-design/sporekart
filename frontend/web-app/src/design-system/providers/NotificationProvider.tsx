import React, { createContext, useContext, useState, useCallback } from 'react';

const NotificationContext = createContext<any>(undefined);

export interface Notification {
  id: string;
  type: 'success' | 'error' | 'warning' | 'info';
  title: string;
  message?: string;
  duration?: number;
  action?: {
    label: string;
    onClick: () => void;
  };
  persistent?: boolean;
}

export function NotificationProvider({ children }: { children: React.ReactNode }) {
  const [notifications, setNotifications] = useState<any[]>([]);

  const addNotification = useCallback((notification: any) => {
    const id = Math.random().toString(36).substring(2, 9);
    const newNotification = { ...notification, id, timestamp: Date.now() };
    setNotifications((prev) => [...prev, newNotification]);
    return id;
  }, []);

  const removeNotification = useCallback((id: string) => {
    setNotifications((prev) => prev.filter((n) => n.id !== id));
  }, []);

  const clearNotifications = useCallback(() => {
    setNotifications([]);
  }, []);

  return (
    <NotificationContext.Provider
      value={{
        notifications,
        addNotification,
        removeNotification,
        clearNotifications,
      }}
    >
      {children}
      <NotificationList notifications={notifications} onRemove={removeNotification} />
    </NotificationContext.Provider>
  );
}

function NotificationList({ notifications, onRemove }: { notifications: any[]; onRemove: (id: string) => void }) {
  if (notifications.length === 0) return null;

  return (
    <div className="sk-notification-container" role="region" aria-label="Notifications" aria-live="polite">
      {notifications.map((notification) => (
        <NotificationItem key={notification.id} notification={notification} onRemove={onRemove} />
      ))}
    </div>
  );
}

function NotificationItem({ notification, onRemove: _onRemove }: { notification: any; onRemove: (id: string) => void }) {
  const icons = {
    success: '✅',
    error: '❌',
    warning: '⚠️',
    info: 'ℹ️',
  };

  return (
        <div className={`sk-notification sk-notification--${notification.type}`} role="alert">
          <div className="sk-notification__icon" aria-hidden="true">
            {icons[notification.type as keyof typeof icons]}
      </div>
      <div className="sk-notification__content">
        <strong className="sk-notification__title">{notification.title}</strong>
        {notification.message && <p className="sk-notification__message">{notification.message}</p>}
      </div>
      {notification.action && (
        <button
          className="sk-notification__action"
          onClick={() => {
            notification.action.onClick();
            if (!notification.persistent) {
              // Remove after action
            }
          }}
        >
          {notification.action.label}
        </button>
      )}
      <button
        className="sk-notification__close"
        onClick={() => _onRemove(notification.id)}
        aria-label="Dismiss"
      >
        ×
      </button>
    </div>
  );
}

export function useNotifications() {
  const context = useContext(NotificationContext);
  if (!context) {
    throw new Error('useNotifications must be used within a NotificationProvider');
  }
  return context;
}