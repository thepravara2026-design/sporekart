import React, { useState, useEffect, useCallback } from 'react';
import { useBiCopilot } from './useBiCopilot';
import HealthScoreGauge from './HealthScoreGauge';
import RevenueDashboard from './RevenueDashboard';
import CustomerDashboard from './CustomerDashboard';
import ProductDashboard from './ProductDashboard';
import InventoryDashboard from './InventoryDashboard';
import TrainingDashboard from './TrainingDashboard';
import ForecastPanel from './ForecastPanel';
import InsightsPanel from './InsightsPanel';
import RecommendationsPanel from './RecommendationsPanel';
import RiskPanel from './RiskPanel';
import type { ExecutiveSummary, ChatMessage } from './types/bi';

type TabId = 'dashboard' | 'revenue' | 'customers' | 'products' | 'inventory' | 'training' | 'forecast' | 'insights' | 'recommendations' | 'risks' | 'chat';

interface Tab { id: TabId; label: string; }

const tabs: Tab[] = [
  { id: 'dashboard', label: 'Executive Dashboard' },
  { id: 'revenue', label: 'Revenue' },
  { id: 'customers', label: 'Customers' },
  { id: 'products', label: 'Products' },
  { id: 'inventory', label: 'Inventory' },
  { id: 'training', label: 'Training' },
  { id: 'forecast', label: 'Forecast' },
  { id: 'insights', label: 'Insights' },
  { id: 'recommendations', label: 'Recommendations' },
  { id: 'risks', label: 'Risks' },
  { id: 'chat', label: 'Chat' },
];

