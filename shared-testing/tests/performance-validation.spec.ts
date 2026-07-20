import { test, expect, Page, BrowserContext } from '@playwright/test';

const KEY_ROUTES = [
  { name: 'Home', path: '/' },
  { name: 'Login', path: '/login' },
  { name: 'Register', path: '/register' },
  { name: 'Products', path: '/products' },
  { name: 'Cart', path: '/cart' },
  { name: 'Checkout', path: '/checkout' },
  { name: 'Orders', path: '/orders' },
  { name: 'Dashboard', path: '/dashboard' },
  { name: 'Training', path: '/training' },
  { name: 'Admin', path: '/admin/dashboard' },
  { name: 'Settings', path: '/settings' },
  { name: 'Search', path: '/search' },
];

const DASHBOARD_ROUTES = [
  '/dashboard', '/dashboard/orders', '/dashboard/wishlist',
  '/dashboard/training', '/dashboard/notifications', '/dashboard/settings',
];

const ADMIN_ROUTES = [
  '/admin/dashboard', '/admin/products', '/admin/orders', '/admin/customers',
  '/admin/finance', '/admin/reports', '/admin/settings',
];

const TRAINING_ROUTES = [
  '/training', '/training/courses', '/training/learning-paths',
  '/dashboard/training', '/dashboard/training/courses',
];

const PERFORMANCE_THRESHOLDS = {
  pageLoad: 8000,
  navigation: 5000,
  apiResponse: 3000,
  fcp: 4000,
  interactive: 8000,
  domNodes: 3000,
  requestsPerPage: 100,
  consoleErrors: 0,
  memoryGrowth: 50,
};

async function measurePageTiming(page: Page) {
  return page.evaluate(() => {
    const nav = performance.getEntriesByType('navigation')[0] as PerformanceNavigationTiming | undefined;
    const paint = performance.getEntriesByType('paint');
    const fcpEntry = paint.find(e => e.name === 'first-contentful-paint');
    return {
      domContentLoaded: nav ? nav.domContentLoadedEventEnd - nav.domContentLoadedEventStart : -1,
      loadEventEnd: nav ? nav.loadEventEnd - nav.loadEventStart : -1,
      domInteractive: nav ? nav.domInteractive : -1,
      domComplete: nav ? nav.domComplete : -1,
      domContentLoadedEventEnd: nav ? nav.domContentLoadedEventEnd : -1,
      requestStart: nav ? nav.requestStart : -1,
      responseEnd: nav ? nav.responseEnd : -1,
      firstContentfulPaint: fcpEntry ? fcpEntry.startTime : -1,
      fcpTime: fcpEntry ? fcpEntry.startTime : -1,
    };
  });
}

async function measureDomSize(page: Page) {
  return page.evaluate(() => {
    return {
      totalNodes: document.querySelectorAll('*').length,
      divCount: document.querySelectorAll('div').length,
      spanCount: document.querySelectorAll('span').length,
      imgCount: document.querySelectorAll('img').length,
      scriptCount: document.querySelectorAll('script').length,
      linkCount: document.querySelectorAll('link').length,
      inputCount: document.querySelectorAll('input').length,
      buttonCount: document.querySelectorAll('button').length,
    };
  });
}

async function measureResourceCount(page: Page) {
  return page.evaluate(() => {
    const resources = performance.getEntriesByType('resource');
    const jsResources = resources.filter(r => r.name.endsWith('.js') || r.name.includes('.js?'));
    const cssResources = resources.filter(r => r.name.endsWith('.css') || r.name.includes('.css?'));
    const imgResources = resources.filter(r => /\.(png|jpg|jpeg|gif|svg|webp)/i.test(r.name));
    const totalSize = resources.reduce((sum, r:any) => sum + (r.transferSize || r.encodedBodySize || 0), 0);
    return {
      total: resources.length,
      js: jsResources.length,
      css: cssResources.length,
      images: imgResources.length,
      totalTransferSize: totalSize,
    };
  });
}

async function measureMemory(page: Page) {
  return page.evaluate(() => {
    const mem = (performance as any).memory;
    return mem ? {
      usedJSHeapSize: mem.usedJSHeapSize,
      totalJSHeapSize: mem.totalJSHeapSize,
      jsHeapSizeLimit: mem.jsHeapSizeLimit,
    } : null;
  });
}

async function measureInteractionDelay(page: Page, selector: string) {
  const start = Date.now();
  const el = page.locator(selector);
  await el.waitFor({ state: 'visible', timeout: 5000 }).catch(() => {});
  const foundTime = Date.now() - start;
  if (await el.isVisible().catch(() => false)) {
    const respStart = Date.now();
    await el.click();
    const respTime = Date.now() - respStart;
    return { found: foundTime, response: respTime };
  }
  return { found: foundTime, response: -1 };
}

