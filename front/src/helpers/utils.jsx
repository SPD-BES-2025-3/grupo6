export function sortData(v1, v2) {
    const parseDateFlexible = (dateStr) => {
        if (!dateStr) return new Date(0);

        // Verifica se tem hora
        const hasTime = / - \d{2}:\d{2}:\d{2}$/.test(dateStr);

        if (hasTime) {
            const [datePart, timePart] = dateStr.split(" - ");
            const [day, month, year] = datePart.split("/").map(Number);
            const [hour, minute, second] = timePart.split(":").map(Number);
            return new Date(year, month - 1, day, hour, minute, second);
        } else if (/^\d{2}\/\d{2}\/\d{4}$/.test(dateStr)) {
            const [day, month, year] = dateStr.split("/").map(Number);
            return new Date(year, month - 1, day);
        }

        return new Date(0); // formato inesperado
    };

    const date1 = parseDateFlexible(v1);
    const date2 = parseDateFlexible(v2);

    return date1.getTime() - date2.getTime();
}
