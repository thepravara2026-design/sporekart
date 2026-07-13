import type { ReactNode } from 'react';
import { Seo, type SeoProps } from './Seo';
import { AnnouncementRegion, type AnnouncementRegionProps } from './AnnouncementRegion';
import { PublicHeader } from './PublicHeader';
import { PublicFooter } from './PublicFooter';
import { BreadcrumbFoundation, type BreadcrumbFoundationProps } from './BreadcrumbFoundation';

export interface PublicLayoutProps {
  children: ReactNode;
  seo?: SeoProps;
  breadcrumbs?: BreadcrumbFoundationProps['items'];
  announcement?: AnnouncementRegionProps | null;
}

export function PublicLayout({ children, seo, breadcrumbs, announcement }: PublicLayoutProps) {
  const shellStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    minHeight: '100vh',
    backgroundColor: 'var(--color-bg-canvas, #f4f6f8)',
    fontFamily: 'var(--font-family-sans, system-ui)',
  };

  const mainStyle: React.CSSProperties = {
    flex: '1 1 auto',
    width: '100%',
    display: 'flex',
    flexDirection: 'column',
  };

  return (
    <div className="sk-public-layout" style={shellStyle}>
      <Seo {...seo} />
      {announcement !== null && <AnnouncementRegion {...(announcement ?? {})} />}
      <PublicHeader />
      {breadcrumbs && breadcrumbs.length > 0 && <BreadcrumbFoundation items={breadcrumbs} />}
      <main id="main" className="sk-public-main" style={mainStyle}>
        {children}
      </main>
      <PublicFooter />
    </div>
  );
}
