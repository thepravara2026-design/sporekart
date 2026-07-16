import type {
  StudentCareerProfile, JobOpportunity, PlacementDrive, CompanyPartnership,
  CareerCounselingSession, InterviewPreparation, SkillGapAssessment,
  AlumniProfile, AlumniMentorship, AlumniContribution, AlumniEvent,
  AlumniAnalytics, PlacementDashboard,
  PlacementStatus, CareerDevelopmentStage,
  CompanyPartnershipTier, AlumniEngagementLevel, JobOpportunityType,
  PlacementDriveStage, AlumniContributionType, AlumniEventType, SkillCategory,
  StudentSkill,
} from '../types';

const STU = [
  { id: 'stu-1', name: 'Aarav Sharma' }, { id: 'stu-2', name: 'Priya Patel' }, { id: 'stu-3', name: 'Rahul Singh' },
  { id: 'stu-4', name: 'Ananya Gupta' }, { id: 'stu-5', name: 'Vikram Joshi' }, { id: 'stu-6', name: 'Neha Kapoor' },
  { id: 'stu-7', name: 'Arjun Mehta' }, { id: 'stu-8', name: 'Kavita Reddy' }, { id: 'stu-9', name: 'Rohan Desai' },
  { id: 'stu-10', name: 'Ishita Verma' }, { id: 'stu-11', name: 'Amit Kumar' }, { id: 'stu-12', name: 'Sneha Agarwal' },
  { id: 'stu-13', name: 'Deepak Tiwari' }, { id: 'stu-14', name: 'Pooja Nair' }, { id: 'stu-15', name: 'Karan Malhotra' },
  { id: 'stu-16', name: 'Divya Bhat' }, { id: 'stu-17', name: 'Suresh Iyer' }, { id: 'stu-18', name: 'Meera Choudhury' },
  { id: 'stu-19', name: 'Nitin Saxena' }, { id: 'stu-20', name: 'Lakshmi Rajan' },
  { id: 'stu-21', name: 'Rajesh Kumar' }, { id: 'stu-22', name: 'Anjali Sinha' }, { id: 'stu-23', name: 'Vivek Mishra' },
  { id: 'stu-24', name: 'Pallavi Rao' }, { id: 'stu-25', name: 'Aditya Khanna' },
];

const COURSES = [
  { id: 'course-1', name: 'Full Stack Web Development' },
  { id: 'course-2', name: 'Data Science & Analytics' },
  { id: 'course-3', name: 'Cloud Architecture' },
  { id: 'course-4', name: 'Mobile App Development' },
  { id: 'course-5', name: 'DevOps Engineering' },
];

const BATCHES = [
  { id: 'batch-1', name: 'Batch A' }, { id: 'batch-2', name: 'Batch B' },
  { id: 'batch-3', name: 'Batch C' }, { id: 'batch-4', name: 'Batch D' },
];

const COMPANIES = [
  { id: 'comp-1', name: 'Google', logo: '/logos/google.png', industry: 'Technology' },
  { id: 'comp-2', name: 'Microsoft', logo: '/logos/microsoft.png', industry: 'Technology' },
  { id: 'comp-3', name: 'Amazon', logo: '/logos/amazon.png', industry: 'E-Commerce' },
  { id: 'comp-4', name: 'Infosys', logo: '/logos/infosys.png', industry: 'IT Services' },
  { id: 'comp-5', name: 'TCS', logo: '/logos/tcs.png', industry: 'IT Services' },
  { id: 'comp-6', name: 'Flipkart', logo: '/logos/flipkart.png', industry: 'E-Commerce' },
  { id: 'comp-7', name: 'Wipro', logo: '/logos/wipro.png', industry: 'IT Services' },
  { id: 'comp-8', name: 'IBM', logo: '/logos/ibm.png', industry: 'Technology' },
  { id: 'comp-9', name: 'Deloitte', logo: '/logos/deloitte.png', industry: 'Consulting' },
  { id: 'comp-10', name: 'Goldman Sachs', logo: '/logos/gs.png', industry: 'Finance' },
];

