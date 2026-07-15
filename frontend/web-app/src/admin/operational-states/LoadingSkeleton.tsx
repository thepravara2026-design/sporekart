interface LoadingSkeletonProps {
  lines?: number;
  variant?: 'text' | 'card' | 'table' | 'sidebar';
  width?: string | number;
  height?: string | number;
}

export function LoadingSkeleton({ lines = 3, variant = 'text', width, height }: LoadingSkeletonProps) {
  if (variant === 'card') {
    return (
      <div
        aria-busy="true"
        aria-label="Loading content"
        style={{
          padding: 24,
          border: '1px solid var(--color-border)',
          borderRadius: 'var(--radius-lg)',
          background: 'var(--color-surface)',
        }}
      >
        <div style={{ width: '60%', height: 20, background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-sm)', marginBottom: 16, animation: 'pulse 1.5s ease-in-out infinite' }} />
        <div style={{ width: '100%', height: 12, background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-sm)', marginBottom: 8, animation: 'pulse 1.5s ease-in-out infinite', animationDelay: '0.1s' }} />
        <div style={{ width: '85%', height: 12, background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-sm)', marginBottom: 8, animation: 'pulse 1.5s ease-in-out infinite', animationDelay: '0.2s' }} />
        <div style={{ width: '70%', height: 12, background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-sm)', animation: 'pulse 1.5s ease-in-out infinite', animationDelay: '0.3s' }} />
      </div>
    );
  }

  if (variant === 'table') {
    return (
      <div aria-busy="true" aria-label="Loading table">
        <div style={{ display: 'flex', gap: 16, padding: '12px 16px', borderBottom: '1px solid var(--color-border)' }}>
          {[40, 25, 15, 20].map((w, i) => (
            <div key={i} style={{ width: `${w}%`, height: 14, background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-sm)', animation: `pulse 1.5s ease-in-out infinite`, animationDelay: `${i * 0.1}s` }} />
          ))}
        </div>
        {Array.from({ length: lines }).map((_, row) => (
          <div key={row} style={{ display: 'flex', gap: 16, padding: '12px 16px', borderBottom: '1px solid var(--color-border)' }}>
            {[40, 25, 15, 20].map((w, col) => (
              <div key={col} style={{ width: `${w}%`, height: 12, background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-sm)', animation: `pulse 1.5s ease-in-out infinite`, animationDelay: `${(row * 4 + col) * 0.05}s` }} />
            ))}
          </div>
        ))}
      </div>
    );
  }

  if (variant === 'sidebar') {
    return (
      <div aria-busy="true" aria-label="Loading navigation" style={{ padding: 8 }}>
        {Array.from({ length: lines }).map((_, i) => (
          <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 10, padding: '8px 12px' }}>
            <div style={{ width: 18, height: 18, borderRadius: 'var(--radius-sm)', background: 'var(--color-surface-hover)', animation: 'pulse 1.5s ease-in-out infinite', animationDelay: `${i * 0.1}s` }} />
            <div style={{ flex: 1, height: 12, background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-sm)', animation: 'pulse 1.5s ease-in-out infinite', animationDelay: `${i * 0.1}s` }} />
          </div>
        ))}
      </div>
    );
  }

  return (
    <div aria-busy="true" aria-label="Loading text" style={{ width: width ?? '100%', height: height ?? 'auto' }}>
      {Array.from({ length: lines }).map((_, i) => (
        <div
          key={i}
          style={{
            width: i === lines - 1 ? '60%' : '100%',
            height: height ?? 14,
            background: 'var(--color-surface-hover)',
            borderRadius: 'var(--radius-sm)',
            marginBottom: 8,
            animation: 'pulse 1.5s ease-in-out infinite',
            animationDelay: `${i * 0.15}s`,
          }}
        />
      ))}
    </div>
  );
}
