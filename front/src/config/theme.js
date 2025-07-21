import { alpha, darken, Fade, lighten } from "@mui/material";
import { createTheme } from "@mui/material/styles";

import { getColors, setThemeColors, themeColors } from "./colors";

const getTheme = (mode, colorArray) => {
    setThemeColors(colorArray[0], colorArray[1]);
    const colors = getColors();

    if (!colors) {
        throw new Error("As cores primária e secundária não foram definidas corretamente.");
    }

    /* 
       A PALETTE serve para configurar as cores padrões do theme do MUI, por exemplo ao chamar um
       <Button variant="contained" color="secondary"/> 
       ele vai aplicar as cores do palette secondary 
    */
    const palette = {
        mode: mode,
        common: {
            black: colors.alpha.black[100],
            white: colors.alpha.white[100],
        },
        primary: {
            ...colors.primary,
            contrastText: colors.alpha.white[100],
        },
        secondary: {
            ...colors.secondary,
            contrastText: colors.alpha.white[100],
        },
        error: {
            ...colors.error,
            contrastText: colors.alpha.white[100],
        },
        success: {
            ...colors.success,
            contrastText: colors.alpha.white[100],
        },
        info: {
            ...colors.info,
            contrastText: colors.alpha.white[100],
        },
        warning: {
            ...colors.warning,
            contrastText: colors.alpha.white[100],
        },
        background: {
            paper: colors.alpha.white[100],
            default: themeColors.lightbackground,
        },
        text:
            {
                primary: colors.alpha.black[100],
                secondary: colors.alpha.black[70],
                disabled: colors.alpha.black[50],
            },
    };

    // Combinação das configurações do tema
    return createTheme({
        palette,
        colors: {
            ...colors, // Inclui todas as propriedades de cores dinâmicas e estáticas
        },
        spacing: 5, // adiciona um spacing padrão em todo o systema que multiplica esse numero ao fazer paddings de qualquer natureza
        breakpoints: {
            // tamanho em que o systema aplicara breakpoints
            values: {
                xs: 0,
                sm: 600,
                md: 960,
                lg: 1280,
                xl: 1840,
            },
        },
        shape: {
            //adiciona um borderRadius padrão em todos os componentes do theme (que não forem sobrescritos no components do theme)
            borderRadius: 10,
        },
        //typographys customizadas do theme
        typography: {
            fontFamily: '"Inter", -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji"',
            h1: {
                fontWeight: 700,
                fontSize: 35,
                color:  themeColors.textPrimary,
            },
            h2: {
                fontWeight: 700,
                fontSize: 30,
                color:  themeColors.textPrimary,
            },
            h3: {
                fontWeight: 700,
                fontSize: 20,
                lineHeight: 1.4,
                color:  themeColors.textPrimary,
            },
            h4: {
                fontWeight: 700,
                fontSize: 16,
                color:  themeColors.textPrimary,
            },
            h5: {
                fontWeight: 700,
                fontSize: 14,
                color:  themeColors.textPrimary,
            },
            h6: {
                fontSize: 15,
                color:  themeColors.textPrimary,
            },
            body1: {
                fontSize: 14,
                color:  themeColors.textPrimary,
            },
            body2: {
                fontSize: 14,
                color:  themeColors.textPrimary,
            },
            button: {
                fontWeight: 600,
                color: "white",
            },
            caption: {
                fontSize: 13,
                textTransform: "uppercase",
                color:  themeColors.textSecondary,
            },
            subtitle1: {
                fontSize: 14,
                color: themeColors.textSecondary,
            },
            subtitle2: {
                fontWeight: 400,
                fontSize: 15,
                color: themeColors.textSecondary,
            },
            overline: {
                fontSize: 13,
                fontWeight: 700,
                color:  themeColors.textPrimary,
                textTransform: "uppercase",
            },
        },

        components: {
            MuiBackdrop: {
                styleOverrides: {
                    root: {
                        backgroundColor: alpha(darken(themeColors.primaryAlt, 0.4), 0.2),
                        backdropFilter: "none", // O blur será aplicado apenas em modais

                        // Aplica o blur apenas em modais
                        "&.modal-backdrop": {
                            backdropFilter: "blur(2px)",
                        },

                        "&.MuiBackdrop-invisible": {
                            backgroundColor: "transparent",
                            backdropFilter: "none", // Sem blur em backdrops invisíveis
                        },
                    },
                },
            },

            MuiCssBaseline: {
                styleOverrides: {
                    "html, body": {
                        width: "100%",
                        height: "100%",
                    },
                    body: {
                        display: "flex",
                        flexDirection: "column",
                        minHeight: "100%",
                        width: "100%",
                        flex: 1,
                        backgroundColor:  themeColors.lightbackground,
                    },
                    "#root": {
                        width: "100%",
                        height: "100%",
                        display: "flex",
                        flex: 1,
                        flexDirection: "column",
                    },
                    a: {
                        "& .MuiTypography-root": {
                            color: themeColors.primary,
                            transition: "color 200ms ease",
                        },
                    },

                    html: {
                        display: "flex",
                        flexDirection: "column",
                        minHeight: "100%",
                        width: "100%",
                        MozOsxFontSmoothing: "grayscale",
                        WebkitFontSmoothing: "antialiased",
                    },
                    ".child-popover .MuiPaper-root .MuiList-root": {
                        flexDirection: "column",
                    },
                    "#nprogress": {
                        pointerEvents: "none",
                    },
                    "#nprogress .bar": {
                        background: colors.primary.lighter,
                    },
                    "#nprogress .spinner-icon": {
                        borderTopColor: colors.primary.lighter,
                        borderLeftColor: colors.primary.lighter,
                    },
                    "#nprogress .peg": {
                        boxShadow: "0 0 15px " + colors.primary.lighter + ", 0 0 8px" + colors.primary.light,
                    },
                    ":root": {
                        "--swiper-theme-color": colors.primary.main,
                    },

                    "::-webkit-scrollbar-thumb:hover": {
                        backgroundColor: themeColors.primary,
                    },
                    code: {
                        background: colors.info.lighter,
                        color: colors.info.dark,
                        padding: 4,
                    },
                    "@keyframes ripple": {
                        "0%": {
                            transform: "scale(.8)",
                            opacity: 1,
                        },
                        "100%": {
                            transform: "scale(2.8)",
                            opacity: 0,
                        },
                    },
                    "@keyframes float": {
                        "0%": {
                            transform: "translate(0%, 0%)",
                        },
                        "100%": {
                            transform: "translate(3%, 3%)",
                        },
                    },
                    "input:-webkit-autofill, input:-webkit-autofill:hover, input:-webkit-autofill:focus, textarea:-webkit-autofill, textarea:-webkit-autofill:hover textarea:-webkit-autofill:focus, select:-webkit-autofill, select:-webkit-autofill:hover, select:-webkit-autofill:focus": {
                        webkitTextFillColor:  themeColors.textPrimary,
                        webkitBoxShadow: "0 0 0px 1000px rgba(0,0,0,0.001) inset !important",
                        transition: "background-color 5000s ease-in-out 0s",
                    },
                },
            },

            MuiAlert: {
                defaultProps: {
                    size: "small",
                },
                styleOverrides: {
                    root: {
                        "&.MuiAlert-standardSuccess": {
                            backgroundColor: alpha(themeColors.success, 0.25),
                            color: themeColors.success,
                            "& .MuiAlertTitle-root": {
                                color: themeColors.success, // Cor para o título
                            },
                        },
                        "&.MuiAlert-standardError": {
                            backgroundColor: alpha(themeColors.error, 0.25),
                            color: themeColors.error,
                            "& .MuiAlertTitle-root": {
                                color: themeColors.error,
                            },
                            
                        },
                        "&.MuiAlert-standardWarning": {
                            backgroundColor: alpha(themeColors.warning, 0.25),
                            color: themeColors.warning,
                            "& .MuiAlertTitle-root": {
                                color: themeColors.warning,
                            },
                        },
                        "&.MuiAlert-standardInfo": {
                            backgroundColor: alpha(themeColors.info, 0.25),
                            color: themeColors.info,
                            "& .MuiAlertTitle-root": {
                                color: themeColors.info,
                            },
                        },
                    },
                },
            },

            MuiCheckbox: {
                defaultProps: {
                    size: "small",
                },
                styleOverrides: {
                    root: {
                        transition: "transform 200ms ease, background-color 200ms ease",
                        borderRadius: "50%",
                        padding: 4,
                        "&.Mui-checked": {
                            color: themeColors.primary,
                            transform: "scale(1.1)", // Efeito de escala
                        },
                        "&:hover": {
                            backgroundColor: alpha(themeColors.primary, 0.1), // Fundo circular no hover
                        },
                        "& .MuiSvgIcon-root": {
                            fontSize: 20, // Personaliza o tamanho do ícone
                        },
                    },
                },
            },

            MuiListSubheader: {
                styleOverrides: {
                    colorPrimary: {
                        fontWeight: "bold",
                        lineHeight: "40px",
                        fontSize: 13,
                        background: colors.alpha.black[5],
                        color: colors.alpha.black[70],
                    },
                },
            },

            MuiCardHeader: {
                styleOverrides: {
                    action: {
                        marginTop: -5,
                        marginBottom: -5,
                    },
                    title: {
                        fontSize: 15,
                        color:  undefined,
                    },
                },
            },

            MuiInputAdornment: {
                styleOverrides: {
                    positionStart: {
                        marginTop: "0px !important",
                    },
                    root: {
                        "&:not(.MuiInputAdornment-hiddenLabel)": {
                            marginTop: "0px !important",
                        },
                    },
                },
            },
            MuiDialog: {
                defaultProps: {
                    slotProps: {
                        container: {
                            onClick: (e) => {
                                e.stopPropagation();
                            },
                        },
                    },
                    TransitionComponent: Fade, // Componente de transição para animação
                    transitionDuration: {
                        enter: 300, // Duração da animação de entrada em ms
                        exit: 200, // Duração da animação de saída em ms
                    },
                },
                styleOverrides: {
                    paper: {
                        overflowY: "hidden",
                        transition: "transform 0.3s ease, opacity 0.3s ease",
                        "&.MuiDialog-paper--open": {
                            opacity: 1,
                            transform: "scale(1)",
                        },
                        "&.MuiDialog-paper--close": {
                            opacity: 0,
                            transform: "scale(0.9)",
                        },
                        transition: "transform 0.3s ease, opacity 0.3s ease",
                       
                    },
                    paperFullScreen: {
                        overflowY: "scroll",
                    },
                },
            },

            MuiRadio: {
                styleOverrides: {
                    root: {
                        borderRadius: "50px", // Mantendo a borda arredondada
                        color:  undefined, // Cor adaptativa com base no modo
                        transition: "all 0.3s ease", // Transição suave para todas as propriedades
                        "&:hover": {
                            backgroundColor:  alpha(colors.primary.main, 0.1), // Efeito hover suave
                        },
                        "&.Mui-checked": {
                            transform: "scale(1.1)", // Efeito de escala ao marcar
                            transition: "transform 0.2s ease", // Suavizando a transição da transformação
                            color: colors.primary.main, // Cor personalizada quando marcado
                        },
                    },
                },
            },

            MuiChip: {
                styleOverrides: {
                    root: {
                        // borderRadius: "12px", // Bordas suaves, minimalistas
                        fontWeight: 500, // Peso de fonte médio para manter legibilidade
                        padding: "0px", // Padding mais compacto
                        height: "22px", // Altura menor, padrão do estilo Flat
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        transition: "background-color 200ms ease, transform 150ms ease",

                        "&.Mui-disabled": {
                            backgroundColor: "transparent", // Sem fundo para disabled
                            cursor: "not-allowed",
                        },
                    },
label:{
textOverflow:"clip"
},
                    colorPrimary: {
                        backgroundColor: alpha(themeColors.primary, 0.25),
                        color: themeColors.primary, // 50% de opacidade
                    },
                    colorSecondary: {
                        backgroundColor: alpha(themeColors.secondary, 0.25),
                        color: themeColors.secondary, // 50% de opacidade
                    },
                    colorSuccess: {
                        backgroundColor: alpha(themeColors.success, 0.25),
                        color: themeColors.success, // 50% de opacidade
                    },
                    colorError: {
                        backgroundColor: alpha(themeColors.error, 0.25),
                        color: themeColors.error, // 50% de opacidade
                    },
                    colorInfo: {
                        backgroundColor: alpha(themeColors.info, 0.25),
                        color: themeColors.info, // 50% de opacidade
                    },
                    colorWarning: {
                        backgroundColor: alpha(themeColors.warning, 0.25),
                        color: themeColors.warning, // 50% de opacidade
                    },
                    deleteIcon: {
                        fontSize: "1rem", // Ícone menor e proporcional
                        marginLeft: "4px", // Espaçamento adequado
                    },
                    avatar: {
                        width: "24px", // Menor e mais integrado
                        height: "24px",
                        fontSize: "0.875rem", // Fonte proporcional
                    },
                    icon: {
                        fontSize: "1rem", // Ícone menor e proporcional
                        marginLeft: "4px",
                        marginRight: "4px",
                    },
                },
            },

            MuiAccordion: {
                styleOverrides: {
                    root: {
                        "&.Mui-expanded": {
                            margin: 0,
                        },
                       
                    },
                },
            },
            MuiAccordionSummary: {
                styleOverrides: {
                    root: {
                        userSelect: "text",
                    },
                },
            },

            MuiAvatar: {
                styleOverrides: {
                    root: {
                        fontSize: 14,
                        fontWeight: "bold",
                       
                    },
                    colorDefault: {
                        background: colors.alpha.black[30],
                        color: colors.alpha.white[100],
                    },
                },
            },

            MuiAvatarGroup: {
                styleOverrides: {
                    root: {
                        alignItems: "center",
                    },
                    avatar: {
                        background: colors.alpha.black[10],
                        fontSize: 13,
                        color: colors.alpha.black[70],
                        fontWeight: "bold",

                        "&:first-of-type": {
                            border: 0,
                            background: "transparent",
                        },
                    },
                },
            },

            MuiListItemAvatar: {
                styleOverrides: {
                    alignItemsFlexStart: {
                        marginTop: 0,
                    },
                },
            },

            MuiPaginationItem: {
                styleOverrides: {
                    page: {
                        fontSize: 13,
                        fontWeight: "bold",
                        transition: "all .2s",
                    },
                    textPrimary: {
                        "&.Mui-selected": {
                            boxShadow: colors.shadows.primary,
                        },
                        "&.MuiButtonBase-root:hover": {
                            background: colors.alpha.black[5],
                        },
                        "&.Mui-selected.MuiButtonBase-root:hover": {
                            background: colors.primary.main,
                        },
                        color:  undefined,
                    },
                },
            },

            MuiButton: {
                styleOverrides: {
                    root: {
                        fontWeight: "bold",
                        paddingLeft: 8,
                        paddingRight: 8,
                        paddingTop: 4,
                        paddingBottom: 4,
                        // borderRadius: 8, // Reduzindo o raio para se aproximar do NextUI
                        textTransform: "none", // Removendo a capitalização para combinar com o estilo do NextUI
                        transition: "all 0.3s ease", // Suavizando as transições

                        ".MuiSvgIcon-root": {
                            transition: "all 0.2s ease",
                            fontSize: "1.25rem", // Ajustando o tamanho do ícone
                        },
                        
                    },
                    endIcon: {
                        marginRight: -8,
                    },
                    textSizeSmall: {
                        padding: "6px 12px",
                        backgroundColor: "transparent", // Sem fundo para botões de texto
                    },
                    textSizeMedium: {
                        padding: "8px 16px",
                        backgroundColor: "transparent",
                    },
                    textSizeLarge: {
                        padding: "10px 18px",
                        backgroundColor: "transparent",
                    },
                },
            },

            MuiToggleButton: {
                defaultProps: {
                    disableRipple: true,
                },
                styleOverrides: {
                    root: {
                        color: colors.primary.main,
                        background: colors.alpha.white[100],
                        transition: "all .2s",

                        "&:hover, &.Mui-selected, &.Mui-selected:hover": {
                            color: colors.alpha.white[100],
                            background: colors.primary.main,
                        },
                    },
                },
            },

            MuiIconButton: {
                styleOverrides: {
                    root: {
                        padding: 8,

                        // "& .MuiTouchRipple-root": {
                        //     borderRadius: 8,
                        // },
                    },
                    sizeSmall: {
                        padding: 4,
                    },
                },
            },

            MuiListItemText: {
                styleOverrides: {
                    root: {
                        margin: 0,
                    },
                },
            },

            MuiListItemButton: {
                styleOverrides: {
                    root: {
                        "& .MuiTouchRipple-root": {
                            opacity: 0.3,
                        },
                    },
                },
            },

            MuiDivider: {
                styleOverrides: {
                    root: {
                        background: "white",
                    },
                },
            },

            MuiPaper: {
                styleOverrides: {
                    root: {
                        padding: 0,
                        //transition: "transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out",
                     
                    },
                    outlined: {
                        boxShadow: colors.shadows.card,
                       
                        overflowY: "hidden",
                    },
                },
            },

            MuiLink: {
                styleOverrides: {
                    root: {
                        color: themeColors.primary,
                        textDecoration: "none",
                        transition: "color 200ms ease",
                        WebkitBackgroundClip: "text", // Clipa o fundo ao texto
                        "&:hover": {
                            color: themeColors.primary,
                        },
                    },
                },
            },

            MuiBreadcrumbs: {
                styleOverrides: {
                    root: {
                        "& li": {
                            padding: 0,
                        },
                        "& svg": {
                            color: themeColors.primary,
                        },
                        "&:hover": {
                            color: themeColors.primary,
                        },
                    },
                    separator: {
                        margin: 0,
                    },
                },
            },

            MuiLinearProgress: {
                styleOverrides: {
                    root: {
                        height: 6,
                        
                    },
                   
                },
            },

            MuiSlider: {
                styleOverrides: {
                    root: {
                        "& .MuiSlider-valueLabelCircle, .MuiSlider-valueLabelLabel": {
                            transform: "none",
                        },
                        "& .MuiSlider-valueLabel": {
                            background: colors.alpha.black[100],
                            color: colors.alpha.white[100],
                        },
                    },
                },
            },

            MuiList: {
                styleOverrides: {
                    root: {
                        padding: 2,

                        "& .MuiListItem-button": {
                            transition: "all .2s",

                            "& > .MuiSvgIcon-root": {
                                minWidth: 34,
                            },

                            "& .MuiTouchRipple-root": {
                                opacity: 0.2,
                            },
                        },
                        "& .MuiListItem-root.MuiButtonBase-root.Mui-selected": {
                            backgroundColor:  alpha(colors.primary.lighter, 0.4),
                        },
                        "& .MuiMenuItem-root.MuiButtonBase-root:active": {
                            backgroundColor: alpha(colors.primary.lighter, 0.4),
                        },
                        "& .MuiMenuItem-root.MuiButtonBase-root .MuiTouchRipple-root": {
                            opacity: 0.2,
                        },
                    },
                    padding: {
                        padding: "12px",
                        "& .MuiListItem-button": {
                            margin: "1px 0",
                        },
                    },
                },
            },

            MuiTabs: {
                styleOverrides: {
                    root: {
                        height: 38,
                        minHeight: 38,
                    },
                },
            },

            MuiTab: {
                styleOverrides: {
                    root: {
                        padding: 0,
                        height: 38,
                        minHeight: 38,
                        transition: "color .2s",
                        textTransform: "capitalize",
                        display: "flex",
                        flexDirection: "row",
                        justifyContent: "flex-start",
                        alignItems: "center",
                        gap: 4,
                        // borderRadius: 8,

                        "&.MuiButtonBase-root": {
                            minWidth: "auto",
                            paddingLeft: 20,
                            paddingRight: 20,
                            marginRight: 4,
                        },
                        "&.Mui-selected, &.Mui-selected:hover": {
                            color: colors.alpha.white[100],
                            zIndex: 5,

                            background: colors.gradients.theme,
                        },
                        "&:hover": {
                            color: colors.alpha.white[100],
                            background: colors.gradients.theme,
                        },
                    },
                },
            },
            MuiMenu: {
                defaultProps: {
                    size: "small",
                },
                styleOverrides: {
                    paper: {
                        boxShadow: "0 8px 16px rgba(0, 0, 0, 0.1)",
                        // Efeito Glass (blur + transparência)
                        background:  "rgba(255, 255, 255, 0.8)",
                        backdropFilter: "blur(12px)",
                        WebkitBackdropFilter: "blur(12px)", // Para compatibilidade com Safari
                        border:  "1px solid rgba(0, 0, 0, 0.1)",
                    },
                    list: {
                        padding: "8px 8px",
                    },
                },
            },
            MuiMenuItem: {
                defaultProps: {
                    size: "small",
                },
                styleOverrides: {
                    root: {
                        transition: "background-color 200ms ease, transform 150ms ease",
                        color:  themeColors.textPrimary,
                        borderRadius: "4px", // Adiciona borda arredondada para combinar com o estilo
                        "&:hover": {
                            color: themeColors.primary,
                            transform: "scale(1.001)",
                            backgroundColor: "rgba(0, 0, 0, 0.05)",
                        },
                        "&.Mui-selected": {
                            color: themeColors.primary,
                            backgroundColor:"rgba(0, 0, 0, 0.08)",
                            "&:hover": {
                                backgroundColor:  "rgba(0, 0, 0, 0.1)",
                            },
                        },
                    },
                },
            },

            MuiListItem: {
                styleOverrides: {
                    root: {
                        "&.MuiButtonBase-root": {
                            color: colors.secondary.main,

                            "&:hover, &:active, &.active, &.Mui-selected": {
                                color: colors.alpha.black[100],
                                background: lighten(colors.primary.lighter, 0.5),
                            },
                        },
                    },
                },
            },

            MuiToolbar: {
                styleOverrides: {
                    root: {
                        minHeight: "0 !important",
                        padding: "0 !important",
                    },
                },
            },

            MuiTimelineDot: {
                styleOverrides: {
                    root: {
                        margin: 0,
                        zIndex: 5,
                        position: "absolute",
                        top: "50%",
                        marginTop: -6,
                        left: -6,
                    },
                    outlined: {
                        backgroundColor: colors.alpha.white[100],
                        boxShadow: "0 0 0 6px " + colors.alpha.white[100],
                    },
                    outlinedPrimary: {
                        backgroundColor: colors.alpha.white[100],
                        boxShadow: "0 0 0 6px " + colors.alpha.white[100],
                    },
                },
            },

            MuiTimelineConnector: {
                styleOverrides: {
                    root: {
                        position: "absolute",
                        height: "100%",
                        top: 0,
                        borderRadius: 50,
                        backgroundColor: colors.alpha.black[10],
                    },
                },
            },

            MuiTimelineItem: {
                styleOverrides: {
                    root: {
                        minHeight: 0,
                        padding: "8px 0",

                        "&:before": {
                            display: "none",
                        },
                       
                    },
                    missingOppositeContent: {
                        "&:before": {
                            display: "none",
                        },
                    },
                },
            },

            MuiTooltip: {
                styleOverrides: {
                    tooltip: {
                        backgroundColor: alpha(colors.primary.main, 0.95),
                      
                        padding: "8px 16px",
                        fontSize: 13,
                    },
                    arrow: {
                        color: alpha(colors.primary.main, 0.95),
                    },
                },
            },

            MuiSwitch: {
                styleOverrides: {
                    root: {
                        height: 35,
                        width: 65,
                        overflow: "visible",

                        "& .MuiButtonBase-root": {
                            position: "absolute",
                            padding: 6,
                            transition: "left 150ms cubic-bezier(0.4, 0, 0.2, 1) 0ms, transform 150ms cubic-bezier(0.4, 0, 0.2, 1) 0ms",
                        },
                        "& .MuiIconButton-root": {
                            borderRadius: 100,
                        },
                        "& .MuiSwitch-switchBase": {
                            top: 3,
                            left: 0,
                            transform: "translateX(0)", // Thumb no início
                            "&.Mui-checked": {
                                transform: "translateX(30px)", // Ajuste o deslocamento para acompanhar o novo width
                            },
                        },
                        "& .MuiSwitch-switchBase.Mui-checked + .MuiSwitch-track": {
                            opacity: 0.3,
                        },
                    },
                    thumb: {
                        width: 25, // Tamanho do thumb
                        height: 25,
                        border: "1px solid " + colors.alpha.black[30],
                        boxShadow: "0px 9px 14px " + alpha(colors.alpha.black[100], 0.1) + ", 0px 2px 2px " + alpha(colors.alpha.black[100], 0.1),
                        backgroundColor:  colors.alpha.white[100],
                        backgroundRepeat: "no-repeat",
                        backgroundPosition: "center",

                        // Adicionando o SVG no estado não checado
                        "&:not(.Mui-checked)": {
                            backgroundImage: `url('data:image/svg+xml;utf8,<svg fill="none" viewBox="0 0 12 12" xmlns="http://www.w3.org/2000/svg"><path d="M4 8l2-2m0 0l2-2M6 6L4 4m2 2l2 2" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>')`,
                        },
                    },
                    track: {
                        backgroundColor: "#e4e7eb", //colors.alpha.black[5],
                        border: "1px solid " + colors.alpha.black[10],
                        boxShadow: "inset 0px 1px 1px " + colors.alpha.black[10],
                        opacity: 1,
                        width: "100%",
                    },
                    colorPrimary: {
                        "& .MuiSwitch-thumb": {
                            backgroundColor: colors.alpha.white[100],
                        },

                        // Aplicando o SVG quando o switch está marcado
                        "&.Mui-checked .MuiSwitch-thumb": {
                            backgroundColor: colors.primary.main,
                            backgroundImage: `url('data:image/svg+xml;utf8,<svg fill="white" viewBox="0 0 12 12" xmlns="http://www.w3.org/2000/svg"><path d="M3.707 5.293a1 1 0 00-1.414 1.414l1.414-1.414zM5 8l-.707.707a1 1 0 001.414 0L5 8zm4.707-3.293a1 1 0 00-1.414-1.414l1.414 1.414zm-7.414 2l2 2 1.414-1.414-2-2-1.414 1.414zm3.414 2l4-4-1.414-1.414-4 4 1.414 1.414z" stroke-width="1" /></svg>')`,
                        },
                    },
                },
            },

            MuiStepper: {
                styleOverrides: {
                    root: {
                        paddingTop: 20,
                        paddingBottom: 20,
                    },
                },
            },

            MuiStepIcon: {
                styleOverrides: {
                    root: {
                        "&.MuiStepIcon-completed": {
                            color: colors.success.main,
                        },
                        color: colors.alpha.black[70],
                    },
                },
            },

            MuiDataGrid: {
                styleOverrides: {
                    root: {
                        border: "none", // Removendo bordas visíveis
                        backgroundColor:  colors.alpha.white[50], // Fundo mais suave
                        color:  colors.alpha.black[90], // Texto de alto contraste
                    },
                    columnHeaders: {
                        backgroundColor:  colors.alpha.black[5], // Fundo suave
                        color:  colors.alpha.black[80], // Texto mais escuro para contraste
                        fontSize: 14,
                        fontWeight: "normal", // Tipografia mais leve
                        textTransform: "none", // Removendo uppercase para um estilo mais minimalista
                    },
                    cell: {
                        fontSize: 14,
                        color:  colors.alpha.black[90], // Texto mais escuro
                        border: "none",
                        //padding: "10px 16px", // Espaçamento suave nas células
                    },
                    row: {
                        "&:hover": {
                            backgroundColor:  alpha(colors.primary.lighter, 0.1), // Efeito hover mais leve
                        },
                        "&.Mui-selected": {
                            backgroundColor:  alpha(colors.primary.main, 0.2), // Cor de seleção mais suave
                            "&:hover": {
                                backgroundColor:  alpha(colors.primary.main, 0.3), // Hover na linha selecionada
                            },
                        },
                    },
                    footerContainer: {
                        backgroundColor:  colors.alpha.white[50], // Fundo suave no rodapé
                        borderTop: `1px solid ${ colors.alpha.black[10]}`, // Borda suave no topo do rodapé
                    },
                    toolbar: {
                        backgroundColor:  colors.alpha.white[50], // Fundo mais leve no toolbar
                        borderBottom: `1px solid ${colors.alpha.black[10]}`, // Borda sutil
                    },
                    pagination: {
                        color: colors.alpha.black[90], // Cor de paginação de alto contraste
                    },
                },
            },

            MuiDataGridColumnHeader: {
                styleOverrides: {
                    title: {
                        textTransform: "none", // Removendo capitalize para estilo mais leve
                        fontSize: 14, // Ajustando tipografia
                        fontWeight: "normal", // Peso mais suave
                    },
                },
            },

            MuiDataGridFooterContainer: {
                styleOverrides: {
                    root: {
                        padding: "12px 16px", // Espaçamento suave
                    },
                },
            },

            MuiInputBase: {
                defaultProps: {
                    size: "small",
                },
                styleOverrides: {
                    root: {
                        // backgroundColor: "white",
                        // borderRadius: "12px", // Bordas mais arredondadas
                        transition: "all 250ms ease-in-out",
                        "&:hover": {
                            borderColor: themeColors.primary, // Destaque ao hover
                        },
                        "&.Mui-focused": {
                            borderColor: themeColors.primary, // Cor da borda ao focar
                        },
                        "&.Mui-error": {
                            borderColor: themeColors.error, // Cor da borda ao focar
                        },
                    },
                },
            },
           
            MuiAutocomplete: {
                defaultProps: {
                    size: "small",
                },
                styleOverrides: {
                    root: {
                        "& .MuiInputLabel-root": {
                            // Label dentro do Select
                            top: 0, // Ajuste fino
                        },
                    },
                    option: {
                        padding: "8px 12px", // Espaçamento interno maior
                        transition: "background-color 200ms ease, transform 150ms ease",
                        '&[data-focus="true"]': {
                            backgroundColor: themeColors.primary, // Fundo no foco
                            color: themeColors.primary, // Texto no foco
                            boxShadow: `0 0 10px ${themeColors.primary}`, // Destaque com sombra
                        },
                        "&:hover": {
                            backgroundColor: themeColors.primaryAlt, // Fundo no hover
                            color: themeColors.primary, // Texto no hover
                        },
                        "&:first-of-type": {
                            borderTopLeftRadius: "12px", // Borda superior esquerda arredondada
                            borderTopRightRadius: "12px", // Borda superior direita arredondada
                        },
                        "&:last-of-type": {
                            borderBottomLeftRadius: "12px", // Borda inferior esquerda arredondada
                            borderBottomRightRadius: "12px", // Borda inferior direita arredondada
                        },
                    },
                    listbox: {
                        // borderRadius: "12px", // Bordas arredondadas
                        boxShadow: "0px 10px 20px rgba(0, 0, 0, 0.2)", // Sombra para destaque
                        padding: "2px", // Espaçamento interno
                        maxHeight: "200px", // Altura máxima
                        overflowY: "auto", // Rolagem para conteúdo longo
                        
                    },
                    popupIndicator: {
                        "&:hover": {
                            color: themeColors.primary,
                        },
                    },
                    clearIndicator: {
                        "&:hover": {
                            color: themeColors.primary,
                        },
                    },
                    noOptions: {
                        padding: "12px",
                        textAlign: "center",
                        fontSize: "0.875rem",
                    },
                },
            },
            MuiSelect: {
                styleOverrides: {
                    root: {
                        "& .MuiInputLabel-root": {
                            // Label dentro do Select
                            top: -7, // Ajuste fino
                        },
                    },
                },
            },

            MuiOutlinedInput: {
                styleOverrides: {
                    root: {
                        "& .MuiInputAdornment-positionEnd.MuiInputAdornment-outlined": {
                            paddingRight: 6,
                        },
                        "&:hover .MuiOutlinedInput-notchedOutline": {
                            borderColor: colors.primary.main,
                        },
                        "&.Mui-focused:hover .MuiOutlinedInput-notchedOutline": {
                            borderColor: colors.primary.main,
                        },
                        "& .MuiOutlinedInput-notchedOutline": {
                            borderColor:  colors.alpha.black[50],
                        },
                    },
                },
            },
            MuiInputLabel: {
                styleOverrides: {
                    root: {
                        // Label dentro do Select
                        top: -7, // Ajuste fino
                        color: themeColors.textSecondary,
                    },
                    shrink: {
                        top: "1px",
                    },
                },
            },
            MuiFilledInput: {
                styleOverrides: {
                    root: {
                        padding: "unset !important",

                        "& .MuiInputAdornment-positionEnd.MuiInputAdornment-outlined": {
                            paddingRight: 6,
                        },
                        "&:hover .MuiOutlinedInput-notchedOutline": {
                            borderColor: colors.alpha.black[50],
                        },
                        "&.Mui-focused:hover .MuiOutlinedInput-notchedOutline": {
                            borderColor: colors.primary.main,
                        },
                        "& .MuiOutlinedInput-notchedOutline": {
                            borderColor: colors.alpha.black[50],
                        },
                        "&::before, &::after": {
                            borderBottom: "none",
                        },
                        "&:hover:not(.Mui-disabled, .Mui-error):before": {
                            borderBottom: "none",
                        },
                        "&.Mui-focused:after": {
                            borderBottom: "none",
                        },
                    },
                },
            },
            MuiFormHelperText: {
                styleOverrides: {
                    root: {
                        textTransform: "none",
                    },
                },
            },
        },

        shadows: [
            "rgba(0, 0, 0, 0.08) 0px 2px 20px", // Original

            // Incrementos de opacidade, deslocamento e raio de desfoque
            "rgba(0, 0, 0, 0.1) 0px 3px 22px", // Variação 1
            "rgba(0, 0, 0, 0.12) 0px 4px 24px", // Variação 2
            "rgba(0, 0, 0, 0.14) 0px 5px 26px", // Variação 3
            "rgba(0, 0, 0, 0.16) 0px 6px 28px", // Variação 4
            "rgba(0, 0, 0, 0.18) 0px 7px 30px", // Variação 5
            "rgba(0, 0, 0, 0.2) 0px 8px 32px", // Variação 6
            "rgba(0, 0, 0, 0.22) 0px 9px 34px", // Variação 7
            "rgba(0, 0, 0, 0.24) 0px 10px 36px", // Variação 8
            "rgba(0, 0, 0, 0.26) 0px 1px 38px", // Variação 9
            "rgba(0, 0, 0, 0.28) 0px 12px 40px", // Variação 10
            "rgba(0, 0, 0, 0.3) 0px 13px 42px", // Variação 11

            // Incrementos de opacidade e ajustes menores nos deslocamentos e desfoque
            "rgba(0, 0, 0, 0.32) 1px 14px 44px", // Variação 12
            "rgba(0, 0, 0, 0.34) 2px 15px 46px", // Variação 13
            "rgba(0, 0, 0, 0.36) 3px 16px 48px", // Variação 14
            "rgba(0, 0, 0, 0.38) 4px 17px 50px", // Variação 15
            "rgba(0, 0, 0, 0.4) 5px 18px 52px", // Variação 16
            "rgba(0, 0, 0, 0.42) 6px 19px 54px", // Variação 17
            "rgba(0, 0, 0, 0.44) 7px 20px 56px", // Variação 18
            "rgba(0, 0, 0, 0.46) 8px 21px 58px", // Variação 19
            "rgba(0, 0, 0, 0.48) 9px 22px 60px", // Variação 20

            // Ajustes maiores nos deslocamentos, mantendo opacidade elevada
            "rgba(0, 0, 0, 0.5) 10px 23px 62px", // Variação 21
            "rgba(0, 0, 0, 0.52) 11px 24px 64px", // Variação 22
            "rgba(0, 0, 0, 0.54) 12px 25px 66px", // Variação 23
            "rgba(0, 0, 0, 0.56) 13px 26px 68px", // Variação 24
            "rgba(0, 0, 0, 0.58) 14px 27px 70px", // Variação 25
        ],
    });
};

export default getTheme;
