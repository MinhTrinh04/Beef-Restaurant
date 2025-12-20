import { useAuth } from 'react-oidc-context';
import { useEffect } from 'react';

const ProtectedRoute = ({ children }) => {
    const auth = useAuth();

    useEffect(() => {
        if (!auth.isLoading && !auth.isAuthenticated) {
            auth.signinRedirect();
        }
    }, [auth.isLoading, auth.isAuthenticated]); // Removed 'auth' object dependency

    if (auth.isLoading) {
        return <div style={{ display: 'flex', justifyContent: 'center', marginTop: '20%' }}>Loading Authentication...</div>;
    }

    if (!auth.isAuthenticated) {
        return null; // Will redirect
    }

    return children;
};

export default ProtectedRoute;
