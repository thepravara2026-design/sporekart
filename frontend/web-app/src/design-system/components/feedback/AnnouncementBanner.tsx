import React from 'react';
import { Banner, BannerProps } from './Banner';

export interface AnnouncementBannerProps extends BannerProps {}

export const AnnouncementBanner: React.FC<AnnouncementBannerProps> = ({
  className = '',
  ...props
}) => {
  return (
    <Banner
      type="announcement"
      className={className}
      {...props}
    />
  );
};

AnnouncementBanner.displayName = 'AnnouncementBanner';
export default AnnouncementBanner;
