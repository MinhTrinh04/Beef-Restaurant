"use client";

import { useEffect } from "react";
import { useSession } from "next-auth/react";
import { http } from "@/lib/http";

export default function AxiosInterceptor({ children }: { children: React.ReactNode }) {
  const { data: session } = useSession();

  useEffect(() => {
    const requestInterceptor = http.interceptors.request.use(
      (config) => {
        if (session?.accessToken) {
          config.headers.Authorization = `Bearer ${session.accessToken}`;
        }
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    return () => {
      http.interceptors.request.eject(requestInterceptor);
    };
  }, [session]);

  return <>{children}</>;
}
