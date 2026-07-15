import { BaseErrorBoundary } from './BaseErrorBoundary';

interface ModuleErrorBoundaryProps {
  children: React.ReactNode;
  moduleName: string;
  onRetry?: () => void;
}

export function ModuleErrorBoundary({ children, moduleName, onRetry }: ModuleErrorBoundaryProps) {
  return (
    <BaseErrorBoundary name={`${moduleName} Error`} onRetry={onRetry}>
      {children}
    </BaseErrorBoundary>
  );
}
