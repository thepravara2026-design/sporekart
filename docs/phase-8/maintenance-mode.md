# Maintenance Mode

## Architecture

Combined usage of operational states and system status components to handle maintenance scenarios. The `OperationalStateDisplay` component renders maintenance, updating, and server-unavailable states. The `SystemStatusPanel` provides real-time service health.

## Usage

```tsx
// Maintenance state
<OperationalStateDisplay state="maintenance" />

// With custom message and actions
<OperationalStateDisplay
  state="maintenance"
  config={{
    title: 'Scheduled Maintenance',
    description: 'The system is undergoing maintenance from 2:00 AM to 4:00 AM UTC.',
    action: { label: 'Check status page', onClick: handleStatus },
    secondaryAction: { label: 'Contact support', onClick: handleSupport },
  }}
/>

// System Updating
<OperationalStateDisplay state="system_updating" />

// Server Unavailable
<OperationalStateDisplay state="server_unavailable" />

// System Status Panel
<SystemStatusPanel services={services} lastUpdated="Just now" />
<SystemStatusPanel services={services} lastUpdated="Just now" compact />
```

## Status Banner

For top-of-page alerts:

```tsx
<div role="alert" style={{ display: 'flex', alignItems: 'center', gap: 12, padding: '12px 20px', borderRadius: 'var(--radius-lg)', background: 'var(--color-warning)15', border: '1px solid var(--color-warning)30', color: 'var(--color-warning)' }}>
  <Icon name="tool" size={20} />
  <div>
    <div style={{ fontWeight: 600 }}>Under Maintenance</div>
    <div style={{ fontSize: 'var(--text-caption)', opacity: 0.8 }}>Scheduled maintenance is in progress.</div>
  </div>
</div>
```

## Extension Points

- Integrate with real maintenance schedule API
- Automatically show/hide banners based on system status
- Route-level maintenance blocking via `OperationalStateDisplay` in layout
