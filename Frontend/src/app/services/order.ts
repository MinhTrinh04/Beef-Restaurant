import http from "@/lib/http";

export interface OrderItemDto {
    productName: string;
    pictureUrl: string;
    unitPrice: number;
    units: number;
    productId: number;
}

export interface OrderDto {
    orderNumber: string;
    date: string;
    status: string;
    total: number;
    description: string;
    city: string;
    street: string;
    state: string;
    country: string;
    zipCode: string;
    orderItems: OrderItemDto[];
    orderId: string;
}

export interface ResponseDto<T> {
    message: string;
    status: string;
    data: T;
}

export async function getMyOrders(): Promise<OrderDto[]> {
    const { data } = await http.get<ResponseDto<OrderDto[]>>("/api/orders/my-orders");
    return data.data;
}

export async function getOrderDetail(orderId: string): Promise<OrderDto> {
    const { data } = await http.get<ResponseDto<OrderDto>>(`/api/orders/my-orders/${orderId}`);
    return data.data;
}

export async function cancelOrder(orderId: string, reason?: string): Promise<boolean> {
    const { data } = await http.put<ResponseDto<boolean>>(`/api/orders/${orderId}/cancel`, null, {
        params: { reason }
    });
    return data.data;
}
