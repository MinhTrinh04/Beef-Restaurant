import axios, { AxiosInstance, AxiosError } from 'axios';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:9000';

// Create axios instance
const apiClient: AxiosInstance = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true, // Important for cookies
});

// Request interceptor - Add auth token
apiClient.interceptors.request.use(
  (config) => {
    // For now, we'll skip auth token (will add in next commit)
    // const token = getAccessToken();
    // if (token) {
    //   config.headers.Authorization = `Bearer ${token}`;
    // }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor - Handle errors
apiClient.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error: AxiosError) => {
    // Handle 401 errors (will implement token refresh later)
    if (error.response?.status === 401) {
      // TODO: Implement token refresh logic
      console.error('Unauthorized - Please login again');
    }

    return Promise.reject(error);
  }
);

export default apiClient;
