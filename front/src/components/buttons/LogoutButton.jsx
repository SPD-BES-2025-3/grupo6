import { UserAuth } from "@/context/auth";
import useLocalStorage from "@/context/localstorage";
import Icon from "@/helpers/iconHelper";
import { IconButton, useTheme } from "@mui/material";

export const LogoutButton = () => {
    const { setUser } = UserAuth();
    const theme = useTheme();

    const handleLogout = () => {
        clearLocalStorageByPrefix("biblioteca-spd");
        setUser(false);
    };

    return (
        <div>
            <IconButton onClick={handleLogout} id="logoutButton" sx={{ color: theme.colors.error.dark }}>
                <Icon name="Logout" size={18} />
            </IconButton>
        </div>
    );
};

export const clearLocalStorageByPrefix = (prefix) => {
    const { removeLocalStorage } = useLocalStorage.getState();
    for (let i = localStorage.length - 1; i >= 0; i--) {
        const key = localStorage.key(i);

        if (key && key.startsWith(prefix)) {
            removeLocalStorage(key);
        }
    }
};
