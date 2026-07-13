import React from 'react';
import { Alert, AlertProps } from './Alert';

export interface PersistentAlertProps extends AlertProps {}

export const PersistentAlert: React.FC<PersistentAlertProps> = ({
  className = '',
  ...props
}) => {
  return (
    <Alert
      {...props}
      dismissible={false}
      className={className}
    />
  );
};

PersistentAlert.displayName = 'PersistentAlert';
export default PersistentAlert;
