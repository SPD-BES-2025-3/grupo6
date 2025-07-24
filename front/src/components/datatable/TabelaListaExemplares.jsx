import { Grid, Paper, Typography, useTheme } from "@mui/material";
import ResourceAvatar from "../Avatar";
import TableQuery from "@/components/datatable";
import { useQuery } from "@tanstack/react-query";
import { getExemplares } from "@/services/livros";
import { formatDate } from "@/helpers/format";

const TabelaListaExemplares = ({ permissao, size = 12 }) => {
    const theme = useTheme();

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
    ];

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

    return (
        <Grid size={{ xs: size }}>
            <Paper sx={{ p: 4, height: "100%" }}>
                <ResourceAvatar sx={{ mt: -5, ml: -5 }} recurso={"Book"} color={theme.colors.gradients.blue1} />

                <Typography variant="h5" mb={3}>
                    Lista de Exemplares
                </Typography>
                <TableQuery columns={columnsExemplaresCadastrados} dataTable={exemplaresData} id="id" />
            </Paper>
        </Grid>
    );
};

export default TabelaListaExemplares;
