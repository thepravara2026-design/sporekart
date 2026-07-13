import { useLocation } from 'react-router-dom';
import { useApp } from '../../context';
import { ALL_ROLES, ROLE_LABELS, type Role } from '../../config/roles';
import { buildBreadcrumb } from '../../config/navigation';

export default function Header({ onToggleSidebar }: { onToggleSidebar: () => void }) {
  const { activeRole, setActiveRole, setPaletteOpen } = useApp();
  const location = useLocation();
  const { workspace } = buildBreadcrumb(location.pathname);

  return (
    <header className="sk-header" role="banner">
      <div className="sk-header__left">
        <button
          className="sk-icon-btn sk-header__menu"
          aria-label="Toggle navigation"
          onClick={onToggleSidebar}
        >
          ☰
        </button>
        <a className="sk-brand" href="/" aria-label="SporeKart home">
          <span className="sk-brand__mark" aria-hidden="true">❖</span>
          <span className="sk-brand__name">SporeKart</span>
        </a>
        {workspace && (
          <span className="sk-header__workspace" aria-label="Current workspace">
            <span aria-hidden="true">{workspace.icon}</span> {workspace.label}
          </span>
        )}
      </div>

      <div className="sk-header__center">
        <button
          className="sk-search-trigger"
          onClick={() => setPaletteOpen(true)}
          aria-label="Open search and command palette"
        >
          <span aria-hidden="true">🔍</span> Search or jump to…
          <kbd>⌘K</kbd>
        </button>
      </div>

      <div className="sk-header__right">
        <button
          className="sk-icon-btn"
          aria-label="Quick action"
          onClick={() => setPaletteOpen(true)}
        >
          <span aria-hidden="true">＋</span>
        </button>
        <button className="sk-icon-btn" aria-label="Notifications">
          <span aria-hidden="true">🔔</span>
        </button>
        <button className="sk-icon-btn" aria-label="AI assistant" onClick={() => setPaletteOpen(true)}>
          <span aria-hidden="true">✨</span>
        </button>

        <label className="sk-role-switch">
          <span className="sk-visually-hidden">Review role</span>
          <select
            value={activeRole}
            onChange={(e) => setActiveRole(e.target.value as Role)}
            aria-label="Switch review role"
          >
            {ALL_ROLES.map((r) => (
              <option key={r} value={r}>
                {ROLE_LABELS[r]}
              </option>
            ))}
          </select>
        </label>

        <button className="sk-profile" aria-label="Account menu">
          <span className="sk-avatar" aria-hidden="true">{(ROLE_LABELS[activeRole][0])}</span>
        </button>
      </div>
    </header>
  );
}
