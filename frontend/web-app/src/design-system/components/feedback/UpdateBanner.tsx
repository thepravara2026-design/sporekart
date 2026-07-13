import React from 'react';
import { Banner, BannerProps } from './Banner';

export interface UpdateBannerAction {
  label: string;
  onClick: () => void;
}

export interface UpdateBannerProps extends BannerProps {
  action?: UpdateBannerAction;
}

export const UpdateBanner: React.FC<UpdateBannerProps> = ({
  action,
  className = '',
  style,
  ...props
}) => {
  const actionButtonStyle: React.CSSProperties = {
    display: 'inline-flex',
    alignItems: 'center',
    padding: 'var(--space-1) var(--space-inline-md)',
    fontSize: 'var(--text-caption)',
    fontWeight: 'var(--weight-semibold)',
    borderRadius: 'var(--radius-btn)',
    border: '1px solid var(--color-info-500)',
    background: 'var(--color-info-500)',
    color: '#FFFFFF',
    cursor: 'pointer',
    marginLeft: 'var(--space-inline-sm)',
    lineHeight: 'var(--leading-normal)',
  };

  return (
    <Banner
      type="update"
      className={className}
      style={style}
      {...props}
    >
      {action && (
        <button
          style={actionButtonStyle}
          onClick={action.onClick}
          type="button"
        >
          {action.label}
        </button>
      )}
    </Banner>
  );
};

UpdateBanner.displayName = 'UpdateBanner';
export default UpdateBanner;
