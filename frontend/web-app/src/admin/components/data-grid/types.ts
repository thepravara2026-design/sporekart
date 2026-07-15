export interface SortConfig {
  key: string;
  direction: 'asc' | 'desc';
}

export interface FilterConfig {
  id: string;
  type: 'dropdown' | 'checkbox' | 'radio' | 'dateRange' | 'price' | 'status' | 'category' | 'tag' | 'multiSelect' | 'boolean';
  label: string;
  value: string | string[] | [string, string] | boolean | null;
  options?: { label: string; value: string }[];
}

export interface ColumnConfig {
  key: string;
  header: string;
  visible: boolean;
  width?: string;
  pinned?: 'left' | 'right' | false;
  order: number;
  sortable?: boolean;
  filterable?: boolean;
  resizable?: boolean;
}

export interface QueryState {
  search: string;
  filters: FilterConfig[];
  sort: SortConfig[];
  page: number;
  pageSize: number;
  columnConfig: ColumnConfig[];
  selectedRows: Set<string>;
  viewId: string | null;
}

export interface DataGridProps<T = any> {
  columns: DataGridColumn<T>[];
  data: T[];
  total?: number;
  loading?: boolean;
  loadingRows?: number;
  emptyMessage?: string;
  emptyDescription?: string;
  pageSize?: number;
  pageSizeOptions?: number[];
  sortable?: boolean;
  filterable?: boolean;
  selectable?: boolean;
  searchable?: boolean;
  exportable?: boolean;
  resizableColumns?: boolean;
  stickyHeader?: boolean;
  stickyColumns?: number;
  rowKey?: string | ((row: T) => string);
  onRowClick?: (row: T) => void;
  cardViewBreakpoint?: number;
  renderCard?: (row: T) => React.ReactNode;
  virtual?: boolean;
  virtualRowHeight?: number;
  children?: React.ReactNode;
}

export interface DataGridColumn<T = any> {
  key: string;
  header: string;
  render?: (row: T, index: number) => React.ReactNode;
  sortable?: boolean;
  filterable?: boolean;
  filterType?: FilterConfig['type'];
  filterOptions?: { label: string; value: string }[];
  width?: string;
  minWidth?: string;
  maxWidth?: string;
  pinned?: 'left' | 'right' | false;
  resizable?: boolean;
  hidden?: boolean;
  align?: 'left' | 'center' | 'right';
  cellStyle?: React.CSSProperties;
  headerStyle?: React.CSSProperties;
}

export interface FilterOption {
  label: string;
  value: string;
  count?: number;
}

export interface SavedView {
  id: string;
  name: string;
  queryState: Partial<QueryState>;
  pinned?: boolean;
  recent?: boolean;
  default?: boolean;
}

export interface ExportFormat {
  label: string;
  value: 'csv' | 'excel' | 'pdf' | 'print';
  icon?: string;
}
