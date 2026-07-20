import { useEffect, useMemo, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useApp } from '../../context';
import { WORKSPACES, canView } from '../../config/navigation';
import type { Role } from '../../config/roles';

interface Command {
  id: string;
  label: string;
  group: string;
  run: (navigate: (to: string) => void) => void;
}

const QUICK_ACTIONS: Command[] = [
  { id: 'qa-order', label: 'Quick action: New order', group: 'Actions', run: (n) => n('/orders') },
  { id: 'qa-product', label: 'Quick action: New product', group: 'Actions', run: (n) => n('/catalog/new') },
  { id: 'qa-training', label: 'Quick action: New training', group: 'Actions', run: (n) => n('/training/create') },
  { id: 'qa-ai', label: 'Quick action: Ask AI', group: 'Actions', run: (n) => n('/ai') },
  { id: 'qa-ticket', label: 'Quick action: Raise ticket', group: 'Actions', run: (n) => n('/support/tickets') },
];

export default function CommandPalette() {
  const { paletteOpen, setPaletteOpen, auth } = useApp();
  const navigate = useNavigate();
  const inputRef = useRef<HTMLInputElement>(null);
  const [query, setQuery] = useState('');
  const [index, setIndex] = useState(0);

  const commands = useMemo<Command[]>(() => {
    const nav: Command[] = [];
    for (const ws of WORKSPACES) {
      if (!canView(ws.roles, auth.userRole as Role)) continue;
      for (const page of ws.children) {
        if (page.path === '/') continue;
        if (!canView(page.roles, auth.userRole as Role)) continue;
        nav.push({
          id: `nav-${page.path}`,
          label: `Go to ${ws.label} › ${page.label}`,
          group: 'Navigate',
          run: (n) => n(page.path),
        });
      }
    }
    const quick = QUICK_ACTIONS.filter((c) => {
      // Only show quick actions whose target is visible to the role.
      const target = c.id.replace('qa-', '');
      const map: Record<string, string> = {
        order: '/orders',
        product: '/catalog/new',
        training: '/training/create',
        ai: '/ai',
        ticket: '/support/tickets',
      };
      const page = WORKSPACES.flatMap((w) => w.children).find((p) => p.path === map[target]);
      return page ? canView(page.roles, auth.userRole as Role) : true;
    });
    return [...nav, ...quick];
  }, [auth.userRole]);

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return commands;
    return commands.filter((c) => c.label.toLowerCase().includes(q));
  }, [commands, query]);

  useEffect(() => {
    if (paletteOpen) {
      setQuery('');
      setIndex(0);
      setTimeout(() => inputRef.current?.focus(), 0);
    }
  }, [paletteOpen]);

  useEffect(() => {
    setIndex(0);
  }, [query]);

  if (!paletteOpen) return null;

  const close = () => setPaletteOpen(false);
  const choose = (c?: Command) => {
    if (!c) return;
    c.run((to) => navigate(to));
    close();
  };

  const onKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'Escape') close();
    if (e.key === 'ArrowDown') {
      e.preventDefault();
      setIndex((i) => Math.min(i + 1, filtered.length - 1));
    }
    if (e.key === 'ArrowUp') {
      e.preventDefault();
      setIndex((i) => Math.max(i - 1, 0));
    }
    if (e.key === 'Enter') {
      e.preventDefault();
      choose(filtered[index]);
    }
  };

  return (
    <div className="sk-palette-overlay" onMouseDown={close}>
      <div
        className="sk-palette"
        role="dialog"
        aria-modal="true"
        aria-label="Command palette"
        onMouseDown={(e) => e.stopPropagation()}
        onKeyDown={onKeyDown}
      >
        <input
          ref={inputRef}
          className="sk-palette__input"
          placeholder="Search commands, pages, quick actions…"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          aria-label="Command palette search"
        />
        <ul className="sk-palette__list">
          {filtered.length === 0 && <li className="sk-palette__empty">No matches</li>}
          {filtered.map((c, i) => (
            <li key={c.id}>
              <button
                className={`sk-palette__item ${i === index ? 'sk-palette__item--active' : ''}`}
                onMouseEnter={() => setIndex(i)}
                onClick={() => choose(c)}
              >
                <span className="sk-palette__group">{c.group}</span>
                <span>{c.label}</span>
              </button>
            </li>
          ))}
        </ul>
        <p className="sk-palette__hint">
          <kbd>↑</kbd> <kbd>↓</kbd> to move · <kbd>↵</kbd> to select · <kbd>esc</kbd> to close
        </p>
      </div>
    </div>
  );
}
