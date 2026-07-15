# Operational States

## Architecture

Unified display component for all application-level states. Each state has a default icon, title, and description that can be overridden. Supports compact and full-size variants with optional action buttons.

```
OperationalStateDisplay
  ├─ state: OperationalStateType
  ├─ config?: Partial<OperationalStateConfig> (overrides defaults)
  └─ compact?: boolean

LoadingSkeleton
  ├─ variant: 'text' | 'card' | 'table' | 'sidebar'
  └─ lines?: number
```

## Usage

```tsx
<OperationalStateDisplay state="loading" />
<OperationalStateDisplay state="empty" config={{ title: 'No orders', action: { label: 'Create', onClick: handleCreate } }} />
<OperationalStateDisplay state="forbidden" compact />

<LoadingSkeleton variant="table" lines={5} />
<LoadingSkeleton variant="card" />
<LoadingSkeleton variant="sidebar" lines={8} />
<LoadingSkeleton variant="text" lines={3} />
```

## Available States

| Type | Default Title | Default Description |
|---|---|---|
| loading | Loading... | Please wait while we load your content. |
| empty | No data yet | There is nothing to display yet. |
| no_data | No results found | Try adjusting your filters or search query. |
| permission_denied | Permission Denied | You do not have permission to perform this action. |
| unauthorized | Unauthorized | Please sign in to access this page. |
| forbidden | Access Forbidden | You do not have access to this resource. |
| offline | You are offline | Check your internet connection and try again. |
| maintenance | Under Maintenance | This section is temporarily unavailable. |
| system_updating | System Updating | We are rolling out an update. |
| feature_disabled | Feature Disabled | This feature is currently disabled. |
| server_unavailable | Server Unavailable | Our servers are experiencing issues. |
| unexpected_error | Something went wrong | An unexpected error occurred. |

## Extension Points

- Add new states in `types.ts` and `defaultConfig.ts`
- Override any title/description/icon via the `config` prop
- Add custom actions with `config.action` and `config.secondaryAction`
