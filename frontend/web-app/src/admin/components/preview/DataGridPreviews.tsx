import React from 'react';
import { DataGrid } from '../data-grid/DataGrid';
import { GlobalSearch } from '../search/GlobalSearch';
import { SearchBar } from '../search/SearchBar';
import { FilterBar } from '../filters/FilterBar';
import { DataGridProvider } from '../data-grid/DataGridProvider';
import { DataGridPagination } from '../table/DataGridPagination';
import { EnterpriseTable } from '../table/EnterpriseTable';
import type { DataGridColumn } from '../data-grid/types';
import './data-grid-preview.css';

function Section({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <div className="dg-preview-section">
      <h2>{title}</h2>
      {children}
    </div>
  );
}

function Frame({ children, dark }: { children: React.ReactNode; dark?: boolean }) {
  return (
    <div className={`dg-preview-frame ${dark ? 'dg-preview-frame--dark' : ''}`}>
      {children}
    </div>
  );
}

function FrameLabel({ label }: { label: string }) {
  return (
    <div
      style={{
        fontSize: 'var(--text-caption)',
        color: 'var(--color-text-tertiary)',
        textTransform: 'uppercase',
        letterSpacing: '0.05em',
        marginBottom: 8,
        fontWeight: 600,
      }}
    >
      {label}
    </div>
  );
}

function DeviceFrame({ label, className, children }: { label: string; className: string; children: React.ReactNode }) {
  return (
    <div style={{ marginBottom: 24 }}>
      <FrameLabel label={label} />
      <div className={className}>{children}</div>
    </div>
  );
}

// ─── Sample Data ────────────────────────────────────────────────────────────

interface UserRecord {
  id: number;
  name: string;
  email: string;
  role: string;
  status: string;
  department: string;
  joinDate: string;
  salary: number;
  rating: number;
}

const sampleColumns: DataGridColumn<UserRecord>[] = [
  { key: 'id', header: 'ID', width: '60px', sortable: true, filterType: 'dropdown', filterOptions: [] },
  { key: 'name', header: 'Name', sortable: true, filterable: true, filterType: 'dropdown', width: '180px' },
  { key: 'email', header: 'Email', sortable: true, width: '220px' },
  { key: 'role', header: 'Role', sortable: true, filterable: true, filterType: 'dropdown', filterOptions: [
    { label: 'Admin', value: 'admin' },
    { label: 'Editor', value: 'editor' },
    { label: 'Viewer', value: 'viewer' },
    { label: 'Manager', value: 'manager' },
  ]},
  { key: 'status', header: 'Status', sortable: true, filterable: true, filterType: 'dropdown', filterOptions: [
    { label: 'Active', value: 'active' },
    { label: 'Inactive', value: 'inactive' },
    { label: 'Pending', value: 'pending' },
    { label: 'Suspended', value: 'suspended' },
  ]},
  { key: 'department', header: 'Department', sortable: true, filterable: true, filterType: 'multiSelect', filterOptions: [
    { label: 'Engineering', value: 'engineering' },
    { label: 'Marketing', value: 'marketing' },
    { label: 'Sales', value: 'sales' },
    { label: 'Support', value: 'support' },
    { label: 'HR', value: 'hr' },
  ]},
  { key: 'joinDate', header: 'Join Date', sortable: true, filterable: true, filterType: 'dateRange', width: '140px' },
  { key: 'salary', header: 'Salary', sortable: true, render: (row) => `$${row.salary.toLocaleString()}`, width: '120px' },
  { key: 'rating', header: 'Rating', sortable: true, render: (row) => `${row.rating}/5`, width: '80px' },
];

