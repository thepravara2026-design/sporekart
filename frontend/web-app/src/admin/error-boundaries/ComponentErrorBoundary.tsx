import { BaseErrorBoundary } from './BaseErrorBoundary';

interface ComponentErrorBoundaryProps {
  children: React.ReactNode;
  componentName: string;
  fallback?: React.ReactNode;
  onRetry?: () => void;
}

export function ComponentErrorBoundary({ children, componentName, fallback, onRetry }: ComponentErrorBoundaryProps) {
  return (
    <BaseErrorBoundary name={`${componentName} Error`} fallback={fallback} onRetry={onRetry}>
      {children}
    </BaseErrorBoundary>
  );
}
