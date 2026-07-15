import React, { useState } from 'react';
import { Button } from '../../../design-system/components/core/Button';
import { Card } from '../../../design-system/components/composite/Card';
import { Table } from '../../../design-system/components/composite/Table';
import { EmptyState } from '../../../design-system/components/display/EmptyState';
import { Breadcrumb } from '../../../design-system/components/navigation/Breadcrumb';
import { Skeleton } from '../../../design-system/components/display/Skeleton';
import { CardSkeleton } from '../../../design-system/components/display/CardSkeleton';
import { Icon } from '../../../design-system/icons/Icon';
import { Textarea } from '../forms/Textarea';
import { NumberInput } from '../forms/NumberInput';
import { CurrencyInput } from '../forms/CurrencyInput';
import { EmailInput } from '../forms/EmailInput';
import { PhoneInput } from '../forms/PhoneInput';
import { TimePicker } from '../forms/TimePicker';
import { TagSelector } from '../forms/TagSelector';
import { PanelContainer } from '../data/PanelContainer';
import { NoResults } from '../data/NoResults';
import { Tabs } from '../navigation/Tabs';
import { Accordion } from '../navigation/Accordion';
import { Pagination } from '../navigation/Pagination';
import { StatusBadge } from '../status/StatusBadge';
import { PriorityBadge } from '../status/PriorityBadge';
import { OrderBadge } from '../status/OrderBadge';
import type { TableColumn } from '../../../design-system/components/composite/Table';

function Section({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <div style={{ marginBottom: 48 }}>
      <h2 style={{ fontSize: 'var(--text-h2)', margin: '0 0 var(--space-stack-md)', color: 'var(--color-text-primary)' }}>
        {title}
      </h2>
      <div style={{
        background: 'var(--color-bg-surface-default)',
        border: '1px solid var(--color-border-default)',
        borderRadius: 'var(--radius-card)',
        padding: 'var(--space-stack-lg) var(--space-page-x)',
      }}>
        {children}
      </div>
    </div>
  );
}

function VariantRow({ children }: { children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', gap: 'var(--space-inline-md)', flexWrap: 'wrap', alignItems: 'center', marginBottom: 'var(--space-stack-md)' }}>
      {children}
    </div>
  );
}

function RowLabel({ children }: { children: React.ReactNode }) {
  return (
    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', minWidth: 80, flexShrink: 0 }}>{children}</span>
  );
}

