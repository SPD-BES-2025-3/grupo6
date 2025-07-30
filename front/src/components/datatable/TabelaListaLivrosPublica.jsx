import { Dialog, DialogContent, DialogTitle, Grid, IconButton, Paper, Tooltip, Typography, useTheme } from "@mui/material";
import ResourceAvatar from "../Avatar";
import TableQuery from "@/components/datatable";
import { useQuery } from "@tanstack/react-query";
import { getExemplarPorLivroPublic, getLivrosPublic } from "@/services/livros";
import Icon from "@/helpers/iconHelper";
import React from "react";

const TabelaListaLivrosPublica = ({ size = 12 }) => {
    const theme = useTheme();
    const livrosData = useQuery({
        queryKey: ["get-livros-public"],
        queryFn: async () => {
            const fetchFunc = getLivrosPublic;
            if (!!fetchFunc) {
                const response = await getLivrosPublic();
                return response;
            }
        },
    });

    const CloseButton = React.memo(function CloseButton({ onClose }) {
        return (
            <IconButton color="secondary" onClick={onClose} aria-label="close" sx={{ position: "absolute", top: 8, right: 8 }}>
                <Icon name="Cancel" />
            </IconButton>
        );
    });

    const [open, setOpen] = React.useState(false);
    const [nameLivro, setNameLivro] = React.useState(null);
    const [idLivro, setIdLivro] = React.useState(null);

    const handleOpen = React.useCallback(() => setOpen(true), []);
    const handleClose = React.useCallback(() => setOpen(false), []);

    const handleOpenModal = (name, id) => {
        console.log(name, id);
        handleOpen();
        setNameLivro(name);
        setIdLivro(id);
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
            field: "acao",
            headerName: "Ações",
            sortable: true,
            headerAlign: "center",
            flex: 1.0,
            renderCell: (cellValues) => {
                return (
                    <>
                        <Grid display="flex" justifyContent="center" alignItems="center" sx={{ width: "100%" }}>
                            <Tooltip title={"Visualizar Exemplares"} placement="top" disableInteractive>
                                <IconButton
                                    onClick={() => {
                                        handleOpenModal(cellValues.row.nome, cellValues.row.idOrm);
                                    }}
                                >
                                    <Icon name="View" style={{ fill: theme.colors.primary.dark }} />
                                </IconButton>
                            </Tooltip>
                        </Grid>
                    </>
                );
            },
        },
    ];

    const columnsExemplaresCadastrados = [
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
    ];

    const exemplaresData = useQuery({
        queryKey: ["get-exemplares-public"],
        queryFn: async () => {
            const fetchFunc = getExemplarPorLivroPublic;
            if (!!fetchFunc) {
                const response = await getExemplarPorLivroPublic(idLivro);
                return response;
            }
        },
        enabled: !!open,
    });
    return (
        <>
            <Grid size={{ xs: size }}>
                <Paper sx={{ p: 4, height: "100%" }}>
                    <ResourceAvatar sx={{ mt: -5, ml: -5 }} recurso={"Book"} />

                    <Typography variant="h5" mb={3}>
                        Lista de Livros
                    </Typography>
                    <TableQuery columns={columnsLivrosCadastrados} dataTable={livrosData} id="id" />
                </Paper>
            </Grid>

            <Dialog fullWidth maxWidth="sm" open={open} onClose={handleClose}>
                {open && nameLivro && (
                    <>
                        <DialogTitle>
                            Visualizar Exemplares do Livro "{nameLivro}"
                            <CloseButton onClose={handleClose} />
                        </DialogTitle>
                        <DialogContent sx={{ m: 2 }}>
                            <TableQuery columns={columnsExemplaresCadastrados} dataTable={exemplaresData} id="id" />
                        </DialogContent>
                    </>
                )}
            </Dialog>
        </>
    );
};

export default TabelaListaLivrosPublica;
