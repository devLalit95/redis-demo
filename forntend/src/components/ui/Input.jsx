import React from 'react';
import { cn } from '../../utils/cn';

/**
 * Input Component - Reusable input component with mobile-first design
 * 
 * @param {Object} props - Component props
 * @param {string} props.label - Input label
 * @param {boolean} props.required - Show required asterisk
 * @param {string} props.helper - Helper text
 * @param {string} props.error - Error message
 * @param {boolean} props.disabled - Disable input
 * @param {boolean} props.compact - Compact sizing for mobile
 * @param {string} props.className - Additional classes
 * @param {Object} props.rest - Other input attributes
 */
const Input = React.forwardRef(({
  label,
  required = false,
  helper,
  error,
  disabled = false,
  compact = false,
  className,
  ...rest
}, ref) => {
  const inputClasses = cn(
    'form-input',
    error && 'error',
    compact && 'form-input-compact',
    className
  );
  
  return (
    <div className={cn('form-group', compact && 'form-group-compact')}>
      {label && (
        <label className={cn('form-label', required && 'required', compact && 'form-label-compact')}>
          {label}
        </label>
      )}
      <input
        ref={ref}
        className={inputClasses}
        disabled={disabled}
        {...rest}
      />
      {error && <p className={cn('form-error', compact && 'form-error-compact')}>{error}</p>}
      {helper && !error && <p className={cn('form-helper', compact && 'form-helper-compact')}>{helper}</p>}
    </div>
  );
});

Input.displayName = 'Input';

export default Input;