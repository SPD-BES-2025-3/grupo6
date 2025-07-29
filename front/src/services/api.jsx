import axios from "axios";

const hash = "biblioteca-spd";

function getSession() {
    const token = localStorage.getItem(hash + "tokenJWT") || "";
    return token;
}

const ApiClient = () => {
    const token = getSession();

    const api = axios.create({
        baseURL: process.env.BASE_API_URL,
        headers: {
            Authorization: `Bearer ${token}`,
            "Content-type": "application/json",
        },
    });
    api.interceptors.response.use(
        (response) => {
            return response;
        },
        (error) => {
            if (error.response && error.response.status === 401) {
                localStorage.setItem(hash + "tokenJWT", "");
            }
            return Promise.reject(error);
        },
    );
    return api;
};

export { ApiClient };
