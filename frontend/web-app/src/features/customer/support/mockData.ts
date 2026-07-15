export interface SupportMessage {
  sender: 'customer' | 'agent';
  text: string;
  timestamp: string;
}

export interface SupportTicket {
  id: string;
  subject: string;
  status: 'Open' | 'Processing' | 'Resolved';
  priority: 'High' | 'Medium' | 'Low';
  category: 'Order & Shipping' | 'Cultivation Help' | 'Lab tech' | 'Billing & Account';
  createdDate: string;
  updatedDate: string;
  assignedAgent: string;
  messages: SupportMessage[];
}

export interface KbArticle {
  id: string;
  title: string;
  category: 'Lab Work' | 'Cultivation' | 'Orders & Billing';
  description: string;
  body: string;
  readTime: string;
  likes: number;
}

export interface FaqItem {
  category: 'Orders' | 'Spawn' | 'Lab tech';
  q: string;
  a: string;
}

export const INITIAL_TICKETS: SupportTicket[] = [
  {
    id: 'TKT-2026-4412',
    subject: 'Delayed Grain Spawn Delivery (Order #ORD-2026-8842)',
    status: 'Open',
    priority: 'High',
    category: 'Order & Shipping',
    createdDate: '2026-07-12',
    updatedDate: '2026-07-13',
    assignedAgent: 'Support Agent Rahul',
    messages: [
      {
        sender: 'customer',
        text: 'Hello, my order ORD-2026-8842 has been marked in transit for 3 days. Can I get a status update from Delhivery?',
        timestamp: '2026-07-12 10:00 AM'
      },
      {
        sender: 'agent',
        text: 'Hello Jane, I checked with Delhivery. The shipment encountered a local logistics hold at the Bengaluru sorting hub but is scheduled to release tonight. You should receive it tomorrow by 4 PM.',
        timestamp: '2026-07-13 09:30 AM'
      }
    ]
  },
  {
    id: 'TKT-2026-3392',
    subject: 'Mycelium Contamination assistance on Agar plates',
    status: 'Processing',
    priority: 'Medium',
    category: 'Cultivation Help',
    createdDate: '2026-07-11',
    updatedDate: '2026-07-12',
    assignedAgent: 'Dr. Anita Rao',
    messages: [
      {
        sender: 'customer',
        text: 'I poured malt extract agar plates and inoculated them with pink oyster spores. Within 4 days, I see bright green dust forming around the edges. Is this Trichoderma?',
        timestamp: '2026-07-11 02:15 PM'
      },
      {
        sender: 'agent',
        text: 'Yes, green dust indicates Trichoderma sporulation. This usually enters during pouring or due to insufficient cooling times. We recommend discarding the contaminated plate immediately and sterilizing your scalpel red-hot.',
        timestamp: '2026-07-12 11:00 AM'
      }
    ]
  },
  {
    id: 'TKT-2026-1182',
    subject: 'Request for custom tax invoice format',
    status: 'Resolved',
    priority: 'Low',
    category: 'Billing & Account',
    createdDate: '2026-07-09',
    updatedDate: '2026-07-10',
    assignedAgent: 'Finance Admin Priya',
    messages: [
      {
        sender: 'customer',
        text: 'Can we get the GSTIN tax invoice in a structured corporate layout rather than simple receipt templates?',
        timestamp: '2026-07-09 11:00 AM'
      },
      {
        sender: 'agent',
        text: 'I have attached the corporate-formatted GST invoice PDF to this ticket log. You can also download it directly under your orders details panel.',
        timestamp: '2026-07-10 10:00 AM'
      }
    ]
  }
];

export const KB_ARTICLES: KbArticle[] = [
  {
    id: 'kb-sterility-practices',
    title: '5 Rules for Sterile Laboratory Inoculation',
    category: 'Lab Work',
    description: 'Ensure 0% mold rates during wedge transfers, agar inoculations, and liquid culture injections.',
    body: 'Sterile technique is the foundation of successful mycology. 1. Clean your laminar flow hood HEPA filter face. 2. Clean hands and wear latex gloves disinfected with 70% IPA. 3. Flame sterilize inoculation loops and scalpel blades until red hot. Let them cool before cutting agar. 4. Work in a single direction in front of the flow hood. 5. Never cross unsterilized items over open plates.',
    readTime: '5 mins',
    likes: 42
  },
  {
    id: 'kb-substrate-pasteurization',
    title: 'Monotub Substrates: Hydration & Field Capacity',
    category: 'Cultivation',
    description: 'How to pasteurize coco coir and achieve correct moisture levels without drowning mycelium.',
    body: 'Incorrect substrate hydration is a primary cause of mold. To prepare: Hydrate coco coir block with boiling water in an insulated bucket. Casing gypsum addition adds strength. Perform the Field Capacity Squeeze Check: Squeeze a handful of substrate firmly. Only 2 to 3 drops of water should release. If it runs in streams, it is too wet; add dry coir. If no drops release, mist water.',
    readTime: '6 mins',
    likes: 31
  },
  {
    id: 'kb-invoice-gst-faqs',
    title: 'Enterprise Billing & GST Declarations',
    category: 'Orders & Billing',
    description: 'Configure corporate tax details, verify GSTIN claims, and retrieve automated invoices.',
    body: 'To add your GSTIN details to order receipts, update your billing address profile before clicking checkout. Invoices are dispatched to registered corporate emails automatically within 2 hours of Razorpay transaction clearing.',
    readTime: '3 mins',
    likes: 18
  }
];

export const MOCK_FAQS: FaqItem[] = [
  {
    category: 'Orders',
    q: 'How long does shipment dispatch take?',
    a: 'Cultivar grains are prepared fresh. Grain spawn and agar plates are dispatched within 2 business days of order confirmation.'
  },
  {
    category: 'Spawn',
    q: 'What is the shelf life of Pink Oyster grain spawn bags?',
    a: 'Spawn bags should be inoculated immediately on receipt. If delayed, store in a clean refrigerator at 2-4°C for up to 14 days.'
  },
  {
    category: 'Lab tech',
    q: 'Why is my agar forming condensation on the lids?',
    a: 'Pouring agar too hot causes condensation. Wait for the bottle to cool to 50°C before pouring, and stack the petri plates high so heat escapes slowly.'
  }
];
export const MOCK_FEEDBACK = [
  { id: 'f-1', rating: 5, category: 'General', suggestions: 'Love the new training center video player notes!' }
];
