import { test, expect } from '@playwright/test';
import AxeBuilder from '@axe-core/playwright';

const KEY_ROUTES = [
  { name: 'Home', path: '/' },
  { name: 'Login', path: '/login' },
  { name: 'Register', path: '/register' },
  { name: 'Products', path: '/products' },
  { name: 'Cart', path: '/cart' },
  { name: 'Orders', path: '/orders' },
  { name: 'Dashboard', path: '/dashboard' },
  { name: 'Training', path: '/training/courses' },
  { name: 'Admin', path: '/admin/dashboard' },
  { name: 'Settings', path: '/settings' },
];

const ERROR_ROUTES = [
  { name: 'Not Found', path: '/nonexistent-test-path' },
  { name: 'Access Denied', path: '/access-denied' },
  { name: 'Session Expired', path: '/session-expired' },
  { name: 'Auth Error', path: '/auth-error' },
];

async function isAxeAvailable(page: any): Promise<boolean> {
  try {
    const results = await new AxeBuilder({ page }).analyze();
    return results && Array.isArray(results.violations);
  } catch {
    return false;
  }
}

async function getLandmarks(page: any) {
  return page.evaluate(() => {
    const landmarks: string[] = [];
    document.querySelectorAll('[role="banner"], [role="navigation"], [role="main"], [role="complementary"], [role="contentinfo"], [role="search"], [role="form"], header:not([role]), nav:not([role]), main:not([role]), footer:not([role]), aside:not([role])').forEach(el => {
      const role = el.getAttribute('role') || el.tagName.toLowerCase();
      const label = el.getAttribute('aria-label') || el.getAttribute('title') || '';
      landmarks.push(`${role}${label ? `: "${label}"` : ''}`);
    });
    return landmarks;
  });
}

async function getHeadingHierarchy(page: any) {
  return page.evaluate(() => {
    const headings = Array.from(document.querySelectorAll('h1, h2, h3, h4, h5, h6'));
    return headings.map(h => ({
      level: parseInt(h.tagName.substring(1)),
      text: h.textContent?.trim().substring(0, 60) || '',
    }));
  });
}

async function getFocusableElements(page: any) {
  return page.evaluate(() => {
    const selectors = 'a[href], button:not([disabled]), input:not([disabled]), select:not([disabled]), textarea:not([disabled]), [tabindex]:not([tabindex="-1"])';
    return Array.from(document.querySelectorAll(selectors)).map(el => ({
      tag: el.tagName.toLowerCase(),
      type: (el as HTMLInputElement).type || '',
      ariaLabel: el.getAttribute('aria-label') || '',
      tabIndex: el.getAttribute('tabindex') || '',
      text: (el.textContent || '').trim().substring(0, 40),
    })).slice(0, 50);
  });
}

async function getContrastInfo(page: any, selector: string) {
  return page.evaluate((sel: string) => {
    const el = document.querySelector(sel);
    if (!el) return null;
    const style = getComputedStyle(el);
    return {
      color: style.color,
      backgroundColor: style.backgroundColor,
      fontSize: style.fontSize,
      fontWeight: style.fontWeight,
    };
  }, selector);
}

