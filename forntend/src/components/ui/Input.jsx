import React from 'react';
import { cn } from '../../utils/cn';

/**
 * Input Component - Reusable input component
 * 
 * @param {Object} props - Component props
 * @param {string} props.label - Input label
 * @param {boolean} props.required - Show required asterisk
 * @param {string} props.helper - Helper text
 * @param {string} props.error - Error message
 * @param {boolean} props.disabled - Disable input
 * @param {string} props.className - Additional classes
 * @param {Object} props.rest - Other input attributes
 */
const Input = React.forwardRef(({
  label,
  required = false,
  helper,
  error,
  disabled = false,
  className,
  ...rest
}, ref) => {
  const inputClasses = cn(
    'form-input',
    error && 'error',
    className
  );
  
  return (
    <div className="form-group">
      {label && (
        <label className={cn('form-label', required && 'required')}>
          {label}
        </label>
      )}
      <input
        ref={ref}
        className={inputClasses}
        disabled={disabled}
        {...rest}
      />
      {error && <p className="form-error">{error}</p>}
      {helper && !error && <p className="form-helper">{helper}</p>}
    </div>
  );
});

Input.displayName = 'Input';

export default Input;