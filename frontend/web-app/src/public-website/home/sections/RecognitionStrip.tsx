import { PublicContentContainer } from '../../PublicContentContainer';
import { Reveal } from '../Reveal';

const PARTNERS = ['State Agri Board', 'Krishi Vikas', 'Myco Labs', 'FarmCoop', 'AgriTech Hub', 'Rural Collective'];
const CERTIFICATIONS = ['ISO 22000', 'FSSAI', 'Organic NPOP', 'GAP', 'Lab-Verified'];

function LogoChip({ label, kind }: { label: string; kind: 'partner' | 'cert' }) {
  return (
    <span
      aria-label={`${kind === 'partner' ? 'Partner' : 'Certification'} placeholder: ${label}`}
      title={`Placeholder — ${label}`}
      style={{
        display: 'inline-flex',
        alignItems: 'center',
        justifyContent: 'center',
        minWidth: 120,
        padding: 'var(--space-3, 12px) var(--space-4, 16px)',
        borderRadius: 'var(--radius-md, 8px)',
        border: '1px solid var(--color-border-default, #e5e7eb)',
        backgroundColor: 'var(--color-bg-surface-default, #ffffff)',
        color: 'var(--color-text-muted, #9ca3af)',
        fontSize: 'var(--text-body-sm, 14px)',
        fontWeight: 600,
        letterSpacing: '0.02em',
      }}
    >
      {label}
    </span>
  );
}

export function RecognitionStrip() {
  return (
    <section className="sk-home-recognition" aria-label="Recognition and certifications" style={{ backgroundColor: 'var(--color-bg-surface-default, #ffffff)' }}>
      <PublicContentContainer>
        <Reveal>
          <p style={{ margin: 0, textAlign: 'center', fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-secondary, #4b5563)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>
            Recognized by partners & certified to standards
          </p>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-3, 12px)', justifyContent: 'center', marginTop: 'var(--space-4, 16px)' }}>
            {PARTNERS.map((p) => (
              <LogoChip key={p} label={p} kind="partner" />
            ))}
          </div>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-3, 12px)', justifyContent: 'center', marginTop: 'var(--space-4, 16px)' }}>
            {CERTIFICATIONS.map((c) => (
              <LogoChip key={c} label={c} kind="cert" />
            ))}
          </div>
          <p style={{ margin: 'var(--space-3, 12px) 0 0', textAlign: 'center', fontSize: 'var(--text-body-xs, 12px)', color: 'var(--color-text-muted, #9ca3af)', fontStyle: 'italic' }}>
            Partner and certification logos are placeholders pending brand approvals.
          </p>
        </Reveal>
      </PublicContentContainer>
    </section>
  );
}