const PHOTOS = ['/photos/student-1.jpg', '/photos/student-2.jpg', '/photos/student-3.jpg', '/photos/student-4.jpg', '/photos/student-5.jpg'];

const TIERS: CompanyPartnershipTier[] = ['platinum', 'gold', 'silver', 'bronze', 'strategic', 'academic', 'government'];
const ENG_LEVELS: AlumniEngagementLevel[] = ['active', 'moderate', 'low', 'disengaged', 'lifetime', 'ambassador', 'mentor', 'donor'];
const JOB_TYPES: JobOpportunityType[] = ['full-time', 'part-time', 'internship', 'contract', 'freelance', 'apprenticeship', 'fellowship', 'government-job'];
const DRIVE_STAGES: PlacementDriveStage[] = ['announced', 'registrations-open', 'registrations-closed', 'scheduled', 'in-progress', 'results-pending', 'completed', 'cancelled'];
const PLACEMENT_STATUSES: PlacementStatus[] = ['placement-ready', 'resume-shortlisted', 'interview-scheduled', 'interview-completed', 'selected', 'offer-received', 'offer-accepted', 'joined', 'rejected', 'not-placed', 'withdrawn', 'deregistered'];
const CAREER_STAGES: CareerDevelopmentStage[] = ['exploration', 'skill-building', 'resume-preparation', 'interview-preparation', 'job-search', 'placement', 'post-placement', 'alumni', 'mentorship', 'lifelong-learning'];
const CONTRIB_TYPES: AlumniContributionType[] = ['monetary-donation', 'equipment-donation', 'scholarship-fund', 'mentorship-hours', 'guest-lecture', 'industry-project', 'curriculum-advisory', 'placement-support', 'event-participation', 'media-testimonial'];
const EVENT_TYPES: AlumniEventType[] = ['networking', 'workshop', 'webinar', 'reunion', 'mentorship-session', 'industry-visit', 'hackathon', 'career-fair', 'cultural', 'sports', 'annual-meet', 'chapter-meet'];
const SKILL_CATS: SkillCategory[] = ['technical', 'domain', 'soft-skill', 'leadership', 'language', 'certification', 'tool', 'methodology'];

function pick<T>(arr: T[]): T { return arr[Math.floor(Math.random() * arr.length)]; }

function randomDate(daysBack: number): string {
  const d = new Date(); d.setDate(d.getDate() - Math.floor(Math.random() * daysBack));
  return d.toISOString().split('T')[0];
}

function futureDate(daysForward: number): string {
  const d = new Date(); d.setDate(d.getDate() + Math.floor(Math.random() * daysForward));
  return d.toISOString().split('T')[0];
}

function genSkills(): StudentSkill[] {
  const count = Math.floor(Math.random() * 4) + 2;
  return Array.from({ length: count }, () => ({
    category: pick(SKILL_CATS),
    name: pick(['React', 'Python', 'Java', 'AWS', 'Docker', 'SQL', 'Node.js', 'TypeScript', 'Angular', 'Kubernetes']),
    proficiency: pick(['beginner', 'intermediate', 'advanced', 'expert'] as const),
  }));
}