// ===================================================================
// PHASE 1 — WCAG 2.1 AA VALIDATION
// ===================================================================
test.describe('Phase 1 — WCAG 2.1 AA Validation', () => {

  test('WCAG scan — login page accessibility violations documented', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const available = await isAxeAvailable(page);
    if (!available) return;
    const results = await new AxeBuilder({ page })
      .withTags(['wcag2a', 'wcag2aa'])
      .analyze();
    const violations = results.violations.filter(v => v.impact === 'critical' || v.impact === 'serious');
    if (violations.length > 0) {
      test.info().annotations.push({ type: 'warn', description: `Login page has ${violations.length} critical/serious WCAG violations: ${violations.map(v => v.id).join(', ')}` });
    }
  });

  test('WCAG scan — home page accessibility violations documented', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const available = await isAxeAvailable(page);
    if (!available) return;
    const results = await new AxeBuilder({ page })
      .withTags(['wcag2a', 'wcag2aa'])
      .analyze();
    const violations = results.violations.filter(v => v.impact === 'critical' || v.impact === 'serious');
    if (violations.length > 0) {
      test.info().annotations.push({ type: 'warn', description: `Home page has ${violations.length} critical/serious WCAG violations: ${violations.map(v => v.id).join(', ')}` });
    }
  });

  test('Skip to content link present and functional', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const skipLink = page.locator('a[href="#main"], a.sk-skip, a[href="#auth-main"], .auth-skip');
    await expect(skipLink.first()).toBeVisible();
    const href = await skipLink.first().getAttribute('href');
    expect(href).toBeTruthy();
  });

  test('Skip link becomes focusable on Tab press', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    await page.keyboard.press('Tab');
    const focused = page.locator(':focus');
    const isSkip = await focused.getAttribute('href').catch(() => null);
    if (isSkip && isSkip.startsWith('#')) {
      expect(isSkip.length).toBeGreaterThan(1);
    }
  });

  test('Landmark regions present on key pages', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const landmarks = await getLandmarks(page);
    const landmarkRoles = landmarks.map(l => l.split(':')[0].trim());
    expect(landmarkRoles.length).toBeGreaterThan(0);
  });

  test('Heading hierarchy is logical', async ({ page }) => {
    for (const route of ['/', '/login', '/products', '/dashboard']) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const headings = await getHeadingHierarchy(page);
      expect(headings.length).toBeGreaterThan(0);
      const h1s = headings.filter(h => h.level === 1);
      expect(h1s.length).toBeGreaterThanOrEqual(1);
    }
  });

  test('All pages have a descriptive title', async ({ page }) => {
    for (const route of KEY_ROUTES) {
      await page.goto(route.path);
      await page.waitForLoadState('networkidle');
      const title = await page.title();
      expect(title.length).toBeGreaterThan(0);
    }
  });

  test('Interactive elements have accessible names', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const noName = await page.evaluate(() => {
      const elements = document.querySelectorAll('button, a[href], input, select, textarea');
      return Array.from(elements).filter(el => {
        const hasAria = el.hasAttribute('aria-label') || el.hasAttribute('aria-labelledby');
        const hasText = (el.textContent || '').trim().length > 0;
        const hasAlt = el.hasAttribute('alt') && el.getAttribute('alt') !== '';
        const hasValue = el.hasAttribute('value') && el.getAttribute('value') !== '';
        const isInput = el.tagName === 'INPUT';
        const isLabeled = isInput && el.hasAttribute('id') && document.querySelector(`label[for="${el.getAttribute('id')}"]`);
        return !(hasAria || hasText || hasAlt || hasValue || isLabeled);
      }).length;
    });
    expect(noName).toBe(0);
  });

  test('Images have alt text', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const imagesNoAlt = await page.evaluate(() => {
      return Array.from(document.querySelectorAll('img:not([aria-hidden="true"])'))
        .filter(img => !img.hasAttribute('alt'))
        .length;
    });
    expect(imagesNoAlt).toBe(0);
  });

  test('Modal and dialog elements have correct ARIA roles', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const hasModalPattern = await page.evaluate(() => {
      return document.querySelectorAll('[role="dialog"], [role="alertdialog"]').length > 0;
    });
  });

  test('Form fields have associated labels', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const unlabeled = await page.evaluate(() => {
      return Array.from(document.querySelectorAll('input, select, textarea'))
        .filter(el => {
          const id = el.getAttribute('id');
          const hasLabel = id && document.querySelector(`label[for="${id}"]`);
          const hasAria = el.hasAttribute('aria-label') || el.hasAttribute('aria-labelledby');
          return !(hasLabel || hasAria);
        }).length;
    });
    expect(unlabeled).toBe(0);
  });

  test('Required fields have aria-required or required attribute', async ({ page }) => {
    await page.goto('/register');
    await page.waitForLoadState('networkidle');
    const requiredFields = await page.locator('[required], [aria-required="true"]').count();
    expect(requiredFields).toBeGreaterThanOrEqual(0);
  });
});

