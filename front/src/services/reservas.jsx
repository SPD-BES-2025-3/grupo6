import { ApiClient } from "./api";

const getReservas = async (usuarioId) => {
    const api = ApiClient();
    const { data } = await api.get(`/reserva/usuario/${usuarioId}`);
    return data;
};

const cadastraReserva = async (dados) => {
    const api = ApiClient();
    const data = await api.post(`/reserva`, dados);
    return data;
};
export { getReservas, cadastraReserva };
