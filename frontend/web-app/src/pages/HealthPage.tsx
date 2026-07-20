import { useEnv } from '../config/env';

export default function HealthPage() {
  const env = useEnv();

  const health = {
    status: 'ok',
    version: '1.0.0',
    timestamp: new Date().toISOString(),
    uptime: `${Math.floor(performance.now() / 1000)}s`,
    environment: env.mode,
    authProvider: env.featureFlags.authProvider,
    paymentGateway: env.featureFlags.paymentGateway,
  };

  const body = JSON.stringify(health, null, 2);

  return (
    <pre
      style={{
        padding: 32,
        fontFamily: 'monospace',
        fontSize: 14,
        lineHeight: 1.6,
        background: '#f5f5f5',
        borderRadius: 8,
        maxWidth: 600,
        margin: '40px auto',
      }}
    >
      {body}
    </pre>
  );
}
