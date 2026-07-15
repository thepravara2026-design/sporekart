

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
  const DEVICES = [
    { label: 'Desktop', width: 1280 },
    { label: 'Tablet', width: 834 },
    { label: 'Mobile', width: 390 },
  ];

  return (
    <div className="cw-preview">
      <header className="cw-preview__header">
        <div>
          <h1 className="cw-preview__title">{title}</h1>
          <p className="cw-preview__subtitle">{subtitle}</p>
        </div>
        <span className="cw-preview__badge" role="status">
          Approval: {approvalStatus}
        </span>
      </header>

      <div className="cw-preview__devices">
        {DEVICES.map((d) => (
          <figure key={d.label} className="cw-preview__device">
            <figcaption>
              <span>{d.label}</span>
              <span>{d.width}px</span>
            </figcaption>
            <iframe
              className="cw-preview__frame"
              src={route}
              title={`${title} — ${d.label}`}
              loading="lazy"
            />
          </figure>
        ))}
      </div>

      <div className="cw-preview__notes">
        <section className="cw-preview__note">
          <h3>Accessibility notes</h3>
          <ul>
            {accessibilityNotes.map((n) => <li key={n}>{n}</li>)}
          </ul>
        </section>
        <section className="cw-preview__note">
          <h3>Responsive notes</h3>
          <ul>
            {responsiveNotes.map((n) => <li key={n}>{n}</li>)}
          </ul>
        </section>
      </div>
    </div>
  );
}

export default PreviewScaffold;