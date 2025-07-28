import { ApiClient } from "./api";

const getEmprestimos = async () => {
    const api = ApiClient();
    const { data } = await api.get(`/api/emprestimo`);
    return data;
};

const cadastraEmprestimo = async (dados) => {
    const api = ApiClient();
    const data = await api.post(`/api/emprestimo`, dados);
    return data;
};

const devolverEmprestimo = async (id) => {
    const api = ApiClient();
    const data = await api.put(`/api/emprestimo/${id}`);
    return data;
};

const renovarEmprestimo = async (id) => {
    const api = ApiClient();
    const data = await api.put(`/api/emprestimo/${id}/renovar`);
    return data;
};

const getEmprestimosPorUser = async (usuarioId) => {
    const api = ApiClient();
    const { data } = await api.get(`/api/emprestimo/usuario/${usuarioId}`);
    return data;
};

export { getEmprestimos, cadastraEmprestimo, devolverEmprestimo, renovarEmprestimo, getEmprestimosPorUser };
