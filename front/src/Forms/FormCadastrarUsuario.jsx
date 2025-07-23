import Icon from "@/helpers/iconHelper";
import { Grid } from "@mui/material";
import { PasswordElement, SelectElement, TextFieldElement } from "react-hook-form-mui";

const FormCadastrarUsuario = ({}) => {
    return (
        <Grid container spacing={2} pt={2}>
            <Grid size={{ xs: 4 }}>
                <TextFieldElement fullWidth name="nome" label="Nome" />
            </Grid>
            <Grid size={{ xs: 4 }}>
                <TextFieldElement fullWidth name="cpf" label="CPF" />
            </Grid>
            <Grid size={{ xs: 4 }}>
                <TextFieldElement fullWidth name="email" label="E-mail" />
            </Grid>
            <Grid size={{ xs: 12 }}>
                <SelectElement
                    fullWidth
                    name="permissoes"
                    label="Perfil"
                    options={[
                        { id: "ADMINISTRADOR", label: "Administrador" },
                        { id: "USUARIO", label: "Usuário" },
                        { id: "VISITANTE", label: "Visitante" },
                    ]}
                />
            </Grid>
            <Grid size={{ xs: 6 }}>
                <TextFieldElement fullWidth name="login" label="Login" />
            </Grid>
            <Grid size={{ xs: 6 }}>
                <PasswordElement fullWidth name="senha" label="Senha" renderIcon={(visable) => (visable ? <Icon name="NotVisible" /> : <Icon name="View" />)} />
            </Grid>
        </Grid>
    );
};

export default FormCadastrarUsuario;
