"use client";

import React from 'react';
import { AdminLayout } from '@/components/layout/AdminLayout';
import { ShoppingBag, UtensilsCrossed, Users, TrendingUp } from 'lucide-react';

export default function DashboardPage() {
    // Mock statistics (will be replaced with real data from API)
    const stats = [
        {
            name: 'Total Orders',
            value: '156',
            icon: ShoppingBag,
            change: '+12%',
            changeType: 'positive' as const,
        },
        {
            name: 'Menu Items',
            value: '48',
            icon: UtensilsCrossed,
            change: '+3',
            changeType: 'positive' as const,
        },
        {
            name: 'Total Users',
            value: '1,234',
            icon: Users,
            change: '+18%',
            changeType: 'positive' as const,
        },
        {
            name: 'Revenue',
            value: '45,678,000₫',
            icon: TrendingUp,
            change: '+23%',
            changeType: 'positive' as const,
        },
    ];

    return (
        <AdminLayout>
            <div className="space-y-6">
                {/* Page Header */}
                <div>
                    <h1 className="text-3xl font-bold text-text-base font-barlow-condensed">
                        Dashboard
                    </h1>
                    <p className="text-text-muted mt-1">
                        Welcome to Beef Restaurant Admin Dashboard
                    </p>
                </div>

                {/* Stats Grid */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                    {stats.map((stat) => {
                        const Icon = stat.icon;
                        return (
                            <div
                                key={stat.name}
                                className="bg-surface border border-border rounded-lg p-6 hover:border-primary transition-colors"
                            >
                                <div className="flex items-center justify-between">
                                    <div>
                                        <p className="text-text-muted text-sm">{stat.name}</p>
                                        <p className="text-2xl font-bold text-text-base mt-2 font-barlow-condensed">
                                            {stat.value}
                                        </p>
                                    </div>
                                    <div className="w-12 h-12 bg-primary/20 rounded-lg flex items-center justify-center">
                                        <Icon size={24} className="text-primary" />
                                    </div>
                                </div>
                                <div className="mt-4">
                                    <span className={`text-sm ${stat.changeType === 'positive' ? 'text-green-500' : 'text-red-500'}`}>
                                        {stat.change}
                                    </span>
                                    <span className="text-text-muted text-sm ml-2">from last month</span>
                                </div>
                            </div>
                        );
                    })}
                </div>

                {/* Quick Actions */}
                <div className="bg-surface border border-border rounded-lg p-6">
                    <h2 className="text-xl font-semibold text-text-base font-barlow-condensed mb-4">
                        Quick Actions
                    </h2>
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                        <a
                            href="/dashboard/orders"
                            className="p-4 border border-border rounded-lg hover:border-primary hover:bg-background transition-all"
                        >
                            <ShoppingBag size={24} className="text-primary mb-2" />
                            <h3 className="font-medium text-text-base">View Orders</h3>
                            <p className="text-sm text-text-muted mt-1">Manage customer orders</p>
                        </a>
                        <a
                            href="/dashboard/menu"
                            className="p-4 border border-border rounded-lg hover:border-primary hover:bg-background transition-all"
                        >
                            <UtensilsCrossed size={24} className="text-primary mb-2" />
                            <h3 className="font-medium text-text-base">Manage Menu</h3>
                            <p className="text-sm text-text-muted mt-1">Add or edit menu items</p>
                        </a>
                        <a
                            href="/dashboard/users"
                            className="p-4 border border-border rounded-lg hover:border-primary hover:bg-background transition-all"
                        >
                            <Users size={24} className="text-primary mb-2" />
                            <h3 className="font-medium text-text-base">Manage Users</h3>
                            <p className="text-sm text-text-muted mt-1">View and manage users</p>
                        </a>
                    </div>
                </div>
            </div>
        </AdminLayout>
    );
}
