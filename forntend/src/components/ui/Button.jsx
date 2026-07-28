import React from 'react';
import { cn } from '../../utils/cn';
import { Loader2 } from 'lucide-react';

/**
 * Button Component - Reusable button component with multiple variants
 * 
 * @param {Object} props - Component props
 * @param {string} props.variant - Button variant (primary, secondary, success, danger, warning, outline-primary, outline-secondary, outline-success, outline-danger, ghost)
 * @param {string} props.size - Button size (sm, md, lg, xl)
 * @param {boolean} props.loading - Show loading state
 * @param {boolean} props.disabled - Disable button
 * @param {boolean} props.block - Full width button
 * @param {boolean} props.icon - Icon button
 * @param {React.ReactNode} props.children - Button content
 * @param {React.ReactNode} props.leftIcon - Icon to show on left
 * @param {React.ReactNode} props.rightIcon - Icon to show on right
 * @param {string} props.className - Additional classes
 * @param {Object} props.rest - Other button attributes
 */
const Button = React.forwardRef(({
  variant = 'primary',
  size = 'md',
  loading = false,
  disabled = false,
  block = false,
  icon = false,
  leftIcon,
  rightIcon,
  children,
  className,
  ...rest
}, ref) => {
  const baseStyles = 'btn';
  
  const variantStyles = {
    primary: 'btn-primary',
    secondary: 'btn-secondary',
    success: 'btn-success',
    danger: 'btn-danger',
    warning: 'btn-warning',
    'outline-primary': 'btn-outline-primary',
    'outline-secondary': 'btn-outline-secondary',
    'outline-success': 'btn-outline-success',
    'outline-danger': 'btn-outline-danger',
    ghost: 'btn-ghost',
  };
  
  const sizeStyles = {
    sm: 'btn-sm',
    md: 'btn-md',
    lg: 'btn-lg',
    xl: 'btn-xl',
  };
  
  const iconStyles = icon ? 'btn-icon' : '';
  const blockStyles = block ? 'btn-block' : '';
  const loadingStyles = loading ? 'btn-loading' : '';
  
  const classes = cn(
    baseStyles,
    variantStyles[variant],
    sizeStyles[size],
    iconStyles,
    blockStyles,
    loadingStyles,
    className
  );
  
  return (
    <button
      ref={ref}
      className={classes}
      disabled={disabled || loading}
      {...rest}
    >
      {loading && <Loader2 className="absolute w-4 h-4" />}
      {!loading && leftIcon && <span className="btn-icon-wrapper">{leftIcon}</span>}
      {!loading && !icon && children}
      {!loading && rightIcon && <span className="btn-icon-wrapper">{rightIcon}</span>}
    </button>
  );
});

Button.displayName = 'Button';

export default Button;