import React from 'react';
import { Alert, AlertProps } from './Alert';

export interface ErrorAlertProps extends AlertProps {}

export const ErrorAlert: React.FC<ErrorAlertProps> = ({
  className = '',
  ...props
}) => {
  return (
    <Alert
      type="error"
      className={className}
      {...props}
    />
  );
};

ErrorAlert.displayName = 'ErrorAlert';
export default ErrorAlert;
