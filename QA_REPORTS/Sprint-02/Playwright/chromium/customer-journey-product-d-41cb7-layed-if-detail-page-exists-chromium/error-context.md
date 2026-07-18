# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: customer-journey-product-details.spec.ts >> Part 2 — Customer Journey: Phase 7 — Product Details >> Product price and description are displayed if detail page exists
- Location: tests\customer-journey-product-details.spec.ts:38:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/product/1
Call log:
  - navigating to "http://localhost:5173/product/1", waiting until "networkidle"

```

# Test source

```ts
  1   | import { test, expect } from '@playwright/test';
  2   | 
  3   | test.describe('Part 2 — Customer Journey: Phase 7 — Product Details', () => {
  4   | 
  5   |   test('Product detail routes are reachable', async ({ page }) => {
  6   |     const paths = ['/product/1', '/products/1', '/p/1', '/product/test', '/products/sample'];
  7   |     let found = false;
  8   |     for (const path of paths) {
  9   |       const response = await page.goto(path, { waitUntil: 'networkidle' });
  10  |       if (response?.status() !== 404) {
  11  |         found = true;
  12  |         expect(page.url()).toContain('product');
  13  |         break;
  14  |       }
  15  |     }
  16  |     if (!found) {
  17  |       await page.goto('/products', { waitUntil: 'networkidle' });
  18  |     }
  19  |   });
  20  | 
  21  |   test('Product image gallery renders if present', async ({ page }) => {
  22  |     const paths = ['/product/1', '/products/1', '/p/1', '/product/test', '/products/sample'];
  23  |     let foundDetail = false;
  24  |     for (const path of paths) {
  25  |       const response = await page.goto(path, { waitUntil: 'networkidle' });
  26  |       if (response?.status() !== 404) {
  27  |         foundDetail = true;
  28  |         break;
  29  |       }
  30  |     }
  31  |     if (foundDetail) {
  32  |       const images = page.locator('img');
  33  |       const count = await images.count();
  34  |       expect(count).toBeGreaterThan(0);
  35  |     }
  36  |   });
  37  | 
  38  |   test('Product price and description are displayed if detail page exists', async ({ page }) => {
  39  |     const paths = ['/product/1', '/products/1', '/p/1', '/product/test', '/products/sample'];
  40  |     let foundDetail = false;
  41  |     for (const path of paths) {
> 42  |       const response = await page.goto(path, { waitUntil: 'networkidle' });
      |                                   ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/product/1
  43  |       if (response?.status() !== 404) {
  44  |         foundDetail = true;
  45  |         break;
  46  |       }
  47  |     }
  48  |     if (foundDetail) {
  49  |       const bodyText = await page.locator('body').innerText();
  50  |       const hasPrice = bodyText.includes('₹') || bodyText.includes('Rs') || bodyText.includes('price') || bodyText.includes('Price');
  51  |       expect(hasPrice).toBeTruthy();
  52  |     }
  53  |   });
  54  | 
  55  |   test('Quantity selector works on detail page if present', async ({ page }) => {
  56  |     const paths = ['/product/1', '/products/1', '/p/1', '/product/test', '/products/sample'];
  57  |     let foundDetail = false;
  58  |     for (const path of paths) {
  59  |       const response = await page.goto(path, { waitUntil: 'networkidle' });
  60  |       if (response?.status() !== 404) {
  61  |         foundDetail = true;
  62  |         break;
  63  |       }
  64  |     }
  65  |     if (foundDetail) {
  66  |       const qtyInput = page.locator('input[type="number"], [class*="quantity"], [class*="Quantity"]');
  67  |       if (await qtyInput.isVisible().catch(() => false)) {
  68  |         await qtyInput.first().fill('2');
  69  |         await page.waitForTimeout(300);
  70  |       }
  71  |     }
  72  |   });
  73  | 
  74  |   test('Add to cart button exists on detail page if present', async ({ page }) => {
  75  |     const paths = ['/product/1', '/products/1', '/p/1', '/product/test', '/products/sample'];
  76  |     let foundDetail = false;
  77  |     for (const path of paths) {
  78  |       const response = await page.goto(path, { waitUntil: 'networkidle' });
  79  |       if (response?.status() !== 404) {
  80  |         foundDetail = true;
  81  |         break;
  82  |       }
  83  |     }
  84  |     if (foundDetail) {
  85  |       const addToCart = page.locator('button:has-text("Cart"), button:has-text("cart"), button:has-text("Buy"), [aria-label*="cart" i]');
  86  |       if (await addToCart.isVisible().catch(() => false)) {
  87  |         await expect(addToCart.first()).toBeVisible();
  88  |       }
  89  |     }
  90  |   });
  91  | 
  92  |   test('Breadcrumb navigation exists on detail page if present', async ({ page }) => {
  93  |     const paths = ['/product/1', '/products/1', '/p/1', '/product/test', '/products/sample'];
  94  |     let foundDetail = false;
  95  |     for (const path of paths) {
  96  |       const response = await page.goto(path, { waitUntil: 'networkidle' });
  97  |       if (response?.status() !== 404) {
  98  |         foundDetail = true;
  99  |         break;
  100 |       }
  101 |     }
  102 |     if (foundDetail) {
  103 |       const breadcrumb = page.locator('[class*="breadcrumb"], [class*="Breadcrumb"], nav[aria-label="breadcrumb"]');
  104 |       const present = await breadcrumb.isVisible().catch(() => false);
  105 |       expect(present).toBeTruthy();
  106 |     }
  107 |   });
  108 | 
  109 |   test('Invalid product URL shows 404 or error', async ({ page }) => {
  110 |     const response = await page.goto('/product/this-product-does-not-exist-999999', { waitUntil: 'networkidle' });
  111 |     if (response?.status() === 404) {
  112 |       await expect(page.locator('text=404, not found, Not Found').first()).toBeVisible();
  113 |     }
  114 |   });
  115 | 
  116 |   test('Back navigation from product page works', async ({ page }) => {
  117 |     await page.goto('/products', { waitUntil: 'networkidle' });
  118 |     const links = page.locator('a[href*="product"]');
  119 |     if (await links.first().isVisible().catch(() => false)) {
  120 |       await links.first().click();
  121 |       await page.waitForTimeout(1000);
  122 |       await page.goBack();
  123 |       await page.waitForLoadState('networkidle');
  124 |       expect(page.url()).toContain('product');
  125 |     }
  126 |   });
  127 | 
  128 | });
  129 | 
```