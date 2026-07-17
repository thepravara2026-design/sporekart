import type { ReactNode } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useApp } from '../../context';
import type { Role } from '../../config/roles';

interface RequireAuthProps {
  children: ReactNode;
  allowedRoles?: Role[];
}

/**
 * Guards protected route subtrees.
 *
 * Root cause (BUG-RT-001..006): no route-level authentication or
 * authorization existed; every <Route> in App.tsx rendered without any
 * guard, so guests could reach /dashboard and /admin.
 *
 * Fix: derive authentication from the active session role. A 'guest' role
 * means unauthenticated and is redirected to /login. When role-specific
 * access is required (admin areas), the caller passes allowedRoles.
 */
export function RequireAuth({ children, allowedRoles }: RequireAuthProps) {
  const { activeRole } = useApp();
  const location = useLocation();

  if (activeRole === 'guest') {
    return <Navigate to="/login" replace state={{ from: location.pathname }} />;
  }

  if (allowedRoles && !allowedRoles.includes(activeRole)) {
    return <Navigate to="/access-denied" replace />;
  }

  return <>{children}</>;
}
