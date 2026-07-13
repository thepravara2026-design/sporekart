import { useState } from 'react';
import { Assistant, Recommendation, WorkflowInvocation, ExecutionLog, UsageStats } from '../types';
import AssistantSelector from './AssistantSelector';
import RecommendationPanel from './RecommendationPanel';
import WorkflowStatus from './WorkflowStatus';
import ExecutionLogs from './ExecutionLogs';

const ASSISTANTS: Assistant[] = [
  { id: '1', name: 'Sales Copilot', description: 'Analyzes sales data and forecasts trends', status: 'Active', usageCount: 142, avgLatency: 320, successRate: 97 },
  { id: '2', name: 'Report Copilot', description: 'Generates and schedules custom reports', status: 'Active', usageCount: 89, avgLatency: 540, successRate: 94 },
  { id: '3', name: 'Sync Copilot', description: 'Manages data synchronization across systems', status: 'Active', usageCount: 67, avgLatency: 280, successRate: 99 },
  { id: '4', name: 'Alert Copilot', description: 'Monitors and triggers intelligent alerts', status: 'Inactive', usageCount: 34, avgLatency: 150, successRate: 100 },
];

const RECOMMENDATIONS: Recommendation[] = [
  { id: '1', title: 'Optimize Query Performance', description: 'Sales Copilot suggests indexing the orders table to speed up reports.', copilotName: 'Sales Copilot', actionable: true },
  { id: '2', title: 'Schedule Weekly Digest', description: 'Report Copilot can auto-generate a weekly summary every Monday.', copilotName: 'Report Copilot', actionable: true },
];

const WORKFLOWS: WorkflowInvocation[] = [
  { id: '1', name: 'Monthly Sales Report', status: 'Completed', triggeredBy: 'Sales Copilot', result: 'Report delivered', startedAt: new Date().toISOString() },
  { id: '2', name: 'CRM Sync', status: 'Running', triggeredBy: 'Sync Copilot', startedAt: new Date().toISOString() },
];

const LOGS: ExecutionLog[] = [
  { id: '1', action: 'analyze_sales', copilot: 'Sales Copilot', latency: 312, status: 'Success', timestamp: new Date().toISOString() },
  { id: '2', action: 'generate_report', copilot: 'Report Copilot', latency: 0, status: 'Failed', timestamp: new Date().toISOString() },
  { id: '3', action: 'sync_crm', copilot: 'Sync Copilot', latency: 245, status: 'Success', timestamp: new Date().toISOString() },
];

const USAGE: UsageStats = {
  totalConversations: 332,
  totalTasks: 1047,
  avgResponseTime: 342,
  successRate: 96,
};

function AssistantDashboard() {
  const [selected, setSelected] = useState<string | undefined>('1');
  const [recs, setRecs] = useState(RECOMMENDATIONS);

  const handleAction = (id: string) => {
    console.log('Apply recommendation:', id);
  };

  const handleDismiss = (id: string) => {
    setRecs(recs.filter((r) => r.id !== id));
  };

  return (
    <div className="dashboard-page">
      <h2>Assistant Dashboard</h2>

      <div className="usage-summary">
        <div className="stat-card">
          <span className="stat-value">{USAGE.totalConversations}</span>
          <span className="stat-label">Conversations</span>
        </div>
        <div className="stat-card">
          <span className="stat-value">{USAGE.totalTasks}</span>
          <span className="stat-label">Tasks</span>
        </div>
        <div className="stat-card">
          <span className="stat-value">{USAGE.avgResponseTime}ms</span>
          <span className="stat-label">Avg Response</span>
        </div>
        <div className="stat-card">
          <span className="stat-value">{USAGE.successRate}%</span>
          <span className="stat-label">Success Rate</span>
        </div>
      </div>

      <AssistantSelector
        assistants={ASSISTANTS}
        selectedId={selected}
        onSelect={setSelected}
      />

      <div className="dashboard-panels">
        <RecommendationPanel
          recommendations={recs}
          onAction={handleAction}
          onDismiss={handleDismiss}
        />
        <WorkflowStatus workflows={WORKFLOWS} />
      </div>

      <ExecutionLogs logs={LOGS} />
    </div>
  );
}

export default AssistantDashboard;
