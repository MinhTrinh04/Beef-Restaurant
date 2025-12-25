"use client";

import { SessionProvider } from "next-auth/react";
import "./globals.css";

export default function RootLayout({
    children,
}: {
    children: React.ReactNode;
}) {
    return (
        <html lang="en">
            <head>
                <title>Beef Restaurant - Admin Dashboard</title>
                <meta name="description" content="Admin dashboard for managing Beef Restaurant" />
            </head>
            <body>
                <SessionProvider>
                    {children}
                </SessionProvider>
            </body>
        </html>
    );
}
