import { PublicContentContainer } from '../../PublicContentContainer';
import { Reveal } from '../Reveal';

const PARTNERS = ['State Agri Board', 'Krishi Vikas', 'Myco Labs', 'FarmCoop', 'AgriTech Hub', 'Rural Collective'];
const CERTIFICATIONS = ['ISO 22000', 'FSSAI', 'Organic NPOP', 'GAP', 'Lab-Verified'];

export function RecognitionStrip() {
  return (
    <section className="sk-home-recognition" aria-label="Recognition and certifications" style={{ backgroundColor: 'var(--color-bg-surface-default, #ffffff)', padding: 'var(--space-12, 48px) 0', overflow: 'hidden' }}>
      <PublicContentContainer>
        <Reveal>
          <p style={{ margin: 0, textAlign: 'center', fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-accent, #2f6f4f)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>
            Recognized by partners & certified to standards
          </p>
        </Reveal>
      </PublicContentContainer>

      <div style={{ marginTop: 'var(--space-6, 24px)' }}>
        <div className="sk-recognition-track" style={{ animationDuration: '35s' }}>
          {[...PARTNERS, ...PARTNERS].map((label, i) => (
            <span key={`partner-${i}`} className="sk-recognition-chip">
              {label}
            </span>
          ))}
        </div>
        <div className="sk-recognition-track" style={{ animationDuration: '28s', marginTop: 'var(--space-3, 12px)' }}>
          {[...CERTIFICATIONS, ...CERTIFICATIONS].map((label, i) => (
            <span key={`cert-${i}`} className="sk-recognition-chip sk-recognition-chip--cert">
              {label}
            </span>
          ))}
        </div>
      </div>

      <style>{`
        .sk-home-recognition .sk-recognition-track {
          display: flex;
          align-items: center;
          gap: var(--space-3, 12px);
          width: fit-content;
          animation: sk-recognition-scroll 35s linear infinite;
        }
        .sk-home-recognition:hover .sk-recognition-track {
          animation-play-state: paused;
        }
        .sk-home-recognition .sk-recognition-chip {
          display: inline-flex;
          align-items: center;
          justify-content: center;
          padding: var(--space-2, 8px) var(--space-4, 16px);
          border-radius: var(--radius-pill, 999px);
          border: 1px solid var(--color-border-default, #e5e7eb);
          background: var(--color-bg-surface-default, #ffffff);
          color: var(--color-text-secondary, #4b5563);
          font-size: var(--text-body-sm, 14px);
          font-weight: 500;
          white-space: nowrap;
          transition: border-color 200ms ease, color 200ms ease;
        }
        .sk-home-recognition .sk-recognition-chip--cert {
          background: var(--color-bg-accent-subtle, #c8e6c9);
          border-color: transparent;
          color: var(--color-text-accent, #2f6f4f);
          font-weight: 600;
        }
        @keyframes sk-recognition-scroll {
          from { transform: translateX(-50%); }
          to { transform: translateX(0); }
        }
        @media (max-width: 768px) {
          .sk-home-recognition .sk-recognition-track { animation-duration: 25s; }
          .sk-home-recognition .sk-recognition-chip { padding: var(--space-1, 4px) var(--space-3, 12px); font-size: 0.8125rem; }
        }
      `}</style>
    </section>
  );
}
