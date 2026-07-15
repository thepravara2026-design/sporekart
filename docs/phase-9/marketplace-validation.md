# Marketplace Validation

> Companion to [sprint-24-part-10.md](./sprint-24-part-10.md) and [validation-framework.md](./validation-framework.md). Mock Mode.

## Overview

The Marketplace Validation module assesses product readiness across 6 sales channels. Each channel has its own field requirements, data format expectations, and completeness thresholds. Products receive a per-channel readiness score and a list of missing fields, warnings, and recommendations. All data is mock. No marketplace API integration.

## 6 Sales Channels

| # | Channel | ID | Type | Target Market |
|---|---------|----|------|---------------|
| 1 | Amazon | `amazon` | General marketplace | India |
| 2 | Flipkart | `flipkart` | General marketplace | India |
| 3 | AgriBegri | `agribegri` | Agricultural marketplace | India |
| 4 | Google Shopping | `google_shopping` | Shopping aggregator | Global |
| 5 | IndiaMART | `indiamart` | B2B marketplace | India |
| 6 | Export | `export` | International trade | Global |

## Per-Channel Readiness Score

Each channel computes a readiness score based on the percentage of required fields that are populated and valid:

```ts
interface ChannelReadiness {
  channelId: string;
  channelName: string;
  score: number;              // 0–100
  totalFields: number;
  populatedFields: number;
  validFields: number;
  missingFields: MissingField[];
  warnings: string[];
  recommendations: string[];
  status: 'ready' | 'needs_work' | 'incomplete';
}

function calculateChannelScore(readiness: ChannelReadiness): number {
  // Weight: populated fields (60%) + valid fields (40%)
  const populatedWeight = 0.6;
  const validWeight = 0.4;
  const populatedScore = (readiness.populatedFields / readiness.totalFields) * 100;
  const validScore = (readiness.validFields / readiness.totalFields) * 100;
  return Math.round((populatedScore * populatedWeight) + (validScore * validWeight));
}
```

## Channel Field Requirements

Each channel has a unique set of required fields:

| Field | Amazon | Flipkart | AgriBegri | Google Shopping | IndiaMART | Export |
|-------|--------|----------|-----------|-----------------|-----------|--------|
| Title | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| Description | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| SKU | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| MRP | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| Price | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| Images (≥3) | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| Category | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| Brand | ✓ | ✓ | | ✓ | ✓ | ✓ |
| Manufacturer | ✓ | ✓ | ✓ | | ✓ | ✓ |
| Country of Origin | ✓ | ✓ | ✓ | | | ✓ |
| HSN Code | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| GST Rate | ✓ | ✓ | ✓ | | ✓ | ✓ |
| Weight | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| Dimensions | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| FSSAI License | | | ✓ | | | |
| Organic Cert | | | ✓ | | | ✓ |
| Export License | | | | | | ✓ |
| HS Code (6-digit) | | | | | | ✓ |

## Missing Fields Tracking

```ts
interface MissingField {
  field: string;
  channelId: string;
  severity: 'required' | 'recommended' | 'optional';
  message: string;
}

interface MarketplaceReadinessResult {
  productId: string;
  productName: string;
  sku: string;
  channels: ChannelReadiness[];
  overallScore: number;         // average across all channels
  bestChannel: string;          // channel with highest score
  worstChannel: string;         // channel with lowest score
  totalMissingFields: number;
  timestamp: string;
}
```

## Warnings and Recommendations

Warnings are non-blocking issues that may affect listing quality:

```ts
const WARNINGS = {
  amazon: [
    { field: 'description', message: 'Description should be 500+ characters for better ranking' },
    { field: 'images',      message: 'Amazon requires at least 3 high-resolution images (1000×1000)' },
    { field: 'keywords',    message: 'Add 5+ search terms for better discoverability' },
  ],
  flipkart: [
    { field: 'description', message: 'Flipkart recommends bullet points for key features' },
    { field: 'warranty',    message: 'Warranty information improves conversion' },
  ],
  agribegri: [
    { field: 'organic_cert', message: 'Organic certification increases buyer trust on AgriBegri' },
    { field: 'yield_info',   message: 'Expected yield information recommended for agricultural products' },
  ],
  google_shopping: [
    { field: 'gtin',        message: 'GTIN/UPC improves Google Shopping visibility' },
    { field: 'availability', message: 'Set accurate availability status' },
  ],
  indiamart: [
    { field: 'min_order',   message: 'Minimum order quantity recommended for B2B listings' },
    { field: 'moq',         message: 'MOQ information helps serious buyers' },
  ],
  export: [
    { field: 'incoterms',   message: 'Incoterms information required for export listings' },
    { field: 'packaging',   message: 'Export-grade packaging details recommended' },
  ],
};
```

