import { create } from "zustand";

const useOurTheme = create((set) => ({
    theme: "light",
    setTheme: (newTheme) => set({ theme: newTheme }),
    primary: "#0072F5",
    setPrimary: (color) => set({ primary: color }),
    secondary: "#7828C8",
    setSecondary: (color) => set({ secondary: color }),
}));

export default useOurTheme;
