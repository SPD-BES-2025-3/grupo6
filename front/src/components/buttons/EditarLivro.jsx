import React from "react";

import { FormContainer } from "react-hook-form-mui";
import { Button, Dialog, DialogActions, DialogContent, DialogTitle, IconButton, Tooltip, useTheme } from "@mui/material";

import Icon from "@/helpers/iconHelper";

import * as yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import useAlert from "@/context/alert";
import { getLivro, updateLivro } from "@/services/livros";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import FormCadastrarLivro from "@/Forms/FormCadastrarLivro";

const CloseButton = React.memo(function CloseButton({ onClose }) {
    return (
        <IconButton color="secondary" onClick={onClose} aria-label="close" sx={{ position: "absolute", top: 8, right: 8 }}>
            <Icon name="Cancel" />
        </IconButton>
    );
});

const schema = yup.object().shape({
    nome: yup.string().required("Campo obrigatório"),
    autor: yup.string().required("Campo obrigatório"),
    editora: yup.string().required("Campo obrigatório"),
    anoLancamento: yup.string().required("Campo obrigatório"),
});

const EditarLivro = ({ id }) => {
    const theme = useTheme();

    const { createModalAsync, createModal, AlertComponent } = useAlert();
    const queryClient = useQueryClient();

    const [open, setOpen] = React.useState(false);

    const handleOpen = React.useCallback(() => setOpen(true), []);
    const handleClose = React.useCallback(() => setOpen(false), []);

    const handleSubmit = async (data) => {
        const { isConfirmed } = await createModalAsync("warning", { title: "Editar", html: "Deseja mesmo editar este livro?" });
        if (!!isConfirmed) {
            try {
                const response = await updateLivro(data);
                if (response.status === 200) {
                    createModal("success", { showConfirmButton: true, html: <p style={{ textAlign: "center" }}>Livro editado com sucesso!</p> });
                    setOpen(false);
                    queryClient.invalidateQueries(["get-exemplares", "get-livros"]);
                } else {
                    createModal("error", { showConfirmButton: true, title: "Erro", html: <p style={{ textAlign: "center" }}>Ocorreu um erro ao editar o livro</p> });
                }
            } catch (erro) {
                createModal("error", {
                    showConfirmButton: true,
                    title: "Erro",
                    html: (
                        <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
                            <p style={{ textAlign: "center" }}>Ocorreu um erro ao editar o livro</p>
                            <p style={{ textAlign: "center" }}>{erro?.response?.data?.mensagem}</p>
                        </div>
                    ),
                });
            }
        }
    };

    const livroData = useQuery({
        queryKey: ["get-livro", open],
        queryFn: async () => {
            const fetchFunc = getLivro;
            if (!!fetchFunc) {
                const response = await getLivro(id);
                return response;
            }
        },
        enabled: !!open,
    });

    return (
        <>
            <Tooltip title={"Editar Livro"} placement="top" disableInteractive>
                <IconButton onClick={handleOpen}>
                    <Icon name="Edit" style={{ fill: theme.colors.warning.dark }} />
                </IconButton>
            </Tooltip>
            <Dialog fullWidth maxWidth="sm" open={open} onClose={handleClose}>
                {open && (
                    <FormContainer onSuccess={(FormData) => handleSubmit(FormData)} resolver={yupResolver(schema)}>
                        <DialogTitle>
                            Editar Exemplar
                            <CloseButton onClose={handleClose} />
                        </DialogTitle>
                        <DialogContent sx={{ m: 2 }}>
                            <FormCadastrarLivro data={livroData.data} />
                        </DialogContent>
                        <DialogActions>
                            <Button variant="outlined" color="error" onClick={handleClose} sx={{ mr: 1 }} startIcon={<Icon name="Cancel" />}>
                                Cancelar
                            </Button>
                            <Button variant="outlined" type="submit" color="success" startIcon={<Icon name="Success" />}>
                                Salvar
                            </Button>
                        </DialogActions>
                    </FormContainer>
                )}
            </Dialog>
            {AlertComponent}
        </>
    );
};

export default EditarLivro;
