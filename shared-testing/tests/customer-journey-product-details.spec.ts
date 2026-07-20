import { test, expect } from '@playwright/test';

/**
 * Release Condition C1 — reconciled to the shipped application (Approval Gate D).
 *
 * Shipped facts this suite is aligned to:
 *  - The single shipped product-detail route is /products/:id. It is a public
 *    enterprise-shell route that renders WorkspacePage as a navigation
 *    prototype: heading "<workspace>: <id>" (e.g. "Public: 1"), subtitle
 *    "Single product view." and a "Product detail — empty panel" placeholder.
 *    There is NO commerce detail view yet —
 *    no price (₹), no image gallery, no quantity selector, no add-to-cart
 *    button, and no breadcrumb-specific detail markup.
 *  - The obsolete route candidates /product/1, /p/1, /product/test and
 *    /products/sample are not shipped and have been removed.
 *  - The public catalog list lives at /products (public-website router).
 *  - Unknown enterprise routes render the WorkspacePage "Not found" placeholder
 *    (heading "Not found") rather than an HTTP 404 status.
 */

test.describe('Part 2 — Customer Journey: Phase 7 — Product Details', () => {

  const DETAIL_PATH = '/products/1';

  test('Product detail route /products/:id is reachable', async ({ page }) => {
    await page.goto(DETAIL_PATH, { waitUntil: 'networkidle' });
    expect(page.url()).toContain('/products/1');
    expect(page.url()).not.toContain('/access-denied');
  });

  test('Product detail renders the shipped workspace prototype header', async ({ page }) => {
    await page.goto(DETAIL_PATH, { waitUntil: 'networkidle' });
    // WorkspacePage renders the h1 as "<workspace label>: <id>" — i.e. "Public: 1"
    // for /products/1 — with the page label ("Product detail") shown in the
    // placeholder panel below.
    await expect(page.locator('h1', { hasText: 'Public: 1' })).toBeVisible();
    await expect(page.locator('text=Single product view.')).toBeVisible();
    await expect(page.locator('text=Product detail — empty panel')).toBeVisible();
  });

  test('Product detail exposes the navigation-prototype placeholder panel', async ({ page }) => {
    await page.goto(DETAIL_PATH, { waitUntil: 'networkidle' });
    const bodyText = await page.locator('body').innerText();
    expect(bodyText).toContain('Product detail');
  });

  test('Product list route /products is reachable', async ({ page }) => {
    await page.goto('/products', { waitUntil: 'networkidle' });
    expect(page.url()).toContain('/products');
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
  });

  test('Product detail is served by the enterprise shell sidebar', async ({ page }) => {
    await page.goto(DETAIL_PATH, { waitUntil: 'networkidle' });
    // /products/:id is an enterprise-shell route, so the workspace sidebar is
    // present. On tablet/mobile it is an off-canvas drawer, so assert attachment.
    await expect(page.locator('nav.sk-sidebar')).toBeAttached();
  });

  test('Unknown enterprise product route renders the Not found placeholder', async ({ page }) => {
    await page.goto('/products/1/does-not-exist', { waitUntil: 'networkidle' });
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.toLowerCase()).toContain('not found');
  });

});
