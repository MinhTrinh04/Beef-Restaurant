import React from 'react';
import { AuthProvider } from 'react-oidc-context';

/* 
  Keycloak Configuration
  Authority: URL to the Keycloak Realm.
  ClientId: Client ID configured in Keycloak for this frontend. 
            Assumption: 'beef-admin' or 'admin-cli'. 
            If it fails, USER needs to provide correct client id.
  RedirectUri: Where Keycloak redirects after login.
*/
const oidcConfig = {
    authority: "http://localhost:8180/realms/master",
    client_id: "beef-admin",
    redirect_uri: window.location.origin,
    onSigninCallback: (_user) => {
        // You can redirect to specific page here if needed
        window.history.replaceState({}, document.title, window.location.pathname);
    },
    monitorSession: false, // Prevent infinite loop checkSession
    automaticSilentRenew: false, // Temporarily disable to stabilize
    loadUserInfo: true,
};

export const AuthWrapper = ({ children }) => {
    return (
        <AuthProvider {...oidcConfig}>
            {children}
        </AuthProvider>
    );
};
