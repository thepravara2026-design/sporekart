export interface Lesson {
  id: string;
  title: string;
  duration: string;
  completed: boolean;
  videoUrl: string;
  transcript: string;
  notes: string;
  resources: string[];
}

export interface CourseModule {
  title: string;
  lessons: Lesson[];
}

export interface Course {
  id: string;
  title: string;
  description: string;
  category: 'Lab Work' | 'Cultivation' | 'Commercial';
  difficulty: 'Beginner' | 'Intermediate' | 'Advanced';
  duration: string;
  instructorName: string;
  instructorRole: string;
  language: string;
  rating: number;
  reviewsCount: number;
  enrolled: boolean;
  progress: number; // 0 to 100
  bannerGradient: string;
  outcomes: string[];
  requirements: string[];
  modules: CourseModule[];
}

export interface Certificate {
  id: string;
  title: string;
  courseTitle: string;
  completionDate: string;
  verificationCode: string;
}

export interface LiveSession {
  id: string;
  title: string;
  trainer: string;
  date: string;
  time: string;
  spotsRemaining: number;
  registered: boolean;
}

export const INITIAL_COURSES: Course[] = [
  {
    id: 'course-sterile-techniques',
    title: 'Sterile Lab Techniques & Agar Work',
    description: 'Learn how to construct a sterile working environment, prepare malt extract agar (MEA) plates, and perform cleanroom cultures inoculations.',
    category: 'Lab Work',
    difficulty: 'Intermediate',
    duration: '6 hours',
    instructorName: 'Dr. Anita Rao',
    instructorRole: 'Lead Mycology Research Scientist',
    language: 'English',
    rating: 4.9,
    reviewsCount: 154,
    enrolled: true,
    progress: 60,
    bannerGradient: 'linear-gradient(135deg, #0284c7 0%, #0369a1 100%)',
    outcomes: [
      'Understand the principles of sterile inoculation using Still Air Boxes and laminar flow hoods.',
      'Formulate MEA agar plates with correct nutrition ratios.',
      'Perform strain isolates selection and transfer agar wedges safely.',
      'Recognize and isolate green mold and other common contaminations.'
    ],
    requirements: [
      'Basic familiarity with mushroom biology.',
      'Access to simple lab gear (petri dishes, agar powder, scalpel).'
    ],
    modules: [
      {
        title: 'Module 1: Lab Space Prep & Sterilization',
        lessons: [
          {
            id: 'l1-1',
            title: 'Laying Out Your Workspace Layout',
            duration: '15 mins',
            completed: true,
            videoUrl: 'https://www.w3schools.com/html/mov_bbb.mp4',
            transcript: 'Welcome to laying out your sterile workspace. In this lesson, we will cover how to clean your counters, position your flow hood, and disinfect your inoculating tools before starting culture transfers.',
            notes: 'Keep surgical spirit (70% IPA) handy. Disinfect in one direction; do not wipe back and forth.',
            resources: ['Lab_Safety_Checklist.pdf', 'Lab_Setup_Guide.pdf']
          },
          {
            id: 'l1-2',
            title: 'Operating a Pressure Cooker for Agar',
            duration: '25 mins',
            completed: true,
            videoUrl: 'https://www.w3schools.com/html/movie.mp4',
            transcript: 'This lesson demonstrates autoclave and pressure cooker operation. We hold agar at 15 PSI for 45 minutes to sterilize media fully. Make sure container lids are slightly loose.',
            notes: 'Pressure cook at 15 PSI. Let it cool naturally to prevent agar boiling over.',
            resources: ['Sterilization_Temperature_Guide.pdf']
          }
        ]
      },
      {
        title: 'Module 2: Agar Formulations & Pouring',
        lessons: [
          {
            id: 'l2-1',
            title: 'MEA Formulation Recipes',
            duration: '20 mins',
            completed: true,
            videoUrl: 'https://www.w3schools.com/html/mov_bbb.mp4',
            transcript: 'Let us mix Malt Extract Agar (MEA). The standard ratio is 20g Agar agar, 20g Light Malt Extract, and 1 Liter of distilled water. Boil gently until dissolved before autoclaving.',
            notes: 'Standard MEA ratio: 20g Agar, 20g Malt Extract, 1L Water. Yeast is optional for growth boosts.',
            resources: ['Agar_Recipes_Sheet.xlsx']
          },
          {
            id: 'l2-2',
            title: 'Sterile Pouring Techniques',
            duration: '30 mins',
            completed: false,
            videoUrl: 'https://www.w3schools.com/html/movie.mp4',
            transcript: 'Pouring agar requires speed and clean technique. Pour when bottle is cool enough to touch comfortably (approx 50°C). Pour in front of a flow hood or inside a disinfected box.',
            notes: 'Pour at 50°C to minimize condensation. Stack plates to reduce droplets.',
            resources: ['Condensation_Management_Tip.pdf']
          }
        ]
      },
      {
        title: 'Module 3: Culture Inoculation & Isolation',
        lessons: [
          {
            id: 'l3-1',
            title: 'Agar-to-Agar Transfers',
            duration: '35 mins',
            completed: false,
            videoUrl: 'https://www.w3schools.com/html/mov_bbb.mp4',
            transcript: 'Today we perform wedge transfers. Flame sterilize your scalpel until glowing red, let it cool completely on the plate side, cut a small agar wedge, and transfer it directly to a fresh plate.',
            notes: 'Flame sterilize scalpel. Let it cool for 10s to avoid killing spores.',
            resources: ['Wedge_Transfer_Photos.pdf']
          }
        ]
      }
    ]
  },
  {
    id: 'course-monotub-growing',
    title: 'Commercial Monotub Cultivation',
    description: 'A complete commercial growers masterclass on cultivating gourmet cultivars (Oyster, Lion’s Mane) using monotubs.',
    category: 'Cultivation',
    difficulty: 'Beginner',
    duration: '4 hours',
    instructorName: 'Vijay Kumar',
    instructorRole: 'Master Grower & Founder, SporeFarms',
    language: 'Hindi / English',
    rating: 4.8,
    reviewsCount: 210,
    enrolled: true,
    progress: 100,
    bannerGradient: 'linear-gradient(135deg, #10b981 0%, #047857 100%)',
    outcomes: [
      'Design high-yielding monotubs with optimal airflow ports.',
      'Pasteurize straw, coco coir, and gypsum casing mediums.',
      'Control humidity and air exchanges to trigger pinning blocks.',
      'Harvest flushes cleanly to secure high yield rates.'
    ],
    requirements: [
      'No scientific gear needed.',
      'Basic space for growing tub boxes.'
    ],
    modules: [
      {
        title: 'Module 1: Monotub Construction & Drilling',
        lessons: [
          {
            id: 'l2-1-1',
            title: 'Drilling Port Holes Correctly',
            duration: '20 mins',
            completed: true,
            videoUrl: 'https://www.w3schools.com/html/mov_bbb.mp4',
            transcript: 'Drill 2-inch holes. Place two holes low on the long sides and two holes high on the short sides to trigger natural convection gas circulation.',
            notes: 'Convection ventilation is critical: high holes release warm gas; low holes pull fresh air.',
            resources: ['Monotub_Dimensions.pdf']
          }
        ]
      },
      {
        title: 'Module 2: Substrates & Inoculation',
        lessons: [
          {
            id: 'l2-2-1',
            title: 'Coco Coir Bucket Pasteurization',
            duration: '30 mins',
            completed: true,
            videoUrl: 'https://www.w3schools.com/html/movie.mp4',
            transcript: 'Pour boiling water over coco coir and gypsum inside an insulated bucket. Close lid and let it stand overnight to pasteurize completely before spawning.',
            notes: 'Field capacity moisture check: squeeze substrate; only 2-3 drops of water should release.',
            resources: ['Bucket_Tek_Calculations.pdf']
          }
        ]
      }
    ]
  },
  {
    id: 'course-spawn-production',
    title: 'Spawn Production & Inoculation',
    description: 'Scale up your production by preparing grain spawn bags from scratch using rye, wheat, or millet grains.',
    category: 'Commercial',
    difficulty: 'Advanced',
    duration: '5 hours',
    instructorName: 'Dr. Anita Rao',
    instructorRole: 'Lead Mycology Research Scientist',
    language: 'English',
    rating: 4.7,
    reviewsCount: 89,
    enrolled: false,
    progress: 0,
    bannerGradient: 'linear-gradient(135deg, #f59e0b 0%, #b45309 100%)',
    outcomes: [
      'Prepare grain jars and filter patch bags.',
      'Understand grain hydration and boiling limits.',
      'Perform liquid culture to grain spawn inoculations.',
      'Break and shake methods to accelerate growth.'
    ],
    requirements: [
      'Autoclave or large pressure sterilizer.',
      'Liquid culture syringes.'
    ],
    modules: [
      {
        title: 'Module 1: Grain Prep & Boiling',
        lessons: [
          {
            id: 'l3-1-1',
            title: 'Hydrating Grains Without Bursting',
            duration: '40 mins',
            completed: false,
            videoUrl: 'https://www.w3schools.com/html/mov_bbb.mp4',
            transcript: 'Boil rye grains for 15-20 minutes. The grains must absorb water but the outer hulls must not burst. Dry completely on towels before bagging.',
            notes: 'Burst grain hulls release starch, which significantly increases contamination risks.',
            resources: ['Grain_Hydration_Chart.pdf']
          }
        ]
      }
    ]
  }
];

export const MOCK_CERTIFICATES: Certificate[] = [
  {
    id: 'cert-monotub',
    title: 'Certified Gourmet Cultivator',
    courseTitle: 'Commercial Monotub Cultivation',
    completionDate: '2026-07-05',
    verificationCode: 'SK-CERT-MONO-88912',
  }
];

export const LIVE_SESSIONS: LiveSession[] = [
  {
    id: 'webinar-1',
    title: 'Live Lab Q&A: Troubleshooting Contamination',
    trainer: 'Dr. Anita Rao',
    date: '2026-07-18',
    time: '4:00 PM IST',
    spotsRemaining: 12,
    registered: false,
  },
  {
    id: 'webinar-2',
    title: 'Commercial Scale-Up: Bags vs. Bottle Cultivation',
    trainer: 'Vijay Kumar',
    date: '2026-07-22',
    time: '11:00 AM IST',
    spotsRemaining: 8,
    registered: true,
  }
];
