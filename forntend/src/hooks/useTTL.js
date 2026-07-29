/**
 * useTTL Hook
 * Custom hook for managing TTL (Time To Live) countdowns
 */

import { useState, useEffect, useCallback, useRef } from 'react';
import { formatTTL, formatTTLCountdown, getTTLColor, getTTLStatus } from '../utils/formatTTL';

/**
 * Custom hook for TTL countdown
 * @param {number} initialTTL - Initial TTL in seconds
 * @param {Object} options - TTL options
 * @returns {Object} - TTL state and functions
 */
export function useTTL(initialTTL, options = {}) {
  const {
    autoStart = true,
    onExpire,
    onTick,
  } = options;

  const [ttl, setTTL] = useState(initialTTL);
  const [isRunning, setIsRunning] = useState(autoStart);
  const [isExpired, setIsExpired] = useState(false);
  const intervalRef = useRef(null);

  const start = useCallback(() => {
    if (isRunning || ttl <= 0) return;
    setIsRunning(true);
  }, [isRunning, ttl]);

  const pause = useCallback(() => {
    setIsRunning(false);
    if (intervalRef.current) {
      clearInterval(intervalRef.current);
      intervalRef.current = null;
    }
  }, []);

  const reset = useCallback((newTTL = initialTTL) => {
    setTTL(newTTL);
    setIsExpired(false);
    setIsRunning(autoStart);
  }, [initialTTL, autoStart]);

  const setTTLValue = useCallback((newTTL) => {
    setTTL(newTTL);
    setIsExpired(newTTL <= 0);
  }, []);

  // Countdown effect
  useEffect(() => {
    if (!isRunning || ttl <= 0) {
      if (intervalRef.current) {
        clearInterval(intervalRef.current);
        intervalRef.current = null;
      }
      return;
    }

    intervalRef.current = setInterval(() => {
      setTTL((prev) => {
        const newTTL = prev - 1;
        
        if (onTick) {
          onTick(newTTL);
        }
        
        if (newTTL <= 0) {
          setIsExpired(true);
          setIsRunning(false);
          if (intervalRef.current) {
            clearInterval(intervalRef.current);
            intervalRef.current = null;
          }
          if (onExpire) {
            onExpire();
          }
        }
        
        return newTTL;
      });
    }, 1000);

    return () => {
      if (intervalRef.current) {
        clearInterval(intervalRef.current);
        intervalRef.current = null;
      }
    };
  }, [isRunning, ttl, onTick, onExpire]);

  const ttlInfo = {
    value: ttl,
    formatted: formatTTL(ttl, 'detailed'),
    color: getTTLColor(ttl),
    status: getTTLStatus(ttl),
    isExpired,
    isRunning,
    percentage: initialTTL > 0 ? (ttl / initialTTL) * 100 : 0,
  };

  return {
    ttl,
    isRunning,
    isExpired,
    ttlInfo,
    start,
    pause,
    reset,
    setTTL: setTTLValue,
  };
}

/**
 * useTTLCountdown Hook
 * Custom hook for TTL countdown from expiration timestamp
 */
export function useTTLCountdown(expiresAt, options = {}) {
  const {
    autoStart = true,
    onExpire,
    onTick,
  } = options;

  const [remaining, setRemaining] = useState(0);
  const [isExpired, setIsExpired] = useState(false);
  const intervalRef = useRef(null);

  const calculateRemaining = useCallback(() => {
    const now = Math.floor(Date.now() / 1000);
    const remainingTime = expiresAt - now;
    setRemaining(Math.max(0, remainingTime));
    setIsExpired(remainingTime <= 0);
  }, [expiresAt]);

  const start = useCallback(() => {
    if (intervalRef.current) return;
    
    calculateRemaining();
    
    intervalRef.current = setInterval(() => {
      calculateRemaining();
      
      if (onTick) {
        onTick(remaining);
      }
      
      if (remaining <= 0) {
        setIsExpired(true);
        if (intervalRef.current) {
          clearInterval(intervalRef.current);
          intervalRef.current = null;
        }
        if (onExpire) {
          onExpire();
        }
      }
    }, 1000);
  }, [calculateRemaining, remaining, onTick, onExpire]);

  const pause = useCallback(() => {
    if (intervalRef.current) {
      clearInterval(intervalRef.current);
      intervalRef.current = null;
    }
  }, []);

  const reset = useCallback(() => {
    calculateRemaining();
  }, [calculateRemaining]);

  // Auto-start effect
  useEffect(() => {
    if (autoStart && expiresAt) {
      start();
      return () => {
        if (intervalRef.current) {
          clearInterval(intervalRef.current);
          intervalRef.current = null;
        }
      };
    }
  }, [autoStart, expiresAt, start]);

  const ttlInfo = {
    value: remaining,
    formatted: formatTTLCountdown(expiresAt),
    color: getTTLColor(remaining),
    status: getTTLStatus(remaining),
    isExpired,
    expiresAt,
  };

  return {
    remaining,
    isExpired,
    ttlInfo,
    start,
    pause,
    reset,
  };
}

export default useTTL;