import React from 'react';
import { Card } from './Card';

export interface NotificationCardProps {
  title: string;
  message: string;
  time?: string;
  type?: 'info' | 'success' | 'warning' | 'error';
  read?: boolean;
  icon?: React.ReactNode;
  onDismiss?: () => void;
  onClick?: () => void;
  loading?: boolean;
}

const typeColors: Record<string, string> = {
  info: 'var(--color-info)',
  success: 'var(--color-success)',
  warning: 'var(--color-warning)',
  error: 'var(--color-danger)',
};

export const NotificationCard: React.FC<NotificationCardProps> = ({
  title,
  message,
  time,
  type = 'info',
  read = false,
  icon,
  onDismiss,
  onClick,
  loading = false,
}) => {
  return (
    <Card
      variant="default"
      padding="md"
      onClick={onClick}
      hoverable={!!onClick}
      className={!read ? 'sk-notificationcard--unread' : ''}
      loading={loading}
    >
      <div
        className="sk-notificationcard__body"
        style={{
          display: 'flex',
          alignItems: 'flex-start',
          gap: 'var(--space-inline-md)',
        }}
      >
        {!read && (
          <span
            className="sk-notificationcard__unread-dot"
            style={{
              width: 8,
              height: 8,
              borderRadius: 'var(--radius-full)',
              background: typeColors[type],
              flexShrink: 0,
              marginTop: 6,
            }}
          />
        )}
        <div
          className="sk-notificationcard__icon"
          style={{
            width: 32,
            height: 32,
            borderRadius: 'var(--radius-full)',
            background: 'var(--color-bg-surface-default)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            flexShrink: 0,
            color: typeColors[type],
            fontSize: 'var(--icon-sm)',
          }}
        >
          {icon || (
            <span style={{ fontWeight: 'var(--weight-bold)' }}>
              {type === 'error' ? '\u2717' : '\u2139'}
            </span>
          )}
        </div>
        <div
          className="sk-notificationcard__content"
          style={{ flex: 1, minWidth: 0 }}
        >
          <div
            className="sk-notificationcard__header"
            style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              gap: 'var(--space-inline-sm)',
            }}
          >
            <h4
              className="sk-notificationcard__title"
              style={{
                fontSize: 'var(--text-body)',
                fontWeight: read ? 'var(--weight-normal)' : 'var(--weight-semibold)',
                color: 'var(--color-text-primary)',
                margin: 0,
                overflow: 'hidden',
                textOverflow: 'ellipsis',
                whiteSpace: 'nowrap',
              }}
            >
              {title}
            </h4>
            {onDismiss && (
              <button
                className="sk-notificationcard__dismiss"
                style={{
                  background: 'none',
                  border: 'none',
                  cursor: 'pointer',
                  color: 'var(--color-text-disabled)',
                  fontSize: 'var(--text-body)',
                  padding: 0,
                  lineHeight: 1,
                  flexShrink: 0,
                }}
                onClick={(e) => {
                  e.stopPropagation();
                  onDismiss();
                }}
                aria-label="Dismiss notification"
              >
                {'\u2715'}
              </button>
            )}
          </div>
          <p
            className="sk-notificationcard__message"
            style={{
              fontSize: 'var(--text-body-sm)',
              color: 'var(--color-text-secondary)',
              margin: 'var(--space-1) 0 0 0',
              lineHeight: 'var(--leading-normal)',
              overflow: 'hidden',
              textOverflow: 'ellipsis',
              display: '-webkit-box',
              WebkitLineClamp: 2,
              WebkitBoxOrient: 'vertical',
            }}
          >
            {message}
          </p>
          {time && (
            <span
              className="sk-notificationcard__time"
              style={{
                fontSize: 'var(--text-caption)',
                color: 'var(--color-text-disabled)',
                marginTop: 'var(--space-1)',
                display: 'block',
              }}
            >
              {time}
            </span>
          )}
        </div>
      </div>
    </Card>
  );
};

NotificationCard.displayName = 'NotificationCard';

export default NotificationCard;
