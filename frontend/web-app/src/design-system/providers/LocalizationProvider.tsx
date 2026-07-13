import React, { createContext, useContext, useState, useCallback, useEffect } from 'react';

export type Locale = 'en' | 'hi' | 'mr' | 'ta' | 'te' | 'bn' | 'gu' | 'kn' | 'ml' | 'pa' | 'or' | 'as';

const LocalizationContext = createContext<any>(undefined);

const TRANSLATIONS: Record<string, Record<string, string>> = {
  en: {
    'common.save': 'Save',
    'common.cancel': 'Cancel',
    'common.delete': 'Delete',
    'common.edit': 'Edit',
    'common.create': 'Create',
    'common.update': 'Update',
    'common.search': 'Search',
    'common.filter': 'Filter',
    'common.sort': 'Sort',
    'common.export': 'Export',
    'common.import': 'Import',
    'common.loading': 'Loading...',
    'common.error': 'Error',
    'common.success': 'Success',
    'common.warning': 'Warning',
    'common.info': 'Information',
    'common.confirm': 'Confirm',
    'common.yes': 'Yes',
    'common.no': 'No',
    'common.ok': 'OK',
    'common.close': 'Close',
    'common.retry': 'Retry',
    'common.skip': 'Skip',
    'common.previous': 'Previous',
    'common.done': 'Done',
    'common.submit': 'Submit',
    'common.reset': 'Reset',
    'common.clear': 'Clear',
    'common.apply': 'Apply',
    'common.continue': 'Continue',
    'common.back': 'Back',
    'common.finish': 'Finish',
  },
  hi: {
    'common.save': 'सेव करें',
    'common.cancel': 'रद्द करें',
    'common.delete': 'हटाएं',
    'common.edit': 'संपादित करें',
    'common.create': 'बनाएं',
    'common.update': 'अपडेट करें',
    'common.search': 'खोजें',
    'common.filter': 'फ़िल्टर',
    'common.sort': 'क्रमबद्ध करें',
    'common.export': 'निर्यात करें',
    'common.import': 'आयात करें',
    'common.loading': 'लोड हो रहा है...',
    'common.error': 'त्रुटि',
    'common.success': 'सफलता',
    'common.warning': 'चेतावनी',
    'common.info': 'जानकारी',
    'common.confirm': 'पुष्टि करें',
    'common.yes': 'हाँ',
    'common.no': 'नहीं',
    'common.ok': 'ठीक है',
    'common.close': 'बंद करें',
    'common.retry': 'पुनः प्रयास करें',
    'common.skip': 'छोड़ें',
    'common.previous': 'पिछला',
    'common.done': 'पूर्ण',
    'common.submit': 'जमा करें',
    'common.reset': 'रीसेट',
    'common.clear': 'साफ़ करें',
    'common.apply': 'लागू करें',
    'common.continue': 'जारी रखें',
    'common.back': 'वापस',
    'common.finish': 'समाप्त',
  },
  mr: {
    'common.save': 'जतन करा',
    'common.cancel': 'रद्द करा',
    'common.delete': 'हटवा',
    'common.edit': 'संपादित करा',
    'common.create': 'तयार करा',
    'common.update': 'अपडेट करा',
    'common.search': 'शोधा',
    'common.filter': 'फिल्टर',
    'common.sort': 'क्रमवार लावा',
    'common.export': 'निर्यात करा',
    'common.import': 'आयात करा',
    'common.loading': 'लोड होत आहे...',
    'common.error': 'त्रुटी',
    'common.success': 'यश',
    'common.warning': 'चेतावणी',
    'common.info': 'माहिती',
    'common.confirm': 'पुष्टी करा',
    'common.yes': 'होय',
    'common.no': 'नाही',
    'common.ok': 'ठीक आहे',
    'common.close': 'बंद करा',
    'common.retry': 'पुन्हा प्रयत्न करा',
    'common.skip': 'बायपास करा',
    'common.previous': 'मागील',
    'common.done': 'झाले',
    'common.submit': 'सादर करा',
    'common.reset': 'रीसेट करा',
    'common.clear': 'स्वच्छ करा',
    'common.apply': 'लागू करा',
    'common.continue': 'सुरू ठेवा',
    'common.back': 'मागे जा',
    'common.finish': 'समाप्त',
  },
};

const SUPPORTED_LOCALES = [
  { code: 'en', name: 'English', dir: 'ltr' as const },
  { code: 'hi', name: 'हिन्दी', dir: 'ltr' as const },
  { code: 'mr', name: 'मराठी', dir: 'ltr' as const },
  { code: 'ta', name: 'தமிழ்', dir: 'ltr' as const },
  { code: 'te', name: 'తెలుగు', dir: 'ltr' as const },
  { code: 'bn', name: 'বাংলা', dir: 'ltr' as const },
  { code: 'gu', name: 'ગુજરાતી', dir: 'ltr' as const },
  { code: 'kn', name: 'ಕನ್ನಡ', dir: 'ltr' as const },
  { code: 'ml', name: 'മലയാളം', dir: 'ltr' as const },
  { code: 'pa', name: 'ਪੰਜਾਬੀ', dir: 'ltr' as const },
  { code: 'or', name: 'ଓଡ଼ିଆ', dir: 'ltr' as const },
  { code: 'as', name: 'অসমীয়া', dir: 'ltr' as const },
];

export function LocalizationProvider({ children }: { children: React.ReactNode }) {
  const [locale, setLocale] = useState('en');

  useEffect(() => {
    const stored = localStorage.getItem('sporekart-locale');
    if (stored) setLocale(stored);
  }, []);

  const setLocaleWithStorage = useCallback((locale: string) => {
    setLocale(locale);
    localStorage.setItem('sporekart-locale', locale);
    document.documentElement.lang = locale;
  }, []);

  const t = useCallback((key: string, params?: Record<string, string | number>) => {
    let translation = TRANSLATIONS[locale as keyof typeof TRANSLATIONS]?.[key] || TRANSLATIONS.en[key] || key;
    if (params) {
      Object.entries(params).forEach(([key, value]) => {
        translation = translation.replace(new RegExp(`\\{${key}\\}`, 'g'), String(value));
      });
    }
    return translation;
  }, [locale]);

  const formatNumber = useCallback((num: number, options?: Intl.NumberFormatOptions) => {
    return new Intl.NumberFormat(locale, options).format(num);
  }, [locale]);

  const formatDate = useCallback((date: Date, options?: Intl.DateTimeFormatOptions) => {
    return new Intl.DateTimeFormat(locale, options).format(date);
  }, [locale]);

  const formatCurrency = useCallback((amount: number, currency = 'INR') => {
    return new Intl.NumberFormat(locale, { style: 'currency', currency }).format(amount);
  }, [locale]);

  const value = {
    locale,
    setLocale: setLocaleWithStorage,
    t,
    dir: 'ltr',
    formatNumber,
    formatDate,
    formatCurrency,
    supportedLocales: SUPPORTED_LOCALES.map((l) => l.code),
  };

  return (
    <LocalizationContext.Provider value={value}>
      {children}
    </LocalizationContext.Provider>
  );
}

export function useLocalization() {
  const context = useContext(LocalizationContext);
  if (!context) {
    throw new Error('useLocalization must be used within a LocalizationProvider');
  }
  return context;
}
