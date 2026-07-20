import { test, expect } from '@playwright/test';

test('Debug login page content', async ({ page }) => {
  await page.goto('/login');
  await page.waitForLoadState('networkidle');
  await page.waitForTimeout(3000);
  
  const html = await page.content();
  console.log('=== PAGE TITLE ===', await page.title());
  console.log('=== URL ===', page.url());
  
  // Check for key elements
  const bodyText = await page.locator('body').innerText();
  console.log('=== BODY TEXT ===');
  console.log(bodyText.substring(0, 2000));
  
  // Check breadcrumbs / loading / error states
  const hasLoading = await page.locator('.loading, .spinner, [role="status"]').count();
  console.log('=== LOADING INDICATORS ===', hasLoading);
  
  // Take screenshot
  await page.screenshot({ path: 'debug-login.png', fullPage: true });
});
