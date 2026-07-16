import type { Student, StudentDashboardStats, StudentQuickAction, StudentStatus, LearningMode } from '../types';

const firstNames = [
  'Aarav', 'Priya', 'Rohan', 'Ananya', 'Vikram', 'Sneha', 'Arjun', 'Kavya', 'Rahul', 'Isha',
  'Amit', 'Neha', 'Raj', 'Pooja', 'Suresh', 'Lakshmi', 'Deepak', 'Meera', 'Manoj', 'Anita',
  'Sunil', 'Deepa', 'Vijay', 'Shweta', 'Kiran', 'Rekha', 'Prakash', 'Shalini', 'Anil', 'Geeta',
  'Ravi', 'Divya', 'Sachin', 'Ritu', 'Naveen', 'Nisha', 'Gaurav', 'Radhika', 'Harsh', 'Shikha',
  'Siddharth', 'Tanya', 'Akash', 'Bhavna', 'Dinesh', 'Charvi', 'Ganesh', 'Hema', 'Harish', 'Jaya',
];

const lastNames = [
  'Sharma', 'Patel', 'Kumar', 'Singh', 'Verma', 'Gupta', 'Reddy', 'Nair', 'Joshi', 'Deshmukh',
  'Mehta', 'Rao', 'Choudhury', 'Pillai', 'Menon', 'Iyer', 'Bose', 'Sen', 'Das', 'Ghosh',
  'Banerjee', 'Chatterjee', 'Mukherjee', 'Saha', 'Acharya', 'Mishra', 'Tripathi', 'Tiwari', 'Dwivedi', 'Pandey',
  'Yadav', 'Khan', 'Ansari', 'Shaikh', 'Siddiqui', 'Fernandes', 'D\'Souza', 'Rodrigues', 'Thomas', 'George',
  'Philip', 'Samuel', 'Mathews', 'Kurian', 'Varghese', 'John', 'Jacob', 'Abraham', 'Cherian', 'Daniel',
];

const genders: ('male' | 'female' | 'other' | 'not-specified')[] = ['male', 'female', 'male', 'female', 'male', 'female', 'male', 'female', 'male', 'female'];

const nationalities = [
  'Indian', 'Indian', 'Indian', 'Indian', 'Indian', 'Indian', 'Indian', 'Indian', 'Indian', 'Indian',
  'Indian', 'Indian', 'Indian', 'Indian', 'Indian', 'Indian', 'Indian', 'Indian', 'Indian', 'Indian',
  'Indian', 'Indian', 'Nepali', 'Bangladeshi', 'Sri Lankan', 'Bhutanese', 'Maldivian', 'Indian', 'Indian', 'Indian',
];

const states = [
  'Maharashtra', 'Gujarat', 'Karnataka', 'Tamil Nadu', 'Uttar Pradesh', 'West Bengal', 'Kerala', 'Rajasthan',
  'Madhya Pradesh', 'Bihar', 'Punjab', 'Haryana', 'Delhi', 'Telangana', 'Andhra Pradesh', 'Odisha',
  'Assam', 'Jharkhand', 'Chhattisgarh', 'Uttarakhand',
];

const districts: Record<string, string[]> = {
  'Maharashtra': ['Pune', 'Mumbai', 'Nagpur', 'Nashik', 'Aurangabad', 'Thane', 'Solapur', 'Kolhapur'],
  'Gujarat': ['Ahmedabad', 'Surat', 'Vadodara', 'Rajkot', 'Bhavnagar', 'Jamnagar', 'Anand', 'Gandhinagar'],
  'Karnataka': ['Bengaluru', 'Mysuru', 'Hubli', 'Mangaluru', 'Belagavi', 'Dharwad', 'Shivamogga', 'Tumakuru'],
  'Tamil Nadu': ['Chennai', 'Coimbatore', 'Madurai', 'Tiruchirappalli', 'Salem', 'Tirunelveli', 'Vellore', 'Erode'],
  'Uttar Pradesh': ['Lucknow', 'Kanpur', 'Varanasi', 'Agra', 'Prayagraj', 'Ghaziabad', 'Noida', 'Meerut'],
  'West Bengal': ['Kolkata', 'Howrah', 'Darjeeling', 'Siliguri', 'Bardhaman', 'Nadia', 'Murshidabad', 'Hooghly'],
  'Kerala': ['Thiruvananthapuram', 'Kochi', 'Kozhikode', 'Thrissur', 'Alappuzha', 'Kollam', 'Palakkad', 'Kannur'],
  'Rajasthan': ['Jaipur', 'Jodhpur', 'Udaipur', 'Kota', 'Bikaner', 'Ajmer', 'Bhilwara', 'Alwar'],
  'Madhya Pradesh': ['Bhopal', 'Indore', 'Gwalior', 'Jabalpur', 'Ujjain', 'Sagar', 'Dewas', 'Satna'],
  'Bihar': ['Patna', 'Gaya', 'Bhagalpur', 'Muzaffarpur', 'Darbhanga', 'Purnia', 'Bihar Sharif', 'Sasaram'],
  'Punjab': ['Ludhiana', 'Amritsar', 'Jalandhar', 'Patiala', 'Bathinda', 'Mohali', 'Hoshiarpur', 'Pathankot'],
  'Haryana': ['Gurugram', 'Faridabad', 'Panchkula', 'Ambala', 'Karnal', 'Sonipat', 'Rohtak', 'Hisar'],
  'Delhi': ['New Delhi', 'Central Delhi', 'South Delhi', 'North Delhi', 'East Delhi', 'West Delhi', 'Shahdara', 'Dwarka'],
  'Telangana': ['Hyderabad', 'Warangal', 'Nizamabad', 'Karimnagar', 'Khammam', 'Ramagundam', 'Mahabubnagar', 'Nalgonda'],
  'Andhra Pradesh': ['Visakhapatnam', 'Vijayawada', 'Guntur', 'Nellore', 'Kurnool', 'Rajahmundry', 'Tirupati', 'Kakinada'],
  'Odisha': ['Bhubaneswar', 'Cuttack', 'Rourkela', 'Berhampur', 'Sambalpur', 'Puri', 'Balasore', 'Jharsuguda'],
  'Assam': ['Guwahati', 'Silchar', 'Dibrugarh', 'Jorhat', 'Nagaon', 'Tinsukia', 'Bongaigaon', 'Tezpur'],
  'Jharkhand': ['Ranchi', 'Jamshedpur', 'Dhanbad', 'Bokaro', 'Deoghar', 'Hazaribagh', 'Giridih', 'Ramgarh'],
  'Chhattisgarh': ['Raipur', 'Bhilai', 'Bilaspur', 'Korba', 'Raigarh', 'Durg', 'Rajnandgaon', 'Ambikapur'],
  'Uttarakhand': ['Dehradun', 'Haridwar', 'Rishikesh', 'Nainital', 'Haldwani', 'Roorkee', 'Rudrapur', 'Kashipur'],
};

