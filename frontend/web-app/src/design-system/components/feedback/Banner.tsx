import React from 'react';

export type BannerType = 'announcement' | 'maintenance' | 'update' | 'warning' | 'offline' | 'cookie';

export interface BannerProps {
  type?: BannerType;
  message?: string;
  children?: React.ReactNode;
  onClose?: () => void;
  fixed?: boolean;
  position?: 'top' | 'bottom';
  className?: string;
  style?: React.CSSProperties;
}

function AnnouncementIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
      <path d="M3 11a2 2 0 014 0v4a2 2 0 01-4 0v-4z" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
      <path d="M7 11l7-3v8l-7-3" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
      <path d="M14 8.5a4 4 0 000-7" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
    </svg>
  );
}

function MaintenanceIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
      <path d="M14.7 6.3a1 1 0 000 1.4l1.6 1.6a1 1 0 001.4 0l3.77-3.77a6 6 0 01-7.94 7.94l-6.91 6.91a2.12 2.12 0 01-3-3l6.91-6.91a6 6 0 017.94-7.94l-3.76 3.76z" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
    </svg>
  );
}

function UpdateIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
      <path d="M10 2a8 8 0 100 16 8 8 0 000-16z" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
      <path d="M10 6v5l3 2" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
    </svg>
  );
}

function WarningTriangleIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
      <path d="M10 2L2 17h16L10 2z" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
      <path d="M10 7.5v3.5M10 13.5h0" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
    </svg>
  );
}

function OfflineIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
      <line x1="1" y1="1" x2="19" y2="19" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
      <path d="M14.72 9.06A10.94 10.94 0 0117 10.55" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
      <path d="M3 10.55A10.94 10.94 0 016.32 6.69" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
      <path d="M1.52 6.86A15.9 15.9 0 018.08 2.89" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
      <path d="M20.48 6.86A15.9 15.9 0 0012 2.89" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
      <line x1="10" y1="18" x2="10.01" y2="18" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
    </svg>
  );
}

function CookieIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
      <circle cx="10" cy="10" r="8" stroke="currentColor" strokeWidth="1.5" />
      <circle cx="7.5" cy="7.5" r="1" fill="currentColor" />
      <circle cx="12" cy="8.5" r="1" fill="currentColor" />
      <circle cx="10" cy="13" r="1" fill="currentColor" />
      <circle cx="7" cy="11.5" r="0.5" fill="currentColor" />
    </svg>
  );
}

const iconMap: Record<BannerType, React.ReactNode> = {
  announcement: <AnnouncementIcon />,
  maintenance: <MaintenanceIcon />,
  update: <UpdateIcon />,
  warning: <WarningTriangleIcon />,
  offline: <OfflineIcon />,
  cookie: <CookieIcon />,
};

const bannerColors: Record<BannerType, { bg: string; text: string; icon: string }> = {
  announcement: {
    bg: 'var(--color-green-50)',
    text: 'var(--color-green-900)',
    icon: 'var(--color-green-600)',
  },
  maintenance: {
    bg: 'var(--color-warning-50)',
    text: 'var(--color-warning-700)',
    icon: 'var(--color-warning-600)',
  },
  update: {
    bg: 'var(--color-info-50)',
    text: 'var(--color-info-700)',
    icon: 'var(--color-info-600)',
  },
  warning: {
    bg: 'var(--color-danger-50)',
    text: 'var(--color-danger-700)',
    icon: 'var(--color-danger-600)',
  },
  offline: {
    bg: 'var(--color-neutral-100)',
    text: 'var(--color-neutral-900)',
    icon: 'var(--color-neutral-600)',
  },
  cookie: {
    bg: 'var(--color-neutral-900)',
    text: '#FFFFFF',
    icon: '#FFFFFF',
  },
};

export const Banner: React.FC<BannerProps> = ({
  type = 'announcement',
  message,
  children,
  onClose,
  fixed = false,
  position = 'top',
  className = '',
  style,
}) => {
  const colors = bannerColors[type];

  const containerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-md)',
    padding: 'var(--space-inline-sm) var(--space-inline-md)',
    backgroundColor: colors.bg,
    color: colors.text,
    fontSize: 'var(--text-body-sm)',
    lineHeight: 'var(--leading-normal)',
    width: '100%',
    boxSizing: 'border-box',
    ...(fixed
      ? {
          position: 'fixed',
          [position]: 0,
          left: 0,
          right: 0,
          zIndex: 'var(--z-sticky)',
        }
      : {}),
    ...style,
  };

  const iconStyle: React.CSSProperties = {
    flexShrink: 0,
    color: colors.icon,
    width: 'var(--icon-sm)',
    height: 'var(--icon-sm)',
    display: 'flex',
    alignItems: 'center',
  };

  const contentStyle: React.CSSProperties = {
    flex: 1,
    minWidth: 0,
  };

  const closeButtonStyle: React.CSSProperties = {
    flexShrink: 0,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    width: 'var(--icon-sm)',
    height: 'var(--icon-sm)',
    background: 'transparent',
    border: 'none',
    cursor: 'pointer',
    color: 'currentColor',
    padding: 0,
    borderRadius: 'var(--radius-xs)',
    opacity: 0.7,
  };

  return (
    <div
      className={className}
      style={containerStyle}
      role="banner"
      aria-live="polite"
    >
      <span style={iconStyle}>{iconMap[type]}</span>
      <div style={contentStyle}>
        {message && <span>{message}</span>}
        {children}
      </div>
      {onClose && (
        <button
          style={closeButtonStyle}
          onClick={onClose}
          aria-label="Dismiss banner"
          type="button"
        >
          <svg width="16" height="16" viewBox="0 0 16 16" fill="none" aria-hidden="true">
            <path d="M4 4l8 8M12 4l-8 8" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
          </svg>
        </button>
      )}
    </div>
  );
};

Banner.displayName = 'Banner';
export default Banner;