export function ButtonsPreview() {
  return (
    <div style={{ maxWidth: 960, margin: '0 auto', padding: 'var(--space-section-gap) var(--space-page-x)' }}>
      <h1 style={{ fontSize: 'var(--text-h1)', margin: '0 0 var(--space-stack-xs)' }}>Button System</h1>
      <p style={{ color: 'var(--color-text-secondary)', marginBottom: 'var(--space-section-gap)' }}>
        Enterprise button variants with loading, disabled, and icon states.
      </p>
      <Section title="Variants">
        <VariantRow><RowLabel>Primary</RowLabel><Button variant="primary">Primary</Button><Button variant="primary" loading>Loading</Button><Button variant="primary" disabled>Disabled</Button></VariantRow>
        <VariantRow><RowLabel>Secondary</RowLabel><Button variant="secondary">Secondary</Button><Button variant="secondary" loading>Loading</Button><Button variant="secondary" disabled>Disabled</Button></VariantRow>
        <VariantRow><RowLabel>Outline</RowLabel><Button variant="outline">Outline</Button><Button variant="outline" loading>Loading</Button><Button variant="outline" disabled>Disabled</Button></VariantRow>
        <VariantRow><RowLabel>Ghost</RowLabel><Button variant="ghost">Ghost</Button><Button variant="ghost" loading>Loading</Button><Button variant="ghost" disabled>Disabled</Button></VariantRow>
        <VariantRow><RowLabel>Destructive</RowLabel><Button variant="destructive">Danger</Button><Button variant="destructive" loading>Loading</Button><Button variant="destructive" disabled>Disabled</Button></VariantRow>
        <VariantRow><RowLabel>Success</RowLabel><Button variant="success">Success</Button><Button variant="success" loading>Loading</Button><Button variant="success" disabled>Disabled</Button></VariantRow>
        <VariantRow><RowLabel>Warning</RowLabel><Button variant="warning">Warning</Button><Button variant="warning" loading>Loading</Button><Button variant="warning" disabled>Disabled</Button></VariantRow>
      </Section>
      <Section title="Sizes">
        <VariantRow><RowLabel>Small</RowLabel><Button size="sm">Small</Button><Button size="sm" variant="secondary">Small</Button></VariantRow>
        <VariantRow><RowLabel>Medium</RowLabel><Button size="md">Medium</Button><Button size="md" variant="secondary">Medium</Button></VariantRow>
        <VariantRow><RowLabel>Large</RowLabel><Button size="lg">Large</Button><Button size="lg" variant="secondary">Large</Button></VariantRow>
      </Section>
      <Section title="With Icons">
        <VariantRow>
          <Button leftIcon={<Icon name="plus" size={16} color="currentColor" />}>Add User</Button>
          <Button rightIcon={<Icon name="arrow-right" size={16} color="currentColor" />}>Continue</Button>
          <Button variant="secondary" leftIcon={<Icon name="download" size={16} color="currentColor" />}>Export</Button>
        </VariantRow>
      </Section>
      <Section title="Dark Theme">
        <div style={{ padding: 'var(--space-stack-md) var(--space-page-x)', background: '#1a1a2e', color: '#e0e0e0', borderRadius: 'var(--radius-card)' }}>
          <VariantRow><Button variant="primary">Primary</Button><Button variant="secondary">Secondary</Button><Button variant="outline">Outline</Button><Button variant="ghost">Ghost</Button></VariantRow>
          <p style={{ fontSize: 'var(--text-caption)', margin: 0 }}>All button variants inherit theme tokens. Dark mode is handled by the design system theme provider.</p>
        </div>
      </Section>
    </div>
  );
}

export function FormsPreview() {
  const [tags, setTags] = useState<string[]>(['react', 'typescript']);
  return (
    <div style={{ maxWidth: 960, margin: '0 auto', padding: 'var(--space-section-gap) var(--space-page-x)' }}>
      <h1 style={{ fontSize: 'var(--text-h1)', margin: '0 0 var(--space-stack-xs)' }}>Form Components</h1>
      <p style={{ color: 'var(--color-text-secondary)', marginBottom: 'var(--space-section-gap)' }}>
        Enterprise form components with label, error, helper text states.
      </p>
      <Section title="Text Fields">
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-component-gap)' }}>
          <Textarea label="Description" placeholder="Enter a description..." helperText="Max 500 characters" maxLength={500} />
          <Textarea label="With Error" value="Bad data" error="This field has an error" />
          <Textarea label="Disabled" value="Read only content" disabled />
        </div>
      </Section>
      <Section title="Numeric Inputs">
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: 'var(--space-component-gap)' }}>
          <NumberInput label="Quantity" min={0} max={100} helperText="Between 0 and 100" />
          <CurrencyInput label="Price" currency="USD" value={29.99} />
          <CurrencyInput label="Price (EUR)" currency="EUR" value={25.50} />
        </div>
      </Section>
      <Section title="Contact Inputs">
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-component-gap)' }}>
          <EmailInput label="Email Address" placeholder="user@example.com" />
          <PhoneInput label="Phone Number" placeholder="+1 (555) 000-0000" />
          <EmailInput label="With Error" value="invalid" error="Please enter a valid email" />
          <PhoneInput label="Disabled" value="+1 (555) 123-4567" disabled />
        </div>
      </Section>
      <Section title="Time Picker">
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-component-gap)' }}>
          <TimePicker label="Start Time" />
          <TimePicker label="End Time" helperText="Business hours only" />
        </div>
      </Section>
      <Section title="Tag Selector">
        <div style={{ maxWidth: 480 }}>
          <TagSelector
            label="Product Tags"
            tags={tags}
            onChange={setTags}
            suggestions={['react', 'typescript', 'javascript', 'node', 'python', 'rust', 'go', 'design-system', 'components', 'testing']}
            helperText="Type to add tags. Press Enter to confirm."
            maxTags={8}
          />
        </div>
      </Section>
    </div>
  );
}

