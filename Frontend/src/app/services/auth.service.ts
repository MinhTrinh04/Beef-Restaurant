import axios from "axios";

// Adjust this URL if your environment uses a different base URL
const API_URL = "http://localhost:8080/api/v1/users";

export const authService = {
  async register(data: any) {
    const response = await axios.post(`${API_URL}/register`, data);
    return response.data;
  },
};
