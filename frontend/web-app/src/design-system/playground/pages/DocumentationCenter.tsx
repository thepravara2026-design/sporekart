import React from 'react';

const containerStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--space-section-gap)',
  padding: 'var(--space-page-y) var(--space-page-x)',
};

const titleStyle: React.CSSProperties = {
  fontSize: 'var(--text-h1)',
  fontWeight: 'var(--weight-bold)',
  margin: 0,
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

const cardStyle: React.CSSProperties = {
  background: 'var(--color-bg-surface-default)',
  border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-card)',
  padding: '20px',
  lineHeight: '1.6',
  color: 'var(--color-text-secondary)',
};

const treeStyle: React.CSSProperties = {
  fontFamily: 'var(--font-mono)',
  fontSize: 'var(--text-caption)',
  lineHeight: '1.8',
  whiteSpace: 'pre',
  overflowX: 'auto',
  color: 'var(--color-text-primary)',
};

const codeBlockStyle: React.CSSProperties = {
  background: 'var(--color-bg-surface-raised)',
  border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-md)',
  padding: '16px',
  fontFamily: 'var(--font-mono)',
  fontSize: 'var(--text-caption)',
  lineHeight: '1.6',
  overflowX: 'auto',
  whiteSpace: 'pre',
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

const pipelineSteps = [
  { stage: '1. Need Identification', owner: 'Product / Design', description: 'Identify gap in component library' },
  { stage: '2. Design Specification', owner: 'Design Team', description: 'Figma mockups with all states and tokens' },
  { stage: '3. Implementation', owner: 'Engineering', description: 'Code the component following DS standards' },
  { stage: '4. Documentation', owner: 'Engineering', description: 'Write README, stories, and usage examples' },
  { stage: '5. Accessibility Review', owner: 'A11y Team', description: 'Audit against WCAG 2.1 AA criteria' },
  { stage: '6. Code Review', owner: 'DS Team', description: 'Two senior engineers review implementation' },
  { stage: '7. QA & Testing', owner: 'QA', description: 'Functional, visual regression, and integration tests' },
  { stage: '8. Release', owner: 'DS Lead', description: 'Publish to npm and update the component manifest' },
];

const approvalWorkflow = [
  { item: 'New component', approver: 'Design Lead + DS Lead' },
  { item: 'Breaking change', approver: 'Principal DS Architect' },
  { item: 'Token change', approver: 'Design Ops + DS Lead' },
  { item: 'Minor variant addition', approver: 'Component owner' },
  { item: 'Bug fix', approver: 'Component owner (self-approve)' },
  { item: 'Deprecation', approver: 'Principal DS Architect' },
];

const changelog = [
  { version: '2.1.0', date: '2026-07-10', notes: 'Added Toast undo action. Button loading state animation.' },
  { version: '2.0.0', date: '2026-07-01', notes: 'Major release. Icon registry refactored. Dialog v2 API.' },
  { version: '1.12.0', date: '2026-06-20', notes: 'New CommandPalette component. Calendar range picker.' },
  { version: '1.11.0', date: '2026-06-10', notes: 'MultiSelect create-option. FileUpload progress states.' },
  { version: '1.10.0', date: '2026-06-01', notes: 'ThemeProvider dark mode stable. Responsive token system.' },
];

function ArchitectureSection() {
  return (
    <div style={sectionStyle}>
      <h2 style={sectionTitleStyle}>Architecture</h2>
      <div style={cardStyle}>
        <p style={{ marginTop: 0 }}>The design system follows a layered architecture:</p>
        <div style={treeStyle}>
{`App
 └─ ThemeProvider
     ├─ AccessibilityProvider
     ├─ LocalizationProvider
     ├─ DialogProvider
     ├─ ToastProvider
     ├─ NotificationProvider
     ├─ FeatureFlagProvider
     └─ PerformanceProvider
         └─ Components
              ├─ Core (Button, Input, Icon, etc.)
              ├─ Forms (Select, MultiSelect, FileUpload, etc.)
              ├─ Display (Card, Table, Badge, Avatar, etc.)
              ├─ Navigation (Header, Sidebar, Tabs, etc.)
              ├─ Feedback (Dialog, Toast, Alert, Tooltip, etc.)
              ├─ Charts (ChartContainer, LineChart, BarChart, etc.)
              └─ Layout (ContentContainer, PageContainer, Stack, Grid, etc.)`}
        </div>
        <p style={{ marginBottom: 0 }}>
          <strong style={{ color: 'var(--color-text-primary)' }}>Token Flow:</strong>{' '}
          Primitives → Semantic tokens → Component tokens → CSS custom properties.
          All values are resolved at build time via the token manifest.
        </p>
      </div>
    </div>
  );
}

