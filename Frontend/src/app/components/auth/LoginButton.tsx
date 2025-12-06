"use client";

import { signIn, signOut, useSession } from "next-auth/react";
// Checking if I need to create a button or use standard HTML. 
// The project seems to have shadcn/ui or similar structure based on `components.json` and `lib/utils`.
// I'll stick to standard HTML button with classes for now to be safe, or check for Button component.
// Actually, let's use standard button with tailwind classes to avoid dependency issues if Button component is not exactly where I expect.

export default function LoginButton() {
  const { data: session } = useSession();

  if (session) {
    return (
      <div className="flex items-center gap-4">
        <span className="text-sm font-medium hidden md:block">
          Hello, {session.user?.name}
        </span>
        <button
          onClick={() => signOut()}
          className="text-sm font-medium hover:underline"
        >
          Logout
        </button>
      </div>
    );
  }

  return (
    <button
      onClick={() => {
        console.log("Login button clicked");
        signIn("keycloak", { callbackUrl: "/" })
          .then((res) => console.log("signIn result:", res))
          .catch((err) => console.error("signIn error:", err));
      }}
      className="text-sm font-medium hover:underline"
    >
      Login
    </button>
  );
}
