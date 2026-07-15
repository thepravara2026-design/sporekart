import type { Asset, AssetType, AssetStatus } from '../types';

export function mockDelay(ms = 400): Promise<void> {
  return new Promise((r) => setTimeout(r, ms));
}

function a(
  id: string,
  overrides: Partial<Asset> & { name: string; fileName: string; type: AssetType; url: string },
): Asset {
  const now = new Date();
  const base: Asset = {
    id,
    name: '',
    fileName: '',
    fileSize: 0,
    mimeType: 'image/jpeg',
    type: 'image',
    extension: 'jpg',
    url: '',
    thumbnailUrl: '',
    alt: '',
    title: '',
    description: '',
    tags: [],
    collectionIds: [],
    productIds: [],
    status: 'published' as AssetStatus,
    version: 1,
    versions: [],
    createdBy: 'Anita Sharma',
    updatedBy: 'Anita Sharma',
    createdAt: new Date(now.getTime() - Math.random() * 90 * 86400000).toISOString(),
    updatedAt: new Date(now.getTime() - Math.random() * 7 * 86400000).toISOString(),
  };
  const asset = { ...base, ...overrides } satisfies Asset;
  asset.thumbnailUrl = asset.thumbnailUrl || asset.url;
  asset.versions = [
    {
      id: `${id}-v1`,
      version: 1,
      fileSize: asset.fileSize,
      url: asset.url,
      width: asset.width,
      height: asset.height,
      createdAt: asset.createdAt,
      createdBy: 'Anita Sharma',
      reason: 'Original upload',
    },
  ];
  return asset;
}

function sz(mb: number): number {
  return mb * 1024 * 1024;
}

export function imgUrl(seed: string, w = 400, h = 400): string {
  return `https://picsum.photos/seed/${seed}/${w}/${h}`;
}

