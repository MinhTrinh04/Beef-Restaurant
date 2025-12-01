import axios, { AxiosError, AxiosResponse } from "axios";

const baseURL = process.env.NEXT_PUBLIC_API_BASE || "";

export const http = axios.create({
    baseURL,
    withCredentials: true,
});

http.interceptors.response.use(
    (res: AxiosResponse) => res,
    (err: AxiosError) => Promise.reject(err)
);

export default http;