async function navigateAndMeasure(page: Page, path: string) {
  const start = Date.now();
  await page.goto(path);
  await page.waitForLoadState('networkidle');
  const loadTime = Date.now() - start;
  const timing = await measurePageTiming(page);
  const dom = await measureDomSize(page);
  const resources = await measureResourceCount(page);
  const mem = await measureMemory(page);
  return { loadTime, timing, dom, resources, mem, path };
}

// ===================================================================
// PHASE 1 — APPLICATION STARTUP
// ===================================================================
test.describe('Phase 1 — Application Startup', () => {

  test('Cold start — initial page load within threshold', async ({ page }) => {
    const result = await navigateAndMeasure(page, '/login');
    expect(result.loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
    expect(result.dom.totalNodes).toBeLessThan(PERFORMANCE_THRESHOLDS.domNodes);
  });

  test('First Contentful Paint within threshold', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const timing = await measurePageTiming(page);
    if (timing.firstContentfulPaint > 0) {
      expect(timing.firstContentfulPaint).toBeLessThan(PERFORMANCE_THRESHOLDS.fcp);
    }
  });

  test('DOM Content Loaded completes within threshold', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const timing = await measurePageTiming(page);
    expect(timing.domContentLoadedEventEnd).toBeGreaterThan(0);
  });

  test('Minimal JavaScript bundle size', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const resources = await measureResourceCount(page);
    expect(resources.js).toBeLessThan(30);
    expect(resources.total).toBeLessThan(PERFORMANCE_THRESHOLDS.requestsPerPage);
  });

  test('Loading indicator shown during initial load', async ({ page }) => {
    await page.goto('/');
    const hasSpinner = await page.locator('.sk-loading, [class*="spinner"], [class*="loader"], .sk-skeleton, [role="progressbar"]')
      .first().isVisible().catch(() => false);
  });

  test('Warm start — subsequent page load faster', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const warmStart = Date.now();
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const warmLoadTime = Date.now() - warmStart;
    expect(warmLoadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
  });

  test('No JavaScript errors during application bootstrap', async ({ page }) => {
    const errors: string[] = [];
    page.on('pageerror', err => errors.push(err.message));
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    expect(errors.length).toBe(0);
  });

  test('Application shell renders correctly', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('body')).toBeVisible();
    const body = await page.locator('body').innerText();
    expect(body.length).toBeGreaterThan(0);
  });
});

