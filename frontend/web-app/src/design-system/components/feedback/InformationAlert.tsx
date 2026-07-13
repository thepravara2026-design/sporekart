import React from 'react';
import { Alert, AlertProps } from './Alert';

export interface InformationAlertProps extends AlertProps {}

export const InformationAlert: React.FC<InformationAlertProps> = ({
  className = '',
  ...props
}) => {
  return (
    <Alert
      type="info"
      className={className}
      {...props}
    />
  );
};

InformationAlert.displayName = 'InformationAlert';
export default InformationAlert;
