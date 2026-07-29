/**
 * formatPercentage - Format number as percentage
 * @param {number} value - Value to format
 * @param {number} decimals - Number of decimal places
 * @param {boolean} withSymbol - Include % symbol
 * @returns {string} - Formatted percentage string
 */
export function formatPercentage(value, decimals = 1, withSymbol = true) {
  if (value === null || value === undefined) return 'N/A';
  
  if (typeof value !== 'number' || isNaN(value)) return 'Invalid';
  
  const formatted = value.toFixed(decimals);
  
  return withSymbol ? `${formatted}%` : formatted;
}

/**
 * formatRatio - Format ratio as percentage (e.g., cache hit ratio)
 * @param {number} numerator - Numerator value
 * @param {number} denominator - Denominator value
 * @param {number} decimals - Number of decimal places
 * @returns {string} - Formatted percentage string
 */
export function formatRatio(numerator, denominator, decimals = 1) {
  if (denominator === 0) return '0%';
  if (numerator === null || denominator === null) return 'N/A';
  
  const percentage = (numerator / denominator) * 100;
  return formatPercentage(percentage, decimals);
}

/**
 * getPercentageColor - Get color based on percentage value
 * @param {number} value - Percentage value (0-100)
 * @returns {string} - Color class or hex value
 */
export function getPercentageColor(value) {
  if (value === null || value === undefined) return '#9ca3af';
  
  if (value >= 90) return '#22c55e'; // Green - Excellent
  if (value >= 70) return '#0ea5e9'; // Blue - Good
  if (value >= 50) return '#f59e0b'; // Orange - Fair
  return '#ef4444'; // Red - Poor
}

/**
 * getPercentageStatus - Get status based on percentage value
 * @param {number} value - Percentage value (0-100)
 * @returns {string} - Status string
 */
export function getPercentageStatus(value) {
  if (value === null || value === undefined) return 'Unknown';
  
  if (value >= 90) return 'Excellent';
  if (value >= 70) return 'Good';
  if (value >= 50) return 'Fair';
  return 'Poor';
}