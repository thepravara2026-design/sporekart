import React from 'react';
import { Alert, AlertProps } from './Alert';

export interface DismissibleAlertProps extends AlertProps {}

export const DismissibleAlert: React.FC<DismissibleAlertProps> = ({
  onClose,
  className = '',
  ...props
}) => {
  return (
    <Alert
      {...props}
      dismissible
      onClose={onClose}
      className={className}
    />
  );
};

DismissibleAlert.displayName = 'DismissibleAlert';
export default DismissibleAlert;
