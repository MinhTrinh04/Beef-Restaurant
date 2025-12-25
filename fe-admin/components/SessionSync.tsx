"use client";

import { useSession } from "next-auth/react";
import { useEffect } from "react";
import { setSessionToken } from "@/lib/api/client";

export function SessionSync() {
    const { data: session, status } = useSession();

    useEffect(() => {
        console.log('🔄 SessionSync - Status:', status);
        console.log('🔄 SessionSync - Has session:', !!session);
        console.log('🔄 SessionSync - Has accessToken:', !!session?.accessToken);

        if (status === "authenticated" && session?.accessToken) {
            console.log('✅ SessionSync - Setting token in API client');
            setSessionToken(session.accessToken);
        } else if (status === "unauthenticated") {
            console.log('⚠️ SessionSync - Clearing token from API client');
            setSessionToken(undefined);
        }
    }, [session, status]);

    return null; // This component doesn't render anything
}
