import { NavLink } from 'react-router-dom';
import { useApp } from '../../context';
import { getVisibleWorkspaces } from '../../config/navigation';
import type { WorkspaceGroup } from '../../config/roles';

const GROUP_LABELS: Record<WorkspaceGroup, string> = {
  discover: 'Discover',
  operate: 'Operate',
  intelligence: 'Intelligence',
  platform: 'Platform',
};

const GROUP_ORDER: WorkspaceGroup[] = ['discover', 'operate', 'intelligence', 'platform'];

export default function Sidebar({ open, onNavigate }: { open: boolean; onNavigate: () => void }) {
  const { auth } = useApp();
  const workspaces = getVisibleWorkspaces(auth.userRole);

  return (
    <nav
      className={`sk-sidebar ${open ? 'sk-sidebar--open' : ''}`}
      aria-label="Workspaces"
    >
      {GROUP_ORDER.map((group) => {
        const groupWorkspaces = workspaces.filter((w) => w.group === group);
        if (groupWorkspaces.length === 0) return null;
        return (
          <div className="sk-sidebar__group" key={group}>
            <p className="sk-sidebar__group-label">{GROUP_LABELS[group]}</p>
            <ul className="sk-sidebar__list">
              {groupWorkspaces.map((ws) => (
                <li key={ws.id} className="sk-sidebar__item">
                  <NavLink
                    to={ws.rootPath}
                    className={({ isActive }) =>
                      `sk-workspace-link ${isActive ? 'sk-workspace-link--active' : ''}`
                    }
                    onClick={onNavigate}
                  >
                    <span className="sk-workspace-link__icon" aria-hidden="true">
                      {ws.icon}
                    </span>
                    <span className="sk-workspace-link__label">{ws.label}</span>
                  </NavLink>
                  <ul className="sk-sidebar__children">
                    {ws.children
                      .filter((c) => c.path !== ws.rootPath)
                      .map((child) => (
                        <li key={child.path}>
                          <NavLink
                            to={child.path}
                            className={({ isActive }) =>
                              `sk-child-link ${isActive ? 'sk-child-link--active' : ''}`
                            }
                            onClick={onNavigate}
                          >
                            {child.label}
                          </NavLink>
                        </li>
                      ))}
                  </ul>
                </li>
              ))}
            </ul>
          </div>
        );
      })}
    </nav>
  );
}
