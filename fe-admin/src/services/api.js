import axios from 'axios';
import { User } from 'oidc-client-ts';

const api = axios.create({
    baseURL: '/api', // Proxied to http://localhost:9000
    headers: {
        'Content-Type': 'application/json',
    },
});

/*
 * Request Interceptor to add Bearer Token
 * oidc-client-ts stores user in sessionStorage by default with key `oidc.user:${authority}:${clientId}`
 */
api.interceptors.request.use(
    (config) => {
        // Note: If you change authority or client_id in AuthWrapper.jsx, update this key!
        const oidcStorage = sessionStorage.getItem(`oidc.user:http://localhost:8180/realms/master:beef-admin`);
        if (oidcStorage) {
            const user = User.fromStorageString(oidcStorage);
            if (user && user.access_token) {
                config.headers.Authorization = `Bearer ${user.access_token}`;
            }
        }
        return config;
    },
    (error) => Promise.reject(error)
);

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
    // Note: No admin update status endpoint found in backend currently
};

export const userService = {
    getAll: () => api.get('/users/admin/all'),
    delete: (email) => api.delete(`/users/admin/${email}`),
};

export default api;
