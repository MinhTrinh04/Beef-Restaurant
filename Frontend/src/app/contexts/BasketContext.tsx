"use client";

import { createContext, useCallback, useContext, useEffect, useMemo, useState, type ReactNode } from "react";
import { useSession } from "next-auth/react";
import {
    addDishToBasket as addDishService,
    BasketItem,
    getBasket,
    removeBasketItem,
    updateBasketItemQuantity,
} from "@/app/services/basket";
import BasketToast from "@/app/components/common/basket/BasketToast";

type AddDishPayload = Parameters<typeof addDishService>[0];

type ToastVariant = "success" | "error";

interface BasketToastState {
    id: number;
    message: string;
    detail?: string;
    variant: ToastVariant;
}

interface BasketContextValue {
    items: BasketItem[];
    isLoading: boolean;
    isMutating: boolean;
    totalItems: number;
    totalCost: number;
    addDish: (payload: AddDishPayload) => Promise<void>;
    updateQuantity: (productId: number, units: number) => Promise<void>;
    removeItem: (productId: number) => Promise<void>;
    refresh: (options?: { silent?: boolean }) => Promise<void>;
    toast: BasketToastState | null;
    dismissToast: () => void;
}

const BasketContext = createContext<BasketContextValue | undefined>(undefined);

const createToast = (
    message: string,
    detail: string | undefined,
    variant: ToastVariant = "success"
): BasketToastState => ({
    id: Date.now(),
    message,
    detail,
    variant,
});

export const BasketProvider = ({ children }: { children: ReactNode }) => {
    const [items, setItems] = useState<BasketItem[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [isMutating, setIsMutating] = useState<boolean>(false);
    const [toast, setToast] = useState<BasketToastState | null>(null);

    const refresh = useCallback(async (options?: { silent?: boolean; token?: string }) => {
        const silent = options?.silent ?? false;
        if (!silent) {
            setIsLoading(true);
        }
        try {
            const basket = await getBasket(options?.token);
            setItems(Array.isArray(basket.items) ? basket.items : []);
        } catch (error) {
            console.error("Failed to load basket", error);
            setToast(createToast("Couldn't load your basket", "Please try again.", "error"));
        } finally {
            if (!silent) {
                setIsLoading(false);
            }
        }
    }, []);

    const { data: session, status } = useSession();

    useEffect(() => {
        if (status === "loading") return;

        if (status === "authenticated" && session?.accessToken) {
            refresh({ token: session.accessToken });
        } else {
            setItems([]);
            setIsLoading(false);
        }
    }, [refresh, status, session]);

    const withMutation = useCallback(
        async (mutator: () => Promise<void>, success?: BasketToastState) => {
            if (status !== "authenticated") {
                setToast(createToast("Please log in", "You need to be logged in to modify your basket", "error"));
                return;
            }
            setIsMutating(true);
            try {
                await mutator();
                await refresh({ silent: true, token: session?.accessToken });
                if (success) {
                    setToast(success);
                }
            } catch (error: any) {
                console.error("Basket operation failed", error);
                const errorMessage = error.response?.data?.message || error.message || "Unknown error";
                setToast(createToast("Basket update failed", errorMessage, "error"));
            } finally {
                setIsMutating(false);
            }
        },
        [refresh, status, session]
    );

    const addDish = useCallback(
        async (payload: AddDishPayload) => {
            const quantity = Math.max(1, payload.quantity ?? 1);
            await withMutation(
                () => addDishService(payload),
                createToast("Added to basket", `${quantity} × ${payload.title}`)
            );
        },
        [withMutation]
    );

    const updateQuantity = useCallback(
        async (productId: number, units: number) => {
            await withMutation(
                () => updateBasketItemQuantity(productId, units),
                createToast("Basket updated", units > 0 ? `Quantity set to ${units}` : "Item removed")
            );
        },
        [withMutation]
    );

    const removeItem = useCallback(
        async (productId: number) => {
            await withMutation(() => removeBasketItem(productId), createToast("Removed from basket", undefined));
        },
        [withMutation]
    );

    const dismissToast = useCallback(() => {
        setToast(null);
    }, []);

    const { totalItems, totalCost } = useMemo(() => {
        const totals = items.reduce(
            (acc, item) => {
                acc.totalItems += item.units ?? 0;
                acc.totalCost += (item.unitPrice ?? 0) * (item.units ?? 0);
                return acc;
            },
            { totalItems: 0, totalCost: 0 }
        );
        return totals;
    }, [items]);

    const value = useMemo(
        () => ({
            items,
            isLoading,
            isMutating,
            totalItems,
            totalCost,
            addDish,
            updateQuantity,
            removeItem,
            refresh,
            toast,
            dismissToast,
        }),
        [
            items,
            isLoading,
            isMutating,
            totalItems,
            totalCost,
            addDish,
            updateQuantity,
            removeItem,
            refresh,
            toast,
            dismissToast,
        ]
    );

    return (
        <BasketContext.Provider value={value}>
            {children}
            <BasketToast toast={toast} onDismiss={dismissToast} />
        </BasketContext.Provider>
    );
};

export const useBasket = (): BasketContextValue => {
    const context = useContext(BasketContext);
    if (!context) {
        throw new Error("useBasket must be used within a BasketProvider");
    }
    return context;
};
