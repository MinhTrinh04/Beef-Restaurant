import axios, { AxiosError, AxiosResponse } from "axios";

const baseURL = process.env.NEXT_PUBLIC_API_BASE || "";

export const http = axios.create({
    baseURL,
    withCredentials: true,
});

// Request interceptor to attach token
http.interceptors.request.use(
    async (config) => {
        // Dynamic import to avoid SSR issues if used in non-client context contextually
        // specific to how NextAuth exposes getSession.
        const { getSession } = await import("next-auth/react");
        const session = await getSession();

        if (session?.accessToken) {
            config.headers.Authorization = `Bearer ${session.accessToken}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

http.interceptors.response.use(
    (res: AxiosResponse) => res,
    (err: AxiosError) => Promise.reject(err)
);

export default http;
