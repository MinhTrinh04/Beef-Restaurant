"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { getOrderDetail, cancelOrder, OrderDto } from "@/app/services/order";
import HeroInnerBlock from "@/app/components/common/hero-inner/Hero-inner";
import Link from "next/link";

const OrderDetailPage = () => {
    const params = useParams();
    const router = useRouter();
    const orderId = params.id as string;
    const [order, setOrder] = useState<OrderDto | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [isCancelling, setIsCancelling] = useState(false);

    useEffect(() => {
        if (!orderId) return;

        const fetchOrder = async () => {
            try {
                const data = await getOrderDetail(orderId);
                setOrder(data);
            } catch (err: any) {
                setError("Failed to load order details.");
                console.error(err);
            } finally {
                setIsLoading(false);
            }
        };

        fetchOrder();
    }, [orderId]);

    const handleCancel = async () => {
        if (!confirm("Are you sure you want to cancel this order?")) return;
        
        setIsCancelling(true);
        try {
            await cancelOrder(orderId);
            // Refresh order details
            const data = await getOrderDetail(orderId);
            setOrder(data);
            alert("Order cancelled successfully.");
        } catch (err: any) {
            alert("Failed to cancel order: " + (err.response?.data?.message || err.message));
        } finally {
            setIsCancelling(false);
        }
    };

    if (isLoading) return <div className="p-8 text-center">Loading...</div>;
    if (error || !order) return <div className="p-8 text-center text-red-500">{error || "Order not found"}</div>;

    return (
        <>
            <HeroInnerBlock
                title={`Order #${order.orderNumber}`}
                image="/hero/hero-inner-1.jpg"
                altText="Order Details"
                breadcrumbs={[
                    { id: 1, title: "Home", link: "/" },
                    { id: 2, title: "My Orders", link: "/orders" },
                    { id: 3, title: `#${order.orderNumber}`, link: `/orders/${orderId}` }
                ]}
            />
            <section className="section-default">
                <div className="container mx-auto px-4">
                    <div className="bg-surface-secondary p-6 rounded-lg mb-8">
                        <div className="flex flex-wrap justify-between items-start gap-4 mb-6">
                            <div>
                                <h2 className="text-2xl font-bold mb-2">Order Status: <span className="text-primary">{order.status}</span></h2>
                                <p className="text-text-muted">Placed on {new Date(order.date).toLocaleString()}</p>
                            </div>
                            {order.status === 'Submitted' && (
                                <button 
                                    onClick={handleCancel} 
                                    disabled={isCancelling}
                                    className="btn btn__outline border-red-500 text-red-500 hover:bg-red-50"
                                >
                                    {isCancelling ? "Cancelling..." : "Cancel Order"}
                                </button>
                            )}
                        </div>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                            <div>
                                <h3 className="font-bold mb-2">Shipping Address</h3>
                                <p>{order.street}</p>
                                <p>{order.city}, {order.state} {order.zipCode}</p>
                                <p>{order.country}</p>
                            </div>
                            <div>
                                <h3 className="font-bold mb-2">Order Summary</h3>
                                <p className="flex justify-between"><span>Subtotal:</span> <span>${order.total.toFixed(2)}</span></p>
                                <p className="flex justify-between font-bold mt-2 pt-2 border-t border-border-default"><span>Total:</span> <span>${order.total.toFixed(2)}</span></p>
                            </div>
                        </div>
                    </div>

                    <h3 className="text-xl font-bold mb-4">Items</h3>
                    <div className="space-y-4">
                        {order.orderItems.map((item) => (
                            <div key={item.productId} className="flex items-center gap-4 p-4 border border-border-default rounded bg-surface-primary">
                                <div className="w-16 h-16 bg-gray-200 rounded overflow-hidden flex-shrink-0">
                                    {item.pictureUrl && <img src={item.pictureUrl} alt={item.productName} className="w-full h-full object-cover" />}
                                </div>
                                <div className="flex-grow">
                                    <h4 className="font-bold">{item.productName}</h4>
                                    <p className="text-sm text-text-muted">${item.unitPrice.toFixed(2)} x {item.units}</p>
                                </div>
                                <div className="font-bold">
                                    ${(item.unitPrice * item.units).toFixed(2)}
                                </div>
                            </div>
                        ))}
                    </div>
                    
                    <div className="mt-8">
                        <Link href="/orders" className="text-primary hover:underline">&larr; Back to My Orders</Link>
                    </div>
                </div>
            </section>
        </>
    );
};

export default OrderDetailPage;
