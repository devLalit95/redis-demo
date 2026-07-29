import React from 'react';
import { NavLink } from 'react-router-dom';
import { cn } from '../../utils/cn';
import {
  LayoutDashboard,
  Database,
  HardDrive,
  Activity,
  BarChart3,
  Settings,
  HelpCircle,
  Layers,
  Gauge,
  Monitor,
  FileText,
  Hash,
  Plus,
  List as ListIcon,
  Layers as SetIcon,
  Users,
  Zap,
  History,
  TestTube,
} from 'lucide-react';

/**
 * Sidebar Component - Main navigation sidebar
 * 
 * @param {Object} props - Component props
 * @param {boolean} props.isOpen - Sidebar open state
 * @param {boolean} props.collapsed - Sidebar collapsed state
 * @param {Function} props.onClose - Sidebar close handler
 */
const Sidebar = ({ isOpen, collapsed, onClose }) => {
  const navItems = [
    {
      category: 'Main',
      items: [
        { name: 'Dashboard', href: '/', icon: LayoutDashboard },
        { name: 'Student CRUD', href: '/student-crud', icon: Users },
        { name: 'Redis Explorer', href: '/redis-explorer', icon: Database },
        { name: 'String Operations', href: '/string-operations', icon: FileText },
        { name: 'Hash Operations', href: '/hash-operations', icon: Hash },
        { name: 'List Operations', href: '/list-operations', icon: ListIcon },
        { name: 'Set Operations', href: '/set-operations', icon: SetIcon },
        { name: 'Counter Operations', href: '/counter-operations', icon: Plus },
        { name: 'Cache Playground', href: '/cache-playground', icon: Layers },
      ],
    },
    {
      category: 'Performance',
      items: [
        { name: 'Performance Comparison', href: '/performance-comparison', icon: Zap },
        { name: 'Request History', href: '/request-history', icon: History },
        { name: 'Load Testing', href: '/load-testing', icon: TestTube },
        { name: 'Redis Monitor', href: '/redis-monitor', icon: Monitor },
      ],
    },
    {
      category: 'Settings',
      items: [
        { name: 'Configuration', href: '/configuration', icon: Settings },
        { name: 'Help', href: '/help', icon: HelpCircle },
      ],
    },
  ];
  
  const NavItem = ({ item }) => {
    const Icon = item.icon;
    
    return (
      <NavLink
        to={item.href}
        onClick={() => onClose()}
        className={({ isActive }) =>
          cn(
            'flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors',
            isActive
              ? 'bg-primary-50 text-primary-600 dark:bg-primary-900/20 dark:text-primary-400'
              : 'text-gray-700 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-800'
          )
        }
      >
        <Icon className="w-5 h-5 flex-shrink-0" />
        {!collapsed && <span>{item.name}</span>}
      </NavLink>
    );
  };
  
  return (
    <aside
      className={cn(
        'sidebar',
        collapsed && 'sidebar-collapsed',
        isOpen && 'fixed inset-y-0 left-0 z-40 lg:relative lg:inset-auto',
        !isOpen && '-translate-x-full lg:translate-x-0'
      )}
    >
      {/* Sidebar Header */}
      <div className="sidebar-header">
        {!collapsed && (
          <div>
            <h2 className="text-lg font-bold text-gray-900 dark:text-gray-100">
              Redis Demo
            </h2>
            <p className="text-xs text-gray-500 dark:text-gray-400 mt-1">
              Learning Platform
            </p>
          </div>
        )}
        {collapsed && (
          <div className="w-8 h-8 bg-primary-500 rounded-lg flex items-center justify-center">
            <Database className="w-5 h-5 text-white" />
          </div>
        )}
      </div>
      
      {/* Sidebar Content */}
      <div className="sidebar-content">
        {navItems.map((section) => (
          <div key={section.category} className="mb-6">
            {!collapsed && (
              <h3 className="px-3 mb-2 text-xs font-semibold text-gray-500 uppercase tracking-wider">
                {section.category}
              </h3>
            )}
            <div className="space-y-1">
              {section.items.map((item) => (
                <NavItem key={item.name} item={item} />
              ))}
            </div>
          </div>
        ))}
      </div>
      
      {/* Sidebar Footer */}
      <div className="sidebar-footer">
        {!collapsed && (
          <div className="flex items-center gap-3 px-3 py-2">
            <div className="w-8 h-8 bg-gradient-to-br from-primary-500 to-secondary-500 rounded-full flex items-center justify-center">
              <span className="text-white text-sm font-medium">R</span>
            </div>
            <div className="flex-1 min-w-0">
              <p className="text-sm font-medium text-gray-900 dark:text-gray-100 truncate">
                Redis User
              </p>
              <p className="text-xs text-gray-500 dark:text-gray-400 truncate">
                user@redis.demo
              </p>
            </div>
          </div>
        )}
        {collapsed && (
          <div className="flex justify-center">
            <div className="w-8 h-8 bg-gradient-to-br from-primary-500 to-secondary-500 rounded-full flex items-center justify-center">
              <span className="text-white text-sm font-medium">R</span>
            </div>
          </div>
        )}
      </div>
    </aside>
  );
};

export default Sidebar;