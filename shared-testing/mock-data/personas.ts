export interface Persona {
  name: string;
  role: string;
  phone: string;
  email: string;
}

export const personas: Record<string, Persona> = {
  guest: {
    name: 'Guest User',
    role: 'guest',
    phone: '',
    email: '',
  },
  customer: {
    name: 'Ravi Sharma',
    role: 'customer',
    phone: '9876543210',
    email: 'ravi.sharma@example.com',
  },
  returningCustomer: {
    name: 'Priya Patel',
    role: 'customer',
    phone: '9876543211',
    email: 'priya.patel@example.com',
  },
  grower: {
    name: 'Amit Singh',
    role: 'grower',
    phone: '9876543212',
    email: 'amit.singh@example.com',
  },
  trainer: {
    name: 'Sunita Verma',
    role: 'trainer',
    phone: '9876543213',
    email: 'sunita.verma@example.com',
  },
  distributor: {
    name: 'Vikram Joshi',
    role: 'distributor',
    phone: '9876543214',
    email: 'vikram.joshi@example.com',
  },
  support: {
    name: 'Anita Kumar',
    role: 'support',
    phone: '9876543215',
    email: 'anita.kumar@example.com',
  },
  administrator: {
    name: 'Admin User',
    role: 'administrator',
    phone: '9876543216',
    email: 'admin@sporekart.com',
  },
  businessOwner: {
    name: 'Rajesh Gupta',
    role: 'business_owner',
    phone: '9876543217',
    email: 'rajesh.gupta@example.com',
  },
  governanceManager: {
    name: 'Meera Nair',
    role: 'governance_manager',
    phone: '9876543218',
    email: 'meera.nair@example.com',
  },
};

export function getPersona(role: string): Persona {
  const found = Object.values(personas).find((p) => p.role === role);
  if (!found) throw new Error(`Persona not found for role: ${role}`);
  return found;
}
