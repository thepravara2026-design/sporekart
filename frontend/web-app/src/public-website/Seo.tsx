import { useEffect } from 'react';

export interface SeoProps {
  title?: string;
  description?: string;
  canonical?: string;
  type?: string;
  image?: string;
  noindex?: boolean;
  structuredData?: Record<string, unknown> | Record<string, unknown>[];
}

function ensureMeta(selector: string, attributes: Record<string, string>): HTMLMetaElement {
  const existing = document.head.querySelector<HTMLMetaElement>(selector);
  if (existing) {
    return existing;
  }
  const meta = document.createElement('meta');
  Object.entries(attributes).forEach(([key, value]) => meta.setAttribute(key, value));
  document.head.appendChild(meta);
  return meta;
}

function setMetaContent(selector: string, attributes: Record<string, string>, content: string): void {
  const meta = ensureMeta(selector, attributes);
  meta.setAttribute('content', content);
}

function ensureLink(rel: string, href: string): HTMLLinkElement {
  const selector = `link[rel="${rel}"]`;
  const existing = document.head.querySelector<HTMLLinkElement>(selector);
  if (existing) {
    existing.setAttribute('href', href);
    return existing;
  }
  const link = document.createElement('link');
  link.setAttribute('rel', rel);
  link.setAttribute('href', href);
  document.head.appendChild(link);
  return link;
}

function ensureTitle(): HTMLTitleElement {
  if (document.head.querySelector('title')) {
    return document.head.querySelector('title') as HTMLTitleElement;
  }
  const title = document.createElement('title');
  document.head.appendChild(title);
  return title;
}

export function Seo({
  title,
  description,
  canonical,
  type = 'website',
  image,
  noindex = false,
  structuredData,
}: SeoProps): null {
  useEffect(() => {
    const fullTitle = title ? `${title} | SporeKart` : 'SporeKart | Mushroom cultivation, simplified.';

    const titleEl = ensureTitle();
    titleEl.textContent = fullTitle;

    if (description) {
      setMetaContent('meta[name="description"]', { name: 'description' }, description);
    }

    setMetaContent('meta[name="robots"]', { name: 'robots' }, noindex ? 'noindex, nofollow' : 'index, follow');

    setMetaContent('meta[property="og:title"]', { property: 'og:title' }, fullTitle);
    if (description) {
      setMetaContent('meta[property="og:description"]', { property: 'og:description' }, description);
    }
    setMetaContent('meta[property="og:type"]', { property: 'og:type' }, type);
    if (canonical) {
      setMetaContent('meta[property="og:url"]', { property: 'og:url' }, canonical);
    }
    if (image) {
      setMetaContent('meta[property="og:image"]', { property: 'og:image' }, image);
    }

    setMetaContent('meta[name="twitter:card"]', { name: 'twitter:card' }, image ? 'summary_large_image' : 'summary');
    setMetaContent('meta[name="twitter:title"]', { name: 'twitter:title' }, fullTitle);
    if (description) {
      setMetaContent('meta[name="twitter:description"]', { name: 'twitter:description' }, description);
    }

    if (canonical) {
      ensureLink('canonical', canonical);
    }

    let script: HTMLScriptElement | null = null;
    if (structuredData) {
      const existing = document.head.querySelector<HTMLScriptElement>('script[type="application/ld+json"][data-sk-seo]');
      script = existing ?? document.createElement('script');
      script.type = 'application/ld+json';
      script.setAttribute('data-sk-seo', 'true');
      script.textContent = JSON.stringify(structuredData);
      if (!existing) {
        document.head.appendChild(script);
      }
    }

    return () => {
      if (script && script.parentNode) {
        script.parentNode.removeChild(script);
      }
    };
  }, [title, description, canonical, type, image, noindex, structuredData]);

  return null;
}
