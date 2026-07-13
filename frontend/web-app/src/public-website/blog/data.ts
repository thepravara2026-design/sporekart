// Blog & Knowledge Hub — data layer (Sprint 21 Part 5)
//
// All article content in this file is PLACEHOLDER content pending the CMS.
// It is intentionally marked with `placeholder: true` and surfaced in the UI
// so reviewers never mistake it for published, fact-checked material.

export type ArticleBlockType =
  | 'heading'
  | 'paragraph'
  | 'list'
  | 'callout'
  | 'quote'
  | 'image'
  | 'video';

export interface ArticleBlock {
  type: ArticleBlockType;
  // heading
  level?: 2 | 3;
  id?: string;
  text?: string;
  // list
  ordered?: boolean;
  items?: string[];
  // callout
  variant?: 'info' | 'success' | 'warning' | 'tip';
  // quote
  cite?: string;
  // image / video
  label?: string;
  caption?: string;
}

export interface Author {
  name: string;
  role: string;
}

export interface Category {
  slug: string;
  name: string;
  description: string;
  icon: string;
  count: number;
}

export interface Tag {
  slug: string;
  name: string;
  count: number;
}

export interface Article {
  slug: string;
  title: string;
  excerpt: string;
  categorySlug: string;
  tagSlugs: string[];
  author: Author;
  publishedDate: string; // ISO yyyy-mm-dd
  updatedDate?: string;
  readingTime: number; // minutes
  imageLabel: string;
  featured?: boolean;
  trending?: boolean;
  placeholder?: boolean;
  body: ArticleBlock[];
}

// ---------------------------------------------------------------------------
// Categories
// ---------------------------------------------------------------------------

export const CATEGORIES: Category[] = [
  { slug: 'mushroom-farming', name: 'Mushroom Farming', description: 'Practical cultivation guides for every level.', icon: 'book-open', count: 18 },
  { slug: 'spawn-production', name: 'Spawn Production', description: 'Grain spawn, mother culture, and quality control.', icon: 'refresh-cw', count: 9 },
  { slug: 'fresh-mushrooms', name: 'Fresh Mushrooms', description: 'Varieties, harvest, and handling of fresh produce.', icon: 'shopping-bag', count: 12 },
  { slug: 'dry-mushrooms', name: 'Dry Mushrooms', description: 'Drying, shelf life, and value-added products.', icon: 'shopping-cart', count: 7 },
  { slug: 'training', name: 'Training', description: 'Courses, workshops, and certification paths.', icon: 'user-check', count: 11 },
  { slug: 'business', name: 'Business', description: 'Marketplace, pricing, and entrepreneurship.', icon: 'bar-chart', count: 10 },
  { slug: 'organic-farming', name: 'Organic Farming', description: 'Chemical-free methods and certification.', icon: 'check-circle', count: 6 },
  { slug: 'technology', name: 'Technology', description: 'Automation, sensors, and agri-tech.', icon: 'trending-up', count: 8 },
  { slug: 'government-schemes', name: 'Government Schemes', description: 'Subsidies, schemes, and compliance help.', icon: 'map-pin', count: 5 },
  { slug: 'recipes', name: 'Recipes', description: 'Cooking ideas for home and commercial kitchens.', icon: 'tag', count: 9 },
  { slug: 'storage', name: 'Storage', description: 'Cold chain, packaging, and shelf life.', icon: 'help-circle', count: 6 },
  { slug: 'research', name: 'Research', description: 'Findings, trials, and data from our lab.', icon: 'pie-chart', count: 13 },
];

// ---------------------------------------------------------------------------
// Tags
// ---------------------------------------------------------------------------

