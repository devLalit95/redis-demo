import React, { useState } from 'react';
import { Menu, X, Moon, Sun, Bell, Settings, User } from 'lucide-react';
import { cn } from '../../utils/cn';
import Button from '../ui/Button';

/**
 * Navbar Component - Main navigation bar
 * 
 * @param {Object} props - Component props
 * @param {Function} props.onSidebarToggle - Sidebar toggle handler
 * @param {boolean} props.sidebarOpen - Sidebar open state
 * @param {boolean} props.darkMode - Dark mode state
 * @param {Function} props.onThemeToggle - Theme toggle handler
 */
const Navbar = ({
  onSidebarToggle,
  sidebarOpen,
  darkMode,
  onThemeToggle,
}) => {
  const [notificationsOpen, setNotificationsOpen] = useState(false);
  const [userMenuOpen, setUserMenuOpen] = useState(false);
  
  return (
    <nav className="navbar">
      <div className="navbar-left">
        <Button
          variant="ghost"
          icon
          onClick={onSidebarToggle}
          className="lg:hidden"
        >
          {sidebarOpen ? <X /> : <Menu />}
        </Button>
        
        <a href="/" className="navbar-brand">
          Redis Demo
        </a>
      </div>
      
      <div className="navbar-right">
        {/* Theme Toggle */}
        <Button
          variant="ghost"
          icon
          onClick={onThemeToggle}
          title={darkMode ? 'Switch to light mode' : 'Switch to dark mode'}
        >
          {darkMode ? <Sun /> : <Moon />}
        </Button>
        
        {/* Notifications */}
        <div className="relative">
          <Button
            variant="ghost"
            icon
            onClick={() => setNotificationsOpen(!notificationsOpen)}
            title="Notifications"
          >
            <Bell />
          </Button>
          
          {notificationsOpen && (
            <div className="absolute right-0 mt-2 w-80 bg-white dark:bg-gray-800 rounded-lg shadow-lg border border-gray-200 dark:border-gray-700 z-50">
              <div className="p-4 border-b border-gray-200 dark:border-gray-700">
                <h3 className="font-semibold text-gray-900 dark:text-gray-100">Notifications</h3>
              </div>
              <div className="p-4 text-center text-gray-500 dark:text-gray-400">
                No new notifications
              </div>
            </div>
          )}
        </div>
        
        {/* User Menu */}
        <div className="relative">
          <Button
            variant="ghost"
            icon
            onClick={() => setUserMenuOpen(!userMenuOpen)}
            title="User menu"
          >
            <User />
          </Button>
          
          {userMenuOpen && (
            <div className="absolute right-0 mt-2 w-48 bg-white dark:bg-gray-800 rounded-lg shadow-lg border border-gray-200 dark:border-gray-700 z-50">
              <div className="p-2">
                <a
                  href="/settings"
                  className="flex items-center gap-2 px-3 py-2 text-sm text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 rounded-md"
                >
                  <Settings className="w-4 h-4" />
                  Settings
                </a>
              </div>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;