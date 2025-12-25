import axios from 'axios';

const api = axios.create({
    baseURL: (import.meta.env.VITE_API_BASE_URL || '') + '/api/v1',
    withCredentials: true, // ✅ Gửi cookies (admin_refresh_token)
    headers: {
        'Content-Type': 'application/json',
    },
});

// Access token management
let accessToken = localStorage.getItem('admin_access_token');

export const setAccessToken = (token) => {
    console.log('Setting Access Token:', token ? '***' + token.slice(-5) : 'null');
    accessToken = token;
    if (token) {
        localStorage.setItem('admin_access_token', token);
    } else if (token === null) {
        localStorage.removeItem('admin_access_token');
    }
};

export const getAccessToken = () => accessToken;

export const clearAccessToken = () => {
    accessToken = null;
    localStorage.removeItem('admin_access_token');
};

// Request interceptor - thêm Bearer token
api.interceptors.request.use(
    (config) => {
        if (accessToken) {
            config.headers.Authorization = `Bearer ${accessToken}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// Response interceptor - auto refresh khi 401
let isRefreshing = false;
let failedQueue = [];

const processQueue = (error, token = null) => {
    failedQueue.forEach(prom => {
        if (error) {
            prom.reject(error);
        } else {
            prom.resolve(token);
        }
    });
    failedQueue = [];
};

api.interceptors.response.use(
    (response) => response,
    async (error) => {
        // Nếu 401 Unauthorized -> Logout luôn, bỏ qua Refresh Token
        if (error.response?.status === 401) {
            console.warn('⚠️ 401 Unauthorized - Logging out...');
            clearAccessToken();
            if (!window.location.pathname.includes('/login')) {
                window.location.href = '/login';
            }
        }
        return Promise.reject(error);
    }
);

// Service exports
export const menuService = {
    getAll: () => api.get('/menu/items'),
    create: (data) => api.post('/menu/items', data),
    update: (id, data) => api.put(`/menu/items/${id}`, data),
    delete: (id) => api.delete(`/menu/items/${id}`),
};

export const orderService = {
    getAll: () => api.get('/admin/orders'),
    getById: (id) => api.get(`/admin/orders/${id}`),
    getByUser: (userId) => api.get(`/admin/orders/user/${userId}`),
};

export const userService = {
    getAll: () => api.get('/users/admin/all'),
    delete: (email) => api.delete(`/users/admin/${email}`),
};

export default api;
