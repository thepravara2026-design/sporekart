import React from 'react';
import { Icon } from '../../design-system/icons/Icon';
import './auth.css';

export interface AuthLayoutProps {
  children: React.ReactNode;
  brandHeading?: string;
  brandSubheading?: string;
  showFeatures?: boolean;
}

const DEFAULT_FEATURES = [
  { icon: 'leaf', label: 'Traceable, lab-verified cultivars' },
  { icon: 'shield', label: 'Enterprise-grade security & RBAC' },
  { icon: 'truck', label: 'Reliable cold-chain delivery' },
];

export function AuthLayout({
  children,
  brandHeading = 'Mushroom cultivation, simplified.',
  brandSubheading = 'SporeKart connects growers, trainers, distributors and buyers on one trusted platform.',
  showFeatures = true,
}: AuthLayoutProps) {
  return (
    <div className="auth-shell">
      <aside className="auth-brand" aria-hidden="true">
        <div className="auth-brand__top">
          <span className="auth-brand__logo">
            <span className="auth-brand__logo-mark">
              <Icon name="leaf" size={22} color="currentColor" />
            </span>
            SporeKart
          </span>
          <h1 className="auth-brand__headline">{brandHeading}</h1>
          <p className="auth-brand__subhead">{brandSubheading}</p>
          {showFeatures && (
            <ul className="auth-brand__features">
              {DEFAULT_FEATURES.map((f) => (
                <li key={f.label}>
                  <span className="auth-brand__feature-icon">
                    <Icon name={f.icon} size={16} color="currentColor" />
                  </span>
                  {f.label}
                </li>
              ))}
            </ul>
          )}
        </div>
        <div className="auth-brand__bottom">
          <blockquote className="auth-brand__quote">
            “SporeKart cut our onboarding from days to minutes and gave every
            partner a single source of truth.”
            <span className="auth-brand__quote-author">
              — Cultivation Lead, Partner Greenhouse
            </span>
          </blockquote>
        </div>
      </aside>

      <main className="auth-form-panel" id="auth-main">
        <a href="#auth-main" className="auth-skip">Skip to form</a>
        <div className="auth-card">
          <span className="auth-mobile-brand">
            <span className="auth-mobile-brand__mark">
              <Icon name="leaf" size={18} color="currentColor" />
            </span>
            SporeKart
          </span>
          {children}
        </div>
      </main>
    </div>
  );
}

export default AuthLayout;
