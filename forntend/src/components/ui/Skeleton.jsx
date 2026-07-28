import React from 'react';
import { cn } from '../../utils/cn';

/**
 * Skeleton Component - Loading skeleton placeholder
 * 
 * @param {Object} props - Component props
 * @param {string} props.variant - Skeleton variant (text, circular, rectangular, custom)
 * @param {string} props.className - Additional classes
 * @param {Object} props.rest - Other div attributes
 */
const Skeleton = ({
  variant = 'text',
  className,
  ...rest
}) => {
  const variantStyles = {
    text: 'h-4 w-full',
    circular: 'rounded-full',
    rectangular: 'rounded-md',
    custom: '',
  };
  
  const baseStyles = 'animate-pulse bg-gray-200 dark:bg-gray-700';
  
  const classes = cn(
    baseStyles,
    variantStyles[variant],
    variant !== 'circular' && variant !== 'rectangular' && 'rounded',
    className
  );
  
  return <div className={classes} {...rest} />;
};

/**
 * SkeletonLoader Component - Complete skeleton loading state
 */
const SkeletonLoader = ({
  count = 3,
  className,
}) => {
  return (
    <div className={cn('space-y-3', className)}>
      {Array.from({ length: count }).map((_, i) => (
        <Skeleton key={i} variant="text" />
      ))}
    </div>
  );
};

export { Skeleton, SkeletonLoader };