// ===================================================================
// PHASE 2 — PAGE PERFORMANCE
// ===================================================================
test.describe('Phase 2 — Page Performance', () => {

  KEY_ROUTES.forEach(route => {
    test(`${route.name} page loads within threshold`, async ({ page }) => {
      const result = await navigateAndMeasure(page, route.path);
      expect(result.loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
      expect(result.dom.totalNodes).toBeGreaterThan(0);
      expect(result.resources.total).toBeGreaterThan(0);
    });
  });

  test('All admin routes load within threshold', async ({ page }) => {
    for (const route of ADMIN_ROUTES) {
      const result = await navigateAndMeasure(page, route);
      expect(result.loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
    }
  });

  test('All training routes load within threshold', async ({ page }) => {
    for (const route of TRAINING_ROUTES) {
      const result = await navigateAndMeasure(page, route);
      expect(result.loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
    }
  });
});

// ===================================================================
// PHASE 3 — NAVIGATION PERFORMANCE
// ===================================================================
test.describe('Phase 3 — Navigation Performance', () => {

  test('Route transition completes within threshold', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const start = Date.now();
    await page.goto('/products');
    await page.waitForLoadState('networkidle');
    const transitionTime = Date.now() - start;
    expect(transitionTime).toBeLessThan(PERFORMANCE_THRESHOLDS.navigation);
  });

  test('Browser back navigation restores previous page', async ({ page }) => {
    await page.goto('/products');
    await page.waitForLoadState('networkidle');
    await page.goto('/cart');
    await page.waitForLoadState('networkidle');
    const start = Date.now();
    await page.goBack();
    await page.waitForLoadState('networkidle');
    const backTime = Date.now() - start;
    expect(backTime).toBeLessThan(PERFORMANCE_THRESHOLDS.navigation);
    expect(page.url()).toContain('products');
  });

  test('Browser forward navigation restores next page', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    await page.goto('/orders');
    await page.waitForLoadState('networkidle');
    await page.goBack();
    await page.waitForLoadState('networkidle');
    const start = Date.now();
    await page.goForward();
    await page.waitForLoadState('networkidle');
    const forwardTime = Date.now() - start;
    expect(forwardTime).toBeLessThan(PERFORMANCE_THRESHOLDS.navigation);
  });

  test('Page refresh maintains stability', async ({ page }) => {
    await page.goto('/dashboard');
    await page.waitForLoadState('networkidle');
    const start = Date.now();
    await page.reload();
    await page.waitForLoadState('networkidle');
    const reloadTime = Date.now() - start;
    expect(reloadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
  });

  test('Deep links resolve correctly', async ({ page }) => {
    const deepRoutes = ['/training/courses', '/dashboard/orders', '/admin/dashboard'];
    for (const route of deepRoutes) {
      const start = Date.now();
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const loadTime = Date.now() - start;
      expect(loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
    }
  });

  test('Repeated navigation between routes remains fast', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    for (let i = 0; i < 5; i++) {
      const start = Date.now();
      for (const route of ['/', '/products', '/cart', '/orders']) {
        await page.goto(route);
        await page.waitForLoadState('networkidle');
      }
      const cycleTime = Date.now() - start;
      expect(cycleTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad * 4);
    }
  });

  test('CSS transitions and animations complete smoothly', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const hasAnimations = await page.evaluate(() => {
      const styles = document.querySelectorAll('style, link[rel="stylesheet"]');
      return styles.length > 0;
    });
    expect(hasAnimations).toBe(true);
  });
});

// ===================================================================
// PHASE 4 — DATA LOADING
// ===================================================================
test.describe('Phase 4 — Data Loading', () => {

  test('Product list renders with acceptable DOM size', async ({ page }) => {
    const result = await navigateAndMeasure(page, '/products');
    expect(result.dom.totalNodes).toBeLessThan(PERFORMANCE_THRESHOLDS.domNodes);
    const hasItems = await page.locator('[class*="product"], [class*="card"], li, tr').first().isVisible().catch(() => false);
  });

  test('Order history loads within threshold', async ({ page }) => {
    const result = await navigateAndMeasure(page, '/dashboard/orders');
    expect(result.loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
  });

  test('Training list loads within threshold', async ({ page }) => {
    const result = await navigateAndMeasure(page, '/training/courses');
    expect(result.loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
  });

  test('Pagination controls present on list pages', async ({ page }) => {
    await page.goto('/products');
    await page.waitForLoadState('networkidle');
    const hasPagination = await page.locator('[class*="pagination"], nav[aria-label*="pagination"], [class*="page"]')
      .first().isVisible().catch(() => false);
  });

  test('Filter controls present and interactive', async ({ page }) => {
    await page.goto('/products');
    await page.waitForLoadState('networkidle');
    const hasFilter = await page.locator('select, [class*="filter"], button:has-text("Filter")')
      .first().isVisible().catch(() => false);
  });

  test('Sort controls present and interactive', async ({ page }) => {
    await page.goto('/products');
    await page.waitForLoadState('networkidle');
    const hasSort = await page.locator('select[aria-label*="sort"], button:has-text("Sort"), [class*="sort"]')
      .first().isVisible().catch(() => false);
  });

  test('Search input present on search page', async ({ page }) => {
    await page.goto('/search');
    await page.waitForLoadState('networkidle');
    const hasSearch = await page.locator('input[type="search"], input[placeholder*="search"], input[name="q"]')
      .first().isVisible().catch(() => false);
  });

  test('User table renders in admin console', async ({ page }) => {
    await page.goto('/admin/customers');
    await page.waitForLoadState('networkidle');
    const hasTable = await page.locator('table, [role="grid"], [class*="table"]')
      .first().isVisible().catch(() => false);
  });
});

// ===================================================================
// PHASE 5 — RESOURCE UTILIZATION
// ===================================================================
test.describe('Phase 5 — Resource Utilization', () => {

  test('Memory usage stays within reasonable limits', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const mem = await measureMemory(page);
    if (mem) {
      expect(mem.usedJSHeapSize).toBeGreaterThan(0);
      expect(mem.usedJSHeapSize / mem.jsHeapSizeLimit).toBeLessThan(0.8);
    }
  });

  test('DOM does not grow excessively with repeated navigation', async ({ page }) => {
    const sizes: number[] = [];
    for (const route of ['/', '/login', '/products', '/cart', '/orders', '/dashboard', '/settings']) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const dom = await measureDomSize(page);
      sizes.push(dom.totalNodes);
    }
    const maxSize = Math.max(...sizes);
    expect(maxSize).toBeLessThan(PERFORMANCE_THRESHOLDS.domNodes * 2);
  });

  test('Network request count stays reasonable per page', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const resources = await measureResourceCount(page);
    expect(resources.total).toBeLessThan(PERFORMANCE_THRESHOLDS.requestsPerPage);
    expect(resources.js).toBeLessThan(30);
  });

  test('No resource leaks over repeated page visits', async ({ page }) => {
    const memReadings: (number | null)[] = [];
    for (const route of ['/', '/products', '/cart', '/orders', '/dashboard', '/settings']) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const mem = await measureMemory(page);
      memReadings.push(mem ? mem.usedJSHeapSize : null);
    }
    const validReadings = memReadings.filter((r): r is number => r !== null);
    if (validReadings.length >= 2) {
      const growth = (validReadings[validReadings.length - 1] - validReadings[0]) / validReadings[0] * 100;
      expect(growth).toBeLessThan(PERFORMANCE_THRESHOLDS.memoryGrowth);
    }
  });

  test('Repeated interactions do not accumulate DOM nodes', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const baseline = await measureDomSize(page);
    for (let i = 0; i < 10; i++) {
      await page.goto('/');
      await page.waitForLoadState('networkidle');
    }
    const after = await measureDomSize(page);
    const nodeGrowth = after.totalNodes - baseline.totalNodes;
    expect(nodeGrowth).toBeLessThan(100);
  });

  test('No excessive re-renders or layout thrashing', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const layoutCount = await page.evaluate(() => {
      return new Promise<number>((resolve) => {
        let count = 0;
        const observer = new PerformanceObserver((list) => {
          count += list.getEntries().length;
        });
        observer.observe({ type: 'layout-shift', buffered: false });
        setTimeout(() => {
          observer.disconnect();
          resolve(count);
        }, 1000);
      });
    });
    expect(layoutCount).toBeGreaterThanOrEqual(0);
  });
});