function genStudentProfiles(count: number): StudentCareerProfile[] {
  return Array.from({ length: count }, (_, i) => {
    const stu = STU[i % STU.length];
    const course = pick(COURSES);
    const batch = pick(BATCHES);
    const status = pick(PLACEMENT_STATUSES);
    return {
      id: `sp-${i + 1}`, studentId: stu.id, studentName: stu.name, studentPhoto: pick(PHOTOS),
      email: `${stu.name.toLowerCase().replace(' ', '.')}@example.com`,
      phone: `+91-98765${String(43210 + i).slice(0, 5)}`,
      dateOfBirth: randomDate(8000),
      courseId: course.id, courseName: course.name, batchId: batch.id, batchName: batch.name,
      overallScore: Math.floor(Math.random() * 40) + 60,
      technicalScore: Math.floor(Math.random() * 40) + 60,
      domainScore: Math.floor(Math.random() * 40) + 60,
      softSkillScore: Math.floor(Math.random() * 40) + 60,
      resumeUrl: '', portfolioUrl: '', linkedInUrl: '', githubUrl: '',
      careerObjective: `To build a career in ${course.name} with a focus on innovation and impact.`,
      targetRole: pick(['Software Engineer', 'Data Analyst', 'Cloud Architect', 'DevOps Engineer', 'Full Stack Developer']),
      targetIndustry: pick(['Technology', 'Finance', 'Healthcare', 'E-Commerce', 'Consulting']),
      preferredLocation: pick(['Bangalore', 'Hyderabad', 'Pune', 'Mumbai', 'Chennai', 'Delhi NCR']),
      totalExperience: '', noticePeriod: '', currentCtc: '', expectedCtc: '',
      skills: genSkills(), placementStatus: status, careerStage: pick(CAREER_STAGES),
      interviewCount: Math.floor(Math.random() * 5), offerCount: Math.floor(Math.random() * 3),
      placementDate: status === 'joined' ? randomDate(180) : null,
      createdDate: randomDate(365), lastUpdated: randomDate(30),
    };
  });
}

function genJobOpportunities(): JobOpportunity[] {
  const titles = [
    { title: 'Software Engineer', desc: 'Build scalable software solutions and microservices.' },
    { title: 'Data Scientist', desc: 'Analyze complex datasets and build ML models.' },
    { title: 'Cloud Architect', desc: 'Design and implement cloud infrastructure.' },
    { title: 'Frontend Developer', desc: 'Develop responsive web applications.' },
    { title: 'DevOps Engineer', desc: 'Manage CI/CD pipelines and cloud deployments.' },
    { title: 'Backend Developer', desc: 'Design APIs and backend services.' },
    { title: 'Full Stack Developer', desc: 'Build end-to-end web applications.' },
    { title: 'Product Manager', desc: 'Drive product strategy and execution.' },
  ];
  return Array.from({ length: 20 }, (_, i) => {
    const company = pick(COMPANIES);
    const job = pick(titles);
    return {
      id: `job-${i + 1}`, jobId: `JOB-${String(i + 1).padStart(4, '0')}`,
      jobType: pick(JOB_TYPES), companyId: company.id, companyName: company.name,
      companyLogo: company.logo, title: job.title, description: job.desc,
      requiredSkills: ['React', 'Node.js', 'Python', 'SQL'].slice(0, Math.floor(Math.random() * 3) + 1),
      preferredSkills: ['AWS', 'Docker', 'Kubernetes', 'TypeScript'].slice(0, Math.floor(Math.random() * 3) + 1),
      location: pick(['Bangalore', 'Hyderabad', 'Pune', 'Mumbai', 'Remote']),
      salaryRange: `₹${[6, 8, 10, 12, 15, 18, 20, 25][Math.floor(Math.random() * 8)]}-${[10, 12, 15, 18, 20, 25, 30, 40][Math.floor(Math.random() * 8)]} LPA`,
      experienceRequired: `${[0, 0, 1, 2, 3][Math.floor(Math.random() * 5)]}-${[2, 3, 4, 5, 6][Math.floor(Math.random() * 5)]} years`,
      totalPositions: Math.floor(Math.random() * 10) + 1,
      filledPositions: Math.floor(Math.random() * 5),
      postedDate: randomDate(60), applicationDeadline: futureDate(30),
      startDate: futureDate(60), status: pick(['active', 'active', 'active', 'closed', 'on-hold'] as const),
      placementDriveId: Math.random() > 0.5 ? `drive-${Math.floor(Math.random() * 5) + 1}` : null,
      partnerTier: pick(TIERS),
      eligibilityCriteria: { courseIds: [pick(COURSES).id], minScore: Math.floor(Math.random() * 20) + 60, minAttendance: Math.floor(Math.random() * 20) + 75 },
    };
  });
}

