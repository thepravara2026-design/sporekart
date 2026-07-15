import { useState, useCallback } from 'react';
import { Icon } from '../../../design-system/icons/Icon';
import type { KPIData, WidgetConfig } from '../types';
import { DashboardLayout } from '../DashboardLayout';
import { KPIGrid } from '../kpi/KPIGrid';
import { MOCK_KPIS, MOCK_WIDGETS, MOCK_ACTIVITY, MOCK_ANNOUNCEMENTS, MOCK_SYSTEM_STATUS, MOCK_QUICK_ACTIONS } from '../mock/mockData';
import { getAllModuleKPIs } from '../../modules/dashboardWidgets';
import type { DashboardData } from '../DashboardLayout';

function Section({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <div style={{ marginBottom: 48 }}>
      <h2 style={{ fontSize: 'var(--text-h3)', margin: '0 0 8px' }}>{title}</h2>
      {children}
    </div>
  );
}

function Frame({ children, dark }: { children: React.ReactNode; dark?: boolean }) {
  return (
    <div
      style={{
        border: '1px solid var(--color-border)',
        borderRadius: 'var(--radius-lg)',
        padding: 24,
        background: dark ? '#1a1a2e' : 'var(--color-surface)',
        color: dark ? '#e0e0e0' : undefined,
        marginBottom: 16,
      }}
    >
      {children}
    </div>
  );
}

function FrameLabel({ label }: { label: string }) {
  return (
    <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: 8, fontWeight: 600 }}>
      {label}
    </div>
  );
}

const fullDashboardData: DashboardData = {
  kpis: MOCK_KPIS,
  widgets: MOCK_WIDGETS,
  activity: MOCK_ACTIVITY,
  announcements: MOCK_ANNOUNCEMENTS,
  systemStatus: MOCK_SYSTEM_STATUS,
  quickActions: MOCK_QUICK_ACTIONS,
  userName: 'Admin',
  userRole: 'Enterprise Administrator',
};

const emptyDashboardData: DashboardData = {
  kpis: [],
  widgets: [],
  activity: [],
  announcements: [],
  systemStatus: [],
  quickActions: [],
};

