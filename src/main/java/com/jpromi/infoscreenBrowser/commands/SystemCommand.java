package com.jpromi.infoscreenBrowser.commands;

import com.jpromi.infoscreenBrowser.annotaions.BrowserClass;
import com.jpromi.infoscreenBrowser.annotaions.BrowserCommand;

@BrowserClass("system")
public class SystemCommand {

    @BrowserCommand("shutdown")
    public Void systemShutdown() {
        switch (getOS()) {
            case "windows":
                executeCommand("shutdown /s /t 0");
                break;
            case "mac":
                executeCommand("sudo shutdown -h now");
                break;
            case "linux":
                executeCommand("sudo shutdown -h now");
                break;
            default:
                throw new UnsupportedOperationException("Unsupported operating system: " + getOS());
        }
        return null;
    }

    @BrowserCommand("restart")
    public Void systemRestart() {
        switch (getOS()) {
            case "windows":
                executeCommand("shutdown /r /t 0");
                break;
            case "mac":
                executeCommand("sudo reboot");
                break;
            case "linux":
                executeCommand("sudo reboot");
                break;
            default:
                throw new UnsupportedOperationException("Unsupported operating system: " + getOS());
        }
        return null;
    }

    private String getOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "windows";
        } else if (os.contains("mac")) {
            return "mac";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            return "linux";
        } else {
            throw new UnsupportedOperationException("Unsupported operating system: " + os);
        }
    }

    private void executeCommand(String command) {
        try {
            new ProcessBuilder(command).inheritIO().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
