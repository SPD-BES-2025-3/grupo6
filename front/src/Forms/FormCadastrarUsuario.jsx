import Icon from "@/helpers/iconHelper";
import { Grid } from "@mui/material";
import { id } from "date-fns/locale";
import { PasswordElement, SelectElement, TextareaAutosizeElement, TextFieldElement } from "react-hook-form-mui";
import { DatePickerElement } from "react-hook-form-mui/date-pickers";

const FormCadastrarUsuario = ({}) => {
    return (
        <Grid container spacing={2} pt={2}>
            <Grid size={{ xs: 3 }}>
                <TextFieldElement fullWidth name="nome" label="Nome" />
            </Grid>
            <Grid size={{ xs: 3 }}>
                <TextFieldElement fullWidth name="cpf" label="CPF" />
            </Grid>
            <Grid size={{ xs: 3 }}>
                <TextFieldElement fullWidth name="email" label="E-mail" />
            </Grid>
            <Grid size={{ xs: 3 }}>
                <SelectElement
                    fullWidth
                    name="perfil"
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
