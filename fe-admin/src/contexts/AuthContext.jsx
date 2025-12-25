import { createContext, useContext, useState, useEffect } from 'react';
import { authService } from '../services/authService';
import { setAccessToken, clearAccessToken } from '../services/api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const initAuth = async () => {
            try {
                // 1. Check existing token in localStorage first
                const token = localStorage.getItem('admin_access_token');
                if (token) {
                    setAccessToken(token); // Ensure api.js has it set
                    try {
                        const response = await authService.getCurrentUser();
                        setUser(response.data);
                        setLoading(false);
                        return; // ✅ Success with existing token
                    } catch (e) {
                        console.warn('❌ Existing token invalid, trying refresh...', e);
                    }
                }

                // 2. If no token or invalid, try to refresh via Cookie
                const response = await authService.refresh();
                setAccessToken(response.data.accessToken);
                const userRes = await authService.getCurrentUser();
                setUser(userRes.data);

            } catch (error) {
                console.log('ℹ️ No active session found');
                clearAccessToken();
                setUser(null);
            } finally {
                setLoading(false);
            }
        };

        initAuth();
    }, []);

    const login = async (email, password) => {
        const response = await authService.login(email, password);
        setAccessToken(response.data.accessToken);
        setUser(response.data.userProfile);
    };

    const logout = async () => {
        await authService.logout();
        clearAccessToken();
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, loading, login, logout }}>
            {!loading && children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within AuthProvider');
    }
    return context;
};
