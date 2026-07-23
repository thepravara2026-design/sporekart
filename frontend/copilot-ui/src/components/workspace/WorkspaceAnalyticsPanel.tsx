import React from 'react';

interface CopilotUsage {
  copilotId: string;
  copilotName: string;
  messageCount: number;
}

interface WorkspaceAnalytics {
  conversationCountToday: number;
  conversationCountWeek: number;
  activeSessions: number;
  copilotUsage: CopilotUsage[];
  avgLatencyMs: number;
  handoffCount: number;
  topIntents: { intent: string; count: number }[];
}

interface WorkspaceAnalyticsPanelProps {
  analytics?: WorkspaceAnalytics;
  loading?: boolean;
  style?: React.CSSProperties;
}

function StatCard({ label, value, unit }: { label: string; value: number | string; unit?: string }) {
  return (
    <div style={{
      flex: 1,
      minWidth: '100px',
      padding: '10px 12px',
      background: 'var(--cp-surface-2, #f1f5f9)',
      borderRadius: '8px',
      textAlign: 'center',
    }}>
      <div style={{ fontSize: '20px', fontWeight: 700, color: 'var(--cp-text, #1e293b)' }}>
        {value}
        {unit && <span style={{ fontSize: '12px', fontWeight: 400, marginLeft: '2px' }}>{unit}</span>}
      </div>
      <div style={{ fontSize: '11px', color: 'var(--cp-text-secondary, #64748b)', marginTop: '2px' }}>
        {label}
      </div>
    </div>
  );
}

function UsageBar({ name, count, total, color }: { name: string; count: number; total: number; color: string }) {
  const pct = total > 0 ? (count / total) * 100 : 0;
  return (
    <div style={{ marginBottom: '6px' }}>
      <div style={{
        display: 'flex',
        justifyContent: 'space-between',
        fontSize: '12px',
        marginBottom: '2px',
        color: 'var(--cp-text, #1e293b)',
      }}>
        <span>{name}</span>
        <span>{count}</span>
      </div>
      <div style={{
        height: '8px',
        borderRadius: '4px',
        background: 'var(--cp-surface-3, #e2e8f0)',
        overflow: 'hidden',
      }}>
        <div style={{
          width: `${pct}%`,
          height: '100%',
          borderRadius: '4px',
          background: color,
          transition: 'width 0.3s ease',
        }} />
      </div>
    </div>
  );
}

const BAR_COLORS = ['#6366f1', '#0ea5e9', '#f59e0b', '#10b981', '#ec4899', '#8b5cf6'];

export default function WorkspaceAnalyticsPanel({
  analytics,
  loading,
  style,
}: WorkspaceAnalyticsPanelProps) {
  const totalMessages = analytics?.copilotUsage.reduce((a, c) => a + c.messageCount, 0) ?? 0;

  return (
    <div style={{
      padding: '16px',
      display: 'flex',
      flexDirection: 'column',
      gap: '16px',
      ...style,
    }}>
      <div style={{ fontWeight: 600, fontSize: '14px', color: 'var(--cp-text, #1e293b)' }}>
        Workspace Analytics
      </div>

      {loading ? (
        <div style={{ padding: '24px', textAlign: 'center', color: 'var(--cp-text-secondary, #64748b)', fontSize: '13px' }}>
          Loading analytics...
        </div>
      ) : !analytics ? (
        <div style={{ padding: '24px', textAlign: 'center', color: 'var(--cp-text-secondary, #64748b)', fontSize: '13px' }}>
          No analytics available
        </div>
      ) : (
        <>
          <div style={{
            display: 'flex',
            flexWrap: 'wrap',
            gap: '8px',
          }}>
            <StatCard label="Today" value={analytics.conversationCountToday} unit="msgs" />
            <StatCard label="This Week" value={analytics.conversationCountWeek} unit="msgs" />
            <StatCard label="Active Sessions" value={analytics.activeSessions} />
            <StatCard label="Avg Latency" value={Math.round(analytics.avgLatencyMs)} unit="ms" />
            <StatCard label="Handoffs" value={analytics.handoffCount} />
          </div>

          <div style={{
            padding: '12px',
            background: 'var(--cp-surface-2, #f1f5f9)',
            borderRadius: '8px',
          }}>
            <div style={{ fontSize: '13px', fontWeight: 600, marginBottom: '8px', color: 'var(--cp-text, #1e293b)' }}>
              Copilot Usage Distribution
            </div>
            {analytics.copilotUsage.map((cu, i) => (
              <UsageBar
                key={cu.copilotId}
                name={cu.copilotName}
                count={cu.messageCount}
                total={totalMessages}
                color={BAR_COLORS[i % BAR_COLORS.length]}
              />
            ))}
            {analytics.copilotUsage.length === 0 && (
              <div style={{ fontSize: '12px', color: 'var(--cp-text-secondary, #94a3b8)', fontStyle: 'italic' }}>
                No usage data
              </div>
            )}
          </div>

          {analytics.topIntents.length > 0 && (
            <div style={{
              padding: '12px',
              background: 'var(--cp-surface-2, #f1f5f9)',
              borderRadius: '8px',
            }}>
              <div style={{ fontSize: '13px', fontWeight: 600, marginBottom: '8px', color: 'var(--cp-text, #1e293b)' }}>
                Top Intents
              </div>
              {analytics.topIntents.map((ti, i) => (
                <div key={ti.intent} style={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  fontSize: '12px',
                  padding: '3px 0',
                  color: 'var(--cp-text, #1e293b)',
                }}>
                  <span>{i + 1}. {ti.intent}</span>
                  <span style={{ color: 'var(--cp-text-secondary, #64748b)' }}>{ti.count}</span>
                </div>
              ))}
            </div>
          )}
        </>
      )}
    </div>
  );
}
