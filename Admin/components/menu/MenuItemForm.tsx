"use client";

import React from 'react';
import { useForm } from 'react-hook-form';
import { Input } from '@/components/ui/Input';
import { Textarea } from '@/components/ui/Textarea';
import { Button } from '@/components/ui/Button';
import { MenuItemDto } from '@/types';
import { generateSlug } from '@/lib/utils';

interface MenuItemFormProps {
    initialData?: MenuItemDto;
    onSubmit: (data: MenuItemDto) => Promise<void>;
    submitLabel?: string;
    isSubmitting?: boolean;
}

export function MenuItemForm({
    initialData,
    onSubmit,
    submitLabel = 'Save',
    isSubmitting = false,
}: MenuItemFormProps) {
    const {
        register,
        handleSubmit,
        setValue,
        watch,
        formState: { errors },
    } = useForm<MenuItemDto>({
        defaultValues: initialData || {
            name: '',
            slug: '',
            description: '',
            price: 0,
            categoryId: 1,
            imageUrl: '',
            available: true,
            stockQuantity: 0,
        },
    });

    const name = watch('name');

    // Auto-generate slug from name
    const handleNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const newName = e.target.value;
        if (!initialData) {
            // Only auto-generate slug for new items
            setValue('slug', generateSlug(newName));
        }
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {/* Name */}
                <div>
                    <Input
                        label="Name *"
                        {...register('name', { required: 'Name is required' })}
                        onChange={(e) => {
                            register('name').onChange(e);
                            handleNameChange(e);
                        }}
                        error={errors.name?.message}
                        placeholder="e.g., Beef Steak"
                    />
                </div>

                {/* Slug */}
                <div>
                    <Input
                        label="Slug *"
                        {...register('slug', { required: 'Slug is required' })}
                        error={errors.slug?.message}
                        placeholder="e.g., beef-steak"
                    />
                </div>

                {/* Price */}
                <div>
                    <Input
                        label="Price (VND) *"
                        type="number"
                        {...register('price', {
                            required: 'Price is required',
                            min: { value: 0, message: 'Price must be positive' },
                        })}
                        error={errors.price?.message}
                        placeholder="e.g., 250000"
                    />
                </div>

                {/* Stock Quantity */}
                <div>
                    <Input
                        label="Stock Quantity *"
                        type="number"
                        {...register('stockQuantity', {
                            required: 'Stock quantity is required',
                            min: { value: 0, message: 'Stock must be non-negative' },
                        })}
                        error={errors.stockQuantity?.message}
                        placeholder="e.g., 50"
                    />
                </div>

                {/* Category ID */}
                <div>
                    <label className="block text-sm font-medium text-text-base mb-1.5">
                        Category *
                    </label>
                    <select
                        {...register('categoryId', { required: 'Category is required' })}
                        className="w-full px-4 py-2 bg-surface border border-border rounded-md text-text-base focus:outline-none focus:ring-2 focus:ring-primary"
                    >
                        <option value={1}>Appetizers</option>
                        <option value={2}>Main Course</option>
                        <option value={3}>Desserts</option>
                        <option value={4}>Beverages</option>
                    </select>
                    {errors.categoryId && (
                        <p className="mt-1 text-sm text-red-500">{errors.categoryId.message}</p>
                    )}
                </div>

                {/* Available */}
                <div className="flex items-center gap-3 pt-8">
                    <input
                        type="checkbox"
                        id="available"
                        {...register('available')}
                        className="w-5 h-5 text-primary bg-surface border-border rounded focus:ring-primary focus:ring-2"
                    />
                    <label htmlFor="available" className="text-sm font-medium text-text-base">
                        Available for order
                    </label>
                </div>
            </div>

            {/* Description */}
            <div>
                <Textarea
                    label="Description *"
                    {...register('description', { required: 'Description is required' })}
                    error={errors.description?.message}
                    placeholder="Describe the menu item..."
                    rows={4}
                />
            </div>

            {/* Image URL */}
            <div>
                <Input
                    label="Image URL"
                    {...register('imageUrl')}
                    error={errors.imageUrl?.message}
                    placeholder="https://example.com/image.jpg"
                />
                <p className="mt-1 text-xs text-text-muted">
                    Optional: Enter a URL to an image for this menu item
                </p>
            </div>

            {/* Submit Button */}
            <div className="flex items-center gap-3 pt-4">
                <Button type="submit" disabled={isSubmitting}>
                    {isSubmitting ? 'Saving...' : submitLabel}
                </Button>
                <Button
                    type="button"
                    variant="secondary"
                    onClick={() => window.history.back()}
                    disabled={isSubmitting}
                >
                    Cancel
                </Button>
            </div>
        </form>
    );
}
