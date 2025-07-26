import React, { useEffect } from "react";

import { FormContainer } from "react-hook-form-mui";
import { Button, Dialog, DialogActions, DialogContent, DialogTitle, IconButton, useTheme } from "@mui/material";

import Icon from "@/helpers/iconHelper";

import * as yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import useAlert from "@/context/alert";
import { getExemplar, updateExemplar } from "@/services/livros";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import FormCadastrarExemplar from "@/Forms/FormCadastrarExemplar";

const CloseButton = React.memo(function CloseButton({ onClose }) {
    return (
        <IconButton color="secondary" onClick={onClose} aria-label="close" sx={{ position: "absolute", top: 8, right: 8 }}>
            <Icon name="Cancel" />
        </IconButton>
    );
});

const schema = yup.object().shape({
    idLivro: yup.mixed().required("Campo obrigatório"),
    conservacao: yup.mixed().required("Campo obrigatório"),
    numeroEdicao: yup.number().required("Campo obrigatório"),
    disponibilidade: yup.mixed().required("Campo obrigatório"),
});

const EditarExemplar = ({ id }) => {
    const theme = useTheme();

    const { createModalAsync, createModal, AlertComponent } = useAlert();
    const queryClient = useQueryClient();

    const [open, setOpen] = React.useState(false);

    const handleOpen = React.useCallback(() => setOpen(true), []);
    const handleClose = React.useCallback(() => setOpen(false), []);

    const handleSubmit = async (data) => {
        const { isConfirmed } = await createModalAsync("warning", { title: "Cadastrar", html: "Deseja mesmo editar este exemplar?" });
        if (!!isConfirmed) {
            try {
                const response = await updateExemplar(data);
                if (response.status === 200) {
                    createModal("success", { showConfirmButton: true, html: <p style={{ textAlign: "center" }}>Exemplar editado com sucesso!</p> });
                    setOpen(false);
                    queryClient.invalidateQueries(["get-exemplares", "get-livros"]);
                } else {
                    createModal("error", { showConfirmButton: true, title: "Erro", html: <p style={{ textAlign: "center" }}>Ocorreu um erro ao editar o exemplar</p> });
                }
            } catch (erro) {
                createModal("error", {
                    showConfirmButton: true,
                    title: "Erro",
                    html: (
                        <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
                            <p style={{ textAlign: "center" }}>Ocorreu um erro ao editar o exemplar</p>
                            <p style={{ textAlign: "center" }}>{erro?.response?.data?.mensagem}</p>
                        </div>
                    ),
                });
            }
        }
    };

    const exemplarData = useQuery({
        queryKey: ["get-exemplar", open],
        queryFn: async () => {
            const fetchFunc = getExemplar;
            if (!!fetchFunc) {
                const response = await getExemplar(id);
                return response;
            }
        },
        enabled: !!open,
    });

    return (
        <>
            <IconButton onClick={handleOpen}>
                <Icon name="Edit" style={{ fill: theme.colors.warning.dark }} />
            </IconButton>
            <Dialog fullWidth maxWidth="sm" open={open} onClose={handleClose}>
                {open && (
                    <FormContainer onSuccess={(FormData) => handleSubmit(FormData)} resolver={yupResolver(schema)}>
                        <DialogTitle>
                            Editar Exemplar
                            <CloseButton onClose={handleClose} />
                        </DialogTitle>
                        <DialogContent sx={{ m: 2 }}>
                            <FormCadastrarExemplar data={exemplarData.data} />
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

export default EditarExemplar;
