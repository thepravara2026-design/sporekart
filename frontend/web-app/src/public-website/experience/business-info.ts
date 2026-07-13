// Business information blocks (Sprint 21 Part 6)
// All regulated/unknown values are clearly marked as placeholders.

export interface InfoField {
  label: string;
  value: string;
  placeholder?: boolean;
}

export const COMPANY = {
  name: 'SporeKart Agri-Tech Pvt. Ltd.',
  tagline: 'Mushroom cultivation, simplified.',

  // Regulated identifiers — placeholders until verified.
  identifiers: [
    { label: 'GSTIN', value: '27XXXXXXXXX0XXZ1 (placeholder)', placeholder: true },
    { label: 'FSSAI', value: 'XXXXXXXXXXXXXXX (placeholder)', placeholder: true },
    { label: 'CIN / Registration', value: 'U0XXXXXXXXX (placeholder)', placeholder: true },
  ] as InfoField[],

  // Contact channels.
  supportEmail: 'support@sporekart.example.com',
  salesEmail: 'sales@sporekart.example.com',
  trainingEmail: 'training@sporekart.example.com',
  pressEmail: 'press@sporekart.example.com',
  phone: '+91 00000 00000 (placeholder)',
  whatsapp: '+91 00000 00000 (placeholder)',

  // Location.
  addressLines: [
    'SporeKart Research Farm',
    'Village / Taluka (placeholder)',
    'Maharashtra, India — 4XXXXX (placeholder)',
  ],
  businessLocation: 'Maharashtra, India (placeholder)',
  mapEmbedPlaceholder: true,

  // Hours.
  hours: 'Mon–Sat, 9:00 AM – 6:00 PM IST',
  emergencyContact: 'Emergency/urgent cultivation issues: call the grower hotline (placeholder).',

  // Social.
  social: [
    { label: 'WhatsApp', href: 'https://wa.me/', icon: 'message-circle' },
    { label: 'LinkedIn', href: 'https://linkedin.com/', icon: 'external-link' },
    { label: 'Instagram', href: 'https://instagram.com/', icon: 'external-link' },
    { label: 'YouTube', href: 'https://youtube.com/', icon: 'external-link' },
  ],
};

export const CONTACT_CATEGORIES = [
  { value: 'general', label: 'General enquiry' },
  { value: 'products', label: 'Products & orders' },
  { value: 'training', label: 'Training' },
  { value: 'support', label: 'Technical support' },
  { value: 'distributor', label: 'Distributor / wholesale' },
  { value: 'partnership', label: 'Partnership / PR' },
  { value: 'careers', label: 'Careers' },
];

export const CONTACT_QUICK_LINKS = [
  {
    icon: 'shopping-bag',
    title: 'Distributor Enquiry',
    description: 'Bulk spawn, fresh & dried mushrooms for resellers.',
    to: '/contact?subject=Distributor%20Enquiry',
    cta: 'Enquire',
  },
  {
    icon: 'user-check',
    title: 'Training Enquiry',
    description: 'Cohorts, workshops, and certification paths.',
    to: '/training',
    cta: 'Explore',
  },
  {
    icon: 'book-open',
    title: 'Become a Partner',
    description: 'Farm alliances, research, and co-branding.',
    to: '/contact?subject=Partnership',
    cta: 'Partner with us',
  },
  {
    icon: 'tag',
    title: 'Careers',
    description: 'Join the SporeKart team (placeholder openings).',
    to: '/contact?subject=Careers',
    cta: 'Get in touch',
  },
];
