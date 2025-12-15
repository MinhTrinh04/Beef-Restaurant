"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { useForm } from "react-hook-form";
import { authService } from "../services/auth.service";

export default function RegisterPage() {
  const router = useRouter();
  const [error, setError] = useState<string | null>(null);
  const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm();

  const onSubmit = async (data: any) => {
    setError(null);
    try {
        if (data.password !== data.confirmPassword) {
            setError("Passwords do not match");
            return;
        }

      await authService.register({
        email: data.email,
        password: data.password,
        firstName: data.firstName,
        lastName: data.lastName,
        phoneNumber: data.phoneNumber
      });
      
      router.push("/login?registered=true");
    } catch (err: any) {
      setError(err.response?.data?.message || "Registration failed. Please try again.");
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100 dark:bg-zinc-900 px-4 py-8">
      <div className="w-full max-w-md p-8 bg-white dark:bg-zinc-800 rounded-lg shadow-lg">
        <h2 className="text-2xl font-bold text-center mb-6 text-zinc-900 dark:text-zinc-100">Create Account</h2>
        
        {error && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
            <span className="block sm:inline">{error}</span>
          </div>
        )}

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
                <div>
                    <label className="block mb-1 text-sm font-medium text-zinc-700 dark:text-zinc-300">First Name</label>
                    <input
                    {...register("firstName", { required: "First name is required" })}
                    type="text"
                    className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-amber-500 dark:bg-zinc-700 dark:border-zinc-600 dark:text-white"
                    placeholder="First"
                    />
                    {errors.firstName && <p className="text-red-500 text-xs mt-1">{String(errors.firstName.message)}</p>}
                </div>
                <div>
                    <label className="block mb-1 text-sm font-medium text-zinc-700 dark:text-zinc-300">Last Name</label>
                    <input
                    {...register("lastName", { required: "Last name is required" })}
                    type="text"
                    className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-amber-500 dark:bg-zinc-700 dark:border-zinc-600 dark:text-white"
                    placeholder="Last"
                    />
                    {errors.lastName && <p className="text-red-500 text-xs mt-1">{String(errors.lastName.message)}</p>}
                </div>
          </div>

          <div>
            <label className="block mb-1 text-sm font-medium text-zinc-700 dark:text-zinc-300">Email</label>
            <input
              {...register("email", { required: "Email is required", pattern: { value: /^\S+@\S+$/i, message: "Invalid email" } })}
              type="email"
              className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-amber-500 dark:bg-zinc-700 dark:border-zinc-600 dark:text-white"
              placeholder="Enter your email"
            />
            {errors.email && <p className="text-red-500 text-xs mt-1">{String(errors.email.message)}</p>}
          </div>

          <div>
            <label className="block mb-1 text-sm font-medium text-zinc-700 dark:text-zinc-300">Phone Number</label>
            <input
              {...register("phoneNumber", { required: "Phone number is required" })}
              type="tel"
              className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-amber-500 dark:bg-zinc-700 dark:border-zinc-600 dark:text-white"
              placeholder="Enter your phone number"
            />
             {errors.phoneNumber && <p className="text-red-500 text-xs mt-1">{String(errors.phoneNumber.message)}</p>}
          </div>

          <div>
            <label className="block mb-1 text-sm font-medium text-zinc-700 dark:text-zinc-300">Password</label>
            <input
              {...register("password", { required: "Password is required", minLength: { value: 6, message: "Password must be at least 6 characters" } })}
              type="password"
              className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-amber-500 dark:bg-zinc-700 dark:border-zinc-600 dark:text-white"
              placeholder="Create password"
            />
            {errors.password && <p className="text-red-500 text-xs mt-1">{String(errors.password.message)}</p>}
          </div>
          
           <div>
            <label className="block mb-1 text-sm font-medium text-zinc-700 dark:text-zinc-300">Confirm Password</label>
            <input
              {...register("confirmPassword", { required: "Please confirm your password" })}
              type="password"
              className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-amber-500 dark:bg-zinc-700 dark:border-zinc-600 dark:text-white"
              placeholder="Confirm password"
            />
            {errors.confirmPassword && <p className="text-red-500 text-xs mt-1">{String(errors.confirmPassword.message)}</p>}
          </div>

          <button
            type="submit"
            disabled={isSubmitting}
            className="w-full bg-amber-500 hover:bg-amber-600 text-white font-bold py-2 px-4 rounded transition duration-200"
          >
            {isSubmitting ? "Creating Account..." : "Register"}
          </button>
        </form>

        <div className="mt-6 text-center text-sm">
          <p className="text-zinc-600 dark:text-zinc-400">
            Already have an account?{" "}
            <Link href="/login" className="text-amber-500 hover:text-amber-600 font-medium hover:underline">
              Login here
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}
