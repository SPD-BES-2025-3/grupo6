import React from "react";
import useLocalStorage from "../localstorage";
import { jwtDecode } from "jwt-decode";
import { clearLocalStorageByPrefix } from "@/components/buttons/LogoutButton";

const UserContext = React.createContext();

export const AuthContextProvider = ({ children }) => {
    const { getLocalStorage } = useLocalStorage();

    const [user, setUser] = React.useState(false);
    const [dadosUser, setDadosUser] = React.useState(false);
    const [token, setToken] = React.useState("");
    const [showSplash, setShowSplash] = React.useState(true);

    function compararDataComAtual(dataBackend) {
        const dataAtual = new Date();
        const dataBackendConvertida = new Date(dataBackend);

        if (dataBackendConvertida > dataAtual) {
            return true;
        } else if (dataBackendConvertida < dataAtual) {
            return false;
        } else {
            return true;
        }
    }

    React.useEffect(() => {
        setShowSplash(false);

        const token = getLocalStorage("tokenJWT") || false;
        setToken(token);

        const dadosUsuario = getLocalStorage("userData") || false;
        setUser(!!dadosUsuario);
        setDadosUser(dadosUsuario);
    }, []);

    React.useEffect(() => {
        if (!!token) {
            const decodedToken = jwtDecode(token);

            const verifyExpToken = compararDataComAtual(decodedToken.exp * 1000);

            if (!!verifyExpToken) {
                setUser(true);
                setShowSplash(false);
            } else {
                setUser(false);
                setShowSplash(false);
                setDadosUser(false);
                clearLocalStorageByPrefix("biblioteca-spd");
            }
        } else {
            if (token !== "") setShowSplash(false);
        }
    }, [token]);

    const value = React.useMemo(
        () => ({
            user,
            setUser,
            token,
            setToken,
            showSplash,
            setShowSplash,
            dadosUser,
            setDadosUser,
        }),
        [user, showSplash, dadosUser, token]
    );

    return <UserContext.Provider value={value}>{children}</UserContext.Provider>;
};

export const UserAuth = () => {
    return React.useContext(UserContext);
};
