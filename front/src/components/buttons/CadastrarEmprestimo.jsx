import React from "react";

import { FormContainer } from "react-hook-form-mui";
import { Button, Dialog, DialogActions, DialogContent, DialogTitle, IconButton } from "@mui/material";

import Icon from "@/helpers/iconHelper";

import * as yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import useAlert from "@/context/alert";
import FormCadastrarLivro from "@/Forms/FormCadastrarLivro";
import { useQueryClient } from "@tanstack/react-query";
import { cadastraEmprestimo } from "@/services/emprestimos";
import FormCadastrarEmprestimo from "@/Forms/FormCadastrarEmprestimo";

const CloseButton = React.memo(function CloseButton({ onClose }) {
    return (
        <IconButton color="secondary" onClick={onClose} aria-label="close" sx={{ position: "absolute", top: 8, right: 8 }}>
            <Icon name="Cancel" />
        </IconButton>
    );
});

const schema = yup.object().shape({
    idUsuario: yup.string().required("Campo obrigatório"),

    tem_reserva: yup.string().oneOf(["S", "N"], "Valor inválido").required("Campo obrigatório"),

    idReserva: yup.string().when("tem_reserva", {
        is: (val) => val === "S",
        then: (schema) => schema.required("Campo obrigatório"),
        otherwise: (schema) => schema.notRequired(),
    }),

    idExemplar: yup.string().when("tem_reserva", {
        is: (val) => val === "N",
        then: (schema) => schema.required("Campo obrigatório"),
        otherwise: (schema) => schema.notRequired(),
    }),

});
const CadastrarEmprestimo = () => {
    const { createModalAsync, createModal, AlertComponent } = useAlert();
    const queryClient = useQueryClient();

    const [open, setOpen] = React.useState(false);

    const handleOpen = React.useCallback(() => setOpen(true), []);
    const handleClose = React.useCallback(() => setOpen(false), []);

    const handleSubmit = async (data) => {
        const { isConfirmed } = await createModalAsync("warning", { title: "Cadastrar", html: "Deseja mesmo realizar este empréstimo?" });
        if (!!isConfirmed) {
            try {
                console.log(data);
                const response = await cadastraEmprestimo(data);
                if (response.status === 201) {
                    createModal("success", { showConfirmButton: true, html: <p style={{ textAlign: "center" }}>Empréstimo realizado com sucesso!</p> });
                    setOpen(false);
                    queryClient.invalidateQueries(["get-emprestimos", "get-exemplares", "get-livros"]);
                } else {
                    createModal("error", { showConfirmButton: true, title: "Erro", html: <p style={{ textAlign: "center" }}>Ocorreu um erro ao realizar empréstimo</p> });
                }
            } catch (erro) {
                createModal("error", {
                    showConfirmButton: true,
                    title: "Erro",
                    html: (
                        <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
                            <p style={{ textAlign: "center" }}>Ocorreu um erro ao realizar empréstimo</p>
                            <p style={{ textAlign: "center" }}>{erro?.response?.data?.mensagem}</p>
                        </div>
                    ),
                });
            }
        }
    };

    return (
        <>
            <Button variant="contained" fullWidth color="success" onClick={handleOpen}>
                Fazer Empréstimo
            </Button>
            <Dialog fullWidth maxWidth="sm" open={open} onClose={handleClose}>
                {open && (
                    <FormContainer onSuccess={(FormData) => handleSubmit(FormData)} resolver={yupResolver(schema)}>
                        <DialogTitle>
                            Fazer Empréstimo
                            <CloseButton onClose={handleClose} />
                        </DialogTitle>
                        <DialogContent sx={{ m: 2 }}>
                            <FormCadastrarEmprestimo />
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

export default CadastrarEmprestimo;
