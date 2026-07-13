import React, { useState } from 'react';

const containerStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--space-section-gap)',
  padding: 'var(--space-page-y) var(--space-page-x)',
};

const headerStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: '12px',
  flexWrap: 'wrap',
};

const titleStyle: React.CSSProperties = {
  fontSize: 'var(--text-h1)',
  fontWeight: 'var(--weight-bold)',
  margin: 0,
};

const badgeStyle: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  padding: '4px 10px',
  borderRadius: 'var(--radius-pill)',
  background: 'var(--color-bg-primary-default)',
  color: 'var(--color-text-on-primary)',
  fontSize: 'var(--text-caption)',
  fontWeight: 'var(--weight-semibold)',
};

const sectionStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: '12px',
};

const sectionTitleStyle: React.CSSProperties = {
  fontSize: 'var(--text-h2)',
  fontWeight: 'var(--weight-semibold)',
  margin: 0,
};

const tableStyle: React.CSSProperties = {
  width: '100%',
  borderCollapse: 'collapse',
  fontSize: 'var(--text-body)',
};

const thStyle: React.CSSProperties = {
  textAlign: 'left',
  padding: '10px 12px',
  borderBottom: '2px solid var(--color-border-default)',
  fontWeight: 'var(--weight-semibold)',
  color: 'var(--color-text-primary)',
};

const tdStyle: React.CSSProperties = {
  padding: '10px 12px',
  borderBottom: '1px solid var(--color-border-default)',
  color: 'var(--color-text-secondary)',
};

const cardStyle: React.CSSProperties = {
  background: 'var(--color-bg-surface-default)',
  border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-card)',
  padding: '20px',
};

const checklistItemStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: '10px',
  padding: '8px 0',
  borderBottom: '1px solid var(--color-border-default)',
};

const checkboxStyle: React.CSSProperties = {
  width: '18px',
  height: '18px',
  cursor: 'pointer',
  accentColor: 'var(--color-bg-primary-default)',
};

const statusIconStyle = (checked: boolean): React.CSSProperties => ({
  width: '20px',
  height: '20px',
  borderRadius: '50%',
  display: 'inline-flex',
  alignItems: 'center',
  justifyContent: 'center',
  fontSize: '12px',
  fontWeight: 'var(--weight-bold)',
  color: checked ? 'var(--color-text-on-primary)' : 'var(--color-text-secondary)',
  background: checked ? 'var(--color-success-500)' : 'var(--color-bg-surface-raised)',
  border: checked ? 'none' : '1px solid var(--color-border-default)',
});

const wcagLevels = [
  { level: 'A', description: 'Minimum level — essential accessibility', status: 'Pass' },
  { level: 'AA', description: 'Target level — removes major barriers', status: 'Target' },
  { level: 'AAA', description: 'Highest level — enhanced usability', status: 'Optional' },
];

const keyboardShortcuts = [
  { key: 'Tab', description: 'Move focus to next focusable element' },
  { key: 'Shift+Tab', description: 'Move focus to previous focusable element' },
  { key: 'Enter', description: 'Activate button / link / menu item' },
  { key: 'Space', description: 'Toggle checkbox / switch. Activate button' },
  { key: 'Escape', description: 'Close modal / dropdown / popover / dialog' },
  { key: 'ArrowUp', description: 'Navigate up in menus / lists / radio groups' },
  { key: 'ArrowDown', description: 'Navigate down in menus / lists / radio groups' },
  { key: 'ArrowLeft', description: 'Navigate left in tabs / carousels / sliders' },
  { key: 'ArrowRight', description: 'Navigate right in tabs / carousels / sliders' },
  { key: 'Home', description: 'Go to first item in a list / tab / page' },
  { key: 'End', description: 'Go to last item in a list / tab / page' },
];

