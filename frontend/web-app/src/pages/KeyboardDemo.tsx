import { useEffect, useRef, useState } from 'react';
import { Link } from 'react-router-dom';

export default function KeyboardDemo() {
  const [focusLog, setFocusLog] = useState<string[]>([]);
  const logRef = useRef<HTMLDivElement>(null);

  const logFocus = (el: HTMLElement) => {
    const id = el.id || el.tagName + (el.className ? `.${el.className.split(' ')[0]}` : '');
    setFocusLog(prev => [...prev.slice(-19), `[${new Date().toLocaleTimeString()}] Focus: ${id}`]);
  };

  useEffect(() => {
    const handler = (e: FocusEvent) => {
      if (e.target instanceof HTMLElement) logFocus(e.target);
    };
    document.addEventListener('focusin', handler, true);
    return () => document.removeEventListener('focusin', handler, true);
  }, []);

  return (
    <div className="sk-content__header">
      <div className="sk-content__title-row">
        <div>
          <h1>Keyboard Navigation Demo</h1>
          <p className="sk-content__subtitle">Test Tab order, focus management, component patterns, and global shortcuts.</p>
        </div>
      </div>

      <div className="sk-demo-toolbar" role="toolbar" aria-label="Keyboard demo controls">
        <button className="sk-secondary-action" onClick={() => setFocusLog([])}>Clear Log</button>
        <kbd className="sk-kbd-hint">Tab</kbd> next · <kbd className="sk-kbd-hint">Shift+Tab</kbd> prev · <kbd className="sk-kbd-hint">Esc</kbd> close · <kbd className="sk-kbd-hint">Cmd/Ctrl+K</kbd> palette
        <Link to="/demo" className="sk-secondary-action">← Back</Link>
      </div>

      <section className="sk-panel" aria-labelledby="focus-log-title">
        <h2 id="focus-log-title">Live Focus Log</h2>
        <div ref={logRef} className="sk-focus-log" aria-live="polite" aria-atomic="false">
          {focusLog.map((l, i) => <div key={i}>{l}</div>)}
        </div>
      </section>

      <section className="sk-panel" aria-labelledby="shortcuts-title">
        <h2 id="shortcuts-title">Global Shortcuts</h2>
        <table className="sk-table">
          <thead><tr><th>Shortcut</th><th>Action</th><th>Context</th></tr></thead>
          <tbody>
            <tr><td><kbd>Cmd/Ctrl + K</kbd></td><td>Open Command Palette</td><td>Global</td></tr>
            <tr><td><kbd>Cmd/Ctrl + /</kbd></td><td>Focus Global Search</td><td>Global</td></tr>
            <tr><td><kbd>Esc</kbd></td><td>Close dialog/palette/drawer/toast</td><td>Global</td></tr>
            <tr><td><kbd>Tab</kbd></td><td>Next focusable element</td><td>Global</td></tr>
            <tr><td><kbd>Shift + Tab</kbd></td><td>Previous focusable element</td><td>Global</td></tr>
            <tr><td><kbd>Enter</kbd></td><td>Activate button/link</td><td>Focused element</td></tr>
            <tr><td><kbd>Space</kbd></td><td>Toggle checkbox / activate button</td><td>Focused element</td></tr>
            <tr><td><kbd>Arrow Keys</kbd></td><td>Navigate within component</td><td>Menus, tabs, tables, palette</td></tr>
            <tr><td><kbd>Home</kbd></td><td>First item</td><td>List, menu, table row</td></tr>
            <tr><td><kbd>End</kbd></td><td>Last item</td><td>List, menu, table row</td></tr>
            <tr><td><kbd>Page Up / Down</kbd></td><td>Scroll / paginate</td><td>Table, list</td></tr>
          </tbody>
        </table>
      </section>

      <section className="sk-panel" aria-labelledby="components-title">
        <h2 id="components-title">Component Keyboard Patterns</h2>
        <div className="sk-component-grid">
          <article className="sk-demo-card">
            <h3>Sidebar Navigation</h3>
            <ul className="sk-kbd-list">
              <li><kbd>Tab</kbd> → Enter sidebar</li>
              <li><kbd>ArrowDown/Up</kbd> → Next/prev workspace</li>
              <li><kbd>ArrowRight</kbd> → Expand children</li>
              <li><kbd>ArrowLeft</kbd> → Collapse</li>
              <li><kbd>Enter</kbd> → Navigate</li>
            </ul>
          </article>
          <article className="sk-demo-card">
            <h3>Data Table</h3>
            <ul className="sk-kbd-list">
              <li><kbd>Arrow Keys</kbd> → Navigate cells</li>
              <li><kbd>Space</kbd> → Toggle row selection</li>
              <li><kbd>Shift+Arrow</kbd> → Extend selection</li>
              <li><kbd>Enter</kbd> → Row action</li>
              <li><kbd>Esc</kbd> → Clear selection</li>
            </ul>
          </article>
          <article className="sk-demo-card">
            <h3>Tabs</h3>
            <ul className="sk-kbd-list">
              <li><kbd>ArrowLeft/Right</kbd> → Previous/next tab</li>
              <li><kbd>Home/End</kbd> → First/last tab</li>
              <li><kbd>Tab</kbd> → Into panel</li>
            </ul>
          </article>
          <article className="sk-demo-card">
            <h3>Command Palette</h3>
            <ul className="sk-kbd-list">
              <li><kbd>Cmd/Ctrl+K</kbd> → Open</li>
              <li><kbd>ArrowDown/Up</kbd> → Navigate results</li>
              <li><kbd>Enter</kbd> → Execute</li>
              <li><kbd>Esc</kbd> → Close</li>
            </ul>
          </article>
          <article className="sk-demo-card">
            <h3>Date Picker</h3>
            <ul className="sk-kbd-list">
              <li><kbd>Arrow Keys</kbd> → Day/week/month</li>
              <li><kbd>Home/End</kbd> → Month start/end</li>
              <li><kbd>Enter</kbd> → Select</li>
              <li><kbd>Esc</kbd> → Close</li>
            </ul>
          </article>
          <article className="sk-demo-card">
            <h3>Combobox</h3>
            <ul className="sk-kbd-list">
              <li><kbd>ArrowDown</kbd> → Open / next</li>
              <li><kbd>ArrowUp</kbd> → Previous</li>
              <li><kbd>Enter</kbd> → Select</li>
              <li><kbd>Esc</kbd> → Close</li>
            </ul>
          </article>
        </div>
      </section>

      <section className="sk-panel" aria-labelledby="test-title">
        <h2 id="test-title">Interactive Test Area</h2>
        <div className="sk-test-area">
          <div className="sk-test-group">
            <label>Text input <input type="text" placeholder="Tab here first" /></label>
            <label>Select <select><option>Option 1</option><option>Option 2</option></select></label>
            <label><input type="checkbox" /> Checkbox</label>
            <label><input type="radio" name="r" /> Radio 1</label>
            <label><input type="radio" name="r" /> Radio 2</label>
            <button className="sk-primary-action">Primary Button</button>
            <button className="sk-secondary-action">Secondary Button</button>
          </div>
          <div className="sk-test-group" role="tablist" aria-label="Demo tabs">
            <button role="tab" aria-selected="true" className="sk-tab">Tab One</button>
            <button role="tab" aria-selected="false" className="sk-tab">Tab Two</button>
            <button role="tab" aria-selected="false" className="sk-tab">Tab Three</button>
          </div>
          <div className="sk-test-group" role="menu" aria-label="Demo menu">
            <button role="menuitem">Menu Item 1</button>
            <button role="menuitem">Menu Item 2</button>
            <button role="menuitem">Menu Item 3</button>
          </div>
        </div>
      </section>
    </div>
  );
}