export function DashboardPreview() {
  const [widgets, setWidgets] = useState(MOCK_WIDGETS);

  const handlePin = useCallback((id: string) => {
    setWidgets((prev) => prev.map((w) => (w.id === id ? { ...w, pinned: !w.pinned } : w)));
  }, []);

  const handleRemove = useCallback((id: string) => {
    setWidgets((prev) => prev.map((w) => (w.id === id ? { ...w, visible: false } : w)));
  }, []);

  return (
    <div style={{ maxWidth: 1200, margin: '0 auto', padding: 'var(--space-section-gap) var(--space-page-x)' }}>
      <h2 style={{ fontSize: 'var(--text-h1)', margin: '0 0 var(--space-stack-xs)' }}>Enterprise Admin Dashboard</h2>
      <p style={{ color: 'var(--color-text-secondary)', marginBottom: 'var(--space-section-gap)' }}>
        Full dashboard layout with KPI cards, widgets, activity feed, announcements, system status, and quick actions.
      </p>

      <Section title="Desktop">
        <Frame>
          <DashboardLayout
            data={{ ...fullDashboardData, widgets }}
            onPinWidget={handlePin}
            onRemoveWidget={handleRemove}
          />
        </Frame>
      </Section>

      <Section title="Tablet (768px)">
        <div style={{ maxWidth: 768, margin: '0 auto' }}>
          <Frame>
            <DashboardLayout
              data={{ ...fullDashboardData, widgets }}
              onPinWidget={handlePin}
              onRemoveWidget={handleRemove}
            />
          </Frame>
        </div>
      </Section>

      <Section title="Mobile (375px)">
        <div style={{ maxWidth: 375, margin: '0 auto' }}>
          <Frame>
            <DashboardLayout
              data={{ ...fullDashboardData, widgets }}
              onPinWidget={handlePin}
              onRemoveWidget={handleRemove}
            />
          </Frame>
        </div>
      </Section>

      <Section title="Dark Theme">
        <Frame dark>
          <DashboardLayout
            data={{ ...fullDashboardData, widgets: widgets.filter((w) => w.visible).slice(0, 4) }}
            onPinWidget={handlePin}
            onRemoveWidget={handleRemove}
          />
        </Frame>
      </Section>

      <Section title="Loading Dashboard">
        <Frame>
          <DashboardLayout data={fullDashboardData} loading />
        </Frame>
      </Section>

      <Section title="Empty Dashboard">
        <Frame>
          <DashboardLayout data={emptyDashboardData} />
        </Frame>
      </Section>

      <Section title="Module KPI Integration">
        <Frame>
          <FrameLabel label="Products" />
          <KPIGrid kpis={getAllModuleKPIs().filter((k) => k.id.startsWith('products'))} columns={4} />
          <FrameLabel label="Orders" />
          <KPIGrid kpis={getAllModuleKPIs().filter((k) => k.id.startsWith('orders'))} columns={4} />
          <FrameLabel label="Finance" />
          <KPIGrid kpis={getAllModuleKPIs().filter((k) => k.id.startsWith('finance'))} columns={4} />
          <FrameLabel label="Analytics" />
          <KPIGrid kpis={getAllModuleKPIs().filter((k) => k.id.startsWith('analytics'))} columns={4} />
        </Frame>
      </Section>

      <Section title="Widget Variants">
        <Frame>
          <FrameLabel label="KPI Cards" />
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: 'var(--space-component-gap)', marginBottom: 24 }}>
            {fullDashboardData.kpis.slice(0, 4).map((kpi) => (
              <DashboardPreviewKPICard key={kpi.id} kpi={kpi} />
            ))}
          </div>
          <FrameLabel label="Widget Types" />
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: 'var(--space-component-gap)' }}>
            {fullDashboardData.widgets.filter((w) => w.visible).slice(0, 6).map((w) => (
              <DashboardPreviewWidgetCard key={w.id} widget={w} />
            ))}
          </div>
        </Frame>
      </Section>

      <Section title="Accessibility">
        <Frame>
          <ul style={{ color: 'var(--color-text-secondary)', lineHeight: 1.8 }}>
            <li>Dashboard uses semantic HTML: <code>&lt;section&gt;</code>, <code>&lt;h2&gt;</code>, <code>&lt;h3&gt;</code>, <code>&lt;button&gt;</code></li>
            <li>All interactive elements keyboard navigable</li>
            <li>KPI cards announce trend via text (not color-only)</li>
            <li>Widget menus have <code>aria-label</code></li>
            <li>Announcements support dismiss via keyboard</li>
            <li>Loading skeleton uses <code>prefers-reduced-motion</code> compatible animation</li>
            <li>Color contrast meets WCAG 2.2 AA for all text</li>
          </ul>
        </Frame>
      </Section>

      <Section title="Performance">
        <Frame>
          <ul style={{ color: 'var(--color-text-secondary)', lineHeight: 1.8 }}>
            <li>Individual component memoization prevents full dashboard re-renders</li>
            <li>CSS animations for loading skeleton (no JS-driven animation)</li>
            <li>State lifted to preview only, keeping individual widgets stateless</li>
            <li>Mock data uses <code>Array.from</code> for predictable rendering</li>
            <li>Widget grid uses CSS Grid for layout (no JS layout calculations)</li>
            <li>No external charting library loaded</li>
          </ul>
        </Frame>
      </Section>
    </div>
  );
}

function DashboardPreviewKPICard({ kpi }: { kpi: KPIData }) {
  return (
    <div style={{ background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: '20px 24px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 16 }}>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', fontWeight: 500, textTransform: 'uppercase' }}>{kpi.title}</span>
        <div style={{ width: 36, height: 36, borderRadius: 'var(--radius-md)', background: `${kpi.color}1A`, display: 'flex', alignItems: 'center', justifyContent: 'center', color: kpi.color }}><Icon name={kpi.icon} size={18} /></div>
      </div>
      <div style={{ fontSize: 'var(--text-h2)', fontWeight: 700, marginBottom: 8 }}>{kpi.value}</div>
      <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
        <span style={{ color: kpi.trend === 'up' ? 'var(--color-success)' : 'var(--color-error)', fontWeight: 600, fontSize: 'var(--text-body)' }}>
          {kpi.percentage > 0 ? '+' : ''}{kpi.percentage}%
        </span>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{kpi.comparison}</span>
      </div>
    </div>
  );
}

function DashboardPreviewWidgetCard({ widget }: { widget: WidgetConfig }) {
  return (
    <div style={{ background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', overflow: 'hidden' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 8, padding: '14px 16px', borderBottom: '1px solid var(--color-border)' }}>
        <Icon name={widget.icon} size={18} />
        <span style={{ fontWeight: 600, fontSize: 'var(--text-body)' }}>{widget.title}</span>
      </div>
      <div style={{ padding: 16, textAlign: 'center', color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>
        {widget.type} widget placeholder
      </div>
    </div>
  );
}