const ariaItems = [
  { role: 'role="button"', usage: 'Interactive clickable elements that are not <button>' },
  { role: 'role="dialog"', usage: 'Modal and non-modal overlay containers' },
  { role: 'role="alert"', usage: 'Live region for important messages' },
  { role: 'role="tablist" / "tab" / "tabpanel"', usage: 'Tab-based navigation' },
  { role: 'aria-expanded', usage: 'Indicate expandable/collapsible state' },
  { role: 'aria-label', usage: 'Accessible name for elements without visible text' },
  { role: 'aria-describedby', usage: 'Link element to description text' },
  { role: 'aria-live', usage: 'Polite / assertive live region announcements' },
  { role: 'aria-hidden', usage: 'Hide decorative/non-essential elements from AT' },
  { role: 'aria-current', usage: 'Indicate current page / step / item' },
];

const checklistData = [
  'All images have alt text',
  'All form inputs have labels',
  'Keyboard navigation is logical',
  'Focus indicators are visible',
  'Color contrast meets WCAG AA',
  'Screen reader announcements',
  'ARIA landmarks are present',
  'Touch targets are at least 44x44px',
  'Motion can be reduced',
  'Zoom to 200% does not break layout',
];

function ChecklistSection() {
  const [checks, setChecks] = useState<Record<string, boolean>>({});

  return (
    <div style={sectionStyle}>
      <h2 style={sectionTitleStyle}>Accessibility Checklist</h2>
      <div style={cardStyle}>
        {checklistData.map((item) => (
          <div key={item} style={checklistItemStyle}>
            <input
              type="checkbox"
              style={checkboxStyle}
              checked={!!checks[item]}
              onChange={() => setChecks((prev) => ({ ...prev, [item]: !prev[item] }))}
            />
            <div style={statusIconStyle(!!checks[item])}>
              {checks[item] ? '✓' : '○'}
            </div>
            <span style={{ color: 'var(--color-text-primary)', flex: 1 }}>{item}</span>
          </div>
        ))}
      </div>
    </div>
  );
}

