import { Grid, IconButton, Paper, Tooltip, Typography, useTheme } from "@mui/material";
import ResourceAvatar from "../Avatar";
import TableQuery from "@/components/datatable";
import { formatDate, montaMascaraCPF_CNPJ } from "@/helpers/format";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import { getUsuarios } from "@/services/usuarios";
import { deletarLivro, getLivros } from "@/services/livros";
import useAlert from "@/context/alert";
import Icon from "@/helpers/iconHelper";
import EditarLivro from "../buttons/EditarLivro";

const TabelaListaLivros = ({ permissao, size = 12 }) => {
    const theme = useTheme();
    const queryClient = useQueryClient();

    const { createModalAsync, createModal, AlertComponent } = useAlert();

    const livrosData = useQuery({
        queryKey: ["get-livros", !!permissao],
        queryFn: async () => {
            const fetchFunc = getLivros;
            if (!!fetchFunc) {
                const response = await getLivros();
                return response;
            }
        },
        enabled: !!permissao,
    });

    const handleRemove = async (id) => {
        const { isConfirmed } = await createModalAsync("warning", { title: "Cadastrar", html: "Deseja mesmo apagar este livro?" });
        if (!!isConfirmed) {
            try {
                const response = await deletarLivro(id);
                if (response.status !== 500 && response.status !== 404) {
                    createModal("success", { showConfirmButton: true, html: <p style={{ textAlign: "center" }}>Livro deletado com sucesso!</p> });
                    queryClient.invalidateQueries(["get-exemplares", "get-livros"]);
                } else {
                    createModal("error", { showConfirmButton: true, title: "Erro", html: <p style={{ textAlign: "center" }}>Ocorreu um erro ao deletar o livro</p> });
                }
            } catch (erro) {
                createModal("error", {
                    showConfirmButton: true,
                    title: "Erro",
                    html: (
                        <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
                            <p style={{ textAlign: "center" }}>Ocorreu um erro ao deletar o livro</p>
                            <p style={{ textAlign: "center" }}>{erro?.response?.data?.mensagem}</p>
                        </div>
                    ),
                });
            }
        }
    };

    const columnsLivrosCadastrados = [
        {
            field: "id",
            headerName: "Código do livro",
        },
        {
            field: "nome",
            headerName: "Titulo",
            flex: 1,
        },
        {
            field: "autor",
            headerName: "Autor",
            flex: 1.2,
        },
        {
            field: "editora",
            headerName: "Editora",
            flex: 1,
        },
        {
            field: "anoLancamento",
            headerName: "Ano de lancamento",
        },
        {
            field: "quantidadeExemplares",
            headerName: "Quantidade de exemplares",
            flex: 1,
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
                            <EditarLivro id={cellValues.row.id} />
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
                <ResourceAvatar sx={{ mt: -5, ml: -5 }} recurso={"Book"} />

                <Typography variant="h5" mb={3}>
                    Lista de Livros
                </Typography>
                <TableQuery columns={columnsLivrosCadastrados} dataTable={livrosData} id="id" />
            </Paper>
            {AlertComponent}
        </Grid>
    );
};

export default TabelaListaLivros;
