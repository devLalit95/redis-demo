/**
 * capitalize - Capitalize first letter of a string
 * @param {string} str - String to capitalize
 * @returns {string} - Capitalized string
 */
export function capitalize(str) {
  if (!str || typeof str !== 'string') return '';
  
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
}

/**
 * capitalizeAll - Capitalize first letter of each word
 * @param {string} str - String to capitalize
 * @returns {string} - Capitalized string
 */
export function capitalizeAll(str) {
  if (!str || typeof str !== 'string') return '';
  
  return str
    .split(' ')
    .map(word => capitalize(word))
    .join(' ');
}

/**
 * toCamelCase - Convert string to camelCase
 * @param {string} str - String to convert
 * @returns {string} - camelCase string
 */
export function toCamelCase(str) {
  if (!str || typeof str !== 'string') return '';
  
  return str
    .replace(/(?:^\w|[A-Z]|\b\w)/g, (word, index) => {
      return index === 0 ? word.toLowerCase() : word.toUpperCase();
    })
    .replace(/\s+/g, '')
    .replace(/[-_]/g, '');
}

/**
 * toSnakeCase - Convert string to snake_case
 * @param {string} str - String to convert
 * @returns {string} - snake_case string
 */
export function toSnakeCase(str) {
  if (!str || typeof str !== 'string') return '';
  
  return str
    .replace(/\W+/g, ' ')
    .split(/ |\B(?=[A-Z])/)
    .map(word => word.toLowerCase())
    .join('_');
}

/**
 * toKebabCase - Convert string to kebab-case
 * @param {string} str - String to convert
 * @returns {string} - kebab-case string
 */
export function toKebabCase(str) {
  if (!str || typeof str !== 'string') return '';
  
  return str
    .replace(/\W+/g, ' ')
    .split(/ |\B(?=[A-Z])/)
    .map(word => word.toLowerCase())
    .join('-');
}