function GuidelinesSection() {
  return (
    <div style={sectionStyle}>
      <h2 style={sectionTitleStyle}>Guidelines</h2>
      <div style={cardStyle}>
        <h4 style={{ margin: '0 0 8px', color: 'var(--color-text-primary)' }}>When to use which component</h4>
        <ul style={{ margin: '0 0 16px', paddingLeft: '20px', lineHeight: '1.8' }}>
          <li>Use <strong>Button</strong> for primary actions, <strong>Link</strong> for navigation</li>
          <li>Use <strong>Dialog</strong> for confirmations, <strong>Modal</strong> for complex content</li>
          <li>Use <strong>Select</strong> for 5+ options, <strong>RadioGroup</strong> for 2-5 options</li>
          <li>Use <strong>Toast</strong> for transient feedback, <strong>Alert</strong> for inline messages</li>
          <li>Use <strong>Card</strong> for grouped content, <strong>Table</strong> for tabular data</li>
        </ul>

        <h4 style={{ margin: '0 0 8px', color: 'var(--color-text-primary)' }}>Naming Conventions</h4>
        <ul style={{ margin: '0 0 16px', paddingLeft: '20px', lineHeight: '1.8' }}>
          <li>Components: PascalCase (Button, MultiSelect, Icon)</li>
          <li>Props: camelCase (isDisabled, variant, onSelect)</li>
          <li>CSS custom properties: kebab-case (--color-text-primary, --spacing-md)</li>
          <li>Files: PascalCase for components, camelCase for utilities</li>
        </ul>

        <h4 style={{ margin: '0 0 8px', color: 'var(--color-text-primary)' }}>Import Conventions</h4>
        <div style={codeBlockStyle}>
{`import { Button } from '../../design-system/components/Button';
import { Icon } from '../../design-system/icons';
import { registry } from '../../design-system/icons/registry';
import { componentManifest } from '../../design-system/playground/catalog/componentManifest';`}
        </div>

        <h4 style={{ margin: '12px 0 8px', color: 'var(--color-text-primary)' }}>Provider Setup</h4>
        <div style={codeBlockStyle}>
{`import { ThemeProvider } from '../../design-system/providers/ThemeProvider';
import { AccessibilityProvider } from '../../design-system/providers/AccessibilityProvider';

function App() {
  return (
    <ThemeProvider>
      <AccessibilityProvider>
        <YourApp />
      </AccessibilityProvider>
    </ThemeProvider>
  );
}`}
        </div>
      </div>
    </div>
  );
}

function CodingStandardsSection() {
  return (
    <div style={sectionStyle}>
      <h2 style={sectionTitleStyle}>Coding Standards</h2>
      <div style={cardStyle}>
        <ul style={{ margin: 0, paddingLeft: '20px', lineHeight: '2' }}>
          <li><strong>TypeScript strict mode</strong> — all files must have strict type checking enabled</li>
          <li><strong>CSS custom properties only</strong> — no hardcoded color, spacing, or typography values</li>
          <li><strong>No hardcoded values</strong> — use design tokens for all visual properties</li>
          <li><strong>Accessibility first</strong> — keyboard navigation, ARIA labels, focus management</li>
          <li><strong>Responsive design</strong> — all components must work at mobile, tablet, and desktop</li>
        </ul>
      </div>
    </div>
  );
}

function ContributionGuideSection() {
  return (
    <div style={sectionStyle}>
      <h2 style={sectionTitleStyle}>Contribution Guide</h2>
      <div style={cardStyle}>
        <ol style={{ margin: 0, paddingLeft: '20px', lineHeight: '2' }}>
          <li>Identify the need and get approval from the DS team</li>
          <li>Create a Figma spec with all variants, states, and tokens</li>
          <li>Implement the component in <code style={{ fontFamily: 'var(--font-mono)' }}>src/design-system/components/</code></li>
          <li>Add documentation in <code style={{ fontFamily: 'var(--font-mono)' }}>docs/design-system/components/</code></li>
          <li>Register in <code style={{ fontFamily: 'var(--font-mono)' }}>componentManifest.ts</code></li>
          <li>Add preview page in <code style={{ fontFamily: 'var(--font-mono)' }}>playground/pages/</code></li>
          <li>Write unit tests and accessibility tests</li>
          <li>Submit a PR and go through the review pipeline</li>
        </ol>
      </div>
    </div>
  );
}

function ReviewProcessSection() {
  return (
    <div style={sectionStyle}>
      <h2 style={sectionTitleStyle}>Review Process</h2>
      <div style={cardStyle}>
        <p style={{ marginTop: 0 }}>The 8-stage pipeline for component approval:</p>
        <table style={tableStyle}>
          <thead>
            <tr>
              <th style={thStyle}>Stage</th>
              <th style={thStyle}>Owner</th>
              <th style={thStyle}>Description</th>
            </tr>
          </thead>
          <tbody>
            {pipelineSteps.map((s) => (
              <tr key={s.stage}>
                <td style={tdStyle}>{s.stage}</td>
                <td style={tdStyle}>{s.owner}</td>
                <td style={tdStyle}>{s.description}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

function ApprovalWorkflowSection() {
  return (
    <div style={sectionStyle}>
      <h2 style={sectionTitleStyle}>Approval Workflow</h2>
      <div style={cardStyle}>
        <table style={tableStyle}>
          <thead>
            <tr>
              <th style={thStyle}>Change Type</th>
              <th style={thStyle}>Approver</th>
            </tr>
          </thead>
          <tbody>
            {approvalWorkflow.map((a) => (
              <tr key={a.item}>
                <td style={tdStyle}>{a.item}</td>
                <td style={tdStyle}>{a.approver}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

function ReleaseNotesSection() {
  return (
    <div style={sectionStyle}>
      <h2 style={sectionTitleStyle}>Release Notes</h2>
      <div style={cardStyle}>
        <table style={tableStyle}>
          <thead>
            <tr>
              <th style={thStyle}>Version</th>
              <th style={thStyle}>Date</th>
              <th style={thStyle}>Notes</th>
            </tr>
          </thead>
          <tbody>
            {changelog.map((c) => (
              <tr key={c.version}>
                <td style={{ ...tdStyle, fontWeight: 'var(--weight-semibold)' }}>{c.version}</td>
                <td style={tdStyle}>{c.date}</td>
                <td style={tdStyle}>{c.notes}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default function DocumentationCenter() {
  return (
    <div style={containerStyle}>
      <h1 style={titleStyle}>Documentation Center</h1>
      <ArchitectureSection />
      <GuidelinesSection />
      <CodingStandardsSection />
      <ContributionGuideSection />
      <ReviewProcessSection />
      <ApprovalWorkflowSection />
      <ReleaseNotesSection />
    </div>
  );
}
