import { Component } from 'react';

interface BaseErrorBoundaryProps {
  children: React.ReactNode;
  name?: string;
  fallback?: React.ReactNode;
  onError?: (error: Error, errorInfo: React.ErrorInfo) => void;
  onRetry?: () => void;
}

interface BaseErrorBoundaryState {
  error: Error | null;
  hasError: boolean;
}

export class BaseErrorBoundary extends Component<BaseErrorBoundaryProps, BaseErrorBoundaryState> {
  constructor(props: BaseErrorBoundaryProps) {
    super(props);
    this.state = { error: null, hasError: false };
  }

  static getDerivedStateFromError(error: Error) {
    return { error, hasError: true };
  }

  componentDidCatch(error: Error, errorInfo: React.ErrorInfo) {
    this.props.onError?.(error, errorInfo);
  }

  handleRetry = () => {
    this.setState({ error: null, hasError: false });
    this.props.onRetry?.();
  };

  handleReload = () => {
    window.location.reload();
  };

  render() {
    if (this.state.hasError) {
      if (this.props.fallback) return this.props.fallback;

      return (
        <div
          role="alert"
          style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center',
            padding: 48,
            textAlign: 'center',
            minHeight: 200,
          }}
        >
          <div style={{ width: 48, height: 48, borderRadius: '50%', background: 'var(--color-error-alpha, rgba(239,68,68,0.1))', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: 16 }}>
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" style={{ color: 'var(--color-error)' }}>
              <circle cx="12" cy="12" r="10" />
              <line x1="12" y1="8" x2="12" y2="12" />
              <line x1="12" y1="16" x2="12.01" y2="16" />
            </svg>
          </div>
          <h3 style={{ margin: '0 0 4px', fontSize: 'var(--text-h3)', fontWeight: 600, color: 'var(--color-text-primary)' }}>
            {this.props.name ?? 'Something went wrong'}
          </h3>
          <p style={{ margin: '0 0 16px', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', maxWidth: 400 }}>
            {this.state.error?.message ?? 'An unexpected error occurred. Please try again.'}
          </p>
          <div style={{ display: 'flex', gap: 8 }}>
            <button
              onClick={this.handleRetry}
              style={{
                padding: '8px 20px',
                border: 'none',
                borderRadius: 'var(--radius-md)',
                background: 'var(--color-primary)',
                color: '#fff',
                cursor: 'pointer',
                fontSize: 'var(--text-body)',
                fontWeight: 500,
              }}
            >
              Try again
            </button>
            <button
              onClick={this.handleReload}
              style={{
                padding: '8px 20px',
                border: '1px solid var(--color-border)',
                borderRadius: 'var(--radius-md)',
                background: 'transparent',
                color: 'var(--color-text-secondary)',
                cursor: 'pointer',
                fontSize: 'var(--text-body)',
              }}
            >
              Reload page
            </button>
          </div>
        </div>
      );
    }

    return this.props.children;
  }
}
