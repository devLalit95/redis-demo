import React from 'react';
import { cn } from '../../utils/cn';
import { Loader2 } from 'lucide-react';

/**
 * Loader Component - Loading spinner component
 * 
 * @param {Object} props - Component props
 * @param {string} props.size - Loader size (sm, md, lg, xl)
 * @param {string} props.variant - Loader variant (primary, secondary, white)
 * @param {string} props.className - Additional classes
 * @param {string} props.text - Optional loading text
 */
const Loader = ({
  size = 'md',
  variant = 'primary',
  className,
  text,
}) => {
  const sizeStyles = {
    sm: 'w-4 h-4',
    md: 'w-6 h-6',
    lg: 'w-8 h-8',
    xl: 'w-12 h-12',
  };
  
  const variantStyles = {
    primary: 'text-primary-600',
    secondary: 'text-secondary-600',
    white: 'text-white',
  };
  
  const textSizeStyles = {
    sm: 'text-xs',
    md: 'text-sm',
    lg: 'text-base',
    xl: 'text-lg',
  };
  
  return (
    <div className={cn('flex flex-col items-center justify-center gap-2', className)}>
      <Loader2 className={cn('animate-spin', sizeStyles[size], variantStyles[variant])} />
      {text && <p className={cn('text-gray-600 dark:text-gray-400', textSizeStyles[size])}>{text}</p>}
    </div>
  );
};

export default Loader;