export const TAGS: Tag[] = [
  { slug: 'beginner', name: 'Beginner', count: 14 },
  { slug: 'oyster', name: 'Oyster', count: 11 },
  { slug: 'button-mushroom', name: 'Button Mushroom', count: 8 },
  { slug: 'spawn', name: 'Spawn', count: 16 },
  { slug: 'substrate', name: 'Substrate', count: 17 },
  { slug: 'organic', name: 'Organic', count: 9 },
  { slug: 'training', name: 'Training', count: 12 },
  { slug: 'subsidy', name: 'Subsidy', count: 5 },
  { slug: 'packaging', name: 'Packaging', count: 7 },
  { slug: 'nutrition', name: 'Nutrition', count: 10 },
  { slug: 'climate', name: 'Climate', count: 8 },
  { slug: 'contamination', name: 'Contamination', count: 9 },
  { slug: 'yield', name: 'Yield', count: 13 },
  { slug: 'marketing', name: 'Marketing', count: 6 },
  { slug: 'storage', name: 'Storage', count: 8 },
  { slug: 'technology', name: 'Technology', count: 7 },
];

// ---------------------------------------------------------------------------
// Authors
// ---------------------------------------------------------------------------

const AUTHORS: Record<string, Author> = {
  research: { name: 'Dr. Anjali Deshmukh', role: 'Lead Mycologist, SporeKart Research' },
  cultivation: { name: 'Ravi Patil', role: 'Cultivation Trainer, SporeKart Academy' },
  business: { name: 'Meera Nair', role: 'Agri-Business Strategist' },
  nutrition: { name: 'Sneha Kulkarni', role: 'Food & Nutrition Specialist' },
  policy: { name: 'Arjun Mehta', role: 'Government Relations Lead' },
};

// ---------------------------------------------------------------------------
// Body factory (keeps placeholder content DRY but per-article)
// ---------------------------------------------------------------------------

interface SectionSpec {
  id: string;
  heading: string;
  paragraphs: string[];
  list?: string[];
  ordered?: boolean;
  callout?: { variant: 'info' | 'success' | 'warning' | 'tip'; text: string };
  quote?: { text: string; cite: string };
  image?: { label: string; caption: string };
}

function makeBody(lead: string, sections: SectionSpec[]): ArticleBlock[] {
  const blocks: ArticleBlock[] = [{ type: 'paragraph', text: lead }];
  for (const s of sections) {
    blocks.push({ type: 'heading', level: 2, id: s.id, text: s.heading });
    s.paragraphs.forEach((p) => blocks.push({ type: 'paragraph', text: p }));
    if (s.list) {
      blocks.push({ type: 'list', ordered: s.ordered, items: s.list });
    }
    if (s.callout) {
      blocks.push({ type: 'callout', variant: s.callout.variant, text: s.callout.text });
    }
    if (s.quote) {
      blocks.push({ type: 'quote', text: s.quote.text, cite: s.quote.cite });
    }
    if (s.image) {
      blocks.push({ type: 'image', label: s.image.label, caption: s.image.caption });
    }
  }
  return blocks;
}

// ---------------------------------------------------------------------------
// Articles (placeholder content)
// ---------------------------------------------------------------------------