const villagesCities = [
  'Shivaji Nagar', 'Gandhi Chowk', 'Main Bazaar', 'Sadar Bazaar', 'Industrial Area', 'Railway Colony',
  'Lake View', 'Garden Estate', 'River Side', 'Hill Top', 'Green Park', 'Sector 7', 'Model Town',
  'Civil Lines', 'Defence Colony', 'Kamla Nagar', 'Laxmi Nagar', 'Patel Nagar', 'Ashok Nagar', 'Indira Nagar',
  'Shastri Nagar', 'Guru Nanak Colony', 'Shakti Nagar', 'Saraswati Vihar', 'Shyam Nagar', 'Adarsh Nagar',
  'Janakpuri', 'Vikas Puri', 'Paschimi', 'Purab',
];

const courses = [
  'Mushroom Cultivation 101', 'Spawn Production Techniques', 'Commercial Oyster Farming',
  'Quality Control & Grading', 'Mushroom Disease Management', 'FSSAI Compliance Training',
  'Advanced Composting', 'Organic Certification', 'Supply Chain Management',
  'Export Readiness Program', 'Value Added Products', 'Packaging & Labeling',
  'Food Safety & Hygiene', 'Business Management', 'Digital Marketing for Agriculture',
  'Financial Literacy', 'Leadership Development', 'Communication Skills',
  'Sustainability Practices', 'Cold Chain Management',
];

const categories = [
  'Individual Learner', 'Corporate Trainee', 'Institutional', 'Franchise Trainee',
  'Government Sponsored', 'Scholarship',
];

const languages = [
  'Hindi', 'English', 'Marathi', 'Gujarati', 'Tamil', 'Telugu', 'Kannada', 'Malayalam',
  'Bengali', 'Punjabi', 'Odia', 'Assamese', 'Urdu', 'Nepali',
];

const statuses: StudentStatus[] = [
  'prospective', 'applied', 'pending-approval', 'approved', 'enrolled',
  'active', 'inactive', 'completed', 'certified', 'alumni', 'archived',
];

const learningModes: LearningMode[] = ['online', 'offline', 'hybrid'];

const tagPool = [
  'beginner', 'intermediate', 'advanced', 'certification-track', 'government-scheme',
  'scholarship-holder', 'rural', 'urban', 'self-sponsored', 'employer-sponsored',
  'part-time', 'full-time', 'weekend-batch', 'evening-batch', 'morning-batch',
  'hands-on-training', 'theory-focused', 'assessment-completed', 'project-submitted',
  'placement-ready', 'needs-assessment', 'remediation', 'honors', 'fast-track',
];

function randomItem<T>(arr: T[]): T {
  return arr[Math.floor(Math.random() * arr.length)];
}

function randomItems<T>(arr: T[], min: number, max: number): T[] {
  const count = Math.floor(Math.random() * (max - min + 1)) + min;
  const shuffled = [...arr].sort(() => Math.random() - 0.5);
  return shuffled.slice(0, count);
}

function randomDate(start: Date, end: Date): string {
  const d = new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()));
  return d.toISOString().split('T')[0];
}

