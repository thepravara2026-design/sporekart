import {
  UnauthorizedPage,
  ForbiddenPage,
  AuthErrorPage,
  NetworkErrorPage,
  ServerErrorPage,
} from '../pages/ErrorPages';
import '../auth.css';

export default function AuthErrorsGallery() {
  return (
    <div>
      <UnauthorizedPage />
      <ForbiddenPage />
      <AuthErrorPage />
      <NetworkErrorPage />
      <ServerErrorPage />
    </div>
  );
}
