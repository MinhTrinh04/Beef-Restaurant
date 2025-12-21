import { createContext, useContext, useState, useEffect } from 'react';
import { authService } from '../services/authService';
import { setAccessToken, clearAccessToken } from '../services/api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Try to refresh on mount (nếu có admin_refresh_token cookie)
        // Nếu không có cookie, backend sẽ trả 401 và chúng ta im lặng fail
        authService.refresh()
            .then(response => {
                setAccessToken(response.data.accessToken);
                return authService.getCurrentUser();
            })
            .then(response => setUser(response.data))
            .catch((error) => {
                // Silent fail - không có refresh token là bình thường khi chưa login
                console.log('No active session, redirecting to login');
                setUser(null);
            })
            .finally(() => setLoading(false));
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
