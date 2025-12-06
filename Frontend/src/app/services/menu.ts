import { MenuItemDto } from "@/app/types/menu.types";
import { MenuCategory } from "@/app/types/common.types";

const baseURL = process.env.NEXT_PUBLIC_API_BASE || "http://localhost:9000";

/**
 * Format price to display format
 */
function formatPrice(price: number): string {
    return `$${price.toFixed(2)}`;
}

/**
 * Fetch all menu items from the backend
 */
export async function getAllMenuItems(): Promise<MenuItemDto[]> {
    const res = await fetch(`${baseURL}/api/menu/items`, {
        next: { revalidate: 60 }, // Revalidate every 60 seconds
    });
    
    if (!res.ok) {
        throw new Error(`Failed to fetch menu items: ${res.statusText}`);
    }
    
    return res.json();
}

/**
 * Fetch menu items by category ID
 */
export async function getMenuItemsByCategory(categoryId: number): Promise<MenuItemDto[]> {
    const res = await fetch(`${baseURL}/api/menu/items/category/${categoryId}`, {
        next: { revalidate: 60 },
    });
    
    if (!res.ok) {
        throw new Error(`Failed to fetch menu items for category ${categoryId}: ${res.statusText}`);
    }
    
    return res.json();
}

/**
 * Transform backend MenuItemDto[] into frontend MenuCategory[] format
 * Groups items by their menuCategory field
 */
export function transformMenuItemsToCategories(
    items: MenuItemDto[],
    categoryInfo?: Array<{ id: number; name: string; image?: string; description?: string }>
): MenuCategory[] {
    // Group items by category
    const grouped = items.reduce((acc, item) => {
        const categoryId = item.menuCategory;
        
        if (!acc[categoryId]) {
            acc[categoryId] = [];
        }
        
        acc[categoryId].push({
            id: item.id,
            title: item.name,
            price: formatPrice(item.price),
            description: item.description || "",
        });
        
        return acc;
    }, {} as Record<number, Array<{ id: number; title: string; price: string; description: string }>>);
    
    // Convert to MenuCategory array
    return Object.entries(grouped).map(([categoryId, dishes]) => {
        const catId = Number(categoryId);
        const category = categoryInfo?.find(c => c.id === catId);
        
        return {
            id: catId,
            title: category?.name || `Category ${catId}`,
            phrase: category?.description || "",
            image: category?.image || "/menu/menu-1.jpg", // Default image
            altText: category?.name || `Category ${catId}`,
            dishesList: dishes,
            anchor: `category-${catId}`,
        };
    });
}

/**
 * Fetch all menu items and transform them into categories
 */
export async function getMenuCategories(): Promise<MenuCategory[]> {
    const items = await getAllMenuItems();
    return transformMenuItemsToCategories(items);
}

