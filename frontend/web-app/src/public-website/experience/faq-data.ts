// FAQ dataset (Sprint 21 Part 6) — placeholder content pending legal/ops review.

export interface FaqItem {
  q: string;
  a: string;
}

export interface FaqCategory {
  slug: string;
  label: string;
  items: FaqItem[];
}

export const FAQ_CATEGORIES: FaqCategory[] = [
  {
    slug: 'products',
    label: 'Products',
    items: [
      { q: 'What mushroom products do you sell?', a: 'Lab-verified spawn, fresh and dried mushrooms, grow kits, and cultivation inputs. The full catalog launches soon.' },
      { q: 'Are your spawn batches quality-tested?', a: 'Yes — every spawn batch is lab-checked for contamination before it ships. Certification details are being finalized.' },
      { q: 'Do you sell equipment and substrates?', a: 'We plan to offer substrates, bags, and basic growing equipment alongside spawn.' },
    ],
  },
  {
    slug: 'orders',
    label: 'Orders',
    items: [
      { q: 'How do I place a bulk order?', a: 'Use the distributor enquiry form on the contact page, or email sales@sporekart.example.com with your volumes.' },
      { q: 'Can I modify or cancel an order?', a: 'Modifications are possible before processing begins. Contact support with your order ID as early as possible.' },
      { q: 'Do you provide invoices?', a: 'Yes — GST invoices are issued on confirmation. GSTIN is a placeholder until finalized.' },
    ],
  },
  {
    slug: 'shipping',
    label: 'Shipping',
    items: [
      { q: 'Do you ship across India?', a: 'Yes — pan-India delivery is planned, with cold-chain handling for fresh produce where available.' },
      { q: 'How long does delivery take?', a: 'Estimated timelines will be shared at checkout once the catalog launches. Fresh produce is prioritised for speed.' },
      { q: 'Is order tracking available?', a: 'Tracking will be provided where supported once fulfilment goes live.' },
    ],
  },
  {
    slug: 'payments',
    label: 'Payments',
    items: [
      { q: 'What payment methods do you accept?', a: 'UPI, net-banking, and major cards are planned. Final methods will be listed at checkout.' },
      { q: 'Are prices inclusive of taxes?', a: 'Taxes will be shown separately on the invoice. Current prices are placeholder.' },
    ],
  },
  {
    slug: 'training',
    label: 'Training',
    items: [
      { q: 'Do I need experience to join training?', a: 'No. Our Foundations program is built for complete beginners and grows with you.' },
      { q: 'Is support available after a course?', a: 'Yes — post-course field support helps you troubleshoot real growing problems.' },
      { q: 'Will I get a certificate?', a: 'Yes, a completion certificate is issued. Accreditation details are being finalized.' },
    ],
  },
  {
    slug: 'returns',
    label: 'Returns',
    items: [
      { q: 'Can I return a product?', a: 'Returns depend on product type and condition. The full policy is pending finalization.' },
      { q: 'What if my spawn arrives contaminated?', a: 'Contact support with photos and your order ID; quality claims are reviewed case by case.' },
    ],
  },
  {
    slug: 'refunds',
    label: 'Refunds',
    items: [
      { q: 'How do I request a refund?', a: 'Contact our team with your order details to start a request.' },
      { q: 'How long do refunds take?', a: 'Approved refunds are processed within a stated period after verification (timeline pending).' },
    ],
  },
  {
    slug: 'accounts',
    label: 'Accounts',
    items: [
      { q: 'Do I need an account to buy?', a: 'An account helps track orders and training enrolment. Guest checkout may be offered at launch.' },
      { q: 'How do I reset my password?', a: 'Use the account recovery flow (auth is part of a later phase).' },
    ],
  },
  {
    slug: 'general',
    label: 'General Questions',
    items: [
      { q: 'What is included in a growing kit?', a: 'Each kit ships with prepared substrate, verified spawn, and step-by-step instructions so you can start cultivating in days.' },
      { q: 'How do I get cultivation support?', a: 'Our grower support team helps with contamination, fruiting, and yield questions via the support page.' },
      { q: 'Where is SporeKart based?', a: 'We operate a research farm in Maharashtra, India. Exact location is a placeholder.' },
    ],
  },
  {
    slug: 'business',
    label: 'Business Partnership',
    items: [
      { q: 'Can I become a distributor?', a: 'Yes — fill the distributor enquiry form and our team will reach out about volumes and pricing.' },
      { q: 'Do you partner with farms or researchers?', a: 'We welcome farm alliances and research collaborations. Use the partnership enquiry on the contact page.' },
    ],
  },
];

export const ALL_FAQ_ITEMS = FAQ_CATEGORIES.flatMap((c) => c.items.map((it) => ({ ...it, category: c.label })));

export function searchFaq(query: string): { category: string; q: string; a: string }[] {
  const q = query.trim().toLowerCase();
  if (!q) return [];
  return ALL_FAQ_ITEMS.filter((it) =>
    `${it.q} ${it.a} ${it.category}`.toLowerCase().includes(q),
  );
}
