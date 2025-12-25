import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
    title: "Beef Restaurant - Admin Dashboard",
    description: "Admin dashboard for managing Beef Restaurant",
};

export default function RootLayout({
    children,
}: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <html lang="en">
            <body>{children}</body>
        </html>
    );
}
