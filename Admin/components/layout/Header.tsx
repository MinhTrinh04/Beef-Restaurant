"use client";

import React from 'react';
import { User, LogOut } from 'lucide-react';

export function Header() {
    // Mock user data (will be replaced with real auth in next commit)
    const adminUser = {
        email: 'admin@beef.com',
        firstName: 'Admin',
        lastName: 'User',
    };

    const handleLogout = () => {
        // TODO: Implement logout in next commit
        console.log('Logout clicked');
    };

    return (
        <header className="h-16 bg-surface border-b border-border flex items-center justify-between px-6">
            <div className="flex-1" />

            <div className="flex items-center gap-4">
                {/* User info */}
                <div className="flex items-center gap-3">
                    <div className="w-10 h-10 rounded-full bg-primary/20 flex items-center justify-center">
                        <User size={20} className="text-primary" />
                    </div>
                    <div className="hidden md:block">
                        <p className="text-sm font-medium text-text-base">
                            {adminUser.firstName} {adminUser.lastName}
                        </p>
                        <p className="text-xs text-text-muted">{adminUser.email}</p>
                    </div>
                </div>

                {/* Logout button */}
                <button
                    onClick={handleLogout}
                    className="p-2 text-text-muted hover:text-primary transition-colors"
                    title="Logout"
                >
                    <LogOut size={20} />
                </button>
            </div>
        </header>
    );
}