export const MOCK_ASSETS: Asset[] = [
  a('ast-001', { name: 'Organic White Mushrooms', fileName: 'organic-white-mushrooms.jpg', type: 'image', url: imgUrl('mush1', 800, 600), thumbnailUrl: imgUrl('mush1'), width: 800, height: 600, fileSize: sz(2.4), mimeType: 'image/jpeg', extension: 'jpg', alt: 'Fresh organic white mushrooms on a wooden table', title: 'Organic White Mushrooms', description: 'High-resolution product shot of organic white mushrooms.', tags: ['mushroom', 'white', 'organic', 'product'], collectionIds: ['col-001', 'col-003'], productIds: ['PRD-001'], createdAt: '2026-06-01T10:00:00Z', updatedAt: '2026-06-10T14:00:00Z' }),
  a('ast-002', { name: 'Shiitake Mushroom Close-up', fileName: 'shiitake-closeup.jpg', type: 'image', url: imgUrl('shit1', 800, 600), thumbnailUrl: imgUrl('shit1'), width: 800, height: 600, fileSize: sz(1.8), mimeType: 'image/jpeg', extension: 'jpg', alt: 'Close-up of fresh shiitake mushrooms', title: 'Shiitake Mushroom Close-up', description: 'Macro shot highlighting the cap texture of shiitake mushrooms.', tags: ['shiitake', 'closeup', 'texture'], collectionIds: ['col-001'], productIds: ['PRD-002'], createdAt: '2026-06-03T09:00:00Z', updatedAt: '2026-06-11T11:00:00Z' }),
  a('ast-003', { name: 'Mushroom Growing Kit Box', fileName: 'growing-kit-box.png', type: 'image', url: imgUrl('kit1', 800, 800), thumbnailUrl: imgUrl('kit1'), width: 800, height: 800, fileSize: sz(3.2), mimeType: 'image/png', extension: 'png', alt: 'Mushroom growing kit packaging', title: 'Mushroom Growing Kit Box', description: 'Product packaging shot for the home growing kit.', tags: ['kit', 'packaging', 'product'], collectionIds: ['col-001', 'col-002'], productIds: ['PRD-004'], createdBy: 'Rahul Verma', updatedBy: 'Rahul Verma', createdAt: '2026-05-28T08:00:00Z', updatedAt: '2026-06-09T16:00:00Z' }),
  a('ast-004', { name: 'Oyster Mushroom Banner', fileName: 'oyster-banner.jpg', type: 'image', url: imgUrl('oyst1', 1200, 400), thumbnailUrl: imgUrl('oyst1', 400, 133), width: 1200, height: 400, fileSize: sz(1.2), mimeType: 'image/jpeg', extension: 'jpg', alt: 'Oyster mushrooms growing in clusters', title: 'Oyster Mushroom Banner', description: 'Wide banner image of oyster mushroom clusters.', tags: ['oyster', 'banner', 'hero'], collectionIds: ['col-003'], productIds: [], createdAt: '2026-05-20T12:00:00Z', updatedAt: '2026-06-01T10:00:00Z' }),
  a('ast-005', { name: 'Harvest Video - Button Mushrooms', fileName: 'harvest-button.mp4', type: 'video', url: 'https://www.w3schools.com/html/mov_bbb.mp4', thumbnailUrl: imgUrl('harv1'), width: 640, height: 360, fileSize: sz(15), mimeType: 'video/mp4', extension: 'mp4', duration: 42, alt: 'Harvesting button mushrooms time-lapse', title: 'Harvest Video - Button Mushrooms', description: 'Time-lapse video of button mushroom harvest at the farm.', tags: ['harvest', 'video', 'farm', 'button'], collectionIds: ['col-004'], productIds: ['PRD-001'], createdBy: 'Priya Patel', updatedBy: 'Priya Patel', createdAt: '2026-06-05T07:00:00Z', updatedAt: '2026-06-12T09:00:00Z' }),
  a('ast-006', { name: 'Product Label Template', fileName: 'product-label-template.pdf', type: 'document', url: '#', thumbnailUrl: '', fileSize: sz(0.4), mimeType: 'application/pdf', extension: 'pdf', alt: 'Product label PDF template', title: 'Product Label Template', description: 'Editable PDF template for product labels.', tags: ['template', 'label', 'pdf'], collectionIds: ['col-005'], productIds: [], createdBy: 'Suresh Kumar', updatedBy: 'Suresh Kumar', createdAt: '2026-05-15T11:00:00Z', updatedAt: '2026-05-15T11:00:00Z' }),
  a('ast-007', { name: 'Enoki Mushroom Macro', fileName: 'enoki-macro.jpg', type: 'image', url: imgUrl('enok1', 800, 600), thumbnailUrl: imgUrl('enok1'), width: 800, height: 600, fileSize: sz(2.1), mimeType: 'image/jpeg', extension: 'jpg', alt: 'Macro photo of enoki mushrooms', title: 'Enoki Mushroom Macro', description: 'Detailed macro shot of delicate enoki mushrooms.', tags: ['enoki', 'macro', 'delicate'], collectionIds: ['col-001'], productIds: ['PRD-003'], createdAt: '2026-06-02T13:00:00Z', updatedAt: '2026-06-10T10:00:00Z' }),
  a('ast-008', { name: 'Farm Aerial View', fileName: 'farm-aerial.jpg', type: 'image', url: imgUrl('farm1', 1200, 800), thumbnailUrl: imgUrl('farm1', 400, 267), width: 1200, height: 800, fileSize: sz(3.8), mimeType: 'image/jpeg', extension: 'jpg', alt: 'Aerial view of the mushroom farm', title: 'Farm Aerial View', description: 'Drone shot of the entire farm facility.', tags: ['farm', 'aerial', 'drone', 'facility'], collectionIds: ['col-003', 'col-004'], productIds: [], createdAt: '2026-04-10T09:00:00Z', updatedAt: '2026-05-20T14:00:00Z' }),
  a('ast-009', { name: 'Packaging Line Operation', fileName: 'packaging-line.mp4', type: 'video', url: 'https://www.w3schools.com/html/mov_bbb.mp4', thumbnailUrl: imgUrl('pack1'), width: 640, height: 360, fileSize: sz(22), mimeType: 'video/mp4', extension: 'mp4', duration: 120, alt: 'Packaging line in operation', title: 'Packaging Line Operation', description: 'Video showing the automated packaging line.', tags: ['packaging', 'automation', 'facility'], collectionIds: ['col-004'], productIds: [], createdBy: 'Priya Patel', updatedBy: 'Priya Patel', createdAt: '2026-06-07T10:00:00Z', updatedAt: '2026-06-13T08:00:00Z' }),
  a('ast-010', { name: 'Nutrition Facts Sheet', fileName: 'nutrition-facts.pdf', type: 'document', url: '#', thumbnailUrl: '', fileSize: sz(0.2), mimeType: 'application/pdf', extension: 'pdf', alt: 'Nutrition facts information sheet', title: 'Nutrition Facts Sheet', description: 'Official nutrition facts for all mushroom products.', tags: ['nutrition', 'label', 'compliance'], collectionIds: ['col-005'], productIds: ['PRD-001', 'PRD-002', 'PRD-003'], createdAt: '2026-03-01T08:00:00Z', updatedAt: '2026-03-01T08:00:00Z' }),
  a('ast-011', { name: 'Mushroom 3D Model - White Button', fileName: 'white-button-3d.obj', type: '3d_model', url: '#', thumbnailUrl: imgUrl('model1'), width: 512, height: 512, fileSize: sz(8.5), mimeType: 'model/obj', extension: 'obj', alt: '3D model of a white button mushroom', title: 'Mushroom 3D Model - White Button', description: 'Rotatable 3D model for interactive product display.', tags: ['3d', 'model', 'interactive'], collectionIds: ['col-006'], productIds: ['PRD-001'], createdBy: 'Arjun Nair', updatedBy: 'Arjun Nair', createdAt: '2026-06-10T15:00:00Z', updatedAt: '2026-06-14T12:00:00Z' }),
  a('ast-012', { name: 'King Oyster Mushroom', fileName: 'king-oyster.jpg', type: 'image', url: imgUrl('king1', 800, 600), thumbnailUrl: imgUrl('king1'), width: 800, height: 600, fileSize: sz(1.9), mimeType: 'image/jpeg', extension: 'jpg', alt: 'King oyster mushroom on dark background', title: 'King Oyster Mushroom', description: 'Studio shot of a premium king oyster mushroom.', tags: ['oyster', 'king', 'premium', 'product'], collectionIds: ['col-001'], productIds: ['PRD-005'], createdAt: '2026-06-08T11:00:00Z', updatedAt: '2026-06-14T09:00:00Z' }),
  a('ast-013', { name: 'Product Catalog Cover', fileName: 'catalog-cover.jpg', type: 'image', url: imgUrl('cat1', 800, 1000), thumbnailUrl: imgUrl('cat1'), width: 800, height: 1000, fileSize: sz(2.7), mimeType: 'image/jpeg', extension: 'jpg', alt: 'Product catalog cover design', title: 'Product Catalog Cover', description: 'Cover image for the seasonal product catalog.', tags: ['catalog', 'cover', 'marketing'], collectionIds: ['col-002', 'col-003'], productIds: [], createdAt: '2026-05-25T16:00:00Z', updatedAt: '2026-06-05T10:00:00Z' }),
  a('ast-014', { name: 'How-to Video: Grow at Home', fileName: 'how-to-grow.mp4', type: 'video', url: 'https://www.w3schools.com/html/mov_bbb.mp4', thumbnailUrl: imgUrl('grow1'), width: 640, height: 360, fileSize: sz(45), mimeType: 'video/mp4', extension: 'mp4', duration: 300, alt: 'Step-by-step guide to growing mushrooms at home', title: 'How-to Video: Grow at Home', description: 'Tutorial video for customers on using the growing kit.', tags: ['tutorial', 'growing', 'how-to', 'customer'], collectionIds: ['col-004', 'col-007'], productIds: ['PRD-004'], createdBy: 'Priya Patel', updatedBy: 'Priya Patel', createdAt: '2026-06-12T06:00:00Z', updatedAt: '2026-06-15T10:00:00Z' }),
  a('ast-015', { name: 'Social Media Post - Recipe', fileName: 'recipe-post.jpg', type: 'image', url: imgUrl('recipe1', 600, 600), thumbnailUrl: imgUrl('recipe1'), width: 600, height: 600, fileSize: sz(1.5), mimeType: 'image/jpeg', extension: 'jpg', alt: 'Mushroom recipe social media graphic', title: 'Social Media Post - Recipe', description: 'Square social media graphic featuring a mushroom recipe.', tags: ['social', 'recipe', 'marketing', 'square'], collectionIds: ['col-002'], productIds: [], createdAt: '2026-06-11T14:00:00Z', updatedAt: '2026-06-14T11:00:00Z' }),
  a('ast-016', { name: 'Mushroom Compost Guide', fileName: 'compost-guide.pdf', type: 'document', url: '#', thumbnailUrl: '', fileSize: sz(0.6), mimeType: 'application/pdf', extension: 'pdf', alt: 'Guide to mushroom composting techniques', title: 'Mushroom Compost Guide', description: 'Detailed guide on composting techniques for mushroom cultivation.', tags: ['compost', 'guide', 'educational'], collectionIds: ['col-005', 'col-007'], productIds: [], createdAt: '2026-04-20T09:00:00Z', updatedAt: '2026-04-20T09:00:00Z' }),
  a('ast-017', { name: 'Creamy Mushroom Soup Bowl', fileName: 'soup-bowl.jpg', type: 'image', url: imgUrl('soup1', 800, 600), thumbnailUrl: imgUrl('soup1'), width: 800, height: 600, fileSize: sz(2.3), mimeType: 'image/jpeg', extension: 'jpg', alt: 'Bowl of creamy mushroom soup with herbs', title: 'Creamy Mushroom Soup Bowl', description: 'Lifestyle food photography for recipe content.', tags: ['recipe', 'food', 'soup', 'lifestyle'], collectionIds: ['col-002', 'col-003'], productIds: [], createdAt: '2026-06-09T12:00:00Z', updatedAt: '2026-06-13T15:00:00Z' }),
  a('ast-018', { name: 'Warehouse Interior', fileName: 'warehouse.jpg', type: 'image', url: imgUrl('ware1', 800, 600), thumbnailUrl: imgUrl('ware1'), width: 800, height: 600, fileSize: sz(2.0), mimeType: 'image/jpeg', extension: 'jpg', alt: 'Interior of the cold storage warehouse', title: 'Warehouse Interior', description: 'Cold storage warehouse with mushroom inventory.', tags: ['warehouse', 'storage', 'facility', 'cold'], collectionIds: ['col-004'], productIds: [], createdAt: '2026-05-30T08:00:00Z', updatedAt: '2026-06-02T10:00:00Z' }),
  a('ast-019', { name: 'Brand Logo - High Res', fileName: 'brand-logo.png', type: 'image', url: imgUrl('logo1', 400, 400), thumbnailUrl: imgUrl('logo1'), width: 400, height: 400, fileSize: sz(0.8), mimeType: 'image/png', extension: 'png', alt: 'Company brand logo high resolution', title: 'Brand Logo - High Res', description: 'Official brand logo in high resolution for print.', tags: ['brand', 'logo', 'print'], collectionIds: ['col-005'], productIds: [], createdAt: '2026-01-15T10:00:00Z', updatedAt: '2026-01-15T10:00:00Z' }),
  a('ast-020', { name: 'Truffle Mushroom Oil Bottle', fileName: 'truffle-oil.jpg', type: 'image', url: imgUrl('truff1', 600, 800), thumbnailUrl: imgUrl('truff1'), width: 600, height: 800, fileSize: sz(2.6), mimeType: 'image/jpeg', extension: 'jpg', alt: 'Truffle mushroom oil product bottle', title: 'Truffle Mushroom Oil Bottle', description: 'Product shot of truffle-infused mushroom oil.', tags: ['truffle', 'oil', 'product', 'bottle'], collectionIds: ['col-001', 'col-002'], productIds: ['PRD-006'], createdAt: '2026-06-06T14:00:00Z', updatedAt: '2026-06-12T09:00:00Z' }),
  a('ast-021', { name: 'Team Photo - Harvest Crew', fileName: 'team-harvest.jpg', type: 'image', url: imgUrl('team1', 800, 600), thumbnailUrl: imgUrl('team1'), width: 800, height: 600, fileSize: sz(2.9), mimeType: 'image/jpeg', extension: 'jpg', alt: 'Harvest crew team photo', title: 'Team Photo - Harvest Crew', description: 'Group photo of the morning harvest team.', tags: ['team', 'people', 'harvest', 'culture'], collectionIds: ['col-004', 'col-007'], productIds: [], createdAt: '2026-05-18T07:00:00Z', updatedAt: '2026-05-18T07:00:00Z' }),
  a('ast-022', { name: 'Mushroom Infographic - Benefits', fileName: 'benefits-infographic.jpg', type: 'image', url: imgUrl('info1', 600, 900), thumbnailUrl: imgUrl('info1'), width: 600, height: 900, fileSize: sz(1.7), mimeType: 'image/jpeg', extension: 'jpg', alt: 'Infographic about mushroom health benefits', title: 'Mushroom Infographic - Benefits', description: 'Educational infographic on nutritional benefits of mushrooms.', tags: ['infographic', 'health', 'education', 'nutrition'], collectionIds: ['col-007', 'col-003'], productIds: [], createdAt: '2026-05-22T11:00:00Z', updatedAt: '2026-05-22T11:00:00Z' }),
  a('ast-023', { name: 'Product Comparison Chart', fileName: 'comparison-chart.pdf', type: 'document', url: '#', thumbnailUrl: '', fileSize: sz(0.3), mimeType: 'application/pdf', extension: 'pdf', alt: 'Product comparison chart for buyers', title: 'Product Comparison Chart', description: 'Specification comparison across all mushroom variants.', tags: ['comparison', 'specs', 'buyer'], collectionIds: ['col-005'], productIds: ['PRD-001', 'PRD-002', 'PRD-003', 'PRD-005'], createdAt: '2026-03-10T09:00:00Z', updatedAt: '2026-03-10T09:00:00Z' }),
  a('ast-024', { name: 'Customer Testimonial Video', fileName: 'testimonial-1.mp4', type: 'video', url: 'https://www.w3schools.com/html/mov_bbb.mp4', thumbnailUrl: imgUrl('test1'), width: 640, height: 360, fileSize: sz(18), mimeType: 'video/mp4', extension: 'mp4', duration: 90, alt: 'Customer testimonial about mushroom quality', title: 'Customer Testimonial Video', description: 'Video testimonial from a repeat wholesale customer.', tags: ['testimonial', 'customer', 'marketing'], collectionIds: ['col-002', 'col-004'], productIds: [], createdBy: 'Meera Joshi', updatedBy: 'Meera Joshi', createdAt: '2026-06-13T10:00:00Z', updatedAt: '2026-06-15T08:00:00Z' }),
  a('ast-025', { name: 'Lion\'s Mane Mushroom', fileName: 'lions-mane.jpg', type: 'image', url: imgUrl('lion1', 800, 600), thumbnailUrl: imgUrl('lion1'), width: 800, height: 600, fileSize: sz(2.2), mimeType: 'image/jpeg', extension: 'jpg', alt: 'Lion\'s mane mushroom macro shot', title: 'Lion\'s Mane Mushroom', description: 'Striking macro image of a lion\'s mane mushroom.', tags: ['lions-mane', 'macro', 'specialty'], collectionIds: ['col-001'], productIds: ['PRD-007'], createdAt: '2026-06-14T15:00:00Z', updatedAt: '2026-06-16T10:00:00Z' }),
  a('ast-026', { name: 'Mushroom Cultivation Infographic', fileName: 'cultivation-steps.jpg', type: 'image', url: imgUrl('cult1', 600, 900), thumbnailUrl: imgUrl('cult1'), width: 600, height: 900, fileSize: sz(1.9), mimeType: 'image/jpeg', extension: 'jpg', alt: 'Step-by-step mushroom cultivation guide', title: 'Mushroom Cultivation Infographic', description: 'Visual guide showing cultivation steps from spawn to harvest.', tags: ['cultivation', 'guide', 'infographic', 'educational'], collectionIds: ['col-007'], productIds: [], createdAt: '2026-05-10T10:00:00Z', updatedAt: '2026-05-10T10:00:00Z' }),
  a('ast-027', { name: 'Marketing Banner - Summer Sale', fileName: 'summer-sale-banner.jpg', type: 'image', url: imgUrl('summer1', 1200, 400), thumbnailUrl: imgUrl('summer1', 400, 133), width: 1200, height: 400, fileSize: sz(1.4), mimeType: 'image/jpeg', extension: 'jpg', alt: 'Summer sale marketing banner', title: 'Marketing Banner - Summer Sale', description: 'Seasonal promotional banner for summer sale campaign.', tags: ['banner', 'marketing', 'sale', 'summer'], collectionIds: ['col-002', 'col-003'], productIds: [], createdAt: '2026-06-15T08:00:00Z', updatedAt: '2026-06-16T12:00:00Z' }),
  a('ast-028', { name: 'Packaging Design - Variant 2', fileName: 'packaging-v2.ai', type: 'document', url: '#', thumbnailUrl: imgUrl('pkg2'), width: 400, height: 400, fileSize: sz(4.2), mimeType: 'application/postscript', extension: 'ai', alt: 'Packaging design file variant 2', title: 'Packaging Design - Variant 2', description: 'Adobe Illustrator source file for packaging design iteration 2.', tags: ['packaging', 'design', 'ai', 'source'], collectionIds: ['col-005'], productIds: [], createdBy: 'Arjun Nair', updatedBy: 'Arjun Nair', createdAt: '2026-05-12T14:00:00Z', updatedAt: '2026-05-12T14:00:00Z' }),
  a('ast-029', { name: 'Mushroom Sizzle Reel', fileName: 'sizzle-reel.mp4', type: 'video', url: 'https://www.w3schools.com/html/mov_bbb.mp4', thumbnailUrl: imgUrl('sizz1'), width: 1280, height: 720, fileSize: sz(85), mimeType: 'video/mp4', extension: 'mp4', duration: 60, alt: 'Brand sizzle reel showcasing mushrooms', title: 'Mushroom Sizzle Reel', description: 'High-impact brand video for website hero section.', tags: ['brand', 'video', 'hero', 'marketing'], collectionIds: ['col-003', 'col-004'], productIds: [], createdBy: 'Meera Joshi', updatedBy: 'Meera Joshi', createdAt: '2026-06-10T16:00:00Z', updatedAt: '2026-06-15T09:00:00Z' }),
  a('ast-030', { name: 'Chanterelle Mushroom', fileName: 'chanterelle.jpg', type: 'image', url: imgUrl('chant1', 800, 600), thumbnailUrl: imgUrl('chant1'), width: 800, height: 600, fileSize: sz(2.0), mimeType: 'image/jpeg', extension: 'jpg', alt: 'Golden chanterelle mushrooms', title: 'Chanterelle Mushroom', description: 'Studio shot of golden chanterelle mushrooms.', tags: ['chanterelle', 'golden', 'specialty', 'product'], collectionIds: ['col-001'], productIds: ['PRD-008'], createdAt: '2026-06-11T09:00:00Z', updatedAt: '2026-06-14T14:00:00Z' }),
  a('ast-031', { name: 'Background Texture - Mushroom Spores', fileName: 'spore-texture.jpg', type: 'image', url: imgUrl('spore1', 800, 600), thumbnailUrl: imgUrl('spore1'), width: 800, height: 600, fileSize: sz(3.1), mimeType: 'image/jpeg', extension: 'jpg', alt: 'Abstract texture of mushroom spores under microscope', title: 'Background Texture - Mushroom Spores', description: 'Scientific/artistic texture for background use.', tags: ['texture', 'background', 'spore', 'abstract'], collectionIds: ['col-006', 'col-007'], productIds: [], createdAt: '2026-04-05T13:00:00Z', updatedAt: '2026-04-05T13:00:00Z' }),
  a('ast-032', { name: 'Mushroom Audio - Farm Ambience', fileName: 'farm-ambience.mp3', type: 'audio', url: '#', thumbnailUrl: '', fileSize: sz(5.0), mimeType: 'audio/mpeg', extension: 'mp3', duration: 180, alt: 'Ambient farm soundscape', title: 'Mushroom Audio - Farm Ambience', description: 'Ambient audio recording of the farm environment.', tags: ['audio', 'ambience', 'farm'], collectionIds: ['col-004'], productIds: [], createdBy: 'Suresh Kumar', updatedBy: 'Suresh Kumar', createdAt: '2026-05-08T10:00:00Z', updatedAt: '2026-05-08T10:00:00Z' }),
  a('ast-033', { name: 'Supplier Certificate - Organic', fileName: 'organic-cert.pdf', type: 'document', url: '#', thumbnailUrl: '', fileSize: sz(0.15), mimeType: 'application/pdf', extension: 'pdf', alt: 'Organic certification document', title: 'Supplier Certificate - Organic', description: 'Official organic certification from the supplier.', tags: ['certification', 'organic', 'compliance', 'legal'], collectionIds: ['col-005'], productIds: ['PRD-001', 'PRD-002', 'PRD-003'], createdAt: '2026-02-01T09:00:00Z', updatedAt: '2026-02-01T09:00:00Z' }),
  a('ast-034', { name: 'Morel Mushroom', fileName: 'morel.jpg', type: 'image', url: imgUrl('morel1', 800, 600), thumbnailUrl: imgUrl('morel1'), width: 800, height: 600, fileSize: sz(2.5), mimeType: 'image/jpeg', extension: 'jpg', alt: 'Fresh morel mushrooms', title: 'Morel Mushroom', description: 'Premium morel mushroom product photography.', tags: ['morel', 'premium', 'product', 'wild'], collectionIds: ['col-001'], productIds: ['PRD-009'], createdAt: '2026-06-13T10:00:00Z', updatedAt: '2026-06-16T08:00:00Z' }),
  a('ast-035', { name: 'Email Campaign Header', fileName: 'email-header.jpg', type: 'image', url: imgUrl('email1', 600, 200), thumbnailUrl: imgUrl('email1', 400, 133), width: 600, height: 200, fileSize: sz(0.6), mimeType: 'image/jpeg', extension: 'jpg', alt: 'Email marketing campaign header', title: 'Email Campaign Header', description: 'Header image for the monthly newsletter campaign.', tags: ['email', 'marketing', 'campaign', 'header'], collectionIds: ['col-002'], productIds: [], createdAt: '2026-06-14T09:00:00Z', updatedAt: '2026-06-15T11:00:00Z' }),
  a('ast-036', { name: 'Reishi Mushroom', fileName: 'reishi.jpg', type: 'image', url: imgUrl('reishi1', 800, 600), thumbnailUrl: imgUrl('reishi1'), width: 800, height: 600, fileSize: sz(2.1), mimeType: 'image/jpeg', extension: 'jpg', alt: 'Reishi mushroom medicinal', title: 'Reishi Mushroom', description: 'Medicinal reishi mushroom product image.', tags: ['reishi', 'medicinal', 'product', 'specialty'], collectionIds: ['col-001'], productIds: ['PRD-010'], createdAt: '2026-06-12T11:00:00Z', updatedAt: '2026-06-15T14:00:00Z' }),
  a('ast-037', { name: '3D Model - Mushroom Bundle', fileName: 'bundle-3d.glb', type: '3d_model', url: '#', thumbnailUrl: imgUrl('bundle1'), width: 512, height: 512, fileSize: sz(12.0), mimeType: 'model/gltf-binary', extension: 'glb', alt: '3D model bundle of various mushrooms', title: '3D Model - Mushroom Bundle', description: 'Collection of 3D mushroom models for AR product viewer.', tags: ['3d', 'bundle', 'ar', 'interactive'], collectionIds: ['col-006'], productIds: ['PRD-001', 'PRD-002', 'PRD-005'], createdBy: 'Arjun Nair', updatedBy: 'Arjun Nair', createdAt: '2026-06-12T16:00:00Z', updatedAt: '2026-06-15T12:00:00Z' }),
  a('ast-038', { name: 'Product Shot - Assorted Mushrooms', fileName: 'assorted.jpg', type: 'image', url: imgUrl('assort1', 800, 600), thumbnailUrl: imgUrl('assort1'), width: 800, height: 600, fileSize: sz(2.8), mimeType: 'image/jpeg', extension: 'jpg', alt: 'Assorted fresh mushrooms on display', title: 'Product Shot - Assorted Mushrooms', description: 'Hero image showing an assortment of fresh mushrooms.', tags: ['assorted', 'hero', 'product', 'display'], collectionIds: ['col-001', 'col-003'], productIds: [], createdAt: '2026-06-07T09:00:00Z', updatedAt: '2026-06-10T14:00:00Z' }),
  a('ast-039', { name: 'Mushroom Pâté Recipe Card', fileName: 'pate-recipe.pdf', type: 'document', url: '#', thumbnailUrl: '', fileSize: sz(0.3), mimeType: 'application/pdf', extension: 'pdf', alt: 'Mushroom pâté recipe card', title: 'Mushroom Pâté Recipe Card', description: 'Printable recipe card for mushroom pâté.', tags: ['recipe', 'card', 'printable', 'pdf'], collectionIds: ['col-002', 'col-005'], productIds: [], createdAt: '2026-06-10T10:00:00Z', updatedAt: '2026-06-10T10:00:00Z' }),
  a('ast-040', { name: 'Mushroom Voiceover - Brand Story', fileName: 'brand-story.mp3', type: 'audio', url: '#', thumbnailUrl: '', fileSize: sz(8.0), mimeType: 'audio/mpeg', extension: 'mp3', duration: 240, alt: 'Brand story voiceover narration', title: 'Mushroom Voiceover - Brand Story', description: 'Professional voiceover for the About Us video.', tags: ['audio', 'voiceover', 'brand', 'narration'], collectionIds: ['col-004', 'col-003'], productIds: [], createdBy: 'Meera Joshi', updatedBy: 'Meera Joshi', createdAt: '2026-06-08T14:00:00Z', updatedAt: '2026-06-08T14:00:00Z' }),
];

export function getAssetById(id: string): Asset | undefined {
  return MOCK_ASSETS.find((a) => a.id === id);
}

export function getAssetsByCollection(collectionId: string): Asset[] {
  return MOCK_ASSETS.filter((a) => a.collectionIds.includes(collectionId));
}

export function getAssetTypeIcon(type: AssetType): string {
  const icons: Record<AssetType, string> = {
    image: 'image',
    video: 'video',
    document: 'file-text',
    '3d_model': 'box',
    audio: 'music',
  };
  return icons[type];
}

export function getAssetTypeColor(type: AssetType): string {
  const colors: Record<AssetType, string> = {
    image: 'var(--color-accent-blue)',
    video: 'var(--color-accent-purple)',
    document: 'var(--color-accent-orange)',
    '3d_model': 'var(--color-accent-green)',
    audio: 'var(--color-accent-pink)',
  };
  return colors[type];
}
