import { Grid, Paper, Typography } from "@mui/material";
import ResourceAvatar from "../Avatar";
import TableQuery from "@/components/datatable";
import { useQuery } from "@tanstack/react-query";
import { getLivrosPublic } from "@/services/livros";

const TabelaListaLivrosPublica = ({ size = 12 }) => {
    const livrosData = useQuery({
        queryKey: ["get-livros"],
        queryFn: async () => {
            const fetchFunc = getLivrosPublic;
            if (!!fetchFunc) {
                const response = await getLivrosPublic();
                return response;
            }
        },
    });

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
        </Grid>
    );
};

export default TabelaListaLivrosPublica;
