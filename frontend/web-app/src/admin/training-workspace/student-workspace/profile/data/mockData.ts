import type {
  StudentProfile, ProfileCompleteness, TimelineEvent, DocumentMetadata,
  GuardianInfo, EmergencyContact, PersonalInfo, AcademicInfo, ProfessionalInfo,
  LearningPreferences, Address
} from '../types';

const indianStates = [
  'Maharashtra', 'Gujarat', 'Karnataka', 'Tamil Nadu', 'Uttar Pradesh',
  'West Bengal', 'Kerala', 'Rajasthan', 'Madhya Pradesh', 'Bihar',
  'Punjab', 'Haryana', 'Delhi', 'Telangana', 'Andhra Pradesh',
];

function generateAddress(index: number): Address {
  const villages = ['Shivaji Nagar', 'Gandhi Chowk', 'Main Bazaar', 'Lake View', 'Green Park', 'Model Town', 'Civil Lines', 'Defence Colony'];
  const districts = ['Pune', 'Mumbai', 'Bengaluru', 'Chennai', 'Lucknow', 'Kolkata', 'Jaipur', 'Hyderabad'];
  const state = indianStates[index % indianStates.length];
  return {
    village: villages[index % villages.length],
    taluk: `${districts[index % districts.length]} Taluk`,
    district: districts[index % districts.length],
    state,
    country: 'India',
    postalCode: `${String(100000 + index * 111).slice(0, 6)}`,
  };
}

function computeAge(dob: string): number {
  const birth = new Date(dob);
  const now = new Date('2026-07-16');
  let age = now.getFullYear() - birth.getFullYear();
  const m = now.getMonth() - birth.getMonth();
  if (m < 0 || (m === 0 && now.getDate() < birth.getDate())) age--;
  return age;
}

