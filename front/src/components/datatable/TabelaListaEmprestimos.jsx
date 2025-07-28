import { Grid, IconButton, Paper, Tooltip, Typography, useTheme } from "@mui/material";
import ResourceAvatar from "../Avatar";
import TableQuery from "@/components/datatable";
import { formatDate } from "@/helpers/format";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import { devolverEmprestimo, getEmprestimos, renovarEmprestimo } from "@/services/emprestimos";
import Icon from "@/helpers/iconHelper";
import useAlert from "@/context/alert";

const TabelaListaEmprestimos = ({ size = 12, id }) => {
    const theme = useTheme();
    const { createModalAsync, createModal, AlertComponent } = useAlert();
    const queryClient = useQueryClient();

    const handleDevolucao = async (id) => {
        const { isConfirmed } = await createModalAsync("warning", { title: "Devolver", html: "Deseja mesmo registrar esta devolução?" });
        if (!!isConfirmed) {
            try {
                const response = await devolverEmprestimo(id);
                if (response.status !== 500 && response.status !== 404) {
                    createModal("success", { showConfirmButton: true, html: <p style={{ textAlign: "center" }}>Exemplar devolvido com sucesso!</p> });
                    queryClient.invalidateQueries(["get-exemplares", "get-livros", "get-emprestimos"]);
                } else {
                    createModal("error", { showConfirmButton: true, title: "Erro", html: <p style={{ textAlign: "center" }}>Ocorreu um erro ao devolver o exemplar</p> });
                }
            } catch (erro) {
                createModal("error", {
                    showConfirmButton: true,
                    title: "Erro",
                    html: (
                        <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
                            <p style={{ textAlign: "center" }}>Ocorreu um erro ao devolver o exemplar</p>
                            <p style={{ textAlign: "center" }}>{erro?.response?.data?.mensagem}</p>
                        </div>
                    ),
                });
            }
        }
    };

    const handleProrrogacao = async (id) => {
        const { isConfirmed } = await createModalAsync("warning", { title: "Renovar", html: "Deseja mesmo renovar este empréstimo?" });
        if (!!isConfirmed) {
            try {
                const response = await devolverEmprestimo(id);
                if (response.status !== 500 && response.status !== 404) {
                    createModal("success", { showConfirmButton: true, html: <p style={{ textAlign: "center" }}>Empréstimo renovado com sucesso!</p> });
                    queryClient.invalidateQueries(["get-exemplares", "get-livros", "get-emprestimos"]);
                } else {
                    createModal("error", { showConfirmButton: true, title: "Erro", html: <p style={{ textAlign: "center" }}>Ocorreu um erro ao renovar o empréstimo</p> });
                }
            } catch (erro) {
                createModal("error", {
                    showConfirmButton: true,
                    title: "Erro",
                    html: (
                        <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
                            <p style={{ textAlign: "center" }}>Ocorreu um erro ao renovar o empréstimo</p>
                            <p style={{ textAlign: "center" }}>{erro?.response?.data?.mensagem}</p>
                        </div>
                    ),
                });
            }
        }
    };

    const columnsEmprestimos = [
        {
            field: "id",
            headerName: "Código usuário",
            flex: 1,
        },
        {
            field: "usuarioNome",
            headerName: "Nome do Usuário",
            flex: 1,
            renderCell: (cellValues) => cellValues.row.usuario?.nome ?? "—",
        },
        {
            field: "exemplarNome",
            headerName: "Título do Exemplar",
            flex: 1,
            renderCell: (cellValues) => cellValues.row.exemplar?.livro ?? "—",
        },
        {
            field: "dataEmprestimo",
            headerName: "Data do empéstimo",
            flex: 1,
            sortable: true,
            renderCell: (params) => {
                return formatDate(params.value);
            },
        },
        {
            field: "dataPrevistaDevolucao",
            headerName: "Data Prevista da Devolução",
            flex: 1,
            sortable: true,
            renderCell: (params) => {
                return formatDate(params.value);
            },
        },
        {
            field: "dataDevolucao",
            headerName: "Data da Devolução",
            flex: 1,
            sortable: true,
            renderCell: (params) => {
                return formatDate(params.value);
            },
        },
        {
            field: "observacoes",
            headerName: "Observações",
            flex: 1,
        },
        {
            field: "renovacoes",
            headerName: "Qtd. Renovações",
            flex: 1,
        },
        {
            field: "status",
            headerName: "Status do Empréstimo",
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
                        {cellValues.row.status !== "DEVOLVIDO" && (
                            <Grid display="flex" justifyContent="center" alignItems="center" sx={{ width: "100%" }}>
                                <Tooltip title={"Devolver Livro"} placement="top" disableInteractive>
                                    <IconButton
                                        onClick={() => {
                                            handleDevolucao(cellValues.row.id);
                                        }}
                                    >
                                        <Icon name="Return" style={{ fill: theme.colors.primary.dark }} />
                                    </IconButton>
                                </Tooltip>
                                <Tooltip title={"Renovar Empréstimo"} placement="top" disableInteractive>
                                    <IconButton
                                        onClick={() => {
                                            handleProrrogacao(cellValues.row.id);
                                        }}
                                    >
                                        <Icon name="Calendar" size={18} style={{ fill: theme.colors.secondary.dark }} />
                                    </IconButton>
                                </Tooltip>
                            </Grid>
                        )}
                    </>
                );
            },
        },
    ];

    const emprestimosData = useQuery({
        queryKey: ["get-emprestimos"],
        queryFn: async () => {
            const fetchFunc = getEmprestimos;
            if (!!fetchFunc) {
                const response = await getEmprestimos();
                return response;
            }
        },
    });

    return (
        <Grid size={{ xs: size }}>
            <Paper sx={{ p: 4, height: "100%" }}>
                <ResourceAvatar sx={{ mt: -5, ml: -5 }} recurso={"Reservation"} />

                <Typography variant="h5" mb={3}>
                    Lista de empréstimos
                </Typography>
                <TableQuery columns={columnsEmprestimos} dataTable={emprestimosData} id="id" />
            </Paper>
            {AlertComponent}
        </Grid>
    );
};

export default TabelaListaEmprestimos;
