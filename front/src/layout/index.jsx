import React from "react";
import { CircularProgress, Grid } from "@mui/material";
import Login from "./Auth";
import { UserAuth } from "@/context/auth";

const Layout = ({ children, title }) => {
    const { user, showSplash, animation, setAnimation } = UserAuth();

    React.useEffect(() => {
        if (!!animation) {
            setTimeout(() => {
                setAnimation(false);
            }, 6000);
        }
    }, [animation]);
    return (
        <>
            {!!showSplash && (
                <Grid container sx={{ width: "100vw", height: "100vh", display: "flex", justifyContent: "center", alignItems: "center", flexDirection: "column" }}>
                    <CircularProgress />
                </Grid>
            )}
            {!showSplash && !user && <Login />}
            {!showSplash && !!user && (
                <>
                    {/* <Bar /> */}

                    <div style={{ padding: 20, width: "100%", display: "flex", flexDirection: "column", flexGrow: 1, marginTop: 2 }}>{children}</div>
                </>
            )}
        </>
    );
};

export default Layout;
