import { Icon } from '../../design-system/icons/Icon';
import { DEFAULT_STATE_CONFIGS } from './defaultConfig';
import type { OperationalStateType, OperationalStateConfig } from './types';

const STATE_ICONS: Record<OperationalStateType, string> = {
  loading: 'loader',
  empty: 'inbox',
  no_data: 'search',
  permission_denied: 'lock',
  unauthorized: 'user-x',
  forbidden: 'shield-off',
  offline: 'wifi-off',
  maintenance: 'tool',
  system_updating: 'refresh-cw',
  feature_disabled: 'eye-off',
  server_unavailable: 'server-off',
  unexpected_error: 'alert-triangle',
};

interface OperationalStateDisplayProps {
  state?: OperationalStateType;
  config?: Partial<OperationalStateConfig>;
  compact?: boolean;
}

export function OperationalStateDisplay({ state = 'loading', config = {}, compact = false }: OperationalStateDisplayProps) {
  const defaults = DEFAULT_STATE_CONFIGS[state] ?? DEFAULT_STATE_CONFIGS.unexpected_error;
  const icon = config.icon ?? STATE_ICONS[state] ?? 'alert-circle';
  const title = config.title ?? defaults.title ?? '';
  const description = config.description ?? defaults.description ?? '';

  return (
    <div
      role="status"
      aria-live="polite"
      style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        padding: compact ? '24px 16px' : '48px 24px',
        textAlign: 'center',
        color: 'var(--color-text-tertiary)',
        minHeight: compact ? 120 : 240,
      }}
    >
      {state === 'loading' ? (
        <div style={{ marginBottom: 16 }}>
          <div
            aria-label="Loading"
            style={{
              width: 32, height: 32,
              border: '3px solid var(--color-border)',
              borderTopColor: 'var(--color-primary)',
              borderRadius: '50%',
              animation: 'spin 0.8s linear infinite',
            }}
          />
        </div>
      ) : (
        <Icon name={icon} size={compact ? 24 : 40} style={{ marginBottom: 12, opacity: 0.6 }} />
      )}
      {title && (
        <h3 style={{ margin: '0 0 4px', fontSize: compact ? 'var(--text-body)' : 'var(--text-h3)', fontWeight: 600, color: 'var(--color-text-secondary)' }}>
          {title}
        </h3>
      )}
      {description && (
        <p style={{ margin: 0, fontSize: compact ? 'var(--text-caption)' : 'var(--text-body)', maxWidth: 360, lineHeight: 1.5 }}>
          {description}
        </p>
      )}
      {config.action && (
        <button
          onClick={config.action.onClick}
          style={{
            marginTop: 16,
            padding: '8px 20px',
            border: 'none',
            borderRadius: 'var(--radius-md)',
            background: 'var(--color-primary)',
            color: '#fff',
            cursor: 'pointer',
            fontSize: 'var(--text-body)',
            fontWeight: 500,
          }}
        >
          {config.action.label}
        </button>
      )}
      {config.secondaryAction && (
        <button
          onClick={config.secondaryAction.onClick}
          style={{
            marginTop: 8,
            padding: '6px 16px',
            border: '1px solid var(--color-border)',
            borderRadius: 'var(--radius-md)',
            background: 'transparent',
            color: 'var(--color-text-secondary)',
            cursor: 'pointer',
            fontSize: 'var(--text-caption)',
          }}
        >
          {config.secondaryAction.label}
        </button>
      )}
    </div>
  );
}
