import React from "react";
import { Avatar, useTheme } from "@mui/material";
import Icon from "@/helpers/iconHelper";
export default function ResourceAvatar({ recurso, size = 25, sx, color }) {
    const theme = useTheme();
    const backgroundMap = {
        Home: theme.colors.gradients.blue1,
        User: theme.colors.gradients.purple1,
        Book: theme.colors.gradients.purple3,
        Registry: theme.colors.gradients.pink3,
    };
    const background = color || backgroundMap[recurso] || theme.colors.gradients.defaultColor;

    
    return (
        <Avatar sx={{background: background, ...sx }}>
            <Icon name={recurso} size={size} />
        </Avatar>
    );
}
