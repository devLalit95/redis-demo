import React from 'react';
import { cn } from '../../utils/cn';

/**
 * Select Component - Reusable select component
 * 
 * @param {Object} props - Component props
 * @param {string} props.label - Select label
 * @param {boolean} props.required - Show required asterisk
 * @param {string} props.helper - Helper text
 * @param {string} props.error - Error message
 * @param {boolean} props.disabled - Disable select
 * @param {Array} props.options - Array of options {value, label}
 * @param {string} props.placeholder - Placeholder text
 * @param {string} props.className - Additional classes
 * @param {Object} props.rest - Other select attributes
 */
const Select = React.forwardRef(({
  label,
  required = false,
  helper,
  error,
  disabled = false,
  options = [],
  placeholder = 'Select an option',
  className,
  ...rest
}, ref) => {
  const selectClasses = cn(
    'form-input form-select',
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
      <select
        ref={ref}
        className={selectClasses}
        disabled={disabled}
        {...rest}
      >
        {placeholder && (
          <option value="" disabled>
            {placeholder}
          </option>
        )}
        {options.map((option) => (
          <option key={option.value} value={option.value}>
            {option.label}
          </option>
        ))}
      </select>
      {error && <p className="form-error">{error}</p>}
      {helper && !error && <p className="form-helper">{helper}</p>}
    </div>
  );
});

Select.displayName = 'Select';

export default Select;