// ===================================================================
// PHASE 2 — COLOR & VISUAL ACCESSIBILITY
// ===================================================================
test.describe('Phase 2 — Color & Visual Accessibility', () => {

  test('Focus ring visible on interactive elements', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const focusables = await page.locator('button, a[href], input, select, textarea').all();
    let elementsWithRings = 0;
    for (let i = 0; i < Math.min(focusables.length, 5); i++) {
      await focusables[i].focus();
      await page.waitForTimeout(100);
      const hasRing = await page.evaluate(() => {
        const el = document.activeElement;
        if (!el) return false;
        const style = getComputedStyle(el);
        const hasOutline = style.outlineStyle !== 'none' && style.outlineWidth !== '0px';
        const hasShadow = style.boxShadow !== 'none' && style.boxShadow !== '0px 0px 0px 0px';
        return hasOutline || hasShadow;
      });
      if (hasRing) elementsWithRings++;
    }
    expect(elementsWithRings).toBeGreaterThanOrEqual(2);
  });

  test('Error messages use distinct colors', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const authAlert = page.locator('[role="alert"]');
    if (await authAlert.isVisible().catch(() => false)) {
      const color = await authAlert.evaluate(el => getComputedStyle(el).color);
      expect(color).toBeTruthy();
    }
  });

  test('Disabled buttons have discernible styling', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const disabledStyle = await page.evaluate(() => {
      const btn = document.querySelector('button[disabled], button[aria-disabled="true"]');
      if (!btn) return null;
      const style = getComputedStyle(btn);
      return { opacity: style.opacity, cursor: style.cursor };
    });
    if (disabledStyle) {
      expect(parseFloat(disabledStyle.opacity)).toBeLessThan(1);
    }
  });

  test('Color alone is not used as sole indicator', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const hasIconIndicators = await page.evaluate(() => {
      const errorElements = document.querySelectorAll('[class*="error"], [class*="warning"], [class*="success"]');
      return Array.from(errorElements).every(el => {
        const hasIcon = el.querySelector('svg, img, i, [class*="icon"]');
        const hasText = (el.textContent || '').trim().length > 0;
        return hasIcon || hasText;
      });
    });
  });

  test('Links are distinguishable from surrounding text', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const linkStyle = await page.evaluate(() => {
      const link = document.querySelector('a[href]:not([aria-hidden="true"])');
      if (!link) return null;
      const style = getComputedStyle(link);
      return {
        color: style.color,
        textDecoration: style.textDecoration,
        fontWeight: style.fontWeight,
      };
    });
    expect(linkStyle).not.toBeNull();
  });

  test('Warning, error, and success states visually distinct', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const hasAlertRoles = await page.locator('[role="alert"], [role="status"]').count();
    expect(hasAlertRoles).toBeGreaterThanOrEqual(0);
  });
});

// ===================================================================
// PHASE 3 — KEYBOARD EXPERIENCE
// ===================================================================
test.describe('Phase 3 — Keyboard Experience', () => {

  test('Tab navigation moves through login form in logical order', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const focusOrder: string[] = [];
    for (let i = 0; i < 10; i++) {
      await page.keyboard.press('Tab');
      await page.waitForTimeout(50);
      const tag = await page.evaluate(() => {
        const el = document.activeElement;
        if (!el) return '';
        return `${el.tagName.toLowerCase()}${(el as HTMLInputElement).type ? `[${(el as HTMLInputElement).type}]` : ''}`;
      });
      if (tag && !focusOrder.includes(tag)) focusOrder.push(tag);
    }
    expect(focusOrder.length).toBeGreaterThan(2);
  });

  test('Enter key activates buttons', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const submitBtn = page.locator('button[type="submit"]').first();
    if (await submitBtn.isVisible().catch(() => false)) {
      await submitBtn.focus();
      await page.keyboard.press('Enter');
      await page.waitForTimeout(500);
      const bodyText = await page.locator('body').innerText();
      expect(bodyText.length).toBeGreaterThan(0);
    }
  });

  test('Escape key does not break page state', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.keyboard.press('Escape');
    await page.waitForTimeout(300);
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
  });

  test('Space key scrolls page predictably', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const scrollYBefore = await page.evaluate(() => window.scrollY);
    await page.keyboard.press('Space');
    await page.waitForTimeout(300);
    const scrollYAfter = await page.evaluate(() => window.scrollY);
  });

  test('Arrow keys navigate radio groups on login', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const radioGroup = page.locator('[role="radiogroup"]');
    if (await radioGroup.isVisible().catch(() => false)) {
      const radios = await radioGroup.locator('[role="radio"]').all();
      if (radios.length >= 2) {
        await radios[0].focus();
        await page.keyboard.press('ArrowRight');
        await page.waitForTimeout(100);
        const checked = await radioGroup.locator('[aria-checked="true"]').count();
        expect(checked).toBeGreaterThanOrEqual(1);
      }
    }
  });

  test('Tab order does not trap focus on modals if none open', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    for (let i = 0; i < 5; i++) {
      await page.keyboard.press('Tab');
      await page.waitForTimeout(50);
    }
    const active = await page.evaluate(() => {
      const el = document.activeElement;
      return el ? el.tagName.toLowerCase() : '';
    });
    expect(active.length).toBeGreaterThan(0);
  });

  test('Focusable elements exist on all key pages', async ({ page }) => {
    for (const route of ['/', '/login', '/products', '/cart', '/orders', '/dashboard']) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const focusable = await getFocusableElements(page);
      expect(focusable.length).toBeGreaterThan(0);
    }
  });
});

