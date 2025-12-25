// API Response Types
export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  error?: string;
}

export interface ResponseDto<T> {
  success: boolean;
  data: T;
  message?: string;
}

// User Types
export interface UserProfile {
  email: string;
  keycloakUserId: string;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
  createdAt: string;
  updatedAt: string;
}

// Order Types
export interface OrderItem {
  menuItemId: number;
  menuItemName: string;
  quantity: number;
  unitPrice: number;
}

export interface Order {
  orderId: number;
  userId: string;
  userEmail?: string;
  items: OrderItem[];
  totalAmount: number;
  status: string;
  createdAt: string;
  updatedAt: string;
}

// Menu Types
export interface MenuItem {
  id: number;
  name: string;
  slug: string;
  description: string;
  price: number;
  categoryId: number;
  categoryName?: string;
  imageUrl?: string;
  available: boolean;
  stockQuantity: number;
  createdAt?: string;
  updatedAt?: string;
}

export interface MenuItemDto {
  id?: number;
  name: string;
  slug: string;
  description: string;
  price: number;
  categoryId: number;
  imageUrl?: string;
  available?: boolean;
  stockQuantity: number;
}

// Pagination
export interface PaginationParams {
  page: number;
  limit: number;
}

export interface PaginatedResponse<T> {
  data: T[];
  total: number;
  page: number;
  limit: number;
  totalPages: number;
}