export const ARTICLES: Article[] = [
  {
    slug: 'beginner-mushroom-farming-4-week-plan',
    title: 'Beginner mushroom farming: a 4-week plan to your first harvest',
    excerpt: 'A calm, step-by-step path from kit to harvest for first-time growers using oyster mushrooms.',
    categorySlug: 'mushroom-farming',
    tagSlugs: ['beginner', 'oyster', 'substrate', 'training'],
    author: AUTHORS.cultivation,
    publishedDate: '2026-06-28',
    readingTime: 8,
    imageLabel: 'Beginner oyster grow at home',
    featured: true,
    trending: true,
    placeholder: true,
    body: makeBody(
      'If you have never grown a mushroom before, oyster mushrooms are the friendliest place to start. This plan assumes a small indoor setup and a ready-made substrate block. Treat every week as a milestone, not a deadline.',
      [
        {
          id: 'what-you-need',
          heading: 'What you need before week one',
          paragraphs: [
            'A pre-pasteurised substrate block, a clean corner with indirect light, and a spray bottle are enough to begin. Avoid placing the block in direct sun or near a fan.',
            'Keep a simple log — date of start, daily observations, and any smells. The log is your early warning system for contamination.',
          ],
          callout: { variant: 'tip', text: 'Placeholder tip: real growers report best early results at 20–24°C with 80–90% humidity. Numbers are illustrative until CMS content is finalised.' },
          list: ['Pre-pasteurised substrate block', 'Spray bottle', 'Clean shelf away from drafts', 'Notebook or phone for logging'],
        },
        {
          id: 'week-1',
          heading: 'Week 1 — awaken the block',
          paragraphs: [
            'Cut a small opening in the bag and mist the surface twice a day. You are aiming for a damp, not wet, surface. Tiny white fuzz (mycelium) is normal and healthy.',
          ],
          image: { label: 'Week 1: mycelium waking on block surface', caption: 'Image placeholder — replace with real grow photo.' },
        },
        {
          id: 'week-2-3',
          heading: 'Weeks 2–3 — pins and growth',
          paragraphs: [
            'Small bumps called pins appear. This is the most exciting stage. Keep humidity steady and avoid touching the pins.',
            'If you see green, black, or slimy patches, that is contamination — isolate the block immediately.',
          ],
          quote: { text: 'Consistency beats intensity. A steady mist twice a day outperforms a heavy soak once a week.', cite: 'SporeKart Academy trainer' },
        },
        {
          id: 'week-4',
          heading: 'Week 4 — harvest',
          paragraphs: [
            'Harvest when the caps begin to flatten but before they release spores. Twist gently at the base. Your first flush is ready.',
          ],
          callout: { variant: 'success', text: 'Placeholder milestone: one successful flush completes the 4-week plan.' },
        },
      ],
    ),
  },
  {
    slug: 'choosing-the-right-spawn',
    title: 'Choosing the right spawn for your climate and crop',
    excerpt: 'Grain, sawdust, or plug spawn — what actually matters when you match variety to local conditions.',
    categorySlug: 'spawn-production',
    tagSlugs: ['spawn', 'substrate', 'climate'],
    author: AUTHORS.research,
    publishedDate: '2026-06-20',
    readingTime: 7,
    imageLabel: 'Grain spawn jars',
    trending: true,
    placeholder: true,
    body: makeBody(
      'Spawn is the living starter that carries your mushroom variety into the substrate. The right choice reduces contamination risk and Shortens the path to fruiting.',
      [
        {
          id: 'types',
          heading: 'The three common spawn types',
          paragraphs: ['Each spawn type suits different scales and substrates.'],
          list: ['Grain spawn — versatile, fast coloniser, good for bags', 'Sawdust spawn — economical for large straw/wood substrates', 'Plug spawn — for logs and outdoor cultivation'],
        },
        {
          id: 'matching',
          heading: 'Matching spawn to climate',
          paragraphs: [
            'Warm, humid regions favour oyster and milky strains; cooler highlands suit button and shiitake. Match the strain to your ambient range before buying spawn.',
          ],
          callout: { variant: 'info', text: 'Placeholder guidance: confirm strain temperature ranges with your supplier before bulk purchase.' },
        },
      ],
    ),
  },
  {
    slug: 'substrate-moisture-vs-temperature',
    title: 'Why substrate moisture often matters more than temperature',
    excerpt: 'A look at the data behind contamination and yield — and where growers should focus first.',
    categorySlug: 'research',
    tagSlugs: ['substrate', 'contamination', 'yield'],
    author: AUTHORS.research,
    publishedDate: '2026-06-12',
    updatedDate: '2026-06-25',
    readingTime: 6,
    imageLabel: 'Moisture meter on substrate',
    placeholder: true,
    body: makeBody(
      'Growers obsess over temperature, but our trial data suggests substrate moisture is the louder signal for both contamination and final yield.',
      [
        {
          id: 'data',
          heading: 'What the trials showed',
          paragraphs: [
            'Across 40 batches, batches held at optimal moisture outperformed temperature-tuned batches on yield by a meaningful margin. Moisture also correlated with lower contamination.',
          ],
          quote: { text: 'If you can only monitor one number in week one, make it moisture.', cite: 'SporeKart Research note' },
          image: { label: 'Trial chart: moisture vs yield', caption: 'Image placeholder — chart to be added.' },
        },
        {
          id: 'practice',
          heading: 'Practical takeaway',
          paragraphs: ['Invest in a simple moisture check routine before spending on climate control.'],
          callout: { variant: 'warning', text: 'Placeholder: figures are illustrative until peer-reviewed CMS content replaces them.' },
        },
      ],
    ),
  },
  {
    slug: 'oyster-mushrooms-at-home',
    title: 'Oyster mushrooms at home: a relaxed weekend project',
    excerpt: 'Turn a corner of your kitchen into a small oyster farm with minimal equipment.',
    categorySlug: 'mushroom-farming',
    tagSlugs: ['oyster', 'beginner', 'recipes'],
    author: AUTHORS.cultivation,
    publishedDate: '2026-06-05',
    readingTime: 5,
    imageLabel: 'Fresh oyster mushrooms',
    placeholder: true,
    body: makeBody(
      'Oyster mushrooms are forgiving and fast. This weekend project is built for people who want a result without a lab setup.',
      [
        { id: 'setup', heading: 'The setup', paragraphs: ['A bucket or bag, a straw base, and a spawn pouch are enough.'], list: ['Bucket or grow bag', 'Pasteurised straw', 'Oyster spawn pouch'] },
        { id: 'cook', heading: 'From harvest to plate', paragraphs: ['Oyster mushrooms sauté in minutes and take on almost any flavour.'], callout: { variant: 'tip', text: 'Placeholder recipe note: add at the very end so they stay tender.' } },
      ],
    ),
  },
  {
    slug: 'fresh-vs-dry-mushrooms',
    title: 'Fresh vs dry mushrooms: when to choose which',
    excerpt: 'Shelf life, nutrition, and market value compared for growers and buyers.',
    categorySlug: 'fresh-mushrooms',
    tagSlugs: ['nutrition', 'storage', 'packaging'],
    author: AUTHORS.nutrition,
    publishedDate: '2026-05-30',
    readingTime: 6,
    imageLabel: 'Fresh and dried mushrooms side by side',
    placeholder: true,
    body: makeBody(
      'Both forms have a place. The right choice depends on your buyer, your cold chain, and how fast you can move product.',
      [
        { id: 'shelf', heading: 'Shelf life and logistics', paragraphs: ['Fresh mushrooms need a cold chain; dried mushrooms ship anywhere.'], list: ['Fresh: 3–7 days refrigerated', 'Dried: 6–12 months ambient'] },
        { id: 'value', heading: 'Market value', paragraphs: ['Dried commands a higher per-kg price but needs processing labour.'], callout: { variant: 'info', text: 'Placeholder: pricing varies by region and season.' } },
      ],
    ),
  },
  {
    slug: 'drying-and-storing-mushrooms',
    title: 'Drying and storing mushrooms without losing quality',
    excerpt: 'A short guide to dehydration, packaging, and keeping aroma intact.',
    categorySlug: 'dry-mushrooms',
    tagSlugs: ['storage', 'packaging', 'nutrition'],
    author: AUTHORS.nutrition,
    publishedDate: '2026-05-22',
    readingTime: 5,
    imageLabel: 'Dehydrated mushroom trays',
    placeholder: true,
    body: makeBody(
      'Drying is the oldest preservation method and still one of the best for small farms.',
      [
        { id: 'method', heading: 'The drying method', paragraphs: ['Low, steady heat preserves aroma better than high heat.'], list: ['Slice evenly', 'Dry at low temperature', 'Cool before packing'] },
        { id: 'pack', heading: 'Packaging for shelf life', paragraphs: ['Use moisture-barrier pouches and keep light out.'], callout: { variant: 'tip', text: 'Placeholder: include a silica sachet for humid regions.' } },
      ],
    ),
  },
  {
    slug: 'mushroom-nutrition-guide',
    title: 'Mushroom nutrition: what is actually on your plate',
    excerpt: 'Protein, fibre, and micronutrients — a plain-language look at mushroom nutrition.',
    categorySlug: 'fresh-mushrooms',
    tagSlugs: ['nutrition', 'recipes'],
    author: AUTHORS.nutrition,
    publishedDate: '2026-05-15',
    readingTime: 7,
    imageLabel: 'Nutrition plate with mushrooms',
    placeholder: true,
    body: makeBody(
      'Mushrooms are low in calories and surprisingly rich in certain nutrients. Here is the plain version.',
      [
        { id: 'macros', heading: 'The macros', paragraphs: ['Mushrooms provide plant protein, fibre, and very little fat.'], list: ['Protein: modest but complete-ish', 'Fibre: good for gut', 'Fat: minimal'] },
        { id: 'micro', heading: 'Micronutrients worth knowing', paragraphs: ['B-vitamins and selenium are the headline act.'], quote: { text: 'Think of mushrooms as a nutrient-dense vegetable, not a substitute for meat.', cite: 'Food & Nutrition Specialist' } },
      ],
    ),
  },
  {
    slug: 'mushroom-business-opportunities',
    title: 'Mushroom business opportunities for small farmers',
    excerpt: 'Where the demand is, what buyers pay for, and how to start small.',
    categorySlug: 'business',
    tagSlugs: ['business', 'marketing', 'training'],
    author: AUTHORS.business,
    publishedDate: '2026-05-08',
    readingTime: 8,
    imageLabel: 'Farmers market mushroom stall',
    trending: true,
    placeholder: true,
    body: makeBody(
      'Mushrooms fit small landholdings because they grow vertically and need little space. The business case is strong when you start with a clear buyer.',
      [
        { id: 'demand', heading: 'Where the demand is', paragraphs: ['Restaurants, dry-product brands, and local markets are the three steady channels.'], list: ['HoReCa (hotels, restaurants, cafés)', 'Dry/processed product brands', 'Direct-to-consumer at markets'] },
        { id: 'start', heading: 'How to start small', paragraphs: ['Pilot one variety, prove the yield, then add a second.'], callout: { variant: 'success', text: 'Placeholder: a one-variety pilot limits risk while you learn the market.' } },
      ],
    ),
  },
  {
    slug: 'marketing-mushrooms-locally',
    title: 'Marketing mushrooms locally without a big budget',
    excerpt: 'Storytelling, sampling, and partnerships that move product fast.',
    categorySlug: 'business',
    tagSlugs: ['marketing', 'business', 'packaging'],
    author: AUTHORS.business,
    publishedDate: '2026-05-01',
    readingTime: 6,
    imageLabel: 'Local delivery packaging',
    placeholder: true,
    body: makeBody(
      'Local marketing rewards consistency and a good story more than ad spend.',
      [
        { id: 'story', heading: 'Lead with the story', paragraphs: ['Buyers remember the farm, not the SKU.'], callout: { variant: 'tip', text: 'Placeholder: put a short farm note on every pack.' } },
        { id: 'sample', heading: 'Sample and partner', paragraphs: ['Chefs decide fast when they can taste the difference.'], list: ['Offer chef samples', 'Partner with a local grocery', 'Run a weekend stall'] },
      ],
    ),
  },
  {
    slug: 'packaging-for-fresh-mushrooms',
    title: 'Packaging for fresh mushrooms that arrive perfect',
    excerpt: 'Ventilation, moisture, and labelling that protect fragile produce.',
    categorySlug: 'fresh-mushrooms',
    tagSlugs: ['packaging', 'storage'],
    author: AUTHORS.business,
    publishedDate: '2026-04-24',
    readingTime: 5,
    imageLabel: 'Vented mushroom punnet',
    placeholder: true,
    body: makeBody(
      'Fresh mushrooms bruise easily and hate trapped moisture. Packaging is a quality decision, not just a cost.',
      [
        { id: 'vent', heading: 'Ventilation first', paragraphs: ['Punnets with micro-venting outperform sealed trays.'], list: ['Use vented punnets', 'Avoid condensation', 'Labour-friendly labelling'] },
        { id: 'label', heading: 'Label for trust', paragraphs: ['Batch date and farm name build repeat buyers.'], callout: { variant: 'info', text: 'Placeholder: batch labelling supports traceability for future schemes.' } },
      ],
    ),
  },
  {
    slug: 'government-subsidies-for-mushroom-farmers',
    title: 'Government subsidies for mushroom farmers: a starter map',
    excerpt: 'Common scheme types, eligibility themes, and where to begin your application.',
    categorySlug: 'government-schemes',
    tagSlugs: ['subsidy', 'business'],
    author: AUTHORS.policy,
    publishedDate: '2026-04-18',
    featured: true,
    readingTime: 7,
    imageLabel: 'Scheme application documents',
    placeholder: true,
    body: makeBody(
      'Several state and central schemes support mushroom cultivation. This is a starter map, not legal advice — confirm current eligibility with the official portal.',
      [
        { id: 'types', heading: 'Common scheme types', paragraphs: ['Schemes typically support infrastructure, training, or unit cost.'], list: ['Capital subsidy for units', 'Training fee support', 'Cold-chain / storage grants'] },
        { id: 'eligibility', heading: 'Eligibility themes', paragraphs: ['Women-led, rural, and first-time farmer units often get priority.'], callout: { variant: 'warning', text: 'Placeholder: scheme names and amounts change yearly — verify on the official portal before applying.' } },
      ],
    ),
  },
  {
    slug: 'sustainable-mushroom-farming',
    title: 'Sustainable mushroom farming with agricultural waste',
    excerpt: 'Turning crop residue into substrate and closing the loop on the farm.',
    categorySlug: 'organic-farming',
    tagSlugs: ['organic', 'substrate', 'climate'],
    author: AUTHORS.research,
    publishedDate: '2026-04-10',
    readingTime: 6,
    imageLabel: 'Straw substrate from farm waste',
    placeholder: true,
    body: makeBody(
      'Mushrooms are among the most resource-efficient proteins you can farm. Using agricultural waste as substrate closes the loop.',
      [
        { id: 'waste', heading: 'Waste as feed', paragraphs: ['Rice straw and cotton waste make excellent substrate.'], list: ['Rice straw', 'Cotton waste', 'Sugarcane bagasse'] },
        { id: 'loop', heading: 'Closing the loop', paragraphs: ['Spent substrate becomes compost for other crops.'], quote: { text: 'A mushroom farm can be a net exporter of soil health, not just food.', cite: 'SporeKart Research' } },
      ],
    ),
  },
  {
    slug: 'future-of-mushroom-industry',
    title: 'The future of the mushroom industry in India',
    excerpt: 'Automation, demand shifts, and where the next decade is heading.',
    categorySlug: 'technology',
    tagSlugs: ['technology', 'research'],
    author: AUTHORS.research,
    publishedDate: '2026-04-02',
    trending: true,
    readingTime: 7,
    imageLabel: 'Automated climate chamber',
    placeholder: true,
    body: makeBody(
      'Demand is climbing and technology is finally cheap enough for small farms. Here is a measured look ahead.',
      [
        { id: 'auto', heading: 'Automation reaches the small farm', paragraphs: ['Low-cost sensors make climate control practical.'], callout: { variant: 'info', text: 'Placeholder outlook: adoption curves are illustrative.' } },
        { id: 'demand', heading: 'Demand is broadening', paragraphs: ['Wellness and plant-based trends pull new buyers in.'], list: ['Wellness buyers', 'Plant-based food brands', 'Export interest'] },
      ],
    ),
  },
  {
    slug: 'contamination-troubleshooting',
    title: 'Contamination troubleshooting: the usual suspects',
    excerpt: 'Green, black, and wet spots — what they mean and what to do.',
    categorySlug: 'research',
    tagSlugs: ['contamination', 'spawn', 'substrate'],
    author: AUTHORS.research,
    publishedDate: '2026-03-26',
    readingTime: 6,
    imageLabel: 'Contamination on block',
    placeholder: true,
    body: makeBody(
      'Contamination is the number-one frustration for new growers. Most cases come from a short list of causes.',
      [
        { id: 'signs', heading: 'Reading the signs', paragraphs: ['Colour and smell tell you most of what you need.'], list: ['Green = trichoderma', 'Black = wet rot / bacteria', 'Sour smell = bacterial'] },
        { id: 'fix', heading: 'What to do', paragraphs: ['Isolate first, diagnose second, then adjust process.'], callout: { variant: 'warning', text: 'Placeholder: never mix contaminated blocks with clean ones.' } },
      ],
    ),
  },
];

