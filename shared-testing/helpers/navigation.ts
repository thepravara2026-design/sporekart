import { Page, expect } from '@playwright/test';
import { getBaseUrl } from '../utils/env';

export async function navigateTo(page: Page, path: string): Promise<void> {
  await page.goto(`${getBaseUrl()}${path}`);
  await page.waitForLoadState('networkidle');
}

export async function expectUrl(page: Page, expectedPath: string): Promise<void> {
  const url = new URL(page.url());
  expect(url.pathname).toBe(expectedPath);
}

export async function expectRedirectedTo(page: Page, expectedPath: string, timeout = 5000): Promise<void> {
  await page.waitForURL((url) => url.pathname === expectedPath, { timeout });
}

export async function clickSidebarLink(page: Page, label: string): Promise<void> {
  const link = page.locator(`nav a:has-text("${label}"), .sidebar a:has-text("${label}"), [role="navigation"] a:has-text("${label}")`);
  await link.first().click();
  await page.waitForTimeout(500);
}
