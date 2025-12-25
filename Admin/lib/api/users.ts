import apiClient from './client';
import { UserProfile, ApiResponse } from '@/types';

/**
 * Get all users
 */
export async function getAllUsers(): Promise<UserProfile[]> {
  const response = await apiClient.get<ApiResponse<UserProfile[]>>('/api/v1/users/admin/all');
  return response.data.data;
}

/**
 * Get user by email
 */
export async function getUserByEmail(email: string): Promise<UserProfile> {
  const response = await apiClient.get<ApiResponse<UserProfile>>(
    `/api/v1/users/admin/by-email?email=${encodeURIComponent(email)}`
  );
  return response.data.data;
}

/**
 * Delete user
 */
export async function deleteUser(email: string): Promise<void> {
  await apiClient.delete<ApiResponse<void>>(`/api/v1/users/admin/${email}`);
}
