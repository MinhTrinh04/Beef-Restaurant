"use client";

import React, { useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { AdminLayout } from '@/components/layout/AdminLayout';
import { Loading } from '@/components/ui/Loading';
import { Button } from '@/components/ui/Button';
import { getOrderById } from '@/lib/api/orders';
import { Order } from '@/types';
import { formatPrice, formatDate, getOrderStatusBadgeColor } from '@/lib/utils';
import { ArrowLeft, User, ShoppingBag } from 'lucide-react';

export default function OrderDetailPage() {
    const params = useParams();
    const router = useRouter();
    const orderId = parseInt(params.id as string);

    const [order, setOrder] = useState<Order | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (orderId) {
            loadOrder();
        }
    }, [orderId]);

    const loadOrder = async () => {
        try {
            setLoading(true);
            const data = await getOrderById(orderId);
            setOrder(data);
            setError(null);
        } catch (err: any) {
            setError(err.message || 'Failed to load order');
            console.error('Error loading order:', err);
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <AdminLayout>
                <div className="flex items-center justify-center h-96">
                    <Loading size="lg" text="Loading order details..." />
                </div>
            </AdminLayout>
        );
    }

    if (error || !order) {
        return (
            <AdminLayout>
                <div className="flex items-center justify-center h-96">
                    <div className="text-center">
                        <p className="text-red-500 text-lg mb-4">{error || 'Order not found'}</p>
                        <Button onClick={() => router.push('/dashboard/orders')}>
                            Back to Orders
                        </Button>
                    </div>
                </div>
            </AdminLayout>
        );
    }

    return (
        <AdminLayout>
            <div className="space-y-6">
                {/* Header */}
                <div className="flex items-center gap-4">
                    <Button
                        variant="ghost"
                        onClick={() => router.push('/dashboard/orders')}
                        className="!p-2"
                    >
                        <ArrowLeft size={20} />
                    </Button>
                    <div>
                        <h1 className="text-3xl font-bold text-text-base font-barlow-condensed">
                            Order #{order.orderId}
                        </h1>
                        <p className="text-text-muted mt-1">
                            Placed on {formatDate(order.createdAt)}
                        </p>
                    </div>
                </div>

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                    {/* Order Details */}
                    <div className="lg:col-span-2 space-y-6">
                        {/* Items */}
                        <div className="bg-surface border border-border rounded-lg p-6">
                            <div className="flex items-center gap-2 mb-4">
                                <ShoppingBag size={20} className="text-primary" />
                                <h2 className="text-xl font-semibold text-text-base font-barlow-condensed">
                                    Order Items
                                </h2>
                            </div>
                            <div className="space-y-4">
                                {order.items.map((item, index) => (
                                    <div
                                        key={index}
                                        className="flex items-center justify-between py-3 border-b border-border last:border-0"
                                    >
                                        <div className="flex-1">
                                            <p className="font-medium text-text-base">{item.menuItemName}</p>
                                            <p className="text-sm text-text-muted">
                                                {formatPrice(item.unitPrice)} × {item.quantity}
                                            </p>
                                        </div>
                                        <p className="font-semibold text-text-base">
                                            {formatPrice(item.unitPrice * item.quantity)}
                                        </p>
                                    </div>
                                ))}
                            </div>
                            <div className="mt-6 pt-4 border-t border-border">
                                <div className="flex items-center justify-between text-lg font-bold">
                                    <span className="text-text-base">Total</span>
                                    <span className="text-primary">{formatPrice(order.totalAmount)}</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Sidebar */}
                    <div className="space-y-6">
                        {/* Status */}
                        <div className="bg-surface border border-border rounded-lg p-6">
                            <h3 className="text-lg font-semibold text-text-base font-barlow-condensed mb-4">
                                Order Status
                            </h3>
                            <span className={`inline-flex items-center px-3 py-1.5 rounded-full text-sm font-medium ${getOrderStatusBadgeColor(order.status)}`}>
                                {order.status}
                            </span>
                        </div>

                        {/* Customer Info */}
                        <div className="bg-surface border border-border rounded-lg p-6">
                            <div className="flex items-center gap-2 mb-4">
                                <User size={20} className="text-primary" />
                                <h3 className="text-lg font-semibold text-text-base font-barlow-condensed">
                                    Customer
                                </h3>
                            </div>
                            <div className="space-y-2">
                                <div>
                                    <p className="text-sm text-text-muted">User ID</p>
                                    <p className="text-text-base font-medium">{order.userId}</p>
                                </div>
                                {order.userEmail && (
                                    <div>
                                        <p className="text-sm text-text-muted">Email</p>
                                        <p className="text-text-base font-medium">{order.userEmail}</p>
                                    </div>
                                )}
                            </div>
                        </div>

                        {/* Timestamps */}
                        <div className="bg-surface border border-border rounded-lg p-6">
                            <h3 className="text-lg font-semibold text-text-base font-barlow-condensed mb-4">
                                Timeline
                            </h3>
                            <div className="space-y-3">
                                <div>
                                    <p className="text-sm text-text-muted">Created</p>
                                    <p className="text-text-base text-sm">{formatDate(order.createdAt)}</p>
                                </div>
                                <div>
                                    <p className="text-sm text-text-muted">Last Updated</p>
                                    <p className="text-text-base text-sm">{formatDate(order.updatedAt)}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </AdminLayout>
    );
}
