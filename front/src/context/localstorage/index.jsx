import { create } from "zustand";

const useLocalStorage = create((set) => ({
    setLocalStorage: (name, data) => {
        const key = "biblioteca-spd" + name;
        const value = typeof data === "object" ? JSON.stringify(data) : data;
        localStorage.setItem(key, value);
    },

    getLocalStorage: (name) => {
        const key = "biblioteca-spd" + name;
        const item = localStorage.getItem(key);
        // Tenta converter para JSON apenas se parecer com um objeto JSON
        try {
            return item && (item.startsWith("{") || item.startsWith("[")) ? JSON.parse(item) : item;
        } catch (error) {
            console.error("Erro ao parsear JSON:", error);
            return item;
        }
    },

    removeLocalStorage: (name) => {
        localStorage.removeItem(name);
    },
}));

export default useLocalStorage;