export default function BiCopilotPanel() {
  const [activeTab, setActiveTab] = useState<TabId>('dashboard');
  const [dashboard, setDashboard] = useState<ExecutiveSummary | null>(null);
  const [chatInput, setChatInput] = useState('');
  const { loading, error, messages, sendMessage, getDashboard, getForecast } = useBiCopilot();

  useEffect(() => {
    if (!dashboard) {
      getDashboard().then(setDashboard);
    }
  }, [dashboard, getDashboard]);

  const handleSend = useCallback(async () => {
    if (!chatInput.trim()) return;
    setChatInput('');
    await sendMessage(chatInput);
  }, [chatInput, sendMessage]);

  const handleMetricChange = useCallback(async (metric: string) => {
    const forecast = await getForecast(metric);
    if (forecast && dashboard) {
      setDashboard({ ...dashboard, ...{ forecast } } as ExecutiveSummary);
    }
  }, [getForecast, dashboard]);

  const renderContent = () => {
    if (loading && !dashboard && activeTab !== 'chat') {
      return <div style={{ textAlign: 'center', padding: 48, color: '#6b7280' }}>Loading dashboard data...</div>;
    }
    if (error && !dashboard) {
      return <div style={{ textAlign: 'center', padding: 48, color: '#ef4444' }}>Error: {error}</div>;
    }

    switch (activeTab) {
      case 'dashboard':
        return dashboard ? (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 32 }}>
            <HealthScoreGauge healthScore={dashboard.healthScore} />
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))', gap: 12 }}>
              {dashboard.insights.slice(0, 4).map(insight => (
                <div key={insight.insightId} style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 12 }}>
                  <span style={{ fontSize: 11, fontWeight: 600, color: insight.severity === 'CRITICAL' ? '#ef4444' : insight.severity === 'IMPORTANT' ? '#f97316' : '#3b82f6' }}>{insight.severity}</span>
                  <h4 style={{ fontSize: 13, fontWeight: 600, color: '#111827', margin: '4px 0' }}>{insight.title}</h4>
                  <p style={{ fontSize: 12, color: '#6b7280', margin: 0, lineHeight: 1.3 }}>{insight.description}</p>
                </div>
              ))}
            </div>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 24 }}>
              {dashboard.recommendations.length > 0 && (
                <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 16 }}>
                  <h3 style={{ fontSize: 14, fontWeight: 600, color: '#111827', margin: '0 0 12px' }}>Top Recommendations</h3>
                  {dashboard.recommendations.slice(0, 3).map(r => (
                    <div key={r.recommendationId} style={{ padding: '8px 0', borderBottom: '1px solid #f3f4f6' }}>
                      <div style={{ fontSize: 13, fontWeight: 600, color: '#111827' }}>{r.title}</div>
                      <div style={{ fontSize: 12, color: '#6b7280' }}>{r.description}</div>
                    </div>
                  ))}
                </div>
              )}
              {dashboard.risks.length > 0 && (
                <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 16 }}>
                  <h3 style={{ fontSize: 14, fontWeight: 600, color: '#111827', margin: '0 0 12px' }}>Active Risks</h3>
                  {dashboard.risks.filter(r => r.status === 'active').slice(0, 3).map(r => (
                    <div key={r.riskId} style={{ padding: '8px 0', borderBottom: '1px solid #f3f4f6', borderLeft: `3px solid ${r.severity === 'critical' ? '#ef4444' : r.severity === 'high' ? '#f97316' : '#eab308'}`, paddingLeft: 8 }}>
                      <div style={{ fontSize: 13, fontWeight: 600, color: '#111827' }}>{r.title}</div>
                      <div style={{ fontSize: 12, color: '#6b7280' }}>{r.description}</div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        ) : null;
      case 'revenue':
        return dashboard ? <RevenueDashboard data={dashboard.revenue} /> : null;
      case 'customers':
        return dashboard ? <CustomerDashboard data={dashboard.customers} /> : null;
      case 'products':
        return dashboard ? <ProductDashboard data={dashboard.products} /> : null;
      case 'inventory':
        return dashboard ? <InventoryDashboard data={dashboard.inventory} /> : null;
      case 'training':
        return dashboard ? <TrainingDashboard data={dashboard.training} /> : null;
      case 'forecast':
        return dashboard ? <ForecastPanel data={dashboard as any} onMetricChange={handleMetricChange} /> : null;
      case 'insights':
        return dashboard ? <InsightsPanel insights={dashboard.insights} /> : null;
      case 'recommendations':
        return dashboard ? <RecommendationsPanel recommendations={dashboard.recommendations} /> : null;
      case 'risks':
        return dashboard ? <RiskPanel risks={dashboard.risks} /> : null;
      case 'chat':
        return (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 16, height: 500 }}>
            <div style={{ flex: 1, overflowY: 'auto', display: 'flex', flexDirection: 'column', gap: 12, padding: '0 4px' }}>
              {messages.length === 0 && <div style={{ textAlign: 'center', padding: 48, color: '#9ca3af', fontSize: 14 }}>Ask a question about your business data</div>}
              {messages.map(msg => (
                <div key={msg.id} style={{ display: 'flex', justifyContent: msg.role === 'user' ? 'flex-end' : 'flex-start' }}>
                  <div style={{ maxWidth: '80%', padding: '10px 14px', borderRadius: 12, background: msg.role === 'user' ? '#3b82f6' : '#f3f4f6', color: msg.role === 'user' ? '#fff' : '#111827', fontSize: 13, lineHeight: 1.4 }}>
                    {msg.content}
                    {msg.suggestions && msg.suggestions.length > 0 && (
                      <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6, marginTop: 8 }}>
                        {msg.suggestions.map((s, i) => (
                          <button key={i} onClick={() => sendMessage(s.action)} style={{ padding: '4px 10px', background: msg.role === 'user' ? '#2563eb' : '#e5e7eb', border: 'none', borderRadius: 4, cursor: 'pointer', fontSize: 11, color: msg.role === 'user' ? '#fff' : '#374151' }}>
                            {s.label}
                          </button>
                        ))}
                      </div>
                    )}
                  </div>
                </div>
              ))}
              {loading && <div style={{ textAlign: 'center', color: '#9ca3af', fontSize: 12 }}>Thinking...</div>}
            </div>
            <div style={{ display: 'flex', gap: 8 }}>
              <input type="text" value={chatInput} onChange={e => setChatInput(e.target.value)} onKeyDown={e => e.key === 'Enter' && handleSend()}
                placeholder="Ask about revenue, customers, trends..."
                style={{ flex: 1, padding: '10px 14px', border: '1px solid #e5e7eb', borderRadius: 8, fontSize: 13, outline: 'none' }} />
              <button onClick={handleSend} disabled={loading || !chatInput.trim()}
                style={{ padding: '10px 20px', background: '#3b82f6', color: '#fff', border: 'none', borderRadius: 8, cursor: 'pointer', fontSize: 13, fontWeight: 600, opacity: loading || !chatInput.trim() ? 0.5 : 1 }}>
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
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16, fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif' }}>
      <div style={{ display: 'flex', gap: 2, borderBottom: '1px solid #e5e7eb', paddingBottom: 8, overflowX: 'auto', flexWrap: 'nowrap' }}>
        {tabs.map(tab => (
          <button key={tab.id} onClick={() => setActiveTab(tab.id)}
            style={{ padding: '8px 16px', border: 'none', cursor: 'pointer', fontSize: 13, fontWeight: 500, whiteSpace: 'nowrap',
              background: activeTab === tab.id ? '#eff6ff' : 'transparent', color: activeTab === tab.id ? '#3b82f6' : '#6b7280',
              borderBottom: activeTab === tab.id ? '2px solid #3b82f6' : '2px solid transparent', transition: 'all 0.15s' }}>
            {tab.label}
          </button>
        ))}
      </div>
      {error && <div style={{ padding: '8px 16px', background: '#fef2f2', border: '1px solid #fecaca', borderRadius: 6, color: '#ef4444', fontSize: 12 }}>{error}</div>}
      <div>{renderContent()}</div>
    </div>
  );
}
