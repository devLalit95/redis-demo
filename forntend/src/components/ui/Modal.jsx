import React, { useEffect } from 'react';
import { cn } from '../../utils/cn';
import { X } from 'lucide-react';

/**
 * Modal Component - Mobile-first responsive modal
 * 
 * @param {Object} props - Component props
 * @param {boolean} props.isOpen - Modal open state
 * @param {Function} props.onClose - Modal close function
 * @param {string} props.title - Modal title
 * @param {string} props.size - Modal size (sm, md, lg, xl, full) - responsive sizing
 * @param {boolean} props.closeOnOverlay - Close on overlay click
 * @param {boolean} props.closeOnEscape - Close on escape key
 * @param {boolean} props.showCloseButton - Show close button
 * @param {boolean} props.compact - Compact padding for mobile
 * @param {React.ReactNode} props.children - Modal content
 * @param {string} props.className - Additional classes
 */
const Modal = ({
  isOpen,
  onClose,
  title,
  size = 'md',
  closeOnOverlay = true,
  closeOnEscape = true,
  showCloseButton = true,
  compact = true,
  children,
  className,
}) => {
  useEffect(() => {
    if (closeOnEscape) {
      const handleEscape = (e) => {
        if (e.key === 'Escape' && isOpen) {
          onClose();
        }
      };
      document.addEventListener('keydown', handleEscape);
      return () => document.removeEventListener('keydown', handleEscape);
    }
  }, [isOpen, onClose, closeOnEscape]);
  
  useEffect(() => {
    if (isOpen) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = 'unset';
    }
    return () => {
      document.body.style.overflow = 'unset';
    };
  }, [isOpen]);
  
  if (!isOpen) return null;
  
  const sizeStyles = {
    sm: 'max-w-sm',
    md: 'max-w-md',
    lg: 'max-w-lg',
    xl: 'max-w-xl',
    full: 'max-w-full m-2',
  };
  
  const paddingStyles = compact ? 'p-3' : 'p-4';
  
  const handleOverlayClick = (e) => {
    if (closeOnOverlay && e.target === e.currentTarget) {
      onClose();
    }
  };
  
  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center p-2 sm:p-4 bg-black/50 animate-fade-in"
      onClick={handleOverlayClick}
    >
      <div
        className={cn(
          'bg-white dark:bg-gray-800 rounded-lg shadow-xl w-full animate-scale-in flex flex-col max-h-[95vh] sm:max-h-[90vh]',
          sizeStyles[size],
          className
        )}
      >
        {(title || showCloseButton) && (
          <div className={cn('flex items-center justify-between border-b border-gray-200 dark:border-gray-700 flex-shrink-0', compact ? 'p-3' : 'p-4')}>
            {title && (
              <h2 className={cn('font-semibold text-gray-900 dark:text-gray-100', compact ? 'text-base' : 'text-lg')}>
                {title}
              </h2>
            )}
            {showCloseButton && (
              <button
                onClick={onClose}
                className="p-1 rounded hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors"
              >
                <X className={cn('text-gray-500 dark:text-gray-400', compact ? 'w-4 h-4' : 'w-5 h-5')} />
              </button>
            )}
          </div>
        )}
        <div className={cn('overflow-y-auto flex-1', paddingStyles)}>{children}</div>
      </div>
    </div>
  );
};

export default Modal;