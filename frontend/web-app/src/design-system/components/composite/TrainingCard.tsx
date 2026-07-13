import React from 'react';
import { Card } from './Card';

export interface TrainingCardProps {
  title: string;
  instructor?: string;
  duration?: string;
  level?: 'beginner' | 'intermediate' | 'advanced' | 'all';
  rating?: number;
  thumbnail?: string;
  progress?: number;
  loading?: boolean;
}

const levelColors: Record<string, string> = {
  beginner: 'var(--color-success)',
  intermediate: 'var(--color-warning)',
  advanced: 'var(--color-danger)',
  all: 'var(--color-info)',
};

export const TrainingCard: React.FC<TrainingCardProps> = ({
  title,
  instructor,
  duration,
  level,
  rating,
  thumbnail,
  progress,
  loading = false,
}) => {
  return (
    <Card variant="default" padding="none" loading={loading}>
      <div
        className="sk-trainingcard__thumbnail-wrapper"
        style={{
          position: 'relative',
          overflow: 'hidden',
          borderRadius: 'var(--radius-card) var(--radius-card) 0 0',
        }}
      >
        {thumbnail ? (
          <img
            src={thumbnail}
            alt={title}
            className="sk-trainingcard__thumbnail"
            style={{
              width: '100%',
              height: 160,
              objectFit: 'cover',
              display: 'block',
            }}
          />
        ) : (
          <div
            className="sk-trainingcard__thumbnail-placeholder"
            style={{
              width: '100%',
              height: 160,
              background: 'var(--color-bg-skeleton-base)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              color: 'var(--color-text-disabled)',
              fontSize: 'var(--text-caption)',
            }}
          >
            No Thumbnail
          </div>
        )}
        {level && (
          <span
            className="sk-trainingcard__level-badge"
            style={{
              position: 'absolute',
              top: 'var(--space-2)',
              right: 'var(--space-2)',
              fontSize: 'var(--text-caption)',
              fontWeight: 'var(--weight-medium)',
              padding: '2px var(--space-2)',
              borderRadius: 'var(--radius-tag)',
              background: levelColors[level],
              color: '#FFFFFF',
            }}
          >
            {level}
          </span>
        )}
      </div>
      <div
        className="sk-trainingcard__body"
        style={{
          padding: 'var(--space-3)',
          display: 'flex',
          flexDirection: 'column',
          gap: 'var(--space-stack-xs)',
        }}
      >
        <h3
          className="sk-trainingcard__title"
          style={{
            fontSize: 'var(--text-body)',
            fontWeight: 'var(--weight-semibold)',
            color: 'var(--color-text-primary)',
            margin: 0,
            overflow: 'hidden',
            textOverflow: 'ellipsis',
            whiteSpace: 'nowrap',
          }}
        >
          {title}
        </h3>
        <div
          className="sk-trainingcard__meta"
          style={{
            display: 'flex',
            flexWrap: 'wrap',
            gap: 'var(--space-inline-sm)',
            fontSize: 'var(--text-caption)',
            color: 'var(--color-text-secondary)',
          }}
        >
          {instructor && <span>{instructor}</span>}
          {duration && <span>{duration}</span>}
        </div>
        {rating != null && (
          <div
            className="sk-trainingcard__rating"
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: 'var(--space-inline-xs)',
              fontSize: 'var(--text-caption)',
              color: 'var(--color-text-secondary)',
            }}
          >
            <span style={{ color: 'var(--color-warning)' }}>{'\u2605'}</span>
            <span>{rating.toFixed(1)}</span>
          </div>
        )}
        {progress != null && (
          <div
            className="sk-trainingcard__progress"
            style={{
              width: '100%',
              height: 6,
              borderRadius: 'var(--radius-full)',
              background: 'var(--color-bg-skeleton-base)',
              overflow: 'hidden',
              marginTop: 'var(--space-stack-xs)',
            }}
          >
            <div
              className="sk-trainingcard__progress-bar"
              style={{
                width: `${Math.min(100, Math.max(0, progress))}%`,
                height: '100%',
                background: 'var(--color-bg-primary-default)',
                borderRadius: 'var(--radius-full)',
                transition: 'width var(--duration-slow) var(--easing-standard)',
              }}
            />
          </div>
        )}
      </div>
    </Card>
  );
};

TrainingCard.displayName = 'TrainingCard';

export default TrainingCard;
