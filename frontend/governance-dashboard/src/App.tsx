import { useState } from 'react'
import ExecutiveDashboard from './components/ExecutiveDashboard'
import MetricsView from './components/MetricsView'
import KPIDashboard from './components/KPIDashboard'
import ReportsView from './components/ReportsView'
import RiskDistributionView from './components/RiskDistributionView'
import ComplianceStatusView from './components/ComplianceStatusView'

const tabs = [
  { key: 'executive', label: 'Executive Dashboard' },
  { key: 'metrics', label: 'Governance Metrics' },
  { key: 'kpis', label: 'KPI Dashboard' },
  { key: 'reports', label: 'Reports' },
  { key: 'risk', label: 'Risk Dashboard' },
  { key: 'compliance', label: 'Compliance Dashboard' },
] as const

type TabKey = typeof tabs[number]['key']

function App() {
  const [activeTab, setActiveTab] = useState<TabKey>('executive')

  const renderContent = () => {
    switch (activeTab) {
      case 'executive':
        return <ExecutiveDashboard />
      case 'metrics':
        return <MetricsView />
      case 'kpis':
        return <KPIDashboard />
      case 'reports':
        return <ReportsView />
      case 'risk':
        return <RiskDistributionView />
      case 'compliance':
        return <ComplianceStatusView />
    }
  }

  return (
    <div className="app-container">
      <aside className="sidebar">
        <div className="sidebar-header">
          <h1>Governance</h1>
        </div>
        <nav className="sidebar-nav">
          {tabs.map((tab) => (
            <button
              key={tab.key}
              className={`nav-item ${activeTab === tab.key ? 'active' : ''}`}
              onClick={() => setActiveTab(tab.key)}
            >
              {tab.label}
            </button>
          ))}
        </nav>
      </aside>
      <main className="main-content">
        {renderContent()}
      </main>
    </div>
  )
}

export default App