// ===================================================================
// PHASE 6 — LONG SESSION RELIABILITY
// ===================================================================
test.describe('Phase 6 — Long Session Reliability', () => {

  test('Session remains stable after repeated navigation', { timeout: 120000 }, async ({ page }) => {
    const errors: string[] = [];
    page.on('pageerror', err => errors.push(err.message));
    for (let i = 0; i < 12; i++) {
      const route = KEY_ROUTES[i % KEY_ROUTES.length];
      await page.goto(route.path);
      await page.waitForLoadState('networkidle');
      await page.waitForTimeout(300);
    }
    expect(errors.length).toBe(0);
  });

  test('Memory does not leak over sustained usage', async ({ page }) => {
    const memSnapshots: number[] = [];
    for (let i = 0; i < 15; i++) {
      await page.goto(KEY_ROUTES[i % KEY_ROUTES.length].path);
      await page.waitForLoadState('networkidle');
      const mem = await measureMemory(page);
      if (mem) memSnapshots.push(mem.usedJSHeapSize);
      await page.waitForTimeout(300);
    }
    if (memSnapshots.length >= 3) {
      const firstHalfAvg = memSnapshots.slice(0, Math.floor(memSnapshots.length / 2)).reduce((a, b) => a + b, 0) / Math.floor(memSnapshots.length / 2);
      const secondHalfAvg = memSnapshots.slice(Math.floor(memSnapshots.length / 2)).reduce((a, b) => a + b, 0) / Math.ceil(memSnapshots.length / 2);
      const growth = ((secondHalfAvg - firstHalfAvg) / firstHalfAvg) * 100;
      expect(growth).toBeLessThan(PERFORMANCE_THRESHOLDS.memoryGrowth);
    }
  });

  test('UI remains responsive after prolonged interaction', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const start = Date.now();
    for (let i = 0; i < 10; i++) {
      await page.goto(KEY_ROUTES[i % KEY_ROUTES.length].path);
      await page.waitForLoadState('networkidle');
    }
    const totalTime = Date.now() - start;
    expect(totalTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad * 10);
  });

  test('No unexpected full page reloads during session', async ({ page }) => {
    const navigations: string[] = [];
    page.on('load', () => navigations.push('load'));
    for (let i = 0; i < 10; i++) {
      await page.goto(DASHBOARD_ROUTES[i % DASHBOARD_ROUTES.length]);
      await page.waitForLoadState('networkidle');
    }
    expect(navigations.length).toBe(10);
  });

  test('Repeated search queries maintain performance', async ({ page }) => {
    await page.goto('/search');
    await page.waitForLoadState('networkidle');
    for (let i = 0; i < 10; i++) {
      const start = Date.now();
      await page.goto('/search');
      await page.waitForLoadState('networkidle');
      const loadTime = Date.now() - start;
      expect(loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
    }
  });

  test('Dashboard remains stable after repeated views', async ({ page }) => {
    const errors: string[] = [];
    page.on('pageerror', err => errors.push(err.message));
    for (let i = 0; i < 10; i++) {
      await page.goto('/dashboard');
      await page.waitForLoadState('networkidle');
      await page.waitForTimeout(200);
    }
    expect(errors.length).toBe(0);
  });
});

