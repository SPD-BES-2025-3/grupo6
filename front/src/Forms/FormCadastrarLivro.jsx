import { Grid } from "@mui/material";
import { TextFieldElement } from "react-hook-form-mui";

const FormCadastrarLivro = ({}) => {
    return (
        <Grid container spacing={2} pt={2}>
            <Grid size={{ xs: 12 }}>
                <TextFieldElement fullWidth name="nome" label="Nome" />
            </Grid>
            <Grid size={{ xs: 4 }}>
                <TextFieldElement fullWidth name="autor" label="Autor" />
            </Grid>
            <Grid size={{ xs: 4 }}>
                <TextFieldElement fullWidth name="editora" label="Editora" />
            </Grid>
            <Grid size={{ xs: 4 }}>
                <TextFieldElement fullWidth name="anoLancamento" label="Ano de Lançamento" />
            </Grid>
        </Grid>
    );
};

export default FormCadastrarLivro;
