"use client";

import React from 'react';
import { Sidebar } from './Sidebar';
import { Header } from './Header';

interface AdminLayoutProps {
    children: React.ReactNode;
}

export function AdminLayout({ children }: AdminLayoutProps) {
    return (
        <div className="min-h-screen bg-background">
            <Sidebar />

            <div className="lg:pl-64">
                <Header />

                <main className="p-6">
                    {children}
                </main>
            </div>
        </div>
    );
}
