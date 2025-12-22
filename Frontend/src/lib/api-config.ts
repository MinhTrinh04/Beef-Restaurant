// Centralized API configuration
// All API calls should use these constants instead of hardcoded URLs

/**
 * Base URL for API Gateway
 * Default: http://localhost:9000
 * Can be overridden via NEXT_PUBLIC_API_BASE environment variable
 */
export const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE || "http://localhost:9000";

/**
 * API Endpoints
 */
export const API_ENDPOINTS = {
  // User Service (via Gateway: /api/users/*)
  users: {
    login: `${API_BASE_URL}/api/users/login`,
    register: `${API_BASE_URL}/api/users/register`,
    profile: `${API_BASE_URL}/api/users/me`,
    refreshToken: `${API_BASE_URL}/api/users/refresh-token`,
    logout: `${API_BASE_URL}/api/users/logout`,
    checkEmail: (email: string) => `${API_BASE_URL}/api/users/check-email?email=${encodeURIComponent(email)}`,
    resendVerification: `${API_BASE_URL}/api/users/resend-verification`,
    verificationStatus: (email: string) => `${API_BASE_URL}/api/users/verification-status?email=${encodeURIComponent(email)}`,
  },
  
  // Basket Service (via Gateway: /api/basket/*)
  basket: {
    base: `${API_BASE_URL}/api/basket`,
    checkout: `${API_BASE_URL}/api/basket/checkout`,
  },
  
  // Menu Service (via Gateway: /api/menu/*)
  menu: {
    items: `${API_BASE_URL}/api/menu/items`,
    itemsByCategory: (categoryId: number) => `${API_BASE_URL}/api/menu/items/category/${categoryId}`,
  },
  
  // Order Service (via Gateway: /api/orders/*)
  orders: {
    base: `${API_BASE_URL}/api/orders`,
    byId: (id: string) => `${API_BASE_URL}/api/orders/${id}`,
  },
};

/**
 * Legacy exports for backward compatibility
 * @deprecated Use API_ENDPOINTS instead
 */
export const API_URL = {
  users: `${API_BASE_URL}/api/users`,
  basket: `${API_BASE_URL}/api/basket`,
  menu: `${API_BASE_URL}/api/menu`,
  orders: `${API_BASE_URL}/api/orders`,
};
