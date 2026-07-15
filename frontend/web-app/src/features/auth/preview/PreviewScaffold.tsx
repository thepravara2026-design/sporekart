import '../auth.css';

const DEVICES = [
  { label: 'Desktop', width: 1280 },
  { label: 'Tablet', width: 834 },
  { label: 'Mobile', width: 390 },
];

export interface PreviewScaffoldProps {
  title: string;
  subtitle: string;
  route: string;
  approvalStatus?: string;
  accessibilityNotes: string[];
  responsiveNotes: string[];
}

export function PreviewScaffold({
  title,
  subtitle,
  route,
  approvalStatus = 'Pending approval',
  accessibilityNotes,
  responsiveNotes,
}: PreviewScaffoldProps) {
  return (
    <div className="auth-preview">
      <header className="auth-preview__header">
        <div>
          <h1 className="auth-preview__title">{title}</h1>
          <p className="auth-preview__subtitle">{subtitle}</p>
        </div>
        <span className="auth-preview__badge" role="status">
          Approval: {approvalStatus}
        </span>
      </header>

      <div className="auth-preview__devices">
        {DEVICES.map((d) => (
          <figure key={d.label} className="auth-preview__device">
            <figcaption>
              <span>{d.label}</span>
              <span>{d.width}px</span>
            </figcaption>
            <iframe className="auth-preview__frame" src={route} title={`${title} — ${d.label}`} loading="lazy" />
          </figure>
        ))}
      </div>

      <div className="auth-preview__notes">
        <section className="auth-preview__note">
          <h3>Accessibility notes</h3>
          <ul>
            {accessibilityNotes.map((n) => (
              <li key={n}>{n}</li>
            ))}
          </ul>
        </section>
        <section className="auth-preview__note">
          <h3>Responsive notes</h3>
          <ul>
            {responsiveNotes.map((n) => (
              <li key={n}>{n}</li>
            ))}
          </ul>
        </section>
      </div>
    </div>
  );
}

export default PreviewScaffold;
