import { PublicLayout } from '../PublicLayout';
import { PublicContentContainer } from '../PublicContentContainer';
import { Icon } from '../../design-system/icons/Icon';
import { Reveal } from '../home/Reveal';
import { MediaPlaceholder } from '../home/MediaPlaceholder';
import { PageHeader, SectionHeading, CtaBanner, sectionPad } from './PageShell';

const POSTS = [
  { category: 'Guide', title: 'Oyster mushrooms at home: a 4-week plan', excerpt: 'Everything a first-time grower needs to fruit your first batch.', readTime: '8 min read' },
  { category: 'Research', title: 'Why substrate moisture matters more than temperature', excerpt: 'A look at the data behind contamination and yield.', readTime: '6 min read' },
  { category: 'Story', title: 'How a small farm tripled yield in one season', excerpt: 'A cultivator’s journey with SporeKart training.', readTime: '5 min read' },
  { category: 'Guide', title: 'Choosing the right spawn for your climate', excerpt: 'Matching varieties to local conditions.', readTime: '7 min read' },
  { category: 'FAQ', title: 'Common contamination causes (and fixes)', excerpt: 'Troubleshoot the most frequent growing problems.', readTime: '4 min read' },
  { category: 'News', title: 'New training cohorts opening this quarter', excerpt: 'Enrollment, schedules, and what’s new.', readTime: '3 min read' },
];

export default function BlogPage() {
  const crumbs = [{ label: 'Home', href: '/' }, { label: 'Blog' }];
  return (
    <PublicLayout
      breadcrumbs={crumbs}
      seo={{ title: 'Blog — SporeKart', description: 'Guides, research, and stories on mushroom cultivation from the SporeKart team and community.', canonical: 'https://sporekart.example.com/blog' }}
    >
      <PageHeader
        eyebrow="Knowledge"
        title="Guides, research, and stories from the farm"
        intro="Free, practical content to help you grow better — written by our research team and growers."
      />

      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="lg">
            <SectionHeading eyebrow="Latest" title="From the SporeKart library" description="Article content is placeholder pending the CMS." />
            <div style={{ marginTop: 'var(--space-5, 24px)', display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: 'var(--space-4, 16px)' }}>
              {POSTS.map((post) => (
                <a key={post.title} href="/blog" style={{ textDecoration: 'none', display: 'block' }} className="sk-pw-card">
                  <div style={{ height: '100%', border: '1px solid var(--color-border-default, #e5e7eb)', borderRadius: 'var(--radius-lg, 12px)', backgroundColor: 'var(--color-bg-surface-default, #ffffff)', overflow: 'hidden' }}>
                    <MediaPlaceholder label={post.title} icon="image" aspectRatio="16 / 9" />
                    <div style={{ padding: 'var(--space-4, 16px)' }}>
                      <span style={{ fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.06em', color: 'var(--color-text-accent, #1d4ed8)' }}>{post.category} · {post.readTime}</span>
                      <h3 style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-body-lg, 18px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>{post.title}</h3>
                      <p style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6 }}>{post.excerpt}</p>
                      <span style={{ display: 'inline-flex', alignItems: 'center', gap: 4, marginTop: 'var(--space-3, 12px)', fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-accent, #1d4ed8)' }}>Read article <Icon name="arrow-right" size={16} aria-label="Read" /></span>
                    </div>
                  </div>
                </a>
              ))}
            </div>
          </PublicContentContainer>
        </div>
      </Reveal>

      <CtaBanner title="Get new guides in your inbox" description="Join the community for growing tips and training updates." primaryLabel="Subscribe" primaryTo="/#newsletter" />
    </PublicLayout>
  );
}
