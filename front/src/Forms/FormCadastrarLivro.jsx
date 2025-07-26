import { Grid } from "@mui/material";
import { useEffect } from "react";
import { TextFieldElement, useFormContext } from "react-hook-form-mui";

const FormCadastrarLivro = ({ data }) => {
    const { reset, setValue } = useFormContext();

    useEffect(() => {
        if (data) {
            setTimeout(() => {
                setValue("nome", data.nome);
                setValue("autor", data.autor);
                setValue("editora", data.editora);
                setValue("anoLancamento", data.anoLancamento);
                setValue("id", data.id);
            }, 100);
        }
    }, [data, setValue]);
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
