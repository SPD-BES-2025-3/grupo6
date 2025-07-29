import { ApiClient } from "./api";

const getReservas = async (usuarioId) => {
    const api = ApiClient();
    const { data } = await api.get(`/api/reserva/usuario/${usuarioId}`);
    return data;
};

const cadastraReserva = async (dados) => {
    const api = ApiClient();
    const data = await api.post(`/api/reserva`, dados);
    return data;
};

const cancelaReserva = async (id) => {
    const api = ApiClient();
    const data = await api.put(`/api/reserva/cancelar/${id}`);
    return data;
};
export { getReservas, cadastraReserva, cancelaReserva };