function ContrastTable() {
  const ratios = [
    { text: 'Normal text (AA)', ratio: '4.5:1', example: '#333 on #FFF', pass: true },
    { text: 'Large text (AA)', ratio: '3:1', example: '#666 on #FFF', pass: true },
    { text: 'UI components (AA)', ratio: '3:1', example: '#999 on #FFF', pass: false },
    { text: 'Normal text (AAA)', ratio: '7:1', example: '#222 on #FFF', pass: true },
    { text: 'Disabled text', ratio: '3:1', example: '#BBB on #FFF', pass: false },
  ];

  return (
    <table style={tableStyle}>
      <thead>
        <tr>
          <th style={thStyle}>Requirement</th>
          <th style={thStyle}>Ratio</th>
          <th style={thStyle}>Example</th>
          <th style={thStyle}>Status</th>
        </tr>
      </thead>
      <tbody>
        {ratios.map((r) => (
          <tr key={r.text}>
            <td style={tdStyle}>{r.text}</td>
            <td style={tdStyle}>{r.ratio}</td>
            <td style={tdStyle}>{r.example}</td>
            <td style={tdStyle}>
              <span
                style={{
                  color: r.pass ? 'var(--color-success-500)' : 'var(--color-danger-500)',
                  fontWeight: 'var(--weight-semibold)',
                }}
              >
                {r.pass ? 'PASS' : 'FAIL'}
              </span>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

export default function AccessibilityCenter() {
  return (
    <div style={containerStyle}>
      <div style={headerStyle}>
        <h1 style={titleStyle}>Accessibility Center</h1>
        <span style={badgeStyle}>WCAG 2.1 AA</span>
      </div>

      <div style={sectionStyle}>
        <h2 style={sectionTitleStyle}>WCAG Compliance</h2>
        <div style={cardStyle}>
          <p style={{ margin: '0 0 12px', color: 'var(--color-text-secondary)' }}>
            Target: WCAG 2.1 Level AA. All stable components must meet this level.
          </p>
          <table style={tableStyle}>
            <thead>
              <tr>
                <th style={thStyle}>Level</th>
                <th style={thStyle}>Description</th>
                <th style={thStyle}>Status</th>
              </tr>
            </thead>
            <tbody>
              {wcagLevels.map((l) => (
                <tr key={l.level}>
                  <td style={{ ...tdStyle, fontWeight: 'var(--weight-semibold)' }}>{l.level}</td>
                  <td style={tdStyle}>{l.description}</td>
                  <td style={tdStyle}>{l.status}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      <div style={sectionStyle}>
        <h2 style={sectionTitleStyle}>Keyboard Navigation</h2>
        <div style={cardStyle}>
          <p style={{ margin: '0 0 12px', color: 'var(--color-text-secondary)' }}>
            Common keyboard shortcuts used across components.
          </p>
          <table style={tableStyle}>
            <thead>
              <tr>
                <th style={thStyle}>Key</th>
                <th style={thStyle}>Description</th>
              </tr>
            </thead>
            <tbody>
              {keyboardShortcuts.map((k) => (
                <tr key={k.key}>
                  <td style={{ ...tdStyle, fontFamily: 'var(--font-mono)', fontWeight: 'var(--weight-semibold)' }}>
                    {k.key}
                  </td>
                  <td style={tdStyle}>{k.description}</td>
                </tr>
              ))}
            </tbody>
          </table>
          <p style={{ margin: '12px 0 0', color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)' }}>
            Focus is managed through FocusTrap in modals, dialogs, and drawers.
            Tab order follows the logical document flow (DOM order).
          </p>
        </div>
      </div>

      <div style={sectionStyle}>
        <h2 style={sectionTitleStyle}>ARIA Usage</h2>
        <div style={cardStyle}>
          <table style={tableStyle}>
            <thead>
              <tr>
                <th style={thStyle}>ARIA Role / Property</th>
                <th style={thStyle}>Usage</th>
              </tr>
            </thead>
            <tbody>
              {ariaItems.map((a) => (
                <tr key={a.role}>
                  <td style={{ ...tdStyle, fontFamily: 'var(--font-mono)', fontWeight: 'var(--weight-semibold)' }}>
                    {a.role}
                  </td>
                  <td style={tdStyle}>{a.usage}</td>
                </tr>
              ))}
            </tbody>
          </table>
          <div style={{ marginTop: '12px' }}>
            <h4 style={{ margin: '0 0 6px', color: 'var(--color-text-primary)' }}>Best Practices</h4>
            <ul style={{ margin: 0, paddingLeft: '20px', color: 'var(--color-text-secondary)', lineHeight: '1.6' }}>
              <li>Use semantic HTML elements whenever possible before ARIA</li>
              <li>Do not override native semantics unless absolutely necessary</li>
              <li>All interactive elements must be keyboard accessible</li>
              <li>Live regions should use polite by default, assertive sparingly</li>
              <li>Test with actual screen readers (NVDA, VoiceOver, JAWS)</li>
            </ul>
          </div>
        </div>
      </div>

      <div style={sectionStyle}>
        <h2 style={sectionTitleStyle}>Focus Order</h2>
        <div style={cardStyle}>
          <p style={{ margin: 0, color: 'var(--color-text-secondary)', lineHeight: '1.6' }}>
            Logical tab order follows the DOM order. Components are arranged so that focus moves
            in a predictable sequence: header → navigation → main content → footer. Modal overlays
            trap focus within the dialog. Skip-to-content links are available at the top of each page.
          </p>
        </div>
      </div>

      <div style={sectionStyle}>
        <h2 style={sectionTitleStyle}>Reduced Motion</h2>
        <div style={cardStyle}>
          <p style={{ margin: 0, color: 'var(--color-text-secondary)', lineHeight: '1.6' }}>
            All animations and transitions respect the{' '}
            <code style={{ fontFamily: 'var(--font-mono)' }}>prefers-reduced-motion</code> media query.
            When a user enables reduced motion in their OS settings, animations are disabled or reduced
            to essential opacity transitions only. The token <code style={{ fontFamily: 'var(--font-mono)' }}>--duration-instant</code>{' '}
            (0ms) is applied to all non-essential transitions.
          </p>
        </div>
      </div>

      <div style={sectionStyle}>
        <h2 style={sectionTitleStyle}>Contrast Validation</h2>
        <div style={cardStyle}>
          <p style={{ margin: '0 0 12px', color: 'var(--color-text-secondary)' }}>
            Color contrast ratios target WCAG 2.1 AA standards.
          </p>
          <ContrastTable />
        </div>
      </div>

      <ChecklistSection />
    </div>
  );
}
