import React from "react";

const UserContext = React.createContext();

export const AuthContextProvider = ({ children }) => {
    const [user, setUser] = React.useState(false);
    const [dadosUser, setDadosUser] = React.useState([]);

    const [showSplash, setShowSplash] = React.useState(true);
    const [animation, setAnimation] = React.useState(false);


    React.useEffect(() => {
        setShowSplash(false);
    }, []);
    const value = React.useMemo(
        () => ({
            user,
            setUser,
            showSplash,
            setShowSplash,
            animation,
            setAnimation,
            
            dadosUser,
            setDadosUser,
        }),
        [user, showSplash, animation, dadosUser],
    );

    return <UserContext.Provider value={value}>{children}</UserContext.Provider>;
};

export const UserAuth = () => {
    return React.useContext(UserContext);
};

