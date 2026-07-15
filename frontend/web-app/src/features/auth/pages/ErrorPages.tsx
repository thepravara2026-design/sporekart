import { useNavigate } from 'react-router-dom';
import StatusScreen from '../components/StatusScreen';
import '../auth.css';

export function UnauthorizedPage() {
  const navigate = useNavigate();
  return (
    <StatusScreen
      icon="lock"
      tone="warning"
      title="401 · Sign in required"
      body="You need to authenticate before viewing this resource. Please sign in to continue."
      primary={{ label: 'Sign in', onClick: () => navigate('/login') }}
      secondary={{ label: 'Go to home', onClick: () => navigate('/') }}
    />
  );
}

export function ForbiddenPage() {
  const navigate = useNavigate();
  return (
    <StatusScreen
      icon="slash"
      tone="danger"
      title="403 · Forbidden"
      body="You don’t have the required role to access this resource. Role-based access control blocked this request."
      primary={{ label: 'Back to home', onClick: () => navigate('/') }}
      secondary={{ label: 'Contact support', onClick: () => navigate('/support') }}
    />
  );
}

export function AuthErrorPage() {
  const navigate = useNavigate();
  return (
    <StatusScreen
      icon="alert-circle"
      tone="danger"
      title="Authentication error"
      body="Something went wrong while verifying your identity. This is usually temporary — please try signing in again."
      primary={{ label: 'Try again', onClick: () => navigate('/login') }}
      secondary={{ label: 'Contact support', onClick: () => navigate('/support') }}
    />
  );
}

export function NetworkErrorPage() {
  const navigate = useNavigate();
  return (
    <StatusScreen
      icon="wifi"
      tone="warning"
      title="Network error"
      body="We couldn’t reach the authentication service. Check your connection and retry."
      primary={{ label: 'Retry', onClick: () => navigate('/login') }}
      secondary={{ label: 'Go to home', onClick: () => navigate('/') }}
    />
  );
}

export function ServerErrorPage() {
  const navigate = useNavigate();
  return (
    <StatusScreen
      icon="database"
      tone="danger"
      title="Server error"
      body="The authentication service is temporarily unavailable. Our team has been notified. Please try again shortly."
      primary={{ label: 'Retry', onClick: () => navigate('/login') }}
      secondary={{ label: 'Go to home', onClick: () => navigate('/') }}
    />
  );
}
