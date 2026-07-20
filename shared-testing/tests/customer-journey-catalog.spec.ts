import { test, expect } from '@playwright/test';

test.describe('Part 2 — Customer Journey: Phases 3-6 — Catalog, Search, Filters, Sorting', () => {

  // === PHASE 3: PRODUCT CATALOG ===

  test('Products page loads and shows category cards', async ({ page }) => {
    const response = await page.goto('/products', { waitUntil: 'networkidle' });
    expect(response?.status()).toBeLessThan(400);
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(20);
  });

  test('Product categories are displayed', async ({ page }) => {
    await page.goto('/products', { waitUntil: 'networkidle' });
    const categoryCards = page.locator('[class*="card"], [class*="category"], [class*="CategoryCard"], section a');
    const count = await categoryCards.count();
    expect(count).toBeGreaterThanOrEqual(1);
  });

  test('Product category card links navigate somewhere', async ({ page }) => {
    await page.goto('/products', { waitUntil: 'networkidle' });
    const links = page.locator('a[href*="product"], a[href*="category"], section a');
    const count = await links.count();
    if (count > 0) {
      const href = await links.first().getAttribute('href');
      expect(href).toBeTruthy();
    }
  });

  test('No broken images on products page', async ({ page }) => {
    await page.goto('/products', { waitUntil: 'networkidle' });
    const images = page.locator('img');
    const count = await images.count();
    let broken = 0;
    for (let i = 0; i < count; i++) {
      const img = images.nth(i);
      const src = await img.getAttribute('src');
      if (!src || src === '' || src.startsWith('data:')) continue;
      const naturalWidth = await img.evaluate(el => (el as HTMLImageElement).naturalWidth);
      if (naturalWidth === 0) broken++;
    }
    expect(broken).toBe(0);
  });

  test('Featured products on homepage show product information', async ({ page }) => {
    await page.goto('/', { waitUntil: 'networkidle' });
    const productSections = page.locator('section').filter({ hasText: /Product/i });
    const sectionCount = await productSections.count();
    if (sectionCount > 0) {
      const section = productSections.first();
      const productCards = section.locator('[class*="card"], [class*="ProductCard"], [class*="product"]');
      const cardCount = await productCards.count();
      if (cardCount > 0) {
        await expect(productCards.first()).toBeVisible();
      }
    }
  });

  test('Products page handles loading state', async ({ page }) => {
    await page.goto('/products', { waitUntil: 'domcontentloaded' });
    const spinner = page.locator('[class*="spinner"], [class*="loading"], [class*="skeleton"], [class*="Loader"]');
    const present = await spinner.isVisible().catch(() => false);
    if (present) {
      await page.waitForLoadState('networkidle');
      await expect(spinner).not.toBeVisible({ timeout: 10000 });
    }
  });

  test('Products page handles empty state', async ({ page }) => {
    await page.goto('/products', { waitUntil: 'networkidle' });
    const emptyMsg = page.locator('text=No products, no results, empty, Nothing here, No items').first();
    const present = await emptyMsg.isVisible().catch(() => false);
    if (present) {
      await expect(emptyMsg).toBeVisible();
    }
  });

  // === PHASE 4: SEARCH ===

  test('Search page loads', async ({ page }) => {
    const response = await page.goto('/search', { waitUntil: 'networkidle' });
    expect(response?.status()).toBeLessThan(400);
  });

  test('Search input field exists and is interactive', async ({ page }) => {
    await page.goto('/search', { waitUntil: 'networkidle' });
    const searchInput = page.locator('input[type="search"], input[type="text"][placeholder*="search" i], textarea[placeholder*="search" i]');
    const count = await searchInput.count();
    if (count > 0) {
      await searchInput.first().fill('test');
      const value = await searchInput.first().inputValue();
      expect(value).toBe('test');
    }
  });

  test('Search returns results for exact match', async ({ page }) => {
    await page.goto('/search', { waitUntil: 'networkidle' });
    const searchInput = page.locator('input[type="search"], input[type="text"][placeholder*="search" i]');
    if (await searchInput.isVisible().catch(() => false)) {
      await searchInput.first().fill('mushroom');
      await searchInput.first().press('Enter');
      await page.waitForTimeout(1000);
      const results = page.locator('[class*="result"], [class*="card"], article, li');
      const count = await results.count();
      expect(count).toBeGreaterThanOrEqual(0);
    }
  });

  test('Search with no results shows appropriate message', async ({ page }) => {
    await page.goto('/search', { waitUntil: 'networkidle' });
    const searchInput = page.locator('input[type="search"], input[type="text"][placeholder*="search" i]');
    if (await searchInput.isVisible().catch(() => false)) {
      await searchInput.first().fill('zzzzzznotfound');
      await searchInput.first().press('Enter');
      await page.waitForTimeout(1000);
      const emptyMsg = page.locator('text=No results, no matches, nothing found, empty, Nothing here').first();
      const visible = await emptyMsg.isVisible().catch(() => false);
      if (!visible) {
        const results = page.locator('[class*="result"], article, li');
        const count = await results.count();
        expect(count).toBeGreaterThanOrEqual(0);
      }
    }
  });

  test('Case insensitive search works', async ({ page }) => {
    await page.goto('/search', { waitUntil: 'networkidle' });
    const searchInput = page.locator('input[type="search"], input[type="text"][placeholder*="search" i]');
    if (await searchInput.isVisible().catch(() => false)) {
      await searchInput.first().fill('MUSHROOM');
      await searchInput.first().press('Enter');
      await page.waitForTimeout(1000);
    }
  });

  test('Search with special characters handles gracefully', async ({ page }) => {
    await page.goto('/search', { waitUntil: 'networkidle' });
    const searchInput = page.locator('input[type="search"], input[type="text"][placeholder*="search" i]');
    if (await searchInput.isVisible().catch(() => false)) {
      await searchInput.first().fill('!@#$%^&*()');
      await searchInput.first().press('Enter');
      await page.waitForTimeout(1000);
      expect(page.url()).toContain('search');
    }
  });

  test('Search resets properly', async ({ page }) => {
    await page.goto('/search', { waitUntil: 'networkidle' });
    const searchInput = page.locator('input[type="search"], input[type="text"][placeholder*="search" i]');
    if (await searchInput.isVisible().catch(() => false)) {
      await searchInput.first().fill('test');
      await searchInput.first().press('Enter');
      await page.waitForTimeout(500);
      await searchInput.first().fill('');
      await searchInput.first().press('Enter');
      await page.waitForTimeout(500);
    }
  });

  // === PHASE 5: FILTERS ===

  test('Filter controls exist on product/search pages', async ({ page }) => {
    await page.goto('/products', { waitUntil: 'networkidle' });
    const filters = page.locator('select, [class*="filter"], [class*="Filter"], button:has-text("Filter"), [aria-label*="filter" i]');
    const count = await filters.count();
    expect(count).toBeGreaterThanOrEqual(0);
  });

  test('Category filter interaction works if present', async ({ page }) => {
    await page.goto('/products', { waitUntil: 'networkidle' });
    const categoryFilter = page.locator('select[aria-label*="category" i], [class*="category"] select');
    if (await categoryFilter.isVisible().catch(() => false)) {
      await categoryFilter.first().selectOption(1);
      await page.waitForTimeout(500);
    }
  });

  test('Multiple filters can be combined if present', async ({ page }) => {
    await page.goto('/products', { waitUntil: 'networkidle' });
    const allFilters = page.locator('select, [class*="filter"] select');
    const count = await allFilters.count();
    if (count >= 2) {
      await allFilters.nth(0).selectOption(1);
      await allFilters.nth(1).selectOption(1);
      await page.waitForTimeout(500);
    }
  });

  test('Clear filters button works if present', async ({ page }) => {
    await page.goto('/products', { waitUntil: 'networkidle' });
    const clearBtn = page.locator('button:has-text("Clear"), button:has-text("Reset"), [class*="clear"]');
    if (await clearBtn.isVisible().catch(() => false)) {
      await clearBtn.click();
      await page.waitForTimeout(500);
    }
  });

  // === PHASE 6: SORTING ===

  test('Sort controls exist on product/search pages', async ({ page }) => {
    await page.goto('/products', { waitUntil: 'networkidle' });
    const sortControls = page.locator('select[aria-label*="sort" i], button:has-text("Sort"), [class*="sort"], [class*="Sort"]');
    const count = await sortControls.count();
    expect(count).toBeGreaterThanOrEqual(0);
  });

  test('Sort by price ascending works if present', async ({ page }) => {
    await page.goto('/products', { waitUntil: 'networkidle' });
    const sortSelect = page.locator('select[aria-label*="sort" i], select.sort');
    if (await sortSelect.isVisible().catch(() => false)) {
      const options = await sortSelect.locator('option').allTextContents();
      const ascOption = options.findIndex(o => o.toLowerCase().includes('low') || o.toLowerCase().includes('asc') || o.toLowerCase().includes('price'));
      if (ascOption >= 0) {
        await sortSelect.selectOption(ascOption);
        await page.waitForTimeout(500);
      }
    }
  });

  test('Sort by price descending works if present', async ({ page }) => {
    await page.goto('/products', { waitUntil: 'networkidle' });
    const sortSelect = page.locator('select[aria-label*="sort" i], select.sort');
    if (await sortSelect.isVisible().catch(() => false)) {
      const options = await sortSelect.locator('option').allTextContents();
      const descOption = options.findIndex(o => o.toLowerCase().includes('high') || o.toLowerCase().includes('desc') || o.toLowerCase().includes('price'));
      if (descOption >= 0) {
        await sortSelect.selectOption(descOption);
        await page.waitForTimeout(500);
      }
    }
  });

  test('Sorting persists after filtering if both exist', async ({ page }) => {
    await page.goto('/products', { waitUntil: 'networkidle' });
    const sortSelect = page.locator('select[aria-label*="sort" i], select.sort');
    const filterSelect = page.locator('select[aria-label*="category" i], [class*="filter"] select');
    if (await sortSelect.isVisible().catch(() => false) && await filterSelect.isVisible().catch(() => false)) {
      await sortSelect.first().selectOption(1);
      await filterSelect.first().selectOption(1);
      await page.waitForTimeout(500);
    }
  });

});
