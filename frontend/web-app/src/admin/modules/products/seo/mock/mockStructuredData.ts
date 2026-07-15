export const MOCK_STRUCTURED_DATA_EXAMPLES: Record<string, string> = {
  product: `{
  "@context": "https://schema.org",
  "@type": "Product",
  "name": "Fresh Organic Tomatoes (500g)",
  "description": "Farm-fresh organic tomatoes delivered to your doorstep.",
  "sku": "FR-TOM-500",
  "brand": { "@type": "Brand", "name": "GreenLeaf Farms" },
  "offers": {
    "@type": "Offer",
    "price": "40.00",
    "priceCurrency": "INR",
    "availability": "https://schema.org/InStock"
  }
}`,
  breadcrumb: `{
  "@context": "https://schema.org",
  "@type": "BreadcrumbList",
  "itemListElement": [
    { "@type": "ListItem", "position": 1, "name": "Home", "item": "https://sporekart.com" },
    { "@type": "ListItem", "position": 2, "name": "Products", "item": "https://sporekart.com/products" },
    { "@type": "ListItem", "position": 3, "name": "Fresh Produce", "item": "https://sporekart.com/category/fresh" },
    { "@type": "ListItem", "position": 4, "name": "Fresh Organic Tomatoes" }
  ]
}`,
  faq: `{
  "@context": "https://schema.org",
  "@type": "FAQPage",
  "mainEntity": [
    {
      "@type": "Question",
      "name": "How long does delivery take?",
      "acceptedAnswer": { "@type": "Answer", "text": "We deliver within 24-48 hours in metro cities." }
    },
    {
      "@type": "Question",
      "name": "Are the mushrooms organic?",
      "acceptedAnswer": { "@type": "Answer", "text": "Yes, all our mushrooms are certified organic." }
    }
  ]
}`,
  organization: `{
  "@context": "https://schema.org",
  "@type": "Organization",
  "name": "SporeKart",
  "url": "https://sporekart.com",
  "logo": "https://sporekart.com/logo.png"
}`,
  review: `{
  "@context": "https://schema.org",
  "@type": "Review",
  "itemReviewed": { "@type": "Product", "name": "Fresh Organic Tomatoes (500g)" },
  "reviewRating": { "@type": "Rating", "ratingValue": "4.5" },
  "author": { "@type": "Person", "name": "Verified Buyer" }
}`,
};
