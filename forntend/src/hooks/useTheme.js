/**
 * useTheme Hook
 * Custom hook for managing theme (light/dark mode)
 */

import { useState, useEffect, useCallback } from 'react';
import { THEME } from '../constants';
import APP_CONFIG from '../config';

/**
 * Custom hook for theme management
 * @param {Object} options - Theme options
 * @returns {Object} - Theme state and functions
 */
export function useTheme(options = {}) {
  const {
    defaultTheme = APP_CONFIG.SETTINGS.DEFAULT_THEME,
    storageKey = APP_CONFIG.SETTINGS.THEME_STORAGE_KEY,
  } = options;

  const [theme, setThemeState] = useState(() => {
    try {
      const stored = localStorage.getItem(storageKey);
      if (stored) {
        return stored;
      }
      
      // Check system preference
      if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
        return THEME.DARK;
      }
      
      return defaultTheme;
    } catch (error) {
      console.error('Error reading theme from localStorage:', error);
      return defaultTheme;
    }
  });

  const [isDark, setIsDark] = useState(() => theme === THEME.DARK);

  // Apply theme to document
  useEffect(() => {
    const root = document.documentElement;
    
    if (theme === THEME.DARK) {
      root.classList.add('dark');
      setIsDark(true);
    } else {
      root.classList.remove('dark');
      setIsDark(false);
    }
  }, [theme]);

  // Listen for system theme changes when using auto theme
  useEffect(() => {
    if (theme === THEME.AUTO) {
      const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
      
      const handleChange = (e) => {
        const newTheme = e.matches ? THEME.DARK : THEME.LIGHT;
        setThemeState(newTheme);
      };
      
      mediaQuery.addEventListener('change', handleChange);
      
      return () => {
        mediaQuery.removeEventListener('change', handleChange);
      };
    }
  }, [theme]);

  const setTheme = useCallback((newTheme) => {
    try {
      setThemeState(newTheme);
      localStorage.setItem(storageKey, newTheme);
    } catch (error) {
      console.error('Error saving theme to localStorage:', error);
    }
  }, [storageKey]);

  const toggleTheme = useCallback(() => {
    const newTheme = isDark ? THEME.LIGHT : THEME.DARK;
    setTheme(newTheme);
  }, [isDark, setTheme]);

  const resetTheme = useCallback(() => {
    setTheme(defaultTheme);
  }, [defaultTheme, setTheme]);

  return {
    theme,
    isDark,
    setTheme,
    toggleTheme,
    resetTheme,
  };
}

/**
 * useThemeColor Hook
 * Custom hook for getting theme-aware colors
 */
export function useThemeColor() {
  const { isDark } = useTheme();

  const getColor = useCallback((lightColor, darkColor) => {
    return isDark ? darkColor : lightColor;
  }, [isDark]);

  return {
    isDark,
    getColor,
  };
}

export default useTheme;