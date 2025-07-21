function capitalizarPrimeiraLetra(texto) {
    if (!!texto) {
        return texto.charAt(0).toUpperCase() + texto.slice(1).toLowerCase();
    }
}

export {
    capitalizarPrimeiraLetra,
};
