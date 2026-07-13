import { Icon } from '../../../design-system/icons/Icon';
import { Link as RouterLink } from 'react-router-dom';
import { Reveal } from '../Reveal';

const HERO_BACKGROUND = '/hero-bg.png';

const FEATURES = [
  { icon: 'shield', title: 'Lab-verified spawn', subtitle: 'Purity-guaranteed cultures' },
  { icon: 'truck', title: 'Pan-India delivery', subtitle: 'Cold-chain fresh' },
  { icon: 'book-open', title: 'Expert training', subtitle: 'Farmer-first courses' },
  { icon: 'headphone', title: 'Grower support', subtitle: '7 days a week' },
];

export function HeroSection() {
  return (
    <section className="sk-hero" aria-labelledby="sk-hero-title">
      <img
        className="sk-hero__bg"
        src={HERO_BACKGROUND}
        alt="SporeKart fresh mushrooms and cultivation products"
        decoding="async"
      />
      <div className="sk-hero__overlay" aria-hidden="true" />

      <div className="sk-hero__inner">
        <div className="sk-hero__content">
          <Reveal delay={0}>
            <span className="sk-hero__badge">
              <Icon name="sun" size={16} aria-label="Natural" />
              India&rsquo;s organic &amp; natural mushroom ecosystem
            </span>
          </Reveal>

          <Reveal delay={1}>
            <h1 id="sk-hero-title" className="sk-hero__title">
              Grow premium mushrooms with{' '}
              <em>confidence.</em>
            </h1>
          </Reveal>

          <Reveal delay={2}>
            <p className="sk-hero__subtitle">
              From lab-verified spawn to harvest-ready kits and expert training, SporeKart gives every
              cultivator &mdash; home grower, farm, or enterprise &mdash; the tools, knowledge, and support
              to succeed. Technology-driven agriculture, made farmer-friendly.
            </p>
          </Reveal>

          <Reveal delay={3}>
            <div className="sk-hero__actions">
              <RouterLink to="/products" className="sk-hero__btn sk-hero__btn--primary">
                Shop Spawn &amp; Kits
                <Icon name="arrow-right" size={18} aria-label="Shop" />
              </RouterLink>
              <RouterLink to="/training" className="sk-hero__btn sk-hero__btn--glass">
                <Icon name="book-open" size={18} aria-label="Training" />
                Explore Training
              </RouterLink>
            </div>
          </Reveal>
        </div>

        <Reveal delay={3}>
          <ul className="sk-hero__features" aria-label="Why growers choose SporeKart">
            {FEATURES.map((feature) => (
              <li key={feature.title} className="sk-hero__feature">
                <span className="sk-hero__feature-icon" aria-hidden="true">
                  <Icon name={feature.icon} size={20} />
                </span>
                <span className="sk-hero__feature-text">
                  <span className="sk-hero__feature-title">{feature.title}</span>
                  <span className="sk-hero__feature-sub">{feature.subtitle}</span>
                </span>
              </li>
            ))}
          </ul>
        </Reveal>
      </div>

      <style>{`
        .sk-hero {
          position: relative;
          width: 100%;
          min-height: calc(100vh - var(--header-height, 64px));
          display: flex;
          align-items: center;
          overflow: hidden;
          background: linear-gradient(135deg, var(--color-green-900, #153a26) 0%, var(--color-green-700, #265d3f) 100%);
          color: var(--color-text-inverse, #ffffff);
          font-family: var(--font-family-sans, system-ui);
        }
        .sk-hero__bg {
          position: absolute;
          inset: 0;
          width: 100%;
          height: 100%;
          object-fit: cover;
          object-position: center;
          z-index: 0;
        }
        .sk-hero__overlay {
          position: absolute;
          inset: 0;
          z-index: 1;
          background: linear-gradient(90deg, rgba(0,0,0,0.45) 0%, rgba(0,0,0,0.20) 50%, rgba(0,0,0,0) 100%);
        }
        .sk-hero__inner {
          position: relative;
          z-index: 2;
          width: 100%;
          display: flex;
          flex-direction: column;
          justify-content: center;
          gap: var(--space-6, 32px);
          padding: var(--space-8, 48px) var(--space-5, 24px);
          padding-left: clamp(24px, 5vw, 72px);
        }
        .sk-hero__content {
          max-width: 680px;
        }
        .sk-hero__badge {
          display: inline-flex;
          align-items: center;
          gap: var(--space-2, 8px);
          padding: var(--space-1, 4px) var(--space-3, 12px);
          border-radius: var(--radius-pill, 999px);
          background: rgba(255,255,255,0.14);
          border: 1px solid rgba(255,255,255,0.25);
          font-size: var(--text-body-sm, 14px);
          font-weight: 600;
          letter-spacing: 0.02em;
          backdrop-filter: blur(6px);
          -webkit-backdrop-filter: blur(6px);
        }
        .sk-hero__title {
          margin: var(--space-4, 16px) 0 0;
          font-size: clamp(2.125rem, 1.2rem + 3.2vw, 4rem);
          line-height: 1.08;
          font-weight: 800;
          letter-spacing: -0.02em;
        }
        .sk-hero__title em {
          font-style: normal;
          color: var(--color-green-600, #2f6f4f);
        }
        .sk-hero__subtitle {
          margin: var(--space-4, 16px) 0 0;
          font-size: var(--text-body-lg, 18px);
          line-height: 1.6;
          color: rgba(255,255,255,0.88);
          max-width: 560px;
        }
        .sk-hero__actions {
          display: flex;
          flex-wrap: wrap;
          gap: var(--space-3, 12px);
          margin-top: var(--space-6, 32px);
        }
        .sk-hero__btn {
          display: inline-flex;
          align-items: center;
          justify-content: center;
          gap: var(--space-2, 8px);
          padding: 14px 26px;
          height: 52px;
          border-radius: var(--radius-btn, 12px);
          font-size: var(--text-body, 16px);
          font-weight: 700;
          letter-spacing: var(--tracking-wide, 0.02em);
          text-decoration: none;
          cursor: pointer;
          border: 1px solid transparent;
          transition: transform var(--duration-fast, 150ms) var(--easing-standard, ease),
                      box-shadow var(--duration-fast, 150ms) var(--easing-standard, ease),
                      background var(--duration-fast, 150ms) var(--easing-standard, ease);
        }
        .sk-hero__btn--primary {
          background: var(--color-bg-surface-default, #ffffff);
          color: var(--color-green-900, #153a26);
          box-shadow: 0 10px 30px rgba(0,0,0,0.18);
        }
        .sk-hero__btn--primary:hover {
          transform: translateY(-2px);
          box-shadow: 0 16px 40px rgba(0,0,0,0.26);
        }
        .sk-hero__btn--glass {
          background: rgba(255,255,255,0.12);
          color: var(--color-text-inverse, #ffffff);
          border-color: rgba(255,255,255,0.35);
          backdrop-filter: blur(10px);
          -webkit-backdrop-filter: blur(10px);
        }
        .sk-hero__btn--glass:hover {
          background: rgba(255,255,255,0.22);
          transform: translateY(-2px);
        }
        .sk-hero__features {
          list-style: none;
          margin: var(--space-8, 48px) 0 0;
          padding: 0;
          display: grid;
          grid-template-columns: repeat(4, minmax(0, 1fr));
          gap: var(--space-3, 12px);
          max-width: 880px;
        }
        .sk-hero__feature {
          display: flex;
          align-items: center;
          gap: var(--space-3, 12px);
          padding: var(--space-3, 12px);
          border-radius: var(--radius-md, 12px);
          background: rgba(255,255,255,0.08);
          border: 1px solid rgba(255,255,255,0.16);
          backdrop-filter: blur(6px);
          -webkit-backdrop-filter: blur(6px);
        }
        .sk-hero__feature-icon {
          display: inline-flex;
          align-items: center;
          justify-content: center;
          width: 40px;
          height: 40px;
          flex-shrink: 0;
          border-radius: 50%;
          background: rgba(255,255,255,0.16);
          color: var(--color-text-inverse, #ffffff);
        }
        .sk-hero__feature-text {
          display: flex;
          flex-direction: column;
          min-width: 0;
        }
        .sk-hero__feature-title {
          font-size: var(--text-body-sm, 14px);
          font-weight: 600;
          color: var(--color-text-inverse, #ffffff);
        }
        .sk-hero__feature-sub {
          font-size: var(--text-caption, 13px);
          color: rgba(255,255,255,0.78);
        }

        @media (max-width: 1024px) {
          .sk-hero__title { font-size: clamp(2rem, 1rem + 3vw, 3rem); }
          .sk-hero__features { grid-template-columns: repeat(2, minmax(0, 1fr)); }
        }
        @media (max-width: 768px) {
          .sk-hero__inner {
            padding: var(--space-6, 32px) var(--space-4, 16px);
            justify-content: center;
            text-align: center;
          }
          .sk-hero__content { margin: 0 auto; }
          .sk-hero__badge { margin: 0 auto; }
          .sk-hero__title { font-size: clamp(2rem, 1.4rem + 3vw, 2.5rem); }
          .sk-hero__subtitle { margin-left: auto; margin-right: auto; }
          .sk-hero__actions { justify-content: center; }
          .sk-hero__btn { width: 100%; }
          .sk-hero__features { grid-template-columns: repeat(2, minmax(0, 1fr)); }
        }
        @media (max-width: 520px) {
          .sk-hero__features { grid-template-columns: 1fr; }
        }
        @media (prefers-reduced-motion: reduce) {
          .sk-hero__btn { transition: none; }
          .sk-hero__btn:hover { transform: none; }
        }
      `}</style>
    </section>
  );
}

export default HeroSection;
