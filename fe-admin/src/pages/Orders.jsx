import { useState, useEffect } from 'react';
import { orderService } from '../services/api';
import { MdVisibility, MdCheckCircle, MdCancel } from 'react-icons/md';

const Orders = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadOrders();
    }, []);

    const loadOrders = async () => {
        try {
            const response = await orderService.getAll();
            // Backend returns ResponseDto { data: [...], ... }
            const orderList = response.data?.data || [];
            const formattedOrders = orderList.map(o => ({
                id: o.orderId,
                customer: o.buyerName || o.userId,
                total: o.totalAmount || 0,
                status: o.orderStatus,
                date: o.orderDate ? new Date(o.orderDate).toLocaleDateString() : 'N/A'
            }));
            setOrders(formattedOrders);
        } catch (error) {
            console.error("Failed to fetch orders", error);
        } finally {
            setLoading(false);
        }
    };

    const getStatusBadge = (status) => {
        let className = 'badge ';
        switch (status.toLowerCase()) {
            case 'completed':
            case 'paid':
                className += 'badge-success';
                break;
            case 'pending':
                className += 'badge-warning';
                break;
            case 'cancelled':
                className += 'badge-danger'; // I need to add badge-danger to global css
                break;
            default:
                className += 'badge-outline';
        }
        return <span className={className}>{status}</span>;
    };

    return (
        <div>
            <h1 className="header-title">Order Management</h1>

            <div className="card table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Customer</th>
                            <th>Date</th>
                            <th>Total</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        {loading ? (
                            <tr><td colSpan="5" style={{ textAlign: 'center' }}>Loading...</td></tr>
                        ) : orders.map(order => (
                            <tr key={order.id}>
                                <td>#{order.id}</td>
                                <td>{order.customer}</td>
                                <td>{order.date}</td>
                                <td>{Math.round(order.total)}</td>
                                <td>{getStatusBadge(order.status)}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default Orders;
