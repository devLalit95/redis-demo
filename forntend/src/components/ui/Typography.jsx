import React from 'react';
import { cn } from '../../utils/cn';

/**
 * Typography Components - Reusable text components
 */

/**
 * Heading Component
 */
const Heading = ({
  level = 1,
  children,
  className,
  ...rest
}) => {
  const levelStyles = {
    1: 'text-4xl font-bold',
    2: 'text-3xl font-bold',
    3: 'text-2xl font-semibold',
    4: 'text-xl font-semibold',
    5: 'text-lg font-medium',
    6: 'text-base font-medium',
  };
  
  const Tag = `h${level}`;
  
  return (
    <Tag className={cn(levelStyles[level], 'text-gray-900 dark:text-gray-100', className)} {...rest}>
      {children}
    </Tag>
  );
};

/**
 * Text Component
 */
const Text = ({
  size = 'base',
  weight = 'normal',
  children,
  className,
  ...rest
}) => {
  const sizeStyles = {
    xs: 'text-xs',
    sm: 'text-sm',
    base: 'text-base',
    lg: 'text-lg',
    xl: 'text-xl',
    '2xl': 'text-2xl',
    '3xl': 'text-3xl',
  };
  
  const weightStyles = {
    light: 'font-light',
    normal: 'font-normal',
    medium: 'font-medium',
    semibold: 'font-semibold',
    bold: 'font-bold',
  };
  
  return (
    <p className={cn(sizeStyles[size], weightStyles[weight], 'text-gray-700 dark:text-gray-300', className)} {...rest}>
      {children}
    </p>
  );
};

/**
 * Link Component
 */
const Link = ({
  href,
  children,
  external = false,
  className,
  ...rest
}) => {
  return (
    <a
      href={href}
      target={external ? '_blank' : undefined}
      rel={external ? 'noopener noreferrer' : undefined}
      className={cn('text-primary-600 hover:text-primary-700 dark:text-primary-400 dark:hover:text-primary-300 transition-colors', className)}
      {...rest}
    >
      {children}
    </a>
  );
};

/**
 * Code Component
 */
const Code = ({
  inline = true,
  children,
  className,
  ...rest
}) => {
  if (inline) {
    return (
      <code className={cn('px-1.5 py-0.5 bg-gray-100 dark:bg-gray-800 rounded text-sm font-mono text-gray-800 dark:text-gray-200', className)} {...rest}>
        {children}
      </code>
    );
  }
  
  return (
    <code className={cn('block p-4 bg-gray-100 dark:bg-gray-800 rounded-lg text-sm font-mono text-gray-800 dark:text-gray-200 overflow-x-auto', className)} {...rest}>
      {children}
    </code>
  );
};

/**
 * Blockquote Component
 */
const Blockquote = ({ children, className, ...rest }) => {
  return (
    <blockquote className={cn('pl-4 border-l-4 border-primary-500 italic text-gray-700 dark:text-gray-300', className)} {...rest}>
      {children}
    </blockquote>
  );
};

export { Heading, Text, Link, Code, Blockquote };