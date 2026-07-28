import React from 'react';
import { cn } from '../../utils/cn';

/**
 * Card Component - Reusable card component
 * 
 * @param {Object} props - Component props
 * @param {boolean} props.compact - Compact padding
 * @param {boolean} props.bordered - Bordered variant
 * @param {boolean} props.elevated - Elevated shadow
 * @param {boolean} props.interactive - Interactive hover effect
 * @param {React.ReactNode} props.children - Card content
 * @param {string} props.className - Additional classes
 */
const Card = React.forwardRef(({
  compact = false,
  bordered = false,
  elevated = false,
  interactive = false,
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
    className
  );
  
  return (
    <div ref={ref} className={classes} {...rest}>
      {children}
    </div>
  );
});

Card.displayName = 'Card';

/**
 * CardHeader Component - Card header section
 */
const CardHeader = ({ children, className }) => {
  return <div className={cn('card-header', className)}>{children}</div>;
};

/**
 * CardTitle Component - Card title
 */
const CardTitle = ({ children, className }) => {
  return <h3 className={cn('card-title', className)}>{children}</h3>;
};

/**
 * CardSubtitle Component - Card subtitle
 */
const CardSubtitle = ({ children, className }) => {
  return <p className={cn('card-subtitle', className)}>{children}</p>;
};

/**
 * CardBody Component - Card body section
 */
const CardBody = ({ children, className }) => {
  return <div className={cn('card-body', className)}>{children}</div>;
};

/**
 * CardFooter Component - Card footer section
 */
const CardFooter = ({ children, className }) => {
  return <div className={cn('card-footer', className)}>{children}</div>;
};

export { Card, CardHeader, CardTitle, CardSubtitle, CardBody, CardFooter };