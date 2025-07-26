import { getExemplaresPorLivro, getLivros } from "@/services/livros";
import { Grid } from "@mui/material";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { SelectElement, TextareaAutosizeElement, useFormContext } from "react-hook-form-mui";
import { DatePickerElement } from "react-hook-form-mui/date-pickers";

const FormReservarExemplar = ({ idLivro, nomeLivro }) => {
    const { setValue } = useFormContext();

    const [livrosOptions, setLivrosOptions] = useState([]);
    const exemplaresData = useQuery({
        queryKey: ["get-exemplares-livro"],
        queryFn: async () => {
            const fetchFunc = getExemplaresPorLivro;
            if (!!fetchFunc) {
                const response = await getExemplaresPorLivro(idLivro);
                return response;
            }
        },
    });

    useEffect(() => {
        if (exemplaresData.data) {
            const opts = exemplaresData.data.map((livro) => ({
                id: livro.id,
                label: nomeLivro + " - " + livro.conservacao.toLowerCase(),
            }));
            setLivrosOptions(opts);
        }
    }, [exemplaresData.data]);

    // useEffect(() => {
    //     if (data && livrosOptions.length > 0) {
    //         setTimeout(() => {
    //             setValue("disponibilidade", data.disponibilidade);
    //             setValue("conservacao", data.conservacao);
    //             setValue("numeroEdicao", data.numeroEdicao);
    //             setValue("idLivro", data.livroId);
    //             setValue("codigoIdentificacao", data.codigoIdentificacao);
    //             setValue("id", data.id);
    //         }, 100);
    //     }
    // }, [data, livrosOptions, setValue]);

    return (
        <Grid container spacing={2} pt={2}>
            <Grid size={{ xs: 12 }}>
                <SelectElement fullWidth name="idExemplar" label="Exemplar Desejado" options={livrosOptions} />
            </Grid>

            <Grid size={{ xs: 12 }}>
                <DatePickerElement
                    slotProps={{
                        field: { clearable: true },
                        actionBar: {
                            actions: ["clear"],
                        },
                    }}
                    fullWidth
                    name="dataPrevistaRetirada"
                    label="Data Prevista para Retirada"
                    sx={{ width: "100%" }}
                />{" "}
            </Grid>
            <Grid size={{ xs: 12 }}>
                <TextareaAutosizeElement fullWidth label="Observações" name="observacoes" rows={5} />
            </Grid>
        </Grid>
    );
};

export default FormReservarExemplar;
