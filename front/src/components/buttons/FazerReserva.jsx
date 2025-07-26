import React, { useEffect } from "react";

import { FormContainer } from "react-hook-form-mui";
import { Button, Dialog, DialogActions, DialogContent, DialogTitle, IconButton, Tooltip, useTheme } from "@mui/material";

import Icon from "@/helpers/iconHelper";

import * as yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import useAlert from "@/context/alert";
import { getExemplar } from "@/services/livros";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import FormReservarExemplar from "@/Forms/FormReservarExemplar";
import { cadastraReserva } from "@/services/reservas";

const CloseButton = React.memo(function CloseButton({ onClose }) {
    return (
        <IconButton color="secondary" onClick={onClose} aria-label="close" sx={{ position: "absolute", top: 8, right: 8 }}>
            <Icon name="Cancel" />
        </IconButton>
    );
});

const schema = yup.object().shape({
    idExemplar: yup.string().required("Campo obrigatório"),
    dataPrevistaRetirada: yup.date().required("Campo obrigatório"),
    observacoes: yup.string().nullable(),
});

const FazerReserva = ({ idLivro, nomeLivro }) => {
    const theme = useTheme();

    const { createModalAsync, createModal, AlertComponent } = useAlert();
    const queryClient = useQueryClient();

    const [open, setOpen] = React.useState(false);

    const handleOpen = React.useCallback(() => setOpen(true), []);
    const handleClose = React.useCallback(() => setOpen(false), []);

    const handleSubmit = async (data) => {
        const { isConfirmed } = await createModalAsync("warning", { title: "Cadastrar", html: "Deseja mesmo reservar este exemplar?" });
        if (!!isConfirmed) {
            try {
                const payload = {...data, dataReserva: new Date()}
                const response = await cadastraReserva(payload);
                if (response.status === 200) {
                    createModal("success", { showConfirmButton: true, html: <p style={{ textAlign: "center" }}>Exemplar reservado com sucesso!</p> });
                    setOpen(false);
                    queryClient.invalidateQueries(["get-livros", "get-exemplares"]);
                } else {
                    createModal("error", { showConfirmButton: true, title: "Erro", html: <p style={{ textAlign: "center" }}>Ocorreu um erro ao reservar o exemplar</p> });
                }
            } catch (erro) {
                createModal("error", {
                    showConfirmButton: true,
                    title: "Erro",
                    html: (
                        <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
                            <p style={{ textAlign: "center" }}>Ocorreu um erro ao reservar o exemplar</p>
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
            <Tooltip title={"Fazer Reserva"} placement="top" disableInteractive>
                <IconButton onClick={handleOpen}>
                    <Icon name="Reservation" style={{ fill: theme.colors.secondary.dark }} />
                </IconButton>
            </Tooltip>
            <Dialog fullWidth maxWidth="sm" open={open} onClose={handleClose}>
                {open && (
                    <FormContainer onSuccess={(FormData) => handleSubmit(FormData)} resolver={yupResolver(schema)}>
                        <DialogTitle>
                            Reservar Exemplar - {nomeLivro}
                            <CloseButton onClose={handleClose} />
                        </DialogTitle>
                        <DialogContent sx={{ m: 2 }}>
                            <FormReservarExemplar idLivro={idLivro} nomeLivro={nomeLivro} />
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

export default FazerReserva;
