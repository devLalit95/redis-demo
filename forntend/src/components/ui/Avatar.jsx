import React from 'react';
import { cn } from '../../utils/cn';

/**
 * Avatar Component - User avatar component
 * 
 * @param {Object} props - Component props
 * @param {string} props.src - Image source
 * @param {string} props.alt - Alt text
 * @param {string} props.size - Avatar size (sm, md, lg, xl)
 * @param {string} props.name - User name for fallback
 * @param {boolean} props.rounded - Rounded corners
 * @param {string} props.className - Additional classes
 */
const Avatar = ({
  src,
  alt,
  size = 'md',
  name,
  rounded = true,
  className,
}) => {
  const sizeStyles = {
    sm: 'w-8 h-8 text-xs',
    md: 'w-10 h-10 text-sm',
    lg: 'w-12 h-12 text-base',
    xl: 'w-16 h-16 text-lg',
  };
  
  const roundedStyles = rounded ? 'rounded-full' : 'rounded-lg';
  
  const getInitials = (name) => {
    if (!name) return '';
    return name
      .split(' ')
      .map((n) => n[0])
      .join('')
      .toUpperCase()
      .slice(0, 2);
  };
  
  const getBackgroundColor = (name) => {
    if (!name) return 'bg-gray-200 dark:bg-gray-700';
    const colors = [
      'bg-red-500',
      'bg-orange-500',
      'bg-amber-500',
      'bg-yellow-500',
      'bg-lime-500',
      'bg-green-500',
      'bg-emerald-500',
      'bg-teal-500',
      'bg-cyan-500',
      'bg-sky-500',
      'bg-blue-500',
      'bg-indigo-500',
      'bg-violet-500',
      'bg-purple-500',
      'bg-fuchsia-500',
      'bg-pink-500',
      'bg-rose-500',
    ];
    const index = name.charCodeAt(0) % colors.length;
    return colors[index];
  };
  
  if (src) {
    return (
      <img
        src={src}
        alt={alt || 'Avatar'}
        className={cn(sizeStyles[size], roundedStyles, 'object-cover', className)}
      />
    );
  }
  
  return (
    <div
      className={cn(
        sizeStyles[size],
        roundedStyles,
        'flex items-center justify-center font-medium text-white',
        getBackgroundColor(name),
        className
      )}
    >
      {getInitials(name)}
    </div>
  );
};

export default Avatar;