import React from "react";
import useLocalStorage from "../localstorage";

const UserContext = React.createContext();

export const AuthContextProvider = ({ children }) => {
    const { getLocalStorage } = useLocalStorage();

    const [user, setUser] = React.useState(false);
    const [dadosUser, setDadosUser] = React.useState(false);

    const [showSplash, setShowSplash] = React.useState(true);

    React.useEffect(() => {
        setShowSplash(false);
    }, []);

    React.useEffect(() => {
        const dadosUsuario = getLocalStorage("userData") || false;
        setUser(!!dadosUsuario);
        setDadosUser(dadosUsuario);
    }, []);

    const value = React.useMemo(
        () => ({
            user,
            setUser,
            showSplash,
            setShowSplash,
            dadosUser,
            setDadosUser,
        }),
        [user, showSplash, dadosUser],
    );

    return <UserContext.Provider value={value}>{children}</UserContext.Provider>;
};

export const UserAuth = () => {
    return React.useContext(UserContext);
};
