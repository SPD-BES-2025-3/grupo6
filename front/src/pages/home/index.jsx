import Icon from "@/helpers/iconHelper";
import { Box, Button, Grid, Paper, Stack, Typography, useTheme } from "@mui/material";
import PageTitle from "@/components/PageTitle";
import ResourceAvatar from "@/components/Avatar";
import { UserAuth } from "@/context/auth";
import { LogoutButton } from "@/components/buttons/LogoutButton";
import CadastrarUsuario from "@/components/buttons/CadastrarUsuario";
import CadastrarLivro from "@/components/buttons/CadastrarLivro";
import TabelaListaUsuarios from "@/components/datatable/TabelaListaUsuarios";
import TabelaListaLivros from "@/components/datatable/TabelaListaLivros";
import CadastrarExemplar from "@/components/buttons/CadastrarExemplar";
import TabelaListaExemplares from "@/components/datatable/TabelaListaExemplares";

const IndexHome = () => {
    const theme = useTheme();
    const { dadosUser } = UserAuth();
    const perfil = dadosUser && dadosUser.perfil;

    const permissao = {
        ADMINISTRADOR: perfil === "ADMINISTRADOR",
        USUARIO: perfil === "USUARIO",
        VISITANTE: perfil === "VISITANTE",
    };

    return (
        <>
            <PageTitle title="Sistema biblioteca - Início" />
            {dadosUser && (
                <>
                    <Grid container spacing={4}>
                        <Grid size={{ xs: 12, lg: !!permissao.ADMINISTRADOR || !!permissao.USUARIO ? 9 : 12 }}>
                            <Paper sx={{ p: 4, height: "100%" }}>
                                <ResourceAvatar sx={{ mt: -5, ml: -5 }} recurso={"Home"} color={theme.colors.gradients.blue2} />
                                <Box display="flex" justifyContent="center" alignItems="center" flexDirection="column" height="100%" mb={12}>
                                    <Icon size={32} name={"Welcome"} style={{ color: theme.colors.primary.main }} />
                                    <Typography mt={3} variant="h3">
                                        Bem-vindo, <span style={{ textTransform: "capitalize" }}>{dadosUser.nome_usuario}</span>!
                                    </Typography>
                                    <Typography mt={3} variant="h4" fontWeight="normal">
                                        Você está acessando o sistema como <b>{!!permissao.ADMINISTRADOR ? "administrador" : !!permissao.USUARIO ? "usuário" : "visitante"}</b>.
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
                                <Grid size={{ xs: 12, lg: 3 }}>
                                    <Paper sx={{ p: 4, height: "100%" }}>
                                        <ResourceAvatar sx={{ mt: -5, ml: -5 }} recurso={"Config"} />

                                        <Typography variant="h5" mb={3}>
                                            Ações de Administrador
                                        </Typography>
                                        <Stack spacing={4}>
                                            <CadastrarUsuario />
                                            <CadastrarLivro />
                                            <CadastrarExemplar />
                                            <Button
                                                variant="contained"
                                                fullWidth
                                                color="success"
                                                onClick={() => {
                                                    /* navegar para reserva */
                                                }}
                                            >
                                                Fazer Reserva
                                            </Button>
                                        </Stack>
                                    </Paper>
                                </Grid>

                                <TabelaListaLivros size={6} permissao={!!permissao.ADMINISTRADOR} />
                                <TabelaListaExemplares size={6} permissao={!!permissao.ADMINISTRADOR} />
                                <TabelaListaUsuarios size={12} permissao={!!permissao.ADMINISTRADOR} />
                            </>
                        )}
                        {permissao.USUARIO && (
                            <>
                                <Grid size={{ xs: 12, lg: 3 }}>
                                    <Paper sx={{ p: 4, height: "100%" }}>
                                        <ResourceAvatar sx={{ mt: -5, ml: -5 }} recurso={"Config"} />

                                        <Typography variant="h5" mb={3}>
                                            Ações de Usuário
                                        </Typography>
                                        <Stack spacing={4}>
                                            <Button
                                                variant="contained"
                                                fullWidth
                                                color="success"
                                                onClick={() => {
                                                    /* navegar para reserva */
                                                }}
                                            >
                                                Fazer Reserva
                                            </Button>
                                        </Stack>
                                    </Paper>
                                </Grid>
                                <TabelaListaExemplares />
                            </>
                        )}
                        {permissao.VISITANTE && <TabelaListaExemplares />}
                    </Grid>
                </>
            )}
        </>
    );
};
export default IndexHome;
