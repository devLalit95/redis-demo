import React from 'react';
import { cn } from '../../utils/cn';

/**
 * Checkbox Component - Reusable checkbox component
 * 
 * @param {Object} props - Component props
 * @param {boolean} props.checked - Checkbox state
 * @param {Function} props.onChange - Change handler
 * @param {boolean} props.disabled - Disable checkbox
 * @param {string} props.label - Checkbox label
 * @param {boolean} props.required - Show required asterisk
 * @param {string} props.helper - Helper text
 * @param {string} props.error - Error message
 * @param {string} props.className - Additional classes
 */
const Checkbox = React.forwardRef(({
  checked = false,
  onChange,
  disabled = false,
  label,
  required = false,
  helper,
  error,
  className,
  ...rest
}, ref) => {
  const checkboxClasses = cn(
    'form-checkbox',
    'w-4 h-4 text-primary-600 rounded border-gray-300 focus:ring-primary-500',
    error && 'border-danger-500 focus:ring-danger-500',
    className
  );
  
  return (
    <div className="form-group">
      <div className="flex items-start">
        <div className="flex items-center h-5">
          <input
            ref={ref}
            type="checkbox"
            checked={checked}
            onChange={onChange}
            disabled={disabled}
            className={checkboxClasses}
            {...rest}
          />
        </div>
        {label && (
          <div className="ml-3 text-sm">
            <label className={cn('font-medium text-gray-700 dark:text-gray-300', required && 'required')}>
              {label}
            </label>
            {helper && !error && <p className="text-gray-500 dark:text-gray-400">{helper}</p>}
            {error && <p className="form-error">{error}</p>}
          </div>
        )}
      </div>
    </div>
  );
});

Checkbox.displayName = 'Checkbox';

export default Checkbox;