import http from "@/lib/http";

// Assumed basket shape based on common e-shop conventions
export interface BasketItem {
    productId: string | number;
    productName: string;
    unitPrice: number;
    quantity: number;
}

export interface BasketDto {
    buyerId?: string;
    items: BasketItem[];
}

export async function getBasket(): Promise<BasketDto> {
    // Use gateway route: /api/basket/** (gateway rewrites to /api/v1/basket/**)
    const { data } = await http.get<BasketDto>("/api/basket");
    return data ?? { items: [] };
}

export async function updateBasket(basket: BasketDto): Promise<void> {
    // Use gateway route: /api/basket/** (gateway rewrites to /api/v1/basket/**)
    await http.post("/api/basket", basket);
}

export async function addDishToBasket(params: {
    id: string | number;
    title: string;
    price: string; // comes as formatted string in UI
    quantity?: number;
}): Promise<void> {
    const { id, title, price } = params;
    const quantity = Math.max(1, params.quantity ?? 1);

    const unitPrice = parsePrice(price);

    const basket = await getBasket();
    const items = Array.isArray(basket.items) ? [...basket.items] : [];

    const idx = items.findIndex((i) => String(i.productId) === String(id));
    if (idx >= 0) {
        items[idx] = {
            ...items[idx],
            quantity: items[idx].quantity + quantity,
            unitPrice, // keep latest unit price
        };
    } else {
        items.push({
            productId: id,
            productName: title,
            unitPrice,
            quantity,
        });
    }

    await updateBasket({ items });
}

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
