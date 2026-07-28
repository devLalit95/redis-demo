import React from 'react';
import { cn } from '../../utils/cn';

/**
 * Progress Component - Progress bar component
 * 
 * @param {Object} props - Component props
 * @param {number} props.value - Progress value (0-100)
 * @param {string} props.variant - Progress variant (primary, secondary, success, danger, warning)
 * @param {string} props.size - Progress size (sm, md, lg)
 * @param {boolean} props.striped - Show striped animation
 * @param {boolean} props.animated - Animate stripes
 * @param {boolean} props.showLabel - Show percentage label
 * @param {string} props.className - Additional classes
 */
const Progress = ({
  value = 0,
  variant = 'primary',
  size = 'md',
  striped = false,
  animated = false,
  showLabel = false,
  className,
}) => {
  const sizeStyles = {
    sm: 'h-1',
    md: 'h-2',
    lg: 'h-4',
  };
  
  const variantStyles = {
    primary: 'bg-primary-500',
    secondary: 'bg-secondary-500',
    success: 'bg-success-500',
    danger: 'bg-danger-500',
    warning: 'bg-warning-500',
  };
  
  const stripedStyles = striped
    ? 'bg-[length:1rem_1rem] bg-[linear-gradient(45deg,rgba(255,255,255,.15)_25%,transparent_25%,transparent_50%,rgba(255,255,255,.15)_50%,rgba(255,255,255,.15)_75%,transparent_75%,transparent)]'
    : '';
  
  const animatedStyles = animated ? 'animate-[stripe_1s_linear_infinite]' : '';
  
  const percentage = Math.min(100, Math.max(0, value));
  
  return (
    <div className={cn('w-full', className)}>
      <div className={cn('w-full bg-gray-200 dark:bg-gray-700 rounded-full overflow-hidden', sizeStyles[size])}>
        <div
          className={cn(
            'h-full transition-all duration-300 ease-out',
            variantStyles[variant],
            stripedStyles,
            animatedStyles
          )}
          style={{ width: `${percentage}%` }}
        />
      </div>
      {showLabel && (
        <p className="mt-1 text-sm text-gray-600 dark:text-gray-400">
          {percentage}%
        </p>
      )}
    </div>
  );
};

export default Progress;