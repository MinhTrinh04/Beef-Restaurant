import axios, { AxiosInstance, AxiosError, InternalAxiosRequestConfig } from 'axios';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:9000';

// Create axios instance
const apiClient: AxiosInstance = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true, // Important for cookies
});

// Store for session token (will be set by components using useSession)
let cachedSession: { accessToken?: string } | null = null;

export const setSessionToken = (accessToken: string | undefined) => {
  cachedSession = { accessToken };
  console.log('🔐 API Client - Session token cached:', !!accessToken);
  if (accessToken) {
    console.log('🔐 API Client - Token preview:', accessToken.substring(0, 50) + '...');
  }
};

// Request interceptor - Add auth token
apiClient.interceptors.request.use(
  async (config: InternalAxiosRequestConfig) => {
    console.log('🔍 API Client - Making request to:', config.url);
    
    // Try to get session from NextAuth (client-side only)
    if (typeof window !== 'undefined') {
      try {
        // First try cached session
        if (cachedSession?.accessToken) {
          config.headers.Authorization = `Bearer ${cachedSession.accessToken}`;
          console.log('✅ API Client - Using cached token');
          console.log('✅ API Client - Authorization header:', config.headers.Authorization?.substring(0, 70) + '...');
          return config;
        }

        // Fallback to getSession
        const { getSession } = await import('next-auth/react');
        const session = await getSession();
        
        console.log('🔍 API Client - Session from getSession:', {
          hasSession: !!session,
          hasAccessToken: !!session?.accessToken,
        });
        
        if (session?.accessToken) {
          config.headers.Authorization = `Bearer ${session.accessToken}`;
          cachedSession = { accessToken: session.accessToken };
          console.log('✅ API Client - Token added from getSession');
          console.log('✅ API Client - Authorization header:', config.headers.Authorization?.substring(0, 70) + '...');
        } else {
          console.warn('⚠️ API Client - No access token in session');
        }
      } catch (error) {
        console.error('❌ API Client - Error getting session:', error);
      }
    }
    return config;
  },
  (error) => {
    console.error('❌ API Client - Request interceptor error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor - Handle errors
apiClient.interceptors.response.use(
  (response) => {
    console.log('✅ API Client - Response success:', {
      status: response.status,
      url: response.config.url
    });
    return response;
  },
  async (error: AxiosError) => {
    console.error('❌ API Client - Response error:', {
      status: error.response?.status,
      url: error.config?.url,
      message: error.message,
      data: error.response?.data
    });

    // Handle 401 errors
    if (error.response?.status === 401) {
      console.error('🚫 Unauthorized - Token may be invalid or expired');
      console.error('🚫 Response data:', error.response?.data);
      // Clear cached session
      cachedSession = null;
    }

    return Promise.reject(error);
  }
);

export default apiClient;
