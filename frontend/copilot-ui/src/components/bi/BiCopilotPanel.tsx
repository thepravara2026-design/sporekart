import React, { useState } from 'react';
import type {
  DashboardData,
  RevenueMetrics,
  CustomerAnalytics,
  BusinessInsight,
  TrendDataPoint,
  AnomalyAlert,
  ForecastResult,
  CrossCopilotMetric,
  CustomerSegment,
  ChatMessage,
} from './types/bi';
import { useBiCopilot } from './useBiCopilot';
import { BiDashboardPanel } from './BiDashboardPanel';
import { BiInsightCard } from './BiInsightCard';
import { BiRevenueChart } from './BiRevenueChart';
import { BiCustomerChart } from './BiCustomerChart';
import { BiForecastCard } from './BiForecastCard';
import { BiAnomalyAlert } from './BiAnomalyAlert';
import { BiReportGenerator } from './BiReportGenerator';
import type { ReportConfig } from './BiReportGenerator';

type TabId = 'Dashboard' | 'Insights' | 'Trends' | 'Forecast' | 'Segments' | 'Cross-Copilot' | 'Reports' | 'Chat';

interface Tab {
  id: TabId;
  label: string;
}

const TABS: Tab[] = [
  { id: 'Dashboard', label: 'Dashboard' },
  { id: 'Insights', label: 'Insights' },
  { id: 'Trends', label: 'Trends' },
  { id: 'Forecast', label: 'Forecast' },
  { id: 'Segments', label: 'Segments' },
  { id: 'Cross-Copilot', label: 'Cross-Copilot' },
  { id: 'Reports', label: 'Reports' },
  { id: 'Chat', label: 'Chat' },
];

const METRIC_OPTIONS = [
  { value: 'revenue', label: 'Revenue' },
  { value: 'orders', label: 'Orders' },
  { value: 'customers', label: 'Customers' },
  { value: 'students', label: 'Students' },
];

