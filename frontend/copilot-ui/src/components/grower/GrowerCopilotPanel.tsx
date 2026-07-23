import React, { useState, useCallback, useRef, useEffect } from 'react';
import { useGrowerCopilot } from './useGrowerCopilot';
import GrowerCultivationTimeline from './GrowerCultivationTimeline';
import GrowerSpawnCard from './GrowerSpawnCard';
import GrowerSubstrateCard from './GrowerSubstrateCard';
import GrowerDiseaseCard from './GrowerDiseaseCard';
import GrowerYieldCard from './GrowerYieldCard';
import GrowerWeatherCard from './GrowerWeatherCard';
import GrowerBusinessPlanCard from './GrowerBusinessPlanCard';
import GrowerKnowledgeCard from './GrowerKnowledgeCard';
import type { ChatMessage } from './types/grower';

type Tab = 'overview' | 'cultivation' | 'spawn' | 'disease' | 'yield' | 'weather' | 'business' | 'knowledge';

const tabLabels: Record<Tab, string> = {
  overview: 'Overview',
  cultivation: 'Cultivation',
  spawn: 'Spawn & Substrate',
  disease: 'Disease',
  yield: 'Yield',
  weather: 'Weather',
  business: 'Business',
  knowledge: 'Knowledge',
};

const panelStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  height: '100%',
  background: 'var(--cp-bg, #0f0f23)',
  borderRadius: '16px',
  border: '1px solid var(--cp-border, #2a2a4a)',
  overflow: 'hidden',
};

const tabsRowStyle: React.CSSProperties = {
  display: 'flex',
  gap: '2px',
  padding: '8px 8px 0',
  overflowX: 'auto',
  background: 'var(--cp-surface, #1a1a2e)',
  borderBottom: '1px solid var(--cp-border, #2a2a4a)',
};

const tabButtonStyle = (active: boolean): React.CSSProperties => ({
  padding: '8px 14px',
  fontSize: '12px',
  fontWeight: 600,
  border: 'none',
  borderRadius: '8px 8px 0 0',
  cursor: 'pointer',
  whiteSpace: 'nowrap',
  background: active ? 'var(--cp-bg, #0f0f23)' : 'transparent',
  color: active ? 'var(--cp-accent, #4ade80)' : 'var(--cp-text-dim, #888)',
  borderBottom: active ? '2px solid var(--cp-accent, #4ade80)' : '2px solid transparent',
  transition: 'all 0.2s',
});

const contentAreaStyle: React.CSSProperties = {
  flex: 1,
  overflowY: 'auto',
  padding: '12px',
  display: 'flex',
  flexDirection: 'column',
  gap: '12px',
};

const quickActionsStyle: React.CSSProperties = {
  display: 'flex',
  gap: '8px',
  flexWrap: 'wrap',
  marginBottom: 8,
};

const quickBtnStyle: React.CSSProperties = {
  padding: '8px 14px',
  fontSize: '12px',
  fontWeight: 600,
  border: '1px solid var(--cp-border, #2a2a4a)',
  borderRadius: '8px',
  cursor: 'pointer',
  background: 'var(--cp-surface-alt, #16213e)',
  color: 'var(--cp-text, #e0e0e0)',
  transition: 'background 0.2s',
};

const chatContainerStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: '8px',
  maxHeight: 300,
  overflowY: 'auto',
  padding: '8px 0',
};

const messageBubbleStyle = (role: 'user' | 'assistant'): React.CSSProperties => ({
  alignSelf: role === 'user' ? 'flex-end' : 'flex-start',
  maxWidth: '80%',
  padding: '10px 14px',
  borderRadius: '12px',
  fontSize: '13px',
  lineHeight: 1.4,
  background: role === 'user' ? 'var(--cp-accent-dim, #2e7d4f)' : 'var(--cp-surface-alt, #16213e)',
  color: 'var(--cp-text, #e0e0e0)',
  border: role === 'user' ? 'none' : '1px solid var(--cp-border, #2a2a4a)',
});

const chatInputRowStyle: React.CSSProperties = {
  display: 'flex',
  gap: '8px',
  padding: '8px 0',
};

const inputStyle: React.CSSProperties = {
  flex: 1,
  padding: '10px 14px',
  fontSize: '13px',
  borderRadius: '8px',
  border: '1px solid var(--cp-border, #2a2a4a)',
  background: 'var(--cp-surface-alt, #16213e)',
  color: 'var(--cp-text, #e0e0e0)',
  outline: 'none',
};

const sendBtnStyle: React.CSSProperties = {
  padding: '10px 16px',
  fontSize: '12px',
  fontWeight: 600,
  border: 'none',
  borderRadius: '8px',
  cursor: 'pointer',
  background: 'var(--cp-accent, #4ade80)',
  color: '#000',
};

const suggestionBtnStyle: React.CSSProperties = {
  padding: '4px 10px',
  fontSize: '11px',
  border: '1px solid var(--cp-border, #2a2a4a)',
  borderRadius: '16px',
  cursor: 'pointer',
  background: 'transparent',
  color: 'var(--cp-accent, #4ade80)',
  margin: '2px 4px 2px 0',
};

