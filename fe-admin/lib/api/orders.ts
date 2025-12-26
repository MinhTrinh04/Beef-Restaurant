import apiClient from './client';
import { Order, ResponseDto } from '@/types';

// Backend response type (actual API response)
interface BackendOrder {
  orderId: number;
  userId: string;
  buyerEmail?: string;
  buyerName?: string;
  orderDate: string;
  orderStatus: string;
  orderItems: Array<{
    menuItemId: number;
    menuItemName: string;
    quantity: number;
    unitPrice: number;
  }>;
  totalAmount: number;
  createdAt: string;
  updatedAt: string;
  addressStreet?: string;
  addressCity?: string;
  addressState?: string;
  addressCountry?: string;
  description?: string;
}

// Transform backend order to frontend Order type
function transformOrder(backendOrder: BackendOrder): Order {
  return {
    orderId: backendOrder.orderId,
    userId: backendOrder.userId,
    userEmail: backendOrder.buyerEmail,
    items: backendOrder.orderItems || [],
    totalAmount: backendOrder.totalAmount,
    status: backendOrder.orderStatus,
    createdAt: backendOrder.createdAt || backendOrder.orderDate,
    updatedAt: backendOrder.updatedAt,
  };
}

/**
 * Get all orders
 */
export async function getAllOrders(): Promise<Order[]> {
  const response = await apiClient.get<ResponseDto<BackendOrder[]>>('/api/v1/admin/orders');
  return response.data.data.map(transformOrder);
}

/**
 * Get order by ID
 */
export async function getOrderById(orderId: number): Promise<Order> {
  const response = await apiClient.get<ResponseDto<BackendOrder>>(`/api/v1/admin/orders/${orderId}`);
  return transformOrder(response.data.data);
}

/**
 * Get orders by user ID
 */
export async function getOrdersByUserId(userId: string): Promise<Order[]> {
  const response = await apiClient.get<ResponseDto<BackendOrder[]>>(`/api/v1/admin/orders/user/${userId}`);
  return response.data.data.map(transformOrder);
}
