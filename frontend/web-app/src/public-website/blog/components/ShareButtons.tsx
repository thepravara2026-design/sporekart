import { useState } from 'react';
import { Icon } from '../../../design-system/icons/Icon';

export function ShareButtons({ url, title }: { url: string; title: string }) {
  const [copied, setCopied] = useState(false);
  const shareUrl = encodeURIComponent(url);
  const shareTitle = encodeURIComponent(title);

  const links = [
    { label: 'Share on X', href: `https://twitter.com/intent/tweet?url=${shareUrl}&text=${shareTitle}`, icon: 'external-link' },
    { label: 'Share on LinkedIn', href: `https://www.linkedin.com/sharing/share-offsite/?url=${shareUrl}`, icon: 'external-link' },
    { label: 'Share on WhatsApp', href: `https://wa.me/?text=${shareTitle}%20${shareUrl}`, icon: 'external-link' },
  ];

  const copy = async () => {
    try {
      await navigator.clipboard.writeText(url);
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    } catch {
      setCopied(false);
    }
  };

  return (
    <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-2, 8px)' }} aria-label="Share this article">
      {links.map((l) => (
        <a key={l.label} className="sk-blog-share" href={l.href} target="_blank" rel="noopener noreferrer" aria-label={l.label}>
          <Icon name={l.icon} size={18} aria-label={l.label} />
        </a>
      ))}
      <button type="button" className="sk-blog-share" onClick={copy} aria-label={copied ? 'Link copied' : 'Copy link'}>
        <Icon name={copied ? 'check-circle' : 'book-open'} size={18} aria-label={copied ? 'Link copied' : 'Copy link'} />
      </button>
    </div>
  );
}
