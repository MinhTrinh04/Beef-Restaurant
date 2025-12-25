import apiClient from './client';
import { Order, ResponseDto } from '@/types';

/**
 * Get all orders
 */
export async function getAllOrders(): Promise<Order[]> {
  const response = await apiClient.get<ResponseDto<Order[]>>('/api/admin/orders');
  return response.data.data;
}

/**
 * Get order by ID
 */
export async function getOrderById(orderId: number): Promise<Order> {
  const response = await apiClient.get<ResponseDto<Order>>(`/api/admin/orders/${orderId}`);
  return response.data.data;
}

/**
 * Get orders by user ID
 */
export async function getOrdersByUserId(userId: string): Promise<Order[]> {
  const response = await apiClient.get<ResponseDto<Order[]>>(`/api/admin/orders/user/${userId}`);
  return response.data.data;
}
