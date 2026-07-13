import { useState } from 'react'
import ApprovalInbox from './components/ApprovalInbox'
import PendingApprovals from './components/PendingApprovals'
import ApprovalHistory from './components/ApprovalHistory'
import ApprovalMetrics from './components/ApprovalMetrics'
import ApprovalDetails from './components/ApprovalDetails'
import ReviewerTimeline from './components/ReviewerTimeline'

type Tab = 'inbox' | 'pending' | 'history' | 'statistics'

interface NavItem {
  id: Tab
  label: string
}

const navItems: NavItem[] = [
  { id: 'inbox', label: 'Approval Inbox' },
  { id: 'pending', label: 'Pending Approvals' },
  { id: 'history', label: 'Approval History' },
  { id: 'statistics', label: 'Statistics' },
]

function App() {
  const [activeTab, setActiveTab] = useState<Tab>('inbox')
  const [selectedId, setSelectedId] = useState<string | null>(null)

  const renderContent = () => {
    if (selectedId) {
      return (
        <div>
          <button className="back-button" onClick={() => setSelectedId(null)}>
            &larr; Back
          </button>
          <ApprovalDetails id={selectedId} />
          <ReviewerTimeline id={selectedId} />
        </div>
      )
    }
    switch (activeTab) {
      case 'inbox':
        return <ApprovalInbox onSelect={setSelectedId} />
      case 'pending':
        return <PendingApprovals onSelect={setSelectedId} />
      case 'history':
        return <ApprovalHistory onSelect={setSelectedId} />
      case 'statistics':
        return <ApprovalMetrics />
    }
  }

  return (
    <div className="app-container">
      <nav className="sidebar">
        <div className="sidebar-header">
          <h2>Approvals</h2>
        </div>
        <ul className="nav-list">
          {navItems.map((item) => (
            <li key={item.id}>
              <button
                className={`nav-button ${activeTab === item.id ? 'active' : ''}`}
                onClick={() => { setActiveTab(item.id); setSelectedId(null) }}
              >
                {item.label}
              </button>
            </li>
          ))}
        </ul>
      </nav>
      <main className="main-content">
        <header className="content-header">
          <h1>{navItems.find((n) => n.id === activeTab)?.label}</h1>
        </header>
        <div className="content-body">
          {renderContent()}
        </div>
      </main>
    </div>
  )
}

export default App
