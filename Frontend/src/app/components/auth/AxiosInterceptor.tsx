"use client";

import { useEffect } from "react";
import { useSession, signOut } from "next-auth/react";
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

    const responseInterceptor = http.interceptors.response.use(
      (response) => response,
      async (error) => {
        if (error.response && error.response.status === 401) {
          await signOut({ callbackUrl: "/" });
        }
        return Promise.reject(error);
      }
    );

    return () => {
      http.interceptors.request.eject(requestInterceptor);
      http.interceptors.response.eject(responseInterceptor);
    };
  }, [session]);

  return <>{children}</>;
}
