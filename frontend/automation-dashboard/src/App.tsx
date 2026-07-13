import { useState } from 'react'
import AutomationDashboard from './components/AutomationDashboard'
import WorkflowMonitor from './components/WorkflowMonitor'
import JobQueue from './components/JobQueue'
import SchedulerConsole from './components/SchedulerConsole'
import LifecycleViewer from './components/LifecycleViewer'
import ExecutionHistory from './components/ExecutionHistory'

const tabs = [
  { id: 'dashboard', label: 'Automation Dashboard', component: AutomationDashboard },
  { id: 'workflows', label: 'Workflow Monitor', component: WorkflowMonitor },
  { id: 'jobs', label: 'Job Queue', component: JobQueue },
  { id: 'scheduler', label: 'Scheduler', component: SchedulerConsole },
  { id: 'lifecycle', label: 'Lifecycle Viewer', component: LifecycleViewer },
  { id: 'history', label: 'Execution History', component: ExecutionHistory },
]

function App() {
  const [activeTab, setActiveTab] = useState('dashboard')
  const ActiveComponent = tabs.find(t => t.id === activeTab)!.component

  return (
    <div className="app-container">
      <nav className="sidebar">
        <div className="sidebar-header">
          <h2>Automation</h2>
        </div>
        <ul className="nav-list">
          {tabs.map(tab => (
            <li key={tab.id}>
              <button
                className={`nav-item ${activeTab === tab.id ? 'active' : ''}`}
                onClick={() => setActiveTab(tab.id)}
              >
                {tab.label}
              </button>
            </li>
          ))}
        </ul>
      </nav>
      <main className="main-content">
        <ActiveComponent />
      </main>
    </div>
  )
}

export default App