const sampleData: UserRecord[] = Array.from({ length: 50 }, (_, i) => ({
  id: i + 1,
  name: `User ${i + 1}`,
  email: `user${i + 1}@sporekart.com`,
  role: ['admin', 'editor', 'viewer', 'manager'][i % 4],
  status: ['active', 'inactive', 'pending', 'suspended'][i % 4],
  department: ['engineering', 'marketing', 'sales', 'support', 'hr'][i % 5],
  joinDate: new Date(2020, i % 12, (i % 28) + 1).toISOString().split('T')[0],
  salary: 40000 + i * 1500,
  rating: (i % 5) + 1,
}));

const emptyData: UserRecord[] = [];

const largeData: UserRecord[] = Array.from({ length: 250 }, (_, i) => ({
  id: i + 1,
  name: `Employee ${i + 1}`,
  email: `emp${i + 1}@sporekart.com`,
  role: ['admin', 'editor', 'viewer', 'manager'][i % 4],
  status: ['active', 'inactive', 'pending', 'suspended'][i % 4],
  department: ['engineering', 'marketing', 'sales', 'support', 'hr'][i % 5],
  joinDate: new Date(2019, i % 12, (i % 28) + 1).toISOString().split('T')[0],
  salary: 35000 + i * 1200,
  rating: (i % 5) + 1,
}));

// ─── /preview/admin/data-grid ───────────────────────────────────────────────

export function DataGridPreview() {
  return (
    <div style={{ maxWidth: 1200, margin: '0 auto', padding: 'var(--space-section-gap) var(--space-page-x)' }}>
      <h1 style={{ fontSize: 'var(--text-h1)', margin: '0 0 var(--space-stack-xs)' }}>Enterprise Data Grid</h1>
      <p style={{ color: 'var(--color-text-secondary)', marginBottom: 'var(--space-section-gap)' }}>
        Full-featured data grid with search, filters, sorting, pagination, column management, export, and saved views.
      </p>

      <Section title="Desktop">
        <Frame>
          <DataGrid
            columns={sampleColumns}
            data={sampleData}
            selectable
            sortable
            filterable
            searchable
            exportable
            resizableColumns
            stickyHeader
          />
        </Frame>
      </Section>

      <Section title="Tablet">
        <DeviceFrame label="768px viewport" className="dg-preview-tablet">
          <Frame>
            <DataGrid
              columns={sampleColumns}
              data={sampleData.slice(0, 10)}
              selectable
              sortable
              filterable
              searchable
            />
          </Frame>
        </DeviceFrame>
      </Section>

      <Section title="Mobile">
        <DeviceFrame label="375px viewport" className="dg-preview-mobile">
          <Frame>
            <DataGrid
              columns={sampleColumns}
              data={sampleData.slice(0, 5)}
              selectable
              sortable
              filterable
              searchable
            />
          </Frame>
        </DeviceFrame>
      </Section>

      <Section title="Dark Theme">
        <Frame dark>
          <DataGrid
            columns={sampleColumns}
            data={sampleData.slice(0, 5)}
            selectable
            sortable
            filterable
            searchable
          />
        </Frame>
      </Section>

      <Section title="Large Dataset (250 records)">
        <Frame>
          <DataGrid
            columns={sampleColumns}
            data={largeData}
            selectable
            sortable
            filterable
            searchable
          />
        </Frame>
      </Section>

      <Section title="Empty State">
        <Frame>
          <DataGrid
            columns={sampleColumns}
            data={emptyData}
            selectable
            sortable
            filterable
            searchable
            emptyMessage="No users found"
            emptyDescription="Try adjusting your search or filters to find what you're looking for."
          />
        </Frame>
      </Section>

      <Section title="Loading State">
        <Frame>
          <DataGrid
            columns={sampleColumns}
            data={[]}
            loading
            selectable
            sortable
            filterable
            searchable
          />
        </Frame>
      </Section>

      <Section title="Accessibility">
        <Frame>
          <ul style={{ color: 'var(--color-text-secondary)', lineHeight: 1.8 }}>
            <li>All interactive elements are keyboard navigable</li>
            <li>Sortable columns announce sort state via <code>aria-sort</code></li>
            <li>Pagination uses <code>role="navigation"</code> and <code>aria-label</code></li>
            <li>Row selection checkboxes have <code>aria-label</code></li>
            <li>Filter controls have associated <code>aria-label</code> or <code>&lt;label&gt;</code></li>
            <li>Context menu items are focusable</li>
            <li>Loading skeletons use reduced-motion media query</li>
          </ul>
        </Frame>
      </Section>

      <Section title="Performance">
        <Frame>
          <ul style={{ color: 'var(--color-text-secondary)', lineHeight: 1.8 }}>
            <li>URL-synced query state with debounced updates (150ms)</li>
            <li>Memoized column filtering and sorting calculations</li>
            <li>Stable callback references via <code>useStableCallback</code> to minimize re-renders</li>
            <li>Pagination prevents DOM rendering of all rows at once</li>
            <li>Lazy-loaded preview components</li>
            <li>CSS animation for skeleton loading instead of JS-driven</li>
          </ul>
        </Frame>
      </Section>
    </div>
  );
}

