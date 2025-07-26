import React from "react";

import { Grid, Typography, useTheme } from "@mui/material";
import { FormContainer, PasswordElement, TextFieldElement } from "react-hook-form-mui";

import Icon from "@/helpers/iconHelper";
import NextButton from "@/components/buttons/NextButton";
import useAlert from "@/context/alert";
import { UserAuth } from "@/context/auth";
import useLocalStorage from "@/context/localstorage";
import { loginRequest } from "@/services/auth";

import * as yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";

const Login = () => {
    const theme = useTheme();
    const { createModal, AlertComponent } = useAlert();
    const { setUser, setDadosUser, setToken } = UserAuth();
    const { setLocalStorage } = useLocalStorage();

    const handleSubmit = async (data) => {
        try {
            const result = await loginRequest(data);
            if (result.status === 200) {
                if (!!result.data) {
                    const dados = { perfil: result.data.permissoes[0], nome_usuario: result.data.nome, token: result.data.token, id: result.data.id };
                    setLocalStorage("tokenJWT", result.data.token);
                    setToken(result.data.token);
                    setLocalStorage("userData", JSON.stringify(dados));
                    setDadosUser(dados);
                    setUser(true);
                }
            }
        } catch (error) {
            createModal("error", {
                title: "Não foi possível realizar o login",
                html: error?.response?.data,
                dontShowCancel: true,
            });
        }
    };

    const handleLoginVisitante = () => {
        setLocalStorage("userData", JSON.stringify({ perfil: "VISITANTE", nome_usuario: "Visitante" }));
        setDadosUser({ perfil: "VISITANTE", nome_usuario: "Visitante" });
        setUser(true);
    };

    const schema = yup.object().shape({
        login: yup.string().required("Login é obrigatório"),
        senha: yup.string().required("Senha é obrigatória"),
    });

    return (
        <>
            <div
                style={{
                    backgroundImage: `url(/bg-login.webp)`,
                    backgroundColor: "#ffff",
                    backgroundRepeat: "no-repeat",
                    backgroundAttachment: "fixed",
                    backgroundPosition: "center",
                    backgroundSize: "cover",
                    height: "100vh",
                    width: "100vw",
                }}
            >
                <div style={{ position: "absolute", top: "50%", left: "50%", transform: "translate(-50%, -50%)" }}>
                    <FormContainer onSuccess={(FormData) => handleSubmit(FormData)} resolver={yupResolver(schema)}>
                        <div
                            style={{
                                background: "rgb(57 57 57 / 10%)",
                                boxShadow: "rgb(255 255 255 / 56%) 0px 0px 10px",
                                backdropFilter: "blur(9.5px)",
                                WebkitBackdropFilter: "blur(9.5px)",
                                borderRadius: "10px",
                            }}
                        >
                            <div
                                initial={{ opacity: 0, y: -50 }}
                                animate={{ opacity: 1, y: 0 }}
                                transition={{ duration: 0.6, ease: "easeInOut" }}
                                style={{
                                    borderRadius: "10px",
                                    padding: "4rem",
                                }}
                            >
                                <Typography variant="h2" align="center" gutterBottom color={"#fff"}>
                                    Login
                                </Typography>

                                <div style={{ marginTop: 2 }}>
                                    <div initial={{ opacity: 0, x: -50 }} animate={{ opacity: 1, x: 0 }} transition={{ duration: 0.5, delay: 0.2 }} style={{ marginBottom: 20 }}>
                                        <TextFieldElement
                                            fullWidth
                                            id="login"
                                            name="login"
                                            label="Login" // Adicione um label se necessário
                                            sx={{
                                                // Estilizando o texto de entrada
                                                "& .MuiInputBase-input": {
                                                    color: "white !important",
                                                },
                                                // Estilizando o label
                                                "& .MuiInputLabel-root": {
                                                    color: "white  !important",
                                                },
                                                // Estilizando o placeholder
                                                "& .MuiInputBase-input::placeholder": {
                                                    color: "white  !important",
                                                    opacity: 1, // Garante que a cor do placeholder seja aplicada
                                                },
                                                // Estilizando as bordas do campo de entrada
                                                "& .MuiOutlinedInput-root": {
                                                    "& fieldset": {
                                                        borderColor: "white ",
                                                    },
                                                    "&:hover fieldset": {
                                                        borderColor: "white",
                                                    },
                                                    "&.Mui-focused fieldset": {
                                                        borderColor: "white",
                                                    },
                                                },
                                                "& input:-webkit-autofill": {
                                                    WebkitBoxShadow: "0 0 0px 1000px transparent inset", // Remover o fundo do autofill
                                                    WebkitTextFillColor: "white !important", // Definir a cor do texto para branco no autofill
                                                },
                                            }}
                                            InputProps={{
                                                // Ícone no final do campo de entrada
                                                endAdornment: <Icon name="User" style={{ color: "white", marginRight: "18px" }} />,
                                            }}
                                        />
                                    </div>

                                    {/* Campo de Senha */}
                                    <div initial={{ opacity: 0, x: 50 }} animate={{ opacity: 1, x: 0 }} transition={{ duration: 0.5, delay: 0.4 }} style={{ marginBottom: 20 }}>
                                        <PasswordElement
                                            fullWidth
                                            name="senha"
                                            label="Senha"
                                            renderIcon={(visable) => (visable ? <Icon name="NotVisible" /> : <Icon name="View" />)}
                                            sx={{
                                                "& .MuiInputBase-input": {
                                                    color: "white",
                                                },
                                                "& .MuiInputLabel-root": {
                                                    color: "white",
                                                },
                                                "& .MuiInputBase-input::placeholder": {
                                                    color: "white",
                                                    opacity: 1,
                                                },
                                                "& .MuiOutlinedInput-root": {
                                                    "& fieldset": {
                                                        borderColor: "white",
                                                    },
                                                    "&:hover fieldset": {
                                                        borderColor: "white",
                                                    },
                                                    "&.Mui-focused fieldset": {
                                                        borderColor: "white",
                                                    },
                                                },
                                                "& .MuiInputBase-input:-webkit-autofill": {
                                                    color: "white",
                                                },
                                                "& .MuiIconButton-root": {
                                                    color: "white",
                                                },
                                            }}
                                        />
                                    </div>
                                    <Grid sx={{ display: "flex", justifyContent: "center", pb: 2 }}>
                                        <Typography
                                            sx={{ fontSize: "13px", color: theme.palette.primary.main, cursor: "pointer" }}
                                            onClick={() => {
                                                handleLoginVisitante();
                                            }}
                                        >
                                            Entrar como visitante.
                                        </Typography>
                                    </Grid>
                                    <div initial={{ opacity: 0, scale: 0.8 }} animate={{ opacity: 1, scale: 1 }} transition={{ duration: 0.5, delay: 0.6 }} style={{ display: "flex", alignItems: "center", justifyContent: "center" }}>
                                        <NextButton type="submit" text="Entrar" />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </FormContainer>
                </div>
            </div>

            {AlertComponent}
        </>
    );
};

export default Login;
