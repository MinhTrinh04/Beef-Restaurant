import axios from 'axios';

const api = axios.create({
    baseURL: '/api',
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
        const originalRequest = error.config;

        // Nếu 401 và chưa retry
        // QUAN TRỌNG: Không retry nếu request là refresh endpoint (tránh vòng lặp)
        if (error.response?.status === 401 &&
            !originalRequest._retry &&
            !originalRequest.url?.includes('/admin/refresh')) {

            if (isRefreshing) {
                // Đợi refresh hoàn thành
                return new Promise((resolve, reject) => {
                    failedQueue.push({ resolve, reject });
                }).then(token => {
                    originalRequest.headers.Authorization = `Bearer ${token}`;
                    return api(originalRequest);
                }).catch(err => Promise.reject(err));
            }

            originalRequest._retry = true;
            isRefreshing = true;

            try {
                // Gọi refresh endpoint
                console.log('🔄 Attempting to refresh access token...');
                const response = await axios.post('/api/admin/refresh', {}, {
                    withCredentials: true
                });

                const newAccessToken = response.data.data.accessToken;
                console.log('✅ Token refreshed successfully');
                setAccessToken(newAccessToken);

                processQueue(null, newAccessToken);

                // Retry request ban đầu
                originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
                return api(originalRequest);

            } catch (refreshError) {
                console.error('❌ Token refresh failed:', refreshError.response?.data || refreshError.message);
                processQueue(refreshError, null);
                clearAccessToken();
                // Chỉ redirect nếu không phải đang ở trang login
                if (!window.location.pathname.includes('/login')) {
                    window.location.href = '/login';
                }
                return Promise.reject(refreshError);
            } finally {
                isRefreshing = false;
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
