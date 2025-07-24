import React from "react";

import { FormContainer } from "react-hook-form-mui";
import { Button, Dialog, DialogActions, DialogContent, DialogTitle, IconButton } from "@mui/material";

import Icon from "@/helpers/iconHelper";

import * as yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import useAlert from "@/context/alert";
import { cadastraUsuario } from "@/services/usuarios";
import FormCadastrarLivro from "@/Forms/FormCadastrarLivro";
import { cadastraLivro } from "@/services/livros";
import { useQueryClient } from "@tanstack/react-query";

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
    editora: yup.string().required("Campo obrigatórioo"),
    anoLancamento: yup.string().required("Campo obrigatório"),
});

const CadastrarLivro = () => {
    const { createModalAsync, createModal, AlertComponent } = useAlert();
    const queryClient = useQueryClient();

    const [open, setOpen] = React.useState(false);

    const handleOpen = React.useCallback(() => setOpen(true), []);
    const handleClose = React.useCallback(() => setOpen(false), []);

    const handleSubmit = async (data) => {
        const { isConfirmed } = await createModalAsync("warning", { title: "Cadastrar", html: "Deseja mesmo cadastrar este livro?" });
        if (!!isConfirmed) {
            try {
                const response = await cadastraLivro(data);
                if (response.status === 200) {
                    createModal("success", { showConfirmButton: true, html: <p style={{ textAlign: "center" }}>Livro cadastrado com sucesso!</p> });
                    setOpen(false);
                    queryClient.invalidateQueries(["get-livros"]);
                } else {
                    createModal("error", { showConfirmButton: true, title: "Erro", html: <p style={{ textAlign: "center" }}>Ocorreu um erro ao cadastrar o livro</p> });
                }
            } catch (erro) {
                createModal("error", {
                    showConfirmButton: true,
                    title: "Erro",
                    html: (
                        <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
                            <p style={{ textAlign: "center" }}>Ocorreu um erro ao cadastrar o livro</p>
                            <p style={{ textAlign: "center" }}>{erro?.response?.data?.mensagem}</p>
                        </div>
                    ),
                });
            }
        }
    };

    return (
        <>
            <Button variant="contained" fullWidth color="secondary" onClick={handleOpen}>
                Cadastrar Livro
            </Button>
            <Dialog fullWidth maxWidth="sm" open={open} onClose={handleClose}>
                {open && (
                    <FormContainer onSuccess={(FormData) => handleSubmit(FormData)} resolver={yupResolver(schema)}>
                        <DialogTitle>
                            Cadastrar Livro
                            <CloseButton onClose={handleClose} />
                        </DialogTitle>
                        <DialogContent sx={{ m: 2 }}>
                            <FormCadastrarLivro />
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

export default CadastrarLivro;
