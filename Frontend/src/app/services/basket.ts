import http from "@/lib/http";

/**
 * BasketItem matching backend structure
 */
export interface BasketItem {
    productId: number;
    productName: string;
    unitPrice: number;
    units: number; // Backend uses "units" not "quantity"
    pictureUrl?: string;
}

/**
 * Basket matching backend structure
 */
export interface Basket {
    buyerId?: string;
    items: BasketItem[];
}

/**
 * Response DTO from backend
 */
export interface ResponseDto {
    statusCode: string;
    statusMessage: string;
}

/**
 * Payment URL response from checkout
 */
export interface PaymentUrlResponse {
    paymentUrl: string;
}

/**
 * Basket checkout request (matching backend BasketCheckout model)
 * Note: userEmail is extracted from JWT token by backend, not sent in request
 */
export interface BasketCheckout {
    city: string;
    street: string;
    state: string;
    country: string;
}

/**
 * Get basket from backend
 * Returns empty basket if not found (backend throws exception if basket doesn't exist)
 */
export async function getBasket(token?: string): Promise<Basket> {
    try {
        const config = token ? { headers: { Authorization: `Bearer ${token}` } } : {};
        // Use gateway route: /api/basket/** (gateway rewrites to /api/v1/basket/**)
        const { data } = await http.get<Basket>("/api/basket", config);
        return data ?? { items: [] };
    } catch (error: any) {
        const status = error.response?.status;

        // If basket doesn't exist (404), return empty basket
        if (status === 404) {
            return { items: [] };
        }

        // If server error (500), might be Redis connection issue or other server problem
        // Return empty basket to allow app to continue functioning
        if (status === 500) {
            console.warn(
                "Basket service returned 500 error. This might indicate a server issue (e.g., Redis not running). Returning empty basket."
            );
            return { items: [] };
        }

        // Log other errors for debugging
        console.error("Error fetching basket:", error.response?.data || error.message);
        throw error;
    }
}

/**
 * Update or create basket in backend
 */
export async function updateBasket(basket: Basket): Promise<ResponseDto> {
    try {
        // Use gateway route: /api/basket/** (gateway rewrites to /api/v1/basket/**)
        const { data } = await http.post<ResponseDto>("/api/basket", basket);
        return data;
    } catch (error: any) {
        console.error("Error updating basket:", error.response?.data || error.message);
        throw error;
    }
}

/**
 * Delete basket from backend
 */
export async function deleteBasket(): Promise<ResponseDto> {
    const { data } = await http.delete<ResponseDto>("/api/basket");
    return data;
}

/**
 * Add a dish to the basket
 */
export async function addDishToBasket(params: {
    id: string | number;
    title: string;
    price: string; // comes as formatted string in UI
    quantity?: number;
    image?: string; // optional image URL
}): Promise<void> {
    const { id, title, price, image } = params;
    const units = Math.max(1, params.quantity ?? 1);

    const unitPrice = parsePrice(price);
    const productId = typeof id === "string" ? parseInt(id, 10) : id;

    // Get current basket
    const basket = await getBasket();
    const items = Array.isArray(basket.items) ? [...basket.items] : [];

    // Find existing item
    const idx = items.findIndex((i) => i.productId === productId);

    if (idx >= 0) {
        // Update existing item - add to units
        items[idx] = {
            ...items[idx],
            units: items[idx].units + units,
            unitPrice, // keep latest unit price
        };
    } else {
        // Add new item
        items.push({
            productId,
            productName: title,
            unitPrice,
            units,
            pictureUrl: image,
        });
    }

    await updateBasket({ items });
}

/**
 * Update item quantity in basket
 */
export async function updateBasketItemQuantity(productId: number, units: number): Promise<void> {
    const basket = await getBasket();
    const items = Array.isArray(basket.items) ? [...basket.items] : [];

    const idx = items.findIndex((i) => i.productId === productId);

    if (idx >= 0) {
        if (units <= 0) {
            // Remove item if quantity is 0 or less
            items.splice(idx, 1);
        } else {
            // Update quantity
            items[idx] = {
                ...items[idx],
                units,
            };
        }
        await updateBasket({ items });
    }
}

/**
 * Remove item from basket
 */
export async function removeBasketItem(productId: number): Promise<void> {
    await updateBasketItemQuantity(productId, 0);
}

/**
 * Checkout basket and get payment URL
 */
export async function checkoutBasket(checkout: BasketCheckout, requestId?: string): Promise<PaymentUrlResponse> {
    const headers: Record<string, string> = {};
    if (requestId) {
        headers["X-Request-Id"] = requestId;
    }

    const { data } = await http.post<PaymentUrlResponse>("/api/basket/checkout", checkout, { headers });
    return data;
}

/**
 * Parse price string to number
 */
function parsePrice(raw: string): number {
    // Strip currency symbols and locale separators, keep digits and dot/comma
    const cleaned = raw.replace(/[^0-9.,-]/g, "");
    // Convert comma decimal to dot if needed
    const normalized =
        cleaned.indexOf(",") > -1 && cleaned.indexOf(".") === -1
            ? cleaned.replace(/,/g, ".")
            : cleaned.replace(/,/g, "");
    const n = Number(normalized);
    return Number.isFinite(n) ? n : 0;
}