export const BiCopilotPanel: React.FC = () => {
  const [activeTab, setActiveTab] = useState<TabId>('Dashboard');
  const [selectedTrendMetric, setSelectedTrendMetric] = useState('revenue');
  const [forecastMetric, setForecastMetric] = useState('revenue');
  const [forecastPeriods, setForecastPeriods] = useState(6);

  const {
    loading,
    error,
    revenueMetrics,
    customerAnalytics,
    trainingAnalytics,
    cultivationAnalytics,
    insights,
    trends,
    anomalies,
    forecast,
    crossCopilotMetrics,
    segments,
    dashboard,
    messages,
    streaming,
    getDashboard,
    getInsights,
    getTrends,
    getAnomalies,
    forecast: runForecast,
    getCrossCopilotMetrics,
    getSegments,
    sendMessage,
    sendStreamMessage,
    generateReport,
  } = useBiCopilot();

  const handleDashboardChange = async (_type: string) => {
    await getDashboard(_type.toLowerCase());
  };

  const handleRefreshDashboard = () => {
    getDashboard();
    getInsights();
    getAnomalies();
  };

  const handleGenerateReport = async (config: ReportConfig): Promise<Blob | null> => {
    return generateReport(config as unknown as Record<string, unknown>);
  };

  const handleSendChat = async () => {
    const input = document.querySelector<HTMLTextAreaElement>('#bi-chat-input');
    if (!input || !input.value.trim()) return;
    const text = input.value;
    input.value = '';
    await sendMessage(text);
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSendChat();
    }
  };

  const renderTabContent = () => {
    switch (activeTab) {
      case 'Dashboard':
        return (
          <BiDashboardPanel
            dashboard={dashboard}
            revenueMetrics={revenueMetrics}
            customerAnalytics={customerAnalytics}
            insights={insights}
            anomalies={anomalies}
            onRefresh={handleRefreshDashboard}
            onDashboardChange={handleDashboardChange}
            loading={loading}
          />
        );

      case 'Insights':
        return (
          <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
              <h3 style={{ margin: 0, fontSize: 16, fontWeight: 600, color: '#111827' }}>Business Insights</h3>
              <button
                onClick={() => getInsights()}
                disabled={loading}
                style={{
                  padding: '6px 14px',
                  borderRadius: 6,
                  border: '1px solid #e5e7eb',
                  background: '#fff',
                  color: '#374151',
                  fontSize: 13,
                  cursor: 'pointer',
                }}
              >
                {loading ? 'Loading...' : 'Refresh'}
              </button>
            </div>
            {insights.length === 0 && !loading && (
              <div style={{ textAlign: 'center', color: '#9ca3af', padding: 40 }}>No insights available</div>
            )}
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 12 }}>
              {insights.map(insight => (
                <BiInsightCard key={insight.insightId} insight={insight} />
              ))}
            </div>
          </div>
        );

      case 'Trends':
        return (
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: 12, marginBottom: 16 }}>
              <h3 style={{ margin: 0, fontSize: 16, fontWeight: 600, color: '#111827' }}>Trend Analysis</h3>
              <select
                value={selectedTrendMetric}
                onChange={e => {
                  setSelectedTrendMetric(e.target.value);
                  getTrends(e.target.value);
                }}
                style={{
                  padding: '6px 10px',
                  borderRadius: 6,
                  border: '1px solid #e5e7eb',
                  fontSize: 13,
                  color: '#111827',
                }}
              >
                {METRIC_OPTIONS.map(m => <option key={m.value} value={m.value}>{m.label}</option>)}
              </select>
            </div>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
              <BiRevenueChart data={trends} />
              <BiCustomerChart />
            </div>
            {trends.length > 0 && (
              <div style={{ marginTop: 16, background: '#fff', borderRadius: 8, padding: 16, border: '1px solid #e5e7eb' }}>
                <table style={{ width: '100%', fontSize: 12, borderCollapse: 'collapse' }}>
                  <thead>
                    <tr style={{ borderBottom: '2px solid #e5e7eb' }}>
                      <th style={{ textAlign: 'left', padding: '6px 8px', color: '#6b7280' }}>Period</th>
                      <th style={{ textAlign: 'right', padding: '6px 8px', color: '#6b7280' }}>Value</th>
                      <th style={{ textAlign: 'right', padding: '6px 8px', color: '#6b7280' }}>Moving Avg</th>
                      <th style={{ textAlign: 'right', padding: '6px 8px', color: '#6b7280' }}>Change</th>
                      <th style={{ textAlign: 'center', padding: '6px 8px', color: '#6b7280' }}>Direction</th>
                    </tr>
                  </thead>
                  <tbody>
                    {trends.map(point => (
                      <tr key={point.period} style={{ borderBottom: '1px solid #f3f4f6' }}>
                        <td style={{ padding: '6px 8px', color: '#374151' }}>{point.period}</td>
                        <td style={{ padding: '6px 8px', textAlign: 'right', color: '#111827', fontWeight: 600 }}>{point.value.toLocaleString()}</td>
                        <td style={{ padding: '6px 8px', textAlign: 'right', color: '#6b7280' }}>{point.movingAverage.toFixed(1)}</td>
                        <td style={{ padding: '6px 8px', textAlign: 'right', color: point.changePercent >= 0 ? '#22c55e' : '#ef4444' }}>
                          {point.changePercent >= 0 ? '+' : ''}{point.changePercent.toFixed(2)}%
                        </td>
                        <td style={{ padding: '6px 8px', textAlign: 'center' }}>
                          <span style={{
                            display: 'inline-block',
                            padding: '1px 8px',
                            borderRadius: 10,
                            fontSize: 10,
                            fontWeight: 600,
                            background: point.direction === 'up' ? '#dcfce7' : point.direction === 'down' ? '#fef2f2' : '#f3f4f6',
                            color: point.direction === 'up' ? '#22c55e' : point.direction === 'down' ? '#ef4444' : '#6b7280',
                          }}>
                            {point.direction}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        );

      case 'Forecast':
        return (
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: 12, marginBottom: 16, flexWrap: 'wrap' }}>
              <h3 style={{ margin: 0, fontSize: 16, fontWeight: 600, color: '#111827' }}>Forecast</h3>
              <select
                value={forecastMetric}
                onChange={e => setForecastMetric(e.target.value)}
                style={{ padding: '6px 10px', borderRadius: 6, border: '1px solid #e5e7eb', fontSize: 13, color: '#111827' }}
              >
                {METRIC_OPTIONS.map(m => <option key={m.value} value={m.value}>{m.label}</option>)}
              </select>
              <input
                type="number"
                value={forecastPeriods}
                onChange={e => setForecastPeriods(Math.max(1, parseInt(e.target.value) || 1))}
                min={1}
                max={24}
                style={{ width: 60, padding: '6px 10px', borderRadius: 6, border: '1px solid #e5e7eb', fontSize: 13, color: '#111827' }}
              />
              <span style={{ fontSize: 12, color: '#6b7280' }}>periods</span>
              <button
                onClick={() => runForecast(forecastMetric, forecastPeriods)}
                disabled={loading}
                style={{
                  padding: '6px 14px',
                  borderRadius: 6,
                  border: 'none',
                  background: '#3b82f6',
                  color: '#fff',
                  fontSize: 13,
                  fontWeight: 500,
                  cursor: 'pointer',
                }}
              >
                {loading ? 'Running...' : 'Run Forecast'}
              </button>
            </div>
            {forecast && <BiForecastCard forecast={forecast} />}
            {!forecast && !loading && (
              <div style={{ textAlign: 'center', color: '#9ca3af', padding: 60 }}>Select a metric and run forecast</div>
            )}
          </div>
        );

      case 'Segments':
        return (
          <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
              <h3 style={{ margin: 0, fontSize: 16, fontWeight: 600, color: '#111827' }}>Customer Segments</h3>
              <button
                onClick={() => getSegments()}
                disabled={loading}
                style={{ padding: '6px 14px', borderRadius: 6, border: '1px solid #e5e7eb', background: '#fff', fontSize: 13, cursor: 'pointer' }}
              >
                {loading ? 'Loading...' : 'Refresh'}
              </button>
            </div>
            {segments.length === 0 && !loading && (
              <div style={{ textAlign: 'center', color: '#9ca3af', padding: 40 }}>No segment data available</div>
            )}
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: 12 }}>
              {segments.map(segment => (
                <div key={segment.segmentId} style={{ background: '#fff', borderRadius: 8, padding: 16, border: '1px solid #e5e7eb', boxShadow: '0 1px 3px rgba(0,0,0,0.08)' }}>
                  <div style={{ fontSize: 15, fontWeight: 600, color: '#111827', marginBottom: 10 }}>{segment.name}</div>
                  <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 8, marginBottom: 10 }}>
                    <div>
                      <div style={{ fontSize: 11, color: '#9ca3af' }}>Customers</div>
                      <div style={{ fontSize: 16, fontWeight: 700, color: '#111827' }}>{segment.customerCount.toLocaleString()}</div>
                    </div>
                    <div>
                      <div style={{ fontSize: 11, color: '#9ca3af' }}>Revenue</div>
                      <div style={{ fontSize: 16, fontWeight: 700, color: '#111827' }}>${segment.totalRevenue.toLocaleString()}</div>
                    </div>
                    <div>
                      <div style={{ fontSize: 11, color: '#9ca3af' }}>Avg Revenue</div>
                      <div style={{ fontSize: 14, fontWeight: 600, color: '#374151' }}>${segment.averageRevenue.toFixed(2)}</div>
                    </div>
                    <div>
                      <div style={{ fontSize: 11, color: '#9ca3af' }}>Churn Rate</div>
                      <div style={{ fontSize: 14, fontWeight: 600, color: '#ef4444' }}>{(segment.churnRate * 100).toFixed(1)}%</div>
                    </div>
                  </div>
                  {segment.recommendedStrategies.length > 0 && (
                    <div>
                      <div style={{ fontSize: 11, fontWeight: 600, color: '#374151', marginBottom: 4 }}>Strategies</div>
                      {segment.recommendedStrategies.map((strategy, i) => (
                        <div key={i} style={{ fontSize: 12, color: '#6b7280', padding: '2px 0' }}>&bull; {strategy}</div>
                      ))}
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
        );

      case 'Cross-Copilot':
        return (
          <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
              <h3 style={{ margin: 0, fontSize: 16, fontWeight: 600, color: '#111827' }}>Cross-Copilot Metrics</h3>
              <button
                onClick={() => getCrossCopilotMetrics()}
                disabled={loading}
                style={{ padding: '6px 14px', borderRadius: 6, border: '1px solid #e5e7eb', background: '#fff', fontSize: 13, cursor: 'pointer' }}
              >
                {loading ? 'Loading...' : 'Refresh'}
              </button>
            </div>
            {crossCopilotMetrics.length === 0 && !loading && (
              <div style={{ textAlign: 'center', color: '#9ca3af', padding: 40 }}>No cross-copilot data available</div>
            )}
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 12 }}>
              {crossCopilotMetrics.map((metric, i) => (
                <div key={`${metric.copilotType}-${metric.metricName}-${i}`} style={{ background: '#fff', borderRadius: 8, padding: 16, border: '1px solid #e5e7eb' }}>
                  <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 6 }}>
                    <span style={{ fontSize: 12, color: '#6b7280' }}>{metric.copilotType}</span>
                    <span style={{
                      fontSize: 10,
                      fontWeight: 600,
                      padding: '2px 8px',
                      borderRadius: 10,
                      background: metric.trend === 'up' ? '#dcfce7' : metric.trend === 'down' ? '#fef2f2' : '#f3f4f6',
                      color: metric.trend === 'up' ? '#22c55e' : metric.trend === 'down' ? '#ef4444' : '#6b7280',
                    }}>
                      {metric.trend}
                    </span>
                  </div>
                  <div style={{ fontSize: 13, fontWeight: 600, color: '#374151', marginBottom: 6 }}>{metric.metricName}</div>
                  <div style={{ display: 'flex', alignItems: 'baseline', gap: 8 }}>
                    <span style={{ fontSize: 22, fontWeight: 700, color: '#111827' }}>{metric.currentValue.toLocaleString()}</span>
                    <span style={{ fontSize: 12, color: metric.changePercent >= 0 ? '#22c55e' : '#ef4444' }}>
                      {metric.changePercent >= 0 ? '+' : ''}{metric.changePercent.toFixed(1)}%
                    </span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        );

      case 'Reports':
        return <BiReportGenerator onGenerate={handleGenerateReport} loading={loading} />;

      case 'Chat':
        return (
          <div style={{ display: 'flex', flexDirection: 'column', height: 500 }}>
            <h3 style={{ margin: '0 0 16px 0', fontSize: 16, fontWeight: 600, color: '#111827' }}>BI Chat</h3>
            <div style={{ flex: 1, overflowY: 'auto', marginBottom: 12, display: 'flex', flexDirection: 'column', gap: 10 }}>
              {messages.length === 0 && (
                <div style={{ textAlign: 'center', color: '#9ca3af', padding: 40 }}>
                  Ask a question about your business data
                </div>
              )}
              {messages.map(msg => (
                <div
                  key={msg.id}
                  style={{
                    alignSelf: msg.role === 'user' ? 'flex-end' : 'flex-start',
                    maxWidth: '80%',
                    background: msg.role === 'user' ? '#3b82f6' : '#f3f4f6',
                    color: msg.role === 'user' ? '#fff' : '#111827',
                    borderRadius: 12,
                    padding: '10px 14px',
                    fontSize: 13,
                    lineHeight: 1.4,
                  }}
                >
                  {msg.content}
                  {msg.suggestions && msg.suggestions.length > 0 && (
                    <div style={{ marginTop: 8, display: 'flex', flexWrap: 'wrap', gap: 4 }}>
                      {msg.suggestions.map((s, i) => (
                        <button
                          key={i}
                          onClick={async () => {
                            await sendMessage(s.label);
                          }}
                          style={{
                            padding: '4px 10px',
                            borderRadius: 12,
                            border: '1px solid',
                            borderColor: msg.role === 'assistant' ? '#e5e7eb' : 'rgba(255,255,255,0.3)',
                            background: 'transparent',
                            color: msg.role === 'assistant' ? '#374151' : '#fff',
                            fontSize: 11,
                            cursor: 'pointer',
                          }}
                        >
                          {s.label}
                        </button>
                      ))}
                    </div>
                  )}
                </div>
              ))}
              {streaming && (
                <div style={{ alignSelf: 'flex-start', background: '#f3f4f6', borderRadius: 12, padding: '10px 14px', fontSize: 13, color: '#6b7280' }}>
                  Typing...
                </div>
              )}
            </div>
            <div style={{ display: 'flex', gap: 8 }}>
              <textarea
                id="bi-chat-input"
                placeholder="Ask about revenue, customers, or trends..."
                onKeyDown={handleKeyDown}
                rows={2}
                style={{
                  flex: 1,
                  padding: '8px 12px',
                  borderRadius: 8,
                  border: '1px solid #e5e7eb',
                  fontSize: 13,
                  color: '#111827',
                  resize: 'none',
                }}
              />
              <button
                onClick={handleSendChat}
                disabled={loading || streaming}
                style={{
                  padding: '8px 16px',
                  borderRadius: 8,
                  border: 'none',
                  background: '#3b82f6',
                  color: '#fff',
                  fontSize: 13,
                  fontWeight: 600,
                  cursor: 'pointer',
                  alignSelf: 'flex-end',
                }}
              >
                Send
              </button>
            </div>
          </div>
        );

      default:
        return null;
    }
  };

  return (
    <div>
      {error && (
        <div style={{ background: '#fef2f2', color: '#ef4444', padding: '8px 14px', borderRadius: 6, marginBottom: 12, fontSize: 13, border: '1px solid #fecaca' }}>
          {error}
        </div>
      )}

      {/* Tabs */}
      <div style={{ display: 'flex', gap: 4, marginBottom: 20, overflowX: 'auto', paddingBottom: 4 }}>
        {TABS.map(tab => (
          <button
            key={tab.id}
            onClick={() => setActiveTab(tab.id)}
            style={{
              padding: '8px 16px',
              borderRadius: 6,
              border: 'none',
              background: activeTab === tab.id ? '#3b82f6' : 'transparent',
              color: activeTab === tab.id ? '#fff' : '#6b7280',
              fontSize: 13,
              fontWeight: 500,
              cursor: 'pointer',
              whiteSpace: 'nowrap',
            }}
          >
            {tab.label}
          </button>
        ))}
      </div>

      {/* Tab Content */}
      {renderTabContent()}
    </div>
  );
};
