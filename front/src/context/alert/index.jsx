import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import { useCallback, useState } from "react";

import { CircularProgress, Typography } from "@mui/material";
import { FormContainer } from "react-hook-form-mui";
import { capitalizarPrimeiraLetra } from "@/helpers/format";
import Icon from "@/helpers/iconHelper";

const useAlert = () => {
    const [modalConfig, setModalConfig] = useState(null);
    const [resolvePromise, setResolvePromise] = useState(null);

    const createModalAsync = useCallback((type, options) => {
        return new Promise((resolve) => {
            setModalConfig({ ...options, open: true, type });
            setResolvePromise(() => resolve);
        });
    }, []);

    const createModal = useCallback((type, options) => {
        setModalConfig({ ...options, open: true, type });
    }, []);

    const closeModal = useCallback(
        (result) => {
            setModalConfig((prev) => ({ ...prev, open: false }));
            if (resolvePromise) resolvePromise(result);
        },
        [resolvePromise],
    );

    const renderContent = (content) => {
        if (typeof content === "string") {
            return <Typography variant="body1">{content}</Typography>;
        } else if (Array.isArray(content)) {
            return content.map((text, index) => (
                <Typography key={index} variant="body1" gutterBottom>
                    {text}
                </Typography>
            ));
        } else {
            return content;
        }
    };
    const AlertComponent = (
        <>
            {modalConfig && (
                <Dialog open={modalConfig.open} onClose={() => closeModal({ isConfirmed: false })} maxWidth={modalConfig.maxWidth || "xs"} fullWidth>
                    {modalConfig.type === "loading" ? (
                        <DialogContent sx={{ display: "flex", flexDirection: "column", alignItems: "center", py: 4 }}>
                            <Icon name={"Info"} color={"info"} sx={{ fontSize: "70px" }} />
                            <Typography variant="h3" id="dialog-title">
                                Carregando...
                            </Typography>
                            <CircularProgress sx={{ my: 3 }} />
                        </DialogContent>
                    ) : (
                        <>
                            <div style={{ display: "flex", flexDirection: "column", alignItems: "center", padding: 2}}>
                                <Icon name={capitalizarPrimeiraLetra(modalConfig.type)} color={modalConfig.type} sx={{ fontSize: "70px" }} />
                                <Typography variant="h3" id="dialog-title" sx={{ textAlign: "center" }}>
                                    {modalConfig.title}
                                </Typography>
                            </div>
                            <FormContainer
                                onSuccess={(e) => {
                                    closeModal({ isConfirmed: true });
                                }}
                            >
                                <DialogContent>
                                    <div style={{ display: "flex", justifyContent: "center" }}>{renderContent(modalConfig.html)}</div>
                                </DialogContent>
                                <DialogActions>
                                    {!modalConfig.dontShowCancel && (
                                        <Button onClick={() => closeModal({ isConfirmed: false })} color="error">
                                            Cancelar
                                        </Button>
                                    )}
                                    {!modalConfig.dontShowConfirm && (
                                        <Button variant="contained" color="success" type="submit">
                                            {modalConfig.type == "success" ? "Ok" : "Confirmar"}
                                        </Button>
                                    )}
                                </DialogActions>
                            </FormContainer>
                        </>
                    )}
                </Dialog>
            )}
        </>
    );
    return { createModalAsync, createModal, AlertComponent };
};

export default useAlert;
