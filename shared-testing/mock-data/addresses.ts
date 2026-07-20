export interface Address {
  id: string;
  customerId: string;
  label: string;
  line1: string;
  line2: string;
  city: string;
  state: string;
  pincode: string;
  isDefault: boolean;
}

export const addresses: Address[] = [
  {
    id: 'ADDR-001',
    customerId: 'CUST-001',
    label: 'Home',
    line1: '123 Green Field',
    line2: 'Koregaon Park',
    city: 'Pune',
    state: 'Maharashtra',
    pincode: '411001',
    isDefault: true,
  },
  {
    id: 'ADDR-002',
    customerId: 'CUST-001',
    label: 'Farm',
    line1: 'Village Road',
    line2: 'Taluka Haveli',
    city: 'Pune',
    state: 'Maharashtra',
    pincode: '412201',
    isDefault: false,
  },
  {
    id: 'ADDR-003',
    customerId: 'CUST-002',
    label: 'Home',
    line1: '456 Garden Road',
    line2: 'Andheri West',
    city: 'Mumbai',
    state: 'Maharashtra',
    pincode: '400053',
    isDefault: true,
  },
];
