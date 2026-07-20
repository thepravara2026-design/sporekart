import { Component, type ErrorInfo, type ReactNode } from 'react';
import { captureError } from '../lib/sentry';

interface ErrorBoundaryProps {
  children: ReactNode;
}

interface ErrorBoundaryState {
  hasError: boolean;
  message: string;
}

export class ErrorBoundary extends Component<ErrorBoundaryProps, ErrorBoundaryState> {
  state: ErrorBoundaryState = { hasError: false, message: '' };

  static getDerivedStateFromError(error: Error): ErrorBoundaryState {
    return { hasError: true, message: error.message || 'Unexpected error' };
  }

  componentDidCatch(error: Error, info: ErrorInfo) {
    captureError(error, { componentStack: info.componentStack });
  }

  handleReset = () => {
    this.setState({ hasError: false, message: '' });
    window.location.href = '/';
  };

  render() {
    if (this.state.hasError) {
      return (
        <div role="alert" className="sk-error-boundary" style={{ padding: 32, maxWidth: 560, margin: '0 auto' }}>
          <h1 style={{ fontSize: 'var(--text-h2)', marginBottom: 8 }}>Something went wrong</h1>
          <p style={{ color: 'var(--color-text-secondary)', marginBottom: 20 }}>
            An unexpected error occurred while rendering this screen. Your session is safe — you can
            return to the home page and continue.
          </p>
          <button
            type="button"
            onClick={this.handleReset}
            style={{
              padding: '10px 24px',
              border: 'none',
              borderRadius: 'var(--radius-md)',
              background: 'var(--color-primary)',
              color: '#fff',
              cursor: 'pointer',
              fontSize: 'var(--text-body)',
            }}
          >
            Back to home
          </button>
        </div>
      );
    }
    return this.props.children;
  }
}