function genPlacementDrives(): PlacementDrive[] {
  const companies = COMPANIES.slice(0, 5);
  return companies.map((c, i) => {
    const stage = DRIVE_STAGES[i % DRIVE_STAGES.length];
    return {
      id: `drive-${i + 1}`, driveId: `DRIVE-${String(i + 1).padStart(3, '0')}`,
      name: `${c.name} Campus Drive ${2026 - i}`,
      description: `Annual campus recruitment drive for ${c.name}. Multiple roles across engineering teams.`,
      stage, companyId: c.id, companyName: c.name, companyLogo: c.logo,
      jobOpportunities: [],
      registrationsOpen: stage === 'announced' ? futureDate(5) : randomDate(30),
      registrationsClose: futureDate(15),
      driveDate: futureDate(30), venue: 'University Campus Auditorium',
      mode: pick(['on-campus', 'off-campus', 'virtual'] as const),
      registeredStudents: Math.floor(Math.random() * 200) + 50,
      shortlistedStudents: Math.floor(Math.random() * 50) + 10,
      selectedStudents: Math.floor(Math.random() * 15) + 1,
      totalRounds: Math.floor(Math.random() * 3) + 2, currentRound: Math.floor(Math.random() * 3) + 1,
      createdDate: randomDate(90),
    };
  });
}

function genCompanyPartnerships(): CompanyPartnership[] {
  return COMPANIES.map((c, i) => ({
    id: `cp-${i + 1}`, companyId: c.id, companyName: c.name,
    companyLogo: c.logo, website: `https://${c.name.toLowerCase()}.com`,
    industry: c.industry, tier: TIERS[i % TIERS.length],
    partnershipDate: randomDate(730), lastActivityDate: randomDate(30),
    totalHires: Math.floor(Math.random() * 50) + 5,
    totalInternshipsOffered: Math.floor(Math.random() * 30) + 3,
    totalPlacementDrives: Math.floor(Math.random() * 10) + 1,
    contactPerson: `${pick(['Raj', 'Anita', 'Suresh', 'Priya', 'Vikram'])} ${pick(['Kumar', 'Singh', 'Patel', 'Sharma', 'Verma'])}`,
    contactEmail: `hr@${c.name.toLowerCase()}.com`, contactPhone: '+91-9876543210',
    status: pick(['active', 'active', 'active', 'inactive'] as const),
    notes: `${c.name} is a ${TIERS[i % TIERS.length]} tier partner with strong track record.`,
  }));
}

function genCounselingSessions(): CareerCounselingSession[] {
  return Array.from({ length: 15 }, (_, i) => {
    const stu = pick(STU);
    return {
      id: `cs-${i + 1}`, sessionId: `CS-${String(i + 1).padStart(3, '0')}`,
      studentId: stu.id, studentName: stu.name,
      counselorName: pick(['Dr. Mehta', 'Prof. Sharma', 'Ms. Patel', 'Mr. Kumar', 'Dr. Rao']),
      sessionDate: randomDate(60),
      sessionType: pick(['one-on-one', 'group', 'workshop', 'webinar'] as const),
      topic: pick(['Career Planning', 'Resume Building', 'Interview Skills', 'Industry Trends', 'Higher Education', 'Skill Gap Analysis', 'Job Search Strategy']),
      notes: '', actionItems: '',
      status: pick(['scheduled', 'completed', 'completed', 'cancelled', 'no-show'] as const),
      feedback: '', rating: Math.floor(Math.random() * 3) + 3,
      createdDate: randomDate(90),
    };
  });
}

function genInterviewPreparations(): InterviewPreparation[] {
  return Array.from({ length: 20 }, (_, i) => {
    const stu = pick(STU);
    const company = pick(COMPANIES);
    return {
      id: `ip-${i + 1}`, prepId: `IP-${String(i + 1).padStart(3, '0')}`,
      studentId: stu.id, studentName: stu.name,
      companyName: company.name,
      jobTitle: pick(['Software Engineer', 'Data Analyst', 'Cloud Engineer', 'Frontend Developer']),
      roundNumber: Math.floor(Math.random() * 3) + 1,
      roundType: pick(['aptitude', 'technical', 'hr', 'managerial', 'group-discussion', 'presentation'] as const),
      scheduledDate: randomDate(30),
      status: pick(['upcoming', 'completed', 'completed', 'cancelled', 'rescheduled'] as const),
      preparationNotes: '', feedback: '', result: pick(['cleared', 'failed', 'awaiting', null] as const),
      createdDate: randomDate(60),
    };
  });
}

