import { useState } from 'react'
import RiskDashboard from './components/RiskDashboard'
import TrustDashboard from './components/TrustDashboard'
import ConfidenceDashboard from './components/ConfidenceDashboard'
import RiskTimeline from './components/RiskTimeline'
import RecommendationViewer from './components/RecommendationViewer'

const tabs = [
  { key: 'dashboard', label: 'Risk Dashboard' },
  { key: 'trust', label: 'Trust' },
  { key: 'confidence', label: 'Confidence' },
  { key: 'timeline', label: 'Risk Timeline' },
  { key: 'recommendations', label: 'Recommendations' },
] as const

type TabKey = typeof tabs[number]['key']

function App() {
  const [activeTab, setActiveTab] = useState<TabKey>('dashboard')

  const renderContent = () => {
    switch (activeTab) {
      case 'dashboard':
        return <RiskDashboard />
      case 'trust':
        return <TrustDashboard />
      case 'confidence':
        return <ConfidenceDashboard />
      case 'timeline':
        return <RiskTimeline />
      case 'recommendations':
        return <RecommendationViewer />
    }
  }

  return (
    <div className="app-container">
      <aside className="sidebar">
        <div className="sidebar-header">
          <h1>Risk</h1>
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