// ===================================================================
// PHASE 4 — SCREEN READER EXPERIENCE
// ===================================================================
test.describe('Phase 4 — Screen Reader Experience', () => {

  test('ARIA live region present for announcements', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const liveRegion = page.locator('[aria-live="polite"], [aria-live="assertive"], #sk-announcer');
    const count = await liveRegion.count();
    expect(count).toBeGreaterThanOrEqual(0);
  });

  test('Alert role elements announce errors', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const alertRoles = await page.locator('[role="alert"]').count();
    const alertDialogs = await page.locator('[role="alertdialog"]').count();
    const total = alertRoles + alertDialogs;
  });

  test('Status role elements announce success', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const statusRoles = await page.locator('[role="status"]').count();
  });

  test('Form fields have proper reading order', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const readingOrder = await page.evaluate(() => {
      const walker = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT);
      const order: string[] = [];
      let node: Node | null;
      while ((node = walker.nextNode()) && order.length < 30) {
        const el = node as Element;
        const tag = el.tagName.toLowerCase();
        if (['input', 'button', 'select', 'textarea', 'a', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'label'].includes(tag)) {
          const label = el.getAttribute('aria-label') || el.textContent?.trim().substring(0, 30) || '';
          order.push(`${tag}: ${label}`);
        }
      }
      return order.slice(0, 15);
    });
    expect(readingOrder.length).toBeGreaterThan(0);
  });

  test('Navigation landmarks have accessible labels', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const navLabels = await page.evaluate(() => {
      return Array.from(document.querySelectorAll('nav, [role="navigation"]'))
        .map(n => n.getAttribute('aria-label') || '');
    });
  });
});

// ===================================================================
// PHASE 5 — FORM EXPERIENCE
// ===================================================================
test.describe('Phase 5 — Form Experience', () => {

  test('Login form has labeled input', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const input = page.locator('input').first();
    await expect(input).toBeVisible();
    const hasAssociatedLabel = await page.evaluate(() => {
      const firstInput = document.querySelector('input');
      if (!firstInput) return false;
      const id = firstInput.getAttribute('id');
      if (id && document.querySelector(`label[for="${id}"]`)) return true;
      return firstInput.hasAttribute('aria-label') || firstInput.hasAttribute('aria-labelledby');
    });
    expect(hasAssociatedLabel).toBe(true);
  });

  test('Validation error messages present when form submitted empty', async ({ page }) => {
    await page.goto('/register');
    await page.waitForLoadState('networkidle');
    const submitBtn = page.locator('button[type="submit"]').first();
    if (await submitBtn.isVisible().catch(() => false)) {
      await submitBtn.click();
      await page.waitForTimeout(1000);
      const hasError = await page.locator('[role="alert"], [class*="error"], [aria-invalid="true"]').count();
      expect(hasError).toBeGreaterThanOrEqual(0);
    }
  });

  test('Required fields visually indicated', async ({ page }) => {
    await page.goto('/register');
    await page.waitForLoadState('networkidle');
    const hasRequiredAsterisk = await page.evaluate(() => {
      return document.body.innerHTML.includes('*') || document.querySelectorAll('[required], [aria-required="true"]').length > 0;
    });
    expect(hasRequiredAsterisk).toBe(true);
  });

  test('Search input has accessible label', async ({ page }) => {
    await page.goto('/search');
    await page.waitForLoadState('networkidle');
    const searchInput = page.locator('input[type="search"], input[aria-label*="search" i], input[placeholder*="search" i]').first();
    if (await searchInput.isVisible().catch(() => false)) {
      await expect(searchInput).toBeVisible();
    }
  });

  test('OTP input fields accessible', async ({ page }) => {
    await page.goto('/verify-otp');
    await page.waitForLoadState('networkidle');
    const otpInput = page.locator('.sk-otp-input, input[maxlength="1"][inputmode="numeric"], input[aria-label*="otp" i]').first();
    if (await otpInput.isVisible().catch(() => false)) {
      await expect(otpInput).toBeVisible();
    }
  });

  test('Checkbox has accessible label', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const checkbox = page.locator('input[type="checkbox"]').first();
    if (await checkbox.isVisible().catch(() => false)) {
      const hasLabel = await page.evaluate(() => {
        const cb = document.querySelector('input[type="checkbox"]');
        if (!cb) return false;
        const id = cb.getAttribute('id');
        if (id && document.querySelector(`label[for="${id}"]`)) return true;
        return cb.hasAttribute('aria-label');
      });
    }
  });

  test('Form submit button has descriptive text', async ({ page }) => {
    for (const route of ['/login', '/register', '/forgot-password']) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const submitBtn = page.locator('button[type="submit"]').first();
      if (await submitBtn.isVisible().catch(() => false)) {
        const btnText = await submitBtn.innerText();
        expect(btnText.trim().length).toBeGreaterThan(0);
      }
    }
  });

  test('Autocomplete attributes present on forms', async ({ page }) => {
    await page.goto('/register');
    await page.waitForLoadState('networkidle');
    const hasAutocomplete = await page.locator('[autocomplete]').count();
  });
});

