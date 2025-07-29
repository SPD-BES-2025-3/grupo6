import React, { useMemo } from "react";
import { CacheProvider } from "@emotion/react";
import CssBaseline from "@mui/material/CssBaseline";
import { ThemeProvider } from "@mui/material/styles";
import { LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ptBR } from "date-fns/locale";

import "@/styles/globals.css";

import Layout from "@/layout";
import PageTitle from "@/components/PageTitle";
import createEmotionCache from "@/config/createEmotionCache";
import { AuthContextProvider } from "@/context/auth";
import getTheme from "@/config/theme";
import useOurTheme from "@/context/theme";

const clientSideEmotionCache = createEmotionCache();
const queryClient = new QueryClient();

export default function MyApp(props) {
    const { Component, emotionCache = clientSideEmotionCache, pageProps } = props;

    const { theme, primary, secondary } = useOurTheme();

    const selectedTheme = useMemo(() => getTheme(theme, [primary, secondary]), [theme, [primary, secondary]]);

    return (
        <>
            <QueryClientProvider client={queryClient}>
                <PageTitle title="Sistema de biblioteca" />
                <CacheProvider value={emotionCache}>
                    <ThemeProvider theme={selectedTheme}>
                        <LocalizationProvider adapterLocale={ptBR} dateAdapter={AdapterDateFns}>
                            <AuthContextProvider>
                                <QueryClientProvider client={queryClient}>
                                    <CssBaseline />
                                    <Layout>
                                        <Component {...pageProps} />
                                    </Layout>
                                </QueryClientProvider>
                            </AuthContextProvider>
                        </LocalizationProvider>
                    </ThemeProvider>
                </CacheProvider>
            </QueryClientProvider>
        </>
    );
}