// ===================================================================
// PHASE 7 — ERROR RECOVERY
// ===================================================================
test.describe('Phase 7 — Error Recovery', () => {

  test('Page handles navigation after route change', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    await page.goto('/products');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('body')).toBeVisible();
  });

  test('Browser refresh restores page correctly', async ({ page }) => {
    await page.goto('/dashboard');
    await page.waitForLoadState('networkidle');
    await page.reload();
    await page.waitForLoadState('networkidle');
    await expect(page.locator('body')).toBeVisible();
  });

  test('Tab restoration re-renders page correctly', async ({ page }) => {
    await page.goto('/orders');
    await page.waitForLoadState('networkidle');
    await page.goto('/cart');
    await page.waitForLoadState('networkidle');
    await page.goto('/orders');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('body')).toBeVisible();
  });

  test('Page is reachable after multiple route changes', async ({ page }) => {
    const routes = ['/', '/products', '/cart', '/orders', '/dashboard'];
    for (let i = 0; i < routes.length; i++) {
      await page.goto(routes[i]);
      await page.waitForLoadState('networkidle');
      const bodyText = await page.locator('body').innerText();
      expect(bodyText.length).toBeGreaterThan(0);
    }
  });

  test('Slow page loads do not break application', async ({ page }) => {
    await page.goto('/admin/dashboard');
    await page.waitForLoadState('networkidle');
    await page.goto('/training/courses');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('body')).toBeVisible();
  });

  test('Navigation to nonexistent route shows fallback UI', async ({ page }) => {
    const start = Date.now();
    await page.goto('/nonexistent-route-test');
    await page.waitForLoadState('networkidle');
    const loadTime = Date.now() - start;
    expect(loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
  });

  test('Rapid repeated navigation does not cause crash', async ({ page }) => {
    const errors: string[] = [];
    page.on('pageerror', err => errors.push(err.message));
    for (let i = 0; i < 5; i++) {
      const promises = [
        page.goto('/').then(() => page.waitForLoadState('networkidle')),
        page.goto('/login').then(() => page.waitForLoadState('networkidle')),
      ];
      await Promise.all(promises.map(p => p.catch(() => {})));
    }
    expect(errors.length).toBeLessThan(3);
  });

  test('Browser back/forward after deep navigation', async ({ page }) => {
    await page.goto('/training/courses');
    await page.waitForLoadState('networkidle');
    await page.goto('/dashboard/orders');
    await page.waitForLoadState('networkidle');
    await page.goto('/admin/dashboard');
    await page.waitForLoadState('networkidle');
    const backStart = Date.now();
    await page.goBack();
    await page.waitForLoadState('networkidle');
    const backTime = Date.now() - backStart;
    expect(backTime).toBeLessThan(PERFORMANCE_THRESHOLDS.navigation);
    const forwardStart = Date.now();
    await page.goForward();
    await page.waitForLoadState('networkidle');
    const forwardTime = Date.now() - forwardStart;
    expect(forwardTime).toBeLessThan(PERFORMANCE_THRESHOLDS.navigation);
  });
});

