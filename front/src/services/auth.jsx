const loginRequest = (payload) => {
    if (payload.LOGIN === "admin" && payload.SENHA === "admin") {
        return { dados: { perfil: "ADMINISTRADOR", nome_usuario: "Arthur" }, result: true, message: "Sucesso!" };
    }
    if (payload.LOGIN === "convidado" && payload.SENHA === "convidado") {
        return { dados: { perfil: "USUARIO", nome_usuario: "Usuário" }, result: true, message: "Sucesso!" };
    }
};

export { loginRequest };
