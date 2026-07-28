import React from 'react';
import { cn } from '../../utils/cn';

/**
 * Divider Component - Visual divider/separator
 * 
 * @param {Object} props - Component props
 * @param {boolean} props.vertical - Vertical divider
 * @param {string} props.variant - Divider variant (solid, dashed, dotted)
 * @param {string} props.label - Optional label text
 * @param {string} props.className - Additional classes
 */
const Divider = ({
  vertical = false,
  variant = 'solid',
  label,
  className,
}) => {
  const variantStyles = {
    solid: 'border-solid',
    dashed: 'border-dashed',
    dotted: 'border-dotted',
  };
  
  if (label) {
    return (
      <div className={cn('relative', className)}>
        <div className="absolute inset-0 flex items-center">
          <div className={cn('w-full border-t border-gray-200 dark:border-gray-700', variantStyles[variant])} />
        </div>
        <div className="relative flex justify-center">
          <span className="bg-white dark:bg-gray-900 px-2 text-sm text-gray-500 dark:text-gray-400">
            {label}
          </span>
        </div>
      </div>
    );
  }
  
  if (vertical) {
    return (
      <div
        className={cn(
          'h-full border-l border-gray-200 dark:border-gray-700',
          variantStyles[variant],
          className
        )}
      />
    );
  }
  
  return (
    <div
      className={cn(
        'w-full border-t border-gray-200 dark:border-gray-700',
        variantStyles[variant],
        className
      )}
    />
  );
};

export default Divider;