const loadingOverlayStyle: React.CSSProperties = {
  padding: '8px 12px',
  fontSize: '12px',
  color: 'var(--cp-text-dim, #aaa)',
  fontStyle: 'italic',
};

const errorStyle: React.CSSProperties = {
  padding: '8px 12px',
  fontSize: '12px',
  color: '#f87171',
  background: '#3a2020',
  borderRadius: '8px',
};

const emptyStyle: React.CSSProperties = {
  padding: '24px',
  textAlign: 'center',
  color: 'var(--cp-text-dim, #888)',
  fontSize: '13px',
};

export default function GrowerCopilotPanel() {
  const [activeTab, setActiveTab] = useState<Tab>('overview');
  const [chatInput, setChatInput] = useState('');
  const chatEndRef = useRef<HTMLDivElement>(null);

  const {
    messages,
    spawnRecommendations,
    substrateRecommendations,
    diseaseDiagnoses,
    yieldPredictions,
    weather,
    forecast,
    businessPlans,
    knowledgeResults,
    cropHealth,
    loading,
    streaming,
    error,
    sendMessage,
    sendStreamMessage,
    recommendSpawn,
    recommendSubstrate,
    diagnoseDisease,
    predictYield,
    getWeather,
    getForecast,
    createBusinessPlan,
    searchKnowledge,
    getCropHealth,
    clearError,
  } = useGrowerCopilot();

  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const handleSend = useCallback(() => {
    const trimmed = chatInput.trim();
    if (!trimmed || loading || streaming) return;
    setChatInput('');
    sendMessage(trimmed);
  }, [chatInput, loading, streaming, sendMessage]);

  const handleKeyDown = useCallback((e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  }, [handleSend]);

  const handleSuggestionClick = useCallback((action: string) => {
    switch (action) {
      case 'diagnose': diagnoseDisease(); setActiveTab('disease'); break;
      case 'yield': predictYield(); setActiveTab('yield'); break;
      case 'weather': getWeather(); getForecast(); setActiveTab('weather'); break;
      case 'business': createBusinessPlan(); setActiveTab('business'); break;
      case 'spawn': recommendSpawn(); recommendSubstrate(); setActiveTab('spawn'); break;
      case 'knowledge': searchKnowledge('mushroom cultivation'); setActiveTab('knowledge'); break;
      case 'health': getCropHealth(); setActiveTab('overview'); break;
    }
  }, [diagnoseDisease, predictYield, getWeather, getForecast, createBusinessPlan, recommendSpawn, recommendSubstrate, searchKnowledge, getCropHealth]);

  const renderTabContent = () => {
    switch (activeTab) {
      case 'overview':
        return (
          <>
            <div style={quickActionsStyle}>
              <button style={quickBtnStyle} onClick={() => handleSuggestionClick('diagnose')}>🔍 Diagnose Disease</button>
              <button style={quickBtnStyle} onClick={() => handleSuggestionClick('yield')}>📊 Predict Yield</button>
              <button style={quickBtnStyle} onClick={() => handleSuggestionClick('weather')}>🌤️ Check Weather</button>
              <button style={quickBtnStyle} onClick={() => handleSuggestionClick('business')}>💼 Business Plan</button>
              <button style={quickBtnStyle} onClick={() => handleSuggestionClick('spawn')}>🧫 Spawn & Substrate</button>
              <button style={quickBtnStyle} onClick={() => handleSuggestionClick('knowledge')}>📚 Knowledge</button>
              <button style={quickBtnStyle} onClick={() => handleSuggestionClick('health')}>❤️ Crop Health</button>
            </div>

            {cropHealth && (
              <div style={{
                padding: '16px',
                background: 'var(--cp-surface, #1a1a2e)',
                borderRadius: '12px',
                border: '1px solid var(--cp-border, #2a2a4a)',
              }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12 }}>
                  <span style={{ fontSize: '16px', fontWeight: 600, color: 'var(--cp-text, #e0e0e0)' }}>Crop Health</span>
                  <span style={{
                    fontSize: '20px',
                    fontWeight: 700,
                    color: cropHealth.healthScore > 70 ? '#4ade80' : cropHealth.healthScore > 40 ? '#facc15' : '#f87171',
                  }}>
                    {cropHealth.healthScore}/100
                  </span>
                </div>
                {cropHealth.issues.length > 0 && (
                  <ul style={{ margin: '0 0 8px', paddingLeft: 16, fontSize: '12px', color: '#f87171' }}>
                    {cropHealth.issues.map(i => <li key={i}>{i}</li>)}
                  </ul>
                )}
                {cropHealth.recommendations.length > 0 && (
                  <ul style={{ margin: 0, paddingLeft: 16, fontSize: '12px', color: 'var(--cp-accent, #4ade80)' }}>
                    {cropHealth.recommendations.map(r => <li key={r}>{r}</li>)}
                  </ul>
                )}
              </div>
            )}

            {!cropHealth && <div style={emptyStyle}>Use the quick actions above or chat with the copilot.</div>}
          </>
        );

      case 'cultivation':
        return <GrowerCultivationTimeline />;

      case 'spawn':
        return (
          <>
            {spawnRecommendations.length > 0 && (
              <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
                <div style={{ fontSize: '14px', fontWeight: 600, color: 'var(--cp-text, #e0e0e0)' }}>Spawn Recommendations</div>
                {spawnRecommendations.map(s => <GrowerSpawnCard key={s.spawnId} recommendation={s} />)}
              </div>
            )}
            {substrateRecommendations.length > 0 && (
              <div style={{ display: 'flex', flexDirection: 'column', gap: 12, marginTop: 8 }}>
                <div style={{ fontSize: '14px', fontWeight: 600, color: 'var(--cp-text, #e0e0e0)' }}>Substrate Recommendations</div>
                {substrateRecommendations.map(s => <GrowerSubstrateCard key={s.substrateId} recommendation={s} />)}
              </div>
            )}
            {spawnRecommendations.length === 0 && substrateRecommendations.length === 0 && (
              <div style={emptyStyle}>Click "Spawn & Substrate" quick action to load recommendations.</div>
            )}
          </>
        );

      case 'disease':
        return (
          <>
            {diseaseDiagnoses.length > 0 ? (
              <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
                {diseaseDiagnoses.map(d => <GrowerDiseaseCard key={d.diseaseId} disease={d} />)}
              </div>
            ) : (
              <div style={emptyStyle}>Click "Diagnose Disease" quick action to run a diagnosis.</div>
            )}
          </>
        );

      case 'yield':
        return (
          <>
            {yieldPredictions.length > 0 ? (
              <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
                {yieldPredictions.map(y => <GrowerYieldCard key={y.predictionId} prediction={y} />)}
              </div>
            ) : (
              <div style={emptyStyle}>Click "Predict Yield" quick action to see predictions.</div>
            )}
          </>
        );

      case 'weather':
        return (
          <>
            {weather ? (
              <GrowerWeatherCard weather={weather} forecast={forecast} />
            ) : (
              <div style={emptyStyle}>Click "Check Weather" quick action to load weather data.</div>
            )}
          </>
        );

      case 'business':
        return (
          <>
            {businessPlans.length > 0 ? (
              <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
                {businessPlans.map(b => <GrowerBusinessPlanCard key={b.planId} plan={b} />)}
              </div>
            ) : (
              <div style={emptyStyle}>Click "Business Plan" quick action to generate a plan.</div>
            )}
          </>
        );

      case 'knowledge':
        return (
          <>
            {knowledgeResults.length > 0 ? (
              <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
                {knowledgeResults.map(k => <GrowerKnowledgeCard key={k.articleId} article={k} />)}
              </div>
            ) : (
              <div style={emptyStyle}>Click "Knowledge" quick action to search articles.</div>
            )}
          </>
        );
    }
  };

  return (
    <div style={panelStyle}>
      <div style={tabsRowStyle}>
        {(Object.keys(tabLabels) as Tab[]).map(tab => (
          <button key={tab} style={tabButtonStyle(activeTab === tab)} onClick={() => setActiveTab(tab)}>
            {tabLabels[tab]}
          </button>
        ))}
      </div>

      <div style={contentAreaStyle}>
        {error && (
          <div style={errorStyle}>
            {error}
            <button onClick={clearError} style={{ marginLeft: 8, background: 'none', border: 'none', color: '#f87171', cursor: 'pointer' }}>✕</button>
          </div>
        )}

        {renderTabContent()}

        <div style={{ borderTop: '1px solid var(--cp-border, #2a2a4a)', paddingTop: 8 }}>
          <div style={chatContainerStyle}>
            {messages.map(msg => (
              <div key={msg.id} style={messageBubbleStyle(msg.role)}>
                {msg.content}
                {msg.suggestions && msg.suggestions.length > 0 && (
                  <div style={{ marginTop: 8, display: 'flex', flexWrap: 'wrap' }}>
                    {msg.suggestions.map((s, i) => (
                      <button key={i} style={suggestionBtnStyle} onClick={() => handleSuggestionClick(s.action)}>
                        {s.label}
                      </button>
                    ))}
                  </div>
                )}
              </div>
            ))}
            {loading && <div style={loadingOverlayStyle}>Thinking...</div>}
            {streaming && <div style={loadingOverlayStyle}>Generating response...</div>}
            <div ref={chatEndRef} />
          </div>

          <div style={chatInputRowStyle}>
            <input
              style={inputStyle}
              placeholder="Ask the grower copilot..."
              value={chatInput}
              onChange={e => setChatInput(e.target.value)}
              onKeyDown={handleKeyDown}
              disabled={loading || streaming}
            />
            <button style={{ ...sendBtnStyle, opacity: loading || streaming ? 0.5 : 1 }} onClick={handleSend} disabled={loading || streaming}>
              Send
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
