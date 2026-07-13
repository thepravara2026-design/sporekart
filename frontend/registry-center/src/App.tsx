import { Routes, Route, Navigate } from 'react-router-dom';
import Layout from './components/Layout';
import ProviderRegistry from './pages/ProviderRegistry';
import PromptRegistry from './pages/PromptRegistry';
import KnowledgeRegistry from './pages/KnowledgeRegistry';
import UsageDashboard from './pages/UsageDashboard';
import ConfigCenter from './pages/ConfigCenter';
import EventCatalog from './pages/EventCatalog';
import ApiRegistry from './pages/ApiRegistry';
import CapabilityExplorer from './pages/CapabilityExplorer';
import AdrViewer from './pages/AdrViewer';

export default function App() {
  return (
    <Layout>
      <Routes>
        <Route path="/" element={<Navigate to="/provider-registry" replace />} />
        <Route path="/provider-registry" element={<ProviderRegistry />} />
        <Route path="/prompt-registry" element={<PromptRegistry />} />
        <Route path="/knowledge-registry" element={<KnowledgeRegistry />} />
        <Route path="/usage-tracking" element={<UsageDashboard />} />
        <Route path="/config-registry" element={<ConfigCenter />} />
        <Route path="/event-catalog" element={<EventCatalog />} />
        <Route path="/api-registry" element={<ApiRegistry />} />
        <Route path="/capability-discovery" element={<CapabilityExplorer />} />
        <Route path="/adr" element={<AdrViewer />} />
        <Route path="*" element={<Navigate to="/provider-registry" replace />} />
      </Routes>
    </Layout>
  );
}