function computeCompleteness(profile: Partial<StudentProfile>): ProfileCompleteness {
  let personalScore = 0; const personalTotal = 8;
  if (profile.bloodGroup) personalScore++;
  if (profile.alternatePhone) personalScore++;
  if (profile.governmentId) personalScore++;
  if (profile.personalInfo?.currentAddress?.village) personalScore++;
  if (profile.personalInfo?.currentAddress?.postalCode) personalScore++;
  if (profile.personalInfo?.permanentAddress?.village) personalScore++;
  if (profile.personalInfo?.employer) personalScore++;
  if (profile.personalInfo?.maritalStatus && profile.personalInfo.maritalStatus !== 'not-specified') personalScore++;

  let academicScore = 0; const academicTotal = 7;
  if (profile.academicInfo?.highestQualification) academicScore++;
  if (profile.academicInfo?.specialization) academicScore++;
  if (profile.academicInfo?.institution) academicScore++;
  if (profile.academicInfo?.graduationYear) academicScore++;
  if (profile.academicInfo?.previousCourses?.length) academicScore++;
  if (profile.academicInfo?.learningInterests?.length) academicScore++;
  if (profile.academicInfo?.currentEducation) academicScore++;

  let profScore = 0; const profTotal = 6;
  if (profile.professionalInfo?.currentProfession) profScore++;
  if (profile.professionalInfo?.industry) profScore++;
  if (profile.professionalInfo?.skills?.length) profScore++;
  if (profile.professionalInfo?.certifications?.length) profScore++;
  if ((profile.professionalInfo?.yearsOfExperience ?? 0) > 0) profScore++;
  if (profile.professionalInfo?.professionType) profScore++;

  let learnScore = 0; const learnTotal = 6;
  if (profile.learningPreferences?.preferredLanguage) learnScore++;
  if (profile.learningPreferences?.secondaryLanguages?.length) learnScore++;
  if (profile.learningPreferences?.preferredMode) learnScore++;
  if (profile.learningPreferences?.timePreferences?.length) learnScore++;
  if (profile.learningPreferences?.learningGoals?.length) learnScore++;
  if (profile.learningPreferences?.preferredTrainingCategory) learnScore++;

  let guardianScore = 0; const guardianTotal = 4;
  if (profile.guardian?.name) guardianScore++;
  if (profile.guardian?.phone) guardianScore++;
  if (profile.guardian?.relationship) guardianScore++;
  if (profile.guardian?.email) guardianScore++;

  let emergencyScore = 0; const emergencyTotal = 4;
  if (profile.emergencyContact?.primaryName) emergencyScore++;
  if (profile.emergencyContact?.primaryPhone) emergencyScore++;
  if (profile.emergencyContact?.secondaryName) emergencyScore++;
  if (profile.emergencyContact?.secondaryPhone) emergencyScore++;

  let docScore = 0; const docTotal = 8;
  (profile.documents || []).forEach((d) => {
    if (d.status !== 'not-uploaded') docScore++;
  });

  const total = personalTotal + academicTotal + profTotal + learnTotal + guardianTotal + emergencyTotal + docTotal;
  const completed = personalScore + academicScore + profScore + learnScore + guardianScore + emergencyScore + docScore;
  const overall = total > 0 ? Math.round((completed / total) * 100) : 0;

  const suggestions: string[] = [];
  if (personalScore < personalTotal) suggestions.push('Complete your personal information');
  if (!profile.bloodGroup) suggestions.push('Add your blood group');
  if (!profile.governmentId) suggestions.push('Provide a government ID');
  if (academicScore < academicTotal) suggestions.push('Add your academic qualifications');
  if (!profile.academicInfo?.graduationYear) suggestions.push('Add your graduation year');
  if (profScore < profTotal) suggestions.push('Complete your professional profile');
  if (!profile.professionalInfo?.skills?.length) suggestions.push('Add your skills');
  if (!profile.professionalInfo?.certifications?.length) suggestions.push('Add any certifications');
  if (learnScore < learnTotal) suggestions.push('Set your learning preferences');
  if (!profile.learningPreferences?.learningGoals?.length) suggestions.push('Define your learning goals');
  if (guardianScore < guardianTotal) suggestions.push('Add guardian information');
  if (emergencyScore < emergencyTotal) suggestions.push('Add emergency contacts');
  if (docScore < docTotal) suggestions.push('Upload required documents');

  return {
    overall,
    sections: {
      personal: { completed: personalScore, total: personalTotal, percent: Math.round((personalScore / personalTotal) * 100) },
      academic: { completed: academicScore, total: academicTotal, percent: Math.round((academicScore / academicTotal) * 100) },
      professional: { completed: profScore, total: profTotal, percent: Math.round((profScore / profTotal) * 100) },
      learningPreferences: { completed: learnScore, total: learnTotal, percent: Math.round((learnScore / learnTotal) * 100) },
      guardian: { completed: guardianScore, total: guardianTotal, percent: Math.round((guardianScore / guardianTotal) * 100) },
      emergencyContact: { completed: emergencyScore, total: emergencyTotal, percent: Math.round((emergencyScore / emergencyTotal) * 100) },
      documents: { completed: docScore, total: docTotal, percent: Math.round((docScore / docTotal) * 100) },
    },
    suggestions: suggestions.slice(0, 5),
  };
}

