import { useState } from 'react';

const chipVariants = ['default', 'primary', 'success', 'warning', 'danger', 'info', 'neutral'] as const;
const chipSizes = ['sm', 'md', 'lg'] as const;

const variantChipStyles: Record<string, React.CSSProperties> = {
  default: { background: 'var(--color-bg-subtle, #f3f4f6)', color: 'var(--color-text-primary)', border: '1px solid var(--color-border-default)' },
  primary: { background: 'var(--color-bg-primary-subtle, #eff6ff)', color: 'var(--color-brand, #2563eb)', border: '1px solid var(--color-brand, #2563eb)' },
  success: { background: 'var(--color-bg-success-subtle, #dcfce7)', color: 'var(--color-success, #16a34a)', border: '1px solid var(--color-success, #16a34a)' },
  warning: { background: 'var(--color-bg-warning-subtle, #fef3c7)', color: 'var(--color-warning, #f59e0b)', border: '1px solid var(--color-warning, #f59e0b)' },
  danger: { background: 'var(--color-bg-danger-subtle, #fee2e2)', color: 'var(--color-danger, #dc2626)', border: '1px solid var(--color-danger, #dc2626)' },
  info: { background: 'var(--color-bg-info-subtle, #e0f2fe)', color: 'var(--color-info, #0ea5e9)', border: '1px solid var(--color-info, #0ea5e9)' },
  neutral: { background: 'var(--color-bg-neutral-subtle, #f3f4f6)', color: 'var(--color-text-secondary)', border: '1px solid var(--color-border-default)' },
};

const sizeChipStyles: Record<string, React.CSSProperties> = {
  sm: { padding: '2px 8px', fontSize: 'var(--text-xs)', borderRadius: 'var(--radius-sm)' },
  md: { padding: '4px 12px', fontSize: 'var(--text-caption)', borderRadius: 'var(--radius-md)' },
  lg: { padding: '6px 16px', fontSize: 'var(--text-sm)', borderRadius: 'var(--radius-lg)' },
};

const Section = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
    <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{label}</h2>
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>{children}</div>
  </section>
);

const CardFrame = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
    <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', alignItems: 'center' }}>{children}</div>
  </div>
);

const XIcon = () => (<svg width="12" height="12" viewBox="0 0 12 12" fill="none"><path d="M2 2l8 8M10 2l-8 8" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/></svg>);

const Chip = ({ style, children, onClick, onRemove }: { style: React.CSSProperties; children: React.ReactNode; onClick?: () => void; onRemove?: () => void }) => (
  <span onClick={onClick} style={{ display: 'inline-flex', alignItems: 'center', gap: '4px', cursor: onClick ? 'pointer' : 'default', ...style }}>
    {children}
    {onRemove && (
      <span onClick={(e) => { e.stopPropagation(); onRemove(); }} style={{ cursor: 'pointer', display: 'flex', marginLeft: '2px' }}>
        <XIcon />
      </span>
    )}
  </span>
);

