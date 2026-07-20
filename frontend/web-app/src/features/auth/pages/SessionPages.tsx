import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import StatusScreen from '../components/StatusScreen';
import { useApp } from '../../../context';
import '../auth.css';

export function SessionExpiredPage() {
  const navigate = useNavigate();
  return (
    <StatusScreen
      icon="clock"
      tone="warning"
      title="Your session expired"
      body="For your security, you were signed out after a period of inactivity. Sign in again to continue where you left off."
      primary={{ label: 'Sign in again', onClick: () => navigate('/login') }}
      secondary={{ label: 'Go to home', onClick: () => navigate('/') }}
    />
  );
}

export function LoggedOutPage() {
  const navigate = useNavigate();
  return (
    <StatusScreen
      icon="log-out"
      tone="info"
      title="You’ve been signed out"
      body="Thanks for using SporeKart. You can sign back in anytime — your data is safe."
      primary={{ label: 'Sign in', onClick: () => navigate('/login') }}
      secondary={{ label: 'Back to home', onClick: () => navigate('/') }}
    />
  );
}

export function AuthLoadingPage() {
  const navigate = useNavigate();
  const { auth } = useApp();

  useEffect(() => {
    if (auth.isAuthenticated) {
      const t = window.setTimeout(() => navigate('/dashboard', { replace: true }), 800);
      return () => window.clearTimeout(t);
    }
  }, [auth.isAuthenticated, navigate]);

  if (!auth.isAuthenticated && !auth.loading) {
    navigate('/login', { replace: true });
    return null;
  }

  return (
    <div className="auth-status">
      <main className="auth-status__inner">
        <span className="auth-spinner" role="status" aria-label="Establishing your session" />
        <h1 className="auth-status__title" style={{ marginTop: 'var(--space-stack-md)' }}>
          Establishing your session
        </h1>
        <p className="auth-status__body">Securing your workspace and loading your role…</p>
      </main>
    </div>
  );
}

export function AccessDeniedPage() {
  const navigate = useNavigate();
  return (
    <StatusScreen
      icon="slash"
      tone="danger"
      title="Access denied"
      body="Your account doesn’t have permission to view this page. If you believe this is a mistake, contact an administrator or your account manager."
      primary={{ label: 'Back to home', onClick: () => navigate('/') }}
      secondary={{ label: 'Contact support', onClick: () => navigate('/support') }}
    />
  );
}
