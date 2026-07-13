import React from 'react';
import { Alert, AlertProps } from './Alert';

export interface SuccessAlertProps extends AlertProps {}

export const SuccessAlert: React.FC<SuccessAlertProps> = ({
  className = '',
  ...props
}) => {
  return (
    <Alert
      type="success"
      className={className}
      {...props}
    />
  );
};

SuccessAlert.displayName = 'SuccessAlert';
export default SuccessAlert;