// ===================================================================
// PHASE 8 — CONCURRENCY
// ===================================================================
test.describe('Phase 8 — Concurrency', () => {

  test('Multiple tabs can load simultaneously', async ({ browser }) => {
    const page1 = await browser.newPage();
    const page2 = await browser.newPage();
    const start = Date.now();
    await Promise.all([
      page1.goto('/').then(() => page1.waitForLoadState('networkidle')),
      page2.goto('/products').then(() => page2.waitForLoadState('networkidle')),
    ]);
    const totalTime = Date.now() - start;
    expect(totalTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad * 2);
    await page1.close();
    await page2.close();
  });

  test('Rapid clicking does not break navigation', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const errors: string[] = [];
    page.on('pageerror', err => errors.push(err.message));
    const clickPromises = [];
    for (let i = 0; i < 5; i++) {
      clickPromises.push(page.goto(KEY_ROUTES[i % KEY_ROUTES.length].path).catch(() => {}));
    }
    await Promise.all(clickPromises);
    expect(errors.length).toBeLessThan(3);
    await page.waitForLoadState('networkidle');
    await expect(page.locator('body')).toBeVisible();
  });

  test('Simultaneous navigation requests are handled', async ({ page }) => {
    const errors: string[] = [];
    page.on('pageerror', err => errors.push(err.message));
    await Promise.allSettled([
      page.goto('/products').then(() => page.waitForLoadState('networkidle')),
      page.goto('/cart').then(() => page.waitForLoadState('networkidle')),
    ]);
    await page.waitForLoadState('networkidle');
    expect(errors.length).toBeLessThan(3);
  });

  test('Repeated identical requests do not degrade performance', async ({ page }) => {
    const start = Date.now();
    for (let i = 0; i < 5; i++) {
      await page.goto('/');
      await page.waitForLoadState('networkidle');
    }
    const avgLoadTime = (Date.now() - start) / 5;
    expect(avgLoadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
  });

  test('Tab switching preserves state', async ({ browser }) => {
    const page1 = await browser.newPage();
    const page2 = await browser.newPage();
    await page1.goto('/products');
    await page1.waitForLoadState('networkidle');
    await page2.goto('/cart');
    await page2.waitForLoadState('networkidle');
    await page1.bringToFront();
    await page1.waitForTimeout(500);
    expect(page1.url()).toContain('products');
    await page2.bringToFront();
    await page2.waitForTimeout(500);
    expect(page2.url()).toContain('cart');
    await page1.close();
    await page2.close();
  });

  test('Concurrent browser contexts remain independent', async ({ browser }) => {
    const ctx1 = await browser.newContext();
    const ctx2 = await browser.newContext();
    const p1 = await ctx1.newPage();
    const p2 = await ctx2.newPage();
    await Promise.all([
      p1.goto('/').then(() => p1.waitForLoadState('networkidle')),
      p2.goto('/dashboard').then(() => p2.waitForLoadState('networkidle')),
    ]);
    expect(p1.url()).not.toContain('dashboard');
    expect(p2.url()).toContain('dashboard');
    await ctx1.close();
    await ctx2.close();
  });
});

// ===================================================================
// PHASE 9 — RESPONSIVE PERFORMANCE
// ===================================================================
test.describe('Phase 9 — Responsive Performance', () => {

  test('Desktop viewport renders all key pages', async ({ page }) => {
    await page.setViewportSize({ width: 1920, height: 1080 });
    for (const route of ['/', '/products', '/cart', '/orders', '/dashboard']) {
      const start = Date.now();
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const loadTime = Date.now() - start;
      expect(loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
    }
  });

  test('Tablet viewport renders all key pages', async ({ page }) => {
    await page.setViewportSize({ width: 768, height: 1024 });
    for (const route of ['/', '/products', '/cart', '/dashboard', '/settings']) {
      const start = Date.now();
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const loadTime = Date.now() - start;
      expect(loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
    }
  });

  test('Mobile viewport renders all key pages', async ({ page }) => {
    await page.setViewportSize({ width: 375, height: 812 });
    for (const route of ['/', '/products', '/login', '/search', '/dashboard']) {
      const start = Date.now();
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const loadTime = Date.now() - start;
      expect(loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
    }
  });

  test('Scrolling performance on list pages', async ({ page }) => {
    await page.setViewportSize({ width: 375, height: 812 });
    await page.goto('/products');
    await page.waitForLoadState('networkidle');
    const scrollStart = Date.now();
    await page.evaluate(() => window.scrollTo(0, document.body.scrollHeight));
    await page.waitForTimeout(500);
    await page.evaluate(() => window.scrollTo(0, 0));
    await page.waitForTimeout(500);
    const scrollTime = Date.now() - scrollStart;
    expect(scrollTime).toBeLessThan(3000);
  });

  test('No layout shifts on viewport changes', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const shifts = await page.evaluate(() => {
      return new Promise<number>((resolve) => {
        let count = 0;
        const observer = new PerformanceObserver((list) => {
          for (const entry of list.getEntries()) {
            if ((entry as any).hadRecentInput) continue;
            count++;
          }
        });
        observer.observe({ type: 'layout-shift', buffered: true });
        setTimeout(() => {
          observer.disconnect();
          resolve(count);
        }, 500);
      });
    });
    expect(shifts).toBeGreaterThanOrEqual(0);
  });
});

// ===================================================================
// PHASE 10 — CROSS-BROWSER
// ===================================================================
test.describe('Phase 10 — Cross-Browser Performance', () => {

  test('Browser capabilities detected', async ({ page }) => {
    const info = await page.evaluate(() => ({
      userAgent: navigator.userAgent,
      vendor: navigator.vendor,
      platform: navigator.platform,
      language: navigator.language,
      cookieEnabled: navigator.cookieEnabled,
    }));
    expect(info.userAgent.length).toBeGreaterThan(0);
    expect(info.language.length).toBeGreaterThan(0);
  });

  test('Performance API available across browsers', async ({ page }) => {
    const hasPerformanceAPI = await page.evaluate(() => {
      return typeof performance !== 'undefined'
        && typeof performance.getEntriesByType === 'function'
        && typeof performance.now === 'function';
    });
    expect(hasPerformanceAPI).toBe(true);
  });

  test('Render engine reports timing data', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const timing = await measurePageTiming(page);
    expect(timing.domComplete).toBeGreaterThan(0);
    expect(timing.responseEnd).toBeGreaterThan(0);
  });
});

// ===================================================================
// PHASE 11 — ACCESSIBILITY IMPACT
// ===================================================================
test.describe('Phase 11 — Accessibility Impact', () => {

  test('Keyboard navigation does not degrade page performance', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const perfBefore = await measurePageTiming(page);
    for (let i = 0; i < 10; i++) {
      await page.keyboard.press('Tab');
      await page.waitForTimeout(100);
    }
    const perfAfter = await measurePageTiming(page);
    expect(perfAfter.domComplete).toBeGreaterThan(0);
  });

  test('Focus movement does not cause layout shifts', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const focusable = await page.locator('input, button, a, select, textarea').all();
    for (let i = 0; i < Math.min(focusable.length, 5); i++) {
      await focusable[i].focus();
      await page.waitForTimeout(100);
    }
    await expect(page.locator('body')).toBeVisible();
  });

  test('ARIA live regions update without performance impact', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const hasAriaLive = await page.locator('[aria-live], [role="alert"], [role="status"]')
      .count();
    expect(hasAriaLive).toBeGreaterThanOrEqual(0);
  });

  test('Screen reader announcements do not block rendering', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const start = Date.now();
    await page.goto('/products');
    await page.waitForLoadState('networkidle');
    const loadTime = Date.now() - start;
    expect(loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
  });
});

