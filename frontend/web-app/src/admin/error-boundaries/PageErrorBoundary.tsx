import { BaseErrorBoundary } from './BaseErrorBoundary';

interface PageErrorBoundaryProps {
  children: React.ReactNode;
  pageName: string;
  onRetry?: () => void;
}

export function PageErrorBoundary({ children, pageName, onRetry }: PageErrorBoundaryProps) {
  return (
    <BaseErrorBoundary name={`${pageName} Error`} onRetry={onRetry}>
      {children}
    </BaseErrorBoundary>
  );
}