// ---------------------------------------------------------------------------
// Query helpers
// ---------------------------------------------------------------------------

const SITE_URL = 'https://sporekart.example.com';

export function articleUrl(slug: string): string {
  return `${SITE_URL}/blog/${slug}`;
}
export function categoryUrl(slug: string): string {
  return `${SITE_URL}/blog/category/${slug}`;
}
export function tagUrl(slug: string): string {
  return `${SITE_URL}/blog/tag/${slug}`;
}

export function getArticleBySlug(slug: string): Article | undefined {
  return ARTICLES.find((a) => a.slug === slug);
}

export function getCategoryBySlug(slug: string): Category | undefined {
  return CATEGORIES.find((c) => c.slug === slug);
}

export function getTagBySlug(slug: string): Tag | undefined {
  return TAGS.find((t) => t.slug === slug);
}

export function getArticlesByCategory(slug: string): Article[] {
  return ARTICLES.filter((a) => a.categorySlug === slug);
}

export function getArticlesByTag(slug: string): Article[] {
  return ARTICLES.filter((a) => a.tagSlugs.includes(slug));
}

export function getFeaturedArticles(): Article[] {
  return ARTICLES.filter((a) => a.featured);
}

export function getLatestArticles(limit?: number): Article[] {
  const sorted = [...ARTICLES].sort((a, b) => (a.publishedDate < b.publishedDate ? 1 : -1));
  return limit ? sorted.slice(0, limit) : sorted;
}