function generatePhone(): string {
  const prefixes = ['98', '97', '96', '95', '94', '93', '92', '91', '90', '89', '88', '87', '86', '85', '84', '83', '82', '81', '80'];
  return `+91-${randomItem(prefixes)}${String(Math.floor(Math.random() * 100000000)).padStart(8, '0')}`;
}

function generateStudentId(index: number): string {
  return `STU-${String(2026 - Math.floor(index / 12))}-${String(index + 1).padStart(4, '0')}`;
}

function generateEnrollmentId(index: number, status: StudentStatus): string | null {
  if (['prospective', 'applied', 'pending-approval'].includes(status)) return null;
  return `ENR-${String(2026 - Math.floor(index / 12))}-${String(index + 1).padStart(4, '0')}`;
}

export function generateMockStudents(count: number = 50): Student[] {
  const students: Student[] = [];
  for (let i = 0; i < count; i++) {
    const firstName = firstNames[i % firstNames.length];
    const lastName = lastNames[i % lastNames.length];
    const fullName = `${firstName} ${lastName}`;
    const state = randomItem(states);
    const district = randomItem(districts[state] || ['Central']);
    const status = statuses[i % statuses.length];
    const langIdx = i % languages.length;
    const courseIdx = i % courses.length;

    students.push({
      id: `student-${i + 1}`,
      studentId: generateStudentId(i),
      enrollmentId: generateEnrollmentId(i, status),
      fullName,
      preferredName: firstName,
      profilePhotoUrl: null,
      email: `${firstName.toLowerCase()}.${lastName.toLowerCase()}${i + 1}@example.com`,
      phone: generatePhone(),
      gender: genders[i % genders.length],
      dateOfBirth: randomDate(new Date('1985-01-01'), new Date('2006-12-31')),
      nationality: randomItem(nationalities),
      state,
      district,
      villageCity: randomItem(villagesCities),
      registrationDate: randomDate(new Date('2024-01-01'), new Date('2026-07-16')),
      status,
      currentBatch: status === 'archived' ? null : `B-2026-${String((i % 12) + 1).padStart(2, '0')}`,
      course: status === 'archived' ? null : courses[courseIdx],
      learningMode: learningModes[Math.floor(i / 17) % learningModes.length],
      language: languages[langIdx],
      category: categories[Math.floor(i / 9) % categories.length],
      tags: randomItems(tagPool, 1, 4),
    });
  }
  return students;
}

export const MOCK_STUDENTS: Student[] = generateMockStudents(50);

export const MOCK_STUDENT_STATS: StudentDashboardStats = {
  totalStudents: MOCK_STUDENTS.length,
  activeStudents: MOCK_STUDENTS.filter((s) => s.status === 'active').length,
  pendingApprovals: MOCK_STUDENTS.filter((s) => s.status === 'pending-approval').length,
  newRegistrations: MOCK_STUDENTS.filter((s) => {
    const thirtyDaysAgo = new Date(Date.now() - 30 * 24 * 60 * 60 * 1000);
    return new Date(s.registrationDate) >= thirtyDaysAgo;
  }).length,
  inactiveStudents: MOCK_STUDENTS.filter((s) => s.status === 'inactive').length,
  archivedStudents: MOCK_STUDENTS.filter((s) => s.status === 'archived').length,
  studentsByCourse: courses.slice(0, 8).map((course) => ({
    course,
    count: MOCK_STUDENTS.filter((s) => s.course === course).length,
  })),
  studentsByLanguage: languages.slice(0, 8).map((language) => ({
    language,
    count: MOCK_STUDENTS.filter((s) => s.language === language).length,
  })),
  studentsByLearningMode: learningModes.map((mode) => ({
    mode,
    count: MOCK_STUDENTS.filter((s) => s.learningMode === mode).length,
  })),
  futureAlumniCount: MOCK_STUDENTS.filter((s) => s.status === 'completed' || s.status === 'certified').length,
  futureCertifiedCount: MOCK_STUDENTS.filter((s) => s.status === 'certified' || s.status === 'active').length,
};

export const MOCK_QUICK_ACTIONS: StudentQuickAction[] = [
  { id: 'add-student', label: 'Add Student', description: 'Register a new student', icon: 'user-plus' },
  { id: 'import', label: 'Import', description: 'Bulk import students', icon: 'upload' },
  { id: 'export', label: 'Export', description: 'Export student data', icon: 'download' },
  { id: 'assign-course', label: 'Assign Course', description: 'Assign students to courses', icon: 'book-open' },
  { id: 'assign-batch', label: 'Assign Batch', description: 'Assign students to batches', icon: 'calendar' },
  { id: 'generate-report', label: 'Generate Report', description: 'Create student reports', icon: 'bar-chart' },
  { id: 'send-notification', label: 'Send Notification', description: 'Notify students', icon: 'message-circle' },
  { id: 'merge-records', label: 'Merge Records', description: 'Merge duplicate student records', icon: 'repeat' },
];

export const MOCK_STATUS_COUNTS: Record<string, number> = {};
for (const s of statuses) {
  MOCK_STATUS_COUNTS[s] = MOCK_STUDENTS.filter((st) => st.status === s).length;
}
