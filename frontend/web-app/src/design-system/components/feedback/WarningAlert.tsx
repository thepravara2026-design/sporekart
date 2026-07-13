import React from 'react';
import { Alert, AlertProps } from './Alert';

export interface WarningAlertProps extends AlertProps {}

export const WarningAlert: React.FC<WarningAlertProps> = ({
  className = '',
  ...props
}) => {
  return (
    <Alert
      type="warning"
      className={className}
      {...props}
    />
  );
};

WarningAlert.displayName = 'WarningAlert';
export default WarningAlert;
