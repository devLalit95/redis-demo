import React from 'react';
import { cn } from '../../utils/cn';

/**
 * Card Component - Reusable card component with mobile-first design
 * 
 * @param {Object} props - Component props
 * @param {boolean} props.compact - Compact padding (mobile-first default)
 * @param {boolean} props.bordered - Bordered variant
 * @param {boolean} props.elevated - Elevated shadow
 * @param {boolean} props.interactive - Interactive hover effect
 * @param {boolean} props.noPadding - Remove all padding
 * @param {string} props.title - Card title
 * @param {React.ReactNode} props.children - Card content
 * @param {string} props.className - Additional classes
 */
const Card = React.forwardRef(({
  compact = true,
  bordered = false,
  elevated = false,
  interactive = false,
  noPadding = false,
  title,
  children,
  className,
  ...rest
}, ref) => {
  const classes = cn(
    'card',
    compact && 'card-compact',
    bordered && 'card-bordered',
    elevated && 'card-elevated',
    interactive && 'card-interactive',
    noPadding && 'card-no-padding',
    className
  );
  
  return (
    <div ref={ref} className={classes} {...rest}>
      {title && <div className="card-header"><h3 className="card-title">{title}</h3></div>}
      {children}
    </div>
  );
});

Card.displayName = 'Card';

/**
 * CardHeader Component - Card header section with compact mobile styling
 */
const CardHeader = ({ children, className, compact = true }) => {
  return <div className={cn('card-header', compact && 'card-header-compact', className)}>{children}</div>;
};

/**
 * CardTitle Component - Card title with responsive sizing
 */
const CardTitle = ({ children, className, size = 'md' }) => {
  const sizeClasses = {
    sm: 'text-sm',
    md: 'text-base',
    lg: 'text-lg',
    xl: 'text-xl'
  };
  return <h3 className={cn('card-title', sizeClasses[size], className)}>{children}</h3>;
};

/**
 * CardSubtitle Component - Card subtitle
 */
const CardSubtitle = ({ children, className }) => {
  return <p className={cn('card-subtitle', className)}>{children}</p>;
};

/**
 * CardBody Component - Card body section with compact mobile styling
 */
const CardBody = ({ children, className, compact = true }) => {
  return <div className={cn('card-body', compact && 'card-body-compact', className)}>{children}</div>;
};

/**
 * CardFooter Component - Card footer section
 */
const CardFooter = ({ children, className, compact = true }) => {
  return <div className={cn('card-footer', compact && 'card-footer-compact', className)}>{children}</div>;
};

export { Card, CardHeader, CardTitle, CardSubtitle, CardBody, CardFooter };
export default Card;