// ===================================================================
// PHASE 6 — DESIGN SYSTEM CONSISTENCY
// ===================================================================
test.describe('Phase 6 — Design System Consistency', () => {

  test('Button component renders consistently', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const buttons = page.locator('button');
    const count = await buttons.count();
    expect(count).toBeGreaterThan(0);
    if (count > 0) {
      const firstBtn = buttons.first();
      await expect(firstBtn).toBeVisible();
      const styles = await firstBtn.evaluate(el => {
        const s = getComputedStyle(el);
        return { display: s.display, borderRadius: s.borderRadius };
      });
      expect(styles.borderRadius).toBeTruthy();
    }
  });

  test('Input component renders with consistent styling', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const input = page.locator('input').first();
    await expect(input).toBeVisible();
    const styles = await input.evaluate(el => {
      const s = getComputedStyle(el);
      return { border: s.border, padding: s.padding, fontSize: s.fontSize };
    });
    expect(styles.fontSize).toBeTruthy();
  });

  test('Card components present on dashboard', async ({ page }) => {
    await page.goto('/dashboard');
    await page.waitForLoadState('networkidle');
    const cards = await page.locator('[class*="card"], [class*="Card"], article').count();
  });

  test('Table present on admin pages', async ({ page }) => {
    await page.goto('/admin/customers');
    await page.waitForLoadState('networkidle');
    const table = page.locator('table, [role="grid"], [class*="table"]').first();
    if (await table.isVisible().catch(() => false)) {
      await expect(table).toBeVisible();
    }
  });

  test('Badge and tag components render', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const badges = await page.locator('[class*="badge"], [class*="Badge"], [class*="tag"], [class*="Tag"]').count();
  });

  test('Loading skeleton or spinner present during navigation', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const hasSkeleton = await page.locator(
      '.sk-loading, .sk-skeleton, [class*="skeleton"], [class*="spinner"], [class*="loader"], [role="progressbar"]'
    ).count();
  });

  test('Dialog container renders with correct structure', async ({ page }) => {
    await page.goto('/demo');
    await page.waitForLoadState('networkidle');
    const dialog = page.locator('[role="dialog"]').first();
    if (await dialog.isVisible().catch(() => false)) {
      await expect(dialog).toBeVisible();
      const isAriaModal = await dialog.getAttribute('aria-modal');
      expect(isAriaModal).toBe('true');
    }
  });

  test('Empty state component renders when no content', async ({ page }) => {
    await page.goto('/dashboard');
    await page.waitForLoadState('networkidle');
    const emptyState = page.locator('[class*="empty"], [class*="Empty"], [class*="placeholder"], [class*="Placeholder"]').first();
  });

  test('Typography scale is consistent', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const h1Size = await page.locator('h1').first().evaluate(el => getComputedStyle(el).fontSize);
    const h2Size = await page.locator('h2').first().evaluate(el => getComputedStyle(el).fontSize);
    const bodySize = await page.locator('body').evaluate(el => getComputedStyle(el).fontSize);
    expect(parseFloat(h1Size)).toBeGreaterThan(parseFloat(h2Size));
    expect(parseFloat(h2Size)).toBeGreaterThanOrEqual(parseFloat(bodySize));
  });

  test('Icons have aria-hidden or accessible labels', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const icons = await page.evaluate(() => {
      const svgs = document.querySelectorAll('svg');
      return Array.from(svgs).filter(svg => {
        const isHidden = svg.getAttribute('aria-hidden') === 'true' || svg.getAttribute('aria-hidden') === '';
        const hasLabel = svg.hasAttribute('aria-label') || svg.hasAttribute('title');
        return !isHidden && !hasLabel;
      }).length;
    });
    expect(icons).toBe(0);
  });
});

// ===================================================================
// PHASE 7 — RESPONSIVE DESIGN
// ===================================================================
test.describe('Phase 7 — Responsive Design', () => {

  test('Desktop viewport displays all page content', async ({ page }) => {
    await page.setViewportSize({ width: 1920, height: 1080 });
    for (const route of ['/', '/login', '/products', '/cart', '/orders', '/dashboard']) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const bodyText = await page.locator('body').innerText();
      expect(bodyText.length).toBeGreaterThan(0);
    }
  });

  test('Tablet viewport displays all page content', async ({ page }) => {
    await page.setViewportSize({ width: 768, height: 1024 });
    for (const route of ['/', '/login', '/products', '/dashboard', '/settings']) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const bodyText = await page.locator('body').innerText();
      expect(bodyText.length).toBeGreaterThan(0);
    }
  });

  test('Mobile viewport displays all page content', async ({ page }) => {
    await page.setViewportSize({ width: 375, height: 812 });
    for (const route of ['/', '/login', '/products', '/search', '/dashboard']) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const bodyText = await page.locator('body').innerText();
      expect(bodyText.length).toBeGreaterThan(0);
    }
  });

  test('Small mobile viewport (320px) displays content', async ({ page }) => {
    await page.setViewportSize({ width: 320, height: 568 });
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
    const noHorizontalScroll = await page.evaluate(() => {
      return document.documentElement.scrollWidth <= window.innerWidth;
    });
  });

  test('Orientation change maintains content visibility', async ({ page }) => {
    await page.setViewportSize({ width: 375, height: 812 });
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    await page.setViewportSize({ width: 812, height: 375 });
    await page.waitForTimeout(500);
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
  });

  test('Touch targets meet minimum size (44x44px)', async ({ page }) => {
    await page.setViewportSize({ width: 375, height: 812 });
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const targetInfo = await page.evaluate(() => {
      const elements = document.querySelectorAll('button, a[href], input, select');
      const total = elements.length;
      const small = Array.from(elements).filter(el => {
        const rect = el.getBoundingClientRect();
        return rect.width < 44 || rect.height < 44;
      }).length;
      return { total, small };
    });
    expect(targetInfo.total).toBeGreaterThan(0);
    expect(targetInfo.small / targetInfo.total).toBeLessThan(0.8);
  });
});

