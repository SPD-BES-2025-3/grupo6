import { Grid, Paper, Typography } from "@mui/material";
import ResourceAvatar from "../Avatar";
import TableQuery from "@/components/datatable";
import { formatDate, montaMascaraCPF_CNPJ } from "@/helpers/format";
import { useQuery } from "@tanstack/react-query";
import { getUsuarios } from "@/services/usuarios";

const TabelaListaUsuarios = ({ permissao, size = 12 }) => {
    const columnsUsuariosCadastrados = [
        {
            field: "id",
            headerName: "Código usuário",
            flex: 1,
        },
        {
            field: "nome",
            headerName: "Nome",
            flex: 1,
        },
        {
            field: "email",
            headerName: "E-mail",
            flex: 1.2,
        },
        {
            field: "cpf",
            headerName: "CPF",
            flex: 1,

            renderCell: (params) => {
                return montaMascaraCPF_CNPJ(params.value);
            },
        },
        {
            field: "login",
            headerName: "Login",
            flex: 1,
        },
        {
            field: "dataCadastro",
            headerName: "Data de Cadastro",
            flex: 1,
            sortable: true,
            renderCell: (params) => {
                return formatDate(params.value);
            },
        },
        {
            field: "permissoes",
            headerName: "Perfil",
            flex: 1,
        },
    ];

    const usuariosData = useQuery({
        queryKey: ["get-usuarios", !!permissao],
        queryFn: async () => {
            const fetchFunc = getUsuarios;
            if (!!fetchFunc) {
                const response = await getUsuarios();
                return response;
            }
        },
        enabled: !!permissao,
    });

    return (
        <Grid size={{ xs: size }}>
            <Paper sx={{ p: 4, height: "100%" }}>
                <ResourceAvatar sx={{ mt: -5, ml: -5 }} recurso={"Book"} />

                <Typography variant="h5" mb={3}>
                    Lista de usuários
                </Typography>
                <TableQuery columns={columnsUsuariosCadastrados} dataTable={usuariosData} id="id" />
            </Paper>
        </Grid>
    );
};

export default TabelaListaUsuarios;
