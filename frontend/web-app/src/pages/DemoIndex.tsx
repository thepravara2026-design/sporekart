import { Link } from 'react-router-dom';

export default function DemoIndex() {
  const demos = [
    { path: '/demo/responsive', label: 'Responsive Layout', desc: 'Viewport toggle, grid overlay, sidebar drawer/rail behavior', icon: '📱' },
    { path: '/demo/keyboard', label: 'Keyboard Navigation', desc: 'Focus order visualizer, component patterns, shortcut cheat sheet', icon: '⌨️' },
    { path: '/demo/loading', label: 'Loading Experience', desc: 'Route skeletons, section skeletons, action loaders, offline, retry', icon: '⏳' },
    { path: '/demo/errors', label: 'Error Pages', desc: '404, 403, 401, 500, network, timeout, validation, conflict', icon: '⚠️' },
    { path: '/demo/empty', label: 'Empty States', desc: 'All 30+ empty states with standard/filtered/permission/error variants', icon: '📭' },
    { path: '/demo/forms', label: 'Form Patterns', desc: 'Validation, OTP, address, checkout steps, auto-save, file upload', icon: '📝' },
    { path: '/demo/microcopy', label: 'Microcopy Gallery', desc: 'Buttons, toasts, labels, errors, empty states, tooltips, loading', icon: '💬' },
  ];

  return (
    <div className="sk-content__header">
      <div className="sk-content__title-row">
        <div>
          <h1>UX Standards Demos</h1>
          <p className="sk-content__subtitle">Part 1C — Interactive demonstrations of all UX standards</p>
        </div>
      </div>
      <section className="sk-panel" aria-label="Demo gallery">
        <div className="sk-demo-grid">
          {demos.map((d) => (
            <article key={d.path} className="sk-demo-card">
              <div className="sk-demo-card__icon" aria-hidden="true">{d.icon}</div>
              <h3>{d.label}</h3>
              <p className="sk-demo-card__desc">{d.desc}</p>
              <Link to={d.path} className="sk-primary-action sk-demo-card__cta">
                Open
              </Link>
            </article>
          ))}
        </div>
      </section>
    </div>
  );
}