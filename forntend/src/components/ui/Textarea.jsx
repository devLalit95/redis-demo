import React from 'react';
import { cn } from '../../utils/cn';

/**
 * Textarea Component - Reusable textarea component
 * 
 * @param {Object} props - Component props
 * @param {string} props.label - Textarea label
 * @param {boolean} props.required - Show required asterisk
 * @param {string} props.helper - Helper text
 * @param {string} props.error - Error message
 * @param {boolean} props.disabled - Disable textarea
 * @param {number} props.rows - Number of rows
 * @param {string} props.className - Additional classes
 * @param {Object} props.rest - Other textarea attributes
 */
const Textarea = React.forwardRef(({
  label,
  required = false,
  helper,
  error,
  disabled = false,
  rows = 4,
  className,
  ...rest
}, ref) => {
  const textareaClasses = cn(
    'form-input form-textarea',
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
      <textarea
        ref={ref}
        className={textareaClasses}
        disabled={disabled}
        rows={rows}
        {...rest}
      />
      {error && <p className="form-error">{error}</p>}
      {helper && !error && <p className="form-helper">{helper}</p>}
    </div>
  );
});

Textarea.displayName = 'Textarea';

export default Textarea;