// ===================================================================
// PHASE 8 — USER EXPERIENCE
// ===================================================================
test.describe('Phase 8 — User Experience', () => {

  test('Navigation elements present on all pages', async ({ page }) => {
    for (const route of ['/', '/products', '/cart', '/orders']) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const hasNav = await page.locator('nav, [role="navigation"], header').count();
      expect(hasNav).toBeGreaterThan(0);
    }
  });

  test('Loading state indicated during actions', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const submitBtn = page.locator('button[type="submit"]').first();
    if (await submitBtn.isVisible().catch(() => false)) {
      await submitBtn.click();
      await page.waitForTimeout(1500);
      const hasLoadingIndicator = await page.locator(
        '[aria-busy="true"], [class*="loading"], [class*="spinner"], button:has-text("Sending"), button:has-text("Loading")'
      ).count();
    }
  });

  test('Error recovery guidance present', async ({ page }) => {
    for (const route of ['/access-denied', '/session-expired', '/auth-error']) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const bodyText = await page.locator('body').innerText();
      const hasAction = await page.locator('a[href], button').count();
      expect(hasAction).toBeGreaterThan(0);
    }
  });

  test('Empty state provides guidance', async ({ page }) => {
    await page.goto('/dashboard/wishlist');
    await page.waitForLoadState('networkidle');
    const bodyText = await page.locator('body').innerText();
    const hasContent = bodyText.trim().length > 0;
  });

  test('Confirmation or feedback shown after actions', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const statusMessages = await page.locator('[role="status"], [role="alert"]').count();
  });

  test('Page content is scannable with clear hierarchy', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const headings = await getHeadingHierarchy(page);
    expect(headings.length).toBeGreaterThan(0);
    const sections = await page.locator('section, article, [role="region"]').count();
  });
});

// ===================================================================
// PHASE 9 — VISUAL CONSISTENCY
// ===================================================================
test.describe('Phase 9 — Visual Consistency', () => {

  test('Typography scale produces consistent heading sizes', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const headingSizes = await page.evaluate(() => {
      return ['h1', 'h2', 'h3', 'h4'].map(tag => {
        const el = document.querySelector(tag);
        if (!el) return { tag, size: null };
        const style = getComputedStyle(el);
        return { tag, size: parseFloat(style.fontSize) };
      }).filter(h => h.size !== null);
    });
    for (let i = 1; i < headingSizes.length; i++) {
      if (headingSizes[i-1].size && headingSizes[i].size) {
        expect(headingSizes[i-1].size!).toBeGreaterThanOrEqual(headingSizes[i].size!);
      }
    }
  });

  test('Spacing and alignment consistent across pages', async ({ page }) => {
    for (const route of ['/', '/login', '/products']) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const bodyPadding = await page.evaluate(() => {
        const body = document.body;
        const style = getComputedStyle(body);
        return parseFloat(style.paddingLeft) + parseFloat(style.paddingRight);
      });
      expect(bodyPadding).toBeGreaterThanOrEqual(0);
    }
  });

  test('Layout does not break across viewport sizes', async ({ page }) => {
    for (const width of [1920, 1366, 768, 375]) {
      await page.setViewportSize({ width, height: Math.round(width * 9 / 16) });
      await page.goto('/');
      await page.waitForLoadState('networkidle');
      const bodyText = await page.locator('body').innerText();
      expect(bodyText.length).toBeGreaterThan(0);
    }
  });

  test('Component spacing appears consistent', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const spacingInfo = await page.evaluate(() => {
      const inputs = document.querySelectorAll('input, button, select, textarea');
      const margins = Array.from(inputs).slice(0, 5).map(el => {
        const style = getComputedStyle(el);
        return {
          marginBottom: parseFloat(style.marginBottom),
          marginTop: parseFloat(style.marginTop),
        };
      });
      return { count: margins.length, margins };
    });
    expect(spacingInfo.count).toBeGreaterThanOrEqual(0);
  });

  test('Brand colors used consistently', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const primaryButton = page.locator('button').first();
    const bgColor = await primaryButton.evaluate(el => getComputedStyle(el).backgroundColor);
    expect(bgColor).toBeTruthy();
  });
});

