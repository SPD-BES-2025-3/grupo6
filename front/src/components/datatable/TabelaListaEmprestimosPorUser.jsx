import { Grid, IconButton, Paper, Tooltip, Typography, useTheme } from "@mui/material";
import ResourceAvatar from "../Avatar";
import TableQuery from "@/components/datatable";
import { formatDate } from "@/helpers/format";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import { devolverEmprestimo, getEmprestimos, getEmprestimosPorUser, renovarEmprestimo } from "@/services/emprestimos";
import Icon from "@/helpers/iconHelper";
import useAlert from "@/context/alert";

const TabelaListaEmprestimosPorUser = ({ size = 12, id }) => {

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
    ];

    const emprestimosData = useQuery({
        queryKey: ["get-emprestimos"],
        queryFn: async () => {
            const fetchFunc = getEmprestimosPorUser;
            if (!!fetchFunc) {
                const response = await getEmprestimosPorUser(id);
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
        </Grid>
    );
};

export default TabelaListaEmprestimosPorUser;
