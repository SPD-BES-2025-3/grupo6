import { ApiClient } from "./api";

const loginRequest = async (payload) => {
    const api = ApiClient();
    const data = await api.post(`/auth/login`, payload);
    return data;
};

export { loginRequest };
