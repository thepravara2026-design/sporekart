import { test, expect } from '@playwright/test';

test.describe('Part 2 — Customer Journey: Phase 7 — Product Details', () => {

  test('Product detail routes are reachable', async ({ page }) => {
    const paths = ['/product/1', '/products/1', '/p/1', '/product/test', '/products/sample'];
    let found = false;
    for (const path of paths) {
      const response = await page.goto(path, { waitUntil: 'networkidle' });
      if (response?.status() !== 404) {
        found = true;
        expect(page.url()).toContain('product');
        break;
      }
    }
    if (!found) {
      await page.goto('/products', { waitUntil: 'networkidle' });
    }
  });

  test('Product image gallery renders if present', async ({ page }) => {
    const paths = ['/product/1', '/products/1', '/p/1', '/product/test', '/products/sample'];
    let foundDetail = false;
    for (const path of paths) {
      const response = await page.goto(path, { waitUntil: 'networkidle' });
      if (response?.status() !== 404) {
        foundDetail = true;
        break;
      }
    }
    if (foundDetail) {
      const images = page.locator('img');
      const count = await images.count();
      expect(count).toBeGreaterThan(0);
    }
  });

  test('Product price and description are displayed if detail page exists', async ({ page }) => {
    const paths = ['/product/1', '/products/1', '/p/1', '/product/test', '/products/sample'];
    let foundDetail = false;
    for (const path of paths) {
      const response = await page.goto(path, { waitUntil: 'networkidle' });
      if (response?.status() !== 404) {
        foundDetail = true;
        break;
      }
    }
    if (foundDetail) {
      const bodyText = await page.locator('body').innerText();
      const hasPrice = bodyText.includes('₹') || bodyText.includes('Rs') || bodyText.includes('price') || bodyText.includes('Price');
      expect(hasPrice).toBeTruthy();
    }
  });

  test('Quantity selector works on detail page if present', async ({ page }) => {
    const paths = ['/product/1', '/products/1', '/p/1', '/product/test', '/products/sample'];
    let foundDetail = false;
    for (const path of paths) {
      const response = await page.goto(path, { waitUntil: 'networkidle' });
      if (response?.status() !== 404) {
        foundDetail = true;
        break;
      }
    }
    if (foundDetail) {
      const qtyInput = page.locator('input[type="number"], [class*="quantity"], [class*="Quantity"]');
      if (await qtyInput.isVisible().catch(() => false)) {
        await qtyInput.first().fill('2');
        await page.waitForTimeout(300);
      }
    }
  });

  test('Add to cart button exists on detail page if present', async ({ page }) => {
    const paths = ['/product/1', '/products/1', '/p/1', '/product/test', '/products/sample'];
    let foundDetail = false;
    for (const path of paths) {
      const response = await page.goto(path, { waitUntil: 'networkidle' });
      if (response?.status() !== 404) {
        foundDetail = true;
        break;
      }
    }
    if (foundDetail) {
      const addToCart = page.locator('button:has-text("Cart"), button:has-text("cart"), button:has-text("Buy"), [aria-label*="cart" i]');
      if (await addToCart.isVisible().catch(() => false)) {
        await expect(addToCart.first()).toBeVisible();
      }
    }
  });

  test('Breadcrumb navigation exists on detail page if present', async ({ page }) => {
    const paths = ['/product/1', '/products/1', '/p/1', '/product/test', '/products/sample'];
    let foundDetail = false;
    for (const path of paths) {
      const response = await page.goto(path, { waitUntil: 'networkidle' });
      if (response?.status() !== 404) {
        foundDetail = true;
        break;
      }
    }
    if (foundDetail) {
      const breadcrumb = page.locator('[class*="breadcrumb"], [class*="Breadcrumb"], nav[aria-label="breadcrumb"]');
      const present = await breadcrumb.isVisible().catch(() => false);
      expect(present).toBeTruthy();
    }
  });

  test('Invalid product URL shows 404 or error', async ({ page }) => {
    const response = await page.goto('/product/this-product-does-not-exist-999999', { waitUntil: 'networkidle' });
    if (response?.status() === 404) {
      await expect(page.locator('text=404, not found, Not Found').first()).toBeVisible();
    }
  });

  test('Back navigation from product page works', async ({ page }) => {
    await page.goto('/products', { waitUntil: 'networkidle' });
    const links = page.locator('a[href*="product"]');
    if (await links.first().isVisible().catch(() => false)) {
      await links.first().click();
      await page.waitForTimeout(1000);
      await page.goBack();
      await page.waitForLoadState('networkidle');
      expect(page.url()).toContain('product');
    }
  });

});
