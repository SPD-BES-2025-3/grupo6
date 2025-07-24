import { Grid, Paper, Typography } from "@mui/material";
import ResourceAvatar from "../Avatar";
import TableQuery from "@/components/datatable";
import { formatDate, montaMascaraCPF_CNPJ } from "@/helpers/format";
import { useQuery } from "@tanstack/react-query";
import { getUsuarios } from "@/services/usuarios";
import { getLivros } from "@/services/livros";

const TabelaListaLivros = ({ permissao, size = 12 }) => {
    const columnsLivrosCadastrados = [
        {
            field: "id",
            headerName: "Código do livro",
            flex: 1,
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
            field: "quantidadeExemplares",
            headerName: "Quantidade de exemplares",
            flex: 1,
        },
    ];

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

    return (
        <Grid size={{ xs: size }}>
            <Paper sx={{ p: 4, height: "100%" }}>
                <ResourceAvatar sx={{ mt: -5, ml: -5 }} recurso={"User"} />

                <Typography variant="h5" mb={3}>
                    Lista de Livros
                </Typography>
                <TableQuery columns={columnsLivrosCadastrados} dataTable={livrosData} id="id" />
            </Paper>
        </Grid>
    );
};

export default TabelaListaLivros;
