import type { ReactNode } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useApp } from '../../context';
import type { Role } from '../../config/roles';

interface RequireAuthProps {
  children: ReactNode;
  allowedRoles?: Role[];
}

export function RequireAuth({ children, allowedRoles }: RequireAuthProps) {
  const { auth } = useApp();
  const location = useLocation();

  if (auth.loading) {
    return null;
  }

  if (!auth.isAuthenticated) {
    return <Navigate to="/login" replace state={{ from: location.pathname }} />;
  }

  if (allowedRoles && !allowedRoles.includes(auth.userRole)) {
    return <Navigate to="/access-denied" replace />;
  }

  return <>{children}</>;
}
