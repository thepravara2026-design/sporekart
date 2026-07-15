import React from 'react';
import { PermissionGate } from '../../../permissions/PermissionGate';
import { FeatureFlagProvider } from '../../../feature-flags/FeatureFlagProvider';
import { PermissionProvider } from '../../../permissions/PermissionProvider';
import { useSeoState } from './state/useSeoState';
import { SeoNav } from './state/SeoNav';
import { SeoToolbar } from './components/SeoToolbar';
import { SeoDashboard } from './components/SeoDashboard';
import { SeoMetaManager } from './components/SeoMetaManager';
import { SeoUrlManager } from './components/SeoUrlManager';
import { SearchPreview } from './components/SearchPreview';
import { StructuredDataBuilder } from './components/StructuredDataBuilder';
import { PublishingManager } from './components/PublishingManager';
import { MarketplaceReadiness } from './components/MarketplaceReadiness';
import { AiReadinessDashboard } from './components/AiReadinessDashboard';
import { ContentValidation } from './components/ContentValidation';
import { SocialPreview } from './components/SocialPreview';
import type { SocialProfileLink, SeoEntry } from './types';
import { CURRENT_SEO_ROLE } from './types';
import './Seo.css';

function SeoSettings() {
  return (
    <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
      <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>SEO Settings</h3>
      <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
        SEO settings are in Mock Mode. Future settings will include default meta templates, auto-generate slugs, sitemap configuration, and bulk SEO operations.
      </p>
      <div style={{ marginTop: 12, display: 'flex', flexDirection: 'column', gap: 8 }}>
        <div style={settRow}><span>Auto-Generate Slugs</span><span style={{ fontWeight: 'var(--weight-semibold)' }}>Enabled</span></div>
        <div style={settRow}><span>Default Meta Template</span><span style={{ fontWeight: 'var(--weight-semibold)' }}>Product Name — SporeKart</span></div>
        <div style={settRow}><span>Sitemap Auto-Update</span><span style={{ fontWeight: 'var(--weight-semibold)' }}>Daily (Future)</span></div>
        <div style={settRow}><span>Canonical URL Policy</span><span style={{ fontWeight: 'var(--weight-semibold)' }}>Self-referencing</span></div>
        <div style={settRow}><span>Active Role (Mock)</span><span style={{ fontWeight: 'var(--weight-semibold)' }}>{CURRENT_SEO_ROLE}</span></div>
      </div>
    </div>
  );
}

function SeoHelp() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-md)' }}>
      <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
        <h3 style={{ margin: '0 0 8px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>About the SEO Workspace</h3>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 'var(--leading-relaxed)' }}>
          The SEO & Marketplace workspace manages search engine optimization, social previews, structured data, publishing workflow, marketplace readiness, and content validation for SporeKart products. This module operates entirely in Mock Mode.
        </p>
      </div>
      <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
        <h3 style={{ margin: '0 0 8px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>Key Concepts</h3>
        <ul style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 'var(--leading-relaxed)' }}>
          <li><strong>Meta Manager</strong> — Set and optimize title, description, keywords per product</li>
          <li><strong>URL Manager</strong> — Manage slugs and canonical URLs</li>
          <li><strong>Social Previews</strong> — Facebook, Twitter, and LinkedIn card previews</li>
          <li><strong>Search Previews</strong> — Google, Bing, AI assistant, and featured snippet views</li>
          <li><strong>Structured Data</strong> — JSON-LD schema builder (12 types)</li>
          <li><strong>Publishing</strong> — Status workflow from draft to published</li>
          <li><strong>Marketplace</strong> — Readiness for 6 sales channels</li>
        </ul>
      </div>
    </div>
  );
}

