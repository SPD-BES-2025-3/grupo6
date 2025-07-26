function capitalizarPrimeiraLetra(texto) {
    if (!!texto) {
        return texto.charAt(0).toUpperCase() + texto.slice(1).toLowerCase();
    }
}
function convertedArray(arr, id) {
    return arr.map((obj) => ({
        ...obj,
        [id]: Number(obj[id]),
    }));
}
function montaMascaraCPF_CNPJ(valor) {
    if (!!valor) {
        // Remove caracteres não numéricos
        const numeros = valor.toString().replace(/\D/g, "");

        if (numeros.length <= 11) {
            // Formata CPF
            return numeros.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4");
        } else if (numeros.length > 11) {
            // Formata CNPJ
            return numeros.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, "$1.$2.$3/$4-$5");
        } else {
            // Retorna não formatado caso não tenha um tamanho válido
            return valor;
        }
    } else {
        return "";
    }
}
function formatDate(date) {
    if (!!date) {
        const dateObject = new Date(date);

        const day = String(dateObject.getDate()).padStart(2, "0");
        const month = String(dateObject.getMonth() + 1).padStart(2, "0");
        const year = String(dateObject.getFullYear());

        const formattedDate = `${day}/${month}/${year}`;
        return formattedDate;
    } else {
        return "";
    }
}

export { capitalizarPrimeiraLetra, convertedArray, montaMascaraCPF_CNPJ, formatDate };
