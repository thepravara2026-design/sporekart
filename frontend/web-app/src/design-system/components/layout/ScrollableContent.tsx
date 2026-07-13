import React from 'react';

export interface ScrollableContentProps {
  children: React.ReactNode;
  className?: string;
  hideScrollbar?: boolean;
  onScroll?: (e: React.UIEvent<HTMLDivElement>) => void;
}

export const ScrollableContent: React.FC<ScrollableContentProps> = ({
  children,
  className = '',
  hideScrollbar = false,
  onScroll,
}) => {
  const style: React.CSSProperties = {
    overflowY: 'auto',
    flex: 1,
    ...(hideScrollbar ? { scrollbarWidth: 'none' } : {}),
  };

  return (
    <div
      className={`sk-scrollable-content ${className}`.trim()}
      style={style}
      onScroll={onScroll}
    >
      {children}
    </div>
  );
};

ScrollableContent.displayName = 'ScrollableContent';
export default ScrollableContent;