function SeoSocial({ profiles, selectedEntry, activity }: { profiles: SocialProfileLink[]; selectedEntry: SeoEntry | null; activity: Array<{ id: string; platform: string; action: string; user: string; timestamp: string }> }) {
  const grid: React.CSSProperties = { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 12 };
  const card: React.CSSProperties = { padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', gap: 8 };
  const ev: React.CSSProperties = { display: 'flex', gap: 12, padding: '10px 16px', borderBottom: '1px solid var(--color-border)', alignItems: 'center', fontSize: 'var(--text-body-xs)' };
  return (
    <div style={{ padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Social Media</h2>
      <div style={grid}>
        {profiles.map((p) => (
          <div key={p.platform} style={{ ...card, opacity: p.enabled ? 1 : 0.5 }}>
            <div style={{ fontSize: 24 }}>{p.icon}</div>
            <div style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{p.label}</div>
            <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', wordBreak: 'break-all' }}>{p.url}</div>
            <div style={{ fontSize: 'var(--text-body-xs)', color: p.enabled ? 'var(--color-accent-green)' : 'var(--color-accent-orange)', fontWeight: 600 }}>
              {p.enabled ? 'Connected' : 'Not Connected'}
            </div>
          </div>
        ))}
      </div>
      {selectedEntry && (
        <SocialPreview entry={selectedEntry} />
      )}
      <h3 style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginTop: 8 }}>Social Activity</h3>
      <div style={{ borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', overflow: 'hidden' }}>
        {activity.map((a) => (
          <div key={a.id} style={ev}>
            <span style={{ width: 10, height: 10, borderRadius: '50%', background: 'var(--color-accent-purple)', flexShrink: 0 }} />
            <div style={{ flex: 1 }}><span style={{ fontWeight: 500 }}>{a.platform}</span><span style={{ color: 'var(--color-text-tertiary)', marginLeft: 4 }}>{a.action}</span></div>
            <span style={{ color: 'var(--color-text-tertiary)' }}>{a.user}</span>
            <span style={{ color: 'var(--color-text-tertiary)', fontSize: 11 }}>{new Date(a.timestamp).toLocaleDateString()}</span>
          </div>
        ))}
      </div>
    </div>
  );
}

function SeoHistory({ activity }: { activity: Array<{ id: string; type: string; message: string; entityName: string; user: string; timestamp: string; icon: string }> }) {
  const ev: React.CSSProperties = { display: 'flex', gap: 12, padding: '10px 16px', borderBottom: '1px solid var(--color-border)', alignItems: 'center', fontSize: 'var(--text-body-xs)' };
  const dot: Record<string, string> = { search: 'var(--color-accent-blue)', 'file-text': 'var(--color-accent-purple)', code: 'var(--color-accent-green)', link: 'var(--color-accent-orange)', send: 'var(--color-accent-cyan)', 'trending-up': 'var(--color-accent-green)', 'shopping-cart': 'var(--color-accent-yellow)', archive: 'var(--color-accent-red)', cpu: 'var(--color-accent-blue)', calendar: 'var(--color-accent-purple)' };
  return (
    <div style={{ padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Activity History</h2>
      <div style={{ borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', overflow: 'hidden' }}>
        {activity.slice(0, 20).map((a) => (
          <div key={a.id} style={ev}>
            <span style={{ width: 10, height: 10, borderRadius: '50%', background: dot[a.icon] || 'var(--color-text-tertiary)', flexShrink: 0 }} />
            <div style={{ flex: 1 }}><span style={{ fontWeight: 500 }}>{a.entityName}</span><span style={{ color: 'var(--color-text-tertiary)', marginLeft: 4 }}>{a.message}</span></div>
            <span style={{ color: 'var(--color-text-tertiary)' }}>{a.user}</span>
            <span style={{ color: 'var(--color-text-tertiary)', fontSize: 11 }}>{new Date(a.timestamp).toLocaleDateString()}</span>
          </div>
        ))}
      </div>
    </div>
  );
}

export const SeoPage: React.FC = () => {
  const state = useSeoState();

  const renderSection = () => {
    switch (state.section) {
      case 'overview':
        return <SeoDashboard />;
      case 'seo':
        return <SeoDashboard />;
      case 'meta':
        return <SeoMetaManager entries={state.filteredEntries} selectedId={state.selectedId} onSelect={state.toggleSelect} />;
      case 'structured-data':
        return <StructuredDataBuilder entry={state.selectedEntry} examples={state.structuredDataExamples} />;
      case 'urls':
        return <SeoUrlManager entries={state.filteredEntries} />;
      case 'publishing':
        return <PublishingManager />;
      case 'marketplace':
        return <MarketplaceReadiness entry={state.selectedEntry} configs={state.marketplaceConfigs} />;
      case 'search-preview':
        return <SearchPreview entry={state.selectedEntry} />;
      case 'social':
        return <SeoSocial profiles={state.socialProfiles} selectedEntry={state.selectedEntry} activity={state.socialActivity} />;
      case 'ai-readiness':
        return <AiReadinessDashboard ai={state.aiReadiness} />;
      case 'validation':
        return <ContentValidation entries={state.filteredEntries} />;
      case 'history':
        return <SeoHistory activity={state.activity} />;
      case 'settings':
        return <SeoSettings />;
      case 'help':
        return <SeoHelp />;
      default:
        return <SeoDashboard />;
    }
  };

  return (
    <PermissionProvider initialRole="manager">
      <FeatureFlagProvider>
        <PermissionGate action="view" resource="seo">
          <div className="sk-seo-page">
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <div>
                <h1 style={{ margin: 0, fontSize: 'var(--text-h1)', color: 'var(--color-text-primary)' }}>SEO & Marketplace</h1>
                <p style={{ margin: '4px 0 0', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>
                  Manage search optimization, social previews, publishing, and marketplace readiness
                </p>
              </div>
            </div>

            <SeoToolbar
              search={state.search}
              onSearchChange={state.setSearch}
              sort={state.sort}
              onSortChange={state.setSort}
              activeFilterCount={state.activeFilterCount}
              onClearFilters={state.clearAllFilters}
            />

            <div className="sk-seo-grid">
              <aside className="sk-seo-sidebar">
                <SeoNav section={state.section} onSectionChange={state.setSection} />
              </aside>
              <main className="sk-seo-content">
                {renderSection()}
              </main>
            </div>

            <footer style={{ marginTop: 'var(--space-section-gap)', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', borderTop: '1px solid var(--color-border)', paddingTop: 'var(--space-component-gap)', display: 'flex', justifyContent: 'space-between' }}>
              <span>Mock Mode — no persistence. Role: {CURRENT_SEO_ROLE}</span>
              <span>SEO & Marketplace · Sprint 24 Part 9</span>
            </footer>
          </div>
        </PermissionGate>
      </FeatureFlagProvider>
    </PermissionProvider>
  );
};

const settRow: React.CSSProperties = { display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', padding: '4px 0', borderBottom: '1px solid var(--color-border-weak)' };

export default SeoPage;
