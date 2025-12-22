"use client";

import { useState } from "react";
import { useForm } from "react-hook-form";
import { useRouter } from "next/navigation";
import { useBasket } from "@/app/contexts/BasketContext";
import { checkoutBasket, BasketCheckout } from "@/app/services/basket";
import HeroInnerBlock from "@/app/components/common/hero-inner/Hero-inner";

const CheckoutPage = () => {
    const { items, totalCost, clearBasket } = useBasket();
    const router = useRouter();
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const { register, handleSubmit, formState: { errors } } = useForm<BasketCheckout>();

    const onSubmit = async (data: BasketCheckout) => {
        setIsSubmitting(true);
        setError(null);
        try {
            const response = await checkoutBasket(data);
            if (response.paymentUrl) {
                // In a real app, we might redirect to payment gateway.
                // For this demo, we'll assume success and clear basket.
                // But wait, the backend returns a payment URL.
                // If it's a mock URL or internal, we might handle it.
                // Let's assume we redirect to it.
                window.location.href = response.paymentUrl;
            } else {
                // Fallback if no URL (e.g. cash on delivery or immediate success)
                await clearBasket();
                router.push("/orders");
            }
        } catch (err: any) {
            console.error("Checkout error:", err);
            setError(err.message || "An error occurred during checkout.");
        } finally {
            setIsSubmitting(false);
        }
    };

    if (items.length === 0) {
        return (
            <>
                <HeroInnerBlock
                    title="Checkout"
                    image="/hero/hero-inner-1.webp"
                    altText="Checkout"
                    breadcrumbs={[{ id: 1, title: "Home", link: "/" }, { id: 2, title: "Checkout", link: "/checkout" }]}
                />
                <section className="section-default">
                    <div className="container mx-auto px-4 text-center">
                        <h2 className="text-2xl font-bold mb-4">Your basket is empty</h2>
                        <button onClick={() => router.push("/menu")} className="btn btn__solid">
                            Go to Menu
                        </button>
                    </div>
                </section>
            </>
        );
    }

    return (
        <>
            <HeroInnerBlock
                title="Checkout"
                image="/hero/hero-inner-1.webp"
                altText="Checkout"
                breadcrumbs={[{ id: 1, title: "Home", link: "/" }, { id: 2, title: "Checkout", link: "/checkout" }]}
            />
            <section className="section-default">
                <div className="container mx-auto px-4">
                    <div className="grid grid-cols-1 lg:grid-cols-2 gap-12">
                        {/* Order Summary */}
                        <div>
                            <h3 className="text-xl font-bold mb-6">Order Summary</h3>
                            <div className="bg-surface-secondary p-6 rounded-lg">
                                {items.map((item) => (
                                    <div key={item.productId} className="flex justify-between mb-4 border-b border-border-default pb-4 last:border-0">
                                        <div>
                                            <p className="font-medium">{item.productName}</p>
                                            <p className="text-sm text-text-muted">Qty: {item.units}</p>
                                        </div>
                                        <p className="font-medium">${(item.unitPrice * item.units).toFixed(2)}</p>
                                    </div>
                                ))}
                                <div className="flex justify-between mt-4 pt-4 border-t border-border-default font-bold text-lg">
                                    <span>Total</span>
                                    <span>${totalCost.toFixed(2)}</span>
                                </div>
                            </div>
                        </div>

                        {/* Checkout Form */}
                        <div>
                            <h3 className="text-xl font-bold mb-6">Shipping Details</h3>
                            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">

                                <div className="grid grid-cols-2 gap-4">
                                    <div>
                                        <label className="block text-sm font-medium mb-1">City</label>
                                        <input
                                            {...register("city", { required: "City is required" })}
                                            placeholder="Enter your city"
                                            className="w-full p-3 border border-zinc-300 dark:border-zinc-600 rounded-md transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-amber-500 bg-white dark:bg-zinc-800 text-zinc-900 dark:text-white placeholder:text-zinc-400 dark:placeholder:text-zinc-500"
                                        />
                                        {errors.city && <p className="text-red-500 text-sm">{errors.city.message}</p>}
                                    </div>
                                    <div>
                                        <label className="block text-sm font-medium mb-1">State</label>
                                        <input
                                            {...register("state", { required: "State is required" })}
                                            placeholder="Enter your state"
                                            className="w-full p-3 border border-zinc-300 dark:border-zinc-600 rounded-md transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-amber-500 bg-white dark:bg-zinc-800 text-zinc-900 dark:text-white placeholder:text-zinc-400 dark:placeholder:text-zinc-500"
                                        />
                                        {errors.state && <p className="text-red-500 text-sm">{errors.state.message}</p>}
                                    </div>
                                </div>
                                <div>
                                    <label className="block text-sm font-medium mb-1">Street Address</label>
                                    <input
                                        {...register("street", { required: "Street is required" })}
                                        placeholder="Enter your street address"
                                        className="w-full p-3 border border-zinc-300 dark:border-zinc-600 rounded-md transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-amber-500 bg-white dark:bg-zinc-800 text-zinc-900 dark:text-white placeholder:text-zinc-400 dark:placeholder:text-zinc-500"
                                    />
                                    {errors.street && <p className="text-red-500 text-sm">{errors.street.message}</p>}
                                </div>
                                <div>
                                    <label className="block text-sm font-medium mb-1">Country</label>
                                    <input
                                        {...register("country", { required: "Country is required" })}
                                        placeholder="Enter your country"
                                        className="w-full p-3 border border-zinc-300 dark:border-zinc-600 rounded-md transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-amber-500 bg-white dark:bg-zinc-800 text-zinc-900 dark:text-white placeholder:text-zinc-400 dark:placeholder:text-zinc-500"
                                    />
                                    {errors.country && <p className="text-red-500 text-sm">{errors.country.message}</p>}
                                </div>

                                {error && <div className="text-red-500 text-sm mt-2">{error}</div>}

                                <button
                                    type="submit"
                                    disabled={isSubmitting}
                                    className="btn btn__solid w-full mt-6"
                                >
                                    {isSubmitting ? "Processing..." : "Place Order"}
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </section>
        </>
    );
};

export default CheckoutPage;
