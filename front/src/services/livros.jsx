import { ApiClient } from "./api";

const getLivros = async () => {
    const api = ApiClient();
    const { data } = await api.get(`/api/livro`);
    return data;
};

const cadastraLivro = async (dados) => {
    const api = ApiClient();
    const data = await api.post(`/api/livro`, dados);
    return data;
};

const deletarLivro = async (id) => {
    const api = ApiClient();
    const data = await api.delete(`/api/livro/${id}`);
    return data;
};

const updateLivro = async (dados) => {
    const api = ApiClient();
    const data = await api.put(`/api/livro`, dados);
    return data;
};

const getExemplares = async () => {
    const api = ApiClient();
    const { data } = await api.get(`/api/exemplar`);
    return data;
};

const cadastraExemplar = async (dados) => {
    const api = ApiClient();
    const data = await api.post(`/api/exemplar`, dados);
    return data;
};

const deletarExemplar = async (id) => {
    const api = ApiClient();
    const data = await api.delete(`/api/exemplar/${id}`);
    return data;
};

const updateExemplar = async (dados) => {
    const api = ApiClient();
    const data = await api.put(`/api/exemplar`, dados);
    return data;
};

const getExemplar = async (id) => {
    const api = ApiClient();
    const { data } = await api.get(`/api/exemplar/${id}`);
    return data;
};

const getLivro = async (id) => {
    const api = ApiClient();
    const { data } = await api.get(`/api/livro/${id}`);
    return data;
};

const getExemplaresPorLivro = async (id) => {
    const api = ApiClient();
    const { data } = await api.get(`/api/livro/${id}/exemplares-disponiveis`);
    return data;
};

const getLivrosPublic = async () => {
    const api = ApiClient();
    const { data } = await api.get(`/public/livro`);
    return data;
};

const getExemplarPorLivroPublic = async (id) => {
    const api = ApiClient();
    const { data } = await api.get(`/public/exemplar/livro/${id}`);
    return data;
};

export { getLivros, cadastraLivro, deletarLivro, updateLivro, getExemplares, cadastraExemplar, deletarExemplar, updateExemplar, getExemplar, getLivro, getExemplaresPorLivro, getLivrosPublic, getExemplarPorLivroPublic };
