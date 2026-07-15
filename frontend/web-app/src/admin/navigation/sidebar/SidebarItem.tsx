import { memo } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Icon } from '../../../design-system/icons/Icon';
import type { NavItem } from '../types';

interface SidebarItemProps {
  item: NavItem;
  depth?: number;
  mini?: boolean;
  onToggle?: (id: string) => void;
  collapsed?: boolean;
  isActive?: boolean;
}

export const SidebarItem = memo(function SidebarItem({ item, depth = 0, mini, onToggle, collapsed, isActive }: SidebarItemProps) {
  const navigate = useNavigate();
  const location = useLocation();
  const active = isActive ?? (item.href ? location.pathname.startsWith(item.href) : false);
  const hasChildren = item.children && item.children.length > 0;

  const handleClick = () => {
    if (item.disabled) return;
    if (hasChildren) {
      onToggle?.(item.id);
    } else if (item.href) {
      navigate(item.href);
    }
  };

  if (item.separator) {
    return <div style={{ height: 1, background: 'var(--color-border)', margin: '8px 12px' }} />;
  }

  if (mini) {
    if (item.isGroup) return null;
    return (
      <button
        onClick={handleClick}
        disabled={item.disabled}
        aria-label={item.label}
        title={item.label}
        aria-expanded={hasChildren ? !collapsed : undefined}
        aria-haspopup={hasChildren ? 'true' as const : undefined}
        style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          width: 40,
          height: 40,
          margin: '2px auto',
          borderRadius: 'var(--radius-md)',
          border: 'none',
          background: active ? 'var(--color-primary-alpha)' : 'transparent',
          color: active ? 'var(--color-primary)' : 'var(--color-text-secondary)',
          cursor: item.disabled ? 'not-allowed' : 'pointer',
          opacity: item.disabled ? 0.4 : 1,
          position: 'relative',
        }}
      >
        {item.icon && <Icon name={item.icon} size={18} />}
        {item.badge && (
          <span style={{ position: 'absolute', top: 2, right: 2, width: 8, height: 8, borderRadius: '50%', background: item.badgeColor || 'var(--color-error)' }} />
        )}
      </button>
    );
  }

  return (
    <div>
      <button
        onClick={handleClick}
        disabled={item.disabled}
        aria-expanded={hasChildren ? !collapsed : undefined}
        aria-haspopup={hasChildren ? 'true' as const : undefined}
        style={{
          display: 'flex',
          alignItems: 'center',
          gap: 10,
          width: '100%',
          padding: '8px 12px',
          paddingLeft: 12 + depth * 16,
          border: 'none',
          borderRadius: 'var(--radius-md)',
          background: active ? 'var(--color-primary-alpha)' : 'transparent',
          color: active ? 'var(--color-primary)' : 'var(--color-text-secondary)',
          cursor: item.disabled ? 'not-allowed' : 'pointer',
          fontSize: 'var(--text-body)',
          fontWeight: active ? 600 : 400,
          opacity: item.disabled ? 0.4 : 1,
        }}
      >
        {item.icon && <Icon name={item.icon} size={18} />}
        <span style={{ flex: 1, textAlign: 'left' }}>{item.label}</span>
        {item.badge && (
          <span
            style={{
              background: item.badgeColor || 'var(--color-primary)',
              color: '#fff',
              fontSize: 11,
              fontWeight: 600,
              borderRadius: 'var(--radius-full)',
              padding: '0 6px',
              minWidth: 18,
              textAlign: 'center',
              lineHeight: '18px',
            }}
          >
            {item.badge}
          </span>
        )}
        {hasChildren && (
          <Icon name={collapsed ? 'chevron-right' : 'chevron-down'} size={14} />
        )}
      </button>
      {hasChildren && !collapsed && (
        <div>
          {item.children!.map((child) => (
            <SidebarItem
              key={child.id}
              item={child}
              depth={depth + 1}
              onToggle={onToggle}
              collapsed={collapsed}
            />
          ))}
        </div>
      )}
    </div>
  );
});
