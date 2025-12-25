"use client";

import React, { useEffect, useState } from 'react';
import { AdminLayout } from '@/components/layout/AdminLayout';
import { Loading } from '@/components/ui/Loading';
import { Button } from '@/components/ui/Button';
import { Modal } from '@/components/ui/Modal';
import { ToastContainer } from '@/components/ui/Toast';
import { getAllMenuItems, deleteMenuItem } from '@/lib/api/menu';
import { MenuItem } from '@/types';
import { formatPrice } from '@/lib/utils';
import { Plus, Search, Edit, Trash2, Eye } from 'lucide-react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';

export default function MenuPage() {
    const router = useRouter();
    const [menuItems, setMenuItems] = useState<MenuItem[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [deleteModalOpen, setDeleteModalOpen] = useState(false);
    const [itemToDelete, setItemToDelete] = useState<MenuItem | null>(null);
    const [deleting, setDeleting] = useState(false);
    const [toasts, setToasts] = useState<Array<{ id: string; message: string; type: 'success' | 'error' | 'info' | 'warning' }>>([]);

    useEffect(() => {
        loadMenuItems();
    }, []);

    const loadMenuItems = async () => {
        try {
            setLoading(true);
            const data = await getAllMenuItems();
            setMenuItems(data);
            setError(null);
        } catch (err: any) {
            setError(err.message || 'Failed to load menu items');
            console.error('Error loading menu items:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleDeleteClick = (item: MenuItem) => {
        setItemToDelete(item);
        setDeleteModalOpen(true);
    };

    const handleDeleteConfirm = async () => {
        if (!itemToDelete) return;

        try {
            setDeleting(true);
            await deleteMenuItem(itemToDelete.id);
            setMenuItems(menuItems.filter(item => item.id !== itemToDelete.id));
            addToast('Menu item deleted successfully', 'success');
            setDeleteModalOpen(false);
            setItemToDelete(null);
        } catch (err: any) {
            addToast(err.message || 'Failed to delete menu item', 'error');
        } finally {
            setDeleting(false);
        }
    };

    const addToast = (message: string, type: 'success' | 'error' | 'info' | 'warning') => {
        const id = Math.random().toString(36).substr(2, 9);
        setToasts([...toasts, { id, message, type }]);
    };

    const removeToast = (id: string) => {
        setToasts(toasts.filter(toast => toast.id !== id));
    };

    const filteredItems = menuItems.filter((item) => {
        const searchLower = searchTerm.toLowerCase();
        return (
            item.name.toLowerCase().includes(searchLower) ||
            item.description.toLowerCase().includes(searchLower) ||
            item.categoryName?.toLowerCase().includes(searchLower)
        );
    });

    if (loading) {
        return (
            <AdminLayout>
                <div className="flex items-center justify-center h-96">
                    <Loading size="lg" text="Loading menu items..." />
                </div>
            </AdminLayout>
        );
    }

    if (error) {
        return (
            <AdminLayout>
                <div className="flex items-center justify-center h-96">
                    <div className="text-center">
                        <p className="text-red-500 text-lg mb-4">{error}</p>
                        <Button onClick={loadMenuItems}>Retry</Button>
                    </div>
                </div>
            </AdminLayout>
        );
    }

    return (
        <AdminLayout>
            <ToastContainer toasts={toasts} removeToast={removeToast} />

            <div className="space-y-6">
                {/* Page Header */}
                <div className="flex items-center justify-between">
                    <div>
                        <h1 className="text-3xl font-bold text-text-base font-barlow-condensed">
                            Menu Items
                        </h1>
                        <p className="text-text-muted mt-1">
                            Manage restaurant menu items
                        </p>
                    </div>
                    <Link href="/dashboard/menu/new">
                        <Button>
                            <Plus size={20} className="mr-2" />
                            Add New Item
                        </Button>
                    </Link>
                </div>

                {/* Search */}
                <div className="relative">
                    <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-text-muted" size={20} />
                    <input
                        type="text"
                        placeholder="Search menu items..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="w-full pl-10 pr-4 py-3 bg-surface border border-border rounded-md text-text-base placeholder:text-text-muted focus:outline-none focus:ring-2 focus:ring-primary"
                    />
                </div>

                {/* Menu Items Grid */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {filteredItems.length === 0 ? (
                        <div className="col-span-full text-center py-12 text-text-muted">
                            {searchTerm ? 'No menu items found matching your search' : 'No menu items yet'}
                        </div>
                    ) : (
                        filteredItems.map((item) => (
                            <div
                                key={item.id}
                                className="bg-surface border border-border rounded-lg overflow-hidden hover:border-primary transition-all group"
                            >
                                {/* Image */}
                                <div className="aspect-video bg-background relative overflow-hidden">
                                    {item.imageUrl ? (
                                        <img
                                            src={item.imageUrl}
                                            alt={item.name}
                                            className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                                        />
                                    ) : (
                                        <div className="w-full h-full flex items-center justify-center text-text-muted">
                                            No Image
                                        </div>
                                    )}
                                    {!item.available && (
                                        <div className="absolute inset-0 bg-black/60 flex items-center justify-center">
                                            <span className="bg-red-500 text-white px-3 py-1 rounded-full text-sm font-medium">
                                                Unavailable
                                            </span>
                                        </div>
                                    )}
                                </div>

                                {/* Content */}
                                <div className="p-4">
                                    <h3 className="text-lg font-semibold text-text-base font-barlow-condensed mb-1">
                                        {item.name}
                                    </h3>
                                    <p className="text-sm text-text-muted mb-3 line-clamp-2">
                                        {item.description}
                                    </p>
                                    <div className="flex items-center justify-between mb-4">
                                        <span className="text-xl font-bold text-primary">
                                            {formatPrice(item.price)}
                                        </span>
                                        <span className="text-sm text-text-muted">
                                            Stock: {item.stockQuantity}
                                        </span>
                                    </div>

                                    {/* Actions */}
                                    <div className="flex items-center gap-2">
                                        <Link href={`/dashboard/menu/${item.id}/edit`} className="flex-1">
                                            <Button variant="secondary" size="sm" className="w-full">
                                                <Edit size={16} className="mr-1" />
                                                Edit
                                            </Button>
                                        </Link>
                                        <Button
                                            variant="danger"
                                            size="sm"
                                            onClick={() => handleDeleteClick(item)}
                                        >
                                            <Trash2 size={16} />
                                        </Button>
                                    </div>
                                </div>
                            </div>
                        ))
                    )}
                </div>
            </div>

            {/* Delete Confirmation Modal */}
            <Modal
                isOpen={deleteModalOpen}
                onClose={() => !deleting && setDeleteModalOpen(false)}
                title="Delete Menu Item"
                size="sm"
            >
                <div className="space-y-4">
                    <p className="text-text-base">
                        Are you sure you want to delete <strong>{itemToDelete?.name}</strong>?
                        This action cannot be undone.
                    </p>
                    <div className="flex items-center gap-3 justify-end">
                        <Button
                            variant="secondary"
                            onClick={() => setDeleteModalOpen(false)}
                            disabled={deleting}
                        >
                            Cancel
                        </Button>
                        <Button
                            variant="danger"
                            onClick={handleDeleteConfirm}
                            disabled={deleting}
                        >
                            {deleting ? 'Deleting...' : 'Delete'}
                        </Button>
                    </div>
                </div>
            </Modal>
        </AdminLayout>
    );
}
