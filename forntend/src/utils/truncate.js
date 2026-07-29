/**
 * truncate - Truncate string to specified length
 * @param {string} str - String to truncate
 * @param {number} length - Maximum length
 * @param {string} suffix - Suffix to add when truncated (default: '...')
 * @returns {string} - Truncated string
 */
export function truncate(str, length = 50, suffix = '...') {
  if (!str || typeof str !== 'string') return '';
  
  if (str.length <= length) return str;
  
  return str.slice(0, length - suffix.length) + suffix;
}

/**
 * truncateWords - Truncate string to specified number of words
 * @param {string} str - String to truncate
 * @param {number} words - Maximum number of words
 * @param {string} suffix - Suffix to add when truncated (default: '...')
 * @returns {string} - Truncated string
 */
export function truncateWords(str, words = 10, suffix = '...') {
  if (!str || typeof str !== 'string') return '';
  
  const wordArray = str.split(' ');
  
  if (wordArray.length <= words) return str;
  
  return wordArray.slice(0, words).join(' ') + suffix;
}

/**
 * truncateMiddle - Truncate string from the middle
 * @param {string} str - String to truncate
 * @param {number} length - Maximum length
 * @param {string} separator - Separator to use in the middle (default: '...')
 * @returns {string} - Truncated string
 */
export function truncateMiddle(str, length = 20, separator = '...') {
  if (!str || typeof str !== 'string') return '';
  
  if (str.length <= length) return str;
  
  const halfLength = Math.floor((length - separator.length) / 2);
  return str.slice(0, halfLength) + separator + str.slice(-halfLength);
}

/**
 * truncateStart - Truncate string from the start
 * @param {string} str - String to truncate
 * @param {number} length - Maximum length
 * @param {string} suffix - Suffix to add when truncated (default: '...')
 * @returns {string} - Truncated string
 */
export function truncateStart(str, length = 20, suffix = '...') {
  if (!str || typeof str !== 'string') return '';
  
  if (str.length <= length) return str;
  
  return suffix + str.slice(str.length - length + suffix.length);
}