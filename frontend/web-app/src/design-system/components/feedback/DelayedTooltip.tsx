import React from 'react';
import { Tooltip, TooltipProps } from './Tooltip';

export interface DelayedTooltipProps extends TooltipProps {
  showDelay?: number;
  hideDelay?: number;
}

export const DelayedTooltip: React.FC<DelayedTooltipProps> = ({
  showDelay = 500,
  hideDelay = 300,
  ...props
}) => {
  return (
    <Tooltip
      showDelay={showDelay}
      hideDelay={hideDelay}
      {...props}
    />
  );
};

DelayedTooltip.displayName = 'DelayedTooltip';
export default DelayedTooltip;
