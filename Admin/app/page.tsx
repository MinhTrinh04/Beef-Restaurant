"use client";

import { redirect } from "next/navigation";
import { useEffect } from "react";

export default function Home() {
    useEffect(() => {
        // Redirect to dashboard (for now, will add auth check later)
        redirect("/dashboard");
    }, []);

    return null;
}
