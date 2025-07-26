import { ApiClient } from "./api";

const getUsuarios = async () => {
    const api = ApiClient();
    const { data } = await api.get(`/usuario`);
    return data;
};

const cadastraUsuario = async (dados) => {
    const api = ApiClient();
    const data = await api.post(`/usuario`, dados);
    return data;
};

export { getUsuarios, cadastraUsuario };