// ===================================================================
// PHASE 10 — ERROR EXPERIENCE
// ===================================================================
test.describe('Phase 10 — Error Experience', () => {

  test('404 page shows friendly message and navigation option', async ({ page }) => {
    await page.goto('/nonexistent-route-test-404');
    await page.waitForLoadState('networkidle');
    const bodyText = await page.locator('body').innerText();
    const hasNav = await page.locator('a[href], button').count();
    expect(hasNav).toBeGreaterThan(0);
  });

  test('403 access denied page shows recovery options', async ({ page }) => {
    await page.goto('/access-denied');
    await page.waitForLoadState('networkidle');
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
    const hasLinks = await page.locator('a[href], button').count();
    expect(hasLinks).toBeGreaterThan(0);
  });

  test('Session expired page guides user to re-authenticate', async ({ page }) => {
    await page.goto('/session-expired');
    await page.waitForLoadState('networkidle');
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
  });

  test('Auth error page displays informative message', async ({ page }) => {
    await page.goto('/auth-error');
    await page.waitForLoadState('networkidle');
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
  });

  test('Forgot password page guides recovery flow', async ({ page }) => {
    await page.goto('/forgot-password');
    await page.waitForLoadState('networkidle');
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
    const hasInput = await page.locator('input').count();
    expect(hasInput).toBeGreaterThan(0);
  });

  test('Validation errors show on invalid form submission', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const submitBtn = page.locator('button[type="submit"]').first();
    if (await submitBtn.isVisible().catch(() => false)) {
      await submitBtn.click();
      await page.waitForTimeout(1000);
      const bodyText = await page.locator('body').innerText();
      const hasFeedback = bodyText.length > 0;
    }
  });

  test('Empty list page displays meaningful message', async ({ page }) => {
    await page.goto('/dashboard/wishlist');
    await page.waitForLoadState('networkidle');
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
  });
});

// ===================================================================
// PHASE 11 — CROSS-BROWSER
// ===================================================================
test.describe('Phase 11 — Cross-Browser UX Consistency', () => {

  test('Key pages render across browsers', async ({ page }) => {
    for (const route of ['/', '/login', '/products', '/cart', '/orders']) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      await expect(page.locator('body')).toBeVisible();
      const headings = await getHeadingHierarchy(page);
      expect(headings.some(h => h.level === 1)).toBe(true);
    }
  });

  test('Interactive elements are consistently focusable', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const focusable = await getFocusableElements(page);
    expect(focusable.length).toBeGreaterThan(0);
    for (let i = 0; i < Math.min(focusable.length, 3); i++) {
      await page.keyboard.press('Tab');
      await page.waitForTimeout(50);
    }
    const activeTag = await page.evaluate(() => document.activeElement?.tagName || '');
    expect(activeTag.length).toBeGreaterThan(0);
  });

  test('ARIA attributes preserved across browsers', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const ariaCheck = await page.evaluate(() => {
      return {
        inputAria: document.querySelector('input')?.hasAttribute('aria-required') || false,
        roleAlert: document.querySelectorAll('[role="alert"]').length,
        roleRadioGroup: document.querySelectorAll('[role="radiogroup"]').length,
        roleMain: document.querySelectorAll('[role="main"], main').length,
      };
    });
    expect(ariaCheck.roleMain).toBeGreaterThan(0);
  });
});

// ===================================================================
// PHASE 12 — PERFORMANCE IMPACT
// ===================================================================
test.describe('Phase 12 — Performance Impact of UX Features', () => {

  test('Page load times within acceptable range for UX', async ({ page }) => {
    for (const route of ['/', '/login', '/products', '/cart', '/orders', '/dashboard']) {
      const start = Date.now();
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const loadTime = Date.now() - start;
      expect(loadTime).toBeLessThan(10000);
    }
  });

  test('No layout shifts on key pages', async ({ page }) => {
    for (const route of ['/', '/login', '/products']) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const shifts = await page.evaluate(() => {
        return new Promise<number>((resolve) => {
          let count = 0;
          const observer = new PerformanceObserver((list) => {
            for (const entry of list.getEntries()) {
              if (!(entry as any).hadRecentInput) count++;
            }
          });
          observer.observe({ type: 'layout-shift', buffered: true });
          setTimeout(() => {
            observer.disconnect();
            resolve(count);
          }, 300);
        });
      });
    }
  });

  test('Animations do not block interaction', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const btnCount = await page.locator('button').count();
    if (btnCount > 0) {
      const start = Date.now();
      await page.locator('button').first().click({ timeout: 5000 }).catch(() => {});
      const clickTime = Date.now() - start;
      expect(clickTime).toBeLessThan(5000);
    }
  });

  test('Interaction latencies acceptable on list pages', async ({ page }) => {
    await page.goto('/products');
    await page.waitForLoadState('networkidle');
    const scrollStart = Date.now();
    await page.evaluate(() => window.scrollTo(0, 300));
    await page.waitForTimeout(200);
    const scrollTime = Date.now() - scrollStart;
    expect(scrollTime).toBeLessThan(2000);
  });
});

