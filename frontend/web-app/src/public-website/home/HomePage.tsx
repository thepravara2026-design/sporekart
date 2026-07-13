import { PublicLayout } from '../PublicLayout';
import { HeroSection } from './sections/HeroSection';
import { StoryBand } from './sections/StoryBand';
import { TrustStrip } from './sections/TrustStrip';
import { RecognitionStrip } from './sections/RecognitionStrip';
import { FeaturedProducts } from './sections/FeaturedProducts';
import { TrainingHighlight } from './sections/TrainingHighlight';
import { WhyChoose } from './sections/WhyChoose';
import { SuccessStories } from './sections/SuccessStories';
import { CultivationJourney } from './sections/CultivationJourney';
import { ResourcesPreview } from './sections/ResourcesPreview';
import { FaqPreview, FAQ_ITEMS } from './sections/FaqPreview';
import { NewsletterCta } from './sections/NewsletterCta';
import { Reveal } from './Reveal';
import { ScrollProgress } from './ScrollProgress';
import { MotionStyles } from './Reveal';

const HOMEPAGE_STRUCTURED_DATA = [
  {
    '@context': 'https://schema.org',
    '@type': 'Organization',
    name: 'SporeKart',
    url: 'https://sporekart.example.com',
    slogan: 'India’s trusted mushroom cultivation ecosystem',
    description:
      'SporeKart provides lab-verified mushroom spawn, fresh and dried mushrooms, professional training, and farmer-first cultivation support.',
  },
  {
    '@context': 'https://schema.org',
    '@type': 'WebSite',
    name: 'SporeKart',
    url: 'https://sporekart.example.com',
    potentialAction: {
      '@type': 'SearchAction',
      target: 'https://sporekart.example.com/search?q={search_term_string}',
      'query-input': 'required name=search_term_string',
    },
  },
  {
    '@context': 'https://schema.org',
    '@type': 'BreadcrumbList',
    itemListElement: [
      {
        '@type': 'ListItem',
        position: 1,
        name: 'Home',
        item: 'https://sporekart.example.com/',
      },
    ],
  },
  {
    '@context': 'https://schema.org',
    '@type': 'FAQPage',
    mainEntity: FAQ_ITEMS.map((item) => ({
      '@type': 'Question',
      name: item.q,
      acceptedAnswer: { '@type': 'Answer', text: item.a },
    })),
  },
];

export default function HomePage() {
  return (
    <PublicLayout
      seo={{
        title: 'SporeKart — India’s Trusted Mushroom Cultivation Ecosystem',
        description:
          'Lab-verified mushroom spawn, fresh and dried mushrooms, professional training, and farmer-first support. Technology-driven agriculture for every cultivator.',
        canonical: 'https://sporekart.example.com/',
        type: 'website',
        structuredData: HOMEPAGE_STRUCTURED_DATA,
      }}
      breadcrumbs={[]}
    >
      <MotionStyles />
      <ScrollProgress />
      <HeroSection />
      <Reveal>
        <StoryBand />
      </Reveal>
      <Reveal>
        <TrustStrip />
      </Reveal>
      <Reveal>
        <RecognitionStrip />
      </Reveal>
      <Reveal>
        <FeaturedProducts />
      </Reveal>
      <Reveal>
        <TrainingHighlight />
      </Reveal>
      <Reveal>
        <WhyChoose />
      </Reveal>
      <Reveal>
        <CultivationJourney />
      </Reveal>
      <Reveal>
        <SuccessStories />
      </Reveal>
      <Reveal>
        <ResourcesPreview />
      </Reveal>
      <Reveal>
        <FaqPreview />
      </Reveal>
      <Reveal>
        <NewsletterCta />
      </Reveal>
    </PublicLayout>
  );
}