function genSkillGapAssessments(): SkillGapAssessment[] {
  const skills = ['React', 'Python', 'AWS', 'Docker', 'SQL', 'Node.js', 'TypeScript', 'Kubernetes', 'Java', 'Angular'];
  return Array.from({ length: 30 }, (_, i) => {
    const stu = pick(STU);
    const skill = pick(skills);
    const current = Math.floor(Math.random() * 60) + 20;
    const required = Math.floor(Math.random() * 30) + 60;
    return {
      id: `sg-${i + 1}`, assessmentId: `SG-${String(i + 1).padStart(3, '0')}`,
      studentId: stu.id, studentName: stu.name,
      category: pick(SKILL_CATS), skillName: skill,
      currentLevel: current, requiredLevel: required,
      gap: Math.max(0, required - current),
      priority: required - current > 40 ? 'critical' : required - current > 25 ? 'high' : required - current > 10 ? 'medium' : 'low',
      suggestedResources: '', targetDate: Math.random() > 0.4 ? futureDate(60) : null,
      status: pick(['identified', 'in-progress', 'addressed'] as const),
    };
  });
}

function genAlumniProfiles(): AlumniProfile[] {
  return Array.from({ length: 20 }, (_, i) => {
    const stu = STU[i % STU.length];
    const course = pick(COURSES);
    const batch = pick(BATCHES);
    const company = pick(COMPANIES);
    return {
      id: `alum-${i + 1}`, alumniId: `AL-${String(i + 1).padStart(4, '0')}`,
      studentId: stu.id, fullName: stu.name, photo: pick(PHOTOS),
      email: `${stu.name.toLowerCase().replace(' ', '.')}@alumni.com`, phone: '+91-9876543210',
      batchId: batch.id, batchName: batch.name,
      courseId: course.id, courseName: course.name,
      graduationYear: 2026 - Math.floor(Math.random() * 5),
      completionDate: randomDate(365),
      currentCompany: company.name, currentPosition: pick(['Software Engineer', 'Senior Developer', 'Tech Lead', 'Data Scientist', 'Product Manager']),
      industry: company.industry, location: pick(['Bangalore', 'Hyderabad', 'Pune', 'Mumbai', 'San Francisco', 'London']),
      linkedInUrl: '', engagementLevel: pick(ENG_LEVELS),
      mentorshipStatus: pick(['available', 'active', 'unavailable'] as const),
      totalMentorshipHours: Math.floor(Math.random() * 100),
      contributionsCount: Math.floor(Math.random() * 10),
      isAmbassador: Math.random() > 0.8, isDonor: Math.random() > 0.85,
      bio: '', achievements: '', createdDate: randomDate(365), lastActiveDate: randomDate(30),
    };
  });
}

function genMentorships(): AlumniMentorship[] {
  return Array.from({ length: 10 }, (_, i) => {
    const mentor = pick(alumniList);
    const mentee = pick(STU);
    return {
      id: `ment-${i + 1}`, mentorshipId: `MENT-${String(i + 1).padStart(3, '0')}`,
      mentorAlumniId: mentor.alumniId, mentorName: mentor.fullName,
      menteeStudentId: mentee.id, menteeStudentName: mentee.name,
      startDate: randomDate(180), endDate: Math.random() > 0.6 ? randomDate(30) : null,
      focusArea: pick(['career-guidance', 'technical', 'domain', 'interview-prep', 'entrepreneurship', 'leadership'] as const),
      status: pick(['active', 'completed', 'paused'] as const),
      sessionsCompleted: Math.floor(Math.random() * 12) + 1,
      totalHours: Math.floor(Math.random() * 40) + 5,
      feedback: '', rating: Math.floor(Math.random() * 2) + 4,
    };
  });
}

