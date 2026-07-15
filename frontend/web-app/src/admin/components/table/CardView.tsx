import React, { memo } from 'react';

interface CardViewProps<T> {
  data: T[];
  renderCard: (row: T) => React.ReactNode;
  rowKeyFn: (row: T) => string;
}

export const CardView = memo(function CardView<T>({ data, renderCard, rowKeyFn }: CardViewProps<T>) {
  if (data.length === 0) return null;
  return (
    <div
      style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))',
        gap: 'var(--space-component-gap)',
      }}
    >
      {data.map((row) => (
        <div key={rowKeyFn(row)}>{renderCard(row)}</div>
      ))}
    </div>
  );
}) as React.FC<CardViewProps<any>>;
