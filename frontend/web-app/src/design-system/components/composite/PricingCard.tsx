import React from 'react';
import { Card } from './Card';

export interface PricingCardProps {
  title: string;
  price: string | number;
  currency?: string;
  period?: string;
  features: string[];
  highlighted?: boolean;
  ctaLabel?: string;
  onCta?: () => void;
  loading?: boolean;
}

export const PricingCard: React.FC<PricingCardProps> = ({
  title,
  price,
  currency = '$',
  period = '/mo',
  features,
  highlighted = false,
  ctaLabel = 'Get Started',
  onCta,
  loading = false,
}) => {
  return (
    <Card
      variant={highlighted ? 'elevated' : 'outlined'}
      padding="lg"
      loading={loading}
      className={highlighted ? 'sk-pricingcard--highlighted' : ''}
    >
      {highlighted && (
        <div
          className="sk-pricingcard__badge"
          style={{
            position: 'absolute',
            top: 0,
            right: 0,
            background: 'var(--color-bg-primary-default)',
            color: 'var(--color-text-on-primary)',
            fontSize: 'var(--text-caption)',
            fontWeight: 'var(--weight-semibold)',
            padding: 'var(--space-1) var(--space-3)',
            borderRadius: '0 var(--radius-card) 0 var(--radius-tag)',
          }}
        >
          Popular
        </div>
      )}
      <div
        className="sk-pricingcard__header"
        style={{
          textAlign: 'center',
          marginBottom: 'var(--space-stack-md)',
        }}
      >
        <h3
          className="sk-pricingcard__title"
          style={{
            fontSize: 'var(--text-h5)',
            fontWeight: 'var(--weight-semibold)',
            color: 'var(--color-text-primary)',
            margin: '0 0 var(--space-stack-sm) 0',
          }}
        >
          {title}
        </h3>
        <div
          className="sk-pricingcard__price"
          style={{
            fontSize: 'var(--text-h1)',
            fontWeight: 'var(--weight-bold)',
            color: 'var(--color-text-primary)',
            lineHeight: 'var(--leading-tight)',
          }}
        >
          <span style={{ fontSize: 'var(--text-h4)', verticalAlign: 'super' }}>{currency}</span>
          {price}
          <span
            style={{
              fontSize: 'var(--text-body)',
              fontWeight: 'var(--weight-normal)',
              color: 'var(--color-text-secondary)',
            }}
          >
            {period}
          </span>
        </div>
      </div>
      <ul
        className="sk-pricingcard__features"
        style={{
          listStyle: 'none',
          padding: 0,
          margin: '0 0 var(--space-stack-md) 0',
          display: 'flex',
          flexDirection: 'column',
          gap: 'var(--space-stack-xs)',
        }}
      >
        {features.map((feature, idx) => (
          <li
            key={idx}
            className="sk-pricingcard__feature"
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: 'var(--space-inline-sm)',
              fontSize: 'var(--text-body-sm)',
              color: 'var(--color-text-secondary)',
            }}
          >
            <span style={{ color: 'var(--color-success)', fontWeight: 'var(--weight-bold)' }}>
              {'\u2713'}
            </span>
            {feature}
          </li>
        ))}
      </ul>
      {onCta && (
        <button
          className="sk-pricingcard__cta"
          style={{
            width: '100%',
            padding: 'var(--space-2) var(--space-4)',
            background: highlighted
              ? 'var(--color-bg-primary-default)'
              : 'var(--color-bg-surface-default)',
            color: highlighted
              ? 'var(--color-text-on-primary)'
              : 'var(--color-text-primary)',
            border: highlighted
              ? 'var(--border-width-thin) solid var(--color-bg-primary-default)'
              : 'var(--border-width-thin) solid var(--color-border-default)',
            borderRadius: 'var(--radius-btn)',
            fontWeight: 'var(--weight-semibold)',
            fontSize: 'var(--text-button)',
            cursor: 'pointer',
            transition: 'all var(--duration-fast) var(--easing-standard)',
          }}
          onClick={onCta}
        >
          {ctaLabel}
        </button>
      )}
    </Card>
  );
};

PricingCard.displayName = 'PricingCard';

export default PricingCard;
