// Centralized API configuration
// All API calls should use these constants instead of hardcoded URLs

/**
 * Base URL for API Gateway
 * Default: http://localhost:9000
 * Can be overridden via NEXT_PUBLIC_API_BASE environment variable
 */
const getApiBaseUrl = () => {
  // On server-side (Next.js SSR), call the Gateway service directly via internal K8s DNS
  // This avoids DNS issues with external domains and bypasses SSL verification
  if (typeof window === 'undefined') {
    return process.env.INTERNAL_API_BASE_URL || "http://gateway-service:9000";
  }
  // On client-side (Browser), use the public API URL
  return process.env.NEXT_PUBLIC_API_BASE || "http://localhost:9000";
};

export const API_BASE_URL = getApiBaseUrl();

/**
 * API Endpoints
 */
export const API_ENDPOINTS = {
  // User Service (via Gateway: /api/v1/users/*)
  users: {
    login: `${API_BASE_URL}/api/v1/users/login`,
    register: `${API_BASE_URL}/api/v1/users/register`,
    profile: `${API_BASE_URL}/api/v1/users/me`,
    refreshToken: `${API_BASE_URL}/api/v1/users/refresh-token`,
    logout: `${API_BASE_URL}/api/v1/users/logout`,
    checkEmail: (email: string) => `${API_BASE_URL}/api/v1/users/check-email?email=${encodeURIComponent(email)}`,
    resendVerification: `${API_BASE_URL}/api/v1/users/resend-verification`,
    verificationStatus: (email: string) => `${API_BASE_URL}/api/v1/users/verification-status?email=${encodeURIComponent(email)}`,
  },

  // Basket Service (via Gateway: /api/v1/basket/*)
  basket: {
    base: `${API_BASE_URL}/api/v1/basket`,
    checkout: `${API_BASE_URL}/api/v1/basket/checkout`,
  },

  // Menu Service (via Gateway: /api/v1/menu/*)
  menu: {
    items: `${API_BASE_URL}/api/v1/menu/items`,
    itemsByCategory: (categoryId: number) => `${API_BASE_URL}/api/v1/menu/items/category/${categoryId}`,
  },

  // Order Service (via Gateway: /api/v1/orders/*)
  orders: {
    base: `${API_BASE_URL}/api/v1/orders`,
    byId: (id: string) => `${API_BASE_URL}/api/v1/orders/${id}`,
  },
};

/**
 * Legacy exports for backward compatibility
 * @deprecated Use API_ENDPOINTS instead
 */
export const API_URL = {
  users: `${API_BASE_URL}/api/v1/users`,
  basket: `${API_BASE_URL}/api/v1/basket`,
  menu: `${API_BASE_URL}/api/v1/menu`,
  orders: `${API_BASE_URL}/api/v1/orders`,
};
