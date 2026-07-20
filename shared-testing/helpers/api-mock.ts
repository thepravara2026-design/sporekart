import { Page } from '@playwright/test';

export async function mockApiResponse(page: Page, urlPattern: string, responseBody: unknown, status = 200): Promise<void> {
  await page.route(urlPattern, async (route) => {
    await route.fulfill({
      status,
      contentType: 'application/json',
      body: JSON.stringify(responseBody),
    });
  });
}

export async function mockApiError(page: Page, urlPattern: string, status = 500, message = 'Internal Server Error'): Promise<void> {
  await page.route(urlPattern, async (route) => {
    await route.fulfill({
      status,
      contentType: 'application/json',
      body: JSON.stringify({ error: message }),
    });
  });
}

export async function mockApiDelay(page: Page, urlPattern: string, delayMs = 2000): Promise<void> {
  await page.route(urlPattern, async (route) => {
    await new Promise((r) => setTimeout(r, delayMs));
    await route.continue();
  });
}

export async function mockNetworkError(page: Page, urlPattern: string): Promise<void> {
  await page.route(urlPattern, async (route) => {
    await route.abort('connectionrefused');
  });
}
