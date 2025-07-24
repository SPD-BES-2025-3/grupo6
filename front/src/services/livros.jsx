import { ApiClient } from "./api";

const getLivros = async () => {
    const api = ApiClient();
    const { data } = await api.get(`/livro`);
    return data;
};

const cadastraLivro = async (dados) => {
    const api = ApiClient();
    const data = await api.post(`/livro`, dados);
    return data;
};

export { getLivros, cadastraLivro };