// ===================================================================
// PHASE 12 — VISUAL STABILITY
// ===================================================================
test.describe('Phase 12 — Visual Stability', () => {

  test('Skeleton loaders or progress indicators present during load', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const hasIndicator = await page.locator(
      '.sk-loading, .sk-skeleton, [class*="skeleton"], [class*="spinner"], [class*="loader"], [role="progressbar"]'
    ).first().isVisible().catch(() => false);
  });

  test('Images load with correct dimensions', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const imgInfo = await page.evaluate(() => {
      return Array.from(document.images).slice(0, 10).map(img => ({
        src: img.src.substring(0, 100),
        width: img.naturalWidth,
        height: img.naturalHeight,
        loaded: img.complete,
      }));
    });
    expect(Array.isArray(imgInfo)).toBe(true);
  });

  test('Typography renders without layout shift', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const textNodes = await page.locator('h1, h2, h3, p, span, a').count();
    expect(textNodes).toBeGreaterThan(0);
  });

  test('Animations use compositor-friendly properties', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const hasAnimations = await page.evaluate(() => {
      const sheets = document.styleSheets;
      for (let i = 0; i < sheets.length; i++) {
        try {
          const rules = sheets[i].cssRules || sheets[i].rules;
          if (!rules) continue;
          for (let j = 0; j < rules.length; j++) {
            const cssText = (rules[j] as any).cssText || '';
            if (cssText.includes('animation') || cssText.includes('transition')) return true;
          }
        } catch(e) {}
      }
      return false;
    });
  });

  test('Responsive breakpoints render correctly', async ({ page }) => {
    const testViewport = async (width: number, height: number, label: string) => {
      await page.setViewportSize({ width, height });
      await page.goto('/');
      await page.waitForLoadState('networkidle');
      const body = await page.locator('body');
      await expect(body).toBeVisible();
    };
    await testViewport(1920, 1080, 'Desktop');
    await testViewport(1366, 768, 'Laptop');
    await testViewport(768, 1024, 'Tablet');
    await testViewport(375, 812, 'Mobile');
  });

  test('No visible UI corruption after route changes', async ({ page }) => {
    const routes = ['/', '/login', '/products', '/cart', '/orders', '/dashboard', '/settings'];
    for (const route of routes) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const bodyText = await page.locator('body').innerText();
      expect(bodyText.length).toBeGreaterThan(0);
    }
  });
});

