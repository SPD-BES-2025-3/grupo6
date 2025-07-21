import Icon from "@/helpers/iconHelper";
import { Box, Grid, Paper, Typography, useTheme } from "@mui/material";
import PageTitle from "@/components/PageTitle";
import ResourceAvatar from "@/components/Avatar";
import { UserAuth } from "@/context/auth";
import { LogoutButton } from "@/components/buttons/LogoutButton";

const IndexHome = () => {
    const theme = useTheme();
    const { dadosUser } = UserAuth();
    const perfil = dadosUser && dadosUser.perfil;

    const permissao = {
        ADMINISTRADOR: perfil === "ADMINISTRADOR",
        VISITANTE: perfil === "VISITANTE",
    };

    return (
        <>
            <PageTitle title="Sistema biblioteca - Início" />
            {dadosUser && (
                <>
                    <Grid container spacing={4} maxWidth={1280} margin="0 auto">
                        <Grid size={{ xs: 12, lg: 12 }}>
                            <Paper sx={{ p: 4, height: "100%" }}>
                                <ResourceAvatar sx={{ mt: -5, ml: -5 }} recurso={"Home"} color={theme.colors.gradients.blue2} />
                                <Box display="flex" justifyContent="center" alignItems="center" flexDirection="column" height="100%" mb={12}>
                                    <Icon size={32} name={"Welcome"} style={{ color: theme.colors.primary.main }} />
                                    <Typography mt={3} variant="h3">
                                        Bem-vindo, <span style={{ textTransform: "capitalize" }}>{dadosUser.nome_usuario}</span>!
                                    </Typography>
                                    <Typography mt={3} variant="h4" fontWeight="normal">
                                        Você está acessando o sistema como <b>{!!permissao.ADMINISTRADOR ? "administrador" : "convidado"}</b>.
                                    </Typography>
                                    <Box mt={5} display="flex" flexDirection="column" alignItems="center">
                                        <Typography variant="h4" fontWeight="normal" mb={1}>
                                            Sair do sistema
                                        </Typography>
                                        <LogoutButton />
                                    </Box>
                                </Box>
                            </Paper>
                        </Grid>
                        {permissao.ADMINISTRADOR && (
                            <>
                                <Grid size={{ sm: 12 }}>{/* <TemplateLivros /> */}administrador</Grid>
                            </>
                        )}
                        {permissao.VISITANTE && (
                             <>
                                <Grid size={{ sm: 12 }}>{/* <TemplateLivros /> */}convidado</Grid>
                            </>
                        )}
                    </Grid>
                </>
            )}
        </>
    );
};
export default IndexHome;
