import { PROFILE_NAV_ITEMS } from '../types';

interface ProfileNavigationProps {
  activeId: string;
  onNavigate: (id: string) => void;
}

export function ProfileNavigation({ activeId, onNavigate }: ProfileNavigationProps) {
  return (
    <nav className="profile-subnav" aria-label="Profile sections">
      <div className="profile-subnav__list">
        {PROFILE_NAV_ITEMS.map((item) => (
          <button
            key={item.id}
            className={`profile-subnav__item ${activeId === item.id ? 'profile-subnav__item--active' : ''}`}
            onClick={() => onNavigate(item.id)}
            aria-current={activeId === item.id ? 'page' : undefined}
          >
            <span className="profile-subnav__icon">{item.icon === 'user' ? '👤' : item.icon === 'edit' ? '✏️' : item.icon === 'book-open' ? '📖' : item.icon === 'briefcase' ? '💼' : item.icon === 'book' ? '📚' : item.icon === 'users' ? '👨‍👩‍👧' : item.icon === 'folder' ? '📁' : '🕐'}</span>
            <span className="profile-subnav__label">{item.label}</span>
          </button>
        ))}
      </div>
    </nav>
  );
}
