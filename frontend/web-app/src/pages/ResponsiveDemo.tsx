import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

type Viewport = 'xs' | 'sm' | 'md' | 'lg' | 'xl' | '2xl';

const VIEWPORTS: { key: Viewport; label: string; width: string; desc: string }[] = [
  { key: 'xs', label: 'Mobile', width: '375px', desc: 'Phone portrait' },
  { key: 'sm', label: 'Mobile Large', width: '640px', desc: 'Phone landscape' },
  { key: 'md', label: 'Tablet', width: '768px', desc: 'Tablet portrait' },
  { key: 'lg', label: 'Laptop', width: '1024px', desc: 'Laptop' },
  { key: 'xl', label: 'Desktop', width: '1280px', desc: 'Desktop' },
  { key: '2xl', label: 'Wide', width: '1536px', desc: 'Large desktop' },
];

export default function ResponsiveDemo() {
  const [active, setActive] = useState<Viewport>('xl');
  const [showGrid, setShowGrid] = useState(false);

  useEffect(() => {
    const container = document.querySelector('.sk-responsive-frame');
    if (container) (container as HTMLElement).style.width = VIEWPORTS.find(v => v.key === active)!.width;
  }, [active]);

  return (
    <div className="sk-content__header">
      <div className="sk-content__title-row">
        <div>
          <h1>Responsive Layout Demo</h1>
          <p className="sk-content__subtitle">Test layout adaptation across breakpoints. Toggle grid overlay to verify alignment.</p>
        </div>
      </div>

      <div className="sk-demo-toolbar" role="toolbar" aria-label="Responsive demo controls">
        <div className="sk-viewport-tabs" role="tablist">
          {VIEWPORTS.map(v => (
            <button
              key={v.key}
              role="tab"
              aria-selected={active === v.key}
              aria-label={v.label}
              className={`sk-tab ${active === v.key ? 'sk-tab--active' : ''}`}
              onClick={() => setActive(v.key)}
            >
              {v.label}
            </button>
          ))}
        </div>
        <label className="sk-toggle">
          <input type="checkbox" checked={showGrid} onChange={e => setShowGrid(e.target.checked)} />
          <span>Grid overlay</span>
        </label>
        <Link to="/demo" className="sk-secondary-action">← Back</Link>
      </div>

      <div className={`sk-responsive-frame ${showGrid ? 'sk-responsive-frame--grid' : ''}`} role="region" aria-label={`Viewport: ${VIEWPORTS.find(v => v.key === active)?.label}`}>
        <iframe
          src="/demo/responsive/inner"
          title={`Responsive preview at ${VIEWPORTS.find(v => v.key === active)?.label}`}
          sandbox="allow-scripts allow-same-origin"
          style={{ width: '100%', height: '600px', border: 'none' }}
        />
      </div>

      <section className="sk-panel" style={{ marginTop: '24px' }}>
        <h2>Breakpoint Specifications</h2>
        <table className="sk-table">
          <thead>
            <tr>
              <th>Name</th><th>Width</th><th>Sidebar</th><th>Header Search</th><th>Utility Panel</th><th>Content Max</th>
            </tr>
          </thead>
<tbody>
              <tr><td>xs (Mobile)</td><td>{'<' + ' '}640px</td><td>Drawer (280px)</td><td>Hidden (palette only)</td><td>Bottom sheet</td><td>100%</td></tr>
            <tr><td>sm (Mobile Large)</td><td>640–767px</td><td>Drawer (280px)</td><td>Hidden (palette only)</td><td>Bottom sheet</td><td>100%</td></tr>
            <tr><td>md (Tablet)</td><td>768–1023px</td><td>Icon rail (72px), hover expands</td><td>Hidden (palette only)</td><td>Bottom sheet</td><td>100%</td></tr>
            <tr><td>lg (Laptop)</td><td>1024–1279px</td><td>Icon rail (72px), hover expands</td><td>Hidden (palette only)</td><td>Docked right</td><td>1200px</td></tr>
            <tr><td>xl (Desktop)</td><td>1280–1535px</td><td>Fixed 264px</td><td>Centered 420px</td><td>Docked right</td><td>1200px</td></tr>
            <tr><td>2xl (Wide)</td><td>≥ 1536px</td><td>Fixed 264px</td><td>Centered 420px</td><td>Docked right</td><td>1400px</td></tr>
          </tbody>
        </table>
      </section>
    </div>
  );
}