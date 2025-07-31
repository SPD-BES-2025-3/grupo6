// main.js
const { app, BrowserWindow } = require("electron");

function createWindow() {
    const win = new BrowserWindow({
        width: 1280,
        height: 720,
    });
    win.maximize();
    win.show();
    win.loadURL("http://localhost:4200");
    win.removeMenu();
}

app.whenReady().then(() => {
    createWindow();
});
