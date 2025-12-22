import axios from "axios";
import { API_ENDPOINTS } from "@/lib/api-config";

export const authService = {
  async register(data: any) {
    const response = await axios.post(API_ENDPOINTS.users.register, data);
    return response.data;
  },
};
