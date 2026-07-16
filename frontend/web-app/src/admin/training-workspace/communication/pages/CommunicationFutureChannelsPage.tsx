import { memo } from 'react';
import { INTEGRATION_PROVIDERS } from '../data/communicationMockData';
import type { IntegrationCategory } from '../data/communicationTypes';
import { FutureChannelCard } from '../components';

const CATEGORY_ICON: Record<IntegrationCategory, string> = {
  email: 'mail',
  whatsapp: 'message-circle',
  sms: 'message-square',
  push: 'smartphone',
  calendar: 'calendar',
  crm: 'users',
  erp: 'database',
  commerce: 'shopping-bag',
};

const CommunicationFutureChannelsPage = memo(function CommunicationFutureChannelsPage() {
  return (
    <section id="panel-channels" aria-labelledby="tab-channels" style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)' }}>
      <div
        style={{
          display: 'flex', gap: 'var(--space-3)', alignItems: 'flex-start',
          padding: 'var(--space-4)', borderRadius: 'var(--radius-lg)',
          background: 'var(--color-bg-primary-weak)', border: '1px solid var(--color-border-default)',
        }}
      >
        <div>
          <h2 style={{ margin: 0, fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>Integration Roadmap</h2>
          <p style={{ margin: '4px 0 0', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 1.6 }}>
            The communication platform is designed provider-agnostic. The channels below are architecturally
            supported and will activate once the corresponding provider is integrated. No external delivery
            occurs in Mock Mode.
          </p>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: 'var(--space-4)' }}>
        {INTEGRATION_PROVIDERS.map((p) => (
          <FutureChannelCard key={p.id} provider={p} icon={CATEGORY_ICON[p.category]} />
        ))}
      </div>
    </section>
  );
});

export default CommunicationFutureChannelsPage;
