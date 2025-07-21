import { alpha, lighten, darken } from "@mui/material";

const themeColors = {
    primary: null,
    secondary: null,
    primaryAlt: null,
    error: "#F31260",
    warning: "#f5a524",
    success: "#17C964",
    info: "#0072F5",

    black: "#223354",
    white: "#ffffff",
    blue: "#006FEE",
    purple: "#7828C8",
    green: "#17C964",
    red: "#F31260",
    pink: "#FF4ECD",
    yellow: "#F5A524",
    cyan: "#7EE7FC",
    zinc: "#71717A",
    lightbackground: "#f2f5f9",
    textPrimary: "#000000",
    textSecondary: "#6E7278",
};

// Factory function to generate gradient sets dynamically
const generateDynamicGradients = (colors) => ({
    theme: `linear-gradient(135deg, ${colors.primary} 0%, ${colors.secondary} 100%)`,
});

// Static gradients, shadows, and alpha values (created once)
const staticGradients = {
    blue1: "linear-gradient(135deg, #6B73FF 0%, #000DFF 100%)",
    blue2: "linear-gradient(135deg, #ABDCFF 0%, #0396FF 100%)",
    blue3: "linear-gradient(135deg, #141E30 0%, #243B55 100%)",
    blue4: "linear-gradient(135deg, #2b5876 0%, #4e4376 100%)",
    blue5: "linear-gradient(135deg, #97ABFF 0%, #123597 100%)",
    orange1: "linear-gradient(135deg, #FCCF31 0%, #F55555 100%)",
    orange2: "linear-gradient(135deg, #FFD3A5 0%, #FD6585 100%)",
    orange3: "linear-gradient(135deg, #f6d365 0%, #fda085 100%)",
    purple1: "linear-gradient(135deg, #43CBFF 0%, #9708CC 100%)",
    purple3: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
    pink1: "linear-gradient(135deg, #F6CEEC 0%, #D939CD 100%)",
    pink2: "linear-gradient(135deg, #F761A1 0%, #8C1BAB 100%)",
    green1: "linear-gradient(135deg, #FFF720 0%, #3CD500 100%)",
    green2: "linear-gradient(135deg, #00b09b 0%, #96c93d 100%)",
    black1: "linear-gradient(135deg, #434343 0%, #000000 100%)",
    black2: "linear-gradient(135deg, #29323c 0%, #485563 100%)",
    blue6: "linear-gradient(135deg, #1e3c72 0%, #2a5298 100%)",
    blue7: "linear-gradient(135deg, #89f7fe 0%, #66a6ff 100%)",
    blue8: "linear-gradient(135deg, #0072ff 0%, #00c6ff 100%)",
    blue9: "linear-gradient(135deg, #2193b0 0%, #6dd5ed 100%)",
    blue10: "linear-gradient(135deg, #0052d4 0%, #4364f7 50%, #6fb1fc 100%)",
    orange4: "linear-gradient(135deg, #f2994a 0%, #f2c94c 100%)",
    orange5: "linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%)",
    orange6: "linear-gradient(135deg, #ff9966 0%, #ff5e62 100%)",
    orange7: "linear-gradient(135deg, #ff512f 0%, #f09819 100%)",
    orange8: "linear-gradient(135deg, #ffafbd 0%, #ffc3a0 100%)",
    purple4: "linear-gradient(135deg, #da22ff 0%, #9733ee 100%)",
    purple5: "linear-gradient(135deg, #9d50bb 0%, #6e48aa 100%)",
    purple6: "linear-gradient(135deg, #8e2de2 0%, #4a00e0 100%)",
    purple7: "linear-gradient(135deg, #a445b2 0%, #d41872 52%, #ff0066 100%)",
    purple8: "linear-gradient(135deg, #6a11cb 0%, #2575fc 100%)",
    pink3: "linear-gradient(135deg, #ff9a8b 0%, #ff6a88 55%, #ff99ac 100%)",
    pink4: "linear-gradient(135deg, #ee9ca7 0%, #ffdde1 100%)",
    pink5: "linear-gradient(135deg, #ff758c 0%, #ff7eb3 100%)",
    pink6: "linear-gradient(135deg, #fbc2eb 0%, #a6c1ee 100%)",
    pink7: "linear-gradient(135deg, #ff0844 0%, #ffb199 100%)",
    green3: "linear-gradient(135deg, #00f260 0%, #0575e6 100%)",
    green4: "linear-gradient(135deg, #11998e 0%, #38ef7d 100%)",
    green5: "linear-gradient(135deg, #2af598 0%, #009efd 100%)",
    green6: "linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)",
    green7: "linear-gradient(135deg, #56ab2f 0%, #a8e063 100%)",
    black3: "linear-gradient(135deg, #232526 0%, #414345 100%)",
    black4: "linear-gradient(135deg, #3a3a3a 0%, #0d0d0d 100%)",
    black5: "linear-gradient(135deg, #485563 0%, #29323c 100%)",
    black6: "linear-gradient(135deg, #232526 0%, #414345 100%)",
    black7: "linear-gradient(135deg, #000000 0%, #434343 100%)",
};

const staticShadows = [
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
];

const staticAlpha = (() => {
    const alphaColors = ["white", "black", "blue", "purple", "green", "red", "pink", "yellow", "cyan", "zinc"];
    return alphaColors.reduce((acc, color) => {
        acc[color] = generateAlpha(themeColors[color]);
        return acc;
    }, {});
})();

// Factory function to generate alpha sets
function generateAlpha(color) {
    return {
        5: alpha(color, 0.02),
        10: alpha(color, 0.1),
        30: alpha(color, 0.3),
        50: alpha(color, 0.5),
        70: alpha(color, 0.7),
        100: color,
    };
}

const staticVariants = (() => {
    const baseVariants = ["success", "warning", "error", "info"];
    return baseVariants.reduce((variants, key) => {
        variants[key] = createColorVariants(themeColors[key]);
        return variants;
    }, {});
})();

function createColorVariants(color) {
    return {
        lighter: lighten(color, 0.85),
        light: lighten(color, 0.3),
        main: color,
        dark: darken(color, 0.2),
    };
}

// Function to update dynamic parts of the colors
const updateDynamicColors = (colors) => ({
    gradients: {
        ...staticGradients,
        ...generateDynamicGradients(colors),
    },
    primary: createColorVariants(colors.primary),
    secondary: createColorVariants(colors.secondary),
});

// Store the dynamic colors state
let dynamicColors;

const setThemeColors = (primaryColor, secondaryColor) => {
    themeColors.primary = primaryColor;
    themeColors.secondary = secondaryColor;
    themeColors.primaryAlt = alpha(darken(primaryColor, 0.8), 0.1);

    // Update only the dynamic parts
    dynamicColors = updateDynamicColors(themeColors);
};

const getColors = () => {
    return {
        ...dynamicColors,
        shadows: staticShadows,
        alpha: staticAlpha,
        ...staticVariants,
    };
};

export { themeColors, setThemeColors, getColors };