// ─── /preview/admin/search ──────────────────────────────────────────────────

export function SearchPreview() {
  const [searchValue, setSearchValue] = React.useState('');
  const setGlobalQuery = React.useCallback((_q: string) => {
    // Global search handler
  }, []);

  const suggestions = [
    { id: '1', label: 'Dashboard', category: 'Pages', icon: 'layout' },
    { id: '2', label: 'User Management', category: 'Pages', icon: 'users' },
    { id: '3', label: 'Product Catalog', category: 'Pages', icon: 'package' },
    { id: '4', label: 'Order Management', category: 'Pages', icon: 'shopping-cart' },
    { id: '5', label: 'Analytics', category: 'Pages', icon: 'bar-chart' },
    { id: '6', label: 'Create Product', category: 'Actions', icon: 'plus' },
    { id: '7', label: 'Export Report', category: 'Actions', icon: 'download' },
    { id: '8', label: 'Invite User', category: 'Actions', icon: 'user-plus' },
  ];

  const recentSearches = ['Dashboard settings', 'User permissions', 'Revenue report'];

  return (
    <div style={{ maxWidth: 960, margin: '0 auto', padding: 'var(--space-section-gap) var(--space-page-x)' }}>
      <h1 style={{ fontSize: 'var(--text-h1)', margin: '0 0 var(--space-stack-xs)' }}>Enterprise Search Framework</h1>
      <p style={{ color: 'var(--color-text-secondary)', marginBottom: 'var(--space-section-gap)' }}>
        Global search with suggestions, debounced page search, recent searches, and empty states.
      </p>

      <Section title="Global Search (Ctrl+K)">
        <Frame>
          <FrameLabel label="Desktop" />
          <GlobalSearch
            onSearch={setGlobalQuery}
            suggestions={suggestions}
            recentSearches={recentSearches}
          />
        </Frame>
        <Frame>
          <FrameLabel label="Tablet" />
          <div className="dg-preview-tablet">
            <GlobalSearch
              onSearch={setGlobalQuery}
              suggestions={suggestions}
              recentSearches={recentSearches}
            />
          </div>
        </Frame>
        <Frame>
          <FrameLabel label="Mobile" />
          <div className="dg-preview-mobile">
            <GlobalSearch
              onSearch={setGlobalQuery}
              suggestions={suggestions}
              recentSearches={recentSearches}
            />
          </div>
        </Frame>
        <Frame dark>
          <FrameLabel label="Dark Theme" />
          <GlobalSearch
            onSearch={setGlobalQuery}
            suggestions={suggestions}
            recentSearches={recentSearches}
          />
        </Frame>
      </Section>

      <Section title="Page Search (Debounced)">
        <Frame>
          <FrameLabel label="With instant flag" />
          <SearchBar
            value={searchValue}
            onChange={setSearchValue}
            placeholder="Instant search..."
            instant
          />
        </Frame>
        <Frame>
          <FrameLabel label="With debounce (300ms)" />
          <SearchBar
            value={searchValue}
            onChange={setSearchValue}
            placeholder="Debounced search..."
          />
        </Frame>
      </Section>

      <Section title="Empty State">
        <Frame>
          <span style={{ color: 'var(--color-text-secondary)' }}>
            GlobalSearch with no results for a query will show &ldquo;No results for...&rdquo;
          </span>
        </Frame>
      </Section>

      <Section title="Accessibility">
        <Frame>
          <ul style={{ color: 'var(--color-text-secondary)', lineHeight: 1.8 }}>
            <li>Search inputs have <code>aria-label</code></li>
            <li>Global search uses <code>role="combobox"</code> with <code>aria-expanded</code></li>
            <li>Results panel uses <code>role="listbox"</code> with <code>role="option"</code></li>
            <li>Keyboard navigation: Arrow keys, Enter, Escape, Ctrl+K</li>
            <li>Clear buttons have <code>aria-label</code></li>
          </ul>
        </Frame>
      </Section>

      <Section title="Performance">
        <Frame>
          <ul style={{ color: 'var(--color-text-secondary)', lineHeight: 1.8 }}>
            <li>Debounced onChange prevents excessive re-renders</li>
            <li>Instant mode for real-time filtering (small datasets)</li>
            <li>Memoized suggestion grouping</li>
            <li>Click-outside listener cleanup on unmount</li>
          </ul>
        </Frame>
      </Section>
    </div>
  );
}

