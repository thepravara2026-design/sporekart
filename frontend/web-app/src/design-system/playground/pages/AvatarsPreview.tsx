import { useState } from 'react';

const avatarSizes = ['xs', 'sm', 'md', 'lg', 'xl', '2xl'] as const;

const sizeMap: Record<string, number> = {
  xs: 24, sm: 32, md: 40, lg: 48, xl: 56, '2xl': 64,
};

const initialsColors = [
  '#2563eb', '#16a34a', '#dc2626', '#f59e0b', '#8b5cf6', '#ec4899', '#06b6d4', '#f97316',
];

const names = [
  'Alice Johnson', 'Bob Smith', 'Carol White', 'David Brown',
  'Eve Davis', 'Frank Miller', 'Grace Wilson', 'Hank Moore',
];

const Section = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
    <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{label}</h2>
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>{children}</div>
  </section>
);

const CardFrame = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
    <div style={{ display: 'flex', flexWrap: 'wrap', gap: '12px', alignItems: 'center' }}>{children}</div>
  </div>
);

const Avatar = ({ size, src, initials, color, status, onClick }: { size: string; src?: string; initials?: string; color?: string; status?: string; onClick?: () => void }) => {
  const dim = sizeMap[size];
  return (
    <div onClick={onClick} style={{ position: 'relative', width: dim, height: dim, cursor: onClick ? 'pointer' : 'default', flexShrink: 0 }}>
      {src ? (
        <div style={{ width: dim, height: dim, borderRadius: '50%', background: 'var(--color-bg-subtle, #f3f4f6)', display: 'flex', alignItems: 'center', justifyContent: 'center', overflow: 'hidden' }}>
          <div style={{ width: '100%', height: '100%', background: 'linear-gradient(135deg, #e5e7eb, #d1d5db)' }} />
        </div>
      ) : (
        <div style={{ width: dim, height: dim, borderRadius: '50%', background: color || 'var(--color-brand, #2563eb)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff', fontSize: dim * 0.4, fontWeight: 'var(--weight-semibold)' }}>
          {initials || '?'}
        </div>
      )}
      {status && (
        <span style={{ position: 'absolute', bottom: 0, right: 0, width: dim * 0.3, height: dim * 0.3, borderRadius: '50%', border: '2px solid var(--color-bg-surface-default)', background: status === 'online' ? 'var(--color-success, #16a34a)' : status === 'busy' ? 'var(--color-danger, #dc2626)' : status === 'away' ? 'var(--color-warning, #f59e0b)' : 'var(--color-text-tertiary)' }} />
      )}
    </div>
  );
};

export default function AvatarsPreview() {
  const [_status] = useState<'online' | 'offline' | 'busy' | 'away'>('online');
  const [clicked, setClicked] = useState(false);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Avatars</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Avatar types, sizes, groups</p>
      </div>

      <Section label="Image Avatars">
        <CardFrame label="Placeholder images">
          {avatarSizes.map((s) => (
            <Avatar key={s} size={s} src="placeholder" />
          ))}
        </CardFrame>
      </Section>

      <Section label="Initials Avatars">
        <CardFrame label="Various names and colors">
          {names.map((name, i) => {
            const initials = name.split(' ').map((n) => n[0]).join('');
            return <Avatar key={name} size="md" initials={initials} color={initialsColors[i % initialsColors.length]} />;
          })}
        </CardFrame>
      </Section>

      <Section label="All Sizes">
        <CardFrame label="xs through 2xl">
          {avatarSizes.map((s) => (
            <Avatar key={s} size={s} initials={s.toUpperCase()} />
          ))}
        </CardFrame>
      </Section>

      <Section label="Status Indicators">
        <CardFrame label="Online, offline, busy, away">
          <Avatar size="md" initials="JD" status="online" />
          <Avatar size="md" initials="BS" status="offline" />
          <Avatar size="md" initials="CW" status="busy" />
          <Avatar size="md" initials="DB" status="away" />
        </CardFrame>
      </Section>

      <Section label="Avatar Group">
        <CardFrame label="Overlapping + overflow">
          <div style={{ display: 'flex' }}>
            {names.slice(0, 4).map((name, i) => (
              <div key={name} style={{ marginLeft: i > 0 ? -8 : 0, zIndex: 4 - i, position: 'relative' }}>
                <Avatar size="md" initials={name.split(' ').map((n) => n[0]).join('')} color={initialsColors[i]} />
              </div>
            ))}
            <div style={{ marginLeft: -8, width: 40, height: 40, borderRadius: '50%', background: 'var(--color-bg-subtle, #f3f4f6)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-secondary)' }}>+4</div>
          </div>
        </CardFrame>
      </Section>

      <Section label="Loading State">
        <CardFrame label="Skeleton avatars">
          {avatarSizes.map((s) => (
            <div key={s} style={{ width: sizeMap[s], height: sizeMap[s], borderRadius: '50%', background: 'var(--color-bg-subtle, #f3f4f6)', animation: 'sk-pulse 1.5s ease-in-out infinite' }} />
          ))}
        </CardFrame>
      </Section>

      <Section label="Error / Fallback">
        <CardFrame label="Broken image fallback">
          <Avatar size="md" initials="?" color="var(--color-text-tertiary)" />
          <Avatar size="lg" initials="NA" color="var(--color-danger, #dc2626)" />
        </CardFrame>
      </Section>

      <Section label="Clickable Avatar">
        <CardFrame label="Click to toggle">
          <div onClick={() => setClicked((v) => !v)} style={{ cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '8px' }}>
            <Avatar size="md" initials="JD" />
            <span style={{ fontSize: 'var(--text-sm)', color: 'var(--color-text-secondary)' }}>{clicked ? 'Clicked!' : 'Click me'}</span>
          </div>
        </CardFrame>
      </Section>
    </div>
  );
}
