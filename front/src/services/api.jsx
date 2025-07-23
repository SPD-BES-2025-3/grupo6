import axios from "axios";

const ApiClient = () => {
    const api = axios.create({
        baseURL: process.env.BASE_API_URL,
        headers: {
            "Content-type": "application/json",
        },
    });
    api.interceptors.response.use(
        (response) => {
            return response;
        },
        (error) => {
            return Promise.reject(error);
        },
    );
    return api;
};

export { ApiClient };
