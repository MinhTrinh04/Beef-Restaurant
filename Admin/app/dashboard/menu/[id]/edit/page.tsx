"use client";

import React, { useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { AdminLayout } from '@/components/layout/AdminLayout';
import { MenuItemForm } from '@/components/menu/MenuItemForm';
import { Loading } from '@/components/ui/Loading';
import { Button } from '@/components/ui/Button';
import { getMenuItemById, updateMenuItem } from '@/lib/api/menu';
import { MenuItem, MenuItemDto } from '@/types';
import { ArrowLeft } from 'lucide-react';

export default function EditMenuItemPage() {
    const params = useParams();
    const router = useRouter();
    const itemId = parseInt(params.id as string);

    const [menuItem, setMenuItem] = useState<MenuItem | null>(null);
    const [loading, setLoading] = useState(true);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (itemId) {
            loadMenuItem();
        }
    }, [itemId]);

    const loadMenuItem = async () => {
        try {
            setLoading(true);
            const data = await getMenuItemById(itemId);
            setMenuItem(data);
            setError(null);
        } catch (err: any) {
            setError(err.message || 'Failed to load menu item');
            console.error('Error loading menu item:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = async (data: MenuItemDto) => {
        try {
            setIsSubmitting(true);
            setError(null);
            await updateMenuItem(itemId, data);
            router.push('/dashboard/menu');
        } catch (err: any) {
            setError(err.message || 'Failed to update menu item');
            console.error('Error updating menu item:', err);
        } finally {
            setIsSubmitting(false);
        }
    };

    if (loading) {
        return (
            <AdminLayout>
                <div className="flex items-center justify-center h-96">
                    <Loading size="lg" text="Loading menu item..." />
                </div>
            </AdminLayout>
        );
    }

    if (error && !menuItem) {
        return (
            <AdminLayout>
                <div className="flex items-center justify-center h-96">
                    <div className="text-center">
                        <p className="text-red-500 text-lg mb-4">{error}</p>
                        <Button onClick={() => router.push('/dashboard/menu')}>
                            Back to Menu
                        </Button>
                    </div>
                </div>
            </AdminLayout>
        );
    }

    return (
        <AdminLayout>
            <div className="max-w-4xl space-y-6">
                {/* Header */}
                <div className="flex items-center gap-4">
                    <Button
                        variant="ghost"
                        onClick={() => router.push('/dashboard/menu')}
                        className="!p-2"
                    >
                        <ArrowLeft size={20} />
                    </Button>
                    <div>
                        <h1 className="text-3xl font-bold text-text-base font-barlow-condensed">
                            Edit Menu Item
                        </h1>
                        <p className="text-text-muted mt-1">
                            Update {menuItem?.name}
                        </p>
                    </div>
                </div>

                {/* Error Message */}
                {error && (
                    <div className="bg-red-500/20 border border-red-500 rounded-lg p-4">
                        <p className="text-red-500">{error}</p>
                    </div>
                )}

                {/* Form */}
                {menuItem && (
                    <div className="bg-surface border border-border rounded-lg p-6">
                        <MenuItemForm
                            initialData={{
                                name: menuItem.name,
                                slug: menuItem.slug,
                                description: menuItem.description,
                                price: menuItem.price,
                                categoryId: menuItem.categoryId,
                                imageUrl: menuItem.imageUrl,
                                available: menuItem.available,
                                stockQuantity: menuItem.stockQuantity,
                            }}
                            onSubmit={handleSubmit}
                            submitLabel="Update Menu Item"
                            isSubmitting={isSubmitting}
                        />
                    </div>
                )}
            </div>
        </AdminLayout>
    );
}
