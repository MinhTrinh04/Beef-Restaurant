"use client";

import React from 'react';
import { useSession, signOut } from 'next-auth/react';
import { LogOut, User } from 'lucide-react';
import { Button } from '@/components/ui/Button';

export function Header() {
    const { data: session } = useSession();

    const handleLogout = async () => {
        await signOut({ callbackUrl: '/login' });
    };

    return (
        <header className="bg-surface border-b border-border px-6 py-4">
            <div className="flex items-center justify-between">
                <div>
                    <h2 className="text-xl font-semibold text-text-base font-barlow-condensed">
                        Admin Dashboard
                    </h2>
                </div>

                <div className="flex items-center gap-4">
                    {/* User Info */}
                    <div className="flex items-center gap-3">
                        <div className="w-10 h-10 rounded-full bg-primary/20 flex items-center justify-center">
                            <User size={20} className="text-primary" />
                        </div>
                        <div className="text-right">
                            <p className="text-sm font-medium text-text-base">
                                {session?.user?.name || 'Admin User'}
                            </p>
                            <p className="text-xs text-text-muted">
                                {session?.user?.email || 'admin@example.com'}
                            </p>
                        </div>
                    </div>

                    {/* Logout Button */}
                    <Button
                        variant="ghost"
                        size="sm"
                        onClick={handleLogout}
                        className="!p-2"
                    >
                        <LogOut size={20} />
                    </Button>
                </div>
            </div>
        </header>
    );
}