export function CardsPreview() {
  return (
    <div style={{ maxWidth: 960, margin: '0 auto', padding: 'var(--space-section-gap) var(--space-page-x)' }}>
      <h1 style={{ fontSize: 'var(--text-h1)', margin: '0 0 var(--space-stack-xs)' }}>Card Components</h1>
      <p style={{ color: 'var(--color-text-secondary)', marginBottom: 'var(--space-section-gap)' }}>
        Enterprise card variants with elevated, outlined, and hoverable states.
      </p>
      <Section title="Card Variants">
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 'var(--space-component-gap)' }}>
          <Card variant="elevated" padding="lg">
            <h3 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h4)' }}>Elevated</h3>
            <p style={{ margin: 0, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Shadow-based elevation card.</p>
          </Card>
          <Card variant="outlined" padding="lg">
            <h3 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h4)' }}>Outlined</h3>
            <p style={{ margin: 0, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Border-only card variant.</p>
          </Card>
          <Card variant="default" padding="lg">
            <h3 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h4)' }}>Default</h3>
            <p style={{ margin: 0, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Default card variant.</p>
          </Card>
        </div>
      </Section>
      <Section title="Hoverable Cards">
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 'var(--space-component-gap)' }}>
          <Card variant="elevated" padding="lg" hoverable>
            <h3 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h4)' }}>Hoverable 1</h3>
            <p style={{ margin: 0, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Hover me for lift effect.</p>
          </Card>
          <Card variant="outlined" padding="lg" hoverable>
            <h3 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h4)' }}>Hoverable 2</h3>
            <p style={{ margin: 0, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Hover me for lift effect.</p>
          </Card>
          <Card variant="elevated" padding="lg" hoverable>
            <div style={{ width: 40, height: 40, borderRadius: 'var(--radius-md)', background: 'var(--color-bg-primary-weak)', marginBottom: 'var(--space-stack-sm)' }} />
            <h3 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h4)' }}>With Icon</h3>
            <p style={{ margin: 0, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Card with icon placeholder.</p>
          </Card>
        </div>
      </Section>
      <Section title="Panel Container">
        <PanelContainer title="Panel Title" description="Panel with title, description, and footer" actions={<Button size="sm">Action</Button>} footer="Panel footer content" variant="elevated" padding="lg">
          <p style={{ margin: 0, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>This is the panel body content. Panels provide structured layout with header, body, and footer sections.</p>
        </PanelContainer>
      </Section>
    </div>
  );
}

export function TablesPreview() {
  type TableRow = { name: string; role: string; status: string };
  const columns: TableColumn<TableRow>[] = [
    { key: 'name', header: 'Name', render: (row) => row.name, width: '40%' },
    { key: 'role', header: 'Role', render: (row) => row.role, width: '30%' },
    { key: 'status', header: 'Status', render: (row) => row.status, width: '30%' },
  ];
  const data: TableRow[] = [
    { name: 'Alice Johnson', role: 'Administrator', status: 'Active' },
    { name: 'Bob Smith', role: 'Support Agent', status: 'Active' },
    { name: 'Carol White', role: 'Business Owner', status: 'Inactive' },
    { name: 'David Brown', role: 'Content Manager', status: 'Active' },
  ];
  return (
    <div style={{ maxWidth: 960, margin: '0 auto', padding: 'var(--space-section-gap) var(--space-page-x)' }}>
      <h1 style={{ fontSize: 'var(--text-h1)', margin: '0 0 var(--space-stack-xs)' }}>Table Components</h1>
      <p style={{ color: 'var(--color-text-secondary)', marginBottom: 'var(--space-section-gap)' }}>
        Enterprise table with sorting, pagination, loading, and empty states.
      </p>
      <Section title="Default Table">
        <Table columns={columns} data={data} variant="default" />
      </Section>
      <Section title="Striped Table">
        <Table columns={columns} data={data} variant="striped" />
      </Section>
      <Section title="Bordered Table">
        <Table columns={columns} data={data} variant="bordered" />
      </Section>
      <Section title="Loading State">
        <Table columns={columns} data={data} loading />
      </Section>
      <Section title="Empty State">
        <Table columns={columns} data={[]} empty emptyMessage="No data available" />
      </Section>
      <Section title="With Pagination">
        <Table
          columns={columns}
          data={Array.from({ length: 25 }, (_, i) => ({
            name: `User ${i + 1}`,
            role: i % 3 === 0 ? 'Admin' : i % 3 === 1 ? 'Support' : 'Manager',
            status: i % 4 === 0 ? 'Inactive' : 'Active',
          }))}
          pagination={{ page: 1, pageSize: 5, total: 25, onPageChange: () => {} }}
        />
      </Section>
    </div>
  );
}

export function DialogsPreview() {
  return (
    <div style={{ maxWidth: 960, margin: '0 auto', padding: 'var(--space-section-gap) var(--space-page-x)' }}>
      <h1 style={{ fontSize: 'var(--text-h1)', margin: '0 0 var(--space-stack-xs)' }}>Feedback & Dialog Components</h1>
      <p style={{ color: 'var(--color-text-secondary)', marginBottom: 'var(--space-section-gap)' }}>
        Enterprise feedback components: toasts, alerts, empty states, and dialogs.
      </p>
      <Section title="Empty States">
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: 'var(--space-component-gap)' }}>
          <EmptyState type="noData" title="No items found" description="Get started by creating your first item." action={{ label: 'Create Item', onClick: () => {} }} />
          <EmptyState type="searchEmpty" title="No results" description="Try adjusting your search terms." />
          <EmptyState type="maintenance" title="Something went wrong" description="Please try again later." action={{ label: 'Retry', onClick: () => {} }} />
          <NoResults title="Custom empty" description="Custom no-results component with icon." />
        </div>
      </Section>
      <Section title="Alerts">
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-sm)' }}>
          <div style={{ padding: '12px 16px', borderRadius: 'var(--radius-md)', background: 'var(--color-bg-success-weak)', color: 'var(--color-text-success)', fontSize: 'var(--text-body)' }}>&#10003; Success alert: Operation completed successfully.</div>
          <div style={{ padding: '12px 16px', borderRadius: 'var(--radius-md)', background: 'var(--color-bg-warning-weak)', color: 'var(--color-text-warning)', fontSize: 'var(--text-body)' }}>&#9888; Warning alert: Please review before proceeding.</div>
          <div style={{ padding: '12px 16px', borderRadius: 'var(--radius-md)', background: 'var(--color-bg-danger-weak)', color: 'var(--color-text-danger)', fontSize: 'var(--text-body)' }}>&#10007; Error alert: An error occurred.</div>
          <div style={{ padding: '12px 16px', borderRadius: 'var(--radius-md)', background: 'var(--color-bg-info-weak)', color: 'var(--color-text-info)', fontSize: 'var(--text-body)' }}>&#8505; Info alert: This is informational.</div>
        </div>
      </Section>
    </div>
  );
}

export function LoadingPreview() {
  return (
    <div style={{ maxWidth: 960, margin: '0 auto', padding: 'var(--space-section-gap) var(--space-page-x)' }}>
      <h1 style={{ fontSize: 'var(--text-h1)', margin: '0 0 var(--space-stack-xs)' }}>Loading & Skeleton System</h1>
      <p style={{ color: 'var(--color-text-secondary)', marginBottom: 'var(--space-section-gap)' }}>
        Enterprise loading placeholders with shimmer animation.
      </p>
      <Section title="Skeleton Variants">
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-md)' }}>
          <div style={{ display: 'flex', gap: 'var(--space-inline-lg)' }}>
            <div style={{ flex: 1 }}><Skeleton width="60%" height={20} /><div style={{ height: 8 }} /><Skeleton width="40%" height={14} /></div>
            <div style={{ flex: 1 }}><Skeleton width="80%" height={20} /><div style={{ height: 8 }} /><Skeleton width="50%" height={14} /></div>
          </div>
          <div style={{ display: 'flex', gap: 'var(--space-inline-lg)' }}>
            <Skeleton width={80} height={80} variant="circular" />
            <div style={{ flex: 1 }}><Skeleton width="70%" height={18} /><div style={{ height: 6 }} /><Skeleton width="50%" height={14} /></div>
          </div>
        </div>
      </Section>
      <Section title="Card Skeleton">
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 'var(--space-component-gap)' }}>
          <CardSkeleton />
          <CardSkeleton />
          <CardSkeleton />
        </div>
      </Section>
      <Section title="Table Skeleton">
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-sm)' }}>
          <Skeleton width="100%" height={40} />
          <Skeleton width="100%" height={32} /><Skeleton width="100%" height={32} /><Skeleton width="100%" height={32} />
        </div>
      </Section>
      <Section title="Form Skeleton">
        <div style={{ maxWidth: 480 }}><div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}><Skeleton width="30%" height={14} /><Skeleton width="100%" height={40} /><Skeleton width="30%" height={14} /><Skeleton width="100%" height={40} /><Skeleton width={120} height={36} /></div></div>
      </Section>
    </div>
  );
}

