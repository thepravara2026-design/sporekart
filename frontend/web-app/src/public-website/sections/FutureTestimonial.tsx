import { PublicContentContainer } from '../PublicContentContainer';

export interface FutureTestimonialProps {
  quote?: string;
  author?: string;
  role?: string;
}

export function FutureTestimonial({
  quote = 'SporeKart made our first grow approachable and reliable — the kits just work.',
  author = 'A. Grower',
  role = 'Small-scale farm',
}: FutureTestimonialProps) {
  const style: React.CSSProperties = {
    backgroundColor: 'var(--color-bg-surface-muted, #f8fafc)',
  };

  return (
    <section className="sk-public-testimonial" aria-label="Testimonial" style={style}>
      <PublicContentContainer maxWidth="md">
        <figure style={{ margin: 0, textAlign: 'center' }}>
          <blockquote
            style={{
              margin: 0,
              fontSize: 'var(--text-title-sm, 20px)',
              fontWeight: 500,
              color: 'var(--color-text-primary, #1f2933)',
              lineHeight: 1.5,
            }}
          >
            “{quote}”
          </blockquote>
          <figcaption style={{ marginTop: 'var(--space-4, 16px)', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)' }}>
            <strong style={{ color: 'var(--color-text-primary, #1f2933)' }}>{author}</strong> · {role}
          </figcaption>
        </figure>
      </PublicContentContainer>
    </section>
  );
}