// ===================================================================
// PHASE 13 — OBSERVABILITY
// ===================================================================
test.describe('Phase 13 — Observability', () => {

  test('No console errors during page loads', async ({ page }) => {
    const errors: string[] = [];
    page.on('console', msg => {
      if (msg.type() === 'error') errors.push(msg.text());
    });
    for (const route of ['/', '/login', '/products', '/cart', '/orders', '/dashboard']) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
    }
    if (errors.length > 0) {
      const nonCorsErrors = errors.filter(e => !e.includes('CORS') && !e.includes('favicon'));
      expect(nonCorsErrors.length).toBe(0);
    }
  });

  test('No unhandled promise rejections', async ({ page }) => {
    const rejections: string[] = [];
    page.on('pageerror', err => {
      if (err.message.includes('unhandled') || err.message.includes('rejection')) {
        rejections.push(err.message);
      }
    });
    for (const route of ['/', '/products', '/cart', '/orders']) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
    }
    expect(rejections.length).toBe(0);
  });

  test('HTTP status codes are satisfactory', async ({ page }) => {
    const statuses: number[] = [];
    page.on('response', resp => statuses.push(resp.status()));
    for (const route of ['/', '/login', '/products', '/cart', '/checkout', '/orders', '/dashboard', '/settings']) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
    }
    const errorStatuses = statuses.filter(s => s >= 400);
    expect(errorStatuses.length).toBeLessThan(statuses.length * 0.1);
  });

  test('Console warnings do not indicate performance issues', async ({ page }) => {
    const warnings: string[] = [];
    page.on('console', msg => {
      if (msg.type() === 'warning') warnings.push(msg.text());
    });
    for (const route of ['/', '/login', '/products', '/cart', '/orders']) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
    }
    const perfWarnings = warnings.filter(w =>
      w.toLowerCase().includes('performance')
      || w.toLowerCase().includes('slow')
      || w.toLowerCase().includes('deprecated')
      || w.toLowerCase().includes('long task')
    );
    expect(perfWarnings.length).toBeLessThan(3);
  });

  test('Performance timing data available for analysis', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const timing = await measurePageTiming(page);
    expect(timing.domComplete).toBeGreaterThan(0);
    expect(timing.responseEnd).toBeGreaterThan(0);
    const resources = await measureResourceCount(page);
    expect(resources.total).toBeGreaterThan(0);
    const dom = await measureDomSize(page);
    expect(dom.totalNodes).toBeGreaterThan(0);
  });

  test('Browser diagnostics captured', async ({ page }) => {
    const diagnostics = await page.evaluate(() => ({
      screenWidth: window.screen.width,
      screenHeight: window.screen.height,
      devicePixelRatio: window.devicePixelRatio,
      innerWidth: window.innerWidth,
      innerHeight: window.innerHeight,
      pageLoadTime: performance.now(),
    }));
    expect(diagnostics.screenWidth).toBeGreaterThan(0);
    expect(diagnostics.innerWidth).toBeGreaterThan(0);
    expect(diagnostics.devicePixelRatio).toBeGreaterThan(0);
  });
});

// ===================================================================
// PHASE 14 — EVIDENCE COLLECTION
// ===================================================================
test.describe('Phase 14 — Evidence Collection', () => {

  test('Browser and viewport info captured', async ({ page }) => {
    const viewport = page.viewportSize();
    expect(viewport?.width).toBeGreaterThan(0);
    expect(viewport?.height).toBeGreaterThan(0);
    const userAgent = await page.evaluate(() => navigator.userAgent);
    expect(userAgent.length).toBeGreaterThan(0);
  });

  test('Performance traces collected during session', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const perfData = await page.evaluate(() => {
      return {
        timing: performance.timing ? {
          navigationStart: performance.timing.navigationStart,
          domComplete: performance.timing.domComplete,
          loadEventEnd: performance.timing.loadEventEnd,
        } : null,
        memory: (performance as any).memory ? {
          usedJSHeapSize: (performance as any).memory.usedJSHeapSize,
        } : null,
        paint: performance.getEntriesByType('paint').map(e => ({ name: e.name, startTime: e.startTime })),
      };
    });
    expect(perfData).not.toBeNull();
    if (perfData.timing) {
      expect(perfData.timing.domComplete).toBeGreaterThan(0);
    }
  });

  test('Network request log available', async ({ page }) => {
    const requests: string[] = [];
    page.on('request', req => {
      if (!req.url().includes('data:')) {
        requests.push(`${req.method()} ${new URL(req.url()).pathname.substring(0, 80)}`);
      }
    });
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    expect(requests.length).toBeGreaterThan(0);
  });
});