export function BadgesPreview() {
  return (
    <div style={{ maxWidth: 960, margin: '0 auto', padding: 'var(--space-section-gap) var(--space-page-x)' }}>
      <h1 style={{ fontSize: 'var(--text-h1)', margin: '0 0 var(--space-stack-xs)' }}>Status & Badge Components</h1>
      <p style={{ color: 'var(--color-text-secondary)', marginBottom: 'var(--space-section-gap)' }}>
        Enterprise status badges, priority badges, and order status badges.
      </p>
      <Section title="Status Badges">
        <VariantRow><RowLabel>Default</RowLabel><StatusBadge status="Draft" variant="default" /><StatusBadge status="Draft" variant="default" size="md" /></VariantRow>
        <VariantRow><RowLabel>Success</RowLabel><StatusBadge status="Active" variant="success" /><StatusBadge status="Active" variant="success" pulse /></VariantRow>
        <VariantRow><RowLabel>Warning</RowLabel><StatusBadge status="Pending" variant="warning" /><StatusBadge status="Pending" variant="warning" pulse /></VariantRow>
        <VariantRow><RowLabel>Danger</RowLabel><StatusBadge status="Failed" variant="danger" /><StatusBadge status="Failed" variant="danger" pulse /></VariantRow>
        <VariantRow><RowLabel>Info</RowLabel><StatusBadge status="In Progress" variant="info" /></VariantRow>
        <VariantRow><RowLabel>Neutral</RowLabel><StatusBadge status="Archived" variant="neutral" /></VariantRow>
      </Section>
      <Section title="Priority Badges">
        <VariantRow><PriorityBadge priority="critical" /><PriorityBadge priority="high" /><PriorityBadge priority="medium" /><PriorityBadge priority="low" /></VariantRow>
      </Section>
      <Section title="Order Status Badges">
        <VariantRow>
          <OrderBadge status="pending" /><OrderBadge status="confirmed" /><OrderBadge status="processing" />
          <OrderBadge status="shipped" /><OrderBadge status="delivered" /><OrderBadge status="cancelled" />
          <OrderBadge status="returned" /><OrderBadge status="refunded" />
        </VariantRow>
      </Section>
    </div>
  );
}

