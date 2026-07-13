import { Routes, Route, NavLink } from 'react-router-dom';
import AssistantDashboard from './components/AssistantDashboard';
import ConversationPanel from './components/ConversationPanel';
import TaskTimeline from './components/TaskTimeline';
import ConversationHistory from './components/ConversationHistory';

function App() {
  return (
    <div className="app-layout">
      <aside className="sidebar">
        <div className="sidebar-header">
          <h2>Assistant Hub</h2>
        </div>
        <nav className="sidebar-nav">
          <NavLink to="/" end className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}>
            Dashboard
          </NavLink>
          <NavLink to="/chat" className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}>
            Chat
          </NavLink>
          <NavLink to="/tasks" className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}>
            Tasks
          </NavLink>
          <NavLink to="/history" className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}>
            History
          </NavLink>
        </nav>
      </aside>
      <main className="main-content">
        <Routes>
          <Route path="/" element={<AssistantDashboard />} />
          <Route path="/chat" element={<ConversationPanel />} />
          <Route path="/tasks" element={<TaskTimeline />} />
          <Route path="/history" element={<ConversationHistory />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;