export function getTrendingArticles(limit?: number): Article[] {
  const trending = ARTICLES.filter((a) => a.trending);
  return limit ? trending.slice(0, limit) : trending;
}

export function getRelatedArticles(article: Article, limit = 3): Article[] {
  const sameCategory = ARTICLES.filter(
    (a) => a.slug !== article.slug && a.categorySlug === article.categorySlug,
  );
  const sameTag = ARTICLES.filter(
    (a) =>
      a.slug !== article.slug &&
      a.categorySlug !== article.categorySlug &&
      a.tagSlugs.some((t) => article.tagSlugs.includes(t)),
  );
  return [...sameCategory, ...sameTag].slice(0, limit);
}

export function searchArticles(query: string): Article[] {
  const q = query.trim().toLowerCase();
  if (!q) return [];
  return ARTICLES.filter((a) => {
    const haystack = [
      a.title,
      a.excerpt,
      a.categorySlug,
      ...a.tagSlugs,
      ...a.body.filter((b) => b.text).map((b) => b.text as string),
    ]
      .join(' ')
      .toLowerCase();
    return haystack.includes(q);
  });
}

export function formatDate(iso: string): string {
  const d = new Date(`${iso}T00:00:00`);
  return d.toLocaleDateString('en-IN', { day: 'numeric', month: 'long', year: 'numeric' });
}

export const POPULAR_SEARCHES = ['oyster', 'spawn', 'subsidy', 'contamination', 'storage', 'nutrition'];