export function TabsPreview() {
  const [activeTab, setActiveTab] = useState('tab1');
  const tabs = [
    { id: 'tab1', label: 'Overview', badge: 12 },
    { id: 'tab2', label: 'Details', icon: <Icon name="settings" size={14} color="currentColor" /> },
    { id: 'tab3', label: 'Activity', badge: 5 },
    { id: 'tab4', label: 'Disabled', disabled: true },
  ];
  return (
    <div style={{ maxWidth: 960, margin: '0 auto', padding: 'var(--space-section-gap) var(--space-page-x)' }}>
      <h1 style={{ fontSize: 'var(--text-h1)', margin: '0 0 var(--space-stack-xs)' }}>Navigation Helpers</h1>
      <p style={{ color: 'var(--color-text-secondary)', marginBottom: 'var(--space-section-gap)' }}>
        Enterprise tabs, accordion, pagination components.
      </p>
      <Section title="Tabs">
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-md)' }}>
          <Tabs tabs={tabs} activeId={activeTab} onChange={setActiveTab} variant="underline" />
          <Tabs tabs={tabs} activeId={activeTab} onChange={setActiveTab} variant="pills" />
          <Tabs tabs={tabs} activeId={activeTab} onChange={setActiveTab} variant="buttons" size="sm" />
        </div>
      </Section>
      <Section title="Accordion">
        <Accordion
          items={[
            { id: 'a1', title: 'Section 1', content: <p style={{ margin: 0 }}>Content for section 1. This panel is expandable.</p> },
            { id: 'a2', title: 'Section 2', content: <p style={{ margin: 0 }}>Content for section 2 with more detailed information.</p>, icon: <Icon name="info" size={14} color="currentColor" /> },
            { id: 'a3', title: 'Section 3 (Disabled)', content: <p style={{ margin: 0 }}>This content is not accessible.</p>, disabled: true },
          ]}
          defaultOpenIds={['a1']}
        />
      </Section>
      <Section title="Pagination">
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-md)' }}>
          <Pagination page={1} pageSize={10} total={100} onPageChange={() => {}} />
          <Pagination page={5} pageSize={20} total={250} onPageChange={() => {}} onPageSizeChange={() => {}} />
          <Pagination page={12} pageSize={10} total={150} onPageChange={() => {}} siblingCount={2} />
        </div>
      </Section>
      <Section title="Breadcrumb">
        <Breadcrumb crumbs={[
          { label: 'Admin', href: '/admin/dashboard' },
          { label: 'Settings', href: '/admin/settings' },
          { label: 'Security' },
        ]} />
      </Section>
    </div>
  );
}

