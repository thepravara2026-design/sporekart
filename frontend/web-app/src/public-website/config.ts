import type { ComponentType } from 'react';

export interface PublicRouteDef {
  path: string;
  label: string;
  description: string;
}

export const PUBLIC_WEBSITE_ROUTES: PublicRouteDef[] = [
  { path: '/', label: 'Home', description: 'SporeKart public home and brand entry point.' },
  { path: '/about', label: 'About', description: 'About SporeKart, mission, and the team.' },
  { path: '/products', label: 'Products', description: 'Public product catalog and discovery.' },
  { path: '/training', label: 'Training', description: 'Training programs, courses, and certifications.' },
  { path: '/blog', label: 'Blog', description: 'News, articles, and growing guides.' },
  { path: '/contact', label: 'Contact', description: 'Contact SporeKart support and sales.' },
  { path: '/faq', label: 'FAQ', description: 'Frequently asked questions.' },
  { path: '/certifications', label: 'Certifications', description: 'Certifications, compliance, and quality.' },
  { path: '/privacy-policy', label: 'Privacy Policy', description: 'Privacy policy and data handling.' },
  { path: '/terms-and-conditions', label: 'Terms & Conditions', description: 'Terms of service and conditions.' },
  { path: '/refund-policy', label: 'Refund Policy', description: 'Refund and cancellation policy.' },
  { path: '/shipping-policy', label: 'Shipping Policy', description: 'Shipping and delivery policy.' },
  { path: '/auth', label: 'Sign In', description: 'Public authentication entry point.' },
];

export const PUBLIC_WEBSITE_ROUTE_SET: ReadonlySet<string> = new Set(
  PUBLIC_WEBSITE_ROUTES.map((route) => route.path),
);

export function isPublicWebsiteRoute(pathname: string): boolean {
  if (PUBLIC_WEBSITE_ROUTE_SET.has(pathname)) {
    return true;
  }
  return PUBLIC_WEBSITE_ROUTE_SET.has(pathname.replace(/\/$/, ''));
}

export interface PublicNavItem {
  label: string;
  href: string;
  description?: string;
}

export const PUBLIC_PRIMARY_NAV: PublicNavItem[] = [
  { label: 'Products', href: '/products', description: 'Explore the catalog.' },
  { label: 'Training', href: '/training', description: 'Learn to grow.' },
  { label: 'Blog', href: '/blog', description: 'Guides and news.' },
  { label: 'About', href: '/about', description: 'Who we are.' },
  { label: 'Contact', href: '/contact', description: 'Get in touch.' },
];

export const PUBLIC_FOOTER_NAV: PublicNavItem[] = [
  { label: 'Products', href: '/products' },
  { label: 'Training', href: '/training' },
  { label: 'Blog', href: '/blog' },
  { label: 'About', href: '/about' },
  { label: 'FAQ', href: '/faq' },
  { label: 'Certifications', href: '/certifications' },
  { label: 'Contact', href: '/contact' },
  { label: 'Privacy Policy', href: '/privacy-policy' },
  { label: 'Terms & Conditions', href: '/terms-and-conditions' },
  { label: 'Refund Policy', href: '/refund-policy' },
  { label: 'Shipping Policy', href: '/shipping-policy' },
  { label: 'Sign In', href: '/auth' },
];

export interface PublicPreviewDef {
  path: string;
  label: string;
  description: string;
  Component: ComponentType;
}

export const SITE_NAME = 'SporeKart';
export const SITE_TAGLINE = 'Mushroom cultivation, simplified.';
