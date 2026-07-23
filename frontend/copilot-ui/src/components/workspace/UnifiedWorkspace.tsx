import React, { useState, useMemo } from 'react';
import { useWorkspace } from './useWorkspace';
import CopilotSwitcher from './CopilotSwitcher';
import CopilotMessageBubble from './CopilotMessageBubble';
import ConversationSidebar from './ConversationSidebar';
import SharedContextPanel from './SharedContextPanel';
import WorkspaceAnalyticsPanel from './WorkspaceAnalyticsPanel';
import ActivityTimeline from './ActivityTimeline';
import FloatingAssistant from './FloatingAssistant';
import type { TimelineEvent } from './types/workspace';

interface UnifiedWorkspaceProps {
  workspaceId: string;
  workspaceName?: string;
  style?: React.CSSProperties;
}

type TabId = 'chat' | 'context' | 'analytics' | 'activity';

const TAB_LABELS: Record<TabId, string> = {
  chat: 'Chat',
  context: 'Context',
  analytics: 'Analytics',
  activity: 'Activity',
};

export default function UnifiedWorkspace({
  workspaceId,
  workspaceName = 'Workspace',
  style,
}: UnifiedWorkspaceProps) {
  const {
    messages,
    copilots,
    activeCopilotId,
    context,
    loading,
    error,
    sendMessage,
    sendStreamMessage,
    switchCopilot,
    getContext,
    fetchHistory,
    fetchStatus,
  } = useWorkspace(workspaceId);

  const [streamMode, setStreamMode] = useState(false);
  const [inputValue, setInputValue] = useState('');
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [contextPanelOpen, setContextPanelOpen] = useState(false);
  const [floatingAssistantOpen, setFloatingAssistantOpen] = useState(false);
  const [activeTab, setActiveTab] = useState<TabId>('chat');

  const copilotColorMap = useMemo(() => {
    const map: Record<string, string> = {};
    const colors = ['#6366f1', '#0ea5e9', '#f59e0b', '#10b981', '#ec4899', '#8b5cf6'];
    copilots.forEach((c, i) => {
      map[c.copilotId] = colors[i % colors.length];
    });
    return map;
  }, [copilots]);

  const timelineEvents = useMemo((): TimelineEvent[] => {
    return messages.map(m => ({
      id: m.id,
      type: m.handoff ? 'handoff' as const : 'message' as const,
      title: m.role === 'user' ? 'User message' : `${m.copilotName ?? 'Assistant'} response`,
      description: m.content.slice(0, 100),
      copilotName: m.copilotName,
      copilotId: m.copilotId,
      timestamp: m.timestamp,
    }));
  }, [messages]);

  const handleSend = () => {
    if (!inputValue.trim()) return;
    if (streamMode) {
      sendStreamMessage(inputValue);
    } else {
      sendMessage(inputValue);
    }
    setInputValue('');
  };

  const handleInputKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  const handleSuggestionClick = (action: string, payload?: Record<string, unknown>) => {
    sendMessage(action);
  };

  const activeCopilot = copilots.find(c => c.copilotId === activeCopilotId);

  return (
    <div style={{
      display: 'flex',
      flexDirection: 'column',
      height: '100vh',
      background: 'var(--cp-bg, #f8fafc)',
      color: 'var(--cp-text, #1e293b)',
      fontFamily: 'system-ui, -apple-system, sans-serif',
      fontSize: '14px',
      position: 'relative',
      overflow: 'hidden',
      ...style,
    }}>
      {/* Top Bar */}
      <div style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        padding: '8px 16px',
        background: 'var(--cp-surface, #fff)',
        borderBottom: '1px solid var(--cp-border, #e2e8f0)',
        flexShrink: 0,
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
          <CopilotSwitcher
            copilots={copilots}
            activeCopilotId={activeCopilotId}
            onSwitch={switchCopilot}
          />
          <span style={{ fontWeight: 600, fontSize: '15px', color: 'var(--cp-text, #1e293b)' }}>
            {workspaceName}
          </span>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
          <button
            onClick={() => setSidebarOpen(o => !o)}
            style={{
              padding: '6px 10px',
              borderRadius: '6px',
              border: '1px solid var(--cp-border, #e2e8f0)',
              background: sidebarOpen ? 'var(--cp-primary-bg, #e0e7ff)' : 'var(--cp-surface, #fff)',
              cursor: 'pointer',
              fontSize: '12px',
              color: 'var(--cp-text, #1e293b)',
            }}
          >
            {sidebarOpen ? 'Hide History' : 'History'}
          </button>
          <button
            onClick={() => setContextPanelOpen(o => !o)}
            style={{
              padding: '6px 10px',
              borderRadius: '6px',
              border: '1px solid var(--cp-border, #e2e8f0)',
              background: contextPanelOpen ? 'var(--cp-primary-bg, #e0e7ff)' : 'var(--cp-surface, #fff)',
              cursor: 'pointer',
              fontSize: '12px',
              color: 'var(--cp-text, #1e293b)',
            }}
          >
            {contextPanelOpen ? 'Hide Context' : 'Context'}
          </button>
          <button
            onClick={() => setFloatingAssistantOpen(o => !o)}
            style={{
              padding: '6px 10px',
              borderRadius: '6px',
              border: '1px solid var(--cp-border, #e2e8f0)',
              background: 'var(--cp-surface, #fff)',
              cursor: 'pointer',
              fontSize: '12px',
              color: 'var(--cp-text, #1e293b)',
            }}
          >
            {floatingAssistantOpen ? 'Hide Assistant' : 'Assistant'}
          </button>
        </div>
      </div>

      {/* Main Content */}
      <div style={{
        display: 'flex',
        flex: 1,
        overflow: 'hidden',
      }}>
        {/* Conversation Sidebar */}
        {sidebarOpen && (
          <ConversationSidebar
            messages={messages}
            onSelectMessage={() => {}}
            onClearConversation={() => {}}
            onExport={() => {}}
          />
        )}

        {/* Center Area */}
        <div style={{
          flex: 1,
          display: 'flex',
          flexDirection: 'column',
          overflow: 'hidden',
        }}>
          {/* Tabs */}
          <div style={{
            display: 'flex',
            borderBottom: '1px solid var(--cp-border, #e2e8f0)',
            background: 'var(--cp-surface, #fff)',
            flexShrink: 0,
          }}>
            {(Object.keys(TAB_LABELS) as TabId[]).map(tab => (
              <button
                key={tab}
                onClick={() => setActiveTab(tab)}
                style={{
                  flex: 1,
                  padding: '10px 12px',
                  border: 'none',
                  background: activeTab === tab ? 'var(--cp-primary-bg, #e0e7ff)' : 'transparent',
                  cursor: 'pointer',
                  fontSize: '13px',
                  fontWeight: activeTab === tab ? 600 : 400,
                  color: activeTab === tab ? 'var(--cp-primary, #6366f1)' : 'var(--cp-text-secondary, #64748b)',
                  borderBottom: activeTab === tab ? '2px solid var(--cp-primary, #6366f1)' : '2px solid transparent',
                  transition: 'background 0.15s',
                }}
              >
                {TAB_LABELS[tab]}
              </button>
            ))}
          </div>

          {/* Tab Content */}
          <div style={{ flex: 1, overflow: 'hidden' }}>
            {activeTab === 'chat' && (
              <div style={{
                display: 'flex',
                flexDirection: 'column',
                height: '100%',
              }}>
                {/* Messages */}
                <div style={{
                  flex: 1,
                  overflowY: 'auto',
                  padding: '16px',
                }}>
                  {error && (
                    <div style={{
                      padding: '10px 14px',
                      background: '#fef2f2',
                      color: '#dc2626',
                      borderRadius: '8px',
                      fontSize: '13px',
                      marginBottom: '12px',
                    }}>
                      {error}
                    </div>
                  )}
                  {messages.map(msg => (
                    <CopilotMessageBubble
                      key={msg.id}
                      message={msg}
                      copilotColorMap={copilotColorMap}
                      onSuggestionClick={handleSuggestionClick}
                    />
                  ))}
                  {loading && (
                    <div style={{
                      textAlign: 'center',
                      padding: '16px',
                      color: 'var(--cp-text-secondary, #64748b)',
                      fontSize: '13px',
                    }}>
                      Thinking...
                    </div>
                  )}
                  {messages.length === 0 && !loading && (
                    <div style={{
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      height: '100%',
                      color: 'var(--cp-text-secondary, #64748b)',
                      fontSize: '14px',
                    }}>
                      Start a conversation with {activeCopilot?.name ?? 'a copilot'}
                    </div>
                  )}
                </div>

                {/* Input Area */}
                <div style={{
                  padding: '12px 16px',
                  borderTop: '1px solid var(--cp-border, #e2e8f0)',
                  background: 'var(--cp-surface, #fff)',
                }}>
                  <div style={{ display: 'flex', gap: '8px', marginBottom: '8px' }}>
                    <input
                      type="text"
                      value={inputValue}
                      onChange={e => setInputValue(e.target.value)}
                      onKeyDown={handleInputKeyDown}
                      placeholder={`Message ${activeCopilot?.name ?? 'copilot'}...`}
                      style={{
                        flex: 1,
                        padding: '10px 14px',
                        borderRadius: '8px',
                        border: '1px solid var(--cp-border, #e2e8f0)',
                        fontSize: '14px',
                        outline: 'none',
                        background: 'var(--cp-surface-2, #f8fafc)',
                        color: 'var(--cp-text, #1e293b)',
                      }}
                    />
                    <button
                      onClick={handleSend}
                      disabled={loading || !inputValue.trim()}
                      style={{
                        padding: '10px 18px',
                        borderRadius: '8px',
                        border: 'none',
                        background: loading || !inputValue.trim() ? 'var(--cp-border, #e2e8f0)' : 'var(--cp-primary, #6366f1)',
                        color: '#fff',
                        fontSize: '14px',
                        cursor: loading || !inputValue.trim() ? 'not-allowed' : 'pointer',
                      }}
                    >
                      Send
                    </button>
                  </div>
                  <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
                    <label style={{
                      display: 'flex',
                      alignItems: 'center',
                      gap: '4px',
                      fontSize: '12px',
                      color: 'var(--cp-text-secondary, #64748b)',
                      cursor: 'pointer',
                    }}>
                      <input
                        type="checkbox"
                        checked={streamMode}
                        onChange={e => setStreamMode(e.target.checked)}
                      />
                      Stream
                    </label>
                    <span style={{
                      fontSize: '12px',
                      color: 'var(--cp-text-secondary, #64748b)',
                    }}>
                      Active: {activeCopilot?.name ?? 'None'}
                    </span>
                  </div>
                </div>
              </div>
            )}

            {activeTab === 'context' && (
              <SharedContextPanel
                context={context}
                loading={loading}
                onRefresh={getContext}
                style={{ width: '100%', border: 'none' }}
              />
            )}

            {activeTab === 'analytics' && (
              <WorkspaceAnalyticsPanel
                analytics={{
                  conversationCountToday: messages.length,
                  conversationCountWeek: messages.length,
                  activeSessions: copilots.reduce((a, c) => a + c.activeSessions, 0),
                  copilotUsage: copilots.map(c => ({
                    copilotId: c.copilotId,
                    copilotName: c.name,
                    messageCount: messages.filter(m => m.copilotId === c.copilotId).length,
                  })),
                  avgLatencyMs: copilots.reduce((a, c) => a + c.avgLatencyMs, 0) / Math.max(copilots.length, 1),
                  handoffCount: messages.filter(m => m.handoff).length,
                  topIntents: [],
                }}
                loading={loading}
              />
            )}

            {activeTab === 'activity' && (
              <ActivityTimeline events={timelineEvents} />
            )}
          </div>
        </div>

        {/* Shared Context Panel */}
        {contextPanelOpen && (
          <SharedContextPanel
            context={context}
            loading={loading}
            onRefresh={getContext}
          />
        )}
      </div>

      {/* Floating Assistant */}
      {floatingAssistantOpen && (
        <FloatingAssistant
          copilots={copilots}
          activeCopilotId={activeCopilotId}
          onSendMessage={(msg) => sendMessage(msg)}
          unreadCount={0}
        />
      )}
    </div>
  );
}
