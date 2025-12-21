import NextAuth, { AuthOptions } from "next-auth";
import KeycloakProvider from "next-auth/providers/keycloak";
import CredentialsProvider from "next-auth/providers/credentials";

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
        if (!credentials?.email || !credentials?.password) return null;

        try {
          // Adjust the URL to your Backend API address
          const res = await fetch("http://localhost:8080/api/v1/users/login", {
            method: "POST",
            body: JSON.stringify({
              email: credentials.email,
              password: credentials.password,
            }),
            headers: { "Content-Type": "application/json" },
          });

          const data = await res.json();

          if (res.ok && data.success) {
            // Mapping LoginResponse to User object expected by NextAuth
            return {
              id: data.data.userProfile.email, // using email as ID
              name: `${data.data.userProfile.firstName} ${data.data.userProfile.lastName}`,
              email: data.data.userProfile.email,
              accessToken: data.data.accessToken,
              refreshToken: data.data.refreshToken,
            };
          }
          return null;
        } catch (e) {
            console.error(e);
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