function genContributions(): AlumniContribution[] {
  return Array.from({ length: 15 }, (_, i) => {
    const alumni = pick(alumniList);
    return {
      id: `contrib-${i + 1}`, contributionId: `CONT-${String(i + 1).padStart(3, '0')}`,
      alumniId: alumni.alumniId, alumniName: alumni.fullName,
      type: pick(CONTRIB_TYPES),
      description: `${alumni.fullName} contributed through ${pick(CONTRIB_TYPES).replace(/-/g, ' ')}.`,
      date: randomDate(365), value: `₹${Math.floor(Math.random() * 100000) + 5000}`,
      associatedEventId: Math.random() > 0.7 ? `evt-${Math.floor(Math.random() * 8) + 1}` : null,
      associatedProgram: Math.random() > 0.5 ? pick(['Mentorship Program', 'Guest Lecture Series', 'Career Guidance']) : null,
      status: pick(['acknowledged', 'pending-acknowledgment', 'featured'] as const),
      createdDate: randomDate(365),
    };
  });
}

function genEvents(): AlumniEvent[] {
  return Array.from({ length: 8 }, (_, i) => ({
    id: `evt-${i + 1}`, eventId: `EVT-${String(i + 1).padStart(3, '0')}`,
    eventType: EVENT_TYPES[i % EVENT_TYPES.length],
    title: `${pick(['Annual', 'Spring', 'Summer', 'Winter', 'Tech', 'Cultural'])} ${EVENT_TYPES[i % EVENT_TYPES.length].replace(/-/g, ' ').replace(/\b\w/g, (c) => c.toUpperCase())}`,
    description: `Join us for an exciting alumni ${EVENT_TYPES[i % EVENT_TYPES.length].replace(/-/g, ' ')} event.`,
    date: futureDate(60), time: '10:00 AM',
    venue: pick(['University Auditorium', 'Conference Hall', 'Virtual Zoom', 'Hotel Grand']),
    mode: pick(['online', 'offline', 'hybrid'] as const),
    organizer: pick(['Alumni Association', 'Career Services', 'Student Council']),
    maxAttendees: Math.floor(Math.random() * 200) + 50,
    registeredCount: Math.floor(Math.random() * 100) + 10,
    attendedCount: Math.floor(Math.random() * 80) + 5,
    registrationDeadline: futureDate(30), fee: Math.random() > 0.5 ? 'Free' : `₹${Math.floor(Math.random() * 500) + 100}`,
    status: pick(['announced', 'open', 'open', 'closed', 'in-progress', 'completed'] as const),
    createdBy: 'Alumni Association', createdDate: randomDate(90),
  }));
}

function generateAnalytics(
  profiles: StudentCareerProfile[], alumniList: AlumniProfile[],
  mentorships: AlumniMentorship[], contributions: AlumniContribution[], events: AlumniEvent[],
): AlumniAnalytics {
  const placed = profiles.filter((p) => ['selected', 'offer-received', 'offer-accepted', 'joined'].includes(p.placementStatus));
  const activeAlumni = alumniList.filter((a) => a.engagementLevel === 'active' || a.engagementLevel === 'ambassador' || a.engagementLevel === 'mentor');
  const engagedAlumni = alumniList.filter((a) => a.engagementLevel !== 'disengaged' && a.engagementLevel !== 'low');
  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'];
  return {
    totalAlumni: alumniList.length, activeAlumni: activeAlumni.length, engagedAlumni: engagedAlumni.length,
    totalMentors: alumniList.filter((a) => a.mentorshipStatus !== 'unavailable').length,
    activeMentorships: mentorships.filter((m) => m.status === 'active').length,
    completedMentorships: mentorships.filter((m) => m.status === 'completed').length,
    totalContributions: contributions.length, totalDonations: `₹${Math.floor(Math.random() * 5000000) + 100000}`,
    totalEvents: events.length,
    totalEventRegistrations: events.reduce((s, e) => s + e.registeredCount, 0),
    placementRate: profiles.length ? Math.round((placed.length / profiles.length) * 100) : 0,
    averageSalary: '₹12.5 LPA',
    topRecruiters: COMPANIES.slice(0, 5).map((c) => ({ companyName: c.name, hires: Math.floor(Math.random() * 10) + 1 })),
    alumniByBatch: BATCHES.map((b) => ({ batchName: b.name, count: Math.floor(Math.random() * 8) + 2 })),
    alumniByIndustry: [...new Set(alumniList.map((a) => a.industry))].map((ind) => ({ industry: ind, count: Math.floor(Math.random() * 6) + 1 })),
    alumniByLocation: [...new Set(alumniList.map((a) => a.location))].slice(0, 5).map((loc) => ({ location: loc, count: Math.floor(Math.random() * 5) + 1 })),
    engagementTrends: months.map((m) => ({ month: m, activeCount: Math.floor(Math.random() * 15) + 5, contributionCount: Math.floor(Math.random() * 5) + 1 })),
    placementTrends: months.map((m) => ({ month: m, placedCount: Math.floor(Math.random() * 8) + 1 })),
  };
}

