import apiClient from './client';
import { MenuItem, MenuItemDto, ResponseDto } from '@/types';

/**
 * Get all menu items
 */
export async function getAllMenuItems(): Promise<MenuItem[]> {
  const response = await apiClient.get<MenuItem[]>('/api/menu/items');
  return response.data;
}

/**
 * Get menu item by ID
 */
export async function getMenuItemById(id: number): Promise<MenuItem> {
  const response = await apiClient.get<MenuItem>(`/api/menu/items/${id}`);
  return response.data;
}

/**
 * Create new menu item
 */
export async function createMenuItem(data: MenuItemDto): Promise<void> {
  await apiClient.post<ResponseDto<void>>('/api/menu/items', data);
}

/**
 * Update menu item
 */
export async function updateMenuItem(id: number, data: MenuItemDto): Promise<void> {
  await apiClient.put<ResponseDto<void>>(`/api/menu/items/${id}`, data);
}

/**
 * Delete menu item
 */
export async function deleteMenuItem(id: number): Promise<void> {
  await apiClient.delete<ResponseDto<void>>(`/api/menu/items/${id}`);
}