// ─── /preview/admin/filter ──────────────────────────────────────────────────

export function FilterPreview() {
  const filterColumns = sampleColumns;

  return (
    <div style={{ maxWidth: 960, margin: '0 auto', padding: 'var(--space-section-gap) var(--space-page-x)' }}>
      <h1 style={{ fontSize: 'var(--text-h1)', margin: '0 0 var(--space-stack-xs)' }}>Enterprise Filter Framework</h1>
      <p style={{ color: 'var(--color-text-secondary)', marginBottom: 'var(--space-section-gap)' }}>
        Reusable filter components: dropdown, checkbox, radio, date range, tag, multi-select, and boolean filters.
      </p>

      <Section title="Desktop - All Filter Types">
        <Frame>
          <DataGridProvider columns={filterColumns} data={sampleData.slice(0, 5)}>
            <FilterBar />
          </DataGridProvider>
        </Frame>
      </Section>

      <Section title="Tablet">
        <div className="dg-preview-tablet">
          <Frame>
            <DataGridProvider columns={filterColumns} data={sampleData.slice(0, 5)}>
              <FilterBar />
            </DataGridProvider>
          </Frame>
        </div>
      </Section>

      <Section title="Mobile">
        <div className="dg-preview-mobile">
          <Frame>
            <DataGridProvider columns={filterColumns} data={sampleData.slice(0, 5)}>
              <FilterBar />
            </DataGridProvider>
          </Frame>
        </div>
      </Section>

      <Section title="Dark Theme">
        <Frame dark>
          <DataGridProvider columns={filterColumns} data={sampleData.slice(0, 5)}>
            <FilterBar />
          </DataGridProvider>
        </Frame>
      </Section>

      <Section title="Accessibility">
        <Frame>
          <ul style={{ color: 'var(--color-text-secondary)', lineHeight: 1.8 }}>
            <li>All filter controls have associated labels</li>
            <li>Dropdown filters use <code>aria-haspopup="listbox"</code> and <code>aria-expanded</code></li>
            <li>Checkbox and radio filters use native <code>&lt;input&gt;</code> with associated labels</li>
            <li>Date inputs have <code>aria-label</code></li>
            <li>Filter state announced via active filter count badge</li>
          </ul>
        </Frame>
      </Section>

      <Section title="Performance">
        <Frame>
          <ul style={{ color: 'var(--color-text-secondary)', lineHeight: 1.8 }}>
            <li>Filters reset to page 1 on change to prevent empty results</li>
            <li>Individual filter components prevent unnecessary parent re-renders</li>
            <li>Click-outside listeners cleanup on unmount</li>
            <li>Filter bar only renders when <code>filterable</code> is true</li>
          </ul>
        </Frame>
      </Section>
    </div>
  );
}

