import React from 'react';
import { Banner, BannerProps } from './Banner';

export interface OfflineBannerProps extends BannerProps {}

export const OfflineBanner: React.FC<OfflineBannerProps> = ({
  className = '',
  ...props
}) => {
  return (
    <Banner
      type="offline"
      fixed
      position="top"
      className={className}
      {...props}
    />
  );
};

OfflineBanner.displayName = 'OfflineBanner';
export default OfflineBanner;
