import { Grid, IconButton, Paper, Tooltip, Typography, useTheme } from "@mui/material";
import ResourceAvatar from "../Avatar";
import TableQuery from "@/components/datatable";
import { useQuery } from "@tanstack/react-query";
import { deletarExemplar, getExemplares } from "@/services/livros";
import { formatDate } from "@/helpers/format";
import useAlert from "@/context/alert";
import Icon from "@/helpers/iconHelper";
import EditarExemplar from "../buttons/EditarExemplar";

const TabelaListaExemplares = ({ permissao = false, size = 12 }) => {
    const theme = useTheme();
    const { createModalAsync, createModal, AlertComponent } = useAlert();

    const exemplaresData = useQuery({
        queryKey: ["get-exemplares"],
        queryFn: async () => {
            const fetchFunc = getExemplares;
            if (!!fetchFunc) {
                const response = await getExemplares();
                return response;
            }
        },
    });

    const handleRemove = async (id) => {
        const { isConfirmed } = await createModalAsync("warning", { title: "Cadastrar", html: "Deseja mesmo apagar este exemplar?" });
        if (!!isConfirmed) {
            try {
                const response = await deletarExemplar(id);
                if (response.status !== 500 && response.status !== 404) {
                    createModal("success", { showConfirmButton: true, html: <p style={{ textAlign: "center" }}>Exemplar deletado com sucesso!</p> });
                    setOpen(false);
                    queryClient.invalidateQueries(["get-exemplares", "get-livros"]);
                } else {
                    createModal("error", { showConfirmButton: true, title: "Erro", html: <p style={{ textAlign: "center" }}>Ocorreu um erro ao deletar o exemplar</p> });
                }
            } catch (erro) {
                createModal("error", {
                    showConfirmButton: true,
                    title: "Erro",
                    html: (
                        <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
                            <p style={{ textAlign: "center" }}>Ocorreu um erro ao deletar o exemplar</p>
                            <p style={{ textAlign: "center" }}>{erro?.response?.data?.mensagem}</p>
                        </div>
                    ),
                });
            }
        }
    };

    const columnsExemplaresCadastrados = [
        {
            field: "codigoIdentificacao",
            headerName: "Código de identificação",
            flex: 1,
        },
        {
            field: "livroId",
            headerName: "Código do livro",
            flex: 1,
        },
        {
            field: "livroNome",
            headerName: "Titulo",
            flex: 1,
        },
        {
            field: "livroAutor",
            headerName: "Autor",
            flex: 1.2,
        },
        {
            field: "conservacao",
            headerName: "Estado de conservação",
            flex: 1,
        },
        {
            field: "numeroEdicao",
            headerName: "Número da edição",
            flex: 1,
        },
        {
            field: "disponibilidade",
            headerName: "Disponibilidade",
            flex: 1,
        },
        {
            field: "dataCriacao",
            headerName: "Data de Cadastro",
            flex: 1,
            sortable: true,
            renderCell: (params) => {
                return formatDate(params.value);
            },
        },
        {
            field: "acao",
            headerName: permissao && "Ações",
            sortable: true,
            headerAlign: "center",
            flex: 1.0,
            renderCell: (cellValues) => {
                return permissao ? (
                    <Grid display="flex" justifyContent="center" alignItems="center" sx={{ width: "100%" }}>
                        <Tooltip title={"Remover"} placement="top" disableInteractive>
                            <IconButton
                                onClick={() => {
                                    handleRemove(cellValues.row.id);
                                }}
                            >
                                <Icon name="Trash" style={{ fill: theme.colors.error.dark }} />
                            </IconButton>
                        </Tooltip>

                        <Tooltip title={"Editar Exemplar"} placement="top" disableInteractive>
                            <EditarExemplar id={cellValues.row.id} />
                        </Tooltip>
                    </Grid>
                ) : (
                    <></>
                );
            },
        },
    ];

    return (
        <Grid size={{ xs: size }}>
            <Paper sx={{ p: 4, height: "100%" }}>
                <ResourceAvatar sx={{ mt: -5, ml: -5 }} recurso={"Book"} color={theme.colors.gradients.blue1} />

                <Typography variant="h5" mb={3}>
                    Lista de Exemplares
                </Typography>
                <TableQuery columns={columnsExemplaresCadastrados} dataTable={exemplaresData} id="id" />
            </Paper>
            {AlertComponent}
        </Grid>
    );
};

export default TabelaListaExemplares;