## MarketplaceReadiness Component

`MarketplaceReadiness.tsx` renders:

1. **Overall summary**: Average readiness score across all channels with best/worst channel indicators
2. **Channel grid**: 6 channel cards with score, status, and action button
3. **Channel detail view**: Expandable panel with missing fields, warnings, recommendations
4. **Comparison view**: Side-by-side field comparison across channels
5. **Export report**: Download channel readiness report as CSV (placeholder)

```tsx
<MarketplaceReadiness productId={productId}>
  <MarketplaceSummary
    overallScore={overallScore}
    bestChannel={bestChannel}
    worstChannel={worstChannel}
  />
  <ChannelGrid>
    {channels.map(channel => (
      <ChannelCard
        key={channel.channelId}
        channel={channel}
        onSelect={() => setSelectedChannel(channel.channelId)}
      />
    ))}
  </ChannelGrid>
  {selectedChannel && (
    <ChannelDetail
      channel={getChannel(selectedChannel)}
      onUpdateField={handleFieldUpdate}
    />
  )}
</MarketplaceReadiness>
```

## Future Marketplace API Integration

```ts
// Future integration placeholder
interface MarketplaceAPIIntegration {
  amazon: {
    syncListing: (productId: string) => Promise<AmazonListingResult>;
    validateASIN: (asin: string) => Promise<ASINValidation>;
    getCategoryRequirements: (categoryId: string) => Promise<CategoryRequirements>;
  };
  flipkart: {
    syncListing: (productId: string) => Promise<FlipkartListingResult>;
    validateListing: (data: FlipkartListingData) => Promise<ValidationResult>;
  };
  agribegri: {
    syncProduct: (productId: string) => Promise<AgriBegriResult>;
  };
  googleShopping: {
    generateFeed: (productIds: string[]) => Promise<FeedResult>;
    validateFeedEntry: (entry: ShoppingFeedEntry) => Promise<FeedValidation>;
  };
  indiamart: {
    syncCatalog: (productId: string) => Promise<IndiaMARTResult>;
  };
}
```

## Mock Data

```ts
const MOCK_MARKETPLACE_DATA: MarketplaceReadinessResult[] = [
  {
    productId: 'SK-PROD-1001',
    productName: 'Premium White Mushroom',
    sku: 'SK-PWM-001',
    channels: [
      {
        channelId: 'amazon',
        channelName: 'Amazon',
        score: 85,
        totalFields: 14,
        populatedFields: 13,
        validFields: 12,
        missingFields: [
          { field: 'brand', channelId: 'amazon', severity: 'required', message: 'Brand name required for Amazon listing' },
        ],
        warnings: ['Description should be 500+ characters'],
        recommendations: ['Add GTIN/UPC for better visibility'],
        status: 'needs_work',
      },
      // 5 more channels...
    ],
    overallScore: 78,
    bestChannel: 'indiamart',
    worstChannel: 'google_shopping',
    totalMissingFields: 8,
    timestamp: '2026-07-15T10:30:00Z',
  },
];
```

## Permissions

| Action | Viewer | Editor | Reviewer | Approver | Admin |
|--------|--------|--------|----------|----------|-------|
| View marketplace readiness | ✓ | ✓ | ✓ | ✓ | ✓ |
| View field gaps | ✓ | ✓ | ✓ | ✓ | ✓ |
| Update channel data | | ✓ | ✓ | ✓ | ✓ |
| Export channel report | | | ✓ | ✓ | ✓ |

## Mock Mode

All marketplace channel requirements are mock. No actual marketplace API calls are made. Field requirements are based on common marketplace standards but may not reflect current requirements on any specific platform. Readiness scores are for preview and demonstration only.
