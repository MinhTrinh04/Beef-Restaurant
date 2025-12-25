"use client";

import { useState } from "react";
import { signIn, useSession } from "next-auth/react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { useForm } from "react-hook-form";

export default function LoginPage() {
  const router = useRouter();
  const [error, setError] = useState<string | null>(null);
  const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm();
  const { update } = useSession();


  const onSubmit = async (data: any) => {
    setError(null);
    try {
      console.log("🔑 Submitting login form for:", data.email);

      const result = await signIn("credentials", {
        redirect: false,
        email: data.email,
        password: data.password,
      });

      console.log("📋 SignIn result:", result);

      // Check if login was successful
      // NextAuth returns { ok: true } on success, { error: "..." } on failure
      if (result?.ok) {
        console.log("✅ Login successful, updating session...");

        // Update the session to get the latest data
        await update();

        console.log("✅ Session updated, redirecting to home...");

        // Redirect to home page
        router.push("/");
        router.refresh(); // Force refresh to update UI
      } else {
        console.error("❌ Login failed with error:", result?.error);
        // Always show user-friendly Vietnamese message for any login error
        setError("Email hoặc mật khẩu không chính xác");
      }
    } catch (err) {
      console.error("💥 Unexpected error during login:", err);
      // Show same message for unexpected errors for security
      setError("Email hoặc mật khẩu không chính xác");
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100 dark:bg-zinc-900 px-4">
      <div className="w-full max-w-md p-8 bg-white dark:bg-zinc-800 rounded-lg shadow-lg">
        <h2 className="text-2xl font-bold text-center mb-6 text-zinc-900 dark:text-zinc-100">Welcome Back</h2>

        {error && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
            <span className="block sm:inline">{error}</span>
          </div>
        )}

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
          <div>
            <label className="block mb-1 text-sm font-medium text-zinc-700 dark:text-zinc-300">Email</label>
            <input
              {...register("email", { required: "Email is required" })}
              type="email"
              className="w-full px-4 py-2 border border-zinc-300 rounded-md transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-amber-500 dark:bg-zinc-700 dark:border-zinc-600 dark:text-white dark:focus:border-amber-500"
              placeholder="Enter your email"
            />
            {errors.email && <p className="text-red-500 text-xs mt-1">{String(errors.email.message)}</p>}
          </div>

          <div>
            <label className="block mb-1 text-sm font-medium text-zinc-700 dark:text-zinc-300">Password</label>
            <input
              {...register("password", { required: "Password is required" })}
              type="password"
              className="w-full px-4 py-2 border border-zinc-300 rounded-md transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-amber-500 dark:bg-zinc-700 dark:border-zinc-600 dark:text-white dark:focus:border-amber-500"
              placeholder="Enter your password"
            />
            {errors.password && <p className="text-red-500 text-xs mt-1">{String(errors.password.message)}</p>}
          </div>

          <button
            type="submit"
            disabled={isSubmitting}
            className="w-full bg-amber-500 hover:bg-amber-600 text-white font-bold py-2 px-4 rounded transition duration-200"
          >
            {isSubmitting ? "Logging in..." : "Login"}
          </button>
        </form>

        <div className="my-6 flex items-center">
          <div className="flex-grow border-t border-zinc-300 dark:border-zinc-600"></div>
          <span className="flex-shrink-0 mx-4 text-zinc-500 text-sm">Or</span>
          <div className="flex-grow border-t border-zinc-300 dark:border-zinc-600"></div>
        </div>

        {/* <button
          onClick={() => signIn("keycloak", { callbackUrl: "/" })}
          className="w-full bg-zinc-800 hover:bg-zinc-700 text-white font-bold py-2 px-4 rounded transition duration-200 flex items-center justify-center gap-2"
        >
          <span>Sign in with SSO</span>
        </button> */}

        <div className="mt-6 text-center text-sm">
          <p className="text-zinc-600 dark:text-zinc-400">
            Don&apos;t have an account?{" "}
            <Link href="/register" className="text-amber-500 hover:text-amber-600 font-medium hover:underline">
              Register here
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}
