import { AuthOptions } from "next-auth";
import CredentialsProvider from "next-auth/providers/credentials";

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:9000';

export const authOptions: AuthOptions = {
  providers: [
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
          console.log("🔐 Admin login attempt for:", credentials.email);

          // Call Admin Login API
          const res = await fetch(`${API_URL}/api/v1/admin/login`, {
            method: "POST",
            body: JSON.stringify({
              email: credentials.email,
              password: credentials.password,
            }),
            headers: { "Content-Type": "application/json" },
          });

          const data = await res.json();

          console.log("📡 Admin login response status:", res.status);
          console.log("📦 Admin login response data:", JSON.stringify(data, null, 2));

          // Check if response is successful
          if (!res.ok) {
            console.error("❌ Admin login failed - HTTP error:", res.status, res.statusText);
            console.error("❌ Error message from backend:", data.message);
            return null;
          }

          // Validate response structure
          if (!data.success) {
            console.error("❌ Admin login failed - API returned success=false:", data.message);
            return null;
          }

          if (!data.data) {
            console.error("❌ Admin login failed - Missing data field in response");
            return null;
          }

          // Validate required fields
          const { userProfile, accessToken } = data.data;

          if (!userProfile || !accessToken) {
            console.error("❌ Admin login failed - Missing required fields in data.data:", {
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
          };

          console.log("✅ Admin login successful for:", user.email);
          console.log("✅ User object:", JSON.stringify(user, null, 2));

          return user;
        } catch (e) {
          console.error("💥 Admin login exception:", e);
          return null;
        }
      }
    }),
  ],
  callbacks: {
    async jwt({ token, account, user }) {
      // First login via Credentials
      if (user && account?.provider === "credentials") {
        token.accessToken = user.accessToken;
      }
      return token;
    },
    async session({ session, token }) {
      session.accessToken = token.accessToken as string;
      return session;
    },
  },
  pages: {
    signIn: '/login', // Custom login page
  },
  debug: true, // Enable debug logs
  cookies: {
    sessionToken: {
      name: `next-auth.session-token`,
      options: {
        httpOnly: true,
        sameSite: "lax",
        path: "/",
        secure: process.env.NODE_ENV === "production",
      },
    },
  },
};
