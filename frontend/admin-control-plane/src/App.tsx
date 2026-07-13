import { useState } from 'react'
import AdminDashboard from './components/AdminDashboard'
import ConfigurationCenter from './components/ConfigurationCenter'
import FeatureFlagManager from './components/FeatureFlagManager'
import ModuleManager from './components/ModuleManager'
import EnvironmentManager from './components/EnvironmentManager'
import VersionHistory from './components/VersionHistory'
import AuditViewer from './components/AuditViewer'

const tabs = [
  { id: 'dashboard', label: 'Dashboard' },
  { id: 'configuration', label: 'Configuration' },
  { id: 'feature-flags', label: 'Feature Flags' },
  { id: 'modules', label: 'Modules' },
  { id: 'environments', label: 'Environments' },
  { id: 'version-history', label: 'Version History' },
  { id: 'audit', label: 'Audit Log' },
]

function App() {
  const [activeTab, setActiveTab] = useState('dashboard')

  const renderContent = () => {
    switch (activeTab) {
      case 'dashboard':
        return <AdminDashboard />
      case 'configuration':
        return <ConfigurationCenter />
      case 'feature-flags':
        return <FeatureFlagManager />
      case 'modules':
        return <ModuleManager />
      case 'environments':
        return <EnvironmentManager />
      case 'version-history':
        return <VersionHistory />
      case 'audit':
        return <AuditViewer />
      default:
        return <AdminDashboard />
    }
  }

  return (
    <div className="app-container">
      <aside className="sidebar">
        <div className="sidebar-header">
          <h2>AdminCP</h2>
        </div>
        <nav className="sidebar-nav">
          {tabs.map((tab) => (
            <button
              key={tab.id}
              className={`nav-item ${activeTab === tab.id ? 'active' : ''}`}
              onClick={() => setActiveTab(tab.id)}
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
