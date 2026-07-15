import { BaseErrorBoundary } from './BaseErrorBoundary';

interface GlobalErrorBoundaryProps {
  children: React.ReactNode;
}

export function GlobalErrorBoundary({ children }: GlobalErrorBoundaryProps) {
  return (
    <BaseErrorBoundary name="Application Error">
      {children}
    </BaseErrorBoundary>
  );
}
