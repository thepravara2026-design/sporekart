import { useState, useCallback, useRef, useEffect, useMemo, useId, memo } from 'react';
import { useNavigate } from 'react-router-dom';
import { Icon } from '../../../design-system/icons/Icon';
import type { CommandItem } from '../types';

interface CommandPaletteProps {
  commands: CommandItem[];
  recentCommands?: CommandItem[];
  open: boolean;
  onClose: () => void;
  onExecute?: (command: CommandItem) => void;
}

export const CommandPalette = memo(function CommandPalette({ commands, open, onClose, onExecute }: CommandPaletteProps) {
  const [query, setQuery] = useState('');
  const [selectedIndex, setSelectedIndex] = useState(0);
  const inputRef = useRef<HTMLInputElement>(null);
  const navigate = useNavigate();
  const listboxId = useId();
  const focusTrapRef = useRef<HTMLDivElement>(null);
  const focusableElementsRef = useRef<HTMLElement[]>([]);

  useEffect(() => {
    if (open) {
      setQuery('');
      setSelectedIndex(0);
    }
  }, [open]);

  useEffect(() => {
    if (open) {
      inputRef.current?.focus();
    }
  }, [open]);

  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
        e.preventDefault();
        if (open) onClose();
      }
    };
    document.addEventListener('keydown', handleKeyDown);
    return () => document.removeEventListener('keydown', handleKeyDown);
  }, [open, onClose]);

  useEffect(() => {
    if (!open) return;
    const dialog = focusTrapRef.current;
    if (!dialog) return;
    const focusable = dialog.querySelectorAll<HTMLElement>(
      'a[href], button:not([disabled]), input:not([disabled]), textarea:not([disabled]), select:not([disabled]), [tabindex]:not([tabindex="-1"])'
    );
    focusableElementsRef.current = Array.from(focusable);
    const handleTab = (e: KeyboardEvent) => {
      if (e.key !== 'Tab') return;
      const elements = focusableElementsRef.current;
      if (elements.length === 0) return;
      e.preventDefault();
      const currentIndex = elements.indexOf(document.activeElement as HTMLElement);
      let nextIndex;
      if (e.shiftKey) {
        nextIndex = currentIndex <= 0 ? elements.length - 1 : currentIndex - 1;
      } else {
        nextIndex = currentIndex >= elements.length - 1 ? 0 : currentIndex + 1;
      }
      elements[nextIndex]?.focus();
    };
    document.addEventListener('keydown', handleTab);
    return () => document.removeEventListener('keydown', handleTab);
  }, [open]);

  const filteredCommands = query
    ? commands.filter(
        (c) =>
          c.label.toLowerCase().includes(query.toLowerCase()) ||
          c.keywords?.some((k) => k.includes(query.toLowerCase())) ||
          c.description?.toLowerCase().includes(query.toLowerCase())
      )
    : commands;

  const handleKeyDown = useCallback(
    (e: React.KeyboardEvent) => {
      if (e.key === 'ArrowDown') { e.preventDefault(); setSelectedIndex((prev) => Math.min(prev + 1, filteredCommands.length - 1)); }
      else if (e.key === 'ArrowUp') { e.preventDefault(); setSelectedIndex((prev) => Math.max(prev - 1, 0)); }
      else if (e.key === 'Enter') {
        e.preventDefault();
        const cmd = filteredCommands[selectedIndex];
        if (cmd) {
          onExecute?.(cmd);
          if (cmd.href) navigate(cmd.href);
          onClose();
        }
      } else if (e.key === 'Escape') {
        onClose();
      }
    },
    [filteredCommands, selectedIndex, onExecute, navigate, onClose]
  );

  if (!open) return null;

  const grouped = useMemo(() => {
    return filteredCommands.reduce<Record<string, CommandItem[]>>((acc, cmd) => {
      if (!acc[cmd.category]) acc[cmd.category] = [];
      acc[cmd.category].push(cmd);
      return acc;
    }, {});
  }, [filteredCommands]);

  return (
    <div
      style={{
        position: 'fixed',
        inset: 0,
        zIndex: 10000,
        background: 'rgba(0,0,0,0.4)',
        display: 'flex',
        alignItems: 'flex-start',
        justifyContent: 'center',
        paddingTop: '10vh',
      }}
      onClick={onClose}
    >
      <div
        ref={focusTrapRef}
        role="dialog"
        aria-modal="true"
        aria-label="Command palette"
        onClick={(e) => e.stopPropagation()}
        style={{
          width: '100%',
          maxWidth: 560,
          background: 'var(--color-surface)',
          borderRadius: 'var(--radius-xl)',
          boxShadow: 'var(--elevation-xl)',
          overflow: 'hidden',
        }}
      >
        <div style={{ display: 'flex', alignItems: 'center', gap: 12, padding: '12px 16px', borderBottom: '1px solid var(--color-border)' }}>
          <Icon name="search" size={18} style={{ color: 'var(--color-text-tertiary)' }} />
          <input
            ref={inputRef}
            type="text"
            value={query}
            onChange={(e) => { setQuery(e.target.value); setSelectedIndex(0); }}
            onKeyDown={handleKeyDown}
            placeholder="Search commands..."
            aria-label="Command palette"
            role="combobox"
            aria-expanded="true"
            aria-controls={listboxId}
            aria-activedescendant={filteredCommands[selectedIndex] ? `${listboxId}-option-${selectedIndex}` : undefined}
            style={{
              flex: 1,
              border: 'none',
              background: 'transparent',
              color: 'var(--color-text-primary)',
              fontSize: 'var(--text-body)',
              outline: 'none',
              boxShadow: 'none',
            }}
            onFocus={(e) => e.currentTarget.style.boxShadow = '0 0 0 2px var(--color-primary)'}
            onBlur={(e) => e.currentTarget.style.boxShadow = 'none'}
          />
          <kbd style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', background: 'var(--color-surface-hover)', padding: '2px 6px', borderRadius: 'var(--radius-sm)' }}>ESC</kbd>
        </div>
        <div id={listboxId} role="listbox" style={{ maxHeight: 400, overflowY: 'auto', padding: 8 }}>
          {query && filteredCommands.length === 0 && (
            <div style={{ textAlign: 'center', padding: '24px 16px', color: 'var(--color-text-tertiary)' }}>
              No commands found for "{query}"
            </div>
          )}
          {Object.entries(grouped).map(([category, cmds]) => (
            <div key={category}>
              <div style={{ padding: '6px 12px', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', textTransform: 'uppercase', letterSpacing: '0.05em', fontWeight: 600 }}>
                {category}
              </div>
              {cmds.map((cmd) => {
                const globalIdx = filteredCommands.indexOf(cmd);
                return (
                  <button
                    key={cmd.id}
                    role="option"
                    aria-selected={globalIdx === selectedIndex}
                    id={`${listboxId}-option-${globalIdx}`}
                    onClick={() => {
                      onExecute?.(cmd);
                      if (cmd.href) navigate(cmd.href);
                      onClose();
                    }}
                    style={{
                      display: 'flex',
                      alignItems: 'center',
                      gap: 10,
                      width: '100%',
                      padding: '8px 12px',
                      border: 'none',
                      borderRadius: 'var(--radius-md)',
                      background: globalIdx === selectedIndex ? 'var(--color-primary-alpha)' : 'transparent',
                      cursor: 'pointer',
                      textAlign: 'left',
                      color: 'var(--color-text-primary)',
                      fontSize: 'var(--text-body)',
                    }}
                  >
                    {cmd.icon && <Icon name={cmd.icon} size={16} style={{ color: 'var(--color-text-tertiary)' }} />}
                    <div style={{ flex: 1 }}>
                      <div>{cmd.label}</div>
                      {cmd.description && <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{cmd.description}</div>}
                    </div>
                    {cmd.shortcut && (
                      <kbd style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', background: 'var(--color-surface-hover)', padding: '2px 6px', borderRadius: 'var(--radius-sm)' }}>{cmd.shortcut}</kbd>
                    )}
                  </button>
                );
              })}
            </div>
          ))}
        </div>
        <div style={{ padding: '8px 16px', borderTop: '1px solid var(--color-border)', display: 'flex', gap: 16, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
          <span>↑↓ Navigate</span>
          <span>↵ Select</span>
          <span>Esc Close</span>
        </div>
      </div>
    </div>
  );
});
