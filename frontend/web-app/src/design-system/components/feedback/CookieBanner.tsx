import React from 'react';
import { Banner, BannerProps } from './Banner';

export interface CookieBannerAction {
  label: string;
  onClick: () => void;
}

export interface CookieBannerProps extends BannerProps {
  onAccept?: () => void;
  onDecline?: () => void;
  acceptLabel?: string;
  declineLabel?: string;
}

export const CookieBanner: React.FC<CookieBannerProps> = ({
  onAccept,
  onDecline,
  acceptLabel = 'Accept',
  declineLabel = 'Decline',
  className = '',
  style,
  ...props
}) => {
  const actionsStyle: React.CSSProperties = {
    display: 'flex',
    gap: 'var(--space-inline-sm)',
    flexShrink: 0,
  };

  const acceptButtonStyle: React.CSSProperties = {
    padding: 'var(--space-1) var(--space-inline-md)',
    fontSize: 'var(--text-caption)',
    fontWeight: 'var(--weight-semibold)',
    borderRadius: 'var(--radius-btn)',
    border: 'none',
    background: '#FFFFFF',
    color: 'var(--color-neutral-900)',
    cursor: 'pointer',
    lineHeight: 'var(--leading-normal)',
  };

  const declineButtonStyle: React.CSSProperties = {
    padding: 'var(--space-1) var(--space-inline-md)',
    fontSize: 'var(--text-caption)',
    fontWeight: 'var(--weight-medium)',
    borderRadius: 'var(--radius-btn)',
    border: '1px solid rgba(255,255,255,0.3)',
    background: 'transparent',
    color: '#FFFFFF',
    cursor: 'pointer',
    lineHeight: 'var(--leading-normal)',
  };

  return (
    <Banner
      type="cookie"
      fixed
      position="bottom"
      className={className}
      style={style}
      {...props}
    >
      <div style={actionsStyle}>
        {onAccept && (
          <button
            style={acceptButtonStyle}
            onClick={onAccept}
            type="button"
          >
            {acceptLabel}
          </button>
        )}
        {onDecline && (
          <button
            style={declineButtonStyle}
            onClick={onDecline}
            type="button"
          >
            {declineLabel}
          </button>
        )}
      </div>
    </Banner>
  );
};

CookieBanner.displayName = 'CookieBanner';
export default CookieBanner;
