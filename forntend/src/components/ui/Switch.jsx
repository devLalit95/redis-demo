import React from 'react';
import { cn } from '../../utils/cn';

/**
 * Switch Component - Toggle switch component
 * 
 * @param {Object} props - Component props
 * @param {boolean} props.checked - Switch state
 * @param {Function} props.onChange - Change handler
 * @param {boolean} props.disabled - Disable switch
 * @param {string} props.label - Switch label
 * @param {string} props.description - Switch description
 * @param {string} props.size - Switch size (sm, md, lg)
 * @param {string} props.className - Additional classes
 */
const Switch = React.forwardRef(({
  checked = false,
  onChange,
  disabled = false,
  label,
  description,
  size = 'md',
  className,
  ...rest
}, ref) => {
  const sizeStyles = {
    sm: {
      switch: 'w-8 h-4',
      thumb: 'w-3 h-3',
      translate: 'translate-x-4',
    },
    md: {
      switch: 'w-11 h-6',
      thumb: 'w-5 h-5',
      translate: 'translate-x-5',
    },
    lg: {
      switch: 'w-14 h-7',
      thumb: 'w-6 h-6',
      translate: 'translate-x-7',
    },
  };
  
  const styles = sizeStyles[size];
  
  return (
    <div className={cn('flex items-center', className)}>
      <button
        ref={ref}
        type="button"
        role="switch"
        aria-checked={checked}
        disabled={disabled}
        onClick={() => onChange(!checked)}
        className={cn(
          'relative inline-flex flex-shrink-0 border-2 border-transparent rounded-full cursor-pointer transition-colors duration-200 ease-in-out focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500',
          checked ? 'bg-primary-600' : 'bg-gray-200 dark:bg-gray-700',
          disabled && 'opacity-50 cursor-not-allowed',
          styles.switch
        )}
        {...rest}
      >
        <span
          aria-hidden="true"
          className={cn(
            'pointer-events-none inline-block rounded-full bg-white shadow transform ring-0 transition duration-200 ease-in-out',
            checked ? styles.translate : 'translate-x-0',
            styles.thumb
          )}
        />
      </button>
      {(label || description) && (
        <div className="ml-3">
          {label && (
            <span className="block text-sm font-medium text-gray-900 dark:text-gray-100">
              {label}
            </span>
          )}
          {description && (
            <span className="block text-sm text-gray-500 dark:text-gray-400">
              {description}
            </span>
          )}
        </div>
      )}
    </div>
  );
});

Switch.displayName = 'Switch';

export default Switch;