export function ComponentsIndex() {
  const categories = [
    { id: 'buttons', label: 'Buttons', desc: 'Primary, secondary, outline, ghost, destructive, success, warning — with loading, disabled, and icon states.' },
    { id: 'forms', label: 'Forms', desc: 'Textarea, NumberInput, CurrencyInput, EmailInput, PhoneInput, TimePicker, TagSelector.' },
    { id: 'cards', label: 'Cards', desc: 'Card variants (elevated, outlined, flat), hoverable cards, PanelContainer.' },
    { id: 'tables', label: 'Tables', desc: 'Table with sorting, pagination, loading, empty, striped, bordered variants.' },
    { id: 'dialogs', label: 'Dialogs', desc: 'Empty states, alerts (success, warning, danger, info), confirmation dialogs.' },
    { id: 'loading', label: 'Loading', desc: 'Skeleton, CardSkeleton, table skeleton, form skeleton with shimmer animation.' },
    { id: 'badges', label: 'Badges', desc: 'StatusBadge, PriorityBadge, OrderBadge — with dot indicators, colors, pulse animation.' },
    { id: 'tabs', label: 'Tabs & Nav', desc: 'Tabs (underline, pills, buttons), Accordion, Pagination, Breadcrumb.' },
  ];
  return (
    <div style={{ maxWidth: 960, margin: '0 auto', padding: 'var(--space-section-gap) var(--space-page-x)' }}>
      <h1 style={{ fontSize: 'var(--text-h1)', margin: '0 0 var(--space-stack-xs)' }}>Admin Component Library</h1>
      <p style={{ color: 'var(--color-text-secondary)', marginBottom: 'var(--space-section-gap)' }}>
        Enterprise-grade reusable admin components. Browse by category below.
      </p>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {categories.map((cat) => (
          <Card key={cat.id} variant="elevated" padding="lg" hoverable>
            <h3 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h4)' }}>{cat.label}</h3>
            <p style={{ margin: 0, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{cat.desc}</p>
          </Card>
        ))}
      </div>
    </div>
  );
}
