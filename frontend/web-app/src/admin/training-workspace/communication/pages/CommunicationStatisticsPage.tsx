import { memo, useMemo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import KpiCard from '../../analytics/components/KpiCard';
import WidgetGrid from '../../analytics/components/WidgetGrid';
import WidgetCard from '../../analytics/components/WidgetCard';
import { MOCK_ANNOUNCEMENTS, MOCK_NOTIFICATIONS, MOCK_STATS } from '../data/communicationMockData';
import { CATEGORY_LABELS, PRIORITY_LABELS } from '../data/communicationTypes';
import type { CommunicationCategory, CommunicationPriority } from '../data/communicationTypes';
import { PRIORITY_TONE } from '../data/communicationOptions';
import { formatCount, toneTokens } from '../data/communicationFormatters';

function countBy<T extends string>(items: readonly { key: T }[]): Record<string, number> {
  const map: Record<string, number> = {};
  for (const it of items) map[it.key] = (map[it.key] ?? 0) + 1;
  return map;
}

const BarRow = memo(function BarRow({ label, count, max, color }: { label: string; count: number; max: number; color: string }) {
  const pct = max ? Math.round((count / max) * 100) : 0;
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
        <span>{label}</span>
        <span>{count}</span>
      </div>
      <div style={{ height: 8, borderRadius: 4, background: 'var(--color-bg-surface-muted)', overflow: 'hidden' }}>
        <div style={{ width: `${pct}%`, height: '100%', background: color }} />
      </div>
    </div>
  );
});

const CommunicationStatisticsPage = memo(function CommunicationStatisticsPage() {
  const stats = MOCK_STATS;

  const byCategory = useMemo(() => countBy(MOCK_ANNOUNCEMENTS.map((a) => ({ key: a.category }))), []);
  const byPriority = useMemo(() => countBy(MOCK_ANNOUNCEMENTS.map((a) => ({ key: a.priority }))), []);
  const notifByPriority = useMemo(() => countBy(MOCK_NOTIFICATIONS.map((n) => ({ key: n.priority }))), []);

  const maxCat = Math.max(1, ...Object.values(byCategory));
  const maxPri = Math.max(1, ...Object.values(byPriority));
  const maxNotif = Math.max(1, ...Object.values(notifByPriority));

  return (
    <section id="panel-statistics" aria-labelledby="tab-statistics" style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)' }}>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))', gap: 'var(--space-3)' }}>
        <KpiCard title="Audience Reach" value={formatCount(stats.audienceReachPlaceholder)} subtitle="Placeholder" icon={<Icon name="users" size={18} />} />
        <KpiCard title="Delivered" value={stats.deliveredPlaceholder} subtitle="Simulated" icon={<Icon name="check-circle" size={18} />} />
        <KpiCard title="Failed" value={stats.failedPlaceholder} subtitle="Simulated" icon={<Icon name="x-circle" size={18} />} />
        <KpiCard title="Notifications" value={stats.totalNotifications} subtitle="All time" icon={<Icon name="bell" size={18} />} />
      </div>

      <WidgetGrid minColWidth={340}>
        <WidgetCard title="Announcements by Category">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-3)' }}>
            {(Object.keys(byCategory) as CommunicationCategory[]).sort((a, b) => byCategory[b] - byCategory[a]).map((c) => (
              <BarRow key={c} label={CATEGORY_LABELS[c]} count={byCategory[c]} max={maxCat} color="var(--color-primary)" />
            ))}
          </div>
        </WidgetCard>

        <WidgetCard title="Announcements by Priority">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-3)' }}>
            {(Object.keys(byPriority) as CommunicationPriority[]).sort((a, b) => byPriority[b] - byPriority[a]).map((p) => (
              <BarRow key={p} label={PRIORITY_LABELS[p]} count={byPriority[p]} max={maxPri} color={toneTokens(PRIORITY_TONE[p]).border} />
            ))}
          </div>
        </WidgetCard>

        <WidgetCard title="Notifications by Priority">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-3)' }}>
            {(Object.keys(notifByPriority) as CommunicationPriority[]).sort((a, b) => notifByPriority[b] - notifByPriority[a]).map((p) => (
              <BarRow key={p} label={PRIORITY_LABELS[p]} count={notifByPriority[p]} max={maxNotif} color={toneTokens(PRIORITY_TONE[p]).border} />
            ))}
          </div>
        </WidgetCard>
      </WidgetGrid>

      <p style={{ margin: 0, fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>
        Delivery, reach, and view figures are illustrative placeholders (Mock Mode).
      </p>
    </section>
  );
});

export default CommunicationStatisticsPage;
