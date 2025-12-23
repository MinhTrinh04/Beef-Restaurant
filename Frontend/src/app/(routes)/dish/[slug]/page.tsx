import Image from "next/image";
import DishDetailClient from "./DishDetailClient";
import { MenuItemDto } from "@/app/types/menu.types";

async function fetchDish(slug: string): Promise<MenuItemDto | null> {
    const base = process.env.NEXT_PUBLIC_API_BASE || "";
    // Use gateway route: /api/menu/** (gateway rewrites to /api/v1/menu/**)
    const res = await fetch(`${base}/api/menu/items/slug/${slug}`, {
        next: { revalidate: 60 },
    });
    if (!res.ok) return null;
    return res.json();
}

export default async function DishDetailPage({ params }: { params: { slug: string } }) {
    const item = await fetchDish(params.slug);

    if (!item) {
        return (
            <main className="container mx-auto px-4 py-12">
                <h1 className="text-2xl font-barlow-condensed mb-4">Dish not found</h1>
                <p className="text-text-muted">We couldn&apos;t find that dish. It may have been removed.</p>
            </main>
        );
    }

    // Explicitly serialize the data to ensure it's JSON-serializable
    // This prevents Next.js serialization errors when passing to client components
    const serializedItem: MenuItemDto = {
        id: Number(item.id),
        name: String(item.name),
        description: String(item.description || ""),
        price: Number(item.price),
        slug: String(item.slug),
        image: String(item.image || ""),
        availableStock: Number(item.availableStock),
        reservedStock: Number(item.reservedStock ?? 0),
        menuCategory: Number(item.menuCategory),
    };

    return (
        <main className="container mx-auto px-4 py-12">
            <div className="grid grid-cols-12 gap-8 items-start">
                <div className="col-span-12 md:col-span-5">
                    {serializedItem.image && (
                        <Image
                            src={serializedItem.image}
                            alt={serializedItem.name}
                            width={640}
                            height={800}
                            className="rounded-lg"
                        />
                    )}
                </div>
                <div className="col-span-12 md:col-span-7">
                    <h1 className="dish__title !text-4xl mb-2">{serializedItem.name}</h1>
                    <div className="dish__content !border-b-0 mb-3 pb-0">
                        <span className="dish__price">{Math.round(serializedItem.price)}₫</span>
                    </div>
                    <div className="dish__description mb-6">
                        <span>{serializedItem.description}</span>
                    </div>
                    <div className="text-sm text-text-muted mb-2">In stock: {serializedItem.availableStock}</div>
                    <DishDetailClient item={serializedItem} />
                </div>
            </div>
        </main>
    );
}
