import React from 'react';
import { cn } from '../../utils/cn';

/**
 * Badge Component - Reusable badge component for status indicators
 * 
 * @param {Object} props - Component props
 * @param {string} props.variant - Badge variant (primary, secondary, success, danger, warning, info)
 * @param {string} props.size - Badge size (sm, md, lg)
 * @param {boolean} props.dot - Show dot indicator
 * @param {React.ReactNode} props.children - Badge content
 * @param {string} props.className - Additional classes
 */
const Badge = ({
  variant = 'primary',
  size = 'md',
  dot = false,
  children,
  className,
}) => {
  const baseStyles = 'inline-flex items-center font-medium rounded-full';
  
  const variantStyles = {
    primary: 'bg-primary-100 text-primary-800 dark:bg-primary-900 dark:text-primary-200',
    secondary: 'bg-secondary-100 text-secondary-800 dark:bg-secondary-900 dark:text-secondary-200',
    success: 'bg-success-100 text-success-800 dark:bg-success-900 dark:text-success-200',
    danger: 'bg-danger-100 text-danger-800 dark:bg-danger-900 dark:text-danger-200',
    warning: 'bg-warning-100 text-warning-800 dark:bg-warning-900 dark:text-warning-200',
    info: 'bg-gray-100 text-gray-800 dark:bg-gray-700 dark:text-gray-200',
  };
  
  const sizeStyles = {
    sm: 'px-2 py-0.5 text-xs',
    md: 'px-2.5 py-1 text-sm',
    lg: 'px-3 py-1.5 text-base',
  };
  
  const dotStyles = {
    primary: 'bg-primary-600',
    secondary: 'bg-secondary-600',
    success: 'bg-success-600',
    danger: 'bg-danger-600',
    warning: 'bg-warning-600',
    info: 'bg-gray-600',
  };
  
  const classes = cn(
    baseStyles,
    variantStyles[variant],
    sizeStyles[size],
    className
  );
  
  return (
    <span className={classes}>
      {dot && (
        <span className={cn('w-1.5 h-1.5 rounded-full mr-1.5', dotStyles[variant])} />
      )}
      {children}
    </span>
  );
};

export default Badge;