import React, { useState, useMemo } from 'react';

export interface AvatarProps {
  src?: string;
  alt?: string;
  initials?: string;
  name?: string;
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl' | '2xl';
  status?: 'online' | 'offline' | 'busy' | 'away';
  className?: string;
  onClick?: () => void;
  disabled?: boolean;
  loading?: boolean;
  error?: boolean;
}

const sizeMap: Record<string, number> = {
  xs: 24,
  sm: 32,
  md: 40,
  lg: 48,
  xl: 56,
  '2xl': 72,
};

const statusColors: Record<string, string> = {
  online: 'var(--color-success)',
  offline: 'var(--color-neutral-400)',
  busy: 'var(--color-danger)',
  away: 'var(--color-warning)',
};

function getInitials(name?: string, _initials?: string): string {
  if (_initials) return _initials.slice(0, 2).toUpperCase();
  if (name) {
    const parts = name.trim().split(/\s+/);
    if (parts.length >= 2) return (parts[0][0] + parts[1][0]).toUpperCase();
    return parts[0].slice(0, 2).toUpperCase();
  }
  return '?';
}

export const Avatar: React.FC<AvatarProps> = ({
  src,
  alt = '',
  initials,
  name,
  size = 'md',
  status,
  className = '',
  onClick,
  disabled = false,
  loading = false,
  error = false,
}) => {
  const [imgError, setImgError] = useState(false);
  const dim = sizeMap[size];

  const showImage = src && !imgError && !loading && !error;
  const showInitials = !showImage || error;

  const derivedInitials = useMemo(() => getInitials(name, initials), [name, initials]);

  const containerStyle: React.CSSProperties = {
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    width: dim,
    height: dim,
    borderRadius: 'var(--radius-full)',
    backgroundColor: 'var(--color-bg-primary-default)',
    color: 'var(--color-text-on-primary)',
    overflow: 'hidden',
    flexShrink: 0,
    position: 'relative',
    userSelect: 'none',
    cursor: onClick ? 'pointer' : undefined,
    opacity: disabled ? 'var(--opacity-disabled)' : 1,
    ...(onClick && !disabled ? {
      outline: 'none',
      transition: `box-shadow var(--duration-fast) var(--easing-standard)`,
    } : {}),
  };

  const focusHandler = onClick && !disabled ? (e: React.FocusEvent<HTMLSpanElement>) => {
    e.currentTarget.style.boxShadow = `0 0 0 var(--focus-ring-width) var(--focus-ring-color)`;
  } : undefined;

  const blurHandler = onClick && !disabled ? (e: React.FocusEvent<HTMLSpanElement>) => {
    e.currentTarget.style.boxShadow = 'none';
  } : undefined;

  const initialsStyle: React.CSSProperties = {
    fontSize: dim * 0.4,
    fontWeight: 'var(--weight-semibold)',
    lineHeight: 1,
    textTransform: 'uppercase',
  };

  const statusDotStyle: React.CSSProperties | undefined = status ? {
    position: 'absolute',
    bottom: 0,
    right: 0,
    width: dim * 0.3,
    height: dim * 0.3,
    borderRadius: 'var(--radius-full)',
    backgroundColor: statusColors[status],
    border: `2px solid var(--color-bg-surface-default)`,
    boxSizing: 'content-box',
  } : undefined;

  const skeletonStyle: React.CSSProperties = {
    width: dim,
    height: dim,
    borderRadius: 'var(--radius-full)',
    backgroundColor: 'var(--color-neutral-200)',
    animation: 'sk-pulse 1.5s ease-in-out infinite',
  };

  if (loading) {
    return (
      <span
        className={`sk-avatar sk-avatar--loading ${className}`.trim()}
        style={containerStyle}
        role="status"
        aria-label="Loading avatar"
      >
        <span style={skeletonStyle} />
        <style>{`@keyframes sk-pulse { 0%, 100% { opacity: 0.4; } 50% { opacity: 0.8; } }`}</style>
      </span>
    );
  }

  return (
    <span
      className={`sk-avatar ${className}`.trim()}
      style={containerStyle as React.CSSProperties}
      onClick={disabled ? undefined : onClick}
      onFocus={focusHandler as unknown as React.FocusEventHandler<HTMLSpanElement>}
      onBlur={blurHandler as unknown as React.FocusEventHandler<HTMLSpanElement>}
      tabIndex={onClick && !disabled ? 0 : undefined}
      role={onClick ? 'button' : undefined}
      aria-disabled={disabled}
      aria-label={alt || name || 'Avatar'}
    >
      {showImage && (
        <img
          src={src}
          alt={alt || name || ''}
          style={{
            width: '100%',
            height: '100%',
            objectFit: 'cover',
            display: 'block',
          }}
          onError={() => setImgError(true)}
        />
      )}
      {showInitials && (
        <span style={initialsStyle}>
          {error ? '!' : derivedInitials}
        </span>
      )}
      {statusDotStyle && <span style={statusDotStyle} />}
    </span>
  );
};

Avatar.displayName = 'Avatar';
export default Avatar;

export interface AvatarGroupProps {
  avatars: { src?: string; initials?: string; name?: string; alt?: string }[];
  max?: number;
  size?: AvatarProps['size'];
  className?: string;
}

export const AvatarGroup: React.FC<AvatarGroupProps> = ({
  avatars,
  max = 4,
  size = 'md',
  className = '',
}) => {
  const dim = sizeMap[size];
  const visible = avatars.slice(0, max);
  const overflow = avatars.length - max;

  const containerStyle: React.CSSProperties = {
    display: 'inline-flex',
    alignItems: 'center',
  };

  const overlapStyle: React.CSSProperties = {
    marginLeft: -dim * 0.2,
  };

  const overflowStyle: React.CSSProperties = {
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    width: dim,
    height: dim,
    borderRadius: 'var(--radius-full)',
    backgroundColor: 'var(--color-neutral-300)',
    color: 'var(--color-text-primary)',
    fontSize: dim * 0.35,
    fontWeight: 'var(--weight-semibold)',
    marginLeft: -dim * 0.2,
    flexShrink: 0,
  };

  return (
    <span className={`sk-avatar-group ${className}`.trim()} style={containerStyle}>
      {visible.map((avatar, i) => (
        <span key={i} style={{ ...containerStyle, ...(i > 0 ? overlapStyle : {}), zIndex: visible.length - i }}>
          <Avatar
            src={avatar.src}
            initials={avatar.initials}
            name={avatar.name}
            alt={avatar.alt}
            size={size}
          />
        </span>
      ))}
      {overflow > 0 && (
        <span style={overflowStyle} aria-label={`${overflow} more`}>
          +{overflow}
        </span>
      )}
    </span>
  );
};

AvatarGroup.displayName = 'AvatarGroup';
