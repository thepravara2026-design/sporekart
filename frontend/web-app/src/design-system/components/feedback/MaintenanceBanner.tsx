import React from 'react';
import { Banner, BannerProps } from './Banner';

export interface MaintenanceBannerProps extends BannerProps {}

export const MaintenanceBanner: React.FC<MaintenanceBannerProps> = ({
  className = '',
  ...props
}) => {
  return (
    <Banner
      type="maintenance"
      className={className}
      {...props}
    />
  );
};

MaintenanceBanner.displayName = 'MaintenanceBanner';
export default MaintenanceBanner;
