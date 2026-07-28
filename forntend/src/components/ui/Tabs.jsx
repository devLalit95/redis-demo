import React, { useState } from 'react';
import { cn } from '../../utils/cn';

/**
 * Tabs Component - Reusable tabs component
 * 
 * @param {Object} props - Component props
 * @param {Array} props.tabs - Array of tab objects {id, label, icon, content}
 * @param {string} props.defaultTab - Default active tab id
 * @param {Function} props.onChange - Callback when tab changes
 * @param {string} props.variant - Tab variant (default, pills, underline)
 * @param {string} props.className - Additional classes
 */
const Tabs = ({
  tabs = [],
  defaultTab,
  onChange,
  variant = 'default',
  className,
}) => {
  const [activeTab, setActiveTab] = useState(defaultTab || tabs[0]?.id);
  
  const handleTabChange = (tabId) => {
    setActiveTab(tabId);
    onChange?.(tabId);
  };
  
  const variantStyles = {
    default: {
      tabList: 'border-b border-gray-200 dark:border-gray-700',
      tab: 'px-4 py-2 text-sm font-medium border-b-2 border-transparent',
      activeTab: 'border-primary-500 text-primary-600 dark:text-primary-400',
      inactiveTab: 'text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200',
    },
    pills: {
      tabList: 'bg-gray-100 dark:bg-gray-800 p-1 rounded-lg',
      tab: 'px-4 py-2 text-sm font-medium rounded-md transition-all',
      activeTab: 'bg-white dark:bg-gray-700 text-gray-900 dark:text-gray-100 shadow',
      inactiveTab: 'text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200',
    },
    underline: {
      tabList: 'border-b border-gray-200 dark:border-gray-700',
      tab: 'px-4 py-2 text-sm font-medium border-b-2 border-transparent transition-all',
      activeTab: 'border-primary-500 text-primary-600 dark:text-primary-400',
      inactiveTab: 'text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200',
    },
  };
  
  const styles = variantStyles[variant];
  
  return (
    <div className={cn('w-full', className)}>
      <div className={cn('flex', styles.tabList)}>
        {tabs.map((tab) => (
          <button
            key={tab.id}
            onClick={() => handleTabChange(tab.id)}
            className={cn(
              'flex items-center gap-2 transition-colors',
              styles.tab,
              activeTab === tab.id ? styles.activeTab : styles.inactiveTab
            )}
          >
            {tab.icon && <span>{tab.icon}</span>}
            {tab.label}
          </button>
        ))}
      </div>
      <div className="mt-4">
        {tabs.find((tab) => tab.id === activeTab)?.content}
      </div>
    </div>
  );
};

export default Tabs;