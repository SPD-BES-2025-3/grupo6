import axios from "axios";

const api = axios.create({
    baseURL: process.env.BASE_API_URL,
});

const loginRequest = async (payload) => {
    const data = await api.post(`/api/auth/login`, payload);
    return data;
};

export { loginRequest };
