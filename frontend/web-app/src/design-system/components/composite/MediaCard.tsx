import React from 'react';
import { Card } from './Card';

export interface MediaCardProps {
  title: string;
  image?: string;
  video?: string;
  description?: string;
  actions?: React.ReactNode;
  loading?: boolean;
  error?: string;
}

export const MediaCard: React.FC<MediaCardProps> = ({
  title,
  image,
  video,
  description,
  actions,
  loading = false,
  error,
}) => {
  const hasMedia = !!(image || video);

  return (
    <Card variant="default" padding="none" loading={loading} error={error}>
      {hasMedia && (
        <div
          className="sk-mediacard__media"
          style={{
            overflow: 'hidden',
            borderRadius: 'var(--radius-card) var(--radius-card) 0 0',
          }}
        >
          {image && (
            <img
              src={image}
              alt={title}
              className="sk-mediacard__image"
              style={{
                width: '100%',
                height: 'auto',
                display: 'block',
                aspectRatio: '16 / 9',
                objectFit: 'cover',
              }}
            />
          )}
          {video && (
            <video
              src={video}
              className="sk-mediacard__video"
              style={{
                width: '100%',
                display: 'block',
                aspectRatio: '16 / 9',
              }}
              controls
            />
          )}
        </div>
      )}
      <div
        className="sk-mediacard__body"
        style={{
          padding: hasMedia ? 'var(--space-3)' : 'var(--space-4)',
          display: 'flex',
          flexDirection: 'column',
          gap: 'var(--space-stack-xs)',
        }}
      >
        <h3
          className="sk-mediacard__title"
          style={{
            fontSize: 'var(--text-h6)',
            fontWeight: 'var(--weight-semibold)',
            color: 'var(--color-text-primary)',
            margin: 0,
          }}
        >
          {title}
        </h3>
        {description && (
          <p
            className="sk-mediacard__description"
            style={{
              fontSize: 'var(--text-body-sm)',
              color: 'var(--color-text-secondary)',
              margin: 0,
              lineHeight: 'var(--leading-relaxed)',
            }}
          >
            {description}
          </p>
        )}
        {actions && (
          <div
            className="sk-mediacard__actions"
            style={{
              display: 'flex',
              gap: 'var(--space-inline-sm)',
              marginTop: 'var(--space-stack-xs)',
              paddingTop: 'var(--space-stack-xs)',
              borderTop: 'var(--border-width-thin) solid var(--color-border-default)',
            }}
          >
            {actions}
          </div>
        )}
      </div>
    </Card>
  );
};

MediaCard.displayName = 'MediaCard';

export default MediaCard;
