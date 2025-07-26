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

const deletarLivro = async (id) => {
    const api = ApiClient();
    const data = await api.delete(`/livro/${id}`);
    return data;
};

const updateLivro = async (dados) => {
    const api = ApiClient();
    const data = await api.put(`/livro`, dados);
    return data;
};

const getExemplares = async () => {
    const api = ApiClient();
    const { data } = await api.get(`/exemplar`);
    return data;
};

const cadastraExemplar = async (dados) => {
    const api = ApiClient();
    const data = await api.post(`/exemplar`, dados);
    return data;
};

const deletarExemplar = async (id) => {
    const api = ApiClient();
    const data = await api.delete(`/exemplar/${id}`);
    return data;
};

const updateExemplar = async (dados) => {
    const api = ApiClient();
    const data = await api.put(`/exemplar`, dados);
    return data;
};

const getExemplar = async (id) => {
    const api = ApiClient();
    const { data } = await api.get(`/exemplar/${id}`);
    return data;
};

const getLivro = async (id) => {
    const api = ApiClient();
    const { data } = await api.get(`/livro/${id}`);
    return data;
};

export { getLivros, cadastraLivro, deletarLivro, updateLivro, getExemplares, cadastraExemplar, deletarExemplar, updateExemplar, getExemplar, getLivro };
