import React, { useState } from 'react';
import { cn } from '../../utils/cn';

/**
 * Tooltip Component - Reusable tooltip component
 * 
 * @param {Object} props - Component props
 * @param {React.ReactNode} props.children - Element to show tooltip on
 * @param {string} props.content - Tooltip content
 * @param {string} props.position - Tooltip position (top, bottom, left, right)
 * @param {string} props.className - Additional classes
 */
const Tooltip = ({
  children,
  content,
  position = 'top',
  className,
}) => {
  const [isVisible, setIsVisible] = useState(false);
  
  const positionStyles = {
    top: 'bottom-full left-1/2 -translate-x-1/2 mb-2',
    bottom: 'top-full left-1/2 -translate-x-1/2 mt-2',
    left: 'right-full top-1/2 -translate-y-1/2 mr-2',
    right: 'left-full top-1/2 -translate-y-1/2 ml-2',
  };
  
  return (
    <div
      className="relative inline-block"
      onMouseEnter={() => setIsVisible(true)}
      onMouseLeave={() => setIsVisible(false)}
    >
      {children}
      {isVisible && (
        <div
          className={cn(
            'absolute z-50 px-2 py-1 text-xs text-white bg-gray-900 rounded shadow-lg whitespace-nowrap animate-fade-in',
            positionStyles[position],
            className
          )}
        >
          {content}
        </div>
      )}
    </div>
  );
};

export default Tooltip;