function generateTimeline(studentId: string, regDate: string): TimelineEvent[] {
  const reg = new Date(regDate);
  const addDays = (d: Date, n: number) => {
    const r = new Date(d); r.setDate(r.getDate() + n); return r.toISOString().split('T')[0];
  };
  const addMonths = (d: Date, n: number) => {
    const r = new Date(d); r.setMonth(r.getMonth() + n); return r.toISOString().split('T')[0];
  };
  return [
    { id: `${studentId}-t1`, type: 'registration', label: 'Student Registered', description: 'Initial registration in the system', date: regDate, completed: true, icon: 'user-plus' },
    { id: `${studentId}-t2`, type: 'profile-created', label: 'Profile Created', description: 'Student profile was created', date: addDays(reg, 1), completed: true, icon: 'user' },
    { id: `${studentId}-t3`, type: 'enrollment', label: 'Course Enrollment', description: 'Enrolled in training program', date: addDays(reg, 5), completed: Math.random() > 0.3, icon: 'book-open' },
    { id: `${studentId}-t4`, type: 'attendance', label: 'First Attendance', description: 'Attended first training session', date: addDays(reg, 10), completed: Math.random() > 0.4, icon: 'check-circle' },
    { id: `${studentId}-t5`, type: 'assignment', label: 'First Assignment', description: 'Submitted first assignment', date: addDays(reg, 20), completed: Math.random() > 0.5, icon: 'file' },
    { id: `${studentId}-t6`, type: 'assessment', label: 'Assessment', description: 'Completed module assessment', date: addMonths(reg, 1), completed: Math.random() > 0.6, icon: 'target' },
    { id: `${studentId}-t7`, type: 'certificate', label: 'Certificate Issued', description: 'Course completion certificate issued', date: addMonths(reg, 3), completed: Math.random() > 0.7, icon: 'star' },
    { id: `${studentId}-t8`, type: 'placement', label: 'Placement Assistance', description: 'Placement support initiated', date: addMonths(reg, 4), completed: Math.random() > 0.8, icon: 'briefcase' },
  ];
}

function generateDocuments(studentId: string): DocumentMetadata[] {
  const statuses: DocumentMetadata['status'][] = ['not-uploaded', 'uploaded', 'verified'];
  return [
    { id: `${studentId}-doc1`, type: 'identity-proof', label: 'Aadhaar Card', status: statuses[Math.floor(Math.random() * 3)], uploadedAt: null, verifiedAt: null, notes: null },
    { id: `${studentId}-doc2`, type: 'address-proof', label: 'Address Proof', status: statuses[Math.floor(Math.random() * 3)], uploadedAt: null, verifiedAt: null, notes: null },
    { id: `${studentId}-doc3`, type: 'education-certificate', label: 'Highest Qualification Certificate', status: statuses[Math.floor(Math.random() * 2)], uploadedAt: null, verifiedAt: null, notes: null },
    { id: `${studentId}-doc4`, type: 'photograph', label: 'Passport Size Photo', status: statuses[Math.floor(Math.random() * 2)], uploadedAt: null, verifiedAt: null, notes: null },
    { id: `${studentId}-doc5`, type: 'government-scheme-document', label: 'Scheme Enrollment Proof', status: 'not-uploaded', uploadedAt: null, verifiedAt: null, notes: null },
  ];
}

const qualOptions = [
  '10th Standard', '12th Standard', 'Diploma', "Bachelor's Degree",
  "Master's Degree", 'PhD', 'Vocational Training', 'Certificate Course',
];

const specializationOptions = [
  'Agriculture Science', 'Microbiology', 'Food Technology', 'Business Management',
  'Computer Science', 'Environmental Science', 'Biotechnology', 'Commerce',
];

const institutionOptions = [
  'University of Agriculture', 'State Agricultural College', 'National Institute of Technology',
  'Regional Research Center', 'KVK Training Center', 'Government Polytechnic',
];

const professionOptions = [
  'Mushroom Farmer', 'Agricultural Entrepreneur', 'Food Processor', 'Farm Manager',
  'Quality Control Officer', 'Supply Chain Coordinator', 'Research Assistant', 'Small Business Owner',
];

const skillPool = [
  'Mushroom Cultivation', 'Spawn Production', 'Quality Control', 'Composting',
  'Pest Management', 'Food Safety', 'Packaging', 'Supply Chain',
  'Business Planning', 'Digital Marketing', 'Financial Management', 'Leadership',
];

const learningGoalOptions = [
  'Start a mushroom farm', 'Improve yield quality', 'Learn export procedures',
  'Get certified', 'Start value-added products', 'Build a business',
  'Train others', 'Learn organic farming',
];

const interestOptions = [
  'Oyster Mushroom', 'Button Mushroom', 'Shiitake', 'Spawn Production',
  'Value Addition', 'Organic Certification', 'Export Readiness', 'Composting',
];

function generateProfile(index: number): StudentProfile {
  const studentId = `STU-${String(2025 - Math.floor(index / 12))}-${String(index + 1).padStart(4, '0')}`;
  const firstNames = ['Aarav', 'Priya', 'Rohan', 'Ananya', 'Vikram', 'Sneha', 'Arjun', 'Kavya', 'Rahul', 'Isha'];
  const lastNames = ['Sharma', 'Patel', 'Kumar', 'Singh', 'Verma', 'Gupta', 'Reddy', 'Nair', 'Joshi', 'Deshmukh'];
  const fn = firstNames[index % firstNames.length];
  const ln = lastNames[index % lastNames.length];
  const fullName = `${fn} ${ln}`;
  const dob = `${1985 + (index % 20)}-${String((index % 12) + 1).padStart(2, '0')}-${String((index % 28) + 1).padStart(2, '0')}`;
  const regDate = `202${index % 4}-${String((index % 12) + 1).padStart(2, '0')}-${String((index % 28) + 1).padStart(2, '0')}`;

  const personalInfo: PersonalInfo = {
    bloodGroup: index % 3 === 0 ? null : (['A+', 'B+', 'O+', 'AB+', 'A-', 'O-'][index % 6]),
    maritalStatus: index % 4 === 0 ? 'not-specified' : (['single', 'married', 'single', 'married'][index % 4] as any),
    alternatePhone: index % 2 === 0 ? `+91-9${String(index * 12345).slice(0, 9)}` : null,
    governmentId: index % 3 === 0 ? null : `XXXX-XXXX-${String(1234 + index).slice(0, 4)}`,
    currentAddress: generateAddress(index),
    permanentAddress: generateAddress(index + 5),
    sameAsCurrent: index % 3 === 0,
    employer: index % 2 === 0 ? 'SporeKart Pvt Ltd' : 'Self-Employed',
    businessName: index % 3 === 0 ? `${fn}'s Mushroom Farm` : null,
    annualExperience: `${5 + (index % 20)} years`,
  };

  const academicInfo: AcademicInfo = {
    highestQualification: qualOptions[index % qualOptions.length],
    specialization: specializationOptions[index % specializationOptions.length],
    institution: institutionOptions[index % institutionOptions.length],
    graduationYear: 2010 + (index % 15),
    currentEducation: index % 2 === 0 ? null : 'Advanced Mushroom Cultivation',
    previousCourses: ['Mushroom Cultivation 101', ...(index % 2 === 0 ? [] : ['Spawn Production'])],
    learningInterests: interestOptions.slice(0, 2 + (index % 3)),
    academicNotes: index % 3 === 0 ? null : 'Actively pursuing advanced training',
  };

  const professionalInfo: ProfessionalInfo = {
    currentProfession: professionOptions[index % professionOptions.length],
    professionType: index % 5 === 0 ? 'student' : index % 4 === 0 ? 'farmer' : index % 3 === 0 ? 'entrepreneur' : 'employee',
    industry: index % 2 === 0 ? 'Agriculture' : 'Food Processing',
    yearsOfExperience: 1 + (index % 15),
    skills: skillPool.slice(0, 2 + (index % 4)),
    certifications: index % 2 === 0 ? ['Food Safety Certified'] : [],
    isBusinessOwner: index % 3 === 0,
    isFarmer: index % 4 === 0,
    isStudent: index % 5 === 0,
  };

  const learningPreferences: LearningPreferences = {
    preferredLanguage: ['Hindi', 'English', 'Marathi', 'Tamil', 'Kannada'][index % 5],
    secondaryLanguages: index % 2 === 0 ? ['English'] : ['Hindi', 'English'],
    preferredMode: (['online', 'offline', 'hybrid'] as const)[index % 3],
    timePreferences: [['morning', 'evening'], ['weekend'], ['morning', 'afternoon', 'evening']][index % 3] as any,
    learningGoals: learningGoalOptions.slice(0, 2 + (index % 3)),
    learningInterests: interestOptions.slice(0, 2 + (index % 2)),
    preferredTrainingCategory: ['Beginner', 'Intermediate', 'Advanced'][index % 3],
  };

  const guardian: GuardianInfo | null = index % 4 === 0 ? null : {
    name: `${['Rajesh', 'Sunita', 'Amit', 'Meena'][index % 4]} ${ln}`,
    relationship: (['father', 'mother', 'spouse', 'brother'] as const)[index % 4],
    phone: `+91-98${String(index * 1000000).slice(0, 8)}`,
    email: index % 2 === 0 ? `guardian.${ln.toLowerCase()}@example.com` : null,
    occupation: index % 3 === 0 ? null : 'Farmer',
    address: index % 2 === 0 ? generateAddress(index + 10).village : null,
  };

  const emergencyContact: EmergencyContact | null = {
    primaryName: `${['Rajesh', 'Sunita', 'Amit', 'Meena'][index % 4]} ${ln}`,
    primaryPhone: `+91-99${String(index * 2000000).slice(0, 8)}`,
    primaryRelationship: (['father', 'mother', 'spouse', 'brother'] as const)[index % 4],
    secondaryName: index % 3 === 0 ? null : `${['Neha', 'Ravi', 'Deepa', 'Suresh'][index % 4]} ${ln}`,
    secondaryPhone: index % 3 === 0 ? null : `+91-97${String(index * 3000000).slice(0, 8)}`,
    secondaryRelationship: index % 3 === 0 ? null : (['mother', 'brother', 'sister', 'friend'] as const)[index % 4],
    email: null,
  };

  const documents = generateDocuments(studentId);
  const timeline = generateTimeline(studentId, regDate);

  const profile: StudentProfile = {
    id: `profile-${index + 1}`,
    studentId,
    enrollmentNumber: `ENR-${String(2025 - Math.floor(index / 12))}-${String(index + 1).padStart(4, '0')}`,
    profilePhotoUrl: null,
    fullName,
    preferredName: fn,
    gender: (['male', 'female', 'male', 'female'] as const)[index % 4],
    dateOfBirth: dob,
    age: computeAge(dob),
    bloodGroup: personalInfo.bloodGroup,
    nationality: 'Indian',
    primaryLanguage: learningPreferences.preferredLanguage,
    secondaryLanguages: learningPreferences.secondaryLanguages,
    email: `${fn.toLowerCase()}.${ln.toLowerCase()}${index + 1}@example.com`,
    phone: `+91-98${String(index * 7654321).slice(0, 8)}`,
    alternatePhone: personalInfo.alternatePhone,
    governmentId: personalInfo.governmentId,
    profileStatus: (['active', 'active', 'active', 'incomplete', 'draft', 'pending-verification'] as const)[index % 6],
    registrationDate: regDate,
    lastUpdated: `2026-07-${String(10 + (index % 15)).padStart(2, '0')}`,
    personalInfo,
    academicInfo,
    professionalInfo,
    learningPreferences,
    guardian,
    emergencyContact,
    documents,
    timeline,
    completeness: { overall: 0, sections: {} as any, suggestions: [] },
  };

  profile.completeness = computeCompleteness(profile as any);
  return profile;
}

export function generateMockProfiles(count: number = 10): StudentProfile[] {
  return Array.from({ length: count }, (_, i) => generateProfile(i));
}

export const MOCK_PROFILES: StudentProfile[] = generateMockProfiles(10);

export function getProfileByStudentId(studentId: string): StudentProfile | undefined {
  return MOCK_PROFILES.find((p) => p.studentId === studentId);
}

export function getProfileById(id: string): StudentProfile | undefined {
  return MOCK_PROFILES.find((p) => p.id === id);
}