// ─── /preview/admin/pagination ──────────────────────────────────────────────

export function PaginationPreview() {
  const paginationColumns = sampleColumns;

  return (
    <div style={{ maxWidth: 960, margin: '0 auto', padding: 'var(--space-section-gap) var(--space-page-x)' }}>
      <h1 style={{ fontSize: 'var(--text-h1)', margin: '0 0 var(--space-stack-xs)' }}>Enterprise Pagination Framework</h1>
      <p style={{ color: 'var(--color-text-secondary)', marginBottom: 'var(--space-section-gap)' }}>
        Full-featured pagination with page numbers, ellipsis, page size selector, go-to-page, and keyboard navigation.
      </p>

      <Section title="Desktop - Full Pagination">
        <Frame>
          <DataGridProvider columns={paginationColumns} data={largeData}>
            <DataGridPagination />
          </DataGridProvider>
        </Frame>
      </Section>

      <Section title="Many Pages (100+ pages)">
        <Frame>
          <DataGridProvider columns={paginationColumns} data={largeData}>
            <DataGridPagination />
          </DataGridProvider>
        </Frame>
      </Section>

      <Section title="Single Page (few records)">
        <Frame>
          <DataGridProvider columns={paginationColumns} data={sampleData.slice(0, 3)}>
            <DataGridPagination />
          </DataGridProvider>
        </Frame>
      </Section>

      <Section title="Empty (0 records)">
        <Frame>
          <DataGridProvider columns={paginationColumns} data={[]}>
            <DataGridPagination />
          </DataGridProvider>
        </Frame>
      </Section>

      <Section title="Tablet">
        <div className="dg-preview-tablet">
          <Frame>
            <DataGridProvider columns={paginationColumns} data={largeData}>
              <DataGridPagination />
            </DataGridProvider>
          </Frame>
        </div>
      </Section>

      <Section title="Mobile">
        <div className="dg-preview-mobile">
          <Frame>
            <DataGridProvider columns={paginationColumns} data={largeData}>
              <DataGridPagination />
            </DataGridProvider>
          </Frame>
        </div>
      </Section>

      <Section title="Dark Theme">
        <Frame dark>
          <DataGridProvider columns={paginationColumns} data={largeData}>
            <DataGridPagination />
          </DataGridProvider>
        </Frame>
      </Section>

      <Section title="Accessibility">
        <Frame>
          <ul style={{ color: 'var(--color-text-secondary)', lineHeight: 1.8 }}>
            <li>Pagination uses <code>role="navigation"</code> with <code>aria-label="Pagination"</code></li>
            <li>Page buttons have <code>{'aria-label="Page {n}"'}</code></li>
            <li>Current page marked with <code>aria-current="page"</code></li>
            <li>First/Previous/Next/Last buttons have <code>aria-label</code></li>
            <li>Keyboard: ArrowLeft, ArrowRight, Home, End</li>
            <li>Page size selector has <code>aria-label</code></li>
            <li>Go-to-page input supports Enter key</li>
          </ul>
        </Frame>
      </Section>

      <Section title="Performance">
        <Frame>
          <ul style={{ color: 'var(--color-text-secondary)', lineHeight: 1.8 }}>
            <li>Page numbers memoized with <code>useMemo</code></li>
            <li>No DOM rendering of rows outside visible page</li>
            <li>Page size persisted in <code>localStorage</code></li>
            <li>Minimal re-renders via memoized callbacks</li>
            <li>Ellipsis prevents rendering hundreds of page buttons</li>
          </ul>
        </Frame>
      </Section>
    </div>
  );
}