function generateDashboard(profiles: StudentCareerProfile[], companies: CompanyPartnership[], drives: PlacementDrive[]): PlacementDashboard {
  const placementReady = profiles.filter((p) => p.placementStatus === 'placement-ready').length;
  const shortlisted = profiles.filter((p) => p.placementStatus === 'resume-shortlisted' || p.placementStatus === 'interview-scheduled').length;
  const placed = profiles.filter((p) => ['selected', 'offer-received', 'offer-accepted', 'joined'].includes(p.placementStatus)).length;
  const notPlaced = profiles.filter((p) => p.placementStatus === 'not-placed').length;
  return {
    totalStudents: profiles.length, placementReady, shortlisted, placed, notPlaced,
    activeDrives: drives.filter((d) => d.stage !== 'completed' && d.stage !== 'cancelled').length,
    activeJobOpenings: 20, partnerCompanies: companies.length,
    placementPercentage: profiles.length ? Math.round((placed / profiles.length) * 100) : 0,
    averagePackage: '₹8.5 LPA', highestPackage: '₹25 LPA',
    upcomingDrives: drives.filter((d) => ['announced', 'registrations-open', 'scheduled'].includes(d.stage)).slice(0, 3),
    recentPlacements: profiles.filter((p) => ['joined', 'offer-accepted'].includes(p.placementStatus)).slice(0, 5),
    topRecruiters: companies.filter((c) => c.status === 'active').slice(0, 5),
  };
}

const studentProfiles = genStudentProfiles(25);
const jobOpportunities = genJobOpportunities();
const placementDrives = genPlacementDrives();
const companies = genCompanyPartnerships();
const counselingSessions = genCounselingSessions();
const interviewPrep = genInterviewPreparations();
const skillGaps = genSkillGapAssessments();
const alumniList = genAlumniProfiles();
const mentorships = genMentorships();
const contributions = genContributions();
const events = genEvents();
const analytics = generateAnalytics(studentProfiles, alumniList, mentorships, contributions, events);
const dashboard = generateDashboard(studentProfiles, companies, placementDrives);

export function getStudentCareerProfiles(): StudentCareerProfile[] { return studentProfiles; }
export function getJobOpportunities(): JobOpportunity[] { return jobOpportunities; }
export function getPlacementDrives(): PlacementDrive[] { return placementDrives; }
export function getCompanyPartnerships(): CompanyPartnership[] { return companies; }
export function getCounselingSessions(): CareerCounselingSession[] { return counselingSessions; }
export function getInterviewPreparations(): InterviewPreparation[] { return interviewPrep; }
export function getSkillGapAssessments(): SkillGapAssessment[] { return skillGaps; }
export function getAlumniProfiles(): AlumniProfile[] { return alumniList; }
export function getAlumniMentorships(): AlumniMentorship[] { return mentorships; }
export function getAlumniContributions(): AlumniContribution[] { return contributions; }
export function getAlumniEvents(): AlumniEvent[] { return events; }
export function getAlumniAnalytics(): AlumniAnalytics { return analytics; }
export function getPlacementDashboard(): PlacementDashboard { return dashboard; }