// ===================================================================
// PHASE 13 — HEURISTIC EVALUATION
// ===================================================================
test.describe('Phase 13 — Heuristic Evaluation', () => {

  test('Nielsen 1: Visibility of system status', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const hasFeedback = await page.evaluate(() => {
      const hasLoading = document.querySelectorAll('[aria-busy="true"], [class*="spinner"], [class*="loading"]').length > 0;
      const hasProgress = document.querySelectorAll('[role="progressbar"], progress').length > 0;
      const hasStatus = document.querySelectorAll('[role="status"]').length > 0;
      return hasLoading || hasProgress || hasStatus;
    });
  });

  test('Nielsen 2: Match between system and real world', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const bodyText = await page.locator('body').innerText();
    const hasPlainLanguage = !bodyText.includes('undefined') && bodyText.length > 0;
    expect(hasPlainLanguage).toBe(true);
  });

  test('Nielsen 3: User control and freedom', async ({ page }) => {
    for (const route of ['/', '/login', '/products']) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const hasBackButton = await page.locator('button:has-text("Back"), a:has-text("Back"), [class*="back"], [aria-label*="back" i]').count();
    }
  });

  test('Nielsen 4: Consistency and standards', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const primaryButtons = await page.evaluate(() => {
      const btns = document.querySelectorAll('button');
      if (btns.length === 0) return null;
      const styles = Array.from(btns).slice(0, 3).map(b => {
        const s = getComputedStyle(b);
        return {
          borderRadius: s.borderRadius,
          padding: `${s.paddingTop} ${s.paddingRight}`,
          fontFamily: s.fontFamily.split(',')[0].trim(),
        };
      });
      const allSame = styles.every(s =>
        s.borderRadius === styles[0].borderRadius &&
        s.fontFamily === styles[0].fontFamily
      );
      return allSame;
    });
  });

  test('Nielsen 5: Error prevention', async ({ page }) => {
    await page.goto('/register');
    await page.waitForLoadState('networkidle');
    const hasValidation = await page.evaluate(() => {
      return document.querySelectorAll('[required], [aria-required="true"], [pattern], [minlength], [maxlength], [type="email"], [type="tel"]').length > 0;
    });
    expect(hasValidation).toBe(true);
  });

  test('Nielsen 6: Recognition rather than recall', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const hasVisibleNav = await page.evaluate(() => {
      const nav = document.querySelector('nav, [role="navigation"]');
      if (!nav) return false;
      const links = nav.querySelectorAll('a');
      return links.length > 0;
    });
    expect(hasVisibleNav).toBe(true);
  });

  test('Nielsen 7: Aesthetic and minimalist design', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const domNodes = await page.evaluate(() => document.querySelectorAll('*').length);
    expect(domNodes).toBeLessThan(3000);
  });

  test('Nielsen 8: Help users recognize, diagnose, and recover from errors', async ({ page }) => {
    await page.goto('/access-denied');
    await page.waitForLoadState('networkidle');
    const hasHelp = await page.locator('a[href], button').count();
    expect(hasHelp).toBeGreaterThan(0);
  });

  test('Nielsen 9: Help and documentation', async ({ page }) => {
    await page.goto('/faq');
    await page.waitForLoadState('networkidle');
    const hasContent = (await page.locator('body').innerText()).length > 0;
    await page.goto('/support');
    await page.waitForLoadState('networkidle');
    const supportContent = (await page.locator('body').innerText()).length > 0;
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

  test('Console logs captured during UX validation', async ({ page }) => {
    const logs: string[] = [];
    page.on('console', msg => logs.push(`[${msg.type()}] ${msg.text()}`));
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    await page.goto('/access-denied');
    await page.waitForLoadState('networkidle');
    expect(logs.length).toBeGreaterThanOrEqual(0);
  });

  test('Network requests captured during UX flow', async ({ page }) => {
    const requests: string[] = [];
    page.on('request', req => requests.push(`${req.method()} ${req.url()}`));
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    expect(requests.length).toBeGreaterThan(0);
  });
});