export default function ChipsPreview() {
  const [removableChips, setRemovableChips] = useState<string[]>(['Apple', 'Banana', 'Cherry', 'Date']);
  const [toggleChips, setToggleChips] = useState<Set<string>>(new Set(['Filter 1']));

  const toggleSelectable = (label: string) => {
    const next = new Set(toggleChips);
    if (next.has(label)) next.delete(label); else next.add(label);
    setToggleChips(next);
  };

  const removeChip = (label: string) => {
    setRemovableChips((prev) => prev.filter((c) => c !== label));
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Chips</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All chip variants</p>
      </div>

      <Section label="Filter Chips">
        <CardFrame label="Selected / unselected">
          <Chip style={{ ...variantChipStyles.primary, ...sizeChipStyles.md, fontWeight: 'var(--weight-medium)' }}>Active</Chip>
          <Chip style={{ ...variantChipStyles.default, ...sizeChipStyles.md, fontWeight: 'var(--weight-medium)' }}>Inactive</Chip>
          <Chip style={{ ...variantChipStyles.primary, ...sizeChipStyles.md, fontWeight: 'var(--weight-medium)' }}>Pending</Chip>
          <Chip style={{ ...variantChipStyles.default, ...sizeChipStyles.md, fontWeight: 'var(--weight-medium)' }}>Archived</Chip>
        </CardFrame>
      </Section>

      <Section label="Action Chips">
        <CardFrame label="Clickable">
          <Chip style={{ ...variantChipStyles.primary, ...sizeChipStyles.md, fontWeight: 'var(--weight-medium)' }} onClick={() => {}}>Add Filter</Chip>
          <Chip style={{ ...variantChipStyles.success, ...sizeChipStyles.md, fontWeight: 'var(--weight-medium)' }} onClick={() => {}}>Apply</Chip>
          <Chip style={{ ...variantChipStyles.warning, ...sizeChipStyles.md, fontWeight: 'var(--weight-medium)' }} onClick={() => {}}>Edit</Chip>
          <Chip style={{ ...variantChipStyles.danger, ...sizeChipStyles.md, fontWeight: 'var(--weight-medium)' }} onClick={() => {}}>Delete</Chip>
        </CardFrame>
      </Section>

      <Section label="Selectable Chips">
        <CardFrame label="Toggle behavior">
          {['Filter 1', 'Filter 2', 'Filter 3', 'Filter 4'].map((f) => (
            <Chip key={f} style={{ ...(toggleChips.has(f) ? variantChipStyles.primary : variantChipStyles.default), ...sizeChipStyles.md, fontWeight: 'var(--weight-medium)' }} onClick={() => toggleSelectable(f)}>{f}</Chip>
          ))}
        </CardFrame>
      </Section>

      <Section label="Removable Chips">
        <CardFrame label="With X button">
          {removableChips.map((chip) => (
            <Chip key={chip} style={{ ...variantChipStyles.default, ...sizeChipStyles.md, fontWeight: 'var(--weight-medium)' }} onRemove={() => removeChip(chip)}>{chip}</Chip>
          ))}
        </CardFrame>
      </Section>

      <Section label="Tag Chips">
        <CardFrame label="Read-only">
          {['react', 'typescript', 'css', 'node', 'graphql'].map((tag) => (
            <span key={tag} style={{ ...variantChipStyles.info, ...sizeChipStyles.sm, fontWeight: 'var(--weight-medium)' }}>{tag}</span>
          ))}
        </CardFrame>
      </Section>

      <Section label="All Variant Colors">
        <CardFrame label="All 7 variants">
          {chipVariants.map((v) => (
            <span key={v} style={{ ...variantChipStyles[v], ...sizeChipStyles.md, fontWeight: 'var(--weight-medium)', textTransform: 'capitalize' }}>{v}</span>
          ))}
        </CardFrame>
      </Section>

      <Section label="All Sizes">
        <CardFrame label="sm, md, lg">
          {chipSizes.map((s) => (
            <span key={s} style={{ ...variantChipStyles.primary, ...sizeChipStyles[s], fontWeight: 'var(--weight-medium)', textTransform: 'uppercase' }}>{s}</span>
          ))}
        </CardFrame>
      </Section>

      <Section label="Disabled State">
        <CardFrame label="Disabled chips">
          <span style={{ ...variantChipStyles.default, ...sizeChipStyles.md, fontWeight: 'var(--weight-medium)', opacity: 0.5, cursor: 'not-allowed' }}>Disabled</span>
          <span style={{ ...variantChipStyles.primary, ...sizeChipStyles.md, fontWeight: 'var(--weight-medium)', opacity: 0.5, cursor: 'not-allowed' }}>Disabled</span>
          <span style={{ ...variantChipStyles.success, ...sizeChipStyles.md, fontWeight: 'var(--weight-medium)', opacity: 0.5, cursor: 'not-allowed' }}>Disabled</span>
        </CardFrame>
      </Section>
    </div>
  );
}
