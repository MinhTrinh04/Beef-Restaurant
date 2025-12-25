"use client";

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import { AdminLayout } from '@/components/layout/AdminLayout';
import { MenuItemForm } from '@/components/menu/MenuItemForm';
import { createMenuItem } from '@/lib/api/menu';
import { MenuItemDto } from '@/types';
import { ArrowLeft } from 'lucide-react';
import { Button } from '@/components/ui/Button';

export default function NewMenuItemPage() {
    const router = useRouter();
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleSubmit = async (data: MenuItemDto) => {
        try {
            setIsSubmitting(true);
            setError(null);
            await createMenuItem(data);
            router.push('/dashboard/menu');
        } catch (err: any) {
            setError(err.message || 'Failed to create menu item');
            console.error('Error creating menu item:', err);
        } finally {
            setIsSubmitting(false);
        }
    };

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
                            Add New Menu Item
                        </h1>
                        <p className="text-text-muted mt-1">
                            Create a new item for the restaurant menu
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
                <div className="bg-surface border border-border rounded-lg p-6">
                    <MenuItemForm
                        onSubmit={handleSubmit}
                        submitLabel="Create Menu Item"
                        isSubmitting={isSubmitting}
                    />
                </div>
            </div>
        </AdminLayout>
    );
}
