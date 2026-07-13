import React from 'react';
import { Banner, BannerProps } from './Banner';

export interface WarningBannerProps extends BannerProps {}

export const WarningBanner: React.FC<WarningBannerProps> = ({
  className = '',
  style,
  ...props
}) => {
  const warningStyle: React.CSSProperties = {
    fontWeight: 'var(--weight-semibold)',
    borderBottom: '2px solid var(--color-danger-500)',
    ...style,
  };

  return (
    <Banner
      type="warning"
      className={className}
      style={warningStyle}
      {...props}
    />
  );
};

WarningBanner.displayName = 'WarningBanner';
export default WarningBanner;
