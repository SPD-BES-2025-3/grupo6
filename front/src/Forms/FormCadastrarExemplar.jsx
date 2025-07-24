import { getLivros } from "@/services/livros";
import { Grid } from "@mui/material";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { SelectElement, TextFieldElement } from "react-hook-form-mui";

const FormCadastrarExemplar = ({}) => {
    const [livrosOptions, setLivrosOptions] = useState([]);
    const livrosData = useQuery({
        queryKey: ["get-livros"],
        queryFn: async () => {
            const fetchFunc = getLivros;
            if (!!fetchFunc) {
                const response = await getLivros();
                return response;
            }
        },
    });

    useEffect(() => {
        if (livrosData.data) {
            const opts = livrosData.data.map((livro) => ({
                id: livro.id,
                label: livro.nome,
            }));
            setLivrosOptions(opts);
        }
    }, [livrosData.data]);

    return (
        <Grid container spacing={2} pt={2}>
            <Grid size={{ xs: 6 }}>
                <SelectElement fullWidth name="idLivro" label="Livro" options={livrosOptions} />
            </Grid>
            <Grid size={{ xs: 6 }}>
                <SelectElement
                    fullWidth
                    name="conservacao"
                    label="Estado de Conservação"
                    options={[
                        { id: "NOVO", label: "Novo" },
                        { id: "BOM", label: "Bom" },
                        { id: "REGULAR", label: "Regular" },
                        { id: "RUIM", label: "Ruim" },
                    ]}
                />
            </Grid>
            <Grid size={{ xs: 6 }}>
                <TextFieldElement fullWidth name="numeroEdicao" label="Edição" />
            </Grid>
            <Grid size={{ xs: 6 }}>
                <SelectElement
                    fullWidth
                    name="disponibilidade"
                    label="Disponibilidade"
                    options={[
                        { id: "DISPONIVEL", label: "Disponível" },
                        { id: "EMPRESTADO", label: "Emprestado" },
                        { id: "RESERVADO", label: "Reservado" },
                        { id: "INDISPONIVEL", label: "Indisponível" },
                    ]}
                />
            </Grid>
        </Grid>
    );
};

export default FormCadastrarExemplar;
