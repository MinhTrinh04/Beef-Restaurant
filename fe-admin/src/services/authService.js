import api from './api';

/**
 * Auth Service - Admin authentication
 * Backend: /api/v1/admin/* → Frontend calls: /admin/*
 * Gateway rewrites: /api/admin/* → /api/v1/admin/*
 */
export const authService = {
    /**
     * Admin login
     * Backend sẽ set HttpOnly cookie 'admin_refresh_token'
     */
    login: async (email, password) => {
        const response = await api.post('/admin/login', {
            email,
            password
        });
        return response.data;
    },

    /**
     * Admin logout
     * Backend sẽ xóa cookie
     */
    logout: async () => {
        await api.post('/admin/logout');
    },

    /**
     * Refresh access token
     * Đọc refresh token từ cookie
     */
    refresh: async () => {
        const response = await api.post('/admin/refresh');
        return response.data;
    },

    /**
     * Get current admin user
     * Yêu cầu Bearer token trong header
     */
    getCurrentUser: async () => {
        const response = await api.get('/admin/me');
        return response.data;
    }
};
