import React from 'react';
import { Card } from './Card';

export interface ProfileCardProps {
  name: string;
  role?: string;
  avatar?: string;
  email?: string;
  location?: string;
  status?: 'online' | 'offline' | 'away' | 'busy';
  loading?: boolean;
  error?: string;
}

const statusColors: Record<string, string> = {
  online: 'var(--color-success)',
  offline: 'var(--color-neutral-400)',
  away: 'var(--color-warning)',
  busy: 'var(--color-danger)',
};

export const ProfileCard: React.FC<ProfileCardProps> = ({
  name,
  role,
  avatar,
  email,
  location,
  status,
  loading = false,
  error,
}) => {
  const initials = name
    .split(' ')
    .map((n) => n[0])
    .join('')
    .toUpperCase()
    .slice(0, 2);

  return (
    <Card variant="default" padding="lg" loading={loading} error={error}>
      <div
        className="sk-profilecard__body"
        style={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          textAlign: 'center',
          gap: 'var(--space-stack-sm)',
        }}
      >
        <div
          className="sk-profilecard__avatar-wrapper"
          style={{ position: 'relative', display: 'inline-flex' }}
        >
          {avatar ? (
            <img
              src={avatar}
              alt={name}
              className="sk-profilecard__avatar"
              style={{
                width: 'var(--illustration-md)',
                height: 'var(--illustration-md)',
                borderRadius: 'var(--radius-avatar)',
                objectFit: 'cover',
              }}
            />
          ) : (
            <div
              className="sk-profilecard__avatar-placeholder"
              style={{
                width: 'var(--illustration-md)',
                height: 'var(--illustration-md)',
                borderRadius: 'var(--radius-avatar)',
                background: 'var(--color-bg-primary-weak)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                fontSize: 'var(--text-h2)',
                fontWeight: 'var(--weight-bold)',
                color: 'var(--color-bg-primary-default)',
              }}
            >
              {initials}
            </div>
          )}
          {status && (
            <span
              className="sk-profilecard__status-dot"
              style={{
                position: 'absolute',
                bottom: 2,
                right: 2,
                width: 12,
                height: 12,
                borderRadius: 'var(--radius-full)',
                background: statusColors[status],
                border: '2px solid var(--color-bg-surface-default)',
              }}
              aria-label={`Status: ${status}`}
            />
          )}
        </div>
        <div>
          <h3
            className="sk-profilecard__name"
            style={{
              fontSize: 'var(--text-h5)',
              fontWeight: 'var(--weight-semibold)',
              color: 'var(--color-text-primary)',
              margin: 0,
            }}
          >
            {name}
          </h3>
          {role && (
            <p
              className="sk-profilecard__role"
              style={{
                fontSize: 'var(--text-body-sm)',
                color: 'var(--color-text-secondary)',
                margin: 'var(--space-1) 0 0 0',
              }}
            >
              {role}
            </p>
          )}
        </div>
        {(email || location) && (
          <div
            className="sk-profilecard__details"
            style={{
              display: 'flex',
              flexDirection: 'column',
              gap: 'var(--space-stack-xs)',
              fontSize: 'var(--text-caption)',
              color: 'var(--color-text-secondary)',
              width: '100%',
              borderTop: 'var(--border-width-thin) solid var(--color-border-default)',
              paddingTop: 'var(--space-stack-sm)',
              marginTop: 'var(--space-stack-xs)',
            }}
          >
            {email && <span>{email}</span>}
            {location && <span>{location}</span>}
          </div>
        )}
      </div>
    </Card>
  );
};

ProfileCard.displayName = 'ProfileCard';

export default ProfileCard;
