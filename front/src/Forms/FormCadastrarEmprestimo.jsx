import { getExemplares } from "@/services/livros";
import { getReservas } from "@/services/reservas";
import { getUsuarios } from "@/services/usuarios";
import { Grid, Typography } from "@mui/material";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { RadioButtonGroup, SelectElement, TextareaAutosizeElement, useWatch } from "react-hook-form-mui";

const FormCadastrarEmprestimo = ({ data }) => {
    const idUser = useWatch("idUsuario");
    const temReserva = useWatch("tem_reserva");

    const [userOptions, setUserOptions] = useState([]);
    const [reservasOption, setReservasOption] = useState([]);
    const [exemplaresOption, setExemplaresOption] = useState([]);

    const usuariosData = useQuery({
        queryKey: ["get-usuarios"],
        queryFn: async () => {
            const fetchFunc = getUsuarios;
            if (!!fetchFunc) {
                const response = await getUsuarios();
                return response;
            }
        },
    });

    const reservaData = useQuery({
        queryKey: ["get-reservas", idUser.idUsuario],
        queryFn: async () => {
            const fetchFunc = getReservas;
            if (!!fetchFunc) {
                const response = await getReservas(idUser.idUsuario);
                return response;
            }
        },
        enabled: !!idUser.idUsuario,
    });

    const exemplaresData = useQuery({
        queryKey: ["get-exemplares"],
        queryFn: async () => {
            const fetchFunc = getExemplares;
            if (!!fetchFunc) {
                const response = await getExemplares();
                return response;
            }
        },
        enabled: temReserva.tem_reserva == "N",
    });

    useEffect(() => {
        if (exemplaresData.data) {
            const opts = exemplaresData.data
                .filter((exemplar) => exemplar.disponibilidade === "DISPONIVEL")
                .map((exemplar) => ({
                    id: exemplar.id,
                    label: exemplar.livroNome + " - " + exemplar.conservacao.toLowerCase(),
                }));
            setExemplaresOption(opts);
        }
    }, [exemplaresData.data]);

    useEffect(() => {
        if (usuariosData.data) {
            const opts = usuariosData.data
                .filter((user) => Array.isArray(user.permissoes) && user.permissoes[0] === "USUARIO")
                .map((user) => ({
                    id: user.id,
                    label: user.nome,
                }));
            setUserOptions(opts);
        }
    }, [usuariosData.data]);

    useEffect(() => {
        if (reservaData.data) {
            const opts = reservaData.data
                .filter((reserva) => reserva.statusReserva === "ATIVA")
                .map((reserva) => ({
                    id: reserva.id,
                    label: reserva.exemplar.livro.nome,
                    idExemplar: reserva.exemplar.id,
                }));
            setReservasOption(opts);
        }
    }, [reservaData.data]);

    return (
        <Grid container spacing={2} pt={2}>
            <Grid size={{ xs: 12 }}>
                <SelectElement fullWidth name="idUsuario" label="Usuário" options={userOptions} />
            </Grid>
            {!!idUser.idUsuario && (
                <Grid size={{ xs: 12 }}>
                    <Typography pr={2} variant="h6">
                        O usuário possui reserva?
                    </Typography>
                    <RadioButtonGroup
                        required
                        name="tem_reserva"
                        options={[
                            {
                                id: "S",
                                label: "Sim",
                            },
                            {
                                id: "N",
                                label: "Não",
                            },
                        ]}
                        row
                    />
                </Grid>
            )}

            {idUser && !!reservaData && !!reservaData.isSuccess && temReserva.tem_reserva == "S" && (
                <Grid size={{ xs: 12 }}>
                    <SelectElement fullWidth name="idReserva" label="Reserva" options={reservasOption} />
                </Grid>
            )}
            {!!idUser && temReserva.tem_reserva == "N" && !!exemplaresData && !!exemplaresData.isSuccess && (
                <Grid size={{ xs: 12 }}>
                    <SelectElement fullWidth name="idExemplar" label="Exemplar Desejado" options={exemplaresOption} />
                </Grid>
            )}
            <Grid size={{ xs: 12 }}>
                <TextareaAutosizeElement fullWidth label="Observações" name="observacoes" rows={5} />
            </Grid>
        </Grid>
    );
};

export default FormCadastrarEmprestimo;
