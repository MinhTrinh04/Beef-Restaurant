"use client";

import React, { useEffect, useState } from 'react';
import { AdminLayout } from '@/components/layout/AdminLayout';
import { Loading } from '@/components/ui/Loading';
import { Button } from '@/components/ui/Button';
import { Modal } from '@/components/ui/Modal';
import { ToastContainer } from '@/components/ui/Toast';
import { getAllUsers, deleteUser } from '@/lib/api/users';
import { UserProfile } from '@/types';
import { formatDate } from '@/lib/utils';
import { Search, Trash2, Mail, Calendar } from 'lucide-react';

export default function UsersPage() {
    const [users, setUsers] = useState<UserProfile[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [deleteModalOpen, setDeleteModalOpen] = useState(false);
    const [userToDelete, setUserToDelete] = useState<UserProfile | null>(null);
    const [deleting, setDeleting] = useState(false);
    const [toasts, setToasts] = useState<Array<{ id: string; message: string; type: 'success' | 'error' | 'info' | 'warning' }>>([]);

    useEffect(() => {
        loadUsers();
    }, []);

    const loadUsers = async () => {
        try {
            setLoading(true);
            const data = await getAllUsers();
            setUsers(data);
            setError(null);
        } catch (err: any) {
            setError(err.message || 'Failed to load users');
            console.error('Error loading users:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleDeleteClick = (user: UserProfile) => {
        setUserToDelete(user);
        setDeleteModalOpen(true);
    };

    const handleDeleteConfirm = async () => {
        if (!userToDelete) return;

        try {
            setDeleting(true);
            await deleteUser(userToDelete.email);
            setUsers(users.filter(user => user.email !== userToDelete.email));
            addToast('User deleted successfully', 'success');
            setDeleteModalOpen(false);
            setUserToDelete(null);
        } catch (err: any) {
            addToast(err.message || 'Failed to delete user', 'error');
        } finally {
            setDeleting(false);
        }
    };

    const addToast = (message: string, type: 'success' | 'error' | 'info' | 'warning') => {
        const id = Math.random().toString(36).substr(2, 9);
        setToasts([...toasts, { id, message, type }]);
    };

    const removeToast = (id: string) => {
        setToasts(toasts.filter(toast => toast.id !== id));
    };

    const filteredUsers = users.filter((user) => {
        const searchLower = searchTerm.toLowerCase();
        return (
            user.email.toLowerCase().includes(searchLower) ||
            user.firstName?.toLowerCase().includes(searchLower) ||
            user.lastName?.toLowerCase().includes(searchLower)
        );
    });

    if (loading) {
        return (
            <AdminLayout>
                <div className="flex items-center justify-center h-96">
                    <Loading size="lg" text="Loading users..." />
                </div>
            </AdminLayout>
        );
    }

    if (error) {
        return (
            <AdminLayout>
                <div className="flex items-center justify-center h-96">
                    <div className="text-center">
                        <p className="text-red-500 text-lg mb-4">{error}</p>
                        <Button onClick={loadUsers}>Retry</Button>
                    </div>
                </div>
            </AdminLayout>
        );
    }

    return (
        <AdminLayout>
            <ToastContainer toasts={toasts} removeToast={removeToast} />

            <div className="space-y-6">
                {/* Page Header */}
                <div className="flex items-center justify-between">
                    <div>
                        <h1 className="text-3xl font-bold text-text-base font-barlow-condensed">
                            Users
                        </h1>
                        <p className="text-text-muted mt-1">
                            Manage registered users
                        </p>
                    </div>
                    <div className="text-right">
                        <p className="text-2xl font-bold text-primary font-barlow-condensed">
                            {users.length}
                        </p>
                        <p className="text-sm text-text-muted">Total Users</p>
                    </div>
                </div>

                {/* Search */}
                <div className="relative">
                    <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-text-muted" size={20} />
                    <input
                        type="text"
                        placeholder="Search by email or name..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="w-full pl-10 pr-4 py-3 bg-surface border border-border rounded-md text-text-base placeholder:text-text-muted focus:outline-none focus:ring-2 focus:ring-primary"
                    />
                </div>

                {/* Users Table */}
                <div className="bg-surface border border-border rounded-lg overflow-hidden">
                    <div className="overflow-x-auto">
                        <table className="w-full">
                            <thead className="bg-background border-b border-border">
                                <tr>
                                    <th className="px-6 py-4 text-left text-sm font-semibold text-text-base">
                                        User
                                    </th>
                                    <th className="px-6 py-4 text-left text-sm font-semibold text-text-base">
                                        Email
                                    </th>
                                    <th className="px-6 py-4 text-left text-sm font-semibold text-text-base">
                                        Phone
                                    </th>
                                    <th className="px-6 py-4 text-left text-sm font-semibold text-text-base">
                                        Joined
                                    </th>
                                    <th className="px-6 py-4 text-left text-sm font-semibold text-text-base">
                                        Actions
                                    </th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-border">
                                {filteredUsers.length === 0 ? (
                                    <tr>
                                        <td colSpan={5} className="px-6 py-12 text-center text-text-muted">
                                            {searchTerm ? 'No users found matching your search' : 'No users yet'}
                                        </td>
                                    </tr>
                                ) : (
                                    filteredUsers.map((user) => (
                                        <tr key={user.email} className="hover:bg-background transition-colors">
                                            <td className="px-6 py-4">
                                                <div className="flex items-center gap-3">
                                                    <div className="w-10 h-10 rounded-full bg-primary/20 flex items-center justify-center">
                                                        <span className="text-primary font-semibold">
                                                            {user.firstName?.charAt(0) || user.email.charAt(0).toUpperCase()}
                                                        </span>
                                                    </div>
                                                    <div>
                                                        <p className="text-sm font-medium text-text-base">
                                                            {user.firstName && user.lastName
                                                                ? `${user.firstName} ${user.lastName}`
                                                                : 'N/A'}
                                                        </p>
                                                        <p className="text-xs text-text-muted">
                                                            ID: {user.keycloakUserId?.substring(0, 8)}...
                                                        </p>
                                                    </div>
                                                </div>
                                            </td>
                                            <td className="px-6 py-4">
                                                <div className="flex items-center gap-2 text-sm text-text-base">
                                                    <Mail size={16} className="text-text-muted" />
                                                    {user.email}
                                                </div>
                                            </td>
                                            <td className="px-6 py-4 text-sm text-text-muted">
                                                {user.phoneNumber || 'N/A'}
                                            </td>
                                            <td className="px-6 py-4">
                                                <div className="flex items-center gap-2 text-sm text-text-muted">
                                                    <Calendar size={16} />
                                                    {formatDate(user.createdAt)}
                                                </div>
                                            </td>
                                            <td className="px-6 py-4">
                                                <Button
                                                    variant="danger"
                                                    size="sm"
                                                    onClick={() => handleDeleteClick(user)}
                                                >
                                                    <Trash2 size={16} className="mr-1" />
                                                    Delete
                                                </Button>
                                            </td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>

                {/* Summary */}
                {filteredUsers.length > 0 && (
                    <div className="text-sm text-text-muted">
                        Showing {filteredUsers.length} user{filteredUsers.length !== 1 ? 's' : ''}
                    </div>
                )}
            </div>

            {/* Delete Confirmation Modal */}
            <Modal
                isOpen={deleteModalOpen}
                onClose={() => !deleting && setDeleteModalOpen(false)}
                title="Delete User"
                size="sm"
            >
                <div className="space-y-4">
                    <p className="text-text-base">
                        Are you sure you want to delete user <strong>{userToDelete?.email}</strong>?
                        This action cannot be undone.
                    </p>
                    <div className="flex items-center gap-3 justify-end">
                        <Button
                            variant="secondary"
                            onClick={() => setDeleteModalOpen(false)}
                            disabled={deleting}
                        >
                            Cancel
                        </Button>
                        <Button
                            variant="danger"
                            onClick={handleDeleteConfirm}
                            disabled={deleting}
                        >
                            {deleting ? 'Deleting...' : 'Delete'}
                        </Button>
                    </div>
                </div>
            </Modal>
        </AdminLayout>
    );
}
