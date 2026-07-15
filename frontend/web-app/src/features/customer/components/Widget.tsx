import { Card } from '../../../design-system/components';

export type WidgetState = 'loading' | 'empty' | 'error' | 'success';

export interface WidgetProps {
  children: React.ReactNode;
  title: string;
  subtitle?: string;
  icon?: React.ReactNode;
  footer?: React.ReactNode;
  state?: WidgetState;
  errorMessage?: string;
  variant?: 'default' | 'elevated' | 'outlined';
  onRetry?: () => void;
}

export function Widget({
  children,
  title,
  subtitle,
  icon,
  footer,
  state = 'success',
  errorMessage,
  variant = 'default',
  onRetry,
}: WidgetProps) {
  if (state === 'loading') {
    return (
      <Card variant={variant} padding="lg" loading>
        <div className="cw-widget__skeleton">
          <div className="cw-widget__skeleton-line" style={{ width: '60%', height: '1.5rem' }} />
          <div className="cw-widget__skeleton-line" style={{ width: '40%', height: '1rem', marginTop: 'var(--space-stack-xs)' }} />
          <div className="cw-widget__skeleton-line" style={{ width: '100%', height: '1rem', marginTop: 'var(--space-stack-md)' }} />
          <div className="cw-widget__skeleton-line" style={{ width: '80%', height: '1rem', marginTop: 'var(--space-stack-xs)' }} />
        </div>
      </Card>
    );
  }

  if (state === 'error') {
    return (
      <Card variant={variant} padding="lg" error={errorMessage}>
        <div className="cw-widget__error">
          <div className="cw-widget__error-icon" aria-hidden="true">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <circle cx="12" cy="12" r="10" />
              <line x1="12" y1="8" x2="12" y2="12" />
              <line x1="12" y1="16" x2="12.01" y2="16" />
            </svg>
          </div>
          <p>{errorMessage || 'Something went wrong. Please try again.'}</p>
          {onRetry && (
            <button
              type="button"
              className="cw-widget__retry"
              onClick={onRetry}
            >
              Try again
            </button>
          )}
        </div>
      </Card>
    );
  }

  if (state === 'empty') {
    return (
      <Card variant={variant} padding="lg">
        <div className="cw-widget__empty">
          <p>{children}</p>
        </div>
      </Card>
    );
  }

  return (
    <Card variant={variant} padding="lg" className="cw-widget">
      <div className="cw-widget__header">
        <div className="cw-widget__title-group">
          <h2 className="cw-widget__title">{title}</h2>
          {subtitle && <p className="cw-widget__subtitle">{subtitle}</p>}
        </div>
        {icon && <span className="cw-widget__icon">{icon}</span>}
      </div>
      <div className="cw-widget__body">{children}</div>
      {footer && <div className="cw-widget__footer">{footer}</div>}
    </Card>
  );
}

export default Widget;