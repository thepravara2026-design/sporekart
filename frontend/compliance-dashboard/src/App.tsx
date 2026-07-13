import { useState } from 'react'
import ComplianceDashboard from './components/ComplianceDashboard'
import FrameworkRegistry from './components/FrameworkRegistry'
import ReportsList from './components/ReportsList'
import ViolationViewer from './components/ViolationViewer'
import ExceptionManagement from './components/ExceptionManagement'

const tabs = [
  { key: 'dashboard', label: 'Dashboard' },
  { key: 'frameworks', label: 'Frameworks' },
  { key: 'reports', label: 'Reports' },
  { key: 'violations', label: 'Violations' },
  { key: 'exceptions', label: 'Exceptions' },
] as const

type TabKey = typeof tabs[number]['key']

function App() {
  const [activeTab, setActiveTab] = useState<TabKey>('dashboard')

  const renderContent = () => {
    switch (activeTab) {
      case 'dashboard':
        return <ComplianceDashboard />
      case 'frameworks':
        return <FrameworkRegistry />
      case 'reports':
        return <ReportsList />
      case 'violations':
        return <ViolationViewer />
      case 'exceptions':
        return <ExceptionManagement />
    }
  }

  return (
    <div className="app-container">
      <aside className="sidebar">
        <div className="sidebar-header">
          <h1>Compliance</h1>
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
