"use client";

import { useState } from "react";
import { signIn, useSession } from "next-auth/react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { Button } from "@/components/ui/Button";
import { Input } from "@/components/ui/Input";

export default function LoginPage() {
    const router = useRouter();
    const [error, setError] = useState<string | null>(null);
    const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm();
    const { update } = useSession();

    const onSubmit = async (data: any) => {
        setError(null);
        try {
            console.log("🔑 Submitting admin login form for:", data.email);

            const result = await signIn("credentials", {
                redirect: false,
                email: data.email,
                password: data.password,
            });

            console.log("📋 SignIn result:", result);

            // Check if login was successful
            if (result?.ok) {
                console.log("✅ Admin login successful, updating session...");

                // Update the session to get the latest data
                await update();

                console.log("✅ Session updated, redirecting to dashboard...");

                // Redirect to dashboard
                router.push("/dashboard");
                router.refresh(); // Force refresh to update UI
            } else {
                console.error("❌ Admin login failed with error:", result?.error);
                setError("Email hoặc mật khẩu không chính xác");
            }
        } catch (err) {
            console.error("💥 Unexpected error during admin login:", err);
            setError("Email hoặc mật khẩu không chính xác");
        }
    };

    return (
        <div className="flex items-center justify-center min-h-screen bg-background px-4">
            <div className="w-full max-w-md p-8 bg-surface border border-border rounded-lg shadow-lg">
                <div className="text-center mb-8">
                    <h1 className="text-3xl font-bold text-text-base font-barlow-condensed mb-2">
                        BEEF ADMIN
                    </h1>
                    <p className="text-text-muted">
                        Đăng nhập vào hệ thống quản trị
                    </p>
                </div>

                {error && (
                    <div className="bg-red-500/20 border border-red-500 text-red-500 px-4 py-3 rounded-md mb-6">
                        <span className="block sm:inline">{error}</span>
                    </div>
                )}

                <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                    <div>
                        <Input
                            label="Email *"
                            type="email"
                            {...register("email", { required: "Email là bắt buộc" })}
                            error={errors.email?.message as string}
                            placeholder="admin@example.com"
                        />
                    </div>

                    <div>
                        <Input
                            label="Mật khẩu *"
                            type="password"
                            {...register("password", { required: "Mật khẩu là bắt buộc" })}
                            error={errors.password?.message as string}
                            placeholder="••••••••"
                        />
                    </div>

                    <Button
                        type="submit"
                        disabled={isSubmitting}
                        className="w-full"
                    >
                        {isSubmitting ? "Đang đăng nhập..." : "Đăng nhập"}
                    </Button>
                </form>

                <div className="mt-6 text-center text-sm text-text-muted">
                    <p>Chỉ dành cho quản trị viên</p>
                </div>
            </div>
        </div>
    );
}