// ─── /preview/admin/table ───────────────────────────────────────────────────

export function TablePreview() {
  return (
    <div style={{ maxWidth: 960, margin: '0 auto', padding: 'var(--space-section-gap) var(--space-page-x)' }}>
      <h1 style={{ fontSize: 'var(--text-h1)', margin: '0 0 var(--space-stack-xs)' }}>Enterprise Table Framework</h1>
      <p style={{ color: 'var(--color-text-secondary)', marginBottom: 'var(--space-section-gap)' }}>
        Data table with sorting, sticky headers, resizable columns, column management, bulk selection, context menu, and empty/loading states.
      </p>

      <Section title="Desktop - Full Table">
        <Frame>
          <DataGridProvider columns={sampleColumns} data={sampleData} selectable sortable stickyHeader resizableColumns>
            <EnterpriseTable />
          </DataGridProvider>
        </Frame>
      </Section>

      <Section title="Tablet (responsive)">
        <DeviceFrame label="768px" className="dg-preview-tablet">
          <Frame>
            <DataGridProvider columns={sampleColumns} data={sampleData.slice(0, 5)} selectable>
              <EnterpriseTable />
            </DataGridProvider>
          </Frame>
        </DeviceFrame>
      </Section>

      <Section title="Mobile (horizontal scroll)">
        <DeviceFrame label="375px" className="dg-preview-mobile">
          <Frame>
            <DataGridProvider columns={sampleColumns} data={sampleData.slice(0, 3)} selectable>
              <EnterpriseTable />
            </DataGridProvider>
          </Frame>
        </DeviceFrame>
      </Section>

      <Section title="Dark Theme">
        <Frame dark>
          <DataGridProvider columns={sampleColumns.slice(0, 4)} data={sampleData.slice(0, 5)} selectable>
            <EnterpriseTable />
          </DataGridProvider>
        </Frame>
      </Section>

      <Section title="Loading State">
        <Frame>
          <DataGridProvider columns={sampleColumns} data={[]} loading>
            <EnterpriseTable />
          </DataGridProvider>
        </Frame>
      </Section>

      <Section title="Empty State">
        <Frame>
          <DataGridProvider
            columns={sampleColumns}
            data={[]}
            emptyMessage="No records"
            emptyDescription="There are no records to display in this view."
          >
            <EnterpriseTable />
          </DataGridProvider>
        </Frame>
      </Section>

      <Section title="Large Dataset (250 records)">
        <Frame>
          <DataGridProvider columns={sampleColumns} data={largeData} selectable>
            <EnterpriseTable />
          </DataGridProvider>
        </Frame>
      </Section>

      <Section title="Accessibility">
        <Frame>
          <ul style={{ color: 'var(--color-text-secondary)', lineHeight: 1.8 }}>
            <li>Table headers use <code>aria-sort</code> for sortable columns</li>
            <li>Row checkboxes have <code>aria-label</code></li>
            <li>Select-all checkbox supports indeterminate state</li>
            <li>Keyboard navigation: arrow keys for pagination</li>
            <li>Context menu appears on right-click</li>
            <li>Loading skeleton uses reduced-motion CSS</li>
          </ul>
        </Frame>
      </Section>

      <Section title="Performance">
        <Frame>
          <ul style={{ color: 'var(--color-text-secondary)', lineHeight: 1.8 }}>
            <li>Column resize uses <code>document.addEventListener</code> to avoid re-render on each pixel</li>
            <li>Row background via inline styles (no CSS-in-JS runtime)</li>
            <li>Table body renders only visible rows via pagination</li>
            <li>Minimal React re-renders via memoized handlers</li>
            <li>CSS animations for loading skeletons</li>
          </ul>
        </Frame>
      </Section>
    </div>
  );
}
