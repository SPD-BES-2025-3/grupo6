import React from "react";

import { FormContainer } from "react-hook-form-mui";
import { Button, Dialog, DialogActions, DialogContent, DialogTitle, IconButton } from "@mui/material";

import FormCadastrarUsuario from "@/Forms/FormCadastrarUsuario";
import Icon from "@/helpers/iconHelper";

import * as yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import useAlert from "@/context/alert";
import { cadastraUsuario } from "@/services/usuarios";
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
    email: yup.string().email("E-mail inválido").required("Campo obrigatório"),
    cpf: yup.string().required("Campo obrigatório"),
    login: yup.string().required("Campo obrigatórioo"),
    senha: yup.string().required("Campo obrigatório"),
    permissoes: yup.mixed().required("Campo obrigatório"),
});

const CadastrarUsuario = () => {
    const { createModalAsync, createModal, AlertComponent } = useAlert();
    const queryClient = useQueryClient();

    const [open, setOpen] = React.useState(false);

    const handleOpen = React.useCallback(() => setOpen(true), []);
    const handleClose = React.useCallback(() => setOpen(false), []);

    const handleSubmit = async (data) => {
        const payload = { nome: data.nome, email: data.email, cpf: data.cpf, login: data.login, senha: data.senha, permissoes: [data.permissoes] };

        const { isConfirmed } = await createModalAsync("warning", { title: "Cadastrar", html: "Deseja mesmo cadastrar este usuário?" });
        if (!!isConfirmed) {
            try {
                const response = await cadastraUsuario(payload);
                if (response.status === 200) {
                    createModal("success", { showConfirmButton: true, html: <p style={{ textAlign: "center" }}>Usuário cadastrado com sucesso!</p> });
                    setOpen(false);
                    queryClient.invalidateQueries(["get-usuarios"]);
                } else {
                    createModal("error", { showConfirmButton: true, title: "Erro", html: <p style={{ textAlign: "center" }}>Ocorreu um erro ao cadastrar o usuário</p> });
                }
            } catch (erro) {
                createModal("error", {
                    showConfirmButton: true,
                    title: "Erro",
                    html: (
                        <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
                            <p style={{ textAlign: "center" }}>Ocorreu um erro ao cadastrar o usuário</p>
                            <p style={{ textAlign: "center" }}>{erro?.response?.data?.mensagem}</p>
                        </div>
                    ),
                });
            }
        }
    };

    return (
        <>
            <Button variant="contained" fullWidth color="primary" onClick={handleOpen}>
                Cadastrar Usuário
            </Button>

            <Dialog fullWidth maxWidth="sm" open={open} onClose={handleClose}>
                {open && (
                    <FormContainer onSuccess={(FormData) => handleSubmit(FormData)} resolver={yupResolver(schema)}>
                        <DialogTitle>
                            Cadastrar Usuário
                            <CloseButton onClose={handleClose} />
                        </DialogTitle>
                        <DialogContent sx={{ m: 2 }}>
                            <FormCadastrarUsuario />
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

export default CadastrarUsuario;
