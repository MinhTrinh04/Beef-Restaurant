"use client";

import React, { useEffect, useState } from 'react';
import { AdminLayout } from '@/components/layout/AdminLayout';
import { Loading } from '@/components/ui/Loading';
import { getAllOrders } from '@/lib/api/orders';
import { Order } from '@/types';
import { formatPrice, formatDate, getOrderStatusBadgeColor } from '@/lib/utils';
import { Eye, Search } from 'lucide-react';
import Link from 'next/link';

export default function OrdersPage() {
    const [orders, setOrders] = useState<Order[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        loadOrders();
    }, []);

    const loadOrders = async () => {
        try {
            setLoading(true);
            const data = await getAllOrders();
            setOrders(data);
            setError(null);
        } catch (err: any) {
            setError(err.message || 'Failed to load orders');
            console.error('Error loading orders:', err);
        } finally {
            setLoading(false);
        }
    };

    const filteredOrders = orders.filter((order) => {
        const searchLower = searchTerm.toLowerCase();
        return (
            order.orderId.toString().includes(searchLower) ||
            order.userId.toLowerCase().includes(searchLower) ||
            order.userEmail?.toLowerCase().includes(searchLower) ||
            order.status.toLowerCase().includes(searchLower)
        );
    });

    if (loading) {
        return (
            <AdminLayout>
                <div className="flex items-center justify-center h-96">
                    <Loading size="lg" text="Loading orders..." />
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
                        <button
                            onClick={loadOrders}
                            className="px-4 py-2 bg-primary text-surface-dark rounded-md hover:bg-primary/90"
                        >
                            Retry
                        </button>
                    </div>
                </div>
            </AdminLayout>
        );
    }

    return (
        <AdminLayout>
            <div className="space-y-6">
                {/* Page Header */}
                <div className="flex items-center justify-between">
                    <div>
                        <h1 className="text-3xl font-bold text-text-base font-barlow-condensed">
                            Orders
                        </h1>
                        <p className="text-text-muted mt-1">
                            Manage and view all customer orders
                        </p>
                    </div>
                </div>

                {/* Search */}
                <div className="relative">
                    <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-text-muted" size={20} />
                    <input
                        type="text"
                        placeholder="Search by order ID, user email, or status..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="w-full pl-10 pr-4 py-3 bg-surface border border-border rounded-md text-text-base placeholder:text-text-muted focus:outline-none focus:ring-2 focus:ring-primary"
                    />
                </div>

                {/* Orders Table */}
                <div className="bg-surface border border-border rounded-lg overflow-hidden">
                    <div className="overflow-x-auto">
                        <table className="w-full">
                            <thead className="bg-background border-b border-border">
                                <tr>
                                    <th className="px-6 py-4 text-left text-sm font-semibold text-text-base">
                                        Order ID
                                    </th>
                                    <th className="px-6 py-4 text-left text-sm font-semibold text-text-base">
                                        User
                                    </th>
                                    <th className="px-6 py-4 text-left text-sm font-semibold text-text-base">
                                        Items
                                    </th>
                                    <th className="px-6 py-4 text-left text-sm font-semibold text-text-base">
                                        Total
                                    </th>
                                    <th className="px-6 py-4 text-left text-sm font-semibold text-text-base">
                                        Status
                                    </th>
                                    <th className="px-6 py-4 text-left text-sm font-semibold text-text-base">
                                        Date
                                    </th>
                                    <th className="px-6 py-4 text-left text-sm font-semibold text-text-base">
                                        Actions
                                    </th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-border">
                                {filteredOrders.length === 0 ? (
                                    <tr>
                                        <td colSpan={7} className="px-6 py-12 text-center text-text-muted">
                                            {searchTerm ? 'No orders found matching your search' : 'No orders yet'}
                                        </td>
                                    </tr>
                                ) : (
                                    filteredOrders.map((order) => (
                                        <tr key={order.orderId} className="hover:bg-background transition-colors">
                                            <td className="px-6 py-4 text-sm text-text-base font-medium">
                                                #{order.orderId}
                                            </td>
                                            <td className="px-6 py-4 text-sm text-text-base">
                                                {order.userEmail || order.userId}
                                            </td>
                                            <td className="px-6 py-4 text-sm text-text-muted">
                                                {order.items.length} item{order.items.length !== 1 ? 's' : ''}
                                            </td>
                                            <td className="px-6 py-4 text-sm text-text-base font-medium">
                                                {formatPrice(order.totalAmount)}
                                            </td>
                                            <td className="px-6 py-4">
                                                <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getOrderStatusBadgeColor(order.status)}`}>
                                                    {order.status}
                                                </span>
                                            </td>
                                            <td className="px-6 py-4 text-sm text-text-muted">
                                                {formatDate(order.createdAt)}
                                            </td>
                                            <td className="px-6 py-4">
                                                <Link
                                                    href={`/dashboard/orders/${order.orderId}`}
                                                    className="inline-flex items-center gap-1 text-primary hover:text-primary/80 transition-colors"
                                                >
                                                    <Eye size={16} />
                                                    <span className="text-sm">View</span>
                                                </Link>
                                            </td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>

                {/* Summary */}
                {filteredOrders.length > 0 && (
                    <div className="flex items-center justify-between text-sm text-text-muted">
                        <p>Showing {filteredOrders.length} order{filteredOrders.length !== 1 ? 's' : ''}</p>
                        <p>
                            Total Revenue: <span className="text-primary font-medium">
                                {formatPrice(filteredOrders.reduce((sum, order) => sum + order.totalAmount, 0))}
                            </span>
                        </p>
                    </div>
                )}
            </div>
        </AdminLayout>
    );
}
