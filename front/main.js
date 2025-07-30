// main.js
const { app, BrowserWindow } = require("electron");

function createWindow() {
    const win = new BrowserWindow({
        width: 800,
        height: 600,
    });
    win.maximize();
    win.show();
    win.loadURL("http://localhost:4200");
    win.removeMenu();
}

app.whenReady().then(() => {
    createWindow();
});
