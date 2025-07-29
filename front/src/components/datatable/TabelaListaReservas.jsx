import { Grid, IconButton, Paper, Tooltip, Typography, useTheme } from "@mui/material";
import ResourceAvatar from "../Avatar";
import TableQuery from "@/components/datatable";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import useAlert from "@/context/alert";
import Icon from "@/helpers/iconHelper";
import { UserAuth } from "@/context/auth";
import { cancelaReserva, getReservas } from "@/services/reservas";
import { formatDate } from "@/helpers/format";

const TabelaListaReservas = ({ size = 12 }) => {
    const theme = useTheme();
    const queryClient = useQueryClient();
    const { dadosUser } = UserAuth();

    const { createModalAsync, createModal, AlertComponent } = useAlert();

    const reservasData = useQuery({
        queryKey: ["get-reservas"],
        queryFn: async () => {
            const fetchFunc = getReservas;
            if (!!fetchFunc) {
                const response = await getReservas(dadosUser.id);
                return response;
            }
        },
    });

    const handleRemove = async (id) => {
        const { isConfirmed } = await createModalAsync("warning", { title: "Cancelar", html: "Deseja mesmo cancelar esta reserva?" });
        if (!!isConfirmed) {
            try {
                const response = await cancelaReserva(id);
                if (response.status !== 500 && response.status !== 404) {
                    createModal("success", { showConfirmButton: true, html: <p style={{ textAlign: "center" }}>Reserva cancelada com sucesso!</p> });
                    queryClient.invalidateQueries(["get-exemplares", "get-livros", "get-reservas"]);
                } else {
                    createModal("error", { showConfirmButton: true, title: "Erro", html: <p style={{ textAlign: "center" }}>Ocorreu um erro ao cancelar a reserva</p> });
                }
            } catch (erro) {
                createModal("error", {
                    showConfirmButton: true,
                    title: "Erro",
                    html: (
                        <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
                            <p style={{ textAlign: "center" }}>Ocorreu um erro ao cancelar a reserva</p>
                            <p style={{ textAlign: "center" }}>{erro?.response?.data?.mensagem}</p>
                        </div>
                    ),
                });
            }
        }
    };

    const columnsLivrosReservados = [
        {
            field: "id",
            headerName: "Código da Reserva",
        },
        {
            field: "dataReserva",
            headerName: "Data da Reserva",
            flex: 1,
            sortable: true,
            renderCell: (params) => {
                return formatDate(params.value);
            },
        },
        {
            field: "dataPrevistaRetirada",
            headerName: "Data Prevista da Retirada",
            flex: 1,
            sortable: true,
            renderCell: (params) => {
                return formatDate(params.value);
            },
        },
        {
            field: "dataLimiteRetirada",
            headerName: "Data Limite da Retirada",
            flex: 1,
            sortable: true,
            renderCell: (params) => {
                return formatDate(params.value);
            },
        },
        {
            field: "dataRetirada",
            headerName: "Data da Retirada",
            flex: 1,
            sortable: true,
            renderCell: (params) => {
                return formatDate(params.value);
            },
        },

        {
            field: "observacoes",
            headerName: "Observações",
            flex: 1.2,
        },
        {
            field: "statusReserva",
            headerName: "Status da Reserva",
            flex: 1,
        },
        {
            field: "acao",
            headerName: "Ações",
            sortable: true,
            headerAlign: "center",
            flex: 1.0,
            renderCell: (cellValues) => {
                return (
                    <>
                        {cellValues.row.statusReserva === "ATIVA" && (
                            <Grid display="flex" justifyContent="center" alignItems="center" sx={{ width: "100%" }}>
                                <Tooltip title={"Cancelar"} placement="top" disableInteractive>
                                    <IconButton
                                        onClick={() => {
                                            handleRemove(cellValues.row.id);
                                        }}
                                    >
                                        <Icon name="Trash" style={{ fill: theme.colors.error.dark }} />
                                    </IconButton>
                                </Tooltip>
                            </Grid>
                        )}
                    </>
                );
            },
        },
    ];
    return (
        <Grid size={{ xs: size }}>
            <Paper sx={{ p: 4, height: "100%" }}>
                <ResourceAvatar sx={{ mt: -5, ml: -5 }} recurso={"Reservation"} />

                <Typography variant="h5" mb={3}>
                    Lista de Reservas
                </Typography>
                <TableQuery columns={columnsLivrosReservados} dataTable={reservasData} id="id" />
            </Paper>
            {AlertComponent}
        </Grid>
    );
};

export default TabelaListaReservas;
