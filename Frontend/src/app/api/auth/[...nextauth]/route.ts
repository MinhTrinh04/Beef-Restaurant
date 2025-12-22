import NextAuth, { AuthOptions } from "next-auth";
import KeycloakProvider from "next-auth/providers/keycloak";
import CredentialsProvider from "next-auth/providers/credentials";
import { API_ENDPOINTS } from "@/lib/api-config";

export const authOptions: AuthOptions = {
  providers: [
    KeycloakProvider({
      clientId: process.env.KEYCLOAK_CLIENT_ID || "",
      clientSecret: process.env.KEYCLOAK_CLIENT_SECRET || "",
      issuer: process.env.KEYCLOAK_ISSUER,
    }),
    CredentialsProvider({
      name: "Credentials",
      credentials: {
        email: { label: "Email", type: "email" },
        password: { label: "Password", type: "password" }
      },
      async authorize(credentials) {
        if (!credentials?.email || !credentials?.password) {
          console.log("❌ Missing credentials");
          return null;
        }

        try {
          console.log("🔐 Attempting login for:", credentials.email);
          
          // Call through API Gateway using centralized config
          const res = await fetch(API_ENDPOINTS.users.login, {
            method: "POST",
            body: JSON.stringify({
              email: credentials.email,
              password: credentials.password,
            }),
            headers: { "Content-Type": "application/json" },
          });

          const data = await res.json();
          
          // Enhanced logging
          console.log("📡 Login response status:", res.status);
          console.log("📦 Login response data:", JSON.stringify(data, null, 2));

          // Check if response is successful
          if (!res.ok) {
            console.error("❌ HTTP error:", res.status, res.statusText);
            console.error("❌ Error data:", data);
            return null;
          }

          // Validate response structure
          if (!data.success) {
            console.error("❌ API returned success=false:", data.message);
            return null;
          }

          if (!data.data) {
            console.error("❌ Missing data field in response");
            return null;
          }

          // Validate required fields
          const { userProfile, accessToken, refreshToken } = data.data;
          
          if (!userProfile || !accessToken) {
            console.error("❌ Missing required fields in data.data:", { 
              hasUserProfile: !!userProfile, 
              hasAccessToken: !!accessToken 
            });
            return null;
          }

          // Successfully authenticated - return user object
          const user = {
            id: userProfile.keycloakUserId || userProfile.email,
            name: `${userProfile.firstName} ${userProfile.lastName}`,
            email: userProfile.email,
            accessToken: accessToken,
            refreshToken: refreshToken,
          };

          console.log("✅ Login successful for:", user.email);
          console.log("✅ User object:", JSON.stringify(user, null, 2));
          
          return user;
        } catch (e) {
          console.error("💥 Login exception:", e);
          return null;
        }
      }
    }),
  ],
  callbacks: {
    async jwt({ token, account, user }) {
      // 1. First login via Keycloak
      if (account && account.provider === "keycloak") {
        token.accessToken = account.access_token;
        token.idToken = account.id_token;
      }
      // 2. First login via Credentials
      else if (user && account?.provider === "credentials") {
         token.accessToken = user.accessToken;
         token.refreshToken = user.refreshToken;
      }
      return token;
    },
    async session({ session, token }) {
      session.accessToken = token.accessToken as string;
      return session;
    },
  },
  events: {
    async signOut({ token }) {
      if (token.idToken) {
        const issuerUrl = process.env.KEYCLOAK_ISSUER;
        const logOutUrl = new URL(`${issuerUrl}/protocol/openid-connect/logout`);
        logOutUrl.searchParams.set("id_token_hint", token.idToken as string);
        await fetch(logOutUrl);
      }
    },
  },
  pages: {
    signIn: '/login', // Custom login page
  }
};

const handler = NextAuth(authOptions);

export { handler as GET, handler as POST };
