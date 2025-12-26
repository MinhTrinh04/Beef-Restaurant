import { type ClassValue, clsx } from "clsx";
import { twMerge } from "tailwind-merge";

/**
 * Merge Tailwind CSS classes
 */
export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

/**
 * Format price to VND
 */
export function formatPrice(price: number): string {
  return `${price.toLocaleString('vi-VN')}₫`;
}

/**
 * Format date to Vietnamese format
 */
export function formatDate(date: string | Date): string {
  const d = typeof date === 'string' ? new Date(date) : date;
  return d.toLocaleDateString('vi-VN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
  });
}

/**
 * Format date to short format
 */
export function formatDateShort(date: string | Date): string {
  const d = typeof date === 'string' ? new Date(date) : date;
  return d.toLocaleDateString('vi-VN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
  });
}

/**
 * Truncate text
 */
export function truncate(text: string, length: number): string {
  if (text.length <= length) return text;
  return text.substring(0, length) + '...';
}

/**
 * Generate slug from text
 */
export function generateSlug(text: string): string {
  return text
    .toLowerCase()
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .replace(/[đĐ]/g, 'd')
    .replace(/[^a-z0-9]+/g, '-')
    .replace(/^-+|-+$/g, '');
}

/**
 * Validate email
 */
export function isValidEmail(email: string): boolean {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

/**
 * Get order status color
 */
export function getOrderStatusColor(status: string): string {
  const statusColors: Record<string, string> = {
    pending: 'text-yellow-500',
    confirmed: 'text-blue-500',
    preparing: 'text-purple-500',
    ready: 'text-green-500',
    delivered: 'text-green-700',
    cancelled: 'text-red-500',
    paid: 'text-green-600',
    validated: 'text-yellow-600',
  };
  return statusColors[status?.toLowerCase() || 'unknown'] || 'text-gray-500';
}

/**
 * Get order status badge color
 */
export function getOrderStatusBadgeColor(status: string): string {
  const statusColors: Record<string, string> = {
    pending: 'bg-yellow-500/20 text-yellow-500',
    confirmed: 'bg-blue-500/20 text-blue-500',
    preparing: 'bg-purple-500/20 text-purple-500',
    ready: 'bg-green-500/20 text-green-500',
    delivered: 'bg-green-700/20 text-green-700',
    cancelled: 'bg-red-500/20 text-red-500',
    paid: 'bg-green-600/20 text-green-600',
    validated: 'bg-yellow-600/20 text-yellow-600',
  };
  return statusColors[status?.toLowerCase() || 'unknown'] || 'bg-gray-500/20 text-gray-500';
}
