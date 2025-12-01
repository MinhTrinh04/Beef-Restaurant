"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { getMyOrders, OrderDto } from "@/app/services/order";
import HeroInnerBlock from "@/app/components/common/hero-inner/Hero-inner";

const OrdersPage = () => {
    const [orders, setOrders] = useState<OrderDto[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchOrders = async () => {
            try {
                const data = await getMyOrders();
                setOrders(data);
            } catch (err: any) {
                setError("Failed to load orders.");
                console.error(err);
            } finally {
                setIsLoading(false);
            }
        };

        fetchOrders();
    }, []);

    return (
        <>
            <HeroInnerBlock
                title="My Orders"
                image="/hero/hero-inner-1.jpg"
                altText="My Orders"
                breadcrumbs={[{ id: 1, title: "Home", link: "/" }, { id: 2, title: "My Orders", link: "/orders" }]}
            />
            <section className="section-default">
                <div className="container mx-auto px-4">
                    {isLoading ? (
                        <p>Loading orders...</p>
                    ) : error ? (
                        <p className="text-red-500">{error}</p>
                    ) : orders.length === 0 ? (
                        <div className="text-center">
                            <p className="mb-4">You haven't placed any orders yet.</p>
                            <Link href="/menu" className="btn btn__solid">
                                Browse Menu
                            </Link>
                        </div>
                    ) : (
                        <div className="overflow-x-auto">
                            <table className="w-full text-left border-collapse">
                                <thead>
                                    <tr className="border-b border-border-default">
                                        <th className="p-4">Order #</th>
                                        <th className="p-4">Date</th>
                                        <th className="p-4">Status</th>
                                        <th className="p-4">Total</th>
                                        <th className="p-4">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {orders.map((order) => (
                                        <tr key={order.orderId} className="border-b border-border-default hover:bg-surface-secondary">
                                            <td className="p-4">{order.orderNumber}</td>
                                            <td className="p-4">{new Date(order.date).toLocaleDateString()}</td>
                                            <td className="p-4">
                                                <span className={`px-2 py-1 rounded text-xs font-bold ${
                                                    order.status === 'Submitted' ? 'bg-blue-100 text-blue-800' :
                                                    order.status === 'Paid' ? 'bg-green-100 text-green-800' :
                                                    order.status === 'Cancelled' ? 'bg-red-100 text-red-800' :
                                                    'bg-gray-100 text-gray-800'
                                                }`}>
                                                    {order.status}
                                                </span>
                                            </td>
                                            <td className="p-4">${order.total.toFixed(2)}</td>
                                            <td className="p-4">
                                                <Link href={`/orders/${order.orderId}`} className="text-primary hover:underline">
                                                    View Details
                                                </Link>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    )}
                </div>
            </section>
        </>
    );
};

